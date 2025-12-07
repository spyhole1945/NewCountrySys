const api = require('../../utils/api.js')

Page({
    data: {
        content: '',
        images: [],
        submitting: false,
        canSubmit: false
    },

    // 内容输入
    onContentInput(e) {
        const content = e.detail.value
        this.setData({
            content,
            canSubmit: content.trim().length > 0 || this.data.images.length > 0
        })
    },

    // 选择图片
    chooseImage() {
        const maxCount = 9 - this.data.images.length

        wx.chooseImage({
            count: maxCount,
            sizeType: ['compressed'],
            sourceType: ['album', 'camera'],
            success: (res) => {
                const images = [...this.data.images, ...res.tempFilePaths]
                this.setData({
                    images,
                    canSubmit: this.data.content.trim().length > 0 || images.length > 0
                })
            }
        })
    },

    // 删除图片
    deleteImage(e) {
        const index = e.currentTarget.dataset.index
        const images = this.data.images.filter((_, i) => i !== index)
        this.setData({
            images,
            canSubmit: this.data.content.trim().length > 0 || images.length > 0
        })
    },

    // 取消
    cancel() {
        if (this.data.content || this.data.images.length > 0) {
            wx.showModal({
                title: '提示',
                content: '确定要放弃编辑吗?',
                success: (res) => {
                    if (res.confirm) {
                        wx.navigateBack()
                    }
                }
            })
        } else {
            wx.navigateBack()
        }
    },

    // 提交发布
    async submit() {
        if (!this.data.canSubmit || this.data.submitting) return

        this.setData({ submitting: true })

        try {
            // 上传图片
            let uploadedImages = []
            if (this.data.images.length > 0) {
                wx.showLoading({ title: '上传图片中...' })
                try {
                    const uploadPromises = this.data.images.map(path => api.uploadImage(path))
                    uploadedImages = await Promise.all(uploadPromises)
                } catch (err) {
                    console.error('Image upload failed in create.js:', err);
                    wx.hideLoading()
                    wx.showToast({
                        title: '图片上传失败: ' + err,
                        icon: 'none',
                        duration: 3000
                    })
                    this.setData({ submitting: false })
                    return
                }
                wx.hideLoading()
            }

            const res = await api.createSocialPost({
                content: this.data.content,
                images: JSON.stringify(uploadedImages)
            })

            // 显示积分奖励提示
            const points = res.pointsAwarded || 3
            wx.showToast({
                title: `发布成功，活跃积分 +${points}`,
                icon: 'success',
                duration: 2000
            })

            // 延迟返回
            setTimeout(() => {
                wx.navigateBack()
            }, 2000)

        } catch (error) {
            console.error('发布失败:', error)
            wx.showToast({
                title: '发布失败，请重试',
                icon: 'none'
            })
            this.setData({ submitting: false })
        }
    }
})
