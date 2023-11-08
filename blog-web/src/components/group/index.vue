<template>
    <div class="container">
        <el-card class="group-main">
            <div class="title">
                <i class="iconfont icon-erweima1"></i>
                <span class="titleName">👏🏻欢迎加入讨论组👏🏻</span>
            </div>
            <div class="erweima">
                <img src="./qqgroup.jpg" alt="">
                <span class="desc">大家有什么问题可以加入讨论组</span>
            </div>
        </el-card>
    </div>
</template>
<style lang="scss" scoped>
.container {
    padding: 0;

    .group-main {
        width: 300px;
        border-radius: 10px;
        background-color: var(--background-color);

        &:hover {
            box-shadow: 0 4px 8px 6px rgba(7, 17, 27, .06);
            transition: all .3s;
        }

        .title {
            width: 300px;
            height: 45px;
            border-top-left-radius: 12px;
            border-top-right-radius: 12px;
            border-bottom: 1px solid var(--border-line);

            i {
                margin-left: 20px;
                color: var(--theme-color);
            }

            .titleName {
                margin-left: 10px;
                color: var(--box-title_color);
            }

            i,
            span {
                line-height: 45px;
            }
        }

        .erweima {

            img {
                height: 226px;
                width: 250px;
                margin-left: 25px;
            }

            .desc {
                color: #606266;
                font-size: 14px;
                padding-left: 20px;
                height: 30px;
                display: block;
            }
        }
    }

}
</style>
