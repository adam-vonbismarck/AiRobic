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
      <h2 className="description">{description}</h2>
    </div>
  );
}

export default LoggedOutMenu;
