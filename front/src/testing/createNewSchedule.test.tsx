import {
  fireEvent,
  getByTestId,
  render,
  screen,
  waitFor,
} from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import NewSchedule from "../reactComponents/pages/createNewSchedule";
import "@testing-library/jest-dom";
import { BrowserRouter } from "react-router-dom";

describe("NewSchedule", () => {
  it("renders the form elements", () => {
    render(
      <BrowserRouter>
        <NewSchedule />
      </BrowserRouter>
    );
    expect(
      screen.getByLabelText("Logged in menu container")
    ).toBeInTheDocument();
    expect(screen.getByLabelText("Workout plan form")).toBeInTheDocument();
    expect(
      screen.getByLabelText("Input field to set the number of hours per week")
    ).toBeInTheDocument();
    expect(screen.getByLabelText("Sport dropdown field")).toBeInTheDocument();
    expect(screen.getByLabelText("Goal dropdown field")).toBeInTheDocument();
    expect(screen.getByLabelText("Start date input field")).toBeInTheDocument();
    expect(screen.getByLabelText("End date input field")).toBeInTheDocument();
    expect(screen.getByLabelText("Submit button row")).toBeInTheDocument();
  });

  it("submits the form when the button is clicked", async () => {
    const { getByTestId } = render(
      <BrowserRouter>
        <NewSchedule />
      </BrowserRouter>
    );
    await userEvent.type(
      screen.getByLabelText("Input field to set the number of hours per week"),
      "10"
    );

    await userEvent.type(
      screen.getByLabelText(
        "Input field to set the start date for the workout plan"
      ),
      "2023-05-10"
    );
    await userEvent.type(
      screen.getByLabelText(
        "Input field to set the end date for the workout plan"
      ),
      "2023-05-16"
    );
    await userEvent.click(
      screen.getByRole("button", { name: "Get Workout Plan" })
    );
  });
});
