import React from "react";
import { Link } from "react-router-dom";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import WorkoutCalendar from "../elements/calendar";
import { Parallax } from "react-parallax";

function WorkoutDisplay() {
  return (
    <div className={"content-container"}>
      <div className="menu-container">
        <LoggedInMenu description="Click on a day for more detials and to record your data." />
      </div>
      <div className="content-wrapper">
        <h1>Workout Plan</h1>
        <div className="calendar-container">
          <WorkoutCalendar />
        </div>
      </div>
    </div>
  );
}

export default WorkoutDisplay;
