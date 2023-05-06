import React, { useEffect, useState } from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { useNavigate } from "react-router-dom";
import { Parallax } from "react-parallax";
import { Alert } from "@mui/material";
import {
  getCredentialResponse,
  isNewUser,
  addUser,
  Login,
} from "../GoogleLogin";
import { motion } from "framer-motion";

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
      <LoggedOutMenu description={""} />
      <div className="register-window">
        <div className="content-wrapper">
          <motion.h1
            initial={{ opacity: 0, x: 70 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.0, duration: 0.5 }}
            style={{ color: "#ebe9e9" }}
          >
            Register
          </motion.h1>
          <motion.div
            initial={{ opacity: 0, x: 70 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.0, duration: 0.5 }}
          >
            <div className="sign-in-button">
              <GoogleOAuthProvider clientId="20770065062-amdbsjkao5gag2g7m0b7o4pn411akg80.apps.googleusercontent.com">
                <GoogleLogin
                  shape={"rectangular"}
                  size={"large"}
                  theme={"filled_black"}
                  width={"500"}
                  text={"signup_with"}
                  onSuccess={(credentialResponse) => {
                    getCredentialResponse(credentialResponse.credential)
                      .then((loginToken) => {
                        isNewUser(loginToken.sub)
                          .then((isNewUser) => {
                            if (isNewUser) {
                              addUser(loginToken.sub).then((userAdded) => {
                                if (userAdded) {
                                  Login(loginToken);
                                } else {
                                  setSubmitIssue(true);
                                  setSubmitError(
                                    "Error connecting to our server"
                                  );
                                }
                              });
                            } else {
                              setSubmitIssue(true);
                              setSubmitError(
                                "Email already registered, please sign in instead"
                              );
                            }
                          })
                          .catch((err) => {
                            setSubmitError("Error connecting to our server");
                            setSubmitIssue(true);
                          });
                      })
                      .catch((err) => {
                        setSubmitError("Error connecting to our server");
                        setSubmitIssue(true);
                      });
                  }}
                  onError={() => {
                    setSubmitIssue(true);
                    setSubmitError(
                      "Google authentication failed, please try again"
                    );
                  }}
                />

                {submitIssue && (
                  <div className="error-row">
                    <div
                      className="form-error-message"
                      aria-label="Error message"
                    >
                      <Alert
                        severity="error"
                        variant="filled"
                        sx={{ fontFamily: "Muli", fontSize: 14 }}
                        aria-label="Error: Workout plan submission unsuccessful"
                      >
                        {submitError}
                      </Alert>
                    </div>
                  </div>
                )}
              </GoogleOAuthProvider>
            </div>
          </motion.div>
        </div>
      </div>
    </Parallax>
  );
}
export default Register;
