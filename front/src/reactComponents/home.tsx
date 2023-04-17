import React from "react";
import "../styling/App.css";
import { Link } from "react-router-dom";

function Home() {
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
          <Link to="/about">
            <button className="menu-button"> ABOUT</button>
          </Link>
          <Link to="/register">
            <button className="menu-button">REGISTER</button>
          </Link>
          <Link to="/signin">
            <button className="menu-button">SIGN IN</button>
          </Link>
        </div>
        <h2 className="description">
          The exercise program generator for your fitness journey.
        </h2>
        <div className="social-icons">
          <img src="/assets/icons/instagram.svg" alt="Instagram" />
          <img src="/assets/icons/twitter.svg" alt="Twitter" />
          <img src="/assets/icons/facebook.svg" alt="Facebook" />
        </div>
      </div>
    </div>
  );
}

export default Home;
