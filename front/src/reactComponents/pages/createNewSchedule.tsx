import React from "react";
import { Link } from "react-router-dom";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import WorkoutCalendar from "../elements/calendar";
import { Parallax } from "react-parallax";
import TextField from "@mui/material/TextField";
import { Checkbox, FormControlLabel, Tooltip } from "@mui/material";

function NewSchedule() {
  return (
    <Parallax bgImage={"/assets/images/IMG_7874.jpg"} strength={500}>
      <div className={"content-container"}>
        <div className="menu-container">
          <LoggedInMenu description="Create a new workout plan with our proprietary scheduling algorithm." />
        </div>
        <div className={"content-wrapper"}>
          <h1>Create a New Workout Plan</h1>
          <form>
            <TextField
              id="date-range-picker"
              label="Date Range"
              type="date"
              defaultValue="2023-05-01"
              InputLabelProps={{
                shrink: true,
              }}
            />
            <Tooltip title="Tooltip for Checkbox 1">
              <FormControlLabel
                control={<Checkbox defaultChecked={true} />}
                label="Checkbox 1"
                className={"form-control"}
              />
            </Tooltip>
            <Tooltip title="Tooltip for Checkbox 2">
              <FormControlLabel
                control={<Checkbox />}
                label="Checkbox 2"
                className={"form-control"}
              />
            </Tooltip>
          </form>
        </div>
      </div>
    </Parallax>
  );
}

export default NewSchedule;
