




<template>
  <div>
    <el-card style="width: 80%; margin: 20px auto">
      <div style="font-size: 30px; text-align: center;margin: 10px auto; color: cornflowerblue">待办事项</div>
      <div style="margin-bottom: 20px;">
        <el-input
          v-model="newTask"
          placeholder="需要做什么？"
          class="input-field"
          maxlength="50"
          style="width: calc(100% - 120px); margin-right: 10px; height: 40px"
        />
        <el-button type="primary" style="height: 40px" @click="addTask">添加</el-button>
      </div>
      <el-alert v-if="showError" type="error" :closable="false">任务不能为空！</el-alert>

      <div style="font-size: 30px; text-align: center;margin: 10px auto; color: cornflowerblue">
        已完成 {{ completedTasksCount }} 项，共 {{ tasks.length }} 项
      </div>

      <div v-for="(task, index) in tasks.filter(task => task)" :key="task.id" class="task" style="margin: 10px;">
        <div class="task-row" style="display: flex; align-items: center;">
          <el-checkbox v-model="task.completed" @change="updateTask(task)" style="margin-right: 10px;" />
          <span class="task-name" v-if="!task.isEditing" style="flex: 1;">{{ task.task }}</span>
          <el-input
            v-if="task.isEditing"
            v-model="task.task"
            class="edit-input"
            style="flex: 1; margin-right: 10px;"
          />
        </div>

        <div class="button-group" style="margin-top: 10px;">
          <el-button
            v-if="!task.isEditing"
            type="primary"
            @click="editTask(index)"
            style="margin-right: 5px;"
          >
            编辑
          </el-button>
          <el-button
            v-if="!task.isEditing"
            type="danger"
            @click="deleteTask(task.id)"
          >
            删除
          </el-button>
          <el-button
            v-if="task.isEditing"
            type="primary"
            @click="saveTask(task)"
            style="margin-right: 5px;"
          >
            保存
          </el-button>
          <el-button
            v-if="task.isEditing"
            @click="cancelEdit(index)"
          >
            取消
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Todo",
  data() {
    return {
      newTask: '',
      tasks: [],
      editIndex: null,
      previousTaskName: '',
      showError: false,
    };
  },
  computed: {
    completedTasksCount() {
      return this.tasks.filter(task => task && task.completed).length;
    },
  },
  created() {
    const userId = this.$route.query.userId;
    if (userId) {
        this.loadTasks(userId);
    } else {
        const user = JSON.parse(sessionStorage.getItem('user') || "{}");
        if (user.id) {
            this.loadTasks(user.id);
        }
    }
  },
  methods: {
    loadTasks(userId) {
        if (userId) {
            request.get(`/todo/user/${userId}`).then(res => {
                this.tasks = res.data || [];
            });
        }
    },
    addTask() {
        const user = JSON.parse(sessionStorage.getItem('user') || "{}");
        const userId = this.$route.query.userId || user.id;  // 优先使用传递的 userId，否则使用当前登录用户的 userId
        if (this.newTask.trim()) {
            const task = { task: this.newTask, completed: false, userId: userId };
            request.post("/todo", task).then(res => {
                if (res.code === '0') {
                    this.tasks.push(res.data);
                    this.newTask = '';
                    this.showError = false;
                    this.loadTasks(userId);
                }
            });
        } else {
            this.showError = true;
        }
    },
    toggleEditState(index, isEditing) {
      this.tasks[index].isEditing = isEditing;
      if (isEditing) {
        this.previousTaskName = this.tasks[index].task;
      }
    },
    editTask(index) {
      this.toggleEditState(index, true);
    },
    saveTask(task) {
      request.put("/todo", task).then(res => {
        if (res.code === '0') {
          this.toggleEditState(this.tasks.indexOf(task), false);
        }
      });
    },
    cancelEdit(index) {
      this.tasks[index].task = this.previousTaskName;
      this.toggleEditState(index, false);
    },
    deleteTask(id) {
      request.delete(`/todo/${id}`).then(res => {
        if (res.code === '0') {
          this.tasks = this.tasks.filter(task => task.id !== id);
        }
      });
    },
    updateTask(task) {
      request.put("/todo", task);
    }
  },
};
</script>

<style>
.task {
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 10px;
}
</style>