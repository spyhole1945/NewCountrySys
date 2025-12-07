const api = require('../../utils/api.js')

Page({
    data: {
        userInfo: {
            nickname: '村民',
            phone: '',
            avatar: '',
            points: 0
        },
        officials: [
            { id: 1, name: '张村长', role: '村委会主任', phone: '13800138001' },
            { id: 2, name: '李书记', role: '党支部书记', phone: '13800138002' },
            { id: 3, name: '王会计', role: '村会计', phone: '13800138003' }
        ]
    },

    onLoad() {
        this.loadUserInfo()
    },

    onShow() {
        // 每次显示页面时刷新用户信息(积分可能变化)
        this.loadUserInfo()
    },

    // 加载用户信息
    async loadUserInfo() {
        try {
            const res = await api.getUserInfo()
            this.setData({
                userInfo: res.data || this.data.userInfo
            })
        } catch (error) {
            console.error('加载用户信息失败:', error)
        }
    },

    // 去积分明细
    goPointsLog() {
        wx.navigateTo({
            url: '/pages/profile/points_log'
        })
    },

    // 我的上报
    goMyReports() {
        wx.showToast({
            title: '功能开发中',
            icon: 'none'
        })
    },

    // 我的集市
    goMyMarket() {
        wx.showToast({
            title: '功能开发中',
            icon: 'none'
        })
    },

    // 我的动态
    goMyPosts() {
        wx.showToast({
            title: '功能开发中',
            icon: 'none'
        })
    },

    // 设置
    goSettings() {
        wx.showToast({
            title: '功能开发中',
            icon: 'none'
        })
    },

    // 拨打电话
    callOfficial(e) {
        const phone = e.currentTarget.dataset.phone
        wx.makePhoneCall({
            phoneNumber: phone
        })
    }
})
    ;
