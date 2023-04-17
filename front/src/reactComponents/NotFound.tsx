import React from "react";
import { Link } from "react-router-dom";
import "../styling/App.css";

function NotFound() {
  return (
    <div className={"not-found-container"}>
      {/*<div className="menu-container">*/}
      {/*  <Menu description="The exercise program generator for your fitness journey." />*/}
      {/*</div>*/}
      <div className="content-wrapper-404">
        <Link to="/">
          <div className="logo">
            <img src="/assets/logos/logo-light.svg" alt="Logo" />
          </div>
        </Link>
        <h1>Sorry, this page doesn’t exist.</h1>
        {/*<h2>*/}
        {/*  Unfortunately there’s nothing to see here. You may have mistyped the*/}
        {/*  address or the page may have moved.*/}
        {/*</h2>*/}
        <Link to="/">
          <button className="content-button">Go Home</button>
        </Link>
      </div>
    </div>
  );
}

export default NotFound;
