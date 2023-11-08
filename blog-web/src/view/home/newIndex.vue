<template>
  <section class="main-container">
    <div class="main">
      <section class="main-box">
        <div class="bannerBox">
          <!--  轮播图  -->
          <el-carousel class="banner" :interval="5000" arrow="always">
            <el-carousel-item v-for="(item, index) in bannerList" :key="index">
              <a href="javascript:;" @click="handleClick(item.id)">
                <el-image class="bannerImg" :src="item.avatar"></el-image>
                <h3 class="title">{{ item.title }}</h3>
              </a>
            </el-carousel-item>
          </el-carousel>
        </div>
        <Sidebar></Sidebar>
      </section>
      <!--   热门分类   -->
      <section class="hot_category">
        <el-tabs v-model="activeName">
          <el-tab-pane class="categoryItem" v-for="item in categoryList" :key="item.id">
            <span @click="handleTabClick(item.id)" slot="label">
<!--              <i class="el-icon-folder-opened"></i>-->
              {{ item.name }}
            </span>
          </el-tab-pane>
        </el-tabs>
      </section>
      <!--  文章列表    -->
      <section class="content">
        <div class="articleBox" v-if="articleList.length > 0">
          <el-card class="articleItem" v-for="item in articleList" :key="item.id">
            <div class="articleInfo">
              <div class="articleInfo-item">
                <el-tooltip class="item" effect="dark" content="原创文章" placement="top-start">
                    <span v-if="item.isOriginal" class="original">
                        <i class="el-icon-tickets"></i>
                    </span>
                </el-tooltip>
                <span v-if="item.isStick" class="top">置顶</span>
                <h3 @click="handleClick(item.id)">{{ item.title }}</h3>
                <p>
                  {{ item.summary }}
                </p>
              </div>
              <el-image class="articleImg" :src="item.avatar" fit="scale - down"></el-image>
            </div>
            <div class="bottumItem">
              <div class="articleUser">
                <el-avatar class="userAvatar" :src="item.userAvatar"></el-avatar>
                <span>{{ item.username }}</span>
              </div>
              <div class="tag">
                <el-tag style="margin-right: 8px;" :type="tagStyle[Math.round(Math.random() * 4)]"
                        size="small" v-for="tag in item.tagList" :key="tag.id">{{ tag.name }}
                </el-tag>
              </div>
              <div class="articleOhter">
                <span class="item">
                    <i class="el-icon-view"></i>{{ item.quantity }}
                </span>
                <span class="item">
                    <i class="el-icon-chat-dot-round"></i>{{ item.commentCount }}
                </span>
                <span class="item">
                    <i style="font-size: 0.8rem;" class="iconfont icon-dianzan1"></i>{{ item.likeCount }}
                </span>
                <span class="item">
                    <i class="el-icon-time"></i>{{ item.createTime }}
                </span>
              </div>
            </div>
          </el-card>
          <!-- 分页按钮 -->
          <div class="pageDiv" v-if="pageData.pageNo < pageTotal">
            <a href="javascript:;" class="page" @click="onPage">
              加载更多
            </a>
          </div>
          <div style="text-align: center;color: var(--text-color);" v-else>
            我也是有底线的--
          </div>
        </div>
        <el-empty style=" width: 100%;" v-else description="很抱歉，暂无文章"></el-empty>
        <div class="rightBox">
          <!-- 关注我 -->
          <el-card class="box-card guanzhu">
            <div slot="header" class="clearfix">
              <span>关注我</span>
            </div>
            <ul class="guanzhuList">
              <li v-show="isShow(2)">
                <div class="item qq">
                  <svg-icon icon-class="qq"/>
                  <a :href="'//wpa.qq.com/msgrd?v=3&amp;uin=' + $store.state.webSiteInfo.qqNumber + '&amp;site=qq&amp;menu=yes'"
                     target="_blank">
                    {{ $store.state.webSiteInfo.qqNumber }}
                  </a>
                  <span title="点击复制" @click="copy($store.state.webSiteInfo.qqNumber)"
                        class="copyBtn name">
                                        QQ号
                                    </span>
                </div>
              </li>
              <li v-show="isShow(3)">
                <div class="item github">
                  <svg-icon icon-class="github"/>
                  <a :href="$store.state.webSiteInfo.github" target="_blank">
                    {{ $store.state.webSiteInfo.github }}
                  </a>
                  <span title="点击复制" @click="copy($store.state.webSiteInfo.github)" class="copyBtn name">
                                        github
                                    </span>
                </div>
              </li>
              <li v-show="isShow(4)">
                <div class="item gitee">
                  <svg-icon icon-class="gitee"/>
                  <a :href="$store.state.webSiteInfo.gitee" target="_blank">
                    {{ $store.state.webSiteInfo.gitee }}
                  </a>
                  <span title="点击复制" @click="copy($store.state.webSiteInfo.gitee)" class="copyBtn name">
                                        gitee
                                    </span>
                </div>
              </li>
              <li v-show="isShow(1)">
                <div class="item email">
                  <svg-icon icon-class="email"/>
                  <a :href="'mailto:' + $store.state.webSiteInfo.email" target="_blank" title="邮箱">
                    {{ $store.state.webSiteInfo.email }}
                  </a>
                  <span title="点击复制" @click="copy($store.state.webSiteInfo.email)" class="copyBtn name">
                                        邮箱
                                    </span>
                </div>
              </li>
              <li>
                <div class="item wechat">
                  <svg-icon icon-class="wechat"/>
                  {{ $store.state.webSiteInfo.qqNumber }}
                  <span title="点击复制" @click="copy($store.state.webSiteInfo.qqNumber)"
                        class="copyBtn name">
                                        微信
                                    </span>
                </div>
              </li>
            </ul>
          </el-card>
          <!-- 推荐文章 -->
          <el-card class="box-card recomArticle">
            <div slot="header" class="clearfix">
              <span>推荐文章</span>
            </div>
            <ul class="recomArticleUl">
              <li v-for="(item, index) in newArticleList">
                <div class="item">
                  <el-image style="float: left;" :src="item.avatar" fit="fit"/>
                  <p class="info">
                    <a href="">{{ item.title }}</a>
                  </p>
                  <p class="time">{{ item.createTime }}</p>
                </div>
              </li>
            </ul>
          </el-card>
          <!-- 标签墙 -->
          <el-card class="box-card tag_container">
            <div slot="header" class="clearfix">
              <span>标签墙</span>
              <a href="/tag" class="more">更多</a>
            </div>
            <div class="tag">
                <span @click="handleTagClike(item)" :style="{ backgroundColor: `${randomColor()}` }"
                      class="item" v-for="(item, index) in tagList" :key="index">
                    {{ item.name }}
                </span>
            </div>
          </el-card>
          <Countdown></Countdown>
        </div>
      </section>
    </div>
  </section>
</template>

<script>
import {featchCategory, featchHomeData, fetchArticleList} from '@/api/index'
import Sidebar from '@/components/sidebar/index'
import Countdown from '@/components/countdown/index.vue'

export default {
  name: 'Home',
  data() {
    return {
      loading: [],
      bannerList: [],
      articleList: [],
      pageTotal: 0,
      tagList: [],
      newArticleList: [],
      categoryList: [
        {
          id: null,
          name: "最新"
        }
      ],
      pageData: {
        pageNo: 1,
        pageSize: 9,
      },
      tagStyle: ['', 'success', 'info', 'warning', 'danger'],
      activeName: null
    }
  },
  methods: {
    handleTagClike(item) {
      this.$router.push({name: "/tags", query: {id: item.id, name: item.name}})
    },
    randomColor() {
      var letters = '0123456789ABCDEF';
      var color = '#';
      do {
        for (var i = 0; i < 6; i++) {
          color += letters[Math.floor(Math.random() * 16)];
        }
      } while (color === '#FFFFFF' || color === '#000000');
      return color;
    },
    fetchArticleList() {
      this.openLoading()
      fetchArticleList(this.pageData).then(res => {
        this.articleList.push(...res.data.records);
        this.pageTotal = res.data.pages
        this.loading.close()
      })
    },
    getHomeData() {
      featchHomeData().then(res => {
        //this.categoryList = res.extra.categories
        this.bannerList = res.extra.articles
        this.tagList = res.extra.tagCloud
        this.newArticleList = res.extra.newArticleList
      })
    },
    fetchCategoryList() {
      featchCategory().then(res => {
        this.categoryList.push(...res.data)
      })
    },
    // 打开加载层
    openLoading: function () {
      this.loading = this.$loading({
        lock: true,
        text: "正在加载中~",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },
    handleTabClick(id) {
      this.openLoading()
      this.pageData.pageNo = 1
      this.pageData.categoryId = id
      fetchArticleList(this.pageData).then(res => {
        this.articleList = res.data.records;
        this.pageTotal = res.data.pages
      })
      this.loading.close()
    },
    isShow(type) {
      return this.$store.state.webSiteInfo.showList.indexOf(type) != -1
    },
    handleLogin() {
      this.$store.commit("setLoginFlag", true);// 存储到vuex
    },
    copy(value) {
      const clipboard = new this.Clipboard('.copyBtn', {
        text: () => value
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
    // 分页
    onPage() {
      this.pageData.pageNo++;
      this.fetchArticleList()
    },
  },
  mounted() {
    this.fetchArticleList()
    this.getHomeData()
    this.fetchCategoryList()
  },
  components: {
    Sidebar,
    Countdown
  }
}
</script>

<style lang="scss" scoped>

/deep/ .el-tabs__item {
  color: var(--article-color);
}

/deep/ .is-active {
  color: #409EFF;
}

.banner {
  position: relative;
  height: 420px;

  /deep/ .el-carousel__container {
    height: 100%;
  }

  .title {
    position: absolute;
    height: 50px;
    line-height: 50px;
    bottom: 50px;
    text-align: center;
    width: 100%;
    color: #fff;
    background: rgba(0, 0, 0, .3);
    z-index: 1;
  }
}

.pageDiv {
  display: flex;
  justify-content: center;
}

.page {
  color: var(--site-text-color);
  background: var(--site-color);
  border-radius: 10px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  width: 20%;
  display: inline-block;
  cursor: pointer;
}

.main-container {
  display: flex;
  justify-content: center;

  .main {
    width: 63%;
    margin-top: 80px;

    .main-box {
      width: 100%;
      display: flex;

      .bannerBox {
        width: 70%;
        cursor: pointer;
      }

      .tuijian {
        width: 30%;
        display: inline-block;
        height: 420px;
        margin-left: 20px;

        /deep/ .el-image {
          width: 100%;
          height: 190px;
          cursor: pointer;

          &:last-child {
            margin-top: 20px;
          }
        }
      }
    }

    .hot_category {
      margin-top: 20px;

      /deep/ .el-tabs__item:hover span {
        color: #409EFF;
      }
    }

    .content {
      display: flex;

      .articleBox {
        font-size: 1rem;
        width: 100%;

        .articleItem {
          position: relative;
          padding: 10px 5px 10px 15px;
          background-color: var(--background-color);
          margin-bottom: 20px;
          transition: box-shadow .35s, transform .35s;

          &:hover {
            box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.2);
            transform: translateY(-2px)
          }

          .articleInfo {
            display: flex;
            color: var(--article-color);

            .original {
              content: '';
              position: absolute;
              right: 0;
              top: 0;
              width: 0;
              height: 0;
              border-color: transparent #40c9c6;
              /*上下颜色 左右颜色*/
              border-width: 0 40px 40px 0;
              border-style: solid;

              i {
                position: absolute;
                top: 4px;
                right: -37px;
                font-size: 16px;
                color: aquamarine;
              }
            }

            .articleInfo-item {
              width: 60%;
              height: 100px;

              .top {
                background-image: -webkit-linear-gradient(0deg, #3ca5f6 0, #a86af9 100%);
                padding-left: 5px;
                padding-right: 5px;
                //display: inline-block;
                border-top-right-radius: 5px;
                border-bottom-left-radius: 5px;
                font-size: 0.9rem;
                margin-right: 5px;
                color: #fff;
              }

              h3 {
                cursor: pointer;
                position: relative;
                display: inline-block;

                &:hover {
                  &::after {
                    transform: scaleX(1);
                  }

                  color: var(--theme-color);
                }
              }

              p {
                margin-top: 10px;
                overflow: hidden;
                text-overflow: ellipsis;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 3;
              }
            }

            .articleImg {
              width: 20%;
              height: 100px;
              position: absolute;
              right: 20px;
              top: 20px;
              border-radius: 3px;

              /deep/ .el-image__inner {
                transition: all 0.5s;
              }

              &:hover {
                /deep/ .el-image__inner {
                  transform: scale(1.1);

                }
              }
            }
          }

          .bottumItem {
            display: flex;
            // 垂直方向居中
            align-items: center;
            height: 50px;
            margin-top: 20px;

            .articleUser {
              line-height: 50px;
              font-size: 0.8rem;

              span {
                color: var(--theme-color);
                margin-left: 3px;
              }

              .userAvatar {
                vertical-align: top;
                border: 1px solid var(--border-line);
                transition: transform .5s;

                &:hover {
                  transform: rotate(360deg);
                }
              }
            }

            .tag {
              display: inline-block;
              margin-left: 20px;
            }

            .articleOhter {
              margin-left: 20px;
              font-size: 0.8rem;
              color: var(--text-color);

              .item {
                margin-left: 8px;

                i {
                  margin-left: 3px;
                }
              }

            }
          }
        }
      }

      .rightBox {
        width: 43%;
        margin-left: 20px;

        .box-card {
          font-size: 16px;
          padding: 12px;
          background-color: var(--background-color);
          color: var(--article-color);

          &:hover {
            .clearfix {
              &::before {
                content: '';
                width: 45px;
              }
            }
          }

          .clearfix {
            position: relative;

            &::before {
              content: '';
              width: 25px;
              border: 2px solid #333;
              position: absolute;
              bottom: -10px;
              transition: width .5s;
            }
          }
        }

        .guanzhu {
          margin-bottom: 20px;

          .guanzhuList {
            padding: 15px;
            list-style: none;

            .item {
              margin-bottom: 20px;
              height: 40px;
              line-height: 40px;
              font-size: 12px;
              display: flex;
              position: relative;

              a {
                text-decoration: none;
                color: var(--text-color);
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                width: 100%;
                flex: 1;

                &:hover {
                  color: var(--theme-color);
                }
              }

              .name {
                position: absolute;
                right: 0;
                width: 60px;
                text-align: center;
                cursor: pointer;
                color: #fff;
                transition: width .35s;
              }

              &:hover .name {
                width: 80px;
              }

              svg {
                width: 20px;
                height: 20px;
                margin-left: 5px;
                position: relative;
                bottom: -10px;
                margin-right: 10px;
              }
            }

            .github {
              border: 1px solid #606266;

              .name {
                background-color: #606266;
              }
            }

            .gitee {
              border: 1px solid red;

              .name {
                background-color: red;
              }
            }

            .email {
              border: 1px solid #F56C87;

              .name {
                background-color: #F56C87;
              }
            }

            .wechat {
              border: 1px solid #67C23A;

              .name {
                background-color: #67C23A;
              }
            }

            .qq {
              border: 1px solid #409EFF;

              .name {
                background-color: #409EFF;
              }
            }
          }
        }

        .tag_container {
          margin-top: 20px;
          font-size: 0.9rem;
          background-color: var(--background-color);
          color: var(--text-color);

          &:hover {
            transition: all .3s;
          }

          .clearfix {
            font-size: 16px;
            font-weight: 700;

            .more {
              float: right;
              margin-right: 10px;
              font-size: 13px;
              color: var(--text-color);
              text-decoration: none;
            }
          }

          .tag {
            padding-left: 10px;
            padding-bottom: 10px;
            height: auto;
            margin-top: 5px;

            span {
              text-decoration: none;
              margin-left: 15px;
              padding: 5px;
              display: inline-block;
              margin-bottom: 10px;
              border-radius: 8px;
              color: #fff;
              cursor: pointer;
              transition: all .3s;

              &:hover {
                border-radius: 0;
              }
            }
          }

        }
      }
    }
  }
}

</style>
