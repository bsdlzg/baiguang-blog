<template>
    <div class="theme_main">
        <router-link :to="'/im'">
            <a href="javascript:void(0)" class="joe_action_item chat active">
                <el-tooltip class="item" effect="dark" content="聊天室" placement="top-start">
                    <svg-icon icon-class="charRoom"></svg-icon>
                </el-tooltip>
            </a>
        </router-link>
        <a href="javascript:void(0)" class="joe_action_item theme active" @click="setSkin()">
            <el-tooltip class="item" effect="dark" content="切换主题" placement="top-start">
                <svg-icon v-if="skin == 'deep'" icon-class="shallow"></svg-icon>
                <svg-icon v-else icon-class="deep"></svg-icon>
            </el-tooltip>
        </a>
        <a href="javascript:void(0)" title="回到顶部" class="joe_action_item back2top active" @click="backTop()">
            <el-tooltip class="item" effect="dark" content="回到顶部" placement="top-start">
                <svg-icon icon-class="topBar"></svg-icon>
            </el-tooltip>
        </a>
    </div>
</template>
<script>
import { setSkin, getSkin } from '@/utils/auth'
export default {
    data() {
        return {
            theme: true,
            showBtn: false, // 回到顶部，默认是false，就是隐藏起来
            skin: getSkin()
        }
    },
    mounted() {
        window.addEventListener("scroll", this.toTop, true);
    },

    methods: {
        setSkin() {
            this.skin = getSkin()
            let val = ''
            if (this.skin == 'shallow') { //浅色模式
                val = 'deep'
            } else {
                val = 'shallow'
            }
            setSkin(val)
            this.skin = val
            this.$setSkin()
        },
        toTop() {
            let that = this;
            let scrollTop =
                window.pageYOffset ||
                document.documentElement.scrollTop ||
                document.body.scrollTop;
            that.scrollTop = scrollTop;
            if (scrollTop > 200) {
                this.showBtn = true;
            } else {
                this.showBtn = false;
            }
        },
        backTop() {
            var timer = setInterval(function () {
                let osTop =
                    document.documentElement.scrollTop || document.body.scrollTop;
                let ispeed = Math.floor(-osTop / 5);
                document.documentElement.scrollTop = document.body.scrollTop =
                    osTop + ispeed;
                this.isTop = true;
                if (osTop === 0) {
                    clearInterval(timer);
                }
            }, 30);
        },
    },
}
</script>

<style lang="scss" scoped>
.theme_main {
    position: fixed;

    .joe_action_item.theme.active {
        pointer-events: auto;
        visibility: visible;
        -webkit-transform: scale(1);
        transform: scale(1);
        position: fixed;
        bottom: 150px;
        right: 10px;
    }

    .chat {
        position: fixed;
        bottom: 190px;
        right: 10px;
        margin-bottom: 10px;
    }

    a {
        display: block;
        height: 50px;
        position: relative;
    }

    .joe_action_item svg {
        position: absolute;
        width: 30px;
        height: 30px;
        fill: var(--theme-color);
        top: 9px;
        right: 10px;

    }

    .joe_action_item.back2top.active {
        pointer-events: auto;
        visibility: visible;
        -webkit-transform: scale(1);
        transform: scale(1);
        position: fixed;
        bottom: 100px;
        right: 10px;
        animation: fade-in 0.3s linear 1;
    }

    @keyframes fade-in {
        0% {
            transform: scale(0);
        }

        100% {
            transform: scale(1);
        }
    }
}
</style>
