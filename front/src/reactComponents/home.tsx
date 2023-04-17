import React from "react";
import "../styling/App.css";
import Menu from "./menu";

function Home() {
  return (
    <div className="App">
      <video autoPlay muted loop id="backgroundVideo">
        <source
          src="/assets/video/CS32-background-video.mp4"
          type="video/mp4"
        />
      </video>
      <Menu description="The exercise program generator for your fitness journey." />
    </div>
  );
}

export default Home;
