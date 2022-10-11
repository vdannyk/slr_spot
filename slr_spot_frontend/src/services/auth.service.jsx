import TokenService from "./token.service";
import axiosInstance from "./api";

const register = (firstName, lastName, username, password) => {
  return axiosInstance.post("/user/save", {
    firstName: firstName,
    lastName: lastName,
    email: username,
    password: password,
  })
  .then(function (response) {
    // handle success
    console.log(response);
  })
  .catch(function (response) {
    // handle error
    console.log(response);
  })
};

const login = (username, password) => {
    return axiosInstance.post("/auth/signin", {
      username,
      password,
    })
    .then(function (response) {
      // handle success
      if (response.data.accessToken) {
        TokenService.setUser(response.data);
      }
      console.log(response);
      return response.data;
    })
    .catch(function (response) {
      // handle error
      console.log(response);
    });
};

const logout = () => {
  TokenService.removeUser();
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};

const AuthService = {
  register,
  login,
  logout,
  getCurrentUser,
};

export default AuthService;