import axiosInstance from "./api";

const getFolderTree = (firstName, lastName, username, password) => {
  return axiosInstance.get("/folders")
  .then((response) => {
    return response;
  })
  .catch(() => {
  });
};

const FolderService = {
  getFolderTree,
};

export default FolderService;