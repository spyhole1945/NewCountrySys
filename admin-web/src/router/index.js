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
        meta: { requiresAuth: true, requiresAdmin: true },
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
                meta: { title: '用户管理', requiresAdmin: true }
            }
        ]
    },
    {
        path: '/unauthorized',
        name: 'Unauthorized',
        component: () => import('../views/Unauthorized.vue'),
        meta: { requiresAuth: false }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫 - 增强版
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    // 需要登录但未登录
    if (to.meta.requiresAuth && !authStore.isLoggedIn) {
        next('/login')
        return
    }

    // 已登录访问登录页
    if (to.path === '/login' && authStore.isLoggedIn) {
        next('/dashboard')
        return
    }

    // 检查是否需要管理员权限
    if (to.meta.requiresAdmin && authStore.role !== 'ADMIN') {
        // 非ADMIN角色访问需要管理员权限的页面
        next('/unauthorized')
        return
    }

    // 检查角色是否允许访问后台
    if (to.meta.requiresAuth && authStore.isLoggedIn) {
        const role = authStore.role
        // OFFICIAL和VILLAGER不允许访问后台
        if (role === 'OFFICIAL' || role === 'VILLAGER') {
            authStore.logout()
            next('/login?error=role_not_allowed')
            return
        }
    }

    next()
})

export default router
