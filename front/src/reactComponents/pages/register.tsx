import React, {useState} from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import {Parallax} from "react-parallax";
import {Alert} from "@mui/material";
import {
  getCredentialResponse,
  isNewUser,
  addUser,
  Login
} from "../GoogleLogin";


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
                isNewUser(loginToken.sub).then((isNewUser) => {
                  if (isNewUser) {
                    addUser(loginToken.sub).then((userAdded)=>{
                      if (userAdded) {
                        Login(loginToken)
                      }
                      else {
                        setSubmitIssue(true)
                        setSubmitError("Error adding user to our server, Please try again")
                      }
                    }).catch(err => {
                      setSubmitError("Error adding user to our server, Please try again")
                      setSubmitIssue(true)
                    })
                  } else {
                    setSubmitIssue(true)
                    setSubmitError("This email is already registered, please Sign in instead")
                  }
                }).catch(err => {
                  setSubmitError("Error contacting our server, Please try again")
                  setSubmitIssue(true)
                })
              })
            }

          }
            onError={() => {
              setSubmitIssue(true)
              setSubmitError("Google authentication failed, please try again")
            }}
          />
          {/* <button onClick={() => login()}       //TODO invalid hook ?????
        className="sign-in-button">               
          Sign in with Google 🚀{" "}
          
        </button> */}
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
        </GoogleOAuthProvider>
      </div>
      <LoggedOutMenu description={"Register page"} />
    </div>
      </Parallax>
  );
}
export default Register;
