import * as React from "react";
import { NumericFormat, NumericFormatProps } from "react-number-format";
import TextField from "@mui/material/TextField";
import { Workout } from "./types";
import { IconButton, Slider } from "@mui/material";
import { IMaskInput } from "react-imask";
import moment from "moment";
import "../../styling/WorkoutDetails.css";
import CloseIcon from "@mui/icons-material/Close";

/**

 Renders workout details for the selected date.
 @param {Props} props - The props object containing workout details and other related information.
 @param {string | null} props.selectedDate - The selected date for which workout details need to be displayed.
 @param {Workout[]} props.workoutDetails - An array of objects containing workout details.
 @param {React.Dispatch<React.SetStateAction<Workout[]>>} props.setWorkoutDetails - The state update function for workoutDetails.
 @param {Workout[]} props.workouts - An array of objects containing workout data.
 @param {React.Dispatch<React.SetStateAction<Workout[]>>} props.setWorkouts - The state update function for workouts.
 @param {() => void} props.closeFullscreen - The function for closing the fullscreen view of a workout detail.
 @return {JSX.Element | null} The JSX element representing the workout details for the selected date, or null if no date is selected.
 */
interface Props {
  selectedDate: string | null;
  workoutDetails: Workout[];
  setWorkoutDetails: React.Dispatch<React.SetStateAction<Workout[]>>;
  workouts: Workout[];
  setWorkouts: React.Dispatch<React.SetStateAction<Workout[]>>;
  closeFullscreen: () => void;
}

/**
 * Props for custom input component
 */
interface CustomProps {
  onChange: (event: { target: { name: string; value: string } }) => void;
  name: string;
}

/**
 * Custom component for numeric input field
 * @param props - Component props
 * @param ref - Component reference
 * @returns A numeric input field with custom formatting
 */
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

/**
 * Custom component for masked text input field
 * @param props - Component props
 * @param ref - Component reference
 * @returns A masked text input field
 */
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

/**
 * Renders workout details for the selected date
 * @param {Props} Props - Component props
 * @returns Workout details for the selected date
 */
export const renderWorkoutDetails = ({
  selectedDate,
  workoutDetails,
  setWorkoutDetails,
  workouts,
  setWorkouts,
  closeFullscreen,
}: Props) => {
  // Return null if selectedDate is falsy
  if (!selectedDate) {
    return null;
  }

  // Filter workoutDetails by date
  const workoutsForSelectedDate = workoutDetails.filter(
    (workout) => workout.date === selectedDate
  );

  /**
   * Updates workout details and workouts state
   * @param {Workout} updatedWorkout - Updated workout details
   * @param {number} index - Index of the workout in the workoutDetails array
   */
  // const updateWorkout = (updatedWorkout: Workout, index: number) => {
  //   const updatedWorkoutDetails = [...workoutDetails];
  //   updatedWorkoutDetails[index] = updatedWorkout;
  //   setWorkoutDetails(updatedWorkoutDetails);
  //
  //   // Update workouts
  //   const workoutIndex = workouts.findIndex(
  //     (w) => w.date === updatedWorkout.date && w.title === updatedWorkout.title
  //   );
  //   if (workoutIndex > -1) {
  //     const updatedWorkouts = [...workouts];
  //     updatedWorkouts[workoutIndex] = updatedWorkout;
  //     setWorkouts(updatedWorkouts);
  //   }
  // };

  if (workoutsForSelectedDate.length === 0) {
    return (
      <div className="workout-details">
        <div className="workout-details__header">
          <IconButton className="info-icon">
            <CloseIcon sx={{ color: "#c61924" }} onClick={closeFullscreen} />
          </IconButton>
        </div>
        <h2>
          {"Workouts for " + moment(selectedDate).format("dddd, Do MMMM YYYY")}
        </h2>
        <div>
          <p>
            {" "}
            <strong>No workouts set for this date.</strong>
          </p>
        </div>
      </div>
    );
  } else {
    return (
      <div className="workout-details" aria-label="Workout Details">
        <div
          className="workout-details__header"
          aria-label="Workout Details Header"
        >
          <IconButton className="info-icon" aria-label="Close Icon Button">
            <CloseIcon
              sx={{ color: "#c61924" }}
              onClick={closeFullscreen}
              aria-label="Close Icon"
            />
          </IconButton>
        </div>
        <h2
          aria-label={`Workouts for ${moment(selectedDate).format(
            "dddd, Do MMMM YYYY"
          )}`}
        >
          {"Workouts for " + moment(selectedDate).format("dddd, Do MMMM YYYY")}
        </h2>
        {workoutsForSelectedDate.map((workout, index) => (
          <div
            className={"specific-workout"}
            key={index}
            aria-label={`Workout ${index + 1}`}
          >
            {/*<h3>{workout.title}</h3>*/}
            <p>
              <strong aria-label="Duration">Duration:</strong>{" "}
              {/*{workout.duration} minutes*/}
            </p>
            <p>
              <strong aria-label="Description">Description:</strong>{" "}
              {/*{workout.description}*/}
            </p>
            <p>
              <strong aria-label="Calories Burned">Calories Burned:</strong>{" "}
              {/*{workout.caloriesBurned}*/}
            </p>
            <div
              className="workout-details__split"
              aria-label="Workout Details Split"
            >
              <TextField
                className="custom-textfield"
                label="Distance (meters)"
                type="text"
                placeholder={"2000"}
                // value={workout.distance || ""}
                InputProps={{
                  inputComponent: NumericFormatCustom as any,
                  inputProps: {
                    name: "distance",
                  },
                }}
                onChange={(event) => {
                  const distance = parseInt(event.target.value);
                  const updatedWorkout = { ...workout, distance };
                  // updateWorkout(updatedWorkout, index);
                }}
                aria-label="Workout Distance"
              />
              <TextField
                className="custom-textfield"
                label="Avg /500m"
                // value={workout.avgSplit}
                placeholder={"1:30.0"}
                // defaultValue={workout.avgSplit}
                onChange={(event) => {
                  const avgSplit = event.target.value;
                  const updatedWorkout = { ...workout, avgSplit };
                  // updateWorkout(updatedWorkout, index);
                }}
                name="numberformat"
                id="formatted-numberformat-input"
                InputProps={{
                  inputComponent: TextMaskCustom as any,
                }}
                variant="outlined"
                aria-label="Average Split"
              />
            </div>
            <div className="slider-container" aria-label="Slider Container">
              <h5> RPE (1-10):</h5>
              <Slider
                className="custom-slider"
                aria-label="Perceived Effort"
                valueLabelDisplay="on"
                step={1}
                min={0}
                max={10}
                track={false}
                // value={workout.perceivedEffort}
                // defaultValue={workout.perceivedEffort}
                onChange={(event) => {
                  // @ts-ignore
                  const perceivedEffort = parseInt(event.target.value);
                  const updatedWorkout = { ...workout, perceivedEffort };
                  // updateWorkout(updatedWorkout, index);
                }}
                sx={{
                  "& .MuiSlider-thumb": {
                    color: "#f38418",
                  },
                  "& .MuiSlider-rail": {
                    backgroundImage:
                      "linear-gradient(90deg, rgba(0,204,13,1) 0%, rgba(209,221,221,1) 50%, rgba(255,0,0,1) 100%)",
                  },
                  "& .MuiSlider-valueLabel": {
                    color: "#ebe9e9",
                    backgroundColor: "#f38418",
                    borderRadius: "15px",
                  },
                  "& .MuiSlider-thumb:hover": {
                    boxShadow: "0px 0px 0px 8px rgba(243,132,24,0.16)",
                  },
                  "& .MuiSlider-thumb.Mui-focusVisible": {
                    boxShadow: "0px 0px 0px 8px rgba(243,132,24,0.24)",
                  },
                }}
              />
            </div>
          </div>
        ))}
        <button
          className={"content-button"}
          onClick={closeFullscreen}
          aria-label="Save Button"
        >
          Save
        </button>
      </div>
    );
  }
};
