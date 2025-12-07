const api = require('../../utils/api.js')

Page({
    data: {
        posts: [],
        loading: false,
        page: 1,
        hasMore: true
    },

    onLoad() {
        // Initial load handled by onShow
    },

    onShow() {
        this.setData({
            posts: [],
            page: 1,
            hasMore: true
        })
        this.loadPosts()
    },

    // 加载动态列表
    async loadPosts() {
        if (this.data.loading || !this.data.hasMore) return

        this.setData({ loading: true })

        try {
            const res = await api.getSocialPosts({
                page: this.data.page,
                size: 10
            })

            const newPosts = res.data || []
            console.log('Fetched posts:', newPosts)

            this.setData({
                posts: this.data.page === 1 ? newPosts : [...this.data.posts, ...newPosts],
                hasMore: newPosts.length >= 10,
                page: this.data.page + 1,
                loading: false
            })
        } catch (error) {
            console.error('加载动态失败:', error)
            wx.showToast({
                title: '加载失败',
                icon: 'none'
            })
            this.setData({ loading: false })
        }
    },

    // 点赞/取消点赞
    async toggleLike(e) {
        const { id, liked } = e.currentTarget.dataset

        try {
            if (liked) {
                await api.unlikePost(id)
            } else {
                const res = await api.likePost(id)

                // 显示积分奖励
                if (res.data && res.data.pointsAwarded) {
                    wx.showToast({
                        title: `互动积分 +${res.data.pointsAwarded}`,
                        icon: 'success'
                    })
                }
            }

            // 更新本地状态
            const posts = this.data.posts.map(post => {
                if (post.id === id) {
                    return {
                        ...post,
                        liked: !liked,
                        likeCount: liked ? (post.likeCount - 1) : (post.likeCount + 1)
                    }
                }
                return post
            })

            this.setData({ posts })
        } catch (error) {
            console.error('点赞失败:', error)
            wx.showToast({
                title: '操作失败',
                icon: 'none'
            })
        }
    },

    // 显示评论输入框
    showCommentInput(e) {
        const id = e.currentTarget.dataset.id

        wx.showModal({
            title: '发表评论',
            editable: true,
            placeholderText: '说点什么...',
            success: async (res) => {
                if (res.confirm && res.content) {
                    await this.submitComment(id, res.content)
                }
            }
        })
    },

    // 提交评论
    async submitComment(postId, content) {
        try {
            const res = await api.commentPost(postId, { content })

            // 显示积分奖励
            if (res.data && res.data.pointsAwarded) {
                wx.showToast({
                    title: `互动积分 +${res.data.pointsAwarded}`,
                    icon: 'success'
                })
            }

            // 刷新列表
            this.setData({
                posts: [],
                page: 1,
                hasMore: true
            })
            this.loadPosts()
        } catch (error) {
            console.error('评论失败:', error)
            wx.showToast({
                title: '评论失败',
                icon: 'none'
            })
        }
    },

    // 预览图片
    previewImage(e) {
        const { urls, current } = e.currentTarget.dataset
        wx.previewImage({
            urls: urls,
            current: current
        })
    },

    // 去发布
    goPublish() {
        wx.navigateTo({
            url: '/pages/social/create'
        })
    },

    // 加载更多
    loadMore() {
        this.loadPosts()
    },

    // 下拉刷新
    onPullDownRefresh() {
        this.setData({
            posts: [],
            page: 1,
            hasMore: true
        })
        this.loadPosts()
        wx.stopPullDownRefresh()
    }
})
