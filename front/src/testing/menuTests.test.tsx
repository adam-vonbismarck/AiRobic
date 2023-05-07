import { fireEvent, render, screen } from "@testing-library/react";
import LoggedOutMenu from "../reactComponents/elements/loggedOutMenu";
import { BrowserRouter } from "react-router-dom";
import "@testing-library/jest-dom";
import userEvent from "@testing-library/user-event";
import LoggedInMenu from "../reactComponents/elements/loggedInMenu";
import { vitest } from "vitest";

test("renders the logo", () => {
  const description = "Welcome to my website!";
  render(
    <BrowserRouter>
      <LoggedOutMenu description={description} />
    </BrowserRouter>
  );
  const logo = screen.getByAltText("Logo");
  expect(logo).toBeInTheDocument();
});

test("renders the correct menu items", () => {
  const description = "Welcome to my website!";
  render(
    <BrowserRouter>
      <LoggedOutMenu description={description} />
    </BrowserRouter>
  );
  const registerButton = screen.getByRole("button", { name: "REGISTER" });
  const signInButton = screen.getByRole("button", { name: "SIGN IN" });
  expect(registerButton).toBeInTheDocument();
  expect(signInButton).toBeInTheDocument();
});

test("has the correct role and aria-label attributes", () => {
  const description = "Welcome to my website!";
  render(
    <BrowserRouter>
      <LoggedOutMenu description={description} />
    </BrowserRouter>
  );
  const menu = screen.getByRole("navigation", { name: "Logged Out User Menu" });
  const socialIcons = screen.getByRole("navigation", {
    name: "Social Media Links",
  });
  expect(menu).toBeInTheDocument();
  expect(socialIcons).toBeInTheDocument();
});

test("renders the correct heading text", () => {
  const description = "Welcome back to my website!";
  render(
    <BrowserRouter>
      <LoggedInMenu description={description} />
    </BrowserRouter>
  );
  const heading = screen.getByRole("heading", { name: description });
  expect(heading).toBeInTheDocument();
});

test("renders the user name", () => {
  localStorage.setItem("givenName", "John");
  const description = "Welcome back to my website!";
  render(
    <BrowserRouter>
      <LoggedInMenu description={description} />
    </BrowserRouter>
  );
  const name = screen.getByText("John");
  expect(name).toBeInTheDocument();
  localStorage.clear();
});

test("renders the correct menu items", () => {
  const description = "Welcome back to my website!";
  render(
    <BrowserRouter>
      <LoggedInMenu description={description} />
    </BrowserRouter>
  );
  const nameIcon = screen.getByAltText("Person Icon");
  const planButton = screen.getByRole("button", { name: "WORKOUT PLAN" });
  const createPlanButton = screen.getByRole("button", {
    name: "CREATE NEW SCHEDULE",
  });
  const signOutButton = screen.getByRole("menuitem", { name: "SIGN OUT" });
  expect(nameIcon).toBeInTheDocument();
  expect(planButton).toBeInTheDocument();
  expect(createPlanButton).toBeInTheDocument();
  expect(signOutButton).toBeInTheDocument();
});

test("calls logOut function when sign out button is clicked", () => {
  const description = "Welcome back to my website!";
  render(
    <BrowserRouter>
      <LoggedInMenu description={description} />
    </BrowserRouter>
  );
  const signOutButton = screen.getByRole("menuitem", { name: "SIGN OUT" });
  const logOut = vitest.fn(() => {
    console.log("logOut function called");
  });
  window.alert = vitest.fn();
  Object.defineProperty(window, "localStorage", {
    value: {
      getItem: () => null,
      clear: () => {},
    },
    writable: true,
  });
  fireEvent.click(signOutButton);
  expect(window.location.href).toEqual("http://localhost:3000/");
});
