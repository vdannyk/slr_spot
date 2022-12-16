import axiosInstance from "./api";

const getFolderTree = (firstName, lastName, username, password) => {
  return axiosInstance.get("/folders")
  .then((response) => {
    console.log(response.data);
    setFolders(response.data)
    setLoading(false);
  })
  .catch(() => {
  });
};

const FolderService = {
  getFolderTree,
};

export default FolderService;