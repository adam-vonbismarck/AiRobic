import React, { useEffect } from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { Link } from "react-router-dom";
import { useGoogleLogin } from "@react-oauth/google";



/**
 * https://www.youtube.com/watch?v=roxC8SMs7HU
 * google oauth for react 2023
 */

// const login = useGoogleLogin({
//   onSuccess: async(tokenResponse) => console.log(tokenResponse), //invalid hook call??
//   flow: "auth-code",
// });

function Register() {
  return (
    <div className="register-window">
      <GoogleOAuthProvider clientId="20770065062-amdbsjkao5gag2g7m0b7o4pn411akg80">
        ...
        <GoogleLogin
          onSuccess={(credentialResponse) => {
            console.log(credentialResponse);
          }}
          onError={() => {
            console.log("Login Failed");
          }}
        />
        {/* <button onClick={() => login()}
        className="sign-in-button">
          Sign in with Google 🚀{" "}
          
        </button> */}
        <button onClick={() => console.log("login")} className="sign-in-button">
          Sign in with Google 🚀{" "}
        </button>
        ;
      </GoogleOAuthProvider>
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
