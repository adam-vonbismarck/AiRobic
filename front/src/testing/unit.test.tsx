import "@testing-library/jest-dom";
import React from "react";

import { extractData, getSearchOverlayMock, MockData } from "../overlays";

describe("extractData function", () => {
  test("returns a valid FeatureCollection if overlay data is present", async () => {
    const mockResponse = getSearchOverlayMock("Providence");
    const featureCollection = await extractData(mockResponse);
    expect(featureCollection).toBeDefined();
    if (featureCollection !== undefined) {
      expect(featureCollection.features.length).toBeGreaterThan(0);
      expect(featureCollection.features[0]?.properties?.state).toEqual("NJ");
      expect(featureCollection.features[0]?.properties?.city).toEqual(
        "Union Co."
      );
      expect(featureCollection.features[0]?.properties?.holc_grade).toEqual(
        "B"
      );
    }
  });

  test("throws an error if response is undefined", async () => {
    await expect(extractData(Promise.resolve({
      data: undefined,
      result: "success",
      err_msg: "no error"
    }))).rejects.toEqual(
        "Response was found, but did not contain a valid set of features to overlay."
    );
  });
});

describe("getSearchOverlayMock function", () => {
  test("returns a valid OverlayResponse for a known location", async () => {
    const mockResponse = await getSearchOverlayMock("Providence");
    expect(mockResponse).toBeDefined();
  });

  test("returns undefined for an unknown location", async () => {
    const mockResponse = await getSearchOverlayMock("unknown");
    expect(mockResponse).toBeUndefined();
  });
});

describe("MockData object", () => {
  test("contains valid mock data for known locations", () => {
    expect(MockData.get("Providence")).toBeDefined();
  });

  test("contains undefined for unknown locations", () => {
    expect(MockData.get("unknown")).toBeUndefined();
  });
});
