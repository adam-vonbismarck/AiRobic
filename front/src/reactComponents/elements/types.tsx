export interface Workout {
  workoutsNumber: number;
  dayNumber: number;
  date: string;
  completed: boolean;
  RPE?: number;
  distance?: number;
  split?: string;
  time: number;
  workout: string;
}

export interface Day {
  date: string;
  intensity?: { intensity: string; length: number }[];
  name: string;
  numberOfWorkouts: number;
  type: string;
  workouts?: {
    completed: boolean;
    rpe?: number;
    time: number;
    workout: string;
  }[];
}
