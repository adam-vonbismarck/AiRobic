import React, { useEffect } from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { Link } from "react-router-dom";
import { useGoogleLogin } from "@react-oauth/google";
import { login } from "../GoogleLogin";
import { useNavigate } from "react-router-dom";
import { aD } from "@fullcalendar/core/internal-common";

/**
 * https://www.youtube.com/watch?v=roxC8SMs7HU
 * google oauth for react 2023
 */



function Register() {
  
  return (
    <div className="register-window">
      <div className="sign-in-button">
        <GoogleOAuthProvider clientId="20770065062-amdbsjkao5gag2g7m0b7o4pn411akg80.apps.googleusercontent.com">
          ...
          <GoogleLogin
            onSuccess={(credentialResponse) => {
              var cred = getCredentialResponse(credentialResponse.credential)
              if (checkUser(cred.sub)) {
                addUser(cred.sub)
                Login(cred)
              }
              else {
                console.log("Registration failed")
              }
            }}
            onError={() => {
              console.log("Google authentication Failed");
            }}
          />
          {/* <button onClick={() => login()}       //TODO invalid hook ?????
        className="sign-in-button">               
          Sign in with Google 🚀{" "}
          
        </button> */}
        </GoogleOAuthProvider>
      </div>
      <LoggedOutMenu description={"register page"} />
    </div>
  );
}

function getCredentialResponse(credential: string | undefined): LoginResponse {
fetch(
  "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + credential)
  .then((response: Response) => response.json())
  .then((loginToken) => {
    if (!isLoginResponse(loginToken)) {
      console.log("Login Failed");
    } else {
      return loginToken
    }
  });
  return errLoginResponse;
}

function addUser(userID: string): boolean {
  fetch("localhost:3235/adduser?username="+userID).then((response: Response) => response.json()).then((addResponse) => {
    if (!isAddUserResponse(addResponse)) {
      console.log("Error adding user")   
      return false;            
    } else {
      if (addResponse.result == "success") {
        return true;
      }
      else {
        return false;
      }
    }             
  });
  return false;
}

function checkUser(userID: string): boolean {
  fetch("localhost:3235/checkuser?username=" + userID)
    .then((userResponse: Response) => userResponse.json())
    .then((checkUser) => {
      if (!isCheckUserResponse(checkUser)) {
        console.log("Invalid check user response");
      } else {
        if (checkUser.message == "True") {
          console.log("User checked successfully does not exist")
          console.log("Proceed to register user")
          return true;
        }
        else {
          console.log("User already exists or bad request")
          return false;
        }
      }
    });
  return false;
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
  if (!("result" in rjson) ||("message" in rjson)) return false;
  return true;
}

function isAddUserResponse(rjson: any): rjson is AddUserResponse {
  if (!("result" in rjson) || "message" in rjson) return false;
  return true;
}

export default Register;
