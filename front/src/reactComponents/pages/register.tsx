import React, { useEffect } from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { Link } from "react-router-dom";
import { useGoogleLogin } from "@react-oauth/google";
import { login } from "../GoogleLogin";
import { useNavigate } from "react-router-dom";

/**
 * https://www.youtube.com/watch?v=roxC8SMs7HU
 * google oauth for react 2023
 */



function Register() {
  const navigate = useNavigate();
  return (
    <div className="register-window">
      <div className="sign-in-button">
        <GoogleOAuthProvider clientId="20770065062-amdbsjkao5gag2g7m0b7o4pn411akg80.apps.googleusercontent.com">
          ...
          <GoogleLogin
            onSuccess={(credentialResponse) => {
              fetch("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token="+credentialResponse.credential)
              .then((response: Response) => response.json())
              .then((loginToken) => {
                if (!isLoginResponse(loginToken)) {
                  console.log("Login Failed")
                } else {
                  fetch("")

                  localStorage.setItem("userID", loginToken.sub);
                  console.log(loginToken)
                  localStorage.setItem("givenName", loginToken.given_name);//Can send to back end otherwise and fetch from there
                  localStorage.setItem("loggedIn","true")
                  navigate('/', { replace: true });

                console.log(localStorage);
        }
      });

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
    </div>
  );
}


interface LoginResponse {
  sub: string;
  email: string;
  family_name: string;
  given_name: string;
}

function isLoginResponse(rjson: any): rjson is LoginResponse {
  if (!("sub" in rjson) || !("email" in rjson)) return false;
  return true;
}

export default Register;
