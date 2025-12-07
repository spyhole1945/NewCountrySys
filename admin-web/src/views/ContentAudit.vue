<template>
  <div class="content-audit">
    <h2 class="page-title">内容审核</h2>
    
    <el-card>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="社区动态" name="posts">
          <el-table :data="contentList" v-loading="loading" stripe>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userName" label="发布人" width="120" />
            <el-table-column prop="content" label="内容" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'APPROVED' ? 'success' : 'warning'">
                  {{ row.status === 'APPROVED' ? '已通过' : '待审核' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="发布时间" width="180" />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button
                  v-if="row.status === 'PENDING_REVIEW'"
                  size="small"
                  type="success"
                  @click="approve(row)"
                >
                  通过
                </el-button>
                <el-button size="small" type="danger" @click="deleteItem(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination">
            <el-pagination
              v-model:current-page="pagination.page"
              v-model:page-size="pagination.size"
              :total="pagination.total"
              layout="total, prev, pager, next"
              @current-change="loadContent"
            />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="集市商品" name="market">
          <el-table :data="contentList" v-loading="loading" stripe>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userName" label="发布人" width="120" />
            <el-table-column prop="content" label="商品描述" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'APPROVED' ? 'success' : 'warning'">
                  {{ row.status === 'APPROVED' ? '已通过' : '待审核' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="发布时间" width="180" />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button
                  v-if="row.status === 'PENDING_REVIEW'"
                  size="small"
                  type="success"
                  @click="approve(row)"
                >
                  通过
                </el-button>
                <el-button size="small" type="danger" @click="deleteItem(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination">
            <el-pagination
              v-model:current-page="pagination.page"
              v-model:page-size="pagination.size"
              :total="pagination.total"
              layout="total, prev, pager, next"
              @current-change="loadContent"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCommunityPosts, getMarketItems, approveContent, deleteContent } from '../api/content'

const activeTab = ref('posts')
const loading = ref(false)
const contentList = ref([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})

const loadContent = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.value.page,
      size: pagination.value.size
    }
    
    let data
    if (activeTab.value === 'posts') {
      data = await getCommunityPosts(params)
    } else {
      data = await getMarketItems(params)
    }
    
    contentList.value = data.records
    pagination.value.total = data.total
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  pagination.value.page = 1
  loadContent()
}

const approve = async (row) => {
  try {
    await approveContent(row.type, row.id)
    ElMessage.success('审核通过')
    loadContent()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const deleteItem = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条内容吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteContent(row.type, row.id)
    ElMessage.success('删除成功')
    loadContent()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadContent()
})
</script>

<style scoped>
.content-audit {
  padding: 20px;
}

.page-title {
  margin: 0 0 20px 0;
  font-size: 24px;
  color: #303133;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
