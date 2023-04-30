import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { renderWorkoutDetails } from "./workoutDetails";
import { Workout } from "./types";
import { useState } from "react";
import "../../styling/Calendar.css";

const WorkoutCalendar: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [workoutDetails, setWorkoutDetails] = useState<Workout[]>([]);

  const [workouts, setWorkouts] = useState<Workout[]>([
    {
      title: "Endurance Row",
      date: "2023-04-21",
      duration: 30,
      description:
        "30-minute row at a steady pace to build endurance at a low HR sdfgsdfgds",
      caloriesBurned: 300,
    },
    {
      title: "Interval Row",
      date: "2023-04-21",
      duration: 45,
      description:
        "45-minute row with alternating high-intensity and recovery intervals",
      caloriesBurned: 450,
    },
    {
      title: "Technique Row",
      date: "2023-04-22",
      duration: 60,
      description:
        "60-minute row with emphasis on proper rowing technique and form",
      caloriesBurned: 200,
    },
    {
      title: "Technique Row",
      date: "2023-04-22",
      duration: 60,
      description:
        "60-minute row with emphasis on proper rowing technique and form",
      caloriesBurned: 200,
    },
    {
      title: "Power Row",
      date: "2023-04-23",
      duration: 90,
      description: "90-minute row with emphasis on building power and strength",
      caloriesBurned: 500,
    },
    {
      title: "Endurance Row",
      date: "2023-04-23",
      duration: 90,
      description: "30-minute row with emphasis on building power and strength",
      caloriesBurned: 500,
    },
  ]);

  /**
   * Handles the selection of a date on the calendar.
   * @param selectInfo The information about the selected date.
   */
  const handleDateSelect = (selectInfo: {
    view: { calendar: any };
    startStr: React.SetStateAction<string | null>;
  }) => {
    let calendarApi = selectInfo.view.calendar;
    setSelectedDate(selectInfo.startStr);
    setWorkoutDetails(
      workouts.filter((workout) => workout.date === selectInfo.startStr)
    );
    calendarApi.unselect();
  };

  /**
   * Closes the fullscreen workout details view.
   */
  const closeFullscreen = () => {
    setSelectedDate(null);
  };

  if (!selectedDate) {
    return (
      <div
        className="workout-calendar"
        role="application"
        aria-label="Calendar of workout events"
      >
        <FullCalendar
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          headerToolbar={{
            left: "prev,next",
            center: "title",
            right: "today",
          }}
          initialView="dayGridMonth"
          // editable={true}
          selectable={true}
          selectMirror={true}
          dayMaxEvents={true}
          firstDay={1}
          events={workouts}
          select={handleDateSelect}
          aria-label="Interactive calendar of workout events"
          aria-roledescription="Calendar"
          aria-multiselectable={false}
        />
      </div>
    );
  } else {
    return (
      <div>
        {renderWorkoutDetails({
          selectedDate,
          workoutDetails,
          setWorkoutDetails,
          workouts,
          setWorkouts,
          closeFullscreen,
        })}
      </div>
    );
  }
};

export default WorkoutCalendar;
