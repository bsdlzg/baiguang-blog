<template>
  <section class="category">
  <!--  归档目录  -->
    <el-card class="category-container">
      <section class="title">目前共计{{categoryList.length}}个分类，继续加油</section>
      <section style="border-bottom: 1px solid var(--border-line);margin-bottom: 20px;"></section>
      <section class="categoryList">
        <a @click="handleClike(item)" class="item" v-for="item in categoryList" :key="item.id">
          <el-image class="cover" lazy-src="https://picsum.photos/id/11/10/6" :src="item.avatar" />
          <span class="name">{{item.name}}</span>
          <em class="num">{{item.articleCount}}</em>
          <section class="mask-img"></section>
        </a>
      </section>
    </el-card>
  </section>
</template>

<script>
import {featchCategory} from '@/api'
export default {
  name: 'Category',
  data() {
    return {
      categoryList: [],
      absolute: true,
      opacity: 0.5,
      overlay: 0.5
    }
  },
  methods: {
    handleClike(item) {
      this.$router.push({name: "/category", query: {id: item.id, name: item.name}})
    },
    fetchCategoryList() {
      featchCategory().then(res => {
        this.categoryList = res.data
      })
    }
  },
  created() {
    document.title = "文章分类"
    this.fetchCategoryList()
  }
}
</script>

<style lang="scss" scoped>

@media screen and (min-width: 960px){
  .category {
    display: flex;
    justify-content: center;
    position: relative;
    min-height: calc(100vh - 167px);


    .category-container {
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
        text-align: center;
      }

      &:hover {
        box-shadow: 5px 4px 8px 6px rgba(2, 6, 10, 0.06);
        transition: all .3s;
      }

      .categoryList {
        display: grid;
        grid-template-rows: repeat(5, 1fr);
        grid-template-columns: repeat(5, 1fr);

        .item {
          display: inline-block;
          overflow: hidden;
          border-radius: 5px;
          position: relative;
          margin-bottom: 10px;
          cursor: pointer;
          margin-left: 10px;

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
            height: 18px;
            background-image: linear-gradient(to right, #2afcfc, #4f3edd);
            background-color: #4f3edd;
            border-radius: 8px;
            font-size: 12px;
            padding: 0 8px;
            align-items: center;
            display: flex;
            color: #fff;
            font-style: normal;
            justify-content: center;
            white-space: nowrap;
            position: absolute;
            top: 5px;
            right: 5px;
          }

          .mask-img {
            background: rgba(101, 101, 101, 0.1); //设置透明度 ，改最后一个数值，0-1
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
