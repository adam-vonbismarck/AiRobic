import React from "react";
import { Link } from "react-router-dom";

interface MenuProps {
  description: string;
}

function LoggedOutMenu({ description }: MenuProps) {
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
          <span>{"Adam von Bismarck"}</span>
        </div>

        <Link to="/register" className={"menu-links"}>
          <button className="menu-button">WORKOUT PLAN</button>
        </Link>
        <Link to="/" className={"menu-links"}>
          <button className="menu-button">CREATE NEW SCHEDULE</button>
        </Link>
        <button className="menu-button">SIGN OUT</button>
      </div>
      <h2 className="description">{description}</h2>
    </div>
  );
}

export default LoggedOutMenu;
