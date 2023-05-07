import { motion } from "framer-motion";
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
    <motion.div
      // initial={{ x: "-100%" }}
      // animate={{ x: "0%" }}
      // transition={{ duration: 0.5 }}
      className="menu"
      role="navigation"
      aria-label="Logged Out User Menu"
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
          <Link to="/register" className={"menu-links"} role="menuitem">
            <button className="menu-button">REGISTER</button>
          </Link>
        </motion.div>
        <motion.div
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          initial={{ opacity: 0, y: 70 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2, duration: 0.5 }}
        >
          <Link to="/signin" className={"menu-links"} role="menuitem">
            <button className="menu-button">SIGN IN</button>
          </Link>
        </motion.div>
      </motion.div>
      <motion.h2
        className="description"
        role="heading"
        initial={{ opacity: 0, y: 70 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.2, duration: 0.5 }}
      >
        {description}
      </motion.h2>
      <motion.div
        className="social-icons"
        role="navigation"
        aria-label="Social Media Links"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.6, duration: 0.5 }}
      >
        <motion.div whileHover={{ scale: 1.2 }} whileTap={{ scale: 0.8 }}>
          <a
            className="link"
            href="https://www.tiktok.com/@scottycockle"
            target="_blank"
            rel="noopener noreferrer"
          >
            <img src="/assets/icons/instagram.svg" alt="Instagram" />
          </a>
        </motion.div>

        <motion.div whileHover={{ scale: 1.2 }} whileTap={{ scale: 0.8 }}>
          <a
            className="link"
            href="https://www.tiktok.com/@scottycockle"
            target="_blank"
            rel="noopener noreferrer"
          >
            <img src="/assets/icons/twitter.svg" alt="Twitter" />
          </a>
        </motion.div>

        <motion.div whileHover={{ scale: 1.2 }} whileTap={{ scale: 0.8 }}>
          <a
            className="link"
            href="https://www.tiktok.com/@scottycockle"
            target="_blank"
            rel="noopener noreferrer"
          >
            <img src="/assets/icons/facebook.svg" alt="Facebook" />
          </a>
        </motion.div>
      </motion.div>
    </motion.div>
  );
}

export default LoggedOutMenu;
