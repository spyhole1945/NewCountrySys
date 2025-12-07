<template>
  <div class="issue-management">
    <h2 class="page-title">问题管理</h2>
    
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已完成" value="DONE" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="loadIssues">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 问题列表 -->
    <el-card class="table-card">
      <el-table :data="issueList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="reporterName" label="上报人" width="120" />
        <el-table-column prop="type" label="问题类型" width="120" />
        <el-table-column prop="content" label="问题描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="上报时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetail(row)">详情</el-button>
            <el-button size="small" type="primary" @click="handleIssue(row)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadIssues"
          @current-change="loadIssues"
        />
      </div>
    </el-card>
    
    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="问题详情" width="600px">
      <el-descriptions :column="1" border v-if="currentIssue">
        <el-descriptions-item label="ID">{{ currentIssue.id }}</el-descriptions-item>
        <el-descriptions-item label="上报人">{{ currentIssue.reporterName }}</el-descriptions-item>
        <el-descriptions-item label="问题类型">{{ currentIssue.type }}</el-descriptions-item>
        <el-descriptions-item label="问题描述">{{ currentIssue.content }}</el-descriptions-item>
        <el-descriptions-item label="位置">{{ currentIssue.location }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentIssue.status)">
            {{ getStatusText(currentIssue.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="上报时间">{{ currentIssue.createTime }}</el-descriptions-item>
      </el-descriptions>
      
      <div v-if="currentIssue?.mediaUrls?.length" style="margin-top: 20px;">
        <h4>相关图片:</h4>
        <el-image
          v-for="(url, index) in currentIssue.mediaUrls"
          :key="index"
          :src="url"
          :preview-src-list="currentIssue.mediaUrls"
          style="width: 100px; height: 100px; margin-right: 10px;"
          fit="cover"
        />
      </div>
    </el-dialog>
    
    <!-- 处理对话框 -->
    <el-dialog v-model="handleVisible" title="处理问题" width="500px">
      <el-form :model="handleForm" label-width="80px">
        <el-form-item label="状态">
          <el-select v-model="handleForm.status" placeholder="请选择状态">
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已完成" value="DONE" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="处理意见">
          <el-input
            v-model="handleForm.processNotes"
            type="textarea"
            :rows="4"
            placeholder="请输入处理意见"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandle" :loading="submitting">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getIssueList, getIssueDetail, updateIssueStatus } from '../api/issue'
import { ISSUE_STATUS } from '../utils/constants'

const loading = ref(false)
const issueList = ref([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})

const searchForm = ref({
  status: ''
})

const detailVisible = ref(false)
const handleVisible = ref(false)
const currentIssue = ref(null)
const submitting = ref(false)

const handleForm = ref({
  status: '',
  processNotes: ''
})

const getStatusType = (status) => {
  const types = {
    PENDING: 'warning',
    PROCESSING: 'info',
    DONE: 'success'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  return ISSUE_STATUS[status] || status
}

const loadIssues = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.value.page,
      size: pagination.value.size,
      ...searchForm.value
    }
    const data = await getIssueList(params)
    issueList.value = data.records
    pagination.value.total = data.total
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.value = { status: '' }
  pagination.value.page = 1
  loadIssues()
}

const viewDetail = async (row) => {
  try {
    currentIssue.value = await getIssueDetail(row.id)
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('加载详情失败')
  }
}

const handleIssue = (row) => {
  currentIssue.value = row
  handleForm.value = {
    status: row.status === 'PENDING' ? 'PROCESSING' : 'DONE',
    processNotes: ''
  }
  handleVisible.value = true
}

const submitHandle = async () => {
  if (!handleForm.value.status) {
    ElMessage.warning('请选择状态')
    return
  }
  
  submitting.value = true
  try {
    await updateIssueStatus(currentIssue.value.id, handleForm.value)
    ElMessage.success('处理成功')
    handleVisible.value = false
    loadIssues()
  } catch (error) {
    ElMessage.error('处理失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadIssues()
})
</script>

<style scoped>
.issue-management {
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
