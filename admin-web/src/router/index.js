// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/',
        component: () => import('../layouts/AdminLayout.vue'),
        meta: { requiresAuth: true },
        redirect: '/dashboard',
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('../views/Dashboard.vue'),
                meta: { title: '仪表盘' }
            },
            {
                path: 'issues',
                name: 'IssueManagement',
                component: () => import('../views/IssueManagement.vue'),
                meta: { title: '问题管理' }
            },
            {
                path: 'content',
                name: 'ContentAudit',
                component: () => import('../views/ContentAudit.vue'),
                meta: { title: '内容审核' }
            },
            {
                path: 'users',
                name: 'UserManagement',
                component: () => import('../views/UserManagement.vue'),
                meta: { title: '用户管理' }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    if (to.meta.requiresAuth && !authStore.isLoggedIn) {
        // 需要登录但未登录,跳转到登录页
        next('/login')
    } else if (to.path === '/login' && authStore.isLoggedIn) {
        // 已登录访问登录页,跳转到首页
        next('/dashboard')
    } else {
        next()
    }
})

export default router
