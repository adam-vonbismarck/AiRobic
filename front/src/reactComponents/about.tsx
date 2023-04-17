import React from "react";
import "../styling/App.css";
import { Link } from "react-router-dom";

function About() {
  return (
    <div className="menu">
      <div className="logo">
        <img src="/assets/logos/logo-light.svg" alt="Logo" />
      </div>
      <div className="menu-items">
        <Link to="/">
          <button className="menu-button"> HOME</button>
        </Link>
        <Link to="/register">
          <button className="menu-button">REGISTER</button>
        </Link>
        <Link to="/signin">
          <button className="menu-button">SIGN IN</button>
        </Link>
      </div>
      <h2 className="description">This is the About page.</h2>
      <div className="social-icons">
        <img src="/assets/icons/instagram.svg" alt="Instagram" />
        <img src="/assets/icons/twitter.svg" alt="Twitter" />
        <img src="/assets/icons/facebook.svg" alt="Facebook" />
      </div>
    </div>
  );
}

export default About;
