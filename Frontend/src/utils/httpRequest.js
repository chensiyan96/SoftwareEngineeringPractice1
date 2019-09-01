import axios from 'axios'
import qs from 'qs' // 字符串处理
import merge from 'lodash/merge'
import {clearLoginInfo} from "./index"; // 合并对象工具
import Vue from "vue";

const http = axios.create({
  timeout: 1000 * 30,
  withCredentials: true, // 当前请求为跨域类型时是否在请求中协带cookie
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

/**
 * 请求拦截
 */
http.interceptors.request.use(config => {
  // 处理请求之前的配置
  config.headers['token'] = Vue.cookie.get('blog-token') // // 请求头带上token
  return config
}, error => {
  // 请求失败的处理
  return Promise.reject(error)
})

/**
 * 响应拦截
 */
// http.interceptors.response.use(response => {
//   if (response.data && response.data.code === 401) { // 401 token失效
//     clearLoginInfo()
//     router.push({ name: 'login' })
//   }
//   return response
// }, error => {
//   return Promise.reject(error)
// })

/**
 * 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornUrl = (actionName) => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  return (process.env.NODE_ENV !== 'production' && process.env.OPEN_PROXY ? '/proxyApi/' : window.SITE_CONFIG.baseUrl) + actionName
}

/**
 * get 请求参数处理
 * @param params
 * @param openDefaultParams
 * @returns {*}
 */
http.adornParams = (params = {}, openDefaultParams = false) => {
  var defaluts = {
    't': new Date().getTime()
  }
  return openDefaultParams ? merge(defaluts, params) : params
}
/**
 * post请求参数处理
 * @param data
 * @param openDefaultData
 * @param contentType
 * @returns {string}
 */
http.adornData = (data = {}, openDefaultData = true, contentType = 'json') => {
  var jsonString = contentType === 'json' ? JSON.stringify(data) : qs.stringify(data)
  return jsonString
}

http.encryptData = (data = {}, key, uuid, use = true) => {
  const jsEncrypt = new JSEncrypt();
  jsEncrypt.setPublicKey('-----BEGIN PUBLIC KEY-----' + key + '-----END PUBLIC KEY-----');
  let jsonString;
  if (use) {
    jsonString = jsEncrypt.encrypt(JSON.stringify(data));
  } else {
    jsonString = JSON.stringify(data);
  }
  const encrypt = {
    'uuid': uuid,
    'data': jsonString
  };
  return JSON.stringify(encrypt)
}

export default http
