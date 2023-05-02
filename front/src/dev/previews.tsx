import React from "react";
import {ComponentPreview, Previews} from "@react-buddy/ide-toolbox";
import { PaletteTree } from "./palette";
import About from "../reactComponents/pages/about";
import NewSchedule from "../reactComponents/pages/createNewSchedule";
import ComponentPreviews from "./previews 2";

const ComponentPreviews = () => {
  return (
    <Previews palette={<PaletteTree />}>
      <ComponentPreview path="/About">
        <About />
      </ComponentPreview>
      <ComponentPreview path="/About">
        <About />
      </ComponentPreview>
      <ComponentPreview path="/NewSchedule">
        <NewSchedule />
      </ComponentPreview>
      <ComponentPreview path="/ComponentPreviews">
        <ComponentPreviews />
      </ComponentPreview>
    </Previews>
  );
};

export default ComponentPreviews;
