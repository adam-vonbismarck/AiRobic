import React from "react";

/**
 * Fetches the credential response from google's api, if successful will return a LoginResponse (json) with the user's
 * id, email, and full name.
 * If unsuccessful will return errLoginResponse instead which informs that there was an issue with the response
 * @param credential - retrieved from google authenticator
 */
function getCredentialResponse(
  credential: string | undefined
): Promise<LoginResponse> {
  return new Promise((resolve, reject) => {
    fetch(
      "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + credential
    )
      .then((response: Response) => response.json())
      .then((loginToken) => {
        if (!isLoginResponse(loginToken)) {
          resolve(errLoginResponse);
        } else {
          resolve(loginToken);
        }
      })
      .catch((err) => reject(errLoginResponse));
  });
}

/**
 * Adds the user to the back end via a backend fetch call. resolves the promise as a boolean return type which informs
 * whether the call was successful or not.
 *
 * @param userID - the userID to add to the back end
 */
function addUser(userID: string): Promise<boolean> {
  return new Promise((resolve, reject) => {
    fetch("http://localhost:3235/adduser?username=" + userID)
      .then((response: Response) => response.json())
      .then((addResponse) => {
        if (isAddUserResponse(addResponse) && addResponse.result == "success") {
          resolve(true);
        } else {
          resolve(false);
        }
      })
      .catch((err) => reject(false));
  });
}

/**
 * Deletes the user from the back end via a fetch call to the api. Promise resolves as a boolean to represent if the
 * operation was successful or not
 * @param userID - the user ID to delete from the back end
 */
function deleteUser(userID: string): Promise<DeleteUserResponse> {
  return new Promise((resolve, reject) => {
    fetch("http://localhost:3235/deleteuser?username=" + userID)
      .then((response: Response) => response.json())
      .then((deleteResponse) => {
        resolve(deleteResponse);
      })
      .catch((err) => reject(false));
  });
}

/**
 * Makes an api call to the back end to check whether a given user currently exists or not.
 * If the user does not exist the promise resolves as true, if the user exists, it resolves as false.
 * Else the promise rejects if there is an issue in the checking
 * @param userID
 */
function isNewUser(userID: string): Promise<boolean> {
  return new Promise((resolve, reject) => {
    fetch("http://localhost:3235/checkuser?username=" + userID)
      .then((userResponse: Response) => userResponse.json())
      .then((checkUser) => {
        if (!isCheckUserResponse(checkUser)) {
          reject(false);
        } else {
          if (checkUser.message == "False") {
            resolve(true);
          } else {
            resolve(false);
          }
        }
      })
      .catch((err) => reject(false));
  });
}

/**
 * Logs the user in by setting the localStorage state "loggedIn" to true and redirects them to the home page.
 * Also records the userID and givenName locally
 * @param loginToken - the login response containing userID, name etc
 */
function Login(loginToken: LoginResponse): boolean {
  localStorage.setItem("userID", loginToken.sub);
  localStorage.setItem("givenName", loginToken.given_name);
  localStorage.setItem("loggedIn", "true");
  window.location.href = "/";
  return true;
}

/**
 * Interface representing json response that is returned when a successful login is completed
 */
export interface LoginResponse {
  sub: string;
  email: string;
  family_name: string;
  given_name: string;
}

/**
 * Interface representing json response that is returned when a user is deleted successfully
 */
export interface DeleteUserResponse {
  result: string;
  message: string;
}

/**
 * A LoginResponse that is returned when an error occurs
 */
const errLoginResponse = {
  sub: "ERROR",
  email: "ERROR",
  family_name: "ERROR",
  given_name: "ERROR",
};

/**
 * Interface representing json response that is returned when a successful checkuser call is made to the backend
 */
export interface CheckUserResponse {
  result: string;
  message: string;
}

/**
 * Interface representing json response that is returned when the back end adduser call is successfully made
 */
export interface AddUserResponse {
  result: string;
  message: string;
}

/**
 * Checks a given variable is of type LoginResponse and returns a boolean
 */
function isLoginResponse(rjson: any): rjson is LoginResponse {
  if (!("sub" in rjson) || !("email" in rjson)) return false;
  return true;
}
/**
 * Checks a given variable is of type CheckUserResponse and returns a boolean
 */
function isCheckUserResponse(rjson: any): rjson is CheckUserResponse {
  if (!("result" in rjson) || !("message" in rjson)) return false;
  return true;
}
/**
 * Checks a given variable is of type AddUserResponse and returns a boolean
 */
function isAddUserResponse(rjson: any): rjson is AddUserResponse {
  if (!("result" in rjson) || !("message" in rjson)) return false;
  return true;
}

export {
  getCredentialResponse,
  isNewUser,
  isAddUserResponse,
  addUser,
  errLoginResponse,
  isCheckUserResponse,
  isLoginResponse,
  Login,
  deleteUser,
};
