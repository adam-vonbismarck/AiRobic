import React from "react";
import "../../styling/App.css";
import {
  FormControl,
  FormControlLabel,
  FormLabel,
  IconButton,
  Radio,
  RadioGroup,
  Tooltip,
} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import "../../styling/GenerateWorkout.css";

interface customTooltipProps {
  setSelectedOption: React.Dispatch<React.SetStateAction<string>>;
  setEnableGoaL: React.Dispatch<React.SetStateAction<boolean>>;
}

export const tooltips = ({
  setSelectedOption,
  setEnableGoaL,
}: customTooltipProps) => {
  const handleOptionChange = (event: {
    target: { value: React.SetStateAction<string> };
  }) => {
    setSelectedOption(event.target.value);
    if (event.target.value == "model3") {
      setEnableGoaL(false);
    } else {
      setEnableGoaL(true);
    }
  };

  return (
    <div
      className="radio-group-container"
      role="radiogroup"
      aria-label="Radio group container for choosing the model for the workout plan"
    >
      <div className="radio-group">
        <FormControl>
          <FormLabel
            style={{
              fontFamily: "Muli",
              fontSize: "13pt",
              color: "#ebe9e9",
            }}
            id="demo-row-radio-buttons-group-label"
            aria-label="Choose the model for your workout plan"
          >
            Choose the model for your workout plan
          </FormLabel>
          <RadioGroup
            className={"radio-group"}
            row
            aria-labelledby="demo-row-radio-buttons-group-label"
            name="row-radio-buttons-group"
            defaultValue={"model1"}
            onChange={handleOptionChange}
          >
            <FormControlLabel
              value="model1"
              control={
                <Radio
                  sx={{
                    "&, &.Mui-checked": {
                      color: "#f38418",
                    },
                    "&, &.Mui-unchecked": {
                      color: "#ebe9e9",
                    },
                  }}
                />
              }
              label={
                <div className="radio-label">
                  Standard Model{" "}
                  <Tooltip
                    title="This training algorithm sets the standard by creating a
                       comprehensive week-long plan, serving as the foundation for future weeks.
                       Subsequent plans gradually intensify based on the initial week, ensuring
                       progressive improvement in intensity."
                    aria-label="Standard Model: This training algorithm sets the standard by creating a comprehensive week-long plan, serving as the foundation for future weeks. Subsequent plans gradually intensify based on the initial week, ensuring progressive improvement in intensity."
                  >
                    <IconButton
                      className="info-icon"
                      aria-label="More information about the Standard Model"
                    >
                      <InfoIcon sx={{ color: "#ebe9e9" }} />
                    </IconButton>
                  </Tooltip>
                </div>
              }
              className={"form-control"}
            />
            <FormControlLabel
              value="model2"
              control={
                <Radio
                  sx={{
                    "&, &.Mui-checked": {
                      color: "#f38418",
                    },
                    "&, &.Mui-unchecked": {
                      color: "#ebe9e9",
                    },
                  }}
                />
              }
              label={
                <div className="radio-label">
                  Variable Model{" "}
                  <Tooltip
                    title="Unlike the standard training algorithm, this model generates
                      a unique plan every week, tailored to the individual's progress and needs.
                       By adapting the plan to their changing abilities, the model ensures that
                        the intensity of the workouts continues to challenge the individual and promote growth"
                    aria-label="Variable Model: Unlike the standard training algorithm, this model generates a unique plan every week, tailored to the individual's progress and needs. By adapting the plan to their changing abilities, the model ensures that the intensity of the workouts continues to challenge the individual and promote growth"
                  >
                    <IconButton className="info-icon">
                      <InfoIcon sx={{ color: "#ebe9e9" }} />
                    </IconButton>
                  </Tooltip>
                </div>
              }
              className={"form-control"}
            />
            <FormControlLabel
              value="model3"
              control={
                <Radio
                  role="radio"
                  aria-checked={true}
                  sx={{
                    "&, &.Mui-checked": {
                      color: "#f38418",
                    },
                    "&, &.Mui-unchecked": {
                      color: "#ebe9e9",
                    },
                  }}
                />
              }
              label={
                <div className="radio-label">
                  Goal Oriented{" "}
                  <Tooltip
                    title="Tailored towards a specific goal, such as a 2000m test,
            this model generates a plan that focuses on the relevant skills and techniques
            needed to achieve that goal. By emphasizing these areas of development, the plan
            provides a clear path towards success, while also incorporating progressive challenges
            to enhance overall fitness and performance."
                    role="tooltip"
                    aria-describedby="tooltip-info"
                  >
                    <IconButton
                      className="info-icon"
                      role="button"
                      aria-label="Information"
                      aria-describedby="tooltip-info"
                    >
                      <InfoIcon sx={{ color: "#ebe9e9" }} />
                    </IconButton>
                  </Tooltip>
                </div>
              }
              className={"form-control"}
              role="presentation"
              aria-hidden={true}
            />
          </RadioGroup>
        </FormControl>
      </div>
    </div>
  );
};
