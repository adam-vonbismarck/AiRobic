import React from "react";
import "../../styling/App.css";
import LoggedOutMenu from "../elements/loggedOutMenu";
import LoggedInMenu from "../elements/loggedInMenu";

function Home() {
  if (localStorage.getItem("loggedIn") == "true") {
    return (
      <div className="App">
        <video autoPlay muted loop id="backgroundVideo">
          <source
            src="/assets/video/CS32-background-video.mp4"
            type="video/mp4"
          />
        </video>
        <LoggedInMenu description="The exercise program generator for your fitness journey." />
      </div>
    );
  }
  else {
    return (
      <div className="App">
        <video autoPlay muted loop id="backgroundVideo">
          <source
            src="/assets/video/CS32-background-video.mp4"
            type="video/mp4"
          />
        </video>
        <LoggedOutMenu description="The exercise program generator for your fitness journey." />
      </div>
    );
  }
}

export default Home;
