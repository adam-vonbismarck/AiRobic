/**
 * Functional component for rendering the Workout Display page.
 * @return {JSX.Element} The JSX element representing the Workout Display page.
 */
import React from "react";
import "../../styling/App.css";
import LoggedInMenu from "../elements/loggedInMenu";
import WorkoutCalendar from "../elements/calendar";
import { Parallax } from "react-parallax";

function WorkoutDisplay() {
  return (
    <Parallax
      bgImage={"/assets/images/NaomiBaker_WRCHAMPSD7__13.jpg"}
      strength={500}
      aria-label="Parallax image with workout display page"
    >
      <div className={"content-container"} role="main">
        <div
          className="menu-container"
          aria-label="Logged-in user navigation menu"
          role="navigation"
        >
          <LoggedInMenu description="Click on a day for more details and to record your data." />
        </div>
        <div className="content-wrapper" role="article">
          <h1>Your Workout Plan</h1>
          <WorkoutCalendar />
        </div>
      </div>
    </Parallax>
  );
}

export default WorkoutDisplay;
