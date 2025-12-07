// src/utils/constants.js

// 用户角色
export const USER_ROLES = {
    VILLAGER: '村民',
    OFFICIAL: '村务人员',
    ADMIN: '管理员'
}

// 问题状态
export const ISSUE_STATUS = {
    PENDING: '待处理',
    PROCESSING: '处理中',
    DONE: '已完成'
}

// 问题类型
export const ISSUE_TYPES = [
    '道路损坏',
    '垃圾堆积',
    '路灯故障',
    '水利设施',
    '环境污染',
    '其他问题'
]

// 内容类型
export const CONTENT_TYPES = {
    COMMUNITY_POST: '社区动态',
    MARKET_ITEM: '集市商品'
}

// 内容状态
export const CONTENT_STATUS = {
    PENDING_REVIEW: '待审核',
    APPROVED: '已通过',
    REJECTED: '已拒绝'
}

// 用户状态
export const USER_STATUS = {
    ACTIVE: '正常',
    DISABLED: '禁用'
}
