<template>
  <div>
    <div style="font-size: 30px; text-align: center; margin: 10px auto; color: cornflowerblue">AI 助手</div>
    <div ref="messagesContainer" class="chat-messages">
      <div v-for="(message, index) in messages" :key="index" class="message" :class="message.sender">
        <div class="message-content">
          {{ message.text }}
        </div>
      </div>
    </div>
    <el-alert v-if="showError" type="error" :closable="false">消息不能为空！</el-alert>
    <div class="chat-input">
      <el-input
          v-model="inputMessage"
          placeholder="请输入消息"
          @keyup.enter="sendMessage"
          :disabled="isLoading"
          style="width: calc(100% - 120px); margin-right: 10px; height: 40px"
      />
      <el-button
          type="primary"
          style="height: 40px"
          @click="sendMessage"
          :loading="isLoading"
      >
        {{ isLoading ? '生成中...' : '发送' }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';

const messages = ref([]);
const inputMessage = ref('');
const showError = ref(false);
const isLoading = ref(false);
const messagesContainer = ref(null);

// 自动滚动到底部
const scrollToBottom = async () => {
  await nextTick();
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};

const sendMessage = async () => {
  if (inputMessage.value.trim() === '') {
    showError.value = true;
    return;
  }

  try {
    isLoading.value = true;
    messages.value.push({ text: inputMessage.value, sender: 'user' });
    await scrollToBottom();

    const response = await axios.post('http://localhost:9090/chat',
        inputMessage.value,
        {
          headers: {
            'Content-Type': 'text/plain'
          },
          timeout: 30000 // 设置30秒超时
        }
    );

    // 直接获取后端处理后的字符串结果
    const result = response.data || '无返回内容';

    messages.value.push({
      text: result.replace(/\n/g, '<br>'), // 处理换行显示
      sender: 'llm'
    });

  } catch (error) {
    let errorMsg = '请求失败: ';
    if (error.response) {
      errorMsg += error.response.data.error || error.response.statusText;
    } else if (error.request) {
      errorMsg += '服务器无响应';
    } else {
      errorMsg += error.message;
    }
    ElMessage.error(errorMsg);
  } finally {
    isLoading.value = false;
    inputMessage.value = '';
    showError.value = false;
    await scrollToBottom();
  }
};
</script>

<style scoped>
.chat-messages {
  height: 80%;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 8px;
}

.message {
  margin-bottom: 15px;
  transition: all 0.3s ease;
}

.message-content {
  padding: 12px 16px;
  border-radius: 15px;
  max-width: 75%;
  word-break: break-word;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  white-space: pre-wrap; /* 保留换行符 */
}

.user .message-content {
  background-color: #409eff;
  color: white;
  margin-left: auto;
  border-bottom-right-radius: 5px;
  min-height: 40px;
  display: inline-block;
}

.llm .message-content {
  background-color: #ffffff;
  color: #333;
  margin-right: auto;
  border: 1px solid #eee;
  border-bottom-left-radius: 5px;
  min-height: 40px; /* 最小高度 */
  display: inline-block; /* 使宽度随内容变化 */
}

.chat-input {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
