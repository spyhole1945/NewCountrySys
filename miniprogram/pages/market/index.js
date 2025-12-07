const api = require('../../utils/api.js')

Page({
    data: {
        categories: [
            { id: 'machinery', name: '农机出租' },
            { id: 'land', name: '土地流转' },
            { id: 'carpool', name: '顺风车' },
            { id: 'service', name: '本地服务' }
        ],
        currentCategory: 'machinery',
        currentCategoryName: '农机出租',
        items: [],
        loading: false,
        page: 1,
        hasMore: true
    },

    onLoad() {
        this.loadItems()
    },

    // 切换分类
    switchCategory(e) {
        const id = e.currentTarget.dataset.id
        const category = this.data.categories.find(c => c.id === id)

        this.setData({
            currentCategory: id,
            currentCategoryName: category.name,
            items: [],
            page: 1,
            hasMore: true
        })

        this.loadItems()
    },

    // 加载集市列表
    async loadItems() {
        if (this.data.loading || !this.data.hasMore) return

        this.setData({ loading: true })

        try {
            const res = await api.getMarketItems({
                category: this.data.currentCategory,
                page: this.data.page,
                size: 10
            })

            const newItems = res.data || []

            this.setData({
                items: this.data.page === 1 ? newItems : [...this.data.items, ...newItems],
                hasMore: newItems.length >= 10,
                page: this.data.page + 1,
                loading: false
            })
        } catch (error) {
            console.error('加载集市列表失败:', error)
            wx.showToast({
                title: '加载失败',
                icon: 'none'
            })
            this.setData({ loading: false })
        }
    },

    // 查看详情
    viewDetail(e) {
        const id = e.currentTarget.dataset.id
        wx.navigateTo({
            url: `/pages/market/detail?id=${id}`
        })
    },

    // 去发布
    goPublish() {
        wx.navigateTo({
            url: '/pages/market/create'
        })
    },

    // 下拉刷新
    onPullDownRefresh() {
        this.setData({
            items: [],
            page: 1,
            hasMore: true
        })
        this.loadItems()
        wx.stopPullDownRefresh()
    },

    // 上拉加载更多
    onReachBottom() {
        this.loadItems()
    }
})
