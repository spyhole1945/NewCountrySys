// src/stores/auth.js
import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi } from '../api/auth'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('admin_token') || '',
        userInfo: JSON.parse(localStorage.getItem('admin_user') || 'null')
    }),

    getters: {
        isLoggedIn: (state) => !!state.token,
        username: (state) => state.userInfo?.username || '',
        role: (state) => state.userInfo?.role || ''
    },

    actions: {
        async login(username, password) {
            try {
                const data = await loginApi({ username, password })
                this.token = data.token
                this.userInfo = data.userInfo

                // 保存到localStorage
                localStorage.setItem('admin_token', data.token)
                localStorage.setItem('admin_user', JSON.stringify(data.userInfo))

                return data
            } catch (error) {
                throw error
            }
        },

        async logout() {
            try {
                await logoutApi()
            } catch (error) {
                console.error('登出失败:', error)
            } finally {
                this.token = ''
                this.userInfo = null
                localStorage.removeItem('admin_token')
                localStorage.removeItem('admin_user')
            }
        }
    }
})
