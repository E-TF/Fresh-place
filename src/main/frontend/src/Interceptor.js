import axios from "axios";
import {getAuthorization} from "./routes/Authentication";

axios.interceptors.request.use(
    function (config) {
        if (getAuthorization())
            config.headers['Authorization'] = getAuthorization();
        return config;
    }
)

export default axios;
