<template>
    <div class="page_404">
        <div class="error">
            <img src="./404.jpg" alt="404">
            <h3 class="title">“未找到您要的资源,<span style="color: red;">{{ countDown }}</span>秒后自动跳转到首页”</h3>
            <a href="/" class="error_link">回到主页</a>
        </div>
    </div>
</template>

<script>
export default {
    name: "NF404",
    data() {
        return {
            countDown: 5,
            timer: null
        }
    },
    methods: {
        goHome() {
            this.$router.push('/')
            clearInterval(this.timer)
        }
    },
    created() {
        this.timer = setInterval(() => {
            this.countDown > 0 ? this.countDown-- : this.goHome()
        }, 1000)
    }
}
</script>

<style lang="scss" scoped>
.page_404 {
    position: absolute;
    z-index: 10;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: #fff;
    color: #666;
    text-align: center;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    font-family: sans-serif;
    -ms-text-size-adjust: 100%;
    -webkit-text-size-adjust: 100%;
    -webkit-font-smoothing: antialiased;

    .error {
        margin-top: -10vh;

        img {
            width: 60%;
            max-width: 700px;
        }

        .title {
            font-size: 30px;
            margin-top: 20px;
            margin-bottom: 40px;
            -webkit-user-select: none;
            user-select: none;
            font-weight: 300;
            color: #888;
        }

        .error_link {
            padding: 12px 36px;
            font-size: 14px;
            text-decoration: none;
            -webkit-user-select: none;
            user-select: none;
            color: #999;
            border: 1px solid #999;
            border-radius: 100px;
            -webkit-transition: color .3s;
            transition: color .3s;
        }
    }


}
</style>