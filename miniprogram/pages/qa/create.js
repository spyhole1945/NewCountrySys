// pages/qa/create.js
const api = require('../../utils/api.js');
const app = getApp();

Page({
    data: {
        title: '',
        content: '',
        images: [],
        isSubmitting: false
    },

    onInputTitle(e) {
        this.setData({ title: e.detail.value });
    },

    onInputContent(e) {
        this.setData({ content: e.detail.value });
    },

    chooseImage() {
        wx.chooseImage({
            count: 3,
            sizeType: ['compressed'],
            sourceType: ['album', 'camera'],
            success: (res) => {
                // Mock upload for now, in real app upload to server
                const newImages = res.tempFilePaths;
                this.setData({
                    images: this.data.images.concat(newImages)
                });
            }
        });
    },

    removeImage(e) {
        const index = e.currentTarget.dataset.index;
        const images = this.data.images;
        images.splice(index, 1);
        this.setData({ images });
    },

    async submitQuestion() {
        if (!this.data.title.trim()) {
            wx.showToast({ title: '请输入问题标题', icon: 'none' });
            return;
        }
        if (!this.data.content.trim()) {
            wx.showToast({ title: '请输入问题描述', icon: 'none' });
            return;
        }

        this.setData({ isSubmitting: true });

        try {
            await api.createQuestion({
                title: this.data.title,
                content: this.data.content,
                images: JSON.stringify(this.data.images)
            });

            wx.showToast({
                title: '发布成功',
                icon: 'success'
            });

            setTimeout(() => {
                wx.navigateBack();
            }, 1500);

        } catch (err) {
            console.error(err);
            wx.showToast({ title: '发布失败', icon: 'none' });
            this.setData({ isSubmitting: false });
        }
    }
});
