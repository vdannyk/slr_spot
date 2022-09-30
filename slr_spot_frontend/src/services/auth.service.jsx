import axios from "axios";

const register = (firstName, lastName, email, password) => {
    return axios.post("/user/save", {
        firstName,
        lastName,
        email,
        password,
    });
};

const login = (email, password) => {
    return axios.post("/login", {
        email,
        password,
    })
    .then((response) => {
        if (response.data.accessToken) {
            localStorage.setItem("user", JSON.stringify(response.data));
        }
        return response.data;
    });
};

const logout = () => {
    localStorage.removeItem("user");
};

export default {
    register,
    login,
    logout,
};