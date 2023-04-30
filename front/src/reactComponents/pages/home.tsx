/**

 Functional component for rendering the Home page.
 @return {JSX.Element} The JSX element representing the Home page.
 */
import React from "react";
import "../../styling/App.css";
import LoggedOutMenu from "../elements/loggedOutMenu";
import LoggedInMenu from "../elements/loggedInMenu";
function Home() {
  if (localStorage.getItem("loggedIn") == "true") {
    return (
      <div
        className="App"
        role="region"
        aria-label="Background video and logged-in user navigation menu"
      >
        <video autoPlay muted loop id="backgroundVideo">
          <source
            src="/assets/video/CS32-background-video.mp4"
            type="video/mp4"
          />
        </video>
        <LoggedInMenu
          description="The exercise program generator for your fitness journey."
          aria-label="Logged-in user navigation menu"
        />
      </div>
    );
  } else {
    return (
      <div
        className="App"
        role="region"
        aria-label="Background video and logged-out user navigation menu"
      >
        <video autoPlay muted loop id="backgroundVideo">
          <source
            src="/assets/video/CS32-background-video.mp4"
            type="video/mp4"
          />
        </video>
        <LoggedOutMenu
          description="The exercise program generator for your fitness journey."
          aria-label="Logged-out user navigation menu"
        />
      </div>
    );
  }
}

export default Home;
