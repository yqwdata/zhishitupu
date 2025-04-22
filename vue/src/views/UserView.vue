<template>
  <div style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
    <!--    搜索区-->
    <div style="margin: 10px 0">
      <el-input v-model="search" style="width: 20%" placeholder="请输入关键字" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="100"/>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="nickname" label="昵称"/>
      <el-table-column prop="age" label="年龄"/>
      <el-table-column prop="sex" label="性别"/>
      <el-table-column prop="address" label="地址"/>
      <el-table-column label="头像">
        <template #default="scope">
          <el-avatar :size="40" :src="scope.row.avatar" />
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" min-width="150">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" style="width: 50px" type="primary" @click="handleTodo(scope.row)">待办事件</el-button>
          <el-popconfirm title="确定删除这一段内容吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          :size="PageSize"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
      <el-dialog v-model="dialogVisible" title="提示" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model.number="form.age" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="form.sex" value="男">男</el-radio>
            <el-radio v-model="form.sex" value="女">女</el-radio>
            <el-radio v-model="form.sex" value="未知">未知</el-radio>
          </el-form-item>
          <el-form-item label="地址">
            <el-input type="textarea" v-model="form.address" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="头像">
            <el-upload
                action="http://localhost:9090/files/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
            >
              <el-avatar :size="40" :src="form.avatar" />
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">
              确 定
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: 'User',
  computed: {
    search() {
      return search
    }
  },
  components: {},

  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      PageSize: 10,
      total: 0,
      tableData: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      request.get("/user", {
        params: {
          PageNum: this.currentPage,
          PageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.dialogVisible = true;
      this.form = {};
    },
    save() {
      if (this.form.id) {
        request.put("/user", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: 'success',
              message: "更新成功"
            })
            sessionStorage.setItem("user", JSON.stringify(this.form))
          } else {
            this.$message({
              type: 'error',
              message: res.msg
            })
          }
          this.load()  //刷新表格数据
          this.dialogVisible = false
        })
      } else {
        request.post("/user", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: 'success',
              message: "新增成功"
            })
          } else {
            this.$message({
              type: 'error',
              message: res.msg
            })
          }
          this.load()  //刷新表格数据
          this.dialogVisible = false
        })
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleTodo(row) {
      this.$router.push({ path: '/todo', query: { userId: row.id } });
    },
    handleDelete(id) {
      console.log(id)
      request.delete("/user/" + id).then(res => {
        if (res.code === '0') {
          this.$message({
            type: 'success',
            message: "删除成功"
          })
        } else {
          this.$message({
            type: 'error',
            message: res.msg
          })
        }
        this.load()
      })
    },
    handleSizeChange(PageSize) {  //改变当前每页个数触发
      this.pageSize = PageSize
      this.load()
    },
    handleCurrentChange(PageNum) { //改变当前页码触发
      this.currentPage = PageNum
      this.load()
    },
    handleAvatarSuccess(res, file) {
      this.form.avatar = res.data;
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPNG) {
        this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return (isJPG || isPNG) && isLt2M;
    }
  }
}
</script>
