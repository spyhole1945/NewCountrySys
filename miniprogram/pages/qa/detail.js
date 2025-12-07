const api = require('../../utils/api.js');
const app = getApp();

Page({
    data: {
        question: null,
        answers: [],
        replyContent: ''
    },

    onLoad(options) {
        const id = options.id;
        this.loadDetail(id);
    },

    async loadDetail(id) {
        try {
            const res = await api.getQuestionDetail(id);

            // Map backend fields to frontend display fields if necessary
            const question = res.question;
            if (question.images) {
                try {
                    question.images = JSON.parse(question.images);
                } catch (e) {
                    question.images = [];
                }
            } else {
                question.images = [];
            }
            // Use authorName as author
            question.author = question.authorName || '匿名用户';
            // Use default avatar if missing
            question.avatar = question.authorAvatar || 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0';

            const answers = res.answers.map(a => ({
                ...a,
                author: a.authorName || '匿名用户',
                avatar: a.authorAvatar || 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0',
                likeCount: a.likes || 0
            }));

            this.setData({
                question: question,
                answers: answers
            });
        } catch (err) {
            console.error('加载详情失败', err);
            wx.showToast({ title: '加载失败', icon: 'none' });
        }
    },

    onInput(e) {
        this.setData({ replyContent: e.detail.value });
    },

    async submitAnswer() {
        if (!this.data.replyContent.trim()) {
            wx.showToast({ title: '请输入回答内容', icon: 'none' });
            return;
        }

        wx.showLoading({ title: '提交中' });

        try {
            const res = await api.createAnswer(this.data.question.id, {
                content: this.data.replyContent
            });

            wx.hideLoading();
            wx.showToast({ title: `回答成功，积分+${res.pointsAwarded}`, icon: 'success' });

            // Reload detail to show new answer
            this.loadDetail(this.data.question.id);
            this.setData({ replyContent: '' });

        } catch (err) {
            console.error(err);
            wx.hideLoading();
            wx.showToast({ title: '提交失败', icon: 'none' });
        }
    }
});
