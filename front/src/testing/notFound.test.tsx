import { render } from "@testing-library/react";
import NotFound from "../reactComponents/pages/NotFound";
import { BrowserRouter } from "react-router-dom";
import "@testing-library/jest-dom";

test("renders Not Found page without crashing", () => {
  render(
    <BrowserRouter>
      <NotFound />
    </BrowserRouter>
  );
});

test("displays correct heading", () => {
  const { getByRole } = render(
    <BrowserRouter>
      <NotFound />
    </BrowserRouter>
  );
  const heading = getByRole("heading");
  expect(heading).toHaveTextContent("Sorry, this page doesn’t exist.");
});

test("displays Go Home button", () => {
  const { getByRole } = render(
    <BrowserRouter>
      <NotFound />
    </BrowserRouter>
  );
  const button = getByRole("button", { name: "Go Home" });
  expect(button).toBeInTheDocument();
});

test("contains logo image", () => {
  const { getByRole } = render(
    <BrowserRouter>
      <NotFound />
    </BrowserRouter>
  );
  const img = getByRole("img", { name: "Logo" });
  expect(img).toBeInTheDocument();
});
