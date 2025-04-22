<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex;">
    <div style="width: 200px; padding-left: 30px;font-weight: bold; color: dodgerblue">后台管理</div>
    <div style="flex: 1"></div>
    <div style="width: 200px; padding-top: 10px; display: flex; align-items: center;">
      <el-upload
        action="http://localhost:9090/files/upload"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
        style="margin-right: 10px; margin-top: 10px;"
      >
        <el-avatar :size="40" :src="user.avatar" />
      </el-upload>
      <el-dropdown split-button type="primary" style="margin-left: 10px; margin-top: -5px">
          {{user.nickname}}
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";


export default {
  name: "Header",
  data() {
    return {
      user: {}
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem('user') || "{}");
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.user.avatar = res.data;
      sessionStorage.setItem('user', JSON.stringify(this.user));
      // 将头像 URL 保存到数据库
      request.put("/user", this.user).then(res => {
        if (res.code === '0') {
          this.$message({
            type: 'success',
            message: "头像更新成功"
          });
        } else {
          this.$message({
            type: 'error',
            message: res.msg
          });
        }
      });
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

<style scoped>
</style>