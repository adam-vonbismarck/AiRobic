import {
  fireEvent,
  getByTestId,
  render,
  screen,
  waitFor,
} from "@testing-library/react";
import WorkoutDisplay from "../reactComponents/pages/workoutDisplay";
import "@testing-library/jest-dom";
import { vitest } from "vitest";
import { BrowserRouter } from "react-router-dom";
import WorkoutCalendar from "../reactComponents/elements/calendar";
import { RenderWorkoutDetails } from "/Users/adamvonbismarck/Desktop/cs320/cs32-Final-Project/front/src/reactComponents/elements/workoutDetails";

test("renders the LoggedInMenu component", () => {
  const { getByLabelText } = render(
    <BrowserRouter>
      <WorkoutDisplay />
    </BrowserRouter>
  );
  const loggedInMenu = getByLabelText("Logged-in user navigation menu");
  expect(loggedInMenu).toBeInTheDocument();
});

test("renders the WorkoutCalendar component", () => {
  const { getByRole } = render(
    <BrowserRouter>
      <WorkoutDisplay />
    </BrowserRouter>
  );
  const workoutCalendar = getByRole("application", {
    name: "Calendar of workout events",
  });
  expect(workoutCalendar).toBeInTheDocument();
});

test("fetches workouts and creates events array", async () => {
  const mockResponse = new Response(
    JSON.stringify({
      days: [
        {
          date: "05-07-2023",
          workouts: [
            {
              title: "Workout 1",
              data: {
                rpe: 7,
                distance: 5,
                split: "1:30.3",
              },
              time: "30 mins",
              workout: "Some workout",
            },
          ],
        },
      ],
    })
  );

  globalThis.fetch = vitest.fn(() => Promise.resolve(mockResponse));

  render(
    <BrowserRouter>
      <WorkoutDisplay />
    </BrowserRouter>
  );

  await waitFor(() => {
    expect(globalThis.fetch).toHaveBeenCalledTimes(1);
    expect(globalThis.fetch).toHaveBeenCalledWith(
      "https://cs32airobic-default-rtdb.firebaseio.com/users/null/schedule.json"
    );
  });

  const eventTitle = await screen.getByText("Workout 1");
  expect(eventTitle).toBeInTheDocument();
});

test("fetches workouts and displays workout details", async () => {
  const workouts = [
    {
      title: "Workout 1",
      data: {
        rpe: 7,
        distance: 5,
        split: "1:30.3",
      },
      time: "30 mins",
      workout: "Some workout",
    },
  ];

  const mockResponse = new Response(
    JSON.stringify({
      days: [{ date: "05-15-2023", workouts }],
    })
  );

  const originalFetch = globalThis.fetch;
  globalThis.fetch = vitest.fn(() => Promise.resolve(mockResponse));

  render(
    <BrowserRouter>
      <WorkoutCalendar />
    </BrowserRouter>
  );

  await waitFor(() => {
    expect(globalThis.fetch).toHaveBeenCalledTimes(1);
  });

  const dateCell = screen.getByLabelText("15");
  fireEvent.click(dateCell);

  // const popup = await screen.findByLabelText("Workout 1");
  // expect(popup).toBeInTheDocument();

  const workoutTitle = screen.getByText("Workout 1");
  expect(workoutTitle).toBeInTheDocument();

  globalThis.fetch = originalFetch;
});

it("renders correctly with no workouts", () => {
  const props = {
    selectedDate: "2023-05-07",
    workoutDetails: [],
    setWorkoutDetails: vitest.fn(),
    workouts: [],
    setWorkouts: vitest.fn(),
    closeFullscreen: vitest.fn(),
    splitError: false,
    setSplitError: vitest.fn(),
    distanceError: false,
    setDistanceError: vitest.fn(),
    errorText: "",
    setErrorText: vitest.fn(),
  };
  const { getByText } = render(
    <BrowserRouter>
      <RenderWorkoutDetails {...props} />
    </BrowserRouter>
  );
  expect(getByText("No workouts set for this date.")).toBeInTheDocument();
});

it("renders correctly with workouts", () => {
  const props = {
    selectedDate: "2023-05-07",
    workoutDetails: [
      {
        date: "2023-05-07",
        dayNumber: 1,
        workoutsNumber: 1,
        title: "Workout 1",
        time: 30,
        workout: "Some workout",
        distance: 2000,
        split: "1:30.0",
        RPE: 5,
      },
      {
        date: "2023-05-07",
        dayNumber: 1,
        workoutsNumber: 2,
        title: "Workout 2",
        time: 5,
        workout: "Another workout",
        distance: 3000,
        split: "2:00.0",
        RPE: 8,
      },
    ],
    setWorkoutDetails: vitest.fn(),
    workouts: [],
    setWorkouts: vitest.fn(),
    closeFullscreen: vitest.fn(),
    splitError: false,
    setSplitError: vitest.fn(),
    distanceError: false,
    setDistanceError: vitest.fn(),
    errorText: "",
    setErrorText: vitest.fn(),
  };
  const { getByText } = render(
    <BrowserRouter>
      <RenderWorkoutDetails {...props} />
    </BrowserRouter>
  );
  expect(getByText("Workouts for Sunday, 7th May 2023")).toBeInTheDocument();
  expect(getByText("Workout 1")).toBeInTheDocument();
  expect(getByText("Workout 2")).toBeInTheDocument();
});

it("calls the closeFullscreen function when close button is clicked", () => {
  const closeFullscreen = vitest.fn();
  const props = {
    selectedDate: "2023-05-07",
    workoutDetails: [],
    setWorkoutDetails: vitest.fn(),
    workouts: [],
    setWorkouts: vitest.fn(),
    closeFullscreen,
    splitError: false,
    setSplitError: vitest.fn(),
    distanceError: false,
    setDistanceError: vitest.fn(),
    errorText: "",
    setErrorText: vitest.fn(),
  };
  const { getByTestId } = render(
    <BrowserRouter>
      <RenderWorkoutDetails {...props} />
    </BrowserRouter>
  );
  const closeButton = getByTestId("CloseIcon");
  fireEvent.click(closeButton);
  expect(closeFullscreen).toHaveBeenCalled();
});

test("save button calls correct functions", () => {
  const mockSetWorkoutDetails = vitest.fn();
  const mockCloseFullscreen = vitest.fn();

  const props = {
    selectedDate: "2023-05-09",
    workoutDetails: [
      {
        date: "2023-05-09",
        dayNumber: 10,
        workoutsNumber: 1,
        title: "Workout 1",
        time: 4500,
        workout: "This is a workout",
        distance: 2000,
        split: "1:30.0",
        RPE: 5,
      },
    ],
    setWorkoutDetails: mockSetWorkoutDetails,
    workouts: [
      {
        date: "2023-05-09",
        dayNumber: 10,
        workoutsNumber: 2,
        title: "Workout 2",
        time: 0,
        workout: "This is a workout",
        distance: 2000,
        split: "1:30.0",
        RPE: 5,
      },
    ],
    setWorkouts: vitest.fn(),
    closeFullscreen: mockCloseFullscreen,
    splitError: false,
    setSplitError: vitest.fn(),
    distanceError: false,
    setDistanceError: vitest.fn(),
    errorText: "",
    setErrorText: vitest.fn(),
  };

  const { getByText } = render(
    <BrowserRouter>
      <RenderWorkoutDetails {...props} />
    </BrowserRouter>
  );

  const saveButton = getByText("Save");
  fireEvent.click(saveButton);

  expect(mockSetWorkoutDetails).toHaveBeenCalledWith([]);
  expect(mockCloseFullscreen).toHaveBeenCalled();
});
