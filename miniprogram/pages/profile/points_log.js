const api = require('../../utils/api.js')

Page({
    data: {
        logs: [],
        loading: false,
        page: 1,
        hasMore: true
    },

    onLoad() {
        this.loadLogs()
    },

    // 加载积分记录
    async loadLogs() {
        if (this.data.loading || !this.data.hasMore) return

        this.setData({ loading: true })

        try {
            const res = await api.getPointsLog({
                page: this.data.page,
                size: 20
            })

            const newLogs = res.data || []

            this.setData({
                logs: this.data.page === 1 ? newLogs : [...this.data.logs, ...newLogs],
                hasMore: newLogs.length >= 20,
                page: this.data.page + 1,
                loading: false
            })
        } catch (error) {
            console.error('加载积分记录失败:', error)
            wx.showToast({
                title: '加载失败',
                icon: 'none'
            })
            this.setData({ loading: false })
        }
    },

    // 加载更多
    loadMore() {
        this.loadLogs()
    },

    // 下拉刷新
    onPullDownRefresh() {
        this.setData({
            logs: [],
            page: 1,
            hasMore: true
        })
        this.loadLogs()
        wx.stopPullDownRefresh()
    }
})
