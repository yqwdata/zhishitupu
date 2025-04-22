<template>
  <div>
    <el-card style="width: 40%; margin: 50px auto">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname"/>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age"/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio value="男">男</el-radio>
            <el-radio value="女">女</el-radio>
            <el-radio value="未知">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" show-password/>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保 存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: 'PersonView',
  data() {
    return {
      form: {},
      rules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        age: [
          { required: true, message: '请输入年龄', trigger: 'blur' },
          { pattern: /^\d+$/, message: '年龄必须为数字', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    let str = sessionStorage.getItem('user') || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    update() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          request.put("/user", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: 'success',
                message: '更新成功'
              })
              sessionStorage.setItem("user", JSON.stringify(this.form))
            } else {
              this.$message({
                type: 'error',
                message: res.msg
              })
            }
          })
        } else {
          this.$message({
            type: 'error',
            message: '请检查输入是否正确'
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>