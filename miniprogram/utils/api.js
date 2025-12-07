// utils/api.js
// API接口定义

const { get, post, put, del } = require('./request.js');
const config = require('./config.js');

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
function getCommunityPosts(params) {
  return get('/api/community-posts', params);
}

// 发布社区动态
function createCommunityPost(data) {
  return post('/api/community-posts', data);
}

/**
 * ========== 村友圈相关接口 ==========
 */

// 获取村友圈动态列表
function getSocialPosts(params) {
  return get('/api/social/posts', params);
}

// 发布动态
function createSocialPost(data) {
  return post('/api/social/posts', data);
}

// 点赞动态
function likePost(postId) {
  return post(`/api/social/posts/${postId}/like`);
}

// 取消点赞
function unlikePost(postId) {
  return del(`/api/social/posts/${postId}/like`);
}

// 评论动态
function commentPost(postId, data) {
  return post(`/api/social/posts/${postId}/comments`, data);
}

/**
 * ========== 农产品市场相关接口 ==========
 */

// 获取农产品列表
function getMarketItems(params) {
  return get('/api/market-items', params);
}

// 发布农产品
function createMarketItem(data) {
  return post('/api/market-items', data);
}

/**
 * ========== 积分相关接口 ==========
 */

// 获取积分记录
function getPointsLog(params) {
  return get('/api/points/log', params);
}

// 获取用户信息(包含积分)
function getUserInfo() {
  return get('/api/user/info');
}

/**
 * ========== 农技问答相关接口 ==========
 */

// 获取问题列表
function getQuestions(params) {
  return get('/api/qa/questions', params);
}

// 获取问题详情
function getQuestionDetail(id) {
  return get(`/api/qa/questions/${id}`);
}

// 发布问题
function createQuestion(data) {
  return post('/api/qa/questions', data);
}

// 提交回答
function createAnswer(questionId, data) {
  return post(`/api/qa/questions/${questionId}/answers`, data);
}

// 采纳回答
function acceptAnswer(answerId) {
  return post(`/api/qa/answers/${answerId}/accept`);
}

/**
 * ========== 文件上传相关接口 ==========
 */

// 上传图片
function uploadImage(filePath) {
  return new Promise((resolve, reject) => {
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
        console.log('Upload success response:', res);
        try {
          // wx.uploadFile returns data as a string, need to parse it
          const data = JSON.parse(res.data);
          console.log('Parsed upload data:', data);
          if (data.code === 200 || data.code === 0) {
            // If the returned URL is relative, prepend base URL
            let url = data.data.url;
            if (url.startsWith('/')) {
              url = config.apiBaseUrl + url;
            }
            resolve(url);
          } else {
            console.error('Upload failed with code:', data.code, data.message);
            reject(data.message || '上传失败');
          }
        } catch (e) {
          console.error('JSON parse error:', e, res.data);
          reject('解析响应失败');
        }
      },
      fail: (err) => {
        console.error('Upload network fail:', err);
        reject(err.errMsg || '网络错误');
      }
    });
  });
}

module.exports = {
  // 问题上报
  reportIssue,
  getIssueTimeline,

  // 社区动态
  getCommunityPosts,
  createCommunityPost,

  // 村友圈
  getSocialPosts,
  createSocialPost,
  likePost,
  unlikePost,
  commentPost,

  // 农产品市场
  getMarketItems,
  createMarketItem,

  // 积分系统
  getPointsLog,
  getUserInfo,

  // 农技问答
  getQuestions,
  getQuestionDetail,
  createQuestion,
  createAnswer,
  acceptAnswer,

  // 文件上传
  uploadImage
};
