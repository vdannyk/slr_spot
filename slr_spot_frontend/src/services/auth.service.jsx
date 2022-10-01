import axios from "axios";

const register = (firstName, lastName, email, password) => {
    return axios.post("/user/save", {
        firstName,
        lastName,
        email,
        password,
    });
};

const login = (username, password) => {
    var bodyFormData = new FormData();
    bodyFormData.append("username", username);
    bodyFormData.append("password", password);
    
    return axios({
      method: "post",
      url: "http://localhost:8080/api/auth/signin",
      data: bodyFormData,
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then(function (response) {
      // handle success
      if (response.data.accessToken) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }
      console.log(response);
      return response.data;
    });
    // .catch(function (response) {
    //   // handle error
    //   console.log(response);
    // })
};

const logout = () => {
    localStorage.removeItem("user");
};

export default {
    register,
    login,
    logout,
};