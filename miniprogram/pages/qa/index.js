// pages/qa/index.js
const api = require('../../utils/api.js');
const app = getApp();

Page({
    data: {
        questions: [],
        page: 1,
        hasMore: true,
        isLoading: false
    },

    onLoad(options) {
        // Initial load is handled by onShow
    },

    onShow() {
        // Refresh list when showing page (e.g. returning from create)
        this.loadQuestions(true);
    },

    onPullDownRefresh() {
        this.loadQuestions(true);
    },

    onReachBottom() {
        if (this.data.hasMore && !this.data.isLoading) {
            this.loadQuestions(false);
        }
    },

    async loadQuestions(refresh = false) {
        if (this.data.isLoading) return;

        this.setData({ isLoading: true });

        if (refresh) {
            this.setData({ page: 1, hasMore: true });
        }

        try {
            const res = await api.getQuestions({
                page: refresh ? 1 : this.data.page,
                size: 10
            });

            const newQuestions = refresh ? res.records : this.data.questions.concat(res.records);

            this.setData({
                questions: newQuestions,
                page: (refresh ? 1 : this.data.page) + 1,
                hasMore: newQuestions.length < res.total,
                isLoading: false
            });

            if (refresh) {
                wx.stopPullDownRefresh();
            }
        } catch (err) {
            console.error('加载问题失败', err);
            this.setData({ isLoading: false });
            wx.showToast({
                title: '加载失败',
                icon: 'none'
            });
            if (refresh) {
                wx.stopPullDownRefresh();
            }
        }
    },

    goToDetail(e) {
        const id = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `/pages/qa/detail?id=${id}`
        });
    },

    goToAsk() {
        wx.navigateTo({
            url: '/pages/qa/create'
        });
    }
});
