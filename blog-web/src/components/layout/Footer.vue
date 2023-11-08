<template>
  <div class="footer_container">
    <div class="content">
      <div class="site">
        <p class="run_time">
          <i class="iconfont icon-shuangshandian"></i>
          <span>已运行
              <strong>{{ day }}</strong> 天
              <strong>{{ hours }}</strong> 时
              <strong>{{ minute }}</strong> 分
              <strong>{{ second }}</strong> 秒
          </span>
        </p>
        <p>
          2023 &copy;白光博客
          <a href="https://beian.miit.gov.cn/" target="_blank" style="margin-left: 10px">
            浙ICP备2021036698号-1
          </a>
        </p>
      </div>

      <div class="site-number">
        <span class="site_pv">
          <i class="iconfont icon-fangwenliang"></i>
            访问量
            <em class="pv_num">
                {{ this.$store.state.siteAccess }}
            </em>
        </span>
        <span class="site_uv">
          <i class="iconfont icon-zongfangkeliang"></i>
          访客量
          <em class="uv_num">
              {{ this.$store.state.visitorAccess }}
          </em>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Footer',
  data() {
    return {
      day: "",
      hours: "",
      minute: "",
      second: "",
      url: "https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2020%2F0601%2Fba14f84ep00qb7neh000xd200jx00bog00gn009q.png&thumbnail=650x2147483647&quality=80&type=jpg"
    }
  },
  created() {
    this.timer = setInterval(this.runTime, 1000);
  },
  methods: {
    runTime() {
      var timeold = new Date().getTime() - new Date('2021-09-24 08:08:08').getTime();
      var msPerDay = 24 * 60 * 60 * 1000;
      var daysold = Math.floor(timeold / msPerDay);
      var day = new Date();
      this.day = daysold;
      this.hours = day.getHours();
      this.minute = day.getMinutes();
      this.second = day.getSeconds();
    },
  }
}
</script>

<style lang="scss" scoped>

@keyframes bgSize {
  0% {
    background-size: 300% 100%;
  }

  100% {
    background-size: 100% 100%;
  }
}


.footer_container {
  position: relative;
  margin-top: 50px;
  padding: 10px 20px;
  bottom: 0;
  display: flex;
  align-items: center;
  background-color: #ffffff;
  justify-content: center;
  animation: bgSize 5s infinite ease-in-out alternate;

  .content {

    .site {
      font-size: 14px;
      color: var(--text-color);

      p {
        margin: 10px 0;
      }

      a {
        text-decoration: none;
        color: var(--theme-color);
      }

      .run_time {
        height: 20px;
        align-items: center;

        i {
          font-size: 14px;
          line-height: 20px;
        }

        span {
          line-height: 20px;
          font-size: 14px;
          padding-left: 5px;

          strong {
            color: var(--theme-color);
          }
        }

        .icon-shuangshandian {
          color: #00ff00;
        }
      }
    }

    .site-number {
      i {
        display: inline-block;
        font-size: 12px;
      }

      .site_pv, .site_uv, .online_user {
        //position: relative;
        display: inline-block;
        margin-right: 4px;
        height: 24px;
        padding-left: 10px;
        line-height: 24px;
        overflow: hidden;
        font-size: 12px;
        font-weight: 700;
        color: var(--site-text-color);
        background: var(--site-color);
        border-radius: 5px;
      }

      .pv_num, .uv_num {
        display: inline-block;
        font-size: 12px;
        padding: 0 15px;
      }

      .pv_num {
        background: linear-gradient(0deg, #d0339b, #d03336);
      }

      .uv_num {
        background: linear-gradient(0deg, #f69a69, #e9d161);
      }
    }
  }
}
</style>
