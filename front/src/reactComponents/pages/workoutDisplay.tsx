import React from "react";
import { Link } from "react-router-dom";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import WorkoutCalendar from "../elements/calendar";

function WorkoutDisplay() {
  return (
    <div className={"content-container"}>
      <div className="menu-container">
        <LoggedInMenu description="The exercise program generator for your fitness journey." />
      </div>
      <div className="content-wrapper">
        <div className="calendar-container">
          <WorkoutCalendar />
        </div>
      </div>
    </div>
  );
}

export default WorkoutDisplay;
