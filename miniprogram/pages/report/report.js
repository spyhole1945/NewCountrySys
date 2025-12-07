// pages/report/report.js
const api = require('../../utils/api.js');
const config = require('../../utils/config.js');

Page({
    /**
     * 页面的初始数据
     */
    data: {
        // 问题类型
        issueTypes: config.issueTypes,
        typeIndex: -1,
        selectedType: '',

        // 图片列表
        imageList: [],
        maxImageCount: config.uploadConfig.maxImageCount,

        // 语音相关
        isRecording: false,
        voiceRecorded: false,
        voiceButtonText: '按住说话',
        voiceFilePath: '',
        voiceContent: '', // 语音转文字内容（暂时为空）

        // 位置信息
        location: null,

        // 文字描述
        description: '',

        // 提交状态
        submitting: false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        // 初始化录音管理器
        this.recorderManager = wx.getRecorderManager();
        this.initRecorder();
    },

    /**
     * 初始化录音管理器
     */
    initRecorder() {
        const recorderManager = this.recorderManager;

        // 录音开始事件
        recorderManager.onStart(() => {
            console.log('录音开始');
            this.setData({
                isRecording: true,
                voiceButtonText: '录音中...'
            });
        });

        // 录音结束事件
        recorderManager.onStop((res) => {
            console.log('录音结束', res);
            this.setData({
                isRecording: false,
                voiceRecorded: true,
                voiceButtonText: '按住说话',
                voiceFilePath: res.tempFilePath
            });

            // TODO: 这里可以调用语音转文字API
            // 暂时使用占位文本
            this.setData({
                voiceContent: '[语音说明]'
            });

            wx.showToast({
                title: '语音已录入',
                icon: 'success',
                duration: 1500
            });
        });

        // 录音错误事件
        recorderManager.onError((err) => {
            console.error('录音错误', err);
            this.setData({
                isRecording: false,
                voiceButtonText: '按住说话'
            });
            wx.showToast({
                title: '录音失败',
                icon: 'none',
                duration: 2000
            });
        });
    },

    /**
     * 选择问题类型
     */
    onTypeChange(e) {
        const index = e.detail.value;
        this.setData({
            typeIndex: index,
            selectedType: this.data.issueTypes[index]
        });
    },

    /**
     * 选择图片
     */
    chooseImage() {
        const that = this;
        const remainCount = this.data.maxImageCount - this.data.imageList.length;

        wx.chooseImage({
            count: remainCount,
            sizeType: ['compressed'],
            sourceType: ['album', 'camera'],
            success: (res) => {
                wx.showLoading({
                    title: '上传中...',
                    mask: true
                });

                // 上传所有选中的图片
                const uploadPromises = res.tempFilePaths.map(filePath => {
                    return api.uploadImage(filePath);
                });

                Promise.all(uploadPromises)
                    .then(urls => {
                        wx.hideLoading();
                        const newImageList = that.data.imageList.concat(urls);
                        that.setData({
                            imageList: newImageList
                        });
                        wx.showToast({
                            title: '上传成功',
                            icon: 'success',
                            duration: 1500
                        });
                    })
                    .catch(err => {
                        wx.hideLoading();
                        console.error('图片上传失败', err);
                        wx.showToast({
                            title: '上传失败',
                            icon: 'none',
                            duration: 2000
                        });
                    });
            }
        });
    },

    /**
     * 删除图片
     */
    deleteImage(e) {
        const index = e.currentTarget.dataset.index;
        const imageList = this.data.imageList;
        imageList.splice(index, 1);
        this.setData({
            imageList: imageList
        });
    },

    /**
     * 开始录音
     */
    startRecord() {
        const recorderManager = this.recorderManager;
        const options = {
            format: config.recorderConfig.format,
            sampleRate: config.recorderConfig.sampleRate,
            numberOfChannels: config.recorderConfig.numberOfChannels,
            encodeBitRate: config.recorderConfig.encodeBitRate,
            duration: config.recorderConfig.maxDuration
        };

        recorderManager.start(options);
    },

    /**
     * 停止录音
     */
    stopRecord() {
        this.recorderManager.stop();
    },

    /**
     * 取消录音
     */
    cancelRecord() {
        this.recorderManager.stop();
        this.setData({
            isRecording: false,
            voiceButtonText: '按住说话'
        });
    },

    /**
     * 选择位置
     */
    chooseLocation() {
        wx.chooseLocation({
            success: (res) => {
                console.log('选择位置', res);
                this.setData({
                    location: {
                        name: res.name,
                        address: res.address,
                        latitude: res.latitude,
                        longitude: res.longitude
                    }
                });
            },
            fail: (err) => {
                console.error('选择位置失败', err);
                if (err.errMsg.indexOf('auth deny') !== -1) {
                    wx.showModal({
                        title: '提示',
                        content: '需要授权位置信息才能选择位置',
                        confirmText: '去授权',
                        success: (res) => {
                            if (res.confirm) {
                                wx.openSetting();
                            }
                        }
                    });
                }
            }
        });
    },

    /**
     * 输入文字描述
     */
    onDescriptionInput(e) {
        this.setData({
            description: e.detail.value
        });
    },

    /**
     * 提交问题
     */
    submitIssue() {
        // 验证表单
        if (this.data.typeIndex < 0) {
            wx.showToast({
                title: '请选择问题类型',
                icon: 'none',
                duration: 2000
            });
            return;
        }

        // 构建提交数据，严格对应后端 IssueDTO
        const issueData = {
            type: this.data.selectedType,
            content: this.buildContent(),
            mediaUrls: this.data.imageList
        };

        console.log('提交数据:', issueData);

        // 设置提交状态
        this.setData({
            submitting: true
        });

        // 调用API提交
        api.reportIssue(issueData)
            .then(res => {
                console.log('提交成功', res);
                wx.showToast({
                    title: '提交成功！',
                    icon: 'success',
                    duration: 2000
                });

                // 重置表单
                setTimeout(() => {
                    this.resetForm();
                }, 2000);
            })
            .catch(err => {
                console.error('提交失败', err);
                this.setData({
                    submitting: false
                });
            });
    },

    /**
     * 构建内容字段
     * 将语音内容和文字描述组合
     */
    buildContent() {
        let content = '';

        // 添加语音内容
        if (this.data.voiceRecorded && this.data.voiceContent) {
            content += this.data.voiceContent;
        }

        // 添加文字描述
        if (this.data.description) {
            if (content) {
                content += '\n\n';
            }
            content += this.data.description;
        }

        // 添加位置信息
        if (this.data.location) {
            if (content) {
                content += '\n\n';
            }
            content += `位置: ${this.data.location.name}\n地址: ${this.data.location.address}`;
        }

        // 如果没有任何内容，返回默认文本
        if (!content) {
            content = '用户通过随手拍上报问题';
        }

        return content;
    },

    /**
     * 重置表单
     */
    resetForm() {
        this.setData({
            typeIndex: -1,
            selectedType: '',
            imageList: [],
            isRecording: false,
            voiceRecorded: false,
            voiceButtonText: '按住说话',
            voiceFilePath: '',
            voiceContent: '',
            location: null,
            description: '',
            submitting: false
        });
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {
        // 清理录音管理器
        if (this.recorderManager) {
            this.recorderManager.onStart(null);
            this.recorderManager.onStop(null);
            this.recorderManager.onError(null);
        }
    }
});
