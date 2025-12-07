// pages/index/index.js
Page({
    data: {

    },

    onLoad(options) {

    },

    // 跳转到随手拍
    goToReport() {
        wx.switchTab({
            url: '/pages/report/report'
        });
    },

    // 跳转到村友圈
    goToSocial() {
        wx.navigateTo({
            url: '/pages/social/index'
        });
    },

    // 跳转到找干部
    goToProfile() {
        wx.switchTab({
            url: '/pages/profile/profile'
        });
    }
});
