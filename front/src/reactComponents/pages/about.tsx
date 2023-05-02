/**

 Functional component for rendering the About page.
 @return {JSX.Element} The JSX element representing the About page.
 */
import React from "react";
import "../../styling/App.css";
import LoggedOutMenu from "../elements/loggedOutMenu";
function About() {
  return (
    <LoggedOutMenu
      description={"About page"}
      aria-label="About page navigation menu"
    ></LoggedOutMenu>
  );
}

export default About;
