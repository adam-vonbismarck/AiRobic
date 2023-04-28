import React from "react";
import { Link } from "react-router-dom";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import WorkoutCalendar from "../elements/calendar";
import { Parallax } from "react-parallax";

function NewSchedule() {
  return (
    <Parallax
      bgImage={"/assets/images/NaomiBaker_WRCHAMPSD7__13.jpg"}
      strength={500}
    >
      <div className={"content-container"}>
        <div className="menu-container">
          <LoggedInMenu description="Create a new workout plan to start training with Airobic." />
        </div>
        <div className="content-wrapper">
          <h1>Create a New Workout Plan</h1>
        </div>
      </div>
    </Parallax>
  );
}

export default NewSchedule;
