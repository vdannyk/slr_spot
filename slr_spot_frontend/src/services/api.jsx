import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "https://primary:hfbPPW0lQQf8Z2ygnx0P4V01wlQwAkwq0l1v5GpvgWzNsw4cTM1bTq4Dx6bmIvHN@slrspot-backend.test.azuremicroservices.io/slrspot-server/default/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

export default axiosInstance;