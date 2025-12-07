<template>
  <div class="user-management">
    <h2 class="page-title">用户管理</h2>
    
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="全部" clearable style="width: 120px">
            <el-option label="村民" value="VILLAGER" />
            <el-option label="村务人员" value="OFFICIAL" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="用户名/昵称" clearable style="width: 200px" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="loadUsers">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 用户列表 -->
    <el-card class="table-card">
      <el-table :data="userList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)">
              {{ getRoleText(row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="points" label="积分" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ row.status === 'ACTIVE' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="editRole(row)">修改角色</el-button>
            <el-button
              size="small"
              :type="row.status === 'ACTIVE' ? 'danger' : 'success'"
              @click="toggleStatus(row)"
            >
              {{ row.status === 'ACTIVE' ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          layout="total, prev, pager, next"
          @current-change="loadUsers"
        />
      </div>
    </el-card>
    
    <!-- 修改角色对话框 -->
    <el-dialog v-model="roleVisible" title="修改用户角色" width="400px">
      <el-form :model="roleForm" label-width="80px">
        <el-form-item label="用户">
          <el-text>{{ currentUser?.nickname }} ({{ currentUser?.username }})</el-text>
        </el-form-item>
        
        <el-form-item label="角色">
          <el-select v-model="roleForm.role" placeholder="请选择角色">
            <el-option label="村民" value="VILLAGER" />
            <el-option label="村务人员" value="OFFICIAL" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="roleVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRole" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, updateUserRole, toggleUserStatus } from '../api/user'
import { USER_ROLES } from '../utils/constants'

const loading = ref(false)
const userList = ref([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})

const searchForm = ref({
  role: '',
  keyword: ''
})

const roleVisible = ref(false)
const currentUser = ref(null)
const submitting = ref(false)

const roleForm = ref({
  role: ''
})

const getRoleType = (role) => {
  const types = {
    VILLAGER: 'info',
    OFFICIAL: 'warning',
    ADMIN: 'danger'
  }
  return types[role] || 'info'
}

const getRoleText = (role) => {
  return USER_ROLES[role] || role
}

const loadUsers = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.value.page,
      size: pagination.value.size,
      ...searchForm.value
    }
    const data = await getUserList(params)
    userList.value = data.records
    pagination.value.total = data.total
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.value = { role: '', keyword: '' }
  pagination.value.page = 1
  loadUsers()
}

const editRole = (row) => {
  currentUser.value = row
  roleForm.value.role = row.role
  roleVisible.value = true
}

const submitRole = async () => {
  if (!roleForm.value.role) {
    ElMessage.warning('请选择角色')
    return
  }
  
  submitting.value = true
  try {
    await updateUserRole(currentUser.value.id, roleForm.value)
    ElMessage.success('修改成功')
    roleVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error('修改失败')
  } finally {
    submitting.value = false
  }
}

const toggleStatus = async (row) => {
  const action = row.status === 'ACTIVE' ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(`确定要${action}该用户吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await toggleUserStatus(row.id, row.status !== 'ACTIVE')
    ElMessage.success(`${action}成功`)
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${action}失败`)
    }
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.page-title {
  margin: 0 0 20px 0;
  font-size: 24px;
  color: #303133;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
