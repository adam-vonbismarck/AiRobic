import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { renderWorkoutDetails } from "./workoutDetails";
import { Day, Workout } from "./types";
import { useEffect, useState } from "react";
import "../../styling/Calendar.css";
import { AnimatePresence, motion } from "framer-motion";
import moment from "moment";

const WorkoutCalendar: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [workoutDetails, setWorkoutDetails] = useState<Workout[]>([]);

  const [events, setEvents] = useState<Workout[]>([]);

  const fetchWorkouts = async (): Promise<Workout[]> => {
    const response = await fetch(
      `https://cs32airobic-default-rtdb.firebaseio.com/users/${localStorage.getItem(
        "userID"
      )}/schedule.json`
    );
    const data: { days: Day[] } = await response.json();
    const workouts: Workout[] = [];

    data.days.forEach((day: Day, dayIndex: number) => {
      let workoutsNumber = 1;
      if (day.workouts) {
        day.workouts.forEach((workout, workoutIndex: number) => {
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
      }
    });

    return workouts;
  };

  const [workouts, setWorkouts] = useState<Workout[]>([]);

  useEffect(() => {
    fetchWorkouts().then((workouts) => {
      setWorkouts(workouts);
      setEvents(createEventsArray(workouts));
    });
  }, []);

  function createEventsArray(data: Workout[]) {
    const eventsArray: Workout[] = data.map((item) => {
      const event = {
        title: `Workout ${item.workoutsNumber} for day ${item.dayNumber}`,
        date: moment(item.date, "MM-DD-YYYY").format("yyyy-MM-DD"),
        time: item.time,
        completed: item.completed,
        RPE: item.RPE,
        workout: item.workout,
        dayNumber: item.dayNumber,
        workoutsNumber: item.workoutsNumber,
      };

      return event;
    });

    console.log(eventsArray);
    return eventsArray;
  }

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
      events.filter((workout) => workout.date === selectInfo.startStr)
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
      className="workout-calendar-container-animated"
      role="application"
      aria-label="Calendar of workout events"
      initial={{ opacity: 0, x: 70 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ delay: 0.0, duration: 0.5 }}
    >
      <AnimatePresence mode={"wait"}>
        {selectedDate ? (
          <div style={{ width: "50%" }}>
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
                events,
                setWorkouts,
                closeFullscreen,
              })}
            </motion.div>
          </div>
        ) : (
          <motion.div
            className="workout-calendar"
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
              events={events}
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
