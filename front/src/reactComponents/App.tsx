import React from "react";
import "../styling/App.css";

function App() {
  return (
    <div className="App">
      <video autoPlay muted loop id="backgroundVideo">
        <source
          src="/assets/video/CS32-background-video.mp4"
          type="video/mp4"
        />
      </video>
      <div className="menu">
        <div className="logo">
          <img src="/assets/logos/logo-light.svg" alt="Logo" />
        </div>
        <div className="menu-items">
          <button>About</button>
          <button>Register</button>
          <button>Sign In</button>
        </div>
        <p className="description">
          Short paragraph explaining what the site does
        </p>
        <div className="social-icons">
          <img src="/assets/icons/instagram.svg" alt="Instagram" />
          <img src="/assets/icons/twitter.svg" alt="Twitter" />
          <img src="/assets/icons/facebook.svg" alt="Facebook" />
        </div>
      </div>
    </div>
  );
}

export default App;
