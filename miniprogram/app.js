// app.js
App({
  globalData: {
    userInfo: null,
    token: null,
    apiBaseUrl: 'http://192.168.0.107:8080' // 开发环境，生产环境需要修改
  },

  onLaunch() {
    // 小程序启动时执行
    console.log('乡村同行小程序启动');

    // 检查本地存储的token
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.token = token;
    }

    // 检查本地存储的用户信息
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.globalData.userInfo = userInfo;
    }
  },

  onShow() {
    // 小程序显示时执行
  },

  onHide() {
    // 小程序隐藏时执行
  },

  onError(msg) {
    console.error('小程序错误:', msg);
  },

  // 全局方法：设置用户信息
  setUserInfo(userInfo) {
    this.globalData.userInfo = userInfo;
    wx.setStorageSync('userInfo', userInfo);
  },

  // 全局方法：设置token
  setToken(token) {
    this.globalData.token = token;
    wx.setStorageSync('token', token);
  },

  // 全局方法：清除用户信息
  clearUserInfo() {
    this.globalData.userInfo = null;
    this.globalData.token = null;
    wx.removeStorageSync('userInfo');
    wx.removeStorageSync('token');
  }
});
