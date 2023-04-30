import React from "react";
import { Link } from "react-router-dom";
import "../../styling/Menu.css";
/**
 * Props for the LoggedOutMenu component
 */
interface MenuProps {
  description: string;
}

/**
 * Renders the menu for a logged-out user
 * @param {MenuProps} props - Component props
 * @returns The menu for a logged-out user
 */
function LoggedOutMenu({ description }: MenuProps) {
  return (
    <div className="menu" role="navigation" aria-label="Logged Out User Menu">
      <Link to="/" role="link" aria-label="Homepage">
        <div className="logo">
          <img src="/assets/logos/logo-light.svg" alt="Logo" />
        </div>
      </Link>
      <div className="menu-items" role="menu">
        <Link to="/register" className={"menu-links"} role="menuitem">
          <button className="menu-button">REGISTER</button>
        </Link>
        <Link to="/signin" className={"menu-links"} role="menuitem">
          <button className="menu-button">SIGN IN</button>
        </Link>
      </div>
      <h2 className="description" role="heading">
        {description}
      </h2>
      <div
        className="social-icons"
        role="navigation"
        aria-label="Social Media Links"
      >
        <a
          className="link"
          href="https://www.tiktok.com/@scottycockle"
          target="_blank"
          rel="noopener noreferrer"
        >
          <img src="/assets/icons/instagram.svg" alt="Instagram" />
        </a>

        <a
          className="link"
          href="https://www.tiktok.com/@scottycockle"
          target="_blank"
          rel="noopener noreferrer"
        >
          <img src="/assets/icons/twitter.svg" alt="Twitter" />
        </a>

        <a
          className="link"
          href="https://www.tiktok.com/@scottycockle"
          target="_blank"
          rel="noopener noreferrer"
        >
          <img src="/assets/icons/facebook.svg" alt="Facebook" />
        </a>
      </div>
    </div>
  );
}

export default LoggedOutMenu;
