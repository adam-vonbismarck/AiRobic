import React, { useState } from "react";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import { Parallax } from "react-parallax";
import TextField from "@mui/material/TextField";
import {
  Alert,
  CircularProgress,
  FormControl,
  InputLabel,
  MenuItem,
} from "@mui/material";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import "../../styling/GenerateWorkout.css";
import { useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import { AnimatePresence, motion } from "framer-motion";
import duration from "dayjs/plugin/duration";
import moment from "moment";
import { tooltips } from "../elements/tooltips";

/**

 Component for creating a new workout plan.
 @return {JSX.Element} JSX Element representing the component.
 */
function NewSchedule() {
  // State for selected option and enabling/disabling goal input.
  const [selectedOption, setSelectedOption] = useState("model1");
  const [enableGoal, setEnableGoaL] = useState(true);
  // Get current date using dayjs library.
  const now = dayjs();

  /**

     Handles changes to the selected option input.
     @param {object} event - The event object for the change event.
     */
  const handleOptionChange = (event: {
    target: { value: React.SetStateAction<string> };
  }) => {
    setSelectedOption(event.target.value);
    if (event.target.value == "model3") {
      setEnableGoaL(false);
    } else {
      setEnableGoaL(true);
    }
  };
  // States for sport, goal, and validation errors related to hours per week input.
  const [sport, setSport] = React.useState("");
  const [goal, setGoal] = React.useState("");
  const [hoursPerWeek, setHoursPerWeek] = useState("");
  const [hoursPerWeekError, setHoursPerWeekError] = useState("");

  const handleSportChange = (event: SelectChangeEvent) => {
    setSport(event.target.value as string);
  };

  const handleGoalChange = (event: SelectChangeEvent) => {
    setGoal(event.target.value as string);
  };

  /**
     Validates the hours per week input and sets the error state accordingly.
     @param {string|number} value - The value of the hours per week input.
     */
  const validateHoursPerWeek = (value: number) => {
    if (value < 2.0) {
      setHoursPerWeekError("Minimum 2 hours per week");
    } else if (value > 20.0) {
      setHoursPerWeekError("Maximum 20 hours per week");
    } else {
      setHoursPerWeekError("");
    }
  };
  // States and functions for start and end date inputs and related validation errors.
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [StartDateAfterEndError, setStartDateAfterEndError] = useState("");
  const [dateBeforeTodayError, setDateBeforeTodayError] = useState("");

  /**

     Validates that the start date is before the end date.
     @param {string} start - The value of the start date input.
     @param {string} end - The value of the end date input.
     */
  const validateDate = (start: string, end: string) => {
    if (start > end && start !== "" && end !== "") {
      setStartDateAfterEndError("Start date must be before end date");
    } else {
      setStartDateAfterEndError("");
    }
  };
  /**

     Validates that the date is not before today's date.
     @param {string} dateToCheck - The value of the date input.
     */
  const checkBeforeToday = (dateToCheck: string) => {
    if (now.isAfter(dateToCheck)) {
      setDateBeforeTodayError("Start cannot be before today");
    } else {
      setDateBeforeTodayError("");
    }
  };
  // States for submit error, loading, and required field errors.
  const [submitError, setSubmitError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const [hoursPerWeekEmpty, setHoursPerWeekEmpty] = useState(false);
  const [ageEmpty, setAgeEmpty] = useState(false);
  const [goalEmpty, setGoalEmpty] = useState(false);
  const [startDateEmpty, setStartDateEmpty] = useState(false);
  const [endDateEmpty, setEndDateEmpty] = useState(false);
  const [submitIssue, setSubmitIssue] = useState(false);

  /**

     Generates a workout plan based on the input values.
     @param {object} event - The event object for the click event.
     */
  const generateWorkoutPlan = async (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    event.preventDefault();

    setHoursPerWeekEmpty(hoursPerWeek === "");
    setAgeEmpty(sport === "");
    setStartDateEmpty(startDate === "");
    setEndDateEmpty(endDate === "");

    // If model 3 is selected, set goal empty state.
    if (selectedOption === "model3") {
      setGoalEmpty(goal === "");
    } else {
      setGoalEmpty(false);
    }

    // Check for validation errors and display them if they exist.
    if (StartDateAfterEndError || hoursPerWeekError) {
      setSubmitIssue(true);
      setSubmitError("Please fix errors before submitting");
      return;
    }

    // Check if required fields are empty and display error if they are.
    if (
      hoursPerWeek === "" ||
      (selectedOption === "model3" && goal === "") ||
      startDate === "" ||
      endDate === ""
    ) {
      setSubmitIssue(true);
      setSubmitError("Please fill out all required fields");
      return;
    }

    setLoading(true);

    try {
      let hours = Number(hoursPerWeek);

      dayjs.extend(duration);

      const durationObject = dayjs.duration(hours, "hours");
      const minutes = Math.floor(durationObject.asMinutes());

      const startFormatted = moment(startDate).format("yyyy-MM-DD");
      const endFormatted = moment(endDate).format("yyyy-MM-DD");

      // Create API url based on input values.
      let apiUrl = `http://localhost:3235/create-plan?model=${selectedOption}&hoursPerWeek=${minutes}&sport=${sport}&startDate=${startFormatted}&endDate=${endFormatted}&username=${localStorage.getItem(
        "userID"
      )}`;

      if (selectedOption === "model3") {
        apiUrl += `&goal=${goal}`;
      }

      console.log(apiUrl);

      // Call API to generate workout plan.
      const response = await fetch(apiUrl);

      // Navigate to plan page if successful.
      if (response.ok) {
        setTimeout(() => {
          setLoading(false);
          navigate("/plan");
        }, 5000);
      } else {
        setLoading(false);
        setSubmitIssue(true);
        setSubmitError("Error creating workout plan");
      }
    } catch (error) {
      setLoading(false);
      setSubmitIssue(true);
      setSubmitError("Error creating workout plan.");
    }
  };

  /**

     Renders the button for generating a workout plan.
     @return {JSX.Element} The button JSX element.
     */
  const renderButton = () => {
    if (loading) {
      return (
        <div className="loading-container" role="status" aria-label="Loading">
          <CircularProgress sx={{ color: "#f38418" }} />
        </div>
      );
    } else {
      return (
        <button
          className={"content-button"}
          onClick={(event) => generateWorkoutPlan(event)}
          role="button"
          aria-label="Get Workout Plan"
        >
          Get Workout Plan
        </button>
      );
    }
  };

  return (
    <Parallax
      bgImage={"/assets/images/IMG_7874.jpg"}
      strength={500}
      aria-label="Parallax image with workout plan form"
    >
      <div className={"content-container"}>
        <div className="menu-container" aria-label="Logged in menu container">
          <LoggedInMenu description="Create a new workout plan with our proprietary scheduling algorithm." />
        </div>
        <div className={"content-wrapper"}>
          <motion.h1
            initial={{ opacity: 0, x: 70 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.0, duration: 0.5 }}
          >
            Create a New Workout Plan
          </motion.h1>
          <motion.div
            initial={{ opacity: 0, x: 70 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.0, duration: 0.5 }}
          >
            <form className="form-container" aria-label="Workout plan form">
              <div className="form-row radios">
                {tooltips({ setEnableGoaL, setSelectedOption })}
              </div>
              <div className="form-row dropdown">
                <div
                  className="number-container"
                  aria-label="Hours per week input field"
                  role="group"
                  aria-describedby="hours-error"
                >
                  <div className="number-field-container">
                    <TextField
                      className="custom-textfield"
                      id="number-field"
                      type="number"
                      label="Hours/Week"
                      placeholder={"5"}
                      value={hoursPerWeek}
                      onChange={(event) => setHoursPerWeek(event.target.value)}
                      onBlur={(event) =>
                        validateHoursPerWeek(Number(event.target.value))
                      }
                      error={Boolean(hoursPerWeekError) || hoursPerWeekEmpty}
                      helperText={hoursPerWeekError}
                      aria-label="Input field to set the number of hours per week"
                      aria-invalid={Boolean(hoursPerWeekError)}
                      aria-describedby="hours-error"
                    />
                  </div>
                </div>
                <div
                  className="select-container"
                  aria-label="Sport dropdown field"
                  role="group"
                  aria-describedby="sport-error"
                >
                  <div className="select-field">
                    <FormControl fullWidth>
                      <InputLabel id="demo-simple-select-label">
                        Sport
                      </InputLabel>
                      <Select
                        className="custom-select"
                        labelId="demo-simple-select-label"
                        id="demo-simple-select"
                        value={sport}
                        label="Sport"
                        onChange={handleSportChange}
                        error={ageEmpty}
                        aria-label="Select the sport for the workout plan"
                        aria-invalid={ageEmpty}
                        aria-describedby="sport-error"
                      >
                        <MenuItem value={"Rowing"}>Rowing</MenuItem>
                      </Select>
                    </FormControl>
                  </div>
                </div>
                <div
                  className={"select-container"}
                  aria-label="Goal dropdown field"
                  role="group"
                  aria-describedby="goal-error"
                >
                  <div className="select-field">
                    <FormControl fullWidth>
                      <InputLabel id="demo-simple-select-label">
                        Goal
                      </InputLabel>
                      <Select
                        className="custom-select"
                        labelId="goal-select-dropdown"
                        id="goal-select"
                        value={goal}
                        label="Goal"
                        onChange={handleGoalChange}
                        disabled={enableGoal}
                        error={goalEmpty}
                        aria-label="Select the goal for the workout plan"
                        aria-invalid={goalEmpty}
                        aria-describedby="goal-error"
                      >
                        <MenuItem value={"2k"}>2000m Test</MenuItem>
                        <MenuItem value={"6k"}>6000m Test</MenuItem>
                        <MenuItem value={"30r20"}>30 mins r20</MenuItem>
                      </Select>
                    </FormControl>
                  </div>
                </div>
              </div>
              <div className="form-row datepickers" role="group">
                <div
                  className="date-container"
                  aria-label="Start date input field"
                  role="group"
                  aria-describedby="start-date-error"
                >
                  <label htmlFor="start-date">Start Date</label>
                  <div className="datepicker-container">
                    <TextField
                      className="custom-textfield"
                      id="start-date-field"
                      type="date"
                      value={startDate}
                      onChange={(event) => {
                        setStartDate(event.target.value);
                        validateDate(event.target.value, endDate);
                        checkBeforeToday(event.target.value);
                      }}
                      error={
                        Boolean(StartDateAfterEndError) ||
                        Boolean(dateBeforeTodayError) ||
                        startDateEmpty
                      }
                      helperText={dateBeforeTodayError}
                      style={{ width: "100%" }}
                      aria-label="Input field to set the start date for the workout plan"
                      aria-describedby="start-date-error"
                      aria-invalid={
                        Boolean(StartDateAfterEndError) ||
                        Boolean(dateBeforeTodayError) ||
                        startDateEmpty
                      }
                    />
                  </div>
                </div>
                <div
                  className="date-container"
                  aria-label="End date input field"
                  role="group"
                  aria-describedby="end-date-error"
                >
                  <label htmlFor="basic-date">End Date</label>
                  <div className="datepicker-container">
                    <TextField
                      className="custom-textfield"
                      id="end-date-field"
                      type="date"
                      value={endDate}
                      onChange={(event) => {
                        setEndDate(event.target.value);
                        validateDate(startDate, event.target.value);
                      }}
                      error={Boolean(StartDateAfterEndError) || endDateEmpty}
                      helperText={StartDateAfterEndError}
                      style={{ width: "100%" }}
                      aria-label="Input field to set the end date for the workout plan"
                      aria-invalid={
                        Boolean(StartDateAfterEndError) || endDateEmpty
                      }
                      aria-describedby="end-date-error"
                    />
                  </div>
                </div>
              </div>

              <div className="button-row" aria-label="Submit button row">
                {renderButton()}
              </div>
              {submitIssue && (
                <AnimatePresence>
                  <motion.div
                    className="error-row"
                    initial={{ opacity: 0, y: 50 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: 50 }}
                  >
                    <div
                      className="form-error-message"
                      aria-label="Error message"
                    >
                      <Alert
                        severity="error"
                        variant="filled"
                        sx={{ fontFamily: "Muli" }}
                        aria-label="Error: Workout plan submission unsuccessful"
                      >
                        {submitError}
                      </Alert>
                    </div>
                  </motion.div>
                </AnimatePresence>
              )}
              {loading && (
                <AnimatePresence>
                  <motion.div
                    className="error-row"
                    initial={{ opacity: 0, y: 50 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: 50 }}
                  >
                    <div
                      className="form-error-message"
                      aria-label="Success message"
                    >
                      <Alert
                        severity="success"
                        variant="filled"
                        sx={{ fontFamily: "Muli" }}
                        aria-label="Your workout plan is being generated!"
                      >
                        {"Your workout plan is being generated!"}
                      </Alert>
                    </div>
                  </motion.div>
                </AnimatePresence>
              )}
            </form>
          </motion.div>
        </div>
      </div>
    </Parallax>
  );
}

export default NewSchedule;
