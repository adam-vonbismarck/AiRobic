import React from "react";
import ReactDOM from "react-dom/client";
import "./styling/index.css";
import { getMainOverlay, getSearchedOverlay } from "./overlays";
import App from "./reactComponents/App";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <App
      getMainOverlayResponse={getMainOverlay}
      getSearchOverlayResponse={getSearchedOverlay}
    />
  </React.StrictMode>
);
