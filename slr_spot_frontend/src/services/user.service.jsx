import axios from "axios";
import authHeader from "./auth_header";

const API_URL = "http://localhost:8080/api/";

const getAdminBoard = () => {
    return axios.get(API_URL + "users", { headers: authHeader() });
};

export default {
    getAdminBoard,
};