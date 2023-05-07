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

This project is a web application that allows for users to receive a computer generated workout plan tailored to suit their fitness goals. The user can create and account and upon signing in will have all their data stored such that they can continously check and update the information In its current state the application will give indoor rowing workouts only but can be expanded to other sports in the future.

The front end of the application is built with React and uses several libraries to make a sleek user interface and allows the user to authenticate themselves with Google. Once logged in the user can create a new workout schedule based on the hours per week they wish to train, their mode of training - goal, variable and linear - and the start and end date of their training schedule. Once a schedule has been created it will be saved and they can view it on a calendar at any point in the future once logged in.

The back end of the application handles api requests from the front end which in turn stores a users data for future access using Firebase. It also is responsible for generating the workout plan using a hidden markov modelbased which creates a personalized schedule based on the users desired specifications.

**Github repo:**
[https://github.com/cs0320-s2023/cs32-Final-Project-avonbism-gmilovac-mwinter02-cbaker37]


## Project Description

The project aims to simplify the taks of creating a workout schedule for users by creating a computer generated personalized training plan. With intention to be expandable to more sports in the future, the project is indoor rowing specific and draws from the collective 20+ years of rowing experience from the team members to ensure end users are given high quality and effective training plans. The end users will be able to create new training programs tailored to their inputted specifications and will be able to view their workouts on a calendar for any given day. Their information will be stored such that they can access their workout plan at any time and also can create a new plan if their fitness goals change.


## Design Choices

**Adam stuff**
Explain the relationships between classes/interfaces.
Discuss any specific data structures you used, why you created it, and other high level explanations.
Runtime/ space optimizations you made (if applicable).

**Colin stuff**
Explain the relationships between classes/interfaces.
Discuss any specific data structures you used, why you created it, and other high level explanations.
Runtime/ space optimizations you made (if applicable).

**Gordan stuff**
Explain the relationships between classes/interfaces.
Discuss any specific data structures you used, why you created it, and other high level explanations.
Runtime/ space optimizations you made (if applicable).

**Account Management:**
We used Google Oauth for authorization as it allowed for secure account creation and removed the need to have users to create or store any secure information such passwords on our end.
Once authenticated using google's api the user's unique id is returned and their data is stored via firebase under this unique id.
Registration required the user to not have an account already in our database and once created, the user can sign in at any time to access their workout information.


## Team members and contributions:
**Colin Baker** (*cbaker20*): Back end - Workout generation and markov models
**Marcus Winter** (*mwinter20*): Front end - Authentication and account management
**Gordan Milovac** (*gmilovac*): Back end - API call handlers and database management
**Adam von Bismarck** (*avonbism*): Front end - Website styling and workout display

**Time allocated:** 100+ hours

## Errors / bugs
None found

## Tests
Explain the testing suites that you implemented for your program and how each test ensures that a part of the program works. 

## How to
**Build and run your program**
-> Run Server.java found in /back/src/main/java/edu/brown/cs/student/main/server to start the back end  and then open the front 
-> Using VSCode or your IDE of choice and execute the command "npm install" followed by "npm run dev"
-> Navigate to the provided url in your browser.
You can now use our web app! Click on register button on the left side to get started

**Run the tests you wrote/were provided**
The front end testing classes are found in the directory front/src/testing
The back end testing classes are found in the directory back/src/test/java/edu/brown/cs/student





