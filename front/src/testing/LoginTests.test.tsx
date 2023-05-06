import {
    getCredentialResponse,
    isNewUser,
    isAddUserResponse,
    addUser,
    isCheckUserResponse,
    isLoginResponse,
    Login,
    deleteUser,
    errLoginResponse,
    DeleteUserResponse,
    LoginResponse
} from "../reactComponents/GoogleLogin";



/**
 * Testing class for login and authentication functions. Ensure back end is running
 */
test("isNewUser: User exists", async () => {
    let response: boolean | void =  await(isNewUser("alexfake")).catch(err => console.log("Start Backend!"))
    expect(response).toBe(false)
})

test("isNewUser: User does not exist", async () => {
    let response: boolean | void =  await(isNewUser("alexnotfake")).catch(err => console.log("Start Backend!"))
    expect(response).toBe(true)
})

test("addUser: new user added", async () => {
    let response: boolean | void = await(addUser("alexnotsofake")).catch(err => console.log("Start Backend!"))
    expect(response).toBe(true)
    await(deleteUser("alexnotsofake").catch(err => console.log("Start Backend!")))
})

test("addUser: user already exists", async () => {
    let response: boolean | void = await(addUser("alexfake")).catch(err => console.log("Start Backend!"))
    expect(response).toBe(false)
})

test("getCredentialResponse: invalid credential", async () => {
    let response: LoginResponse | void = await(getCredentialResponse("invalidCredential"))
    expect(response).toBe(errLoginResponse)
})

/**
 * A Google credential response is 1202 characters long, beginning with eyJ.
 * This test attempts to create a random string to fake a credential response
 */
test("getCredentialResponse: fuzztest", async () => {
    for (let i = 0; i<10; i++) {
        let result: string = ""
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_';
        const charactersLength = characters.length;
        let counter = 0;
        while (counter < 1199) {
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
            counter += 1;
        }
        let fakeCredential = "eyJ" + result;
        let response = await(getCredentialResponse(fakeCredential)).catch(err => errLoginResponse)
        expect(response.sub).toBe("ERROR")
    }


})
test("Login: success", () => {
    let response: boolean = Login(mockLoginResponse);
    expect(response).toBe(true);
    expect(localStorage.getItem("userID")).toBe("MOCKUSER");
    expect(localStorage.getItem("givenName")).toBe("User");
    expect(localStorage.getItem("loggedIn")).toBe("true");
    localStorage.clear()
})

test("deleteUser: success", async () => {
    await(addUser("alexfakest")).catch(err => console.log("Start Backend!"))
    let response: DeleteUserResponse = await(deleteUser("alexfakest")).catch(err => {
        console.log("Start Backend!")
        return errDeleteResponse
    })
    expect(response.result).toBe("success")
    expect(response.message).toBe("Successfully deleted alexfakest")
})

test("isLoginResponse: true", () => {
    let response: boolean = isLoginResponse(mockLoginResponse)
    expect(response).toBe(true)
})

test("isLoginResponse: false", () => {
    let response: boolean = isLoginResponse(errDeleteResponse)
    expect(response).toBe(false)
})

test("isAddUserResponse: true", () => {
    let response: boolean = isAddUserResponse(mockAddUserResponse)
    expect(response).toBe(true)
})

test("isAddUserResponse: false", () => {
    let response: boolean = isAddUserResponse(mockLoginResponse)
    expect(response).toBe(false)
})

test("isCheckUserResponse: true", () => {
    let response: boolean = isCheckUserResponse(mockCheckUserResponse)
    expect(response).toBe(true)
})

test("isCheckUserResponse: false", () => {
    let response: boolean = isCheckUserResponse(mockLoginResponse)
    expect(response).toBe(false)
})


const errDeleteResponse = {
    result: "err",
    message: "err"
}

const mockLoginResponse = {
    sub: "MOCKUSER",
    email: "MOCKUSER@gmail.com",
    family_name: "Mock",
    given_name: "User"
}

const mockAddUserResponse = {
    result: "mock",
    message: "mock"
}

const mockCheckUserResponse = {
    result: "mock",
    message: "mock"
}

