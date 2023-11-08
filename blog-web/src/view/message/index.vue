<template>
  <div>
    <!--  banner  -->
    <div class="message-banner" :style="cover">
      <!-- 弹幕输入框 -->
      <div class="message-container">
        <h1 class="message-title">留言板</h1>
        <div class="animated fadeInUp message-input-wrapper">
          <el-input class="input" v-model="content" placeholder="说点什么吧" @focus="show = true"></el-input>
          <el-button style="opacity: .6; margin-left: 10px" class="ml-3 animated bounceInLeft" round @click="addToList"
                     v-show="show">发送</el-button>
        </div>
      </div>
      <!--   弹幕列表   -->
      <div class="barrage-container">
        <vue-baberrage :barrageList="barrageList">
          <template v-slot:default="slotProps">
              <span class="barrage-items">
                  <img :src="slotProps.item.avatar" width="30" height="30" style="border-radius:50%" />
                  <span class="ml-2">{{ slotProps.item.nickname }} :</span>
                  <span class="ml-2">{{ slotProps.item.content }}</span>
              </span>
          </template>
        </vue-baberrage>
      </div>
    </div>
  </div>
</template>

<script>
import { listMessage, addMessage } from "@/api";
export default {
  name: 'Message',
  mounted() {
    document.title = "留言板";
    this.listMessage();
  },
  data() {
    return {
      show: false,
      img: process.env.VUE_APP_IMG_API,
      content: "",
      count: null,
      timer: null,
      barrageList: [],
      user: this.$store.state.userInfo,
    };
  },
  methods: {
    addToList() {
      if (this.count) {
        this.$message.error('30秒后才能再次留言');
        return false;
      }
      if (this.content.trim() === "") {
        this.$message.error('留言不能为空');
        return false;
      }
      var message = {
        avatar: this.user ? this.user.avatar : this.$store.state.webSiteInfo.touristAvatar,
        status: 1,
        nickname: this.user ? this.user.nickname : "游客",
        content: this.content,
        time: Math.floor(Math.random() * (10 - 7)) + 7
      };

      this.content = "";
      addMessage(message).then(res => {
        this.barrageList.push(message);
        this.$message({
          message: '留言成功',
          type: 'success'
        });
      }).catch(err => {
        this.$message.error(err.message)
      });
      const TIME_COUNT = 30;
      if (!this.timer) {
        this.count = TIME_COUNT;
        this.timer = setInterval(() => {
          if (this.count > 0 && this.count <= 30) {
            this.count--;
          } else {
            clearInterval(this.timer);
            this.timer = null;
          }
        }, 1000);
      }
    },
    listMessage() {
      listMessage().then(res => {
        this.barrageList = res.data;
      });
    }
  },
  computed: {
    cover() {
      var cover = "https://img2.baidu.com/it/u=1361506290,4036378790&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500";
      return "background: url(" + cover + ") center center / cover no-repeat";
    }
  }
}
</script>

<style lang="scss" scoped>
/deep/ .el-input__inner {
  border-radius: 50px;
  opacity: .6;
}

.message-banner {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #49b1f5;
  animation: header-effect 1s;

  .message-container {
    position: absolute;
    width: 360px;
    left: 0;
    right: 0;
    text-align: center;
    z-index: 5;
    margin: 0 auto;
    color: #fff;


    .message-title {
      margin-top: 150px;
      color: #eee;
      animation: title-scale 1s;
    }

    .message-input-wrapper {
      display: flex;
      justify-content: center;
      height: 2.5rem;
      margin-top: 2rem;
    }
  }

  .barrage-container {
    position: absolute;
    top: 50px;
    left: 0;
    right: 0;
    bottom: 0;
    height: calc(100% -50px);
    width: 100%;

    .barrage-items {
      background: #000;
      border-radius: 100px;
      color: #fff;
      padding: 5px 10px 5px 5px;
      align-items: center;
      display: flex;
      margin-top: 10PX;
    }
  }
}
</style>
