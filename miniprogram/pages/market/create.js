const api = require('../../utils/api.js')

Page({
    data: {
        categories: [
            { id: 'machinery', name: '农机出租' },
            { id: 'land', name: '土地流转' },
            { id: 'carpool', name: '顺风车' },
            { id: 'service', name: '本地服务' }
        ],
        categoryIndex: 0,
        formData: {
            category: 'machinery',
            title: '',
            description: '',
            price: '',
            phone: '',
            images: []
        },
        submitting: false
    },

    // 分类选择
    onCategoryChange(e) {
        const index = e.detail.value
        this.setData({
            categoryIndex: index,
            'formData.category': this.data.categories[index].id
        })
    },

    // 标题输入
    onTitleInput(e) {
        this.setData({
            'formData.title': e.detail.value
        })
    },

    // 描述输入
    onDescriptionInput(e) {
        this.setData({
            'formData.description': e.detail.value
        })
    },

    // 价格输入
    onPriceInput(e) {
        this.setData({
            'formData.price': e.detail.value
        })
    },

    // 电话输入
    onPhoneInput(e) {
        this.setData({
            'formData.phone': e.detail.value
        })
    },

    // 选择图片
    chooseImage() {
        const maxCount = 3 - this.data.formData.images.length

        wx.chooseImage({
            count: maxCount,
            sizeType: ['compressed'],
            sourceType: ['album', 'camera'],
            success: (res) => {
                // TODO: 实际项目中需要上传到服务器
                // 这里暂时使用本地路径
                const images = [...this.data.formData.images, ...res.tempFilePaths]
                this.setData({
                    'formData.images': images
                })
            }
        })
    },

    // 删除图片
    deleteImage(e) {
        const index = e.currentTarget.dataset.index
        const images = this.data.formData.images.filter((_, i) => i !== index)
        this.setData({
            'formData.images': images
        })
    },

    // 表单验证
    validateForm() {
        const { title, description, phone } = this.data.formData

        if (!title.trim()) {
            wx.showToast({
                title: '请输入标题',
                icon: 'none'
            })
            return false
        }

        if (!description.trim()) {
            wx.showToast({
                title: '请输入详细描述',
                icon: 'none'
            })
            return false
        }

        if (!phone.trim()) {
            wx.showToast({
                title: '请输入联系电话',
                icon: 'none'
            })
            return false
        }

        if (!/^1[3-9]\d{9}$/.test(phone)) {
            wx.showToast({
                title: '请输入正确的手机号',
                icon: 'none'
            })
            return false
        }

        return true
    },

    // 提交发布
    async submit() {
        if (!this.validateForm()) return
        if (this.data.submitting) return

        this.setData({ submitting: true })

        try {
            // TODO: 实际项目中需要先上传图片获取URL
            const res = await api.createMarketItem(this.data.formData)

            // 显示积分奖励提示
            const points = res.data.pointsAwarded || 5
            wx.showToast({
                title: `发布成功，积分 +${points}`,
                icon: 'success',
                duration: 2000
            })

            // 延迟返回，让用户看到提示
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
