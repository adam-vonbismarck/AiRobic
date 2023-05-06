import React, { useEffect, useState } from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { Link, useNavigate } from "react-router-dom";
import { useGoogleLogin } from "@react-oauth/google";
import { Parallax } from "react-parallax";
import { Alert } from "@mui/material";
import {
  getCredentialResponse,
  isNewUser,
  addUser,
  Login,
} from "../GoogleLogin";
import { motion } from "framer-motion";

function Signin() {
  const [submitIssue, setSubmitIssue] = useState(false);
  const [submitError, setSubmitError] = useState("");

  return (
    <Parallax
      bgImage={"/assets/images/NaomiBaker_WRCHAMPSD3_8.jpg"}
      strength={0}
      aria-label="Parallax image with sign in display page"
    >
      <LoggedOutMenu description={""} />
      <div className="register-window">
        <div className="content-wrapper">
          <motion.h1
            initial={{ opacity: 0, x: 70 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.0, duration: 0.5 }}
            style={{ color: "#31393C" }}
          >
            Sign In
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
                  onSuccess={(credentialResponse) => {
                    getCredentialResponse(credentialResponse.credential)
                      .then((loginToken) => {
                        isNewUser(loginToken.sub)
                          .then((isNewUser) => {
                            if (!isNewUser) {
                              Login(loginToken);
                            } else {
                              setSubmitError(
                                "User not found, please register first"
                              );
                              setSubmitIssue(true);
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
  given_name: "ERROR",
};

interface CheckUserResponse {
  result: string;
  message: string;
}

interface AddUserResponse {
  result: string;
  message: string;
}

function isLoginResponse(rjson: any): rjson is LoginResponse {
  if (!("sub" in rjson) || !("email" in rjson)) return false;
  return true;
}

function isCheckUserResponse(rjson: any): rjson is CheckUserResponse {
  if (!("result" in rjson) || "message" in rjson) return false;
  return true;
}

function isAddUserResponse(rjson: any): rjson is AddUserResponse {
  if (!("result" in rjson) || "message" in rjson) return false;
  return true;
}

export default Signin;
