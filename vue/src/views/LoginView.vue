<template>
<div style="width: 100%; height: 100vh; background-color: darkslateblue; overflow: hidden">
  <div style="width: 500px; margin: 20vh auto">
    <div style="color: #cccccc; font-size: 30px; text-align: center; padding: 30px">
      欢迎登录
    </div>
    <el-form ref="form" :model="form" size="large" style="width: 100%;">
      <el-form-item prop="username">
        <el-input v-model="form.username" prefix-icon="User-filled"/>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" prefix-icon="Lock" show-password/>
      </el-form-item>
      <el-form-item>
        <el-button style="width: 45%; margin-right: 38px" type="primary" @click="login">登 录</el-button>
        <el-button style="width: 45%" type="primary" @click="register">注 册</el-button>
      </el-form-item>
    </el-form>
  </div>
</div>
</template>

<script>
import request from "@/utils/request";

export default {
  name:"Login",
  data() {
    return {
      form: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          request.post("/user/login",this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: 'success',
                message: "登录成功"
              })
              sessionStorage.setItem("user",JSON.stringify(res.data))
              this.$router.push("/user")  //跳转到首页
            } else {
              this.$message({
                type: 'error',
                message: res.msg
              })
            }
          })
        }
      })
    },
    register() {
      this.$router.push("/register")
    }
  }
}
</script>

<style scoped>

</style>