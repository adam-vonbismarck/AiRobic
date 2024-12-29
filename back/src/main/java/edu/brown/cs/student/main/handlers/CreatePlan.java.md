# CreatePlan Handler Documentation

## Table of Contents

* [1. Overview](#1-overview)
* [2. Request Parameters](#2-request-parameters)
* [3. Error Handling](#3-error-handling)
* [4. Algorithm Details](#4-algorithm-details)
    * [4.1. Model Selection](#4.1-model-selection)
    * [4.2. `GenerateLinearPlan`](#4.2-generateLinearPlan)
    * [4.3. `GenerateGraphLikePlan`](#4.3-generateGraphLikePlan)
* [5. Database Interaction](#5-database-interaction)


## 1. Overview

The `CreatePlan` handler is a `spark.Route` implementation responsible for generating and storing workout plans for users. It receives user input, performs validation, selects an appropriate plan generation algorithm, and updates the user's database entry with the generated schedule.


## 2. Request Parameters

The handler expects the following query parameters in the HTTP request:

| Parameter       | Type    | Description                                         | Required |
|-----------------|---------|-----------------------------------------------------|----------|
| `username`      | String  | User's username.                                    | Yes      |
| `sport`         | String  | The sport the workout plan is for.                  | Yes      |
| `startDate`     | String  | Start date of the workout plan (yyyy-MM-dd format). | Yes      |
| `endDate`       | String  | End date of the workout plan (yyyy-MM-dd format).   | Yes      |
| `hoursPerWeek`  | String  | Total weekly workout time in hours.                 | Yes      |
| `model`         | String  | The model to use for plan generation ("model1", "model2", "model3"). | Yes      |
| `goal`          | String  | The user's goal (required for "model3").             | Conditional (Yes for model3, No otherwise) |


## 3. Error Handling

The handler performs comprehensive error checking at various stages:

* **Missing parameters:** If any required parameter is missing, it returns an "error_bad_request" response with an appropriate error message.
* **Invalid date format:** If `startDate` or `endDate` are not in the correct format (yyyy-MM-dd), it returns an "error_bad_request" response.
* **Invalid `hoursPerWeek` format:** If `hoursPerWeek` cannot be parsed as an integer, it returns an "error_bad_request" response.
* **Invalid `goal` for model3:** If `goal` is not valid for model3, it returns an error.
* **Internal errors:** Any exceptions during plan generation or database interaction are caught, logged, and returned as "error_bad_request" responses with detailed error messages.


## 4. Algorithm Details

### 4.1. Model Selection

The handler uses a `switch` statement to select the appropriate plan generation algorithm based on the `model` parameter:

* **"model1"**: Uses `GenerateLinearPlan` to create a classic linear workout schedule.
* **"model2"**: Uses `GenerateGraphLikePlan` to create a workout schedule that varies workout intensity.
* **"model3"**: Uses `GenerateLinearPlan` to create a goal-oriented workout schedule based on specified goal (`goal` parameter).  It handles error checking for invalid goals (Workout.NONE or Workout.UT_2).
* **Default**: Returns an "error_bad_request" for invalid model parameters.


### 4.2. `GenerateLinearPlan`

This algorithm (presumably in a separate class) generates a linear workout plan.  The specifics of the algorithm are not provided in the given code snippet but the `generate` method takes the following parameters:

| Parameter       | Type             | Description                                     |
|-----------------|------------------|-------------------------------------------------|
| `parsedMinutes` | int              | Total weekly workout minutes.                   |
| `parsedStart`   | `LocalDate`      | Start date of the plan.                         |
| `parsedEnd`     | `LocalDate`      | End date of the plan.                           |
| `primaryWorkout`| `Workout`        | Main type of workout.                           |
| `secondaryWorkout`| `Workout`       | Secondary type of workout (e.g., for rest or active recovery).|
| `restProportion`| double           | Proportion of rest or secondary workout.       |

The algorithm likely distributes the total workout minutes across the specified period, potentially considering rest days or varying workout intensity over time.


### 4.3. `GenerateGraphLikePlan`

This algorithm (presumably in a separate class) generates a workout schedule that varies the intensity and types of workouts based on sets of high and low intensity workouts.  The `generate` method likely takes similar parameters to `GenerateLinearPlan`, but instead of a single `primaryWorkout`, it uses sets (`high` and `low`) to define workout types.


## 5. Database Interaction

The handler interacts with the database using the `DatabaseCommands` class.  It updates the user's data using:

* `update("{\\"sport\\":\\"" + sport + "\\"}", "users/" + username);`: Updates the user's sport information.
* `update(Serializer.serializeSchedule(built.flatten()), "users/" + username + "/schedule");`: Updates the user's schedule with the generated plan.  In case of `model1`, a `put` operation is used instead of `update`.  This suggests that a `put` operation creates a new entry in the database, whereas an `update` operation modifies an existing one.  The specifics of the database operations (e.g., specific database technology and query structures) are not detailed in the provided code.
