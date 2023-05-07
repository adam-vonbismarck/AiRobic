import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { RenderWorkoutDetails } from "./workoutDetails";
import { Day, Workout } from "./types";
import { useEffect, useState } from "react";
import "../../styling/Calendar.css";
import { AnimatePresence, motion } from "framer-motion";
import moment from "moment";

const WorkoutCalendar: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [workoutDetails, setWorkoutDetails] = useState<Workout[]>([]);
  const [workouts, setWorkouts] = useState<Workout[]>([]);
  const [splitError, setSplitError] = useState<boolean>(false);
  const [distanceError, setDistanceError] = useState<boolean>(false);
  const [errorText, setErrorText] = useState<string>("");

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
            title: workout.title,
            workoutsNumber: workoutsNumber++,
            dayNumber: dayIndex + 1,
            date: day.date,
            RPE: workout.data?.rpe,
            distance: workout.data?.distance,
            split: workout.data?.split,
            time: workout.time,
            workout: workout.workout,
          });
        });
      }
    });

    return workouts;
  };

  useEffect(() => {
    fetchWorkouts().then((workouts) => {
      setWorkouts(createEventsArray(workouts));
      console.log("useeffect");
    });
  }, []);

  function createEventsArray(data: Workout[]) {
    const eventsArray: Workout[] = data.map((item) => {
      const event = {
        title: item.title,
        date: moment(item.date, "MM-DD-YYYY").format("yyyy-MM-DD"),
        time: item.time,
        RPE: item.RPE,
        distance: item.distance,
        split: item.split,
        workout: item.workout,
        dayNumber: item.dayNumber,
        workoutsNumber: item.workoutsNumber,
      };

      return event;
    });

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
      className="workout-calendar-container-animated"
      role="application"
      aria-label="Calendar of workout events"
      initial={{ opacity: 0, x: 70 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ delay: 0.0, duration: 0.5 }}
    >
      <AnimatePresence mode={"wait"}>
        {selectedDate ? (
          <div style={{ width: "70%" }}>
            <motion.div
              key="popup"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              transition={{ duration: 0.2 }}
            >
              {RenderWorkoutDetails({
                selectedDate,
                workoutDetails,
                setWorkoutDetails,
                workouts,
                setWorkouts,
                closeFullscreen,
                splitError,
                setSplitError,
                distanceError,
                setDistanceError,
                errorText,
                setErrorText,
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
