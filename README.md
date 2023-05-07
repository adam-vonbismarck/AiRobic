# CS0320 Final Project

## Project Name: __*AiROBIC*__
```
    _    _ ____   ___  ____ ___ ____ 
   / \  (_)  _ \ / _ \| __ )_ _/ ___|
  / _ \ | | |_) | | | |  _ \| | |    
 / ___ \| |  _ <| |_| | |_) | | |___ 
/_/   \_\_|_| \_\\___/|____/___\____|
```

## Project details

This project is a web application that allows for users to receive a computer generated workout plan tailored to suit their fitness goals. The user can create and account and upon signing in will have all their data stored such that they can continously check and update the information In its current state the application will give indoor rowing workouts only but can be expanded to other sports in the future. <br />

The front end of the application is built with React and uses several libraries to make a sleek user interface and allows the user to authenticate themselves with Google. Once logged in the user can create a new workout schedule based on the hours per week they wish to train, their mode of training - goal, variable and linear - and the start and end date of their training schedule. Once a schedule has been created it will be saved and they can view it on a calendar at any point in the future once logged in.<br />

The back end of the application handles api requests from the front end which in turn stores a users data for future access using Firebase. It also is responsible for generating the workout plan using a hidden markov modelbased which creates a personalized schedule based on the users desired specifications.<br />

**Github repo:**<br />
[https://github.com/cs0320-s2023/cs32-Final-Project-avonbism-gmilovac-mwinter02-cbaker37]


## Project Description

The project aims to simplify the taks of creating a workout schedule for users by creating a computer generated personalized training plan. With intention to be expandable to more sports in the future, the project is indoor rowing specific and draws from the collective 20+ years of rowing experience from the team members to ensure end users are given high quality and effective training plans. The end users will be able to create new training programs tailored to their inputted specifications and will be able to view their workouts on a calendar for any given day. Their information will be stored such that they can access their workout plan at any time and also can create a new plan if their fitness goals change.


## Design Choices

**Workout display** <br />
The front end calendar display was created using a full-calendar react component. This allowed us to focus on styling without having to build a calendar from scratch. This calendar allowed us to import the workkouts from the database and convert them into an events array. This array coupd then be used to display information about the workouts on the calendar itself and could also be updated such as when the user wanted to save information about their workouts.

**Workout Plan Generation/Model Building** <br />
Explain the relationships between classes/interfaces.
Discuss any specific data structures you used, why you created it, and other high level explanations.
Runtime/ space optimizations you made (if applicable).

**Database/API handlers** <br />
We used Firebase Realtime Database to store our information. Firebase can take in information via Terminal commands, so the design choice was to create a class which would generate terminal commands for each action you can do for the Firebase. The methods allow reading and storing various information, formatted in Json file type. Each api call the frontend makes, there is a special class being called that takes in the query from the call and uses the specific database commands to work with it. One of the most complex ones is the CreatePlan() which generates the workout schedule using the Models and stores it in the Database. 

**Account Management:** <br />
We used Google Oauth for authorization as it allowed for secure account creation and removed the need to have users to create or store any secure information such passwords on our end.
Once authenticated using google's api the user's unique id is returned and their data is stored via firebase under this unique id.
Registration required the user to not have an account already in our database and once created, the user can sign in at any time to access their workout information.


## Team members and contributions:
**Colin Baker** (*cbaker20*): Back end - Workout generation and markov models <br />
**Marcus Winter** (*mwinter20*): Front end - Authentication and account management <br />
**Gordan Milovac** (*gmilovac*): Back end - API call handlers and database management <br />
**Adam von Bismarck** (*avonbism*): Front end - Website styling and workout display <br />

**Time allocated:** 100+ hours

## Errors / bugs
None found

## Tests
Every page was tested using ReactDOM testing and any possible user inputs were accounted for to ensure that no unexpected errors occurred.
Authentication methods were extensively tested to ensure no unexpected responses that may result in an incorrect login and we fuzzed the getCredentialResponse to prove that only a valid google response would result in a login.
Database unit tests ensured that database methods worked as expected and how the database responds to the calls.
API handlers were tested to ensure integration between front end api calls and database commands was error free, with each api method tested plus edgecases ensured no unexpected responses were given.
All models tested to ensure functionality and give correct response and random generation tests which tests efficiency of models. Also format tests to test specific formatting (date formatting, string formatting, etc) that we used throughout our code.

## How to
**Build and run your program**<br />
-> Run Server.java found in /back/src/main/java/edu/brown/cs/student/main/server to start the back end  and then open the front <br />
-> Using VSCode or your IDE of choice and execute the command "npm install" followed by "npm run dev" <br />
-> Navigate to the provided url in your browser.<br />
You can now use our web app! Click on register button on the left side to get started

**Run the tests you wrote/were provided**<br />
The front end testing classes are found in the directory front/src/testing<br />
The back end testing classes are found in the directory back/src/test/java/edu/brown/cs/student





