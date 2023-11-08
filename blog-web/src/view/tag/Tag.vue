<template>
  <div class="tag">
    <!-- 归档列表   -->
    <el-card class="tag-container">
      <div class="title">目前共计{{tagList.length}}个标签，继续加油</div>
      <div style="border-bottom: 1px solid var(--border-line);margin-bottom: 20px;"></div>
      <div class="tagItem">
        <a @click="handleClike(item)" class="item" v-for="item in tagList" :key="item.id">
          <el-image class="cover" lazy-src="https://picsum.photos/id/11/10/6" max-height="100%" max-width="100%"
                    :src="item.avatar"></el-image>
          <span class="name">
            {{item.name}}
          </span>
          <em class="num">
            {{ item.articleCount}}篇
          </em>
          <div class="mask-img"></div>
        </a>
      </div>
    </el-card>
  </div>
</template>

<script>

import { fetchTagList } from '@/api'

export default {
  created() {
    document.title = "文章标签";
    this.fetchTagList();
  },

  data: function () {
    return {
      tagList: [],
      absolute: true,
      opacity: 0.5,
      overlay: false,
    };
  },
  methods: {
    fetchTagList() {
      fetchTagList().then(res => {
        this.tagList = res.data;
      });
    },
    handleClike(item) {
      console.log(item)
      this.$router.push({ name: "/tags", query: { id: item.id, name: item.name } })
    }
  },
}
</script>

<style lang="scss" scoped>
@media screen and (min-width: 960px) {
  .tag {
    display: flex;
    justify-content: center;
    position: relative;
    min-height: calc(100vh - 167px);


    .tag-container {
      background-color: var(--background-color);
      border-radius: 12px;
      padding: 20px;
      height: 100%;
      width: 55%;
      margin-top: 80px;


      .title {
        margin-bottom: 10px;
        color: var(--theme-color);
        font-weight: 700;
      }

      &:hover {
        box-shadow: 5px 4px 8px 6px rgba(2, 6, 10, 0.06);
        transition: all .3s;
      }

      .tagItem {
        display: grid;
        grid-template-rows:  repeat(5, 1fr);
        grid-template-columns:  repeat(5, 1fr);

        .item {
          display: inline-block;
          overflow: hidden;
          border-radius: 5px;
          position: relative;
          cursor: pointer;
          margin: 10px;


          &:hover {
            .name {
              color: var(--theme-color);
            }

            .mask-img {
              display: block;
            }
          }

          .cover {
            height: 120px;
            width: 100%;
          }

          .name {
            color: var(--category-text-color);
            font-size: 13px;
            display: block;
            text-align: center;
            background-color: var(--category-backgorud-color);
            line-height: 32px;
          }


          .num {
            display: inline-block;
            height: 10px;
            position: absolute;
            top: 5px;
            right: 5px;
            background-image: linear-gradient(to right, #2afcfc, #4f3edd);
            background-color: #4f3edd;
            border-radius: 8px;
            font-size: 12px;
            padding: 0 8px;
            height: 18px;
            align-items: center;
            display: flex;
            color: #fff;
            font-style: normal;
            white-space: nowrap;
          }

          .mask-img {
            background-color: rgba(101, 101, 101, 0.1); //设置透明度 ，改最后一个数值，0-1
            width: 100%;
            height: 100%;
            z-index: 10;
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            display: none;
          }
        }
      }
    }
  }
}
</style>
