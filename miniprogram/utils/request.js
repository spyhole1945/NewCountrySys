// utils/request.js
// HTTP请求封装

const config = require('./config.js');

/**
 * 封装的HTTP请求方法
 * @param {Object} options - 请求配置
 * @param {String} options.url - 请求URL（相对路径）
 * @param {String} options.method - 请求方法（GET/POST/PUT/DELETE）
 * @param {Object} options.data - 请求数据
 * @param {Boolean} options.showLoading - 是否显示加载提示
 * @param {String} options.loadingText - 加载提示文字
 * @returns {Promise}
 */
function request(options) {
    const {
        url,
        method = 'GET',
        data = {},
        showLoading = true,
        loadingText = '加载中...'
    } = options;

    // 显示加载提示
    if (showLoading) {
        wx.showLoading({
            title: loadingText,
            mask: true
        });
    }

    // 获取token
    const app = getApp();
    const token = app.globalData.token;

    // 构建完整URL
    const fullUrl = config.apiBaseUrl + url;

    return new Promise((resolve, reject) => {
        wx.request({
            url: fullUrl,
            method: method,
            data: data,
            header: {
                'Content-Type': 'application/json',
                'Authorization': token ? `Bearer ${token}` : ''
            },
            timeout: config.requestTimeout,
            success: (res) => {
                // 隐藏加载提示
                if (showLoading) {
                    wx.hideLoading();
                }

                // 处理响应
                if (res.statusCode === 200) {
                    // 后端返回的Result对象格式：{ code, message, data }
                    if (res.data.code === 200 || res.data.code === 0) {
                        resolve(res.data.data);
                    } else {
                        // 业务错误
                        wx.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none',
                            duration: 2000
                        });
                        reject(res.data);
                    }
                } else if (res.statusCode === 401) {
                    // 未授权，清除token并跳转到登录页
                    app.clearUserInfo();
                    wx.showToast({
                        title: '请先登录',
                        icon: 'none',
                        duration: 2000
                    });
                    // TODO: 跳转到登录页
                    // wx.navigateTo({ url: '/pages/login/login' });
                    reject(res.data);
                } else {
                    // HTTP错误
                    wx.showToast({
                        title: `请求失败(${res.statusCode})`,
                        icon: 'none',
                        duration: 2000
                    });
                    reject(res.data);
                }
            },
            fail: (err) => {
                // 隐藏加载提示
                if (showLoading) {
                    wx.hideLoading();
                }

                // 网络错误
                wx.showToast({
                    title: '网络连接失败',
                    icon: 'none',
                    duration: 2000
                });
                console.error('请求失败:', err);
                reject(err);
            }
        });
    });
}

/**
 * GET请求
 */
function get(url, data = {}, showLoading = true) {
    return request({
        url,
        method: 'GET',
        data,
        showLoading
    });
}

/**
 * POST请求
 */
function post(url, data = {}, showLoading = true) {
    return request({
        url,
        method: 'POST',
        data,
        showLoading
    });
}

/**
 * PUT请求
 */
function put(url, data = {}, showLoading = true) {
    return request({
        url,
        method: 'PUT',
        data,
        showLoading
    });
}

/**
 * DELETE请求
 */
function del(url, data = {}, showLoading = true) {
    return request({
        url,
        method: 'DELETE',
        data,
        showLoading
    });
}

module.exports = {
    request,
    get,
    post,
    put,
    del
};
