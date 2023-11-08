<template>
  <header class="header">
    <nav>
      <div class="logo" style="cursor: pointer;">
        <div class="logoBox">
          <router-link :to="'/'">
            <el-image class="img" style="width: 50px;border-radius: 50%; height: 45px;"
                      :src="require('@/assets/logo.png')">
            </el-image>
            <a href="javascript:;">白光博客</a>
          </router-link>

        </div>
      </div>

      <ul class="starlist">
        <li>
                    <span>
                        <router-link :class="path == '/' ? 'active' : ''" :to="'/'">
                            首页
                        </router-link>
                    </span>
        </li>
        <li>
          <el-dropdown trigger="hover">
                        <span class="el-dropdown-link" :class="path == '/archive' || path == '/categorys' || path == '/tag'
                            ? 'active' : ''">
                            文章归档<i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
            <el-dropdown-menu slot="dropdown">
              <router-link style="text-decoration: none;color: #71777c;" :to="'/archive'">
                <el-dropdown-item>归档</el-dropdown-item>
              </router-link>
              <router-link style="text-decoration: none;color: #71777c;" :to="'/categorys'">
                <el-dropdown-item>分类</el-dropdown-item>
              </router-link>

              <router-link style="text-decoration: none;color: #71777c;" :to="'/tag'">
                <el-dropdown-item>标签</el-dropdown-item>
              </router-link>
            </el-dropdown-menu>
          </el-dropdown>
        </li>

        <li>
          <el-dropdown trigger="hover">
                        <span class="el-dropdown-link">
                            发现<i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
            <el-dropdown-menu slot="dropdown">

              <a style="text-decoration: none;color: #71777c;" :href="adminUrl" target="_blank">
                <el-dropdown-item>后台管理</el-dropdown-item>
              </a>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
        <li>
                    <span>
                        <router-link :class="path == '/message' ? 'active' : ''" :to="'/message'">
                            留言板
                        </router-link>
                    </span>
        </li>
        <li>
                    <span>
                        <router-link :class="path == '/links' ? 'active' : ''" :to="'/links'">
                            友情链接
                        </router-link>
                    </span>
        </li>
        <li>
          <el-dropdown trigger="hover">
                        <span class="el-dropdown-link">
                            关于本站<i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
            <el-dropdown-menu slot="dropdown">

              <router-link style="text-decoration: none;color: #71777c;" :to="'/about'">
                <el-dropdown-item>关于本站</el-dropdown-item>
              </router-link>

              <a style="text-decoration: none;color: #71777c;" href="https://gitee.com/hu_chong"
                 target="_blank">
                <el-dropdown-item>网站源码</el-dropdown-item>
              </a>
            </el-dropdown-menu>
          </el-dropdown>
        </li>

      </ul>

      <div class="searchBox" v-show="!noneInput">
        <div class="search_bar search_open">
          <el-input :style="inputStyle" @focus="focus" @blur="blur" type="text" v-model="keywords"
                    placeholder="想搜点什么呢..." @keydown.enter.native="search"/>
          <p class="search_ico" @click="search()">
            <i class="iconfont icon-search"></i>
          </p>
        </div>
        <!-- 热搜框 -->
        <div class="hot_search_main" :style="style">
          <a @click="handleArticle(item.id)" href="javascript:;" v-for="(item, index) in $store.state.hotArticles"
             :key="index">
            <span :style="{ backgroundColor: `${colors[index]}` }">{{ index + 1 }}</span>
            {{ item.title }}
          </a>
        </div>
      </div>
      <div class="articleBtn">
        <el-button size="small" @click="addArticle" type="primary">发表文章</el-button>
      </div>

      <div class="userInfo">
        <el-dropdown trigger="hover">
          <div class="el-dropdown-link">
            <img v-if="!userInfo" :src="require('@/assets/touristAvatar.png')" alt="">
            <img v-else :src="userInfo.avatar" alt=""/>
          </div>
          <el-dropdown-menu slot="dropdown" v-if="userInfo">
            <el-dropdown-item>
              <span @click="openUserInfoDrawer">个人中心</span>
            </el-dropdown-item>
            <el-dropdown-item><a href="javascript:;" @click="logout"
                                 style="text-decoration: none;color: #71777c;">退出登录</a>
            </el-dropdown-item>
          </el-dropdown-menu>

          <el-dropdown-menu slot="dropdown" v-else>
            <div class="loginTip" style="padding: 10px;font-size: 0.9rem;">
              <p>登录网站，获取以下权益</p>
              <div style="margin-top: 10px;">

                                <span>
                                    <i class="el-icon-cloudy"></i> 参与互动评论
                                </span>

                <span style="margin-left: 10px;">
                                    <i class="el-icon-cloudy"></i> 发表优质文章
                                </span>
              </div>
              <el-button @click="handleLogin()" style="margin:  0 auto;display: block;margin-top: 10px;"
                         type="success" size="small">立即登录
              </el-button>
            </div>
          </el-dropdown-menu>
        </el-dropdown>
      </div>

    </nav>

  </header>
</template>
<script>
import {logout} from '@/api'
import {removeToken} from '@/utils/auth'

export default {
  name: 'Header',
  props: {
    userInfo: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      keywords: null,
      user: this.$store.state.userInfo,
      style: null,
      inputStyle: "",
      path: null,
      isMobile: false,
      noneInput: false,
      windowWidth: 0,
      headerClass: "header",
      adminUrl: process.env.VUE_APP_ADMIN_API,
      colors: ["#FE2D46", "#FF6600", "#FAA90E", "#7f7f8c", "#7f7f8c"],
    };
  },

  mounted() {
    this.path = this.$route.path
    // <!--把window.onresize事件挂在到mounted函数上-->
    window.onresize = () => {
      return (() => {
        this.windowWidth = window.innerWidth // 宽
      })()
    }
    window.addEventListener('scroll', this.scrollFn, false)
  },

  watch: {
    // 监听页面宽度
    windowWidth(val) {
      this.isMobile = val < 1119
      this.noneInput = val < 1410
    }

  },

  methods: {
    addArticle() {
      this.user = this.$store.state.userInfo
      if (!this.user) {
        this.handleLogin()
        return;
      }
      this.$store.state.articleDrawer.flag = true;
      this.$store.state.articleDrawer.id = null;
    },
    openUserInfoDrawer() {
      this.$store.state.userInfoDrawer = true
    },
    handleArticle(id) {
      this.$router.push({path: '/articleInfo', query: {articleId: id}})
    },
    focus() {
      this.style = "transform:translate3d(0, 0, 0);opacity:1;visibility:visible;border: 1px solid #dcdfe6"
    },
    blur() {
      this.style = "opacity:0;visibility:hidden"
    },
    search() {
      if (this.keywords == null || this.keywords == "") {
        this.$message.error('请输入搜索内容');
        return;
      }
      this.$router.push({path: '/search', query: {keyword: this.keywords}})
    },
    scrollFn() {
      // if (this.$route.paht != '/message') {
      //     // 页面滚动距顶部距离
      //     let scrollTop =
      //         window.pageYOffset ||
      //         document.documentElement.scrollTop ||
      //         document.body.scrollTop;
      //     let scroll = scrollTop - this.i;
      //     this.i = scrollTop;

      //     // 鼠标向上滚动
      //     if (scroll < 0) {
      //         this.headerClass = "header slideDown"
      //     } else {
      //         this.headerClass = "header slideUp"
      //     }
      // }
    },
    handleLogin() {
      this.$store.commit("setLoginFlag", true);// 存储到vuex
    },
    logout() {
      //如果在个人中心则跳回上一页
      if (this.path === "/user") {
        this.$router.go(-1);
      }
      logout().then(res => {
        removeToken()
        this.$store.commit("setUserInfo", null)
        location.reload()
        this.$message.success('注销成功');
      }).catch(err => {
        console.log(err)
      });
    },
    openDrawer() {
      this.$store.commit("setDrawer", true);
    },
    openSearchDrawer() {
      this.$store.commit("setSearchDrawer", true);
    }
  }
}
</script>
<style scoped lang="scss">
.slideDown {
  transition: transform .5s ease-out;
  transform: translateY(0);
}

.slideUp {
  transition: transform .5s ease-out;
  transform: translateY(-100px);
}

.header {
  position: fixed;
  top: 0;
  width: 100%;
  line-height: 60px;
  z-index: 99;
  background-color: var(--header-back-color);
  backdrop-filter: blur(4px);

  a {
    text-decoration: none;
  }

  nav {
    width: 100%;
    margin: auto;
    overflow: hidden;
    max-width: 1500px;

    .logo {
      float: left;
      font-size: 22px;
      font-weight: 700;

      .img {
        width: 80px;
        height: 40px;
        vertical-align: middle;
        margin-top: -6px;
      }

      a {
        margin-left: 10px;
        color: var(--theme-color);
      }
    }

    .starlist {
      display: block;
      float: left;
      margin-left: 50px;
      overflow: hidden;
      max-width: 1500px;

      li {
        float: left;
        display: block;
        font-size: 14px;
        padding: 0 15px;

        .active {
          color: var(--theme-color);
        }

        a {
          color: #71777c;

          &:hover {
            color: var(--theme-color);
          }
        }

      }
    }

    .searchBox {
      position: absolute;
      right: 340px;
      top: 0;
      display: flex;
      -ms-flex-direction: column;
      flex-direction: column;

      .search_open {
        width: 200px;
        display: inline-block;
        padding-left: 10px;
        height: 60px;

        /deep/ .el-input__inner {
          border: 1px solid #dcdfe6;
          background: #f5f6f7;
          width: 210px;
          padding-left: 8px;
          height: 35px;
          border-radius: 20px;
          transition: all 2s;
        }


      }

      .search_ico {
        position: absolute;
        right: 5px;
        top: 0;
        padding: 0;
        margin: 0;
        line-height: 60px;
        cursor: pointer;
        text-align: right;

        .iconfont {
          font-size: 20px;
          font-weight: 700;
        }
      }

      .hot_search_main {
        background-color: var(--background-color);
        border-radius: 4px;
        box-shadow: 0 0 10px rgba(0, 0, 0, .5);
        position: absolute;
        z-index: 99;
        top: 50px;
        left: 10px;
        right: -10px;
        opacity: 0;
        visibility: hidden;
        transition: visibility .35s, opacity .35s, transform .35s;
        transform: translate3d(0, 15px, 0);

        a {
          height: 40px;
          text-decoration: none;
          display: block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          /* border-bottom: 1px solid #f2f6fc; */
          line-height: 40px;
          padding: 4px 5px;
          color: #606266;
          font-size: 12px;

          span {
            width: 20px;
            height: 20px;
            display: inline-block;
            line-height: 21px;
            text-align: center;
            border-radius: 3px;
            color: var(--background-color);

          }

          &:first-child {
            border-top-left-radius: 4px;
            border-top-right-radius: 4px;
          }

          &:last-child {
            border-bottom-left-radius: 4px;
            border-bottom-right-radius: 4px;
          }

          &:hover {
            background-color: rgb(235, 238, 245);
          }
        }
      }
    }

    .articleBtn {
      position: absolute;
      right: 220px;
      top: 0;

    }


    .userInfo {
      position: absolute;
      right: 150px;
      top: 0;

      /deep/ .el-dropdown {
        width: 35px;
        height: 35px;
        right: 0;
        top: 12px;
      }

      img {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        border: 1px solid var(--border-line);
        position: absolute;
      }

    }
  }
}
</style>
