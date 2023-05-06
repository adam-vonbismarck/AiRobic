import React, { useEffect } from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import {Link, useNavigate} from "react-router-dom";
import { useGoogleLogin } from "@react-oauth/google";
import {Parallax} from "react-parallax";

// const login = useGoogleLogin({
//   onSuccess: (tokenResponse) => console.log(tokenResponse),
// });

/* custom button
<button onClick={() => login()}>
  Sign in with Google 🚀{" "}
</button>;r
*/

/**
 * https://www.youtube.com/watch?v=roxC8SMs7HU
 * google oauth for react 2023
 */

function Sign_in() {
    return (
    <GoogleOAuthProvider clientId="20770065062-amdbsjkao5gag2g7m0b7o4pn411akg80.apps.googleusercontent.com">

    <button className={"SignInButton"}
            onClick={() => useGoogleLogin({
                onSuccess: tokenResponse => console.log(tokenResponse),
                onError: () => console.log("Error")
            })}>
        Sign in with Google 🚀{' '}
    </button>;
        </GoogleOAuthProvider>

    )
}
function Sign__in() {

    // Use this function to trigger the
    // "LogIn With Google" process
    // at the end of which the onSuccess function
    // is triggered
    const login = useGoogleLogin({
        onSuccess: codeResponse => console.log(codeResponse)
    });
    return (
        <div className="App">
            <header className="App-header">
                {/* This is my button. Its just a regular HTML Button */}
                <button onClick={() => login()}>
                    Log In Using Google
                </button>

            </header>
        </div>
    );
}

function Signin() {
    const navigate = useNavigate();
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
                        width={"500"}
                        onSuccess={(credentialResponse) => {
                            fetch("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token="+credentialResponse.credential)
                                .then((response: Response) => response.json())
                                .then((loginToken) => {
                                    if (!isLoginResponse(loginToken)) {
                                        console.log("Login Failed")
                                    } else {
                                        console.log(loginToken.sub);
                                        localStorage.setItem("userID", loginToken.sub);
                                        localStorage.setItem("givenName", loginToken.given_name); //---> remove upon logout: localStorage.clear()
                                        localStorage.setItem("loggedIn","true")
                                        console.log(loginToken.family_name);
                                        console.log(loginToken.given_name); // --->

                                        navigate('/', { replace: true });

                                        console.log(localStorage);
                                    }
                                });
                        }}
                        onError={() => {
                            console.log("Login Failed");
                        }}
                    />
                    {/* <button onClick={() => login()}
        className="sign-in-button">
          Sign in with Google 🚀{" "}

        </button> */}
                </GoogleOAuthProvider>
            </div>
            <LoggedOutMenu description={"Sign in page"} />
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

export default Signin;
