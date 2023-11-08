import axios from "axios";
import store from '@/store'

import {getToken, removeToken} from '@/utils/auth'

// create an axios instance
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
    timeout: 36000 // request timeout
})

// request interceptor
service.interceptors.request.use(
    config => {
        //do something before request is sent
        let token = getToken()
        if (token != null) {
            // let each request carry token
            // ['X-Token'] is a custom headers key
            // please modify it according to the actual situation
            config.headers.Authorization = token
        }

        return config
    },
    error => {
        // do something with request error
        console.log(error) // for debug
        return Promise.reject(error)
    }
)

// response interceptor
service.interceptors.response.use(

    response => {
        const res = response.data
        // store.commit('SET_LOADING',false);
        // if the custom code is not 20000, it is judged as an error.
        if (res.code == 401) {
            removeToken()
            store.state.loginFlag = true
        }
        if (res.code !== 200) {
            return Promise.reject(new Error(res.message || 'Error'))
        } else {
            return res
        }
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)

export default service
