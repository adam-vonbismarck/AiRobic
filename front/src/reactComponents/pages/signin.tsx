import React, {useEffect, useState} from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import {Parallax} from "react-parallax";
import { getCredentialResponse, isNewUser, Login } from "../GoogleLogin";
import {Alert, makeStyles} from "@mui/material";


function Signin() {
    const [submitIssue, setSubmitIssue] = useState(false);
    const [submitError, setSubmitError] = useState("");
    return (
        <Parallax
            bgImage={"/assets/images/NaomiBaker_WRCHAMPSD3_8.jpg"}
            strength={0}
            aria-label="Parallax image with sign in display page"

        >


        <div className="register-window">
            <div className="sign-in-button">

                <GoogleOAuthProvider clientId="20770065062-amdbsjkao5gag2g7m0b7o4pn411akg80.apps.googleusercontent.com">
                    <h2>Sign in below</h2>
                    <GoogleLogin
                        shape={"rectangular"}
                        size={"large"}
                        theme={"filled_black"}
                        width={"400"}
                        onSuccess={(credentialResponse) => {

                            getCredentialResponse(credentialResponse.credential).then(loginToken => {
                                isNewUser(loginToken.sub).then(isNewUser => {
                                    if (!isNewUser) {
                                        Login(loginToken)
                                    }
                                    else {
                                        setSubmitError("Account not found, Please register first")
                                        setSubmitIssue(true)
                                    }
                                }).catch(err => {
                                    setSubmitError("Error contacting our server, Please try again")
                                    setSubmitIssue(true)
                                })
                            }).catch(err => {
                                setSubmitError("Error contacting our server, Please try again")
                                setSubmitIssue(true)
                            })
                        }}
                        onError={() => {
                            setSubmitError("Google authentication failed, please try again")
                            setSubmitIssue(true)

                        }}
                    />
                    {submitIssue && (
                        <div className="error-row">
                            <div className="form-error-message" aria-label="Error message">
                                <Alert
                                    severity="error"
                                    variant="filled"
                                    sx={{ fontFamily: "Muli" }}
                                    aria-label="Error: Sign in unsuccessful"
                                >
                                    {submitError}
                                </Alert>
                            </div>
                        </div>
                    )}
                </GoogleOAuthProvider>
            </div>
            <LoggedOutMenu description={"Sign in page"} />
        </div>
        </Parallax>
    );
}


export default Signin;
