import React from "react";
import { Link } from "react-router-dom";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import WorkoutCalendar from "../elements/calendar";
import { Parallax } from "react-parallax";

function WorkoutDisplay() {
  return (
    <Parallax
      bgImage={"/assets/images/NaomiBaker_WRCHAMPSD7__13.jpg"}
      strength={500}
    >
      <div className={"content-container"}>
        <div className="menu-container">
          <LoggedInMenu description="Click on a day for more detials and to record your data." />
        </div>
        <div className="content-wrapper">
          <h1>Your Workout Plan</h1>
          <WorkoutCalendar />
        </div>
      </div>
    </Parallax>
  );
}

export default WorkoutDisplay;
