import React, { useState } from "react";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import { Parallax } from "react-parallax";
import TextField from "@mui/material/TextField";
import {
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
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import InfoIcon from "@mui/icons-material/Info";
import "../../styling/GenerateWorkout.css";

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

  const renderTextField = () => {
    if (selectedOption === "model3") {
      return (
        <div className="select-field">
          <FormControl fullWidth>
            <InputLabel id="demo-simple-select-label">Goal</InputLabel>
            <Select
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
            <div className="form-row datepickers">
              <div className="date-container">
                <label htmlFor="start-date">Start Date</label>
                <div className="datepicker-container">
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DemoContainer components={["DatePicker"]}>
                      <DatePicker />
                    </DemoContainer>
                  </LocalizationProvider>
                </div>
              </div>
              <div className="date-container">
                <label htmlFor="basic-date">Basic Date Picker</label>
                <div className="datepicker-container">
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DemoContainer components={["DatePicker"]}>
                      <DatePicker />
                    </DemoContainer>
                  </LocalizationProvider>
                </div>
              </div>
            </div>
            <div className="form-row radios">
              <div className="radio-group-container">
                <label>Options</label>
                <div className="radio-group">
                  <FormControl>
                    <FormLabel id="demo-row-radio-buttons-group-label">
                      Model with which to create schedule
                    </FormLabel>
                    <RadioGroup
                      row
                      aria-labelledby="demo-row-radio-buttons-group-label"
                      name="row-radio-buttons-group"
                      defaultValue={"model1"}
                      onChange={handleOptionChange}
                    >
                      <FormControlLabel
                        value="model1"
                        control={<Radio />}
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
                                <InfoIcon />
                              </IconButton>
                            </Tooltip>
                          </div>
                        }
                        className={"form-control"}
                      />
                      <FormControlLabel
                        value="model2"
                        control={<Radio />}
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
                                <InfoIcon />
                              </IconButton>
                            </Tooltip>
                          </div>
                        }
                        className={"form-control"}
                      />
                      <FormControlLabel
                        value="model3"
                        control={<Radio />}
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
                                <InfoIcon />
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
              <div className="select-container">
                <div className="select-field">
                  <FormControl fullWidth>
                    <InputLabel id="demo-simple-select-label">Sport</InputLabel>
                    <Select
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
              <div className="number-container">
                <div className="number-field-container">
                  <TextField
                    id="number-field"
                    type="number"
                    label="Number of Hours"
                    placeholder={"Enter a number"}
                  />
                </div>
              </div>
              <div className={"select-container"}>{renderTextField()}</div>
            </div>
            <button className={"content-button"}>Get Workout Plan</button>
          </form>
        </div>
      </div>
    </Parallax>
  );
}

export default NewSchedule;
