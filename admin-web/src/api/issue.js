// src/api/issue.js
import request from '../utils/request'

// 获取问题列表
export function getIssueList(params) {
    return request({
        url: '/api/admin/issues',
        method: 'get',
        params
    })
}

// 获取问题详情
export function getIssueDetail(id) {
    return request({
        url: `/api/admin/issues/${id}`,
        method: 'get'
    })
}

// 更新问题状态
export function updateIssueStatus(id, data) {
    return request({
        url: `/api/admin/issues/${id}/status`,
        method: 'put',
        data
    })
}

// 获取统计数据
export function getIssueStats() {
    return request({
        url: '/api/admin/issues/stats',
        method: 'get'
    })
}
