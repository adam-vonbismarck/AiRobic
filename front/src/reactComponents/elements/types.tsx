export interface Workout {
  workoutsNumber: number;
  dayNumber: number;
  date: string;
  RPE?: number;
  distance?: number;
  split?: string;
  time: number;
  workout: string;
}

export interface Day {
  date: string;
  day: string;
  numberOfWorkouts: number;
  type: string;
  workouts?: {
    time: number;
    workout: string;
    data?: {
      distance?: number;
      split?: string;
      rpe?: number;
    };
  }[];
}
