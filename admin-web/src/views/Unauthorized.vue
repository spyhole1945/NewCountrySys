<template>
  <div class="unauthorized-container">
    <div class="unauthorized-content">
      <el-result
        icon="warning"
        title="权限不足"
        sub-title="抱歉,您没有权限访问此页面"
      >
        <template #extra>
          <el-button type="primary" @click="goBack">返回</el-button>
          <el-button @click="logout">退出登录</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const goBack = () => {
  router.back()
}

const logout = async () => {
  await authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.unauthorized-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.unauthorized-content {
  background: white;
  padding: 60px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
