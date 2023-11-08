<template>
    <div class="container">
        <el-card class="notice-main">
            <div class="title">
                <i class="el-icon-bell"></i>
                <span class="titleName">公告信息</span>
            </div>
            <div class="info">
                <span>{{ $store.state.webSiteInfo.bulletin }}</span>
            </div>
        </el-card>
    </div>
</template>
<script>
export default {
    data() {
        return {
        }
    },
    methods: {

    }
}
</script>
<style lang="scss" scoped>
.container {
    padding: 0;

    .notice-main {
        width: 300px;
        margin-top: 15px;
        border-radius: 10px;
        background-color: var(--background-color);

        &:hover {
            box-shadow: 5px 4px 8px 6px rgba(7, 17, 27, .06);
            transition: all .3s;
        }

        .title {
            width: 300px;
            height: 40px;

            i {
                color: var(--theme-color);
                font-size: 16px;
                line-height: 40px;
                padding-left: 20px;
                font-weight: 700;
            }

            .titleName {
                color: var(--box-title_color);
                line-height: 40px;
                font-size: 16px;
                padding-left: 10px;
                font-weight: 600;
            }
        }

        .info {
            border-top: 1px solid var(--border-line);
            background: var(--notice-background);
            animation: light 2s ease-in-out infinite alternate;

            span {
                padding: 15px;
                line-height: 50px;
                font-size: 14px;
                color: var(--text-color);
            }
        }
    }

}

@keyframes light {
    from {
        box-shadow: inset 0 0 4px var(--theme-color);
    }

    to {
        box-shadow: inset 0 0 15px var(--theme-color);
    }
}
</style>
