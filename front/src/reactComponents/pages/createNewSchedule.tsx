import React, { useRef, useState } from "react";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import { Parallax } from "react-parallax";
import TextField from "@mui/material/TextField";
import {
  Alert,
  CircularProgress,
  Fade,
  FormControl,
  FormControlLabel,
  FormLabel,
  IconButton,
  InputLabel,
  MenuItem,
  Radio,
  RadioGroup,
  Slide,
  Tooltip,
} from "@mui/material";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import InfoIcon from "@mui/icons-material/Info";
import "../../styling/GenerateWorkout.css";
import { useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import { AnimatePresence, motion } from "framer-motion";

/**

 Component for creating a new workout plan.
 @return {JSX.Element} JSX Element representing the component.
 */
function NewSchedule() {
  // State for selected option and enabling/disabling goal input.
  const [selectedOption, setSelectedOption] = useState("option1");
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
  // States for age, goal, and validation errors related to hours per week input.
  const [age, setAge] = React.useState("");
  const [goal, setGoal] = React.useState("");
  const [hoursPerWeek, setHoursPerWeek] = useState("");
  const [hoursPerWeekError, setHoursPerWeekError] = useState("");

  const handleSportChange = (event: SelectChangeEvent) => {
    setAge(event.target.value as string);
  };

  const handleGoalChange = (event: SelectChangeEvent) => {
    setGoal(event.target.value as string);
  };

  /**
     Validates the hours per week input and sets the error state accordingly.
     @param {string|number} value - The value of the hours per week input.
     */
  const validateHoursPerWeek = (value: string | number) => {
    if (value < 3.0) {
      setHoursPerWeekError("Minimum 3 hours per week");
    } else if (value > 30.0) {
      setHoursPerWeekError("Maximum 30 hours per week");
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
    setAgeEmpty(age === "");
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
      // Create API url based on input values.
      let apiUrl = `http://localhost:3535/create-plan?model=${selectedOption}&hoursPerWeek=${hoursPerWeek}&sport=${age}&startDate=${startDate}&endDate=${endDate}`;

      if (selectedOption === "model3") {
        apiUrl += `&goal=${goal}`;
        console.log(goal);
      }

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
        setSubmitError("Error creating workout plan");
      }
    } catch (error) {
      setLoading(false);
      setSubmitError("Error creating workout plan");
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
                <div
                  className="radio-group-container"
                  role="radiogroup"
                  aria-label="Radio group container for choosing the model for the workout plan"
                >
                  <div className="radio-group">
                    <FormControl>
                      <FormLabel
                        style={{
                          fontFamily: "Muli",
                          fontSize: "13pt",
                          color: "#ebe9e9",
                        }}
                        id="demo-row-radio-buttons-group-label"
                        aria-label="Choose the model for your workout plan"
                      >
                        Choose the model for your workout plan
                      </FormLabel>
                      <RadioGroup
                        className={"radio-group"}
                        row
                        aria-labelledby="demo-row-radio-buttons-group-label"
                        name="row-radio-buttons-group"
                        defaultValue={"model1"}
                        onChange={handleOptionChange}
                      >
                        <FormControlLabel
                          value="model1"
                          control={
                            <Radio
                              sx={{
                                "&, &.Mui-checked": {
                                  color: "#f38418",
                                },
                                "&, &.Mui-unchecked": {
                                  color: "#ebe9e9",
                                },
                              }}
                            />
                          }
                          label={
                            <div className="radio-label">
                              Standard Model{" "}
                              <Tooltip
                                title="This training algorithm sets the standard by creating a
                       comprehensive week-long plan, serving as the foundation for future weeks.
                       Subsequent plans gradually intensify based on the initial week, ensuring
                       progressive improvement in intensity."
                                aria-label="Standard Model: This training algorithm sets the standard by creating a comprehensive week-long plan, serving as the foundation for future weeks. Subsequent plans gradually intensify based on the initial week, ensuring progressive improvement in intensity."
                              >
                                <IconButton
                                  className="info-icon"
                                  aria-label="More information about the Standard Model"
                                >
                                  <InfoIcon sx={{ color: "#ebe9e9" }} />
                                </IconButton>
                              </Tooltip>
                            </div>
                          }
                          className={"form-control"}
                        />
                        <FormControlLabel
                          value="model2"
                          control={
                            <Radio
                              sx={{
                                "&, &.Mui-checked": {
                                  color: "#f38418",
                                },
                                "&, &.Mui-unchecked": {
                                  color: "#ebe9e9",
                                },
                              }}
                            />
                          }
                          label={
                            <div className="radio-label">
                              Variable Model{" "}
                              <Tooltip
                                title="Unlike the standard training algorithm, this model generates
                      a unique plan every week, tailored to the individual's progress and needs.
                       By adapting the plan to their changing abilities, the model ensures that
                        the intensity of the workouts continues to challenge the individual and promote growth"
                                aria-label="Variable Model: Unlike the standard training algorithm, this model generates a unique plan every week, tailored to the individual's progress and needs. By adapting the plan to their changing abilities, the model ensures that the intensity of the workouts continues to challenge the individual and promote growth"
                              >
                                <IconButton className="info-icon">
                                  <InfoIcon sx={{ color: "#ebe9e9" }} />
                                </IconButton>
                              </Tooltip>
                            </div>
                          }
                          className={"form-control"}
                        />
                        <FormControlLabel
                          value="model3"
                          control={
                            <Radio
                              role="radio"
                              aria-checked={true}
                              sx={{
                                "&, &.Mui-checked": {
                                  color: "#f38418",
                                },
                                "&, &.Mui-unchecked": {
                                  color: "#ebe9e9",
                                },
                              }}
                            />
                          }
                          label={
                            <div className="radio-label">
                              Goal Oriented{" "}
                              <Tooltip
                                title="Tailored towards a specific goal, such as a 2000m test,
            this model generates a plan that focuses on the relevant skills and techniques
            needed to achieve that goal. By emphasizing these areas of development, the plan
            provides a clear path towards success, while also incorporating progressive challenges
            to enhance overall fitness and performance."
                                role="tooltip"
                                aria-describedby="tooltip-info"
                              >
                                <IconButton
                                  className="info-icon"
                                  role="button"
                                  aria-label="Information"
                                  aria-describedby="tooltip-info"
                                >
                                  <InfoIcon sx={{ color: "#ebe9e9" }} />
                                </IconButton>
                              </Tooltip>
                            </div>
                          }
                          className={"form-control"}
                          role="presentation"
                          aria-hidden={true}
                        />
                      </RadioGroup>
                    </FormControl>
                  </div>
                </div>
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
                      placeholder={"2.5"}
                      value={hoursPerWeek}
                      onChange={(event) => setHoursPerWeek(event.target.value)}
                      onBlur={(event) =>
                        validateHoursPerWeek(event.target.value)
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
                        value={age}
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
                        <MenuItem value={"2000m"}>2000m Test</MenuItem>
                        <MenuItem value={"6000m"}>6000m Test</MenuItem>
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
                    {Boolean(StartDateAfterEndError) || endDateEmpty ? (
                      <div id="end-date-error" className="error-message">
                        {StartDateAfterEndError}
                      </div>
                    ) : null}
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
            </form>
          </motion.div>
        </div>
      </div>
    </Parallax>
  );
}

export default NewSchedule;
