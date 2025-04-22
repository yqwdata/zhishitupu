<script>
export default {
  name: 'Aside'
}
</script>

<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 50px)"
        default-active="/"
        router
        :default-openeds="['1']"
        class="el-menu-vertical-demo"
    >
      <el-sub-menu index="1">
        <template #title>
          <el-icon><Management /></el-icon>
          <span>系统管理</span>
        </template>
        <el-menu-item index="user">
          <el-icon><user/></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="todo">
          <el-icon><List /></el-icon>
          <span>事件管理</span>
        </el-menu-item>
      </el-sub-menu>
      <el-menu-item index="chat">
        <el-icon><ChatDotRound /></el-icon>
        <span>AI助手</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<style scoped>

</style>