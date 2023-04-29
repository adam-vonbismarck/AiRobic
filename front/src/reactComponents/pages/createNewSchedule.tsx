import React, { useState } from "react";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import { Parallax } from "react-parallax";
import TextField from "@mui/material/TextField";
import {
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
  Tooltip,
} from "@mui/material";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import InfoIcon from "@mui/icons-material/Info";
import "../../styling/GenerateWorkout.css";
import { useNavigate } from "react-router-dom";

function NewSchedule() {
  const [selectedOption, setSelectedOption] = useState("option1");

  const handleOptionChange = (event: {
    target: { value: React.SetStateAction<string> };
  }) => {
    setSelectedOption(event.target.value);
  };

  const [age, setAge] = React.useState("");
  const [goal, setGoal] = React.useState("");

  const handleSportChange = (event: SelectChangeEvent) => {
    setAge(event.target.value as string);
  };

  const handleGoalChange = (event: SelectChangeEvent) => {
    setGoal(event.target.value as string);
  };

  const [hoursPerWeek, setHoursPerWeek] = useState("");
  const [hoursPerWeekError, setHoursPerWeekError] = useState("");

  const validateHoursPerWeek = (value: string | number) => {
    if (value < 3.0) {
      setHoursPerWeekError("Minimum 3 hours per week");
    } else if (value > 30.0) {
      setHoursPerWeekError("Maximum 30 hours per week");
    } else {
      setHoursPerWeekError("");
    }
  };

  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [dateError, setDateError] = useState("");
  const [submitIssue, setSubmitIssue] = useState(false);

  const validateDate = (start: string, end: string) => {
    if (start > end && start !== "" && end !== "") {
      setDateError("Start date must be before end date");
    } else {
      setDateError("");
    }
  };

  const [submitError, setSubmitError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const generateWorkoutPlan = async (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    event.preventDefault();

    if (dateError || hoursPerWeekError) {
      setSubmitIssue(true);
      setSubmitError("Please fix errors before submitting");
      return;
    }

    if (
      hoursPerWeek === "" ||
      (selectedOption === "model3" && goal === "") ||
      startDate === "" ||
      endDate === ""
    ) {
      setSubmitError("Please fill out all required fields");
      return;
    }

    setLoading(true);

    try {
      let apiUrl = `http://localhost:3535/create-plan?model=${selectedOption}&hoursPerWeek=${hoursPerWeek}&sport=${age}&startDate=${startDate}&endDate=${endDate}`;

      if (selectedOption === "model3") {
        apiUrl += `&goal=${goal}`;
      }

      const response = await fetch(apiUrl);

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

  const renderTextField = () => {
    if (selectedOption === "model3") {
      return (
        <Fade in={true} timeout={500} unmountOnExit>
          <div className="select-field">
            <FormControl fullWidth>
              <InputLabel id="demo-simple-select-label">Goal</InputLabel>
              <Select
                className="custom-select"
                labelId="goal-select-dropdown"
                id="goal-select"
                value={goal}
                label="Goal"
                onChange={handleGoalChange}
              >
                <MenuItem value={2000}>2000m Test</MenuItem>
                <MenuItem value={5000}>5000m Test</MenuItem>
              </Select>
            </FormControl>
          </div>
        </Fade>
      );
    } else {
      return (
        <Fade in={false} timeout={500} unmountOnExit>
          <div className="select-field">
            <FormControl fullWidth>
              <InputLabel id="demo-simple-select-label">Goal</InputLabel>
              <Select
                className="custom-select"
                labelId="goal-select-dropdown"
                id="goal-select"
                value={goal}
                label="Goal"
                onChange={handleGoalChange}
              >
                <MenuItem value={2000}>2000m Test</MenuItem>
                <MenuItem value={5000}>5000m Test</MenuItem>
              </Select>
            </FormControl>
          </div>
        </Fade>
      );
    }
  };

  return (
    <Parallax bgImage={"/assets/images/IMG_7874.jpg"} strength={500}>
      <div className={"content-container"}>
        <div className="menu-container">
          <LoggedInMenu description="Create a new workout plan with our proprietary scheduling algorithm." />
        </div>
        <div className={"content-wrapper"}>
          <h1>Create a New Workout Plan</h1>
          <form className="form-container">
            <div className="form-row radios">
              <div className="radio-group-container">
                {/*<label>Options</label>*/}
                <div className="radio-group">
                  <FormControl>
                    <FormLabel
                      style={{
                        fontFamily: "Muli",
                        fontSize: "12pt",
                        color: "#ebe9e9",
                      }}
                      id="demo-row-radio-buttons-group-label"
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
                            >
                              <IconButton className="info-icon">
                                <InfoIcon sx={{ color: "#ebe9e9" }} />
                              </IconButton>
                            </Tooltip>
                          </div>
                        }
                        className={"form-control"}
                      />
                    </RadioGroup>
                  </FormControl>
                </div>
              </div>
            </div>
            <div className="form-row dropdown">
              <div className="number-container">
                <div className="number-field-container">
                  <TextField
                    className="custom-textfield"
                    id="number-field"
                    type="number"
                    label="Hours/Week"
                    placeholder={"2.5"}
                    value={hoursPerWeek}
                    onChange={(event) => setHoursPerWeek(event.target.value)}
                    onBlur={(event) => validateHoursPerWeek(event.target.value)}
                    error={Boolean(hoursPerWeekError)}
                    helperText={hoursPerWeekError}
                  />
                </div>
              </div>
              <div className="select-container">
                <div className="select-field">
                  <FormControl fullWidth>
                    <InputLabel id="demo-simple-select-label">Sport</InputLabel>
                    <Select
                      className="custom-select"
                      labelId="demo-simple-select-label"
                      id="demo-simple-select"
                      value={age}
                      label="Sport"
                      onChange={handleSportChange}
                    >
                      <MenuItem value={"Rowing"}>Rowing</MenuItem>
                    </Select>
                  </FormControl>
                </div>
              </div>
              <div className={"select-container"}>{renderTextField()}</div>
            </div>
            <div className="form-row datepickers">
              <div className="date-container">
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
                    }}
                    error={Boolean(dateError)}
                  />
                </div>
              </div>
              <div className="date-container">
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
                    error={Boolean(dateError)}
                    helperText={dateError}
                  />
                </div>
              </div>
            </div>
            <div className="button-row">
              <button
                className={"content-button"}
                onClick={(event) => generateWorkoutPlan(event)}
              >
                Get Workout Plan
              </button>
            </div>
            {loading && submitIssue && (
              <div className="error-row">
                <div className="form-error-message">{submitError}</div>
                <div className="loading-container">
                  <CircularProgress />
                </div>
              </div>
            )}
          </form>
        </div>
      </div>
    </Parallax>
  );
}

export default NewSchedule;
