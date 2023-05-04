import React, {useEffect, useState} from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { useNavigate } from "react-router-dom";
import {Parallax} from "react-parallax";
import {Alert} from "@mui/material";

/**
 * https://www.youtube.com/watch?v=roxC8SMs7HU
 * google oauth for react 2023
 */



function Register() {
  const [submitIssue, setSubmitIssue] = useState(false);
  const [submitError, setSubmitError] = useState("");

  return (
      <Parallax
          bgImage={"/assets/images/GettyImages-1170269618-2.jpg"}
          strength={100}
          aria-label="Parallax image with register display page"
      >
    <div className="register-window">
      <div className="sign-in-button">
        <GoogleOAuthProvider clientId="20770065062-amdbsjkao5gag2g7m0b7o4pn411akg80.apps.googleusercontent.com">
          <h2>Register Below</h2>
          <GoogleLogin
              shape={"rectangular"}
              size={"large"}
              theme={"filled_black"}
              width={"500"}
              text={"signup_with"}

            onSuccess={(credentialResponse) => {

              getCredentialResponse(credentialResponse.credential).then((loginToken) => {
                checkUser(loginToken.sub).then((isNewUser) => {
                  console.log("user checked")
                  if (isNewUser) {
                    addUser(loginToken.sub).then((userAdded)=>{
                      console.log("user add")
                      if (userAdded) {
                        console.log("User added successfully")
                        Login(loginToken)
                      }
                      else {
                        console.log("Failed to add user")
                        setSubmitIssue(true)
                        setSubmitError("Error adding user to our server, Please try again")
                      }
                    })
                  } else {
                    console.log("User already exists")
                    setSubmitIssue(true)
                    setSubmitError("This email is already registered, please Sign in instead")
                  }
                }).catch(err => {
                  setSubmitError("Error contacting our server, Please try again")
                  setSubmitIssue(true)
                  console.log("error checking user DISPLAY ERROR MESSAGE")
                })
              })
            }

          }
            onError={() => {
              console.log("Google authentication Failed");
              setSubmitIssue(true)
              setSubmitError("Google authentication failed, please try again")
            }}
          />
          {/* <button onClick={() => login()}       //TODO invalid hook ?????
        className="sign-in-button">               
          Sign in with Google 🚀{" "}
          
        </button> */}
        </GoogleOAuthProvider>
      </div>
      <LoggedOutMenu description={"Register page"} />
    </div>
        {submitIssue && (
            <div className="error-row">
              <div className="form-error-message" aria-label="Error message">
                <Alert
                    severity="error"
                    variant="filled"
                    sx={{ fontFamily: "Muli" }}
                    aria-label="Error: Workout plan submission unsuccessful"
                >
                  {submitError}
                </Alert>
              </div>
            </div>
        )}
      </Parallax>
  );
}

function getCredentialResponse(credential: string | undefined): Promise<LoginResponse> {
return new Promise((resolve, reject) => {

fetch("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + credential)
  .then((response: Response) => response.json())
  .then((loginToken) => {
    if (!isLoginResponse(loginToken)) {
      console.log("Login Failed");
      reject(errLoginResponse)
    } else {
      console.log(loginToken);
      resolve(loginToken)
    }
  }).catch(err => reject(errLoginResponse));
})

  
}

function addUser(userID: string): Promise<boolean> {
  return new Promise((resolve, reject) => {
    fetch("http://localhost:3235/adduser?username=" + userID)
      .then((response: Response) => response.json())
      .then((addResponse) => {
        if (isAddUserResponse(addResponse) && addResponse.result == "success") {
          resolve(true)
        }
        else {
          reject(false)
        }
      }).catch(err => reject(false));
  })
}

function checkUser(userID: string): Promise<boolean> {
  return new Promise((resolve, reject) => {
    fetch("http://localhost:3235/checkuser?username=" + userID)
      .then((userResponse: Response) => userResponse.json())
      .then((checkUser) => {
        if (!isCheckUserResponse(checkUser)) {
          console.log(checkUser)
          console.log("Invalid check user response");
          reject(false)
        } else {
          if (checkUser.message == "False") {
            console.log("User checked successfully does not exist");
            console.log("Proceed to register user");
            resolve(true);
          } else {
            console.log(checkUser)
            console.log("User already exists or bad request");
            reject(false);
          }
        }
      }).catch(err => reject(false));
  })
}

function Login(loginToken: LoginResponse) {
  const navigate = useNavigate();
  localStorage.setItem("userID", loginToken.sub);
  console.log(loginToken);
  localStorage.setItem("givenName", loginToken.given_name); //Can send to back end otherwise and fetch from there
  localStorage.setItem("loggedIn", "true");
  navigate("/", { replace: true });
  console.log(localStorage);
}


interface LoginResponse {
  sub: string;
  email: string;
  family_name: string;
  given_name: string;
}

const errLoginResponse = {
  sub: "ERROR",
  email: "ERROR",
  family_name: "ERROR",
  given_name: "ERROR"
}

interface CheckUserResponse {
  result: string;
  message: string;
}

interface AddUserResponse{
  result: string;
  message: string;
}

function isLoginResponse(rjson: any): rjson is LoginResponse {
  if (!("sub" in rjson) || !("email" in rjson)) return false;
  return true;
}

function isCheckUserResponse(rjson: any): rjson is CheckUserResponse {
  // if (!("result" in rjson)) {
  //   console.log("no result")
  // }
  // if (!("message" in rjson)) {
  //   console.log("no message")
  // }
  if (!("result" in rjson) || !("message" in rjson)) return false;
  return true;
}

function isAddUserResponse(rjson: any): rjson is AddUserResponse {
  if (!("result" in rjson) || !("message" in rjson)) return false;
  return true;
}

export default Register;
