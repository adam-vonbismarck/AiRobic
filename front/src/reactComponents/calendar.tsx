// WorkoutCalendar.tsx
import React, { useState } from "react";
import FullCalendar from "@fullcalendar/react";
import DateSelectArg from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
// import "@fullcalendar/core/main.css";
// import "@fullcalendar/daygrid/main.css";
// import "./WorkoutCalendar.css";

interface Workout {
  title: string;
  date: string;
}

const WorkoutCalendar: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [workoutDetails, setWorkoutDetails] = useState<Workout[]>([]);

  const workouts: Workout[] = [
    { title: "Cardio", date: "2023-04-21" },
    { title: "Yoga", date: "2023-04-22" },
    { title: "Strength Training", date: "2023-04-23" },
  ];

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

    return (
      <div className="workout-details">
        <h2>{selectedDate}</h2>
        {workoutDetails.map((workout, index) => (
          <div key={index}>
            <h3>{workout.title}</h3>
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
          left: "prev,next today",
          center: "title",
          right: "dayGridMonth,timeGridWeek,timeGridDay",
        }}
        initialView="dayGridMonth"
        editable={true}
        selectable={true}
        selectMirror={true}
        dayMaxEvents={true}
        events={workouts}
        select={handleDateSelect}
      />
      {renderWorkoutDetails()}
    </div>
  );
};

export default WorkoutCalendar;
