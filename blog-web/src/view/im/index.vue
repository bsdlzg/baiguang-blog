<template>
  <div class="im-main">
    <div class="im-warpper">
      <el-card class="item">
        <!-- 标题 -->
        <div class="title">{{ title }}</div>
        <div class="messageBox" id="messageBox" ref="messageContainer">
          <!-- 加载更多 -->
          <div class="more" v-show="pageData.pageNo < totalPage" @click="handleMore">加载更多</div>
          <!-- 消息内容框 -->
          <div class="messageItem" v-for="(item, index) in  messages " :key="index">
            <!-- 左边消息框 别人发送的消息 -->
            <div :class="item.isWithdraw ? 'withdraw' : 'left'" v-if="user && item.fromUserId != user.id">
              <img :src="item.fromUserAvatar" @error="item.fromUserAvatar = errImg"
                   :title="item.fromUserNickname">
              <div class="info">
                                <span class="nickname">
                                    {{ item.fromUserNickname }}
                                    <span class="time" style="">{{ item.createTime }}</span>
                                </span>
                <span v-if="!item.isWithdraw" v-html="item.content" class="messageContent"
                      @contextmenu.prevent="openMenu($event, item, index)">
                                </span>
                <span v-else>
                                    " {{ item.fromUserNickname }} " 撤回了一条消息
                                </span>
              </div>
            </div>
            <!-- 右边消息框 自己发送的消息 -->
            <div :class="item.isWithdraw ? 'withdraw' : 'right'" v-else>
              <img :src="item.fromUserAvatar" @error="item.fromUserAvatar = errImg"
                   :title="item.fromUserNickname">
              <div class="info">
                <div class="nickname">
                  <span style="margin-left: 3px;font-size: 12px;">{{ item.createTime }}</span>
                  {{ item.fromUserNickname }}
                </div>
                <span v-if="!item.isWithdraw" v-html="item.content" class="nowMessageContent"
                      @contextmenu.prevent="openMenu($event, item, index)">
                                </span>
                <span v-else>
                                    " {{ item.fromUserNickname }} " 撤回了一条消息
                                </span>

              </div>
            </div>
          </div>
        </div>
        <!-- 输入框 -->
        <div class="input-box">
          <!-- 输入选择 如表情、图片等 -->
          <div class="selelctBox">
                        <span class="emoji" @click.stop="handleOpen">
                            <i class="iconfont icon-biaoqing"></i>
                            <i class="iconfont icon-tupian"></i>
                        </span>
          </div>
          <!-- 表情框 -->
          <div class="emoji-wrapper" v-show="emojiShow">
                        <span :title="item.description" class="emojiItem" v-for="( item, index ) of  emojiList "
                              :key="index" @click.stop="addEmoji(item, $event)">
                            {{ item.emoji }}
                        </span>
          </div>
          <!-- 输入内容 -->
          <textarea class="contentBox" placeholder="说点什么呢" v-model="text"></textarea>
          <el-button class="btn" @click="send">发送[Ctrl+Enter]</el-button>
        </div>

        <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
          <li @click="clipboard" class="copyBtn">
            <i class="iconfont icon-fuzhi1"></i> 复制
          </li>
          <li @click="translate">
            <i class="iconfont icon-fanyi"></i>翻译
          </li>
          <li @click="withdraw">
            <i class="iconfont icon-chehui"></i>撤回
          </li>
        </ul>
      </el-card>
      <!-- 在线用户 -->
      <div class="online">
        <ul class="online-item">
          <li ref="groud" class="onlineLi active" @click="selectUserIm()">
            <img :src="$store.state.webSiteInfo.logo" alt="">
            <span>白光博客交流群</span>
          </li>
        </ul>
        <div class="onlineCount">当前在线人数：{{ onlineUserList.length }}</div>

        <ul class="online-item">
          <li ref="olineItem" class="onlineLi" v-for="(item, index) in onlineUserList" :key="index"
              @click="selectUserIm(item, index)">
            <img :src="item.avatar" alt="">
            <span>{{ item.nickname }}</span>
          </li>
        </ul>
      </div>
    </div>


  </div>
</template>

<script>
let socket;
import {getImHistory, getUserImHistoryList} from '@/api'

export default {
  name: 'Im',
  data() {
    return {
      websoketUrl: process.env.VUE_APP_WEBSOCKET_API,
      visible: false,
      top: 0,
      left: 0,
      text: "",
      messages: [],
      emojiList: [],
      emojiShow: false,
      errImg: "http://img.shiyit.com/1645512111007.png",
      user: this.$store.state.userInfo,
      totalPage: 0,
      isBackTop: false,
      message: null,
      selectIndex: null,
      title: "白光博客交流群",
      lastIndex: null,
      pageData: {
        pageNo: 1,
        pageSize: 10
      },
      onlineUserList: [],
      selectUserOnline: null,
    }
  },

  mounted() {
    const bc = new BroadcastChannel('my-channel');
    bc.onmessage = (event) => {
      const {userValue} = event.data;
      if (userValue !== this.user) { // 如果cookie值有变化，则更新数据
        this.user = JSON.parse(userValue);
      }
    }
    document.addEventListener("click", this.handleClose)
    window.addEventListener('keydown', this.handkeyEnter, true)//开启监听键盘按下事件
  },
  watch: {
    //   监听属性对象，newValue为新的值，也就是改变后的值
    visible(newValue, oldValue) {
      if (newValue) {
        document.body.addEventListener("click", this.closeMenu);
      } else {

        document.body.removeEventListener("click", this.closeMenu);
      }
    },
    user(newName) {
      if (typeof (newName) == 'string') {
        this.user = JSON.parse(newName)
      } else {
        this.user = newName
        //发送soket连接
        this.init()
      }
    },
    messages() {
      if (this.isBackTop) {
        this.$refs.messageContainer.scrollTop = 0
        this.isBackTop = false
      } else {
        this.$nextTick(() => {
          this.$refs.messageContainer.scrollTop = this.$refs.messageContainer.scrollHeight;
        })
      }

    }
  },
  created() {
    this.init()
    document.oncontextmenu = new Function("event.returnValue=false");
    this.emojiList = require('@/assets/emoji.json');
  },
  methods: {
    //选择用户单聊
    selectUserIm(item, index) {
      if (this.lastIndex != null) {
        this.$refs.olineItem[this.lastIndex].className = "onlineLi"
      }
      if (index != null) {
        this.$refs.olineItem[index].className += " active"
        this.$refs.groud.className = "onlineLi"
        this.lastIndex = index
      } else {
        this.$refs.groud.className += " active"
      }

      this.pageData.pageNo = 1

      //为空则是群聊
      if (!item) {
        this.title = "白光博客交流群"
        this.messages = []
        this.selectUserOnline = null;
        this.getHistoryList()
        return;
      }
      if (item.id == this.user.id) {
        this.$message.error('不能对自己发送消息');
        return;
      }
      this.title = item.nickname
      this.pageData.fromUserId = this.user.id
      this.pageData.toUserId = item.id
      this.messages = []
      this.selectUserOnline = item
      getUserImHistoryList(this.pageData).then(res => {
        let arr = res.data.records
        for (let i = arr.length - 1; i >= 0; i--) {
          this.messages.push(arr[i])
        }
        this.totalPage = res.data.pages
      })
    },
    //右击
    openMenu(e, item, index) {
      var x = e.pageX; //这个应该是相对于整个浏览器页面的x坐标，左上角为坐标原点（0,0）
      var y = e.pageY; //这个应该是相对于整个浏览器页面的y坐标，左上角为坐标原点（0,0）
      this.top = y;
      this.left = x;
      this.visible = true; //显示菜单
      this.message = item
      this.selectIndex = index
    },
    //撤回
    withdraw() {
      if (this.user.id != this.message.fromUserId) {
        this.$message.error('只能撤回自己发送的消息哦');
        return;
      }

      if (this.message.isWithdraw) {
        this.$message.error('消息已被撤回');
        return;
      }

      this.message.isWithdraw = 1
      this.message.content = "消息已撤回"
      this.message.index = this.selectIndex

      let message = {code: this.selectUserOnline == null ? 2 : 1, messageData: this.message}
      socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发
    },
    //翻译
    translate() {
      window.open("https://fanyi.baidu.com/?aldtype=16047#zh/en/" + this.message.content, '_blank')
    },
    //复制
    clipboard() {
      const clipboard = new this.Clipboard('.copyBtn', {
        text: () => this.message.content
      })
      clipboard.on('success', () => {
        this.$message.success("复制成功");
        clipboard.destroy()
      })
      clipboard.on('error', () => {
        this.$message.error('复制失败');
        clipboard.destroy()
      })
    },
    //关闭菜单
    closeMenu() {
      this.visible = false; //关闭菜单
    },
    //加载更多消息
    handleMore() {
      this.pageData.pageNo++;
      this.isBackTop = true
      getImHistory(this.pageData).then(res => {
        let arr = res.data.records
        for (let i = 0; i < arr.length; i++) {
          this.messages.unshift(arr[i])
        }
      })
    },
    //获取群聊历史记录
    getHistoryList() {
      getImHistory(this.pageData).then(res => {
        let arr = res.data.records
        for (let i = arr.length - 1; i >= 0; i--) {
          this.messages.push(arr[i])
        }
        this.totalPage = res.data.pages
      })
    },
    //Ctrl+Enter事件
    handkeyEnter(event) {
      if (event.ctrlKey && event.keyCode == 13) {
        this.send()
      }
    },
    //打开表情框
    handleOpen() {
      this.emojiShow = !this.emojiShow
    },
    //关闭表情框
    handleClose() {
      this.emojiShow = false
    },
    //添加表情
    addEmoji(obj, e) {
      this.text += obj.emoji;
    },
    //发送消息
    send() {
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        if (!this.user) {
          this.$message.error('请先登录');
          this.$store.state.loginFlag = true
          return;
        }
        if (!this.text) {
          this.$message.error('请输入内容');
          return;
        }
        let message = {code: 2,
          messageData: {
            fromUserId: this.user.id,
            content: this.text,
            fromUserAvatar: this.user.avatar,
            fromUserNickname: this.user.nickname
          }
        }

        if (this.selectUserOnline != null) {
          message = {
            code: 1,
            messageData: {
              fromUserId: this.user.id,
              fromUserAvatar: this.user.avatar,
              fromUserNickname: this.user.nickname,
              toUserId: this.selectUserOnline.id,
              toUserAvatar: this.selectUserOnline.avatar,
              toUserNickname: this.selectUserOnline.nickname,
              content: this.text,
            }
          }
        }
        socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发
        // this.messages.push(message)
        // 构建消息内容，本人消息
        this.text = "";
      }
    },
    //初始化socket
    init() {
      let _this = this;
      if (!_this.user) {
        this.$message.error('登录才能进行群聊');
        return;
      }
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        let socketUrl = _this.websoketUrl + "?userId=" + _this.user.id;
        if (socket != null) {
          socket.close();
          socket = null;
        }
        // 开启一个websocket服务
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {

          console.log("websocket已打开");
          //连接成功后获取历史聊天记录
          _this.getHistoryList()

        };
        //  浏览器端收消息，获得从服务端发送过来的文本消息
        socket.onmessage = function (msg) {
          console.log("收到数据====" + msg.data)
          let data = JSON.parse(msg.data)
          //在线用户
          if (data.type == 3) {
            _this.onlineUserList = data.ext.userOnlineList
            return;
          }
          //群聊
          if (data.code == 2) {
            if (_this.selectUserOnline) {
              return;
            }
            // 这是撤回的逻辑
            if (data.messageData.index != null) {
              _this.messages[data.messageData.index].content = data.messageData.content
              _this.messages[data.messageData.index].isWithdraw = 1
              return;
            }
            _this.messages.push(data.messageData)
            return;
          }
          //单聊
          if (data.code == 1) {
            if (!_this.selectUserOnline) {
              return;
            }
            if (_this.selectUserOnline.id == data.messageData.fromUserId || _this.selectUserOnline.id == data.messageData.toUserId) {
              //这是撤回的逻辑
              if (data.messageData.index != null) {
                _this.messages[data.messageData.index].content = data.messageData.content
                _this.messages[data.messageData.index].isWithdraw = 1
                return;
              }
              _this.messages.push(data.messageData)
              return;
            }
            return;
          }
        };
        //关闭事件
        socket.onclose = function () {
          console.log("websocket已关闭");

        };
        //发生了错误事件
        socket.onerror = function () {
          console.log("websocket发生了错误");

        }
      }
    },
  }
}
</script>


<style lang="scss" scoped>
.im-main {
  min-height: calc(100vh - 167px);
  display: flex;
  justify-content: center;
  position: relative;

  @media screen and (max-width: 1118px) {
    .im-warpper {
      width: 100%;
    }
  }

  @media screen and (min-width: 1119px) {
    .im-warpper {
      width: 60%;
    }
  }

  .im-warpper {
    color: var(--im-text-color);
    border-radius: 12px;
    display: flex;
    border: 2px solid #cdcdcd;
    height: 100%;
    margin-top: 80px;


    .online {
      width: 220px;
      background-color: var(--im-backgroudColor);
      border-left: 1px solid #cdcdcd;
      background-color: #2e3238;
      padding: 10px;
      color: #fff;

      .onlineCount {
        padding: 10px;
      }

      .online-item {
        list-style: none;

        .onlineLi {
          cursor: pointer;
          padding-left: 5px;
          height: 30px;
          line-height: 30px;
          border-radius: 5px;
          margin-top: 5px;

          &:hover {
            background-color: #ccc;
          }
        }

        .active {
          background-color: #ccc;

        }

        img {
          width: 25px;
          height: 25px;
          border-radius: 50%;
          vertical-align: middle;
        }

        span {
          line-height: 30px;
          margin-left: 10px;
        }
      }
    }

    .item {
      background-color: var(--im-backgroudColor);
      width: 100%;

      box-shadow: none;


      .title {
        text-align: center;
        padding: 10px 0;
      }

      .messageBox,
      .emoji-wrapper {

        &::-webkit-scrollbar {
          width: 5px;
        }

        &::-webkit-scrollbar-thumb {
          background: linear-gradient(180deg, #F0BBC3, #10A44A);
          border-radius: 5px;
        }
      }

      .messageBox {
        height: 500px;
        overflow: auto;


        .more {
          text-align: center;
          margin-top: 10px;
          cursor: pointer;
        }

        .messageItem {
          margin-top: 20px;
          margin-bottom: 20px;

          .withdraw {
            text-align: center;

            img,
            .nickname {
              display: none
            }
          }

          .left {
            padding: 5px 10px;
            display: flex;

            .info {
              margin-left: 5px;

              .nickname {
                font-size: 0.8rem;
                display: block;
                margin-bottom: 3px;

                .time {
                  margin-left: 3px;
                  font-size: 12px;
                }
              }
            }
          }

          img {
            width: 45px;
            height: 45px;
            border-radius: 50%;
          }

          .messageContent,
          .nowMessageContent {
            display: inline-block;
            max-width: 400px;
            color: #555;
            padding: 5px;
            border-radius: 3px;
          }

          .messageContent {
            background-color: var(--im-box-backgroudColor);
            border-radius: 2px 18px 18px;
            padding: 8px;
          }


          .right {
            margin: 2px;
            padding: 5px 10px;
            position: relative;
            display: flex;
            flex-direction: row-reverse;

            .nowMessageContent {
              background-color: var(--im-user-box-backgroudColor);
              border-radius: 18px 2px 18px 18px;
              padding: 8px;
            }

            img {
              float: right;
              margin-left: 10px;
            }

            .info {
              float: right;
              display: flex;
              flex-direction: row-reverse;
              flex-wrap: wrap;

              .nickname {
                display: inline-block;
                text-align: right;
                font-size: 0.8rem;
                margin-bottom: 3px;
                width: 100%;

              }
            }
          }
        }
      }


      .input-box {
        border-top: 1px solid #fff;
        margin-top: 20px;
        position: relative;

        .selelctBox {
          margin-top: 10px;

          i {
            cursor: pointer;
            font-size: 20px;
            margin-left: 20px;
            color: var(--text-color);
          }

        }


        .emoji-wrapper {
          background-color: var(--background-color);
          width: 400px;
          height: 200px;
          overflow-y: auto;
          border-radius: 5px;
          position: absolute;
          top: -220px;

          .emojiItem {
            cursor: pointer;
            margin-bottom: 5px;
            font-size: 1.5rem;
            text-align: center;
            display: inline-block;

            &:hover {
              background-color: #eeeeee;
            }
          }
        }

        .contentBox {
          height: 160px;
          width: 100%;
          padding: 20px;
          border: none;
          outline: none;
          resize: none;
          background-color: var(--im-backgroudColor);
        }

        .btn {
          position: absolute;
          right: 10px;
          bottom: 10px;
          width: auto;
          height: 40px;
          border-radius: 10px;
          text-align: center;
          line-height: 40px;
          padding: 0 5px;
          background-image: linear-gradient(to left, #80CBC4, #4DB6AC, #26A69A);
        }
      }

      .contextmenu {
        margin: 0;
        background: #fff;
        z-index: 3000;
        position: fixed; //关键样式设置固定定位
        list-style-type: none;
        padding: 5px 0;
        font-size: 12px;
        font-weight: 400;
        color: #333;
        box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
        width: 100px;
        border-bottom-left-radius: 0 !important;
        border-bottom-right-radius: 0 !important;

        li {
          margin: 0;
          padding: 7px 16px;
          cursor: pointer;

          &:hover {
            background: #eee;
          }

          i {
            margin-right: 3px;
          }
        }


      }
    }
  }


}
</style>
