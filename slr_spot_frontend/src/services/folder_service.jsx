import axiosInstance from "./api";

const getFolderTree = (reviewId) => {
  return axiosInstance.get("/folders/tree", { params: {
    reviewId
  }})
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