<template>
  <div class="artcile_main">
    <el-card class="item">
      <div class="title">
        <span>
            <i style="color: var(--theme-color);font-weight: 700;margin-right: 5px;"
               class="iconfont icon-fenlei"></i>
            以下是
            <span style="color: var(--theme-color);font-weight: 700;">{{ name }}</span>
            相关的文章：
        </span>
      </div>
      <div v-if="articleList.length">
        <ul class="ul_item" v-for="item in articleList" :key="item.id">
          <li class="main_li" @click="handleClick(item.id)" data-wow-duration="2s" data-wow-iteration="1">
            <div class="thumbnail">
              <el-image class="article_cover" :src="item.avatar" :alt="item.title"/>
              <i class="iconfont icon-tupian"></i>
              <span class="time">{{ formatDate(item.createTime) }}</span>
            </div>
            <div class="information">
              <div class="titleName">
                <span v-if="item.isStick == 1" class="top">置顶</span>
                {{ item.title }}
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
                  <li v-for="tag in item.tagList" class="tagLi">
                    <i class="iconfont icon-biaoqian"></i>
                    <span class="tag_name" @click="">{{ tag.name }}</span>
                  </li>
                </ul>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <div class="empty-box" v-else>
        <el-empty description="哎呀，文章丢失啦..."></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>

import {fetchArticleList} from '@/api'

export default {
  data() {
    return {
      pageData: {
        pageNo: 1,
        pageSize: 6,
        tagId: this.$route.query.id,
      },
      name: this.$route.query.name,
      pageTotal: 0,
      articleList: []
    }
  },
  created() {
    document.title = "标签 - " + this.name
    this.fetchArticleList()
  },
  methods: {
    // 分页
    onPage() {
      this.pageData.pageNo++;
      this.fetchArticleList()
    },
    handleClick(id) {
      this.$router.push({path: '/articleInfo', query: {articleId: id}})
    },
    formatDate: function (value, args) {
      var dt = new Date(value);
      let year = dt.getFullYear();
      let month = (dt.getMonth() + 1).toString().padStart(2, '0');
      let date = dt.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${date}`;
    },
    fetchArticleList() {
      fetchArticleList(this.pageData).then(res => {
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

@media screen and (min-width: 960px) {
  .artcile_main {
    min-height: calc(100vh - 167px);
    display: flex;
    justify-content: center;
    position: relative;

    .item {
      border-radius: 12px;
      background-color: var(--background-color);
      width: 80%;
      height: 100%;
      padding: 10px;
      margin-top: 80px;

      &:hover {
        box-shadow: 5px 4px 8px 6px rgba(7, 17, 27, .06);
        transition: all .3s;
      }

      .title {
        height: 45px;
        position: relative;
        border-bottom: 1px solid var(--border-line);
        line-height: 45px;
        color: var(--text-color);
      }

      .ul_item {
        padding-left: 20px;
        cursor: pointer;


        &:hover {
          .time {
            transform: translateX(-110%) translateY(5%);
            transition: all 0.5s;
          }
        }

        .main_li {
          display: flex;
          margin-top: 15px;
          padding-bottom: 15px;
          border-bottom: 1px solid var(--border-line);
          list-style: none;
          position: relative;

          .thumbnail {
            flex-shrink: 0;
            position: relative;
            width: 210px;
            height: 140px;
            margin-right: 15px;
            overflow: hidden;
            text-decoration: none;

            .article_cover {
              border-radius: 5px;
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
              right: -39%;
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
            width: 100%;

            .titleName {
              color: var(--article-color);
              font-size: 18px;
              line-height: 24px;
              max-height: 48px;
              -webkit-transition: color .35s;
              transition: color .35s;
              text-decoration: none;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              width: 100%;

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
              // 透明度
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
                    margin: 0 5px;
                  }

                  &:last-child:after {
                    content: "";
                  }

                  i {
                    padding: 0 3px;
                    font-size: 13px;
                  }
                }

                .tagLi {
                  color: var(--text-color);

                  &:hover {
                    color: var(--theme-color);
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
}
</style>
