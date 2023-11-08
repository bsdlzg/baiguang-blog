<template>
  <div>
    <!-- 头部 -->
    <Header :userInfo="userInfo"></Header>
    <!-- 内容 -->
    <transition name="moveCartoon" appear>
      <div v-if="validate" style="min-height: calc(100vh - 167px);">
        <router-view :key="$route.fullPath" />
      </div>
      <div v-else style="min-height:100vh">
        <router-view :key="$route.fullPath" />
      </div>
    </transition>
    <!--  底部  -->
    <Footer v-show="$route.path != '/message'"></Footer>

    <!-- 登录模态框 -->
    <Login></Login>
    <!-- 发表文章 -->
    <ArticleModel></ArticleModel>
    <!-- 个人中心 -->
    <User></User>
    <!-- 侧边栏 -->
    <Sidebar></Sidebar>
  </div>
</template>

<script>

import Header from '@/components/layout/Header'
import Footer from '@/components/layout/Footer'
import Sidebar from '@/components/layout/Sidebar.vue'
import Login from '@/components/model/Login.vue'
import ArticleModel from '@/view/article/ArticleModel'
import User from '@/view/user/User.vue'

import {getWebSiteInfo, selectUserInfoByToken} from '@/api/index'
import { setToken, getToken } from '@/utils/auth'

export default {
  data() {
    return {
      userInfo: null,
    }
  },
  components:{
    Header,
    Footer,
    Login,
    ArticleModel,
    User,
    Sidebar
  },
  methods:{
    validate() {
      return this.$route.path == '/search' || this.$route.path == '/category';
    }
  },
  created() {
    let flag = window.location.href.indexOf("token") != -1
    if (flag) {
      let token = this.$route.query.token
      setToken(token)
    }

    // 从cookie中获取token
    let token = getToken()
    if (token != null) {
      selectUserInfoByToken(token).then(res => {
        this.userInfo = res.data
        this.$store.commit("setUserInfo", res.data)
      })
    }

    getWebSiteInfo().then(res => {
      let siteCount = {
        articleCount: res.extra.articleCount,
        tagCount: res.extra.tagCount,
        categoryCount: res.extra.categoryCount,
      }
      this.$store.commit("setWebSiteInfo", res.extra.webConfig)
      this.$store.commit("setSiteCount", siteCount)
      this.$store.commit("setHotArticles", res.extra.hotArticles)

      this.$store.state.siteAccess = res.extra.siteAccess
      this.$store.state.visitorAccess = res.extra.visitorAccess
    })

  }
}
</script>

<style scoped>
/* 类名要对应回 name 的属性值 */
.moveCartoon-enter-active {
  animation: move .8s;
}

.moveCartoon-leave-active {
  animation: move .8s reverse;
}

@keyframes move {
  from {
    transform: translateX(-100%);
  }

  to {
    transform: translate(0);
  }
}
</style>
