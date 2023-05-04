import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { renderWorkoutDetails } from "./workoutDetails";
import { Workout } from "./types";
import { useState } from "react";
import "../../styling/Calendar.css";
import { AnimatePresence, motion } from "framer-motion";

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

  return (
    <motion.div
      className="workout-calendar"
      role="application"
      aria-label="Calendar of workout events"
      initial={{ opacity: 0, x: 70 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ delay: 0.0, duration: 0.5 }}
    >
      <AnimatePresence mode={"wait"}>
        {selectedDate ? (
          <motion.div
            key="popup"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.2 }}
          >
            {renderWorkoutDetails({
              selectedDate,
              workoutDetails,
              setWorkoutDetails,
              workouts,
              setWorkouts,
              closeFullscreen,
            })}
          </motion.div>
        ) : (
          <motion.div
            key="calendar"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.2 }}
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
          </motion.div>
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default WorkoutCalendar;
