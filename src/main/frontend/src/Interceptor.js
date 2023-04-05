import axios from "axios";
import {getAuthorization} from "./routes/Authentication";

axios.interceptors.request.use(
    function (config) {
        if(getAuthorization() != null) {
            axios.defaults.headers.common['Authorization'] = getAuthorization();
        }
        return config;
    }
)

export default axios;
