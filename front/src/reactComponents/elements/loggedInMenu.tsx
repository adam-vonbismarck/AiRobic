import React from "react";
import { Link } from "react-router-dom";
import "../../styling/Menu.css";

/**
 * Props for the LoggedInMenu component
 */
interface MenuProps {
  description: string;
}

/**
 * Logs the user out of the application
 */
function logOut() {
  localStorage.clear();
  window.location.reload();
}

/**
 * Renders the menu for a logged-in user
 * @param {MenuProps} props - Component props
 * @returns The menu for a logged-in user
 */
function LoggedInMenu({ description }: MenuProps) {
  return (
    <div className="menu" role="navigation" aria-label="Logged In User Menu">
      <Link to="/" role="link" aria-label="Homepage">
        <div className="logo">
          <img src="/assets/logos/logo-light.svg" alt="Logo" />
        </div>
      </Link>
      <div className="menu-items" role="menu">
        <div className="name" role="menuitem">
          <img src="/assets/icons/person.svg" alt="Person Icon" />
          <span>{localStorage.getItem("givenName")}</span>
        </div>
        <Link to="/plan" className={"menu-links"} role="menuitem">
          <button className="menu-button">WORKOUT PLAN</button>
        </Link>
        <Link to="/create-plan" className={"menu-links"} role="menuitem">
          <button className="menu-button">CREATE NEW SCHEDULE</button>
        </Link>
        <Link to="/" className="menu-links" role="menuitem">
          <button
            className="menu-button"
            onClick={logOut}
            aria-label="Sign Out"
          >
            SIGN OUT
          </button>
        </Link>
      </div>
      <h2 className="description" role="heading">
        {description}
      </h2>
    </div>
  );
}

export default LoggedInMenu;
