# GenerateGraphLikePlan Class Documentation

## Table of Contents

1. [Introduction](#introduction)
2. [Class Overview](#class-overview)
3. [Method: `generate`](#method-generate)
4. [Method: `generateWeek`](#method-generateweek)


## 1. Introduction

This document provides internal code documentation for the `GenerateGraphLikePlan` class, explaining its functionality and the algorithms used in its methods.


## 2. Class Overview

The `GenerateGraphLikePlan` class constructs a workout schedule based on specified constraints and a variable Markov model.  It takes parameters defining the desired workout plan (start and end dates, workout types, intensity percentages, and total time) and produces a `Schedule` object. The schedule generation process carefully accounts for the total workout time constraint and ensures Sundays are always rest days.  The class uses a `VariableModelBuilder` to create a Markov model representing transitions between workout types.  The schedule is built week by week, with special handling for the first and last weeks to properly account for partial weeks and the requirement for Sundays to be rest days.


## 3. Method: `generate`

This method is the main entry point for generating a workout schedule.

**Signature:**

```java
public Schedule generate(
    int minutes,
    LocalDate startDate,
    LocalDate endDate,
    Set<Workout> highIntensityLabels,
    Set<Workout> lowIntensityLabels,
    double highIntensityPercent)
    throws IOException, InvalidDistributionException, InvalidScheduleException,
        NoWorkoutTypeException, FormatterFailureException, InvalidDatesException
```

**Parameters:**

| Parameter             | Type             | Description                                                                                                         |
|----------------------|-------------------|---------------------------------------------------------------------------------------------------------------------|
| `minutes`            | `int`             | Total workout time in minutes.                                                                                         |
| `startDate`          | `LocalDate`       | Start date of the workout plan.                                                                                       |
| `endDate`            | `LocalDate`       | End date of the workout plan.                                                                                         |
| `highIntensityLabels` | `Set<Workout>`   | Set of high-intensity workout types.                                                                                 |
| `lowIntensityLabels`  | `Set<Workout>`   | Set of low-intensity workout types.                                                                                 |
| `highIntensityPercent`| `double`          | Percentage of high-intensity workouts in the plan.                                                                 |

**Return Value:**

A `Schedule` object representing the generated workout plan.

**Exceptions:**

| Exception                     | Description                                                                                                                                |
|---------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| `IOException`                   | Problems reading workout files.                                                                                                                |
| `InvalidDistributionException` | Invalid distribution in the underlying Markov model.                                                                                          |
| `InvalidScheduleException`      | Problems generating the schedule (e.g., constructing `Days`).                                                                                |
| `NoWorkoutTypeException`        | Issues with workout type sets (null, empty, or containing null `Workout` objects).                                                            |
| `FormatterFailureException`     | Problems converting Markov model emissions into a usable format.                                                                              |
| `InvalidDatesException`        | Start date is not before the end date.                                                                                                   |


**Algorithm:**

1. **Input Validation:** Checks if `startDate` is before `endDate`. Throws `InvalidDatesException` if not.
2. **Model Building:** Uses `VariableModelBuilder` and `RowingWorkoutByName` to construct a `MarkovModel` based on the provided workout types and intensity percentages.
3. **Schedule Generation:**  The core logic divides the schedule into weeks, handling the first and last weeks as special cases to ensure Sundays are rest days. The process iterates through weeks, using `generateWeek` to create each week's schedule.
4. **Week Aggregation:**  Combines the generated weeks into a final `Schedule` object.  The returned schedule designates the first or second week as the representative week (depending on the number of weeks generated).

## 4. Method: `generateWeek`

This method generates a single week of the workout schedule using the provided Markov model.

**Signature:**

```java
private Week generateWeek(int minutes, LocalDate startDay, LocalDate endDay, MarkovModel model)
    throws InvalidDistributionException, FormatterFailureException, InvalidScheduleException
```

**Parameters:**

| Parameter    | Type             | Description                                                                     |
|--------------|-------------------|---------------------------------------------------------------------------------|
| `minutes`    | `int`             | Approximate total workout time for the week (in minutes).                       |
| `startDay`   | `LocalDate`       | Start date of the week.                                                         |
| `endDay`     | `LocalDate`       | End date of the week.                                                           |
| `model`      | `MarkovModel`     | The Markov model used to generate workout suggestions.                           |

**Return Value:**

A `Week` object representing the generated week's schedule.

**Exceptions:**

| Exception                     | Description                                                                      |
|---------------------------------|----------------------------------------------------------------------------------|
| `InvalidDistributionException` | Invalid distribution in the Markov model.                                          |
| `FormatterFailureException`     | Problems formatting Markov model emissions.                                     |
| `InvalidScheduleException`      | Problems constructing `Schedule` components (e.g., `Days`).                     |


**Algorithm:**

1. **Day Initialization:** Creates a list of `Day` objects for each day of the week, initially without any workouts.
2. **Workout Generation:** Generates a list of potential `Emission` objects (workout suggestions) from the `MarkovModel` using `generateFormattedEmissions`.  The number of potential workouts is limited by `MAX_WORKOUTS`.
3. **Workout Assignment (Minutes Constraint):** Iterates through the potential workouts, adding them to the week's schedule until the `minutes` constraint is met.
4. **Workout Distribution:** Distributes the remaining workouts across the days of the week in a way that attempts to balance the workout load.  This distribution process uses a cumulative counter to distribute workouts proportionally across the days of the week, aiming for a relatively even distribution.  This algorithm resembles the logic found in the `DistributeWorkouts` method from the `ScheduleBuilder` class (though the implementation details are not directly shown here).
5. **Week Construction:** Combines the days with their assigned workouts into a `Week` object.


The algorithms used in both methods ensure a feasible workout schedule is generated while adhering to the specified constraints and respecting the designated rest day (Sunday).  The special casing for short weeks and the handling of partial weeks at the beginning and end of the schedule adds robustness to the schedule generation process.
