// src/api/content.js
import request from '../utils/request'

// 获取社区动态列表
export function getCommunityPosts(params) {
    return request({
        url: '/api/admin/content/posts',
        method: 'get',
        params
    })
}

// 获取集市商品列表
export function getMarketItems(params) {
    return request({
        url: '/api/admin/content/market-items',
        method: 'get',
        params
    })
}

// 审核通过内容
export function approveContent(type, id) {
    return request({
        url: `/api/admin/content/${type}/${id}/approve`,
        method: 'put'
    })
}

// 删除内容
export function deleteContent(type, id) {
    return request({
        url: `/api/admin/content/${type}/${id}`,
        method: 'delete'
    })
}
