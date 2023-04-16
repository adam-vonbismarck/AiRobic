import { FeatureCollection } from "geojson";
import { FillLayer, LineLayer } from "react-map-gl";
import {
  AllDataMock,
  CapeCodMock,
  MontroseMock,
  NoDataMock,
  ProvidenceMock,
} from "./testing/MockCityData";

/*
This map is used to store mock data for testing purposes.
 */
export const MockData = new Map<string, OverlayResponse>([
  ["Providence", ProvidenceMock],
  ["CapeCod", CapeCodMock],
  ["Montrose", MontroseMock],
  ["nodata", NoDataMock],
  ["alldata", AllDataMock],
]);

/**
 * Adds a mock response to the map of mock data.
 *
 * @param key - the key to associate with the mock response
 * @param response - the mock response to add to the map
 */
export function addMock(key: string, response: OverlayResponse) {
  MockData.set(key, response);
}

/**
 * This interface defines the expected response from our backend server (all expected fields).
 */
export interface OverlayResponse {
  data: FeatureCollection | undefined;
  result: string;
  err_msg: string;
}

/**
 * Checks if a potential json item is a FeatureCollection (correct form of GeoJson data)
 *
 * @param json - the json item to check
 * @returns a boolean indicating whether the json is of the correct FeatureCollection type
 */
function isFeatureCollection(json: any): json is FeatureCollection {
  return json.type === "FeatureCollection";
}

/**
 * Checks if a JSON response is of the correct OverlayResponse format.
 *
 * @param rjson - the JSON response to check
 * @returns a boolean indicating whether the response is of the correct format
 */
function isOverlayResponse(rjson: any): rjson is OverlayResponse {
  if (!("result" in rjson)) return false;
  if (!("err_msg" in rjson)) return false;
  return "data" in rjson;
}

/**
 * Retrieves the main overlay from the server.
 *
 * @returns a promise that resolves to the main overlay or rejects with an error
 */
export function getMainOverlay(): Promise<OverlayResponse> {
  return fetch("http://localhost:3235/redline")
    .then((response: Response) => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error(`HTTP error: ${response.status}`);
      }
    })
    .then((responseObject: any) => {
      if (isOverlayResponse(responseObject)) {
        return responseObject;
      } else {
        throw new Error(`Invalid response object: ${responseObject}`);
      }
    });
}

/**
 * Retrieves the mock response associated with a specified keyword.
 *
 * @param keyword - the keyword to use for retrieving the mock response
 * @returns a promise that resolves to the mock response or undefined
 */
export function getSearchOverlayMock(
  keyword: string
): Promise<OverlayResponse | undefined> {
  return Promise.resolve(MockData.get(keyword));
}

/**
 * Retrieves a searched overlay from the server.
 *
 * @param keyword - the keyword to search for
 * @returns a promise that resolves to the searched overlay or rejects with an error
 */
export function getSearchedOverlay(keyword: string): Promise<OverlayResponse> {
  return fetch(`http://localhost:3235/redlinesearch?key=${keyword}`)
    .then((response: Response) => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error(`HTTP error: ${response.status}`);
      }
    })
    .then((responseObject: any) => {
      if (isOverlayResponse(responseObject)) {
        return responseObject;
      } else {
        throw new Error(`Invalid response object: ${responseObject}`);
      }
    });
}

/**
 * Extracts the FeatureCollection from an OverlayResponse.
 *
 * @param response - the promise that resolves to an OverlayResponse
 * @returns a promise that resolves to the FeatureCollection or undefined
 */
export function extractData(
  response: Promise<OverlayResponse | undefined>
): Promise<FeatureCollection | undefined> {
  return response.then((overlay: OverlayResponse | undefined) => {
    if (overlay == undefined) {
      return Promise.reject("No response was found.");
    }
    if (overlay.data == undefined || !isFeatureCollection(overlay.data)) {
      return Promise.reject(
        "Response was found, but did not contain a valid set of features to overlay."
      );
    }
    return overlay.data;
  });
}

/**
 * Styling information for the redlining layer example (overlays by HOLC grade)
 */
const propertyName = "holc_grade";
export const geoLayer: FillLayer = {
  id: "geo_data",
  type: "fill",
  paint: {
    "fill-color": [
      "match",
      ["get", propertyName],
      "A",
      "#5bcc04",
      "B",
      "#04b8cc",
      "C",
      "#e9ed0e",
      "D",
      "#d11d1d",
      "#ccc",
    ],
    "fill-opacity": 0.2,
  },
};

/**
 * Styling information for a searched region of the redlining data.
 */
export const geoSearchLayer: LineLayer = {
  id: "geo_search_data",
  type: "line",
  paint: {
    "line-color": "#c5cc04",
    "line-opacity": 0.9,
    "line-width": 2,
  },
};
