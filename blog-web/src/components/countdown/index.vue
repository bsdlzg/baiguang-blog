<template>
    <div class="countdown-main">
        <el-card class="box">
            <div class="title">
                <i class="iconfont icon-daojishi"></i>
                <span>人生倒计时</span>
            </div>
            <div class="content">
                <div class="item">
                    <div>
                        <span class="name">今日已经过去 <strong>{{ today.time }}</strong> 小时</span>
                    </div>
                    <div class="progress">
                        <div class="progress-bar">
                            <div class="progress-bar-inner progress-bar-inner-1" :style="today.style"></div>
                        </div>
                        <div class="progressName">{{ today.percentage }}%</div>
                    </div>
                </div>

                <div class="item">
                    <div>
                        <span class="name">这周已经过去 <strong>{{ week.time }}</strong> 天</span>
                    </div>
                    <div class="progress">
                        <div class="progress-bar">
                            <div class="progress-bar-inner progress-bar-inner-2" :style="week.style"></div>
                        </div>
                        <div class="progressName">{{ week.percentage }}%</div>
                    </div>
                </div>

                <div class="item">
                    <div>
                        <span class="name">本月已经过去 <strong>{{ monthDay.time }}</strong> 天</span>
                    </div>
                    <div class="progress">
                        <div class="progress-bar">
                            <div class="progress-bar-inner progress-bar-inner-3" :style="monthDay.style"></div>
                        </div>
                        <div class="progressName">{{ monthDay.percentage }}%</div>
                    </div>
                </div>

                <div class="item">
                    <div>
                        <span class="name">今年已经过去 <strong>{{ month.time }}</strong> 个月</span>
                    </div>
                    <div class="progress">
                        <div class="progress-bar">
                            <div class="progress-bar-inner progress-bar-inner-4" :style="month.style"></div>
                        </div>
                        <div class="progressName">{{ month.percentage }}%</div>
                    </div>
                </div>
            </div>
        </el-card>
    </div>
</template>
<script>
export default {
    data() {
        return {
            today: {
                time: 0,
                percentage: 0,
                style: '0%'
            },
            week: {
                time: 0,
                percentage: 0,
                style: '0%'
            },
            monthDay: {
                time: 0,
                percentage: 0,
                style: '0%'
            },
            month: {
                time: 0,
                percentage: 0,
                style: '0%'
            }
        }
    },
    created() {
        this.getTodayTime()
        this.getWeekTime()
        this.getMonthDay()
        this.getMonth()
    },
    methods: {
        getTodayTime() {
            var now = new Date();
            this.today.time = now.getHours()
            let percentage = this.today.time / 24 * 100;
            this.today.style = 'width: ' + percentage + '%';
            this.today.percentage = Math.round(percentage);
        },
        getWeekTime() {
            var week = new Date().getDay();
            if (week === 0) {
              week = 6;
            } else {
              week = week - 1;
            }
            this.week.time = week;
            let percentage = week / 7 * 100;
            this.week.style = 'width: ' + percentage + '%';
            this.week.percentage = Math.round(percentage);
        },
        getMonthDay() {
            var date = new Date();
            this.monthDay.time = date.getDate();
            date.setMonth(date.getMonth() + 1); // 先设置为下个月
            date.setDate(0); // 再置0，变成当前月最后一天
            let percentage = this.monthDay.time / date.getDate()
            if (percentage > 1) {
                percentage = 100
                this.monthDay.percentage = 100
            } else {
                percentage = percentage * 100;
                this.monthDay.percentage = Math.round(percentage);
            }

            this.monthDay.style = 'width: ' + percentage + '%';


        },
        getMonth() {
            var date = new Date();
            this.month.time = date.getMonth() + 1;
            let percentage = this.month.time / 12 * 100;
            this.month.style = 'width: ' + percentage + '%';
            this.month.percentage = Math.round(percentage);

        }
    }

}
</script>
<style lang="scss" scoped>
.countdown-main {
    padding: 0;

    .box {
        margin-top: 20px;
        border-radius: 3px;
        background-color: var(--background-color);

        &:hover {
            box-shadow: 5px 4px 8px 6px rgba(7, 17, 27, .06);
            transition: all .3s;
        }

        .title {
            border-top-left-radius: 12px;
            border-top-right-radius: 12px;
            border-bottom: 1px solid var(--border-line);
            padding: 10px 0;

            i {
                margin-left: 20px;
                color: var(--theme-color);
            }

            span {
                margin-left: 10px;
                font-weight: 700;
                color: var(--box-title_color);
            }
        }

        .content {
            border-bottom-left-radius: 12px;
            border-bottom-right-radius: 12px;

            .item {
                margin-left: 10px;
                font-size: 12px;
                padding: 10px 0;
            }

            .name {
                color: var(--text-color);

                strong {
                    color: var(--theme-color);
                }
            }

            .progress {
                display: flex;
                align-items: center;
                margin-top: 5px;

                .progress-bar {
                    height: 10px;
                    border-radius: 5px;
                    overflow: hidden;
                    background: var(--border-line);
                    width: 0px;
                    min-width: 0px;
                    -webkit-box-flex: 1;
                    flex: 1;
                    margin-right: 5px;

                    .progress-bar-inner {
                        height: 100%;
                        border-radius: 5px;
                        animation: progress 750ms linear infinite;
                        transition: width .35s;
                    }

                    .progress-bar-inner-1 {
                        background: #bde6ff;
                        background-size: 30px 30px;
                        background: linear-gradient(135deg, rgb(80, 191, 255) 25%, transparent 25%, transparent 50%, rgb(80, 191, 255) 50%, rgb(80, 191, 255) 75%, transparent 75%, transparent 100%) 0% 0% / 30px 30px rgb(189, 230, 255);
                    }

                    .progress-bar-inner-2 {
                        background: #ffd980;
                        background-size: 30px 30px;
                        background-image: linear-gradient(135deg, #f7ba2a 25%, transparent 25%, transparent 50%, #f7ba2a 50%, #f7ba2a 75%, transparent 75%, transparent 100%);
                    }

                    .progress-bar-inner-3 {
                        background: #ffa9a9;
                        background-image: linear-gradient(135deg, #ff4949 25%, transparent 25%, transparent 50%, #ff4949 50%, #ff4949 75%, transparent 75%, transparent 100%);
                        background-size: 30px 30px;
                    }

                    .progress-bar-inner-4 {
                        background: #67c23a;
                        background-image: linear-gradient(135deg, #4f9e28 25%, transparent 25%, transparent 50%, #4f9e28 50%, #4f9e28 75%, transparent 75%, transparent 100%);
                        background-size: 30px 30px;
                    }
                }

                .progressName {
                    width: 38px;
                    font-size: 12px;
                    color: var(--text-color);
                }
            }

        }
    }


    @keyframes progress {
        0% {
            background-position: 0 0
        }

        to {
            background-position: 30px 0
        }
    }
}
</style>
