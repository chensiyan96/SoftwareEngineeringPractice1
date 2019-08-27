import axios from 'axios'
import qs from 'qs' // 字符串处理
import merge from 'lodash/merge' // 合并对象工具

const http = axios.create({
  timeout: 1000 * 30,
  // withCredentials: true, // 当前请求为跨域类型时是否在请求中协带cookie
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

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
  console.log(jsonString)
  return jsonString
}

http.encryptData = (data = {}) => {
  var jsonString = JSON.stringify(data)
  var encrypt = {
    'uuid': data.uuid,
    'data': jsonString
  }
  var string = JSON.stringify(encrypt)
  console.log(string)
  return string
}

export default http
