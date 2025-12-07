// utils/api.js
// API接口定义

const { get, post, put, del } = require('./request.js');

/**
 * ========== 问题上报相关接口 ==========
 */

// 上报问题
function reportIssue(data) {
    return post('/api/issues', data);
}

// 获取问题时间线
function getIssueTimeline(issueId) {
    return get(`/api/issues/${issueId}/timeline`);
}

/**
 * ========== 社区动态相关接口 ==========
 */

// 获取社区动态列表
function getCommunityPosts() {
    return get('/community-posts');
}

// 发布社区动态
function createCommunityPost(data) {
    return post('/community-posts', data);
}

/**
 * ========== 农产品市场相关接口 ==========
 */

// 获取农产品列表
function getMarketItems() {
    return get('/market-items');
}

// 发布农产品
function createMarketItem(data) {
    return post('/market-items', data);
}

/**
 * ========== 文件上传相关接口 ==========
 */

// 上传图片（Mock实现）
function uploadImage(filePath) {
    return new Promise((resolve, reject) => {
        // TODO: 实际项目中需要实现真实的图片上传
        // 这里暂时返回mock数据

        // 模拟上传延迟
        setTimeout(() => {
            // 返回mock的图片URL
            const mockUrl = `https://mock-cdn.ruralsync.com/images/${Date.now()}.jpg`;
            resolve(mockUrl);
        }, 500);

        // 真实上传实现示例：
        /*
        const app = getApp();
        const token = app.globalData.token;
        
        wx.uploadFile({
          url: config.apiBaseUrl + '/api/upload/image',
          filePath: filePath,
          name: 'file',
          header: {
            'Authorization': token ? `Bearer ${token}` : ''
          },
          success: (res) => {
            const data = JSON.parse(res.data);
            if (data.code === 200) {
              resolve(data.data.url);
            } else {
              reject(data.message);
            }
          },
          fail: (err) => {
            reject(err);
          }
        });
        */
    });
}

module.exports = {
    // 问题上报
    reportIssue,
    getIssueTimeline,

    // 社区动态
    getCommunityPosts,
    createCommunityPost,

    // 农产品市场
    getMarketItems,
    createMarketItem,

    // 文件上传
    uploadImage
};
