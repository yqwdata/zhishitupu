<template>
  <div style="width: 100%; height: 100vh; background-color: darkslateblue; overflow: hidden">
    <div style="width: 500px; margin: 20vh auto">
      <div style="color: #cccccc; font-size: 30px; text-align: center; padding: 30px">
        欢迎注册
      </div>
      <el-form ref="form" :model="form" size="large" style="width: 100%;" :rules="rules">
        <el-form-item prop="username">
          <el-input v-model="form.username" prefix-icon="User-filled"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item prop="confirm">
          <el-input v-model="form.confirm" prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 45%; margin-right: 38px" type="primary" @click="register">注 册</el-button>
          <el-button style="width: 45%" type="primary" @click="back">返 回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Register",
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
        confirm: [
          {required: true, message: '请确认密码', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    register() {
      if (this.form.password !== this.form.confirm) {
        this.$message({
          type: 'error',
          message: "两次密码输入不一致"
        })
        return
      }
      this.$refs['form'].validate(valid => {
        if (valid) {
          request.post("/user/register", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: 'success',
                message: "注册成功"
              })
              this.$router.push("/login")  //跳转到首页
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
    back(){
      this.$router.push("/login")
    }
  }
}
</script>

<style scoped>

</style>