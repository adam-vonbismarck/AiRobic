import React, { useEffect } from "react";
import LoggedOutMenu from "../elements/loggedOutMenu";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { Link } from "react-router-dom";
import { useGoogleLogin } from "@react-oauth/google";

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
function Register() {
  return (
    <div className="register-window">
      <LoggedOutMenu description={"register page"} />
      <h5>Register below.</h5>
      <Link to="/">
        <button className="register-button">Register with google</button>
      </Link>
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
