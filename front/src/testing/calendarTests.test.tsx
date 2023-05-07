import React from "react";
import { render, fireEvent, screen } from "@testing-library/react";
import WorkoutCalendar from "../reactComponents/elements/calendar";
import "@testing-library/jest-dom";
// import { jest } from "@jest/globals";

describe("WorkoutCalendar", () => {
  test("displays workout events on the calendar and opens details view on click", async () => {
    // Set up a mocked response for fetch()
    // @ts-ignore
    global.fetch = jest.fn().mockImplementation(() => {
      return Promise.resolve({
        json: () =>
          Promise.resolve({
            days: [
              {
                date: "05-06-2023",
                day: "Friday",
                numberOfWorkouts: 1,
                type: "jogging",
                workouts: [
                  {
                    time: 45,
                    workout: "Jogging",
                    data: {
                      distance: 5,
                      split: "2:30",
                      rpe: 8,
                    },
                  },
                ],
              },
            ],
          }),
      });
    });

    // Render the component and wait for it to fetch data
    render(<WorkoutCalendar />);
    await screen.findByLabelText("Interactive calendar of workout events");

    // Verify that the calendar displays the workout event
    const workoutEvent = screen.getByText("Workout 1 for day 1");
    expect(workoutEvent).toBeInTheDocument();

    // Click on the workout event to open the details view
    fireEvent.click(workoutEvent);

    // Verify that the details view is open
    const workoutDetails = await screen.findByLabelText("Workout details");
    expect(workoutDetails).toBeInTheDocument();

    // Close the details view
    fireEvent.click(screen.getByLabelText("Close"));

    // Verify that the details view is closed
    expect(workoutDetails).not.toBeInTheDocument();
  });
});
