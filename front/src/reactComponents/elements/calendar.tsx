// WorkoutCalendar.tsx
import React, { useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
// import "@fullcalendar/core/main.css";
// import "@fullcalendar/daygrid/main.css";
// import "./WorkoutCalendar.css";

interface Workout {
  title: string;
  date: string;
  duration: number;
  description: string;
  caloriesBurned: number;
  distance?: number;
  perceivedEffort?: number;
}

const WorkoutCalendar: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [workoutDetails, setWorkoutDetails] = useState<Workout[]>([]);

  const [workouts, setWorkouts] = useState<Workout[]>([
    {
      title: "Endurance Row",
      date: "2023-04-21",
      duration: 30,
      description: "30-minute row at a steady pace to build endurance",
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
      title: "Power Row",
      date: "2023-04-23",
      duration: 90,
      description: "90-minute row with emphasis on building power and strength",
      caloriesBurned: 500,
    },
  ]);

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

  const closeFullscreen = () => {
    setSelectedDate(null);
  };

  const renderWorkoutDetails = () => {
    if (!selectedDate) {
      return null;
    }

    const workoutsForSelectedDate = workoutDetails.filter(
      (workout) => workout.date === selectedDate
    );

    const updateWorkout = (updatedWorkout: Workout, index: number) => {
      const updatedWorkoutDetails = [...workoutDetails];
      updatedWorkoutDetails[index] = updatedWorkout;
      setWorkoutDetails(updatedWorkoutDetails);

      // Update workouts
      const workoutIndex = workouts.findIndex(
        (w) =>
          w.date === updatedWorkout.date && w.title === updatedWorkout.title
      );
      if (workoutIndex > -1) {
        const updatedWorkouts = [...workouts];
        updatedWorkouts[workoutIndex] = updatedWorkout;
        setWorkouts(updatedWorkouts);
      }
    };

    return (
      <div className="workout-details">
        <h2>{selectedDate}</h2>
        {workoutsForSelectedDate.map((workout, index) => (
          <div key={index}>
            <h3>{workout.title}</h3>
            <p>
              <strong>Duration:</strong> {workout.duration} minutes
            </p>
            <p>
              <strong>Description:</strong> {workout.description}
            </p>
            <p>
              <strong>Calories Burned:</strong> {workout.caloriesBurned}
            </p>
            <label>
              Distance (meters):
              <input
                type="number"
                step="1"
                min="0"
                value={workout.distance || ""}
                onChange={(event) => {
                  const distance = parseInt(event.target.value);
                  const updatedWorkout = { ...workout, distance };
                  updateWorkout(updatedWorkout, index);
                }}
              />
            </label>
            <label>
              Perceived Effort (1-10):
              <input
                type="range"
                min="1"
                max="10"
                value={workout.perceivedEffort || 5}
                onChange={(event) => {
                  const perceivedEffort = parseInt(event.target.value);
                  const updatedWorkout = { ...workout, perceivedEffort };
                  updateWorkout(updatedWorkout, index);
                }}
              />
              {workout.perceivedEffort}
            </label>
          </div>
        ))}
        <button onClick={closeFullscreen}>Close</button>
      </div>
    );
  };

  return (
    <div className="workout-calendar">
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
      />
      {renderWorkoutDetails()}
    </div>
  );
};

export default WorkoutCalendar;
