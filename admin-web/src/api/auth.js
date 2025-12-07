// src/api/auth.js
import request from '../utils/request'

// 登录
export function login(data) {
    return request({
        url: '/api/auth/login',
        method: 'post',
        data
    })
}

// 登出
export function logout() {
    return request({
        url: '/api/auth/logout',
        method: 'post'
    })
}
