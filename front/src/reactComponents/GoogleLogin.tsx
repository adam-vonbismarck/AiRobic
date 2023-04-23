import React, { useEffect } from "react";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GoogleLogin } from "@react-oauth/google";
import { Link } from "react-router-dom";
import { useGoogleLogin } from "@react-oauth/google";

const login = useGoogleLogin({
  onSuccess: async (tokenResponse) => console.log(tokenResponse), //invalid hook call??
  flow: "auth-code",
});


export { login }

