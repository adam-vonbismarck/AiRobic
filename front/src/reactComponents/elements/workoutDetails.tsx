import * as React from "react";
import { NumericFormat, NumericFormatProps } from "react-number-format";
import TextField from "@mui/material/TextField";
import { Workout } from "./types";
import { Slider } from "@mui/material";
import { IMaskInput } from "react-imask";
import moment from "moment";

interface CustomProps {
  onChange: (event: { target: { name: string; value: string } }) => void;
  name: string;
}

interface Props {
  selectedDate: string | null;
  workoutDetails: Workout[];
  setWorkoutDetails: React.Dispatch<React.SetStateAction<Workout[]>>;
  workouts: Workout[];
  setWorkouts: React.Dispatch<React.SetStateAction<Workout[]>>;
  closeFullscreen: () => void;
}

interface CustomProps {
  onChange: (event: { target: { name: string; value: string } }) => void;
  name: string;
}

const NumericFormatCustom = React.forwardRef<NumericFormatProps, CustomProps>(
  function NumericFormatCustom(props, ref) {
    const { onChange, ...other } = props;

    return (
      <>
        <NumericFormat
          {...other}
          getInputRef={ref}
          onValueChange={(values) => {
            onChange({
              target: {
                name: props.name,
                value: values.value,
              },
            });
          }}
          thousandSeparator
          valueIsNumericString
        />
      </>
    );
  }
);

const TextMaskCustom = React.forwardRef<HTMLElement, CustomProps>(
  function TextMaskCustom(props, ref) {
    const { onChange, ...other } = props;
    return (
      <IMaskInput
        {...other}
        mask="0:00.0"
        inputRef={ref}
        onAccept={(value: any) => {
          onChange({ target: { name: props.name, value } });
        }}
        overwrite
      />
    );
  }
);

export const renderWorkoutDetails = ({
  selectedDate,
  workoutDetails,
  setWorkoutDetails,
  workouts,
  setWorkouts,
  closeFullscreen,
}: Props) => {
  if (!selectedDate) {
    return null;
  }

  const workoutsForSelectedDate = workoutDetails.filter(
    (workout) => workout.date === selectedDate
  );

  const updateWorkout = (updatedWorkout: Workout, index: number) => {
    const updatedWorkoutDetails = [...workoutDetails];
    updatedWorkoutDetails[index] = updatedWorkout;
    setWorkoutDetails(updatedWorkoutDetails);

    // Update workouts
    const workoutIndex = workouts.findIndex(
      (w) => w.date === updatedWorkout.date && w.title === updatedWorkout.title
    );
    if (workoutIndex > -1) {
      const updatedWorkouts = [...workouts];
      updatedWorkouts[workoutIndex] = updatedWorkout;
      setWorkouts(updatedWorkouts);
    }
  };

  return (
    <div className="workout-details">
      <h2>
        {"Workouts for " + moment(selectedDate).format("dddd, Do MMMM YYYY")}
      </h2>
      {workoutsForSelectedDate.map((workout, index) => (
        <div key={index}>
          <h3>{workout.title}</h3>
          <p>
            <strong>Duration:</strong> {workout.duration} minutes
          </p>
          <p>
            <strong>Description:</strong> {workout.description}
          </p>
          <p>
            <strong>Calories Burned:</strong> {workout.caloriesBurned}
          </p>
          <div className="workout-details__split">
            <TextField
              className="custom-textfield"
              label="Distance (meters)"
              type="text"
              value={workout.distance || ""}
              InputProps={{
                inputComponent: NumericFormatCustom as any,
                inputProps: {
                  name: "distance",
                },
              }}
              onChange={(event) => {
                const distance = parseInt(event.target.value);
                const updatedWorkout = { ...workout, distance };
                updateWorkout(updatedWorkout, index);
              }}
            />
            <TextField
              className="custom-textfield"
              label="Avg /500m"
              value={workout.avgSplit}
              placeholder={"1:30.0"}
              defaultValue={workout.avgSplit}
              onChange={(event) => {
                const avgSplit = event.target.value;
                const updatedWorkout = { ...workout, avgSplit };
                updateWorkout(updatedWorkout, index);
              }}
              name="numberformat"
              id="formatted-numberformat-input"
              InputProps={{
                inputComponent: TextMaskCustom as any,
              }}
              variant="outlined"
            />
          </div>
          <label className="slider-container">
            Perceived Effort (1-10):
            <Slider
              className="custom-slider"
              aria-label="Default"
              valueLabelDisplay="auto"
              step={1}
              min={0}
              max={10}
              value={workout.perceivedEffort}
              defaultValue={workout.perceivedEffort}
              onChange={(event) => {
                // @ts-ignore
                const perceivedEffort = parseInt(event.target.value);
                const updatedWorkout = { ...workout, perceivedEffort };
                updateWorkout(updatedWorkout, index);
              }}
            />
            {workout.perceivedEffort}
          </label>
        </div>
      ))}
      <button className={"content-button"} onClick={closeFullscreen}>
        Save
      </button>
    </div>
  );
};
