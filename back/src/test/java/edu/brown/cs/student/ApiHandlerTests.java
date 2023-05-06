package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static spark.Spark.after;

import com.squareup.moshi.Json;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/** This class tests the API handlers */
public class ApiHandlerTests {

    private static Moshi moshi;

    /** Sets up moshi before all tests. */
    @BeforeAll
    public static void setupBeforeEverything() {
        moshi = new Moshi.Builder().build();
    }

    /**
     * This method requests a URL and returns a response which is an object of type T.
     *
     * @param apiCall The URL to request.
     * @param tClass The class of the object to be instantiated.
     * @param <T> The type of the object to be instantiated.
     * @return An object of type T
     * @throws IOException If the URL is invalid.
     */
    private <T> T requestAndInstantiate(String apiCall, Class<T> tClass) throws IOException {
        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
        clientConnection.connect();
        assertEquals(200, clientConnection.getResponseCode());

        try (Buffer buf = new Buffer().readFrom(clientConnection.getInputStream())) {
            T response = moshi.adapter(tClass).fromJson(buf);
            clientConnection.disconnect();
            return response;
        }
    }

    /** This method sets up the server before each test. */
    @BeforeEach
    public void setup() {
        after(
                (request, response) -> {
                    response.header("Access-Control-Allow-Origin", "*");
                    response.header("Access-Control-Allow-Methods", "*");
                    response.header("Content-Type", "application/json");
                });

        // Setting up the handler for the GET /order endpoint
        /*CsvLoader csvContainer = new CsvLoader();

        Spark.get("loadcsv", new CsvLoadHandler(csvContainer));
        Spark.get("searchcsv", new CsvSearchHandler(csvContainer));
        Spark.get("viewcsv", new CsvViewHandler(csvContainer));
        Spark.init();*/
        Spark.awaitInitialization(); // don't continue until the server is listening
    }

    /** This method tears down the server after each test. */
    @AfterEach
    public void teardown() {
        Spark.unmap("loadcsv");
        Spark.unmap("searchcsv");
        Spark.unmap("viewcsv");
        Spark.awaitStop();
    }

    /**
     * This method tests loading a csv with a valid file.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testLoadValidFile() throws IOException {
        LoadResponse response =
                this.requestAndInstantiate("loadcsv?filepath=stars/ten-star.csv", LoadResponse.class);
        assertNotNull(response);
        assertEquals("success", response.result);
        assertEquals(
                "CSV from filepath /Users/adamvonbismarck/Desktop/"
                        + "cs320/sprint-3-avonbism-jsilva13/data/stars/ten-star.csv loaded.",
                response.message);
    }

    /**
     * This method tests loading a csv with an invalid file.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testLoadInvalidFile() throws IOException {
        LoadResponse response =
                this.requestAndInstantiate("loadcsv?filepath=stars/eleven-star.csv", LoadResponse.class);
        assertNotNull(response);
        assertEquals("error_datasource", response.result);
        assertEquals(
                "/Users/adamvonbismarck/Desktop/cs320/sprint-3-avonbism-jsilva13/"
                        + "data/stars/eleven-star.csv (No such file or directory)",
                response.message);
    }

    /**
     * This method tests loading a csv with no file.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testLoadNoFile() throws IOException {
        LoadResponse response = this.requestAndInstantiate("loadcsv", LoadResponse.class);
        assertNotNull(response);
        assertEquals("error_bad_request", response.result);
        assertEquals("No filepath provided.", response.message);
    }

    /**
     * This method tests searching a csv with a valid file and no header and index.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testSearchValidFileNoHeaderNoIndex() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        SearchResponse response =
                this.requestAndInstantiate("searchcsv?target=Sol", SearchResponse.class);
        assertNotNull(response);
        assertEquals("success", response.result);
        assertEquals("[[0, Sol, 0, 0, 0]]", response.searchResults.toString());
        assertEquals("Sol", response.query);
        assertFalse(response.hasHeader);
    }

    /**
     * This method tests searching a csv with a valid file and no header but an index.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testSearchValidFileNoHeaderIndex() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        SearchResponseIndex response =
                this.requestAndInstantiate("searchcsv?target=Sol&index=1", SearchResponseIndex.class);
        assertNotNull(response);
        assertEquals("success", response.result);
        assertEquals("[[0, Sol, 0, 0, 0]]", response.searchResults.toString());
        assertEquals("Sol", response.query);
        assertFalse(response.hasHeader);
        assertEquals("1", response.columnIndex);

        response =
                this.requestAndInstantiate("searchcsv?target=Sol&index=0", SearchResponseIndex.class);
        assertEquals("error_bad_request", response.result);
        assertEquals("Sol", response.query);
        assertEquals("No results found.", response.message);
        assertFalse(response.hasHeader);
        assertEquals("0", response.columnIndex);
    }

    /**
     * This method tests searching a csv with a valid file and a header but no index.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testSearchValidFileHeaderNoIndex() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        SearchResponse response =
                this.requestAndInstantiate("searchcsv?target=Sol&header=true", SearchResponse.class);
        assertNotNull(response);
        assertEquals("success", response.result);
        assertEquals("[[0, Sol, 0, 0, 0]]", response.searchResults.toString());
        assertEquals("Sol", response.query);
        assertTrue(response.hasHeader);

        response =
                this.requestAndInstantiate("searchcsv?target=Sol&header=false", SearchResponse.class);
        assertEquals("success", response.result);
        assertEquals("[[0, Sol, 0, 0, 0]]", response.searchResults.toString());
        assertEquals("Sol", response.query);
        assertFalse(response.hasHeader);
    }

    /**
     * This method tests searching a csv with a valid file and a header and index.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testSearchValidFileHeaderIndex() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        SearchResponseIndex response =
                this.requestAndInstantiate(
                        "searchcsv?target=Sol&index=ProperName&header=true", SearchResponseIndex.class);
        assertNotNull(response);
        assertEquals("success", response.result);
        assertEquals("[[0, Sol, 0, 0, 0]]", response.searchResults.toString());
        assertEquals("Sol", response.query);
        assertTrue(response.hasHeader);
        assertEquals("ProperName", response.columnIndex);
    }

    /**
     * This method tests searching a csv with an invalid file.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testSearchInvalidFile() throws IOException {
        SearchResponseError response =
                this.requestAndInstantiate("searchcsv?target=Sol", SearchResponseError.class);
        assertNotNull(response);
        assertEquals("error_bad_request", response.result);
        assertEquals("No CSV loaded.", response.message);
    }

    /**
     * This method tests searching a csv with no query.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testNoQuery() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        SearchResponseError response =
                this.requestAndInstantiate("searchcsv", SearchResponseError.class);
        assertNotNull(response);
        assertEquals("error_bad_request", response.result);
        assertEquals("No query provided.", response.message);
    }

    /**
     * This method tests searching a csv with an invalid index.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testSearchInvalidIndex() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        SearchResponseError response =
                this.requestAndInstantiate("searchcsv?target=Sol&index=9", SearchResponseError.class);
        assertNotNull(response);
        assertEquals("error_bad_request", response.result);
        assertEquals(
                "Index given was not in the bounds of the CSV. " + "Possible values are: 0 - 4",
                response.message);
    }

    /**
     * This method tests searching a csv with an invalid header.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testSearchInvalidHeader() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        SearchResponseError response =
                this.requestAndInstantiate(
                        "searchcsv?target=Sol&index=9&header=true", SearchResponseError.class);
        assertNotNull(response);
        assertEquals("error_bad_request", response.result);
        assertEquals(
                "Index given was not found in the header row."
                        + " Possible values are: [StarID, ProperName, X, Y, Z]",
                response.message);
    }

    /**
     * This method tests searching a csv with no results.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testSearchNoResults() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        SearchResponseError response =
                this.requestAndInstantiate("searchcsv?target=Solstice", SearchResponseError.class);
        assertNotNull(response);
        assertEquals("error_bad_request", response.result);
        assertEquals("No results found.", response.message);
    }

    /**
     * This method tests viewing a csv with a valid file.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testViewNormal() throws IOException {
        this.requestAndInstantiate("loadcsv?filepath=stars/stardata.csv", LoadResponse.class);
        ViewResponse response = this.requestAndInstantiate("viewcsv", ViewResponse.class);
        assertNotNull(response);
        assertEquals("success", response.result);
        assertEquals("[8, Eura, 174.01562, 0.08288, 84.44669]", response.viewResults.get(9).toString());
    }

    /**
     * This method tests viewing a csv with no file loaded.
     *
     * @throws IOException If the file is invalid.
     */
    @Test
    public void testViewNoFile() throws IOException {
        ViewResponseError response = this.requestAndInstantiate("viewcsv", ViewResponseError.class);
        assertNotNull(response);
        assertEquals("error_bad_request", response.result);
        assertEquals("No CSV loaded.", response.message);
    }

    /**
     * Json record for the load response.
     *
     * @param result the result returned by the server
     * @param message the message returned by the server
     */
    public record LoadResponse(
            @Json(name = "result") String result, @Json(name = "message") String message) {}

    /**
     * Json record for the server response.
     *
     * @param result the result returned by the server
     * @param message the message returned by the server
     * @param hasHeader whether the csv has a header
     * @param query the query sent to the server
     * @param searchResults the results returned by the server
     */
    public record SearchResponse(
            @Json(name = "result") String result,
            @Json(name = "message") String message,
            @Json(name = "has header") boolean hasHeader,
            @Json(name = "query") String query,
            @Json(name = "search") List<List<String>> searchResults) {}

    /**
     * Json record for the server response when searching by index.
     *
     * @param result the result returned by the server
     * @param message the message returned by the server
     * @param hasHeader whether the csv has a header
     * @param columnIndex the index of the column
     * @param query the query sent to the server
     * @param searchResults the results returned by the server
     */
    public record SearchResponseIndex(
            @Json(name = "result") String result,
            @Json(name = "message") String message,
            @Json(name = "has header") boolean hasHeader,
            @Json(name = "index") Object columnIndex,
            @Json(name = "query") String query,
            @Json(name = "search") List<List<String>> searchResults) {}

    /**
     * Json record for the server response when there is an error searching.
     *
     * @param result the result returned by the server
     * @param message the message returned by the server
     */
    public record SearchResponseError(
            @Json(name = "result") String result, @Json(name = "message") String message) {}

    /**
     * Json record for the server response when viewing a csv.
     *
     * @param result the result returned by the server
     * @param viewResults the csv returned by the server
     */
    public record ViewResponse(
            @Json(name = "result") String result, @Json(name = "csv") List<List<String>> viewResults) {}

    /**
     * Json record for view when there is an error.
     *
     * @param result the result returned by the server
     * @param message the message returned by the server
     */
    public record ViewResponseError(
            @Json(name = "result") String result, @Json(name = "message") String message) {}
}
