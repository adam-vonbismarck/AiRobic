import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { renderWorkoutDetails } from "./workoutDetails";
import { Workout } from "./types";
import { useEffect, useState } from "react";
import "../../styling/Calendar.css";
import { AnimatePresence, motion } from "framer-motion";

const WorkoutCalendar: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [workoutDetails, setWorkoutDetails] = useState<Workout[]>([]);

  const fetchWorkouts = async (username: string) => {
    const response = await fetch(
      `http://localhost:3235/getuserworkouts?username=${username}`
    );
    const data = await response.json();
    const workouts: Workout[] = [];

    data.message.days.forEach((day: any, dayIndex: number) => {
      let workoutsNumber = 1;
      day.workouts.forEach((workout: any, workoutIndex: number) => {
        workouts.push({
          workoutsNumber: workoutsNumber++,
          dayNumber: dayIndex + 1,
          date: day.date,
          completed: workout.completed,
          RPE: workout.rpe,
          time: workout.time,
          workout: workout.workout,
        });
      });
    });

    return workouts;
  };

  const [workouts, setWorkouts] = useState<Workout[]>([]);

  useEffect(() => {
    fetchWorkouts("111132690994178564729").then((workouts) => {
      setWorkouts(workouts);
    });
  }, []);

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
