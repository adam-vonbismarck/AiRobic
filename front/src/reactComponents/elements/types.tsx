export interface Workout {
  title: string;
  date: string;
  duration: number;
  description: string;
  caloriesBurned: number;
  distance?: number;
  perceivedEffort?: number;
  avgSplit?: string;
}
