// src/api/user.js
import request from '../utils/request'

// 获取用户列表
export function getUserList(params) {
    return request({
        url: '/api/admin/users',
        method: 'get',
        params
    })
}

// 获取用户详情
export function getUserDetail(id) {
    return request({
        url: `/api/admin/users/${id}`,
        method: 'get'
    })
}

// 更新用户角色
export function updateUserRole(id, data) {
    return request({
        url: `/api/admin/users/${id}/role`,
        method: 'put',
        data
    })
}

// 切换用户状态
export function toggleUserStatus(id, active) {
    return request({
        url: `/api/admin/users/${id}/status`,
        method: 'put',
        params: { active }
    })
}
