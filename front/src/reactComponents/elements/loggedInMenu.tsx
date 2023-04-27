import React from "react";
import { Link } from "react-router-dom";

interface MenuProps {
  description: string;
}

function logOut() {
  localStorage.clear();
  window.location.reload();
}

function LoggedInMenu({ description }: MenuProps) {
  return (
    <div className="menu">
      <Link to="/">
        <div className="logo">
          <img src="/assets/logos/logo-light.svg" alt="Logo" />
        </div>
      </Link>
      <div className="menu-items">
        <div className="name">
          <img src="/assets/icons/person.svg" alt="Person Icon" />
          <span>{localStorage.getItem("givenName")}</span>
        </div>

        <Link to="/register" className={"menu-links"}>
          <button className="menu-button">WORKOUT PLAN</button>
        </Link>
        <Link to="/" className={"menu-links"}>
          <button className="menu-button">CREATE NEW SCHEDULE</button>
        </Link>
        <Link to="/" className="menu-links">
          <button className="menu-button"
          onClick={logOut}
          
          >SIGN OUT</button>
        </Link>
      </div>
      <h2 className="description">{description}</h2>
    </div>
  );
}

export default LoggedInMenu;
