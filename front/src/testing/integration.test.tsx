import "@testing-library/jest-dom";
import { fireEvent, render, screen } from "@testing-library/react";
import App from "../reactComponents/App";
import React from "react";
import { getSearchOverlayMock } from "../overlays";
import { act } from "react-dom/test-utils";

beforeEach(() => {
  act(() => {
    render(
      <App
        getMainOverlayResponse={() => getSearchOverlayMock("alldata")}
        getSearchOverlayResponse={getSearchOverlayMock}
      />
    );
  });
});

test("header", async () => {
  expect(screen.getByLabelText("Command Input Box")).toBeInTheDocument();
});

test("input box focus and text input", async () => {
  const inputBox = screen.getByLabelText(
    "Command Input Box"
  ) as HTMLInputElement;

  // Ensure that focus is not on the input box initially
  expect(document.activeElement).not.toBe(inputBox);

  // Trigger "/" key press to switch focus to the input box
  fireEvent.keyDown(document, { key: "/" });
  expect(document.activeElement).toBe(inputBox);

  // Enter text into the input box
  const searchText = "Test search text";
  fireEvent.change(inputBox, { target: { value: searchText } });
  expect(inputBox.value).toBe(searchText);
});
