import axiosInstance from "./api";

const getAdminBoard = () => {
    return axiosInstance.get("/users");
};

const UserService = {
    getAdminBoard,
};

export default UserService;