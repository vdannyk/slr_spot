import { combineReducers } from "redux";
import auth from "./auth";
import message from "./message";

const reducer = combineReducers({
  auth,
  message,
});

export default reducer;