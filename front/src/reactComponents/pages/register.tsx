import React, { useEffect } from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { Link } from "react-router-dom";
import { useGoogleLogin } from "@react-oauth/google";
import { login } from "../GoogleLogin";





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
            console.log(credentialResponse);
            console.log("test")
          }}
          onError={() => {
            console.log("Login Failed");
          }}
          
        />
        {/* <button onClick={() => login()}       //TODO invalid hook ?????
        className="sign-in-button">               
          Sign in with Google 🚀{" "}
          
        </button> */}
      </GoogleOAuthProvider>
      </div>
      <LoggedOutMenu description={"register page"} />
      <h5>Register below.</h5>
    </div>
  );
}

{/* <GoogleLogin
  onSuccess={(credentialResponse) => {
    console.log(credentialResponse);
  }}
  onError={() => {
    console.log("Login Failed");
  }}
/>; */}

export default Register;
