import React from "react";
import { render, fireEvent, screen } from "@testing-library/react";
import WorkoutCalendar from "../reactComponents/elements/calendar";
import "@testing-library/jest-dom";
import { BrowserRouter } from "react-router-dom";

describe("WorkoutCalendar", () => {
  test("displays workout events on the calendar and opens details view on click26", async () => {
    // Set up a mocked response for fetch()
    // @ts-ignore
    global.fetch = vitest.fn().mockImplementation(() => {
      return Promise.resolve({
        json: () =>
          Promise.resolve({
            days: [
              {
                date: "05-16-2023",
                day: "Friday",
                numberOfWorkouts: 1,
                type: "jogging",
                workouts: [
                  {
                    time: 45,
                    workout: "Jogging",
                    title: "Workout 1 for day 1",
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
    render(
      <BrowserRouter>
        <WorkoutCalendar />
      </BrowserRouter>
    );
    setTimeout(() => {
      // your test code here
    }, 2000);
    await screen.findByRole("application");

    // Verify that the calendar displays the workout event
    const workoutEvent = await screen.getByText("16");
    expect(workoutEvent).toBeInTheDocument();

    // Click on the workout event to open the details view
    fireEvent.click(workoutEvent);
  });
});
