// utils/config.js
// 全局配置文件

const config = {
    // API基础URL - 开发环境
    // 生产环境需要修改为实际的服务器地址
    apiBaseUrl: 'http://localhost:8080',

    // 图片上传相关配置
    uploadConfig: {
        // 图片上传接口（暂时使用mock）
        uploadUrl: 'http://localhost:8080/api/upload/image',
        // 允许的图片格式
        allowedImageTypes: ['jpg', 'jpeg', 'png'],
        // 最大图片大小（字节）- 5MB
        maxImageSize: 5 * 1024 * 1024,
        // 最大上传数量
        maxImageCount: 9
    },

    // 语音录制配置
    recorderConfig: {
        // 录音格式
        format: 'mp3',
        // 采样率
        sampleRate: 16000,
        // 录音通道数
        numberOfChannels: 1,
        // 编码码率
        encodeBitRate: 48000,
        // 最大录音时长（毫秒）- 60秒
        maxDuration: 60000
    },

    // 问题类型配置
    issueTypes: [
        '道路损坏',
        '垃圾堆积',
        '路灯故障',
        '水利设施',
        '环境污染',
        '其他问题'
    ],

    // 请求超时时间（毫秒）
    requestTimeout: 10000,

    // 分页配置
    pageSize: 10
};

module.exports = config;
