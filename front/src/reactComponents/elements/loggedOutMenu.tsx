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
        {/*<Link to="/about">*/}
        {/*  <button className="menu-button"> ABOUT</button>*/}
        {/*</Link>*/}
        <Link to="/register">
          <button className="menu-button">REGISTER</button>
        </Link>
        <Link to="/signin">
          <button className="menu-button">SIGN IN</button>
        </Link>
      </div>
      <h2 className="description">{description}</h2>
      <div className="social-icons">
        <a
          className="link"
          href="https://www.tiktok.com/@scottycockle"
          target="_blank"
        >
          <img src="/assets/icons/instagram.svg" alt="Instagram" />
        </a>

        <a
          className="link"
          href="https://www.tiktok.com/@scottycockle"
          target="_blank"
        >
          <img src="/assets/icons/twitter.svg" alt="Twitter" />
        </a>

        <a
          className="link"
          href="https://www.tiktok.com/@scottycockle"
          target="_blank"
        >
          <img src="/assets/icons/facebook.svg" alt="Facebook" />
        </a>
      </div>
    </div>
  );
}

export default LoggedOutMenu;
