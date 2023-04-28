import React, { useState } from "react";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import { Parallax } from "react-parallax";
import TextField from "@mui/material/TextField";
import {
  FormControl,
  FormControlLabel,
  Icon,
  IconButton,
  InputLabel,
  MenuItem,
  Radio,
  Tooltip,
} from "@mui/material";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import InfoIcon from "@mui/icons-material/Info";

function NewSchedule() {
  const [selectedOption, setSelectedOption] = useState("option1");

  const handleOptionChange = (event: {
    target: { value: React.SetStateAction<string> };
  }) => {
    setSelectedOption(event.target.value);
  };

  const [age, setAge] = React.useState("");

  const handleChange = (event: SelectChangeEvent) => {
    setAge(event.target.value as string);
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
                  <Tooltip title="Tooltip for Option 1">
                    <FormControlLabel
                      value="option1"
                      control={<Radio />}
                      label="Option 1"
                      className={"form-control"}
                    />
                  </Tooltip>
                  <Tooltip title="Tooltip for Option 2">
                    <FormControlLabel
                      value="option2"
                      control={<Radio />}
                      label="Option 2"
                      className={"form-control"}
                    />
                  </Tooltip>
                  <Tooltip title="Additional information" arrow>
                    <IconButton className="info-icon">
                      <InfoIcon />
                    </IconButton>
                  </Tooltip>
                </div>
              </div>
              <div className="number-container">
                <label htmlFor="number-field">Number</label>
                <div className="number-field-container">
                  <TextField id="number-field" type="number" />
                </div>
              </div>
            </div>
            <div className="form-row dropdown">
              <div className="select-container">
                <div className="select-field">
                  <FormControl fullWidth>
                    <InputLabel id="demo-simple-select-label">Age</InputLabel>
                    <Select
                      labelId="demo-simple-select-label"
                      id="demo-simple-select"
                      value={age}
                      label="Age"
                      onChange={handleChange}
                    >
                      <MenuItem value={10}>Ten</MenuItem>
                      <MenuItem value={20}>Twenty</MenuItem>
                      <MenuItem value={30}>Thirty</MenuItem>
                    </Select>
                  </FormControl>
                </div>
              </div>
            </div>
            <button className={"content-button"}>Get Workout Plan</button>
          </form>
        </div>
      </div>
    </Parallax>
  );
}

export default NewSchedule;
