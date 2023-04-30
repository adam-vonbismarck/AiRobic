import React from "react";
import { Link } from "react-router-dom";
import "../../styling/NotFound.css";

/**

 Functional component for rendering the Not Found page.
 @return {JSX.Element} The JSX element representing the Not Found page.
 */
function NotFound() {
  return (
    <div
      className="not-found-container"
      role="region"
      aria-label="Not Found Page"
    >
      <div className="content-wrapper-404">
        <Link to="/" aria-label="Link to home page">
          <img src="/assets/logos/logo-light.svg" alt="Logo" />
        </Link>
        <h1 role="heading">Sorry, this page doesn’t exist.</h1>
        <Link to="/" aria-label="Link to home page">
          <button className="content-button">Go Home</button>
        </Link>
      </div>
    </div>
  );
}

export default NotFound;
