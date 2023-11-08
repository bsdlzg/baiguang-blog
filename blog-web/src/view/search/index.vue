<template>
  <div class="artcile_main">
    <el-card class="item">

      <div class="title">
        <span>
        <i style="color: var(--theme-color);margin-right: 5px;" class="iconfont icon-search"></i>
        搜索到
        <em style="color: var(--theme-color);font-weight: 700;">{{ pageTotal }}</em>
        条与
        <span style="color: var(--theme-color);font-weight: 700;">{{ pageData.keyword }}</span>
        相关的文章：
        </span>
      </div>

      <div v-if="articleList.length">
        <ul class="ul_item" v-for="item in  articleList" :id="item.id">
          <li class="main_li" @click="handleClick(item.id)" data-wow-duration="2s" data-wow-iteration="1">
            <div href="" class="thumbnail">
              <img class="article_cover" :src="item.avatar" :alt="item.title" />
              <i class="iconfont icon-tupian"></i>
              <span class="time">{{ formatDate(item.createTime) }}</span>
            </div>
            <div class="information">
              <div class="titleName">
                <span v-if="item.isStick == 1" class="top">置顶</span>
                <span v-html="item.title"></span>
              </div>
              <div class="info">
                {{ item.summary }}
              </div>
              <div class="meta">
                <ul class="items">
                  <li>{{ item.username }}</li>
                  <li>{{ formatDate(item.createTime) }}</li>
                  <li><i class="iconfont icon-yuedu"></i>{{ item.quantity }}</li>
                  <li><i class="el-icon-chat-dot-round"></i>{{ item.commentCount }}</li>
                  <li><i class="iconfont icon-dianzan1"></i>{{ item.likeCount }}</li>
                </ul>
                <ul class="items">
                  <li v-for=" tag  in  item.tagList ">
                    <i class="iconfont icon-biaoqian"></i>
                    <span class="tag_name" @click="">{{ tag.name }}</span>
                  </li>
                </ul>
              </div>
            </div>
          </li>
        </ul>
        <!-- 分页按钮 -->
        <div class="page" v-show="pageData.pageNo < pageTotal" @click="onPage">
          加载更多
        </div>
      </div>
      <div class="empty-box" v-else>
        <el-empty description="哎呀，文章丢失啦..."></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
import { searchArticle } from '@/api'
export default {
  data() {
    return {
      pageData: {
        pageNo: 1,
        pageSize: 6,
        keyword: this.$route.query.keyword,
      },
      pageTotal: 0,
      articleList: []
    }
  },

  created() {
    this.fetchArticleList()
  },
  methods: {
    // 分页
    onPage() {
      this.pageData.pageNo++;
      this.fetchArticleList()
    },
    handleClick(id) {
      this.$router.push({ path: '/articleInfo', query: { articleId: id } })
    },
    formatDate: function (value, args) {
      var dt = new Date(value);
      let year = dt.getFullYear();
      let month = (dt.getMonth() + 1).toString().padStart(2, '0');
      let date = dt.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${date}`;
    },
    fetchArticleList() {
      searchArticle(this.pageData).then(res => {
        this.articleList.push(...res.data.records);
        this.pageTotal = res.data.pages
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  text-align: center;
  background-color: rgba(0, 0, 0, .8);
  width: 120px;
  height: 30px;
  line-height: 30px;
  border-radius: 50px;
  margin: 0 auto;
  margin-top: 20px;
  cursor: pointer;
  color: #fff;
}

.artcile_main {
  display: flex;
  justify-content: center;
  position: relative;

  .item {
    margin-top: 80px;
    border-radius: 12px;
    background-color: var(--background-color);
    width: 80%;
    height: 100%;
    padding: 10px;

    &:hover {
      box-shadow: 5px 4px 8px 6px rgba(7, 17, 27, .06);
      transition: all .3s;
    }

    .title {
      height: 45px;
      border-bottom: 1px solid var(--border-line);
      margin-left: 15px;
      margin-bottom: 20px;

      span {
        display: inline-block;
        font-size: 16px;
        line-height: 45px;
        font-weight: 700;
        color: var(--text-color);
      }

    }

    .ul_item {
      padding-left: 20px;
      cursor: pointer;

      &:hover {
        .time {
          transform: translateX(-110%) translateY(5%);
          transition: all 0.5s;
        }

        .main_li .information .titleName,
        .main_li .information .info {
          color: var(--theme-color);
        }

        .main_li::before {
          transform: scaleX(1);
        }

      }

      &:last-child .main_li {
        border-bottom: 0
      }

      .main_li {
        list-style: none;
        border-bottom: 1px solid var(--border-line);
        display: flex;
        position: relative;
        padding-bottom: 15px;
        margin-bottom: 15px;

        &::before {
          content: "";
          width: 4px;
          height: 30px;
          background-color: var(--theme-color);
          position: absolute;
          left: -15px;
          border-radius: 50px;
          transform: scaleX(0);
          transition: all 0.5s;
        }



        .thumbnail {
          flex-shrink: 0;
          position: relative;
          width: 210px;
          height: 140px;
          margin-right: 15px;
          overflow: hidden;
          text-decoration: none;

          .article_cover {
            border-radius: 4px;
            object-fit: cover;
            width: 100%;
            height: 100%;
          }

          .iconfont {
            position: absolute;
            top: 2px;
            left: 5px;
            color: var(--background-color);
            font-size: 18px;
          }

          .time {
            display: block;
            padding: 5px 5px;
            border-radius: 20px;
            position: absolute;
            font-size: 12px;
            top: 3px;
            right: -38%;
            background-color: var(--theme-color);
            color: var(--background-color);
          }
        }

        .information {
          display: flex;
          -webkit-box-orient: vertical;
          -webkit-box-direction: normal;
          -ms-flex-direction: column;
          flex-direction: column;
          -webkit-box-flex: 1;
          -ms-flex: 1;
          flex: 1;
          min-width: 0;

          .titleName {
            margin-bottom: 10px;
            color: var(--article-color);
            font-size: 18px;
            line-height: 24px;
            max-height: 48px;
            -webkit-transition: color .35s;
            transition: color .35s;
            text-decoration: none;

            .top {
              height: 20px;
              padding: 0 6px;
              margin-right: 5px;
              line-height: 20px;
              font-size: 12px;
              white-space: nowrap;
              vertical-align: 2px;
              color: var(--background-color);
              background-image: -webkit-linear-gradient(0deg, #3ca5f6 0, #a86af9 100%);
              border-radius: 2px 6px;
              display: inline-block;
            }
          }

          .info {
            color: var(--text-color);
            line-height: 22px;
            max-height: 44px;
            opacity: .85;
            text-decoration: none;
          }

          .meta {
            -webkit-box-pack: justify;
            -ms-flex-pack: justify;
            justify-content: space-between;
            margin-top: auto;
            color: var(--text-color);
            font-size: 13px;
            display: flex;
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;

            .items {
              -webkit-box-pack: start;
              -ms-flex-pack: start;
              justify-content: flex-start;
              -ms-flex-negative: 0;
              flex-shrink: 0;
              display: flex;
              -webkit-box-align: center;
              -ms-flex-align: center;
              align-items: center;
              padding: 0 0;

              li {
                list-style: none;
                display: inline-block;

                &:after {
                  content: "/";
                  color: #c0c4cc;
                  padding: 0 5px;
                }

                &:last-child:after {
                  content: "";

                }

                i {
                  padding: 0 3px;
                  font-size: 13px;
                }

                .tag_name {
                  color: var(--text-color);
                  text-decoration: none;

                  &:hover {
                    cursor: pointer;
                    color: var(--theme-color);
                  }
                }
              }
            }
          }
        }
      }
    }

    .empty-box {
      text-align: center;
      margin-bottom: 50px;
      color: var(--text-color);
    }

  }


}


</style>
