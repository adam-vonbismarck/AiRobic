import { motion } from "framer-motion";
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
  window.location.href = "/";
}

/**
 * Renders the menu for a logged-in user
 * @param {MenuProps} props - Component props
 * @returns The menu for a logged-in user
 */
function LoggedInMenu({ description }: MenuProps) {
  return (
    <motion.div
      // initial={{ x: "-100%" }}
      // animate={{ x: "0%" }}
      // transition={{ duration: 0.5 }}
      className="menu"
      role="navigation"
      aria-label="Logged In User Menu"
    >
      <motion.div whileHover={{ scale: 1.05 }}>
        <Link to="/" role="link" aria-label="Homepage">
          <div className="logo">
            <motion.img
              initial={{ opacity: 0, y: 70 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.3 }}
              src="/assets/logos/logo-light.svg"
              alt="Logo"
            />
          </div>
        </Link>
      </motion.div>
      <motion.div
        className="menu-items"
        role="menu"
        // initial={{ opacity: 0 }}
        // animate={{ opacity: 1 }}
        // transition={{ delay: 0.2, duration: 0.5 }}
      >
        <motion.div
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          initial={{ opacity: 0, y: 70 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1, duration: 0.5 }}
        >
          <div className="name" role="menuitem">
            <img src="/assets/icons/person.svg" alt="Person Icon" />
            <span>{localStorage.getItem("givenName")}</span>
          </div>
        </motion.div>
        <motion.div
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          initial={{ opacity: 0, y: 70 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2, duration: 0.5 }}
        >
          <Link to="/plan" className={"menu-links"} role="menuitem">
            <button className="menu-button">WORKOUT PLAN</button>
          </Link>
        </motion.div>
        <motion.div
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          initial={{ opacity: 0, y: 70 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3, duration: 0.5 }}
        >
          <Link to="/create-plan" className={"menu-links"} role="menuitem">
            <button className="menu-button">CREATE NEW SCHEDULE</button>
          </Link>
        </motion.div>
        <motion.div
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          initial={{ opacity: 0, y: 70 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4, duration: 0.5 }}
        >
          <Link to="/" className="menu-links" role="menuitem">
            <button
              className="menu-button"
              onClick={logOut}
              aria-label="Sign Out"
            >
              SIGN OUT
            </button>
          </Link>
        </motion.div>
      </motion.div>
      <motion.h2
        className="description"
        role="heading"
        initial={{ opacity: 0, y: 70 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.4, duration: 0.5 }}
      >
        {description}
      </motion.h2>
    </motion.div>
  );
}

export default LoggedInMenu;
