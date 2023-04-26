import React from "react";
import { Workout } from "./types";

interface Props {
    selectedDate: string | null;
    workoutDetails: Workout[];
    setWorkoutDetails: React.Dispatch<React.SetStateAction<Workout[]>>;
    workouts: Workout[];
    setWorkouts: React.Dispatch<React.SetStateAction<Workout[]>>;
    closeFullscreen: () => void;
}

export const renderWorkoutDetails = ({
                                         selectedDate,
                                         workoutDetails,
                                         setWorkoutDetails,
                                         workouts,
                                         setWorkouts,
                                         closeFullscreen,
                                     }: Props) => {
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
