<template>
    <div class="comment-main">
        <div class="comment-item">
            <div class="box">
                <div class="avatar">
                    <img v-if="user" :src="user.avatar" alt="">
                    <img v-else :src="require('@/assets/touristAvatar.png')" alt="">
                </div>
                <div class="ml-3">
                    <div data-v-0089e256="" class="comment-input">
                        <textarea placeholder="留下点什么吧..." v-model="commentContent" class="comment-textarea"></textarea>
                    </div>
                    <div class="emoji-container">
                        <span @click="chooseEmoji = !chooseEmoji" :class="chooseEmoji ? 'emoji-btn-active' : 'emoji-btn'">
                            <i class="iconfont icon-biaoqing"></i>
                        </span>
                        <el-button @click="addComment" style="" class="upload-btn v-comment-btn">
                            提交
                        </el-button>
                    </div>
                    <div class="emoji-wrapper" v-show="chooseEmoji">
                        <span class="emoji-item" v-for="(item, index) of emojiList" :key="index" @click="addEmoji(item)">
                            <span class="emoji">{{ item.emoji }}
                            </span>
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <ul class="commentwrap">
            <div class="comment-wrp">
                <li class="ul-item" ref="commentBoxref" v-for="(item, index) in commentList" :key="index">
                    <!-- 评论内容 -->
                    <div class="comment" @mouseenter="replyEnter(item.id, false)" @mouseleave="replyLeave(item.id)">
                        <div class="main">
                            <div class="profile">
                                <a :href="item.webSite" target="_blank">
                                    <img :src="item.avatar" alt="">
                                </a>
                            </div>
                            <div class="commentinfo">
                                <section class="commeta">
                                    <div class="left">
                                        <h4 class="author">
                                            <a :href="item.webSite" target="_blank" class="disabled">
                                                <span class="bozhu" v-if="item.userId == articleUserId">
                                                    博主
                                                </span>
                                                {{ item.nickname }}
                                            </a>
                                        </h4>
                                    </div>
                                    <a href="javascript:;" :ref="'replyBtn' + item.id"
                                        @click="replyComment(item, item.id, false)" class="comment-reply-link">回复</a>
                                    <div class="right">
                                        <div class="info">
                                            <time itemprop="datePublished" datetime="1680523318635" class="comment-time">发布于
                                                {{
                                                    formatDate(item.createTime) }}
                                            </time>
                                            <span class="useragent-info">（
                                                <svg-icon :icon-class="item.browser" />
                                                {{ item.browserVersion }} &nbsp;
                                                <svg-icon :icon-class="item.system" />
                                                {{ item.systemVersion }}
                                                ）
                                            </span>
                                            <span class="ipAddress">
                                                IP属地:{{ splitIpAddress(item.ipAddress) }}
                                            </span>
                                        </div>
                                    </div>
                                </section>
                                <div class="body markdown-body">
                                    <div class="markdown-content">
                                        <p>
                                            {{ item.content }}
                                        </p>
                                    </div>
                                </div>
                                <!-- 回复框 -->
                                <Reply :ref="'reply' + item.id" @reloadReply="reloadReply">
                                </Reply>
                            </div>
                        </div>
                    </div>
                    <ul class="children">
                        <div class="comment-wrp">
                            <li class="ul-item" v-for="(childrenItem, childerenIndex) in item.children"
                                :key="childerenIndex">
                                <!-- 评论内容 -->
                                <div class="comment" @mouseenter="replyEnter(childrenItem.id, true)"
                                    @mouseleave="replyLeave(childrenItem.id, true, index)">
                                    <div class="main">
                                        <div class="profile">
                                            <a :href="childrenItem.webSite">
                                                <img :src="childrenItem.avatar" alt="">
                                            </a>
                                        </div>
                                        <div class="commentinfo">
                                            <section class="commeta">
                                                <div class="left">
                                                    <h4 class="author">
                                                        <a :href="childrenItem.webSite" target="_blank" class="disabled">
                                                            <span class="bozhu" v-if="childrenItem.userId == articleUserId">
                                                                博主
                                                            </span>
                                                            {{ childrenItem.nickname }}
                                                        </a>
                                                    </h4>
                                                </div>
                                                <a href="javascript:;" :ref="'childrenBtn' + childrenItem.id"
                                                    @click="replyComment(childrenItem, item.id, true)"
                                                    class="comment-reply-link">回复</a>
                                                <div class="right">
                                                    <div class="info">
                                                        <time itemprop="datePublished" datetime="1680523318635"
                                                            class="comment-time">发布于
                                                            {{ formatDate(childrenItem.createTime) }}
                                                        </time>
                                                        <span class="useragent-info">（
                                                            <svg-icon :icon-class="childrenItem.browser" />
                                                            {{ childrenItem.browserVersion }} &nbsp;
                                                            <svg-icon :icon-class="childrenItem.system" />
                                                            {{ childrenItem.systemVersion }}
                                                            ）
                                                        </span>
                                                        <span class="ipAddress">
                                                            IP属地:{{ splitIpAddress(childrenItem.ipAddress) }}
                                                        </span>
                                                    </div>

                                                </div>
                                            </section>
                                            <div class="body markdown-body">
                                                <div class="markdown-content">
                                                    <p>
                                                        <a href="javascript:;"
                                                            style="color: #99CE00;text-decoration: none;">@{{
                                                                childrenItem.replyNickname }}</a>

                                                        {{ childrenItem.content }}
                                                    </p>
                                                </div>
                                            </div>
                                            <!-- 回复框 -->
                                            <Reply :ref="'replys' + childrenItem.id" @reloadReply="reloadReply">
                                            </Reply>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </div>
                    </ul>
                </li>
                <div class="more-btn" v-if="pageNo < total" @click="moreComment">加载更多...</div>
            </div>
        </ul>
    </div>
</template>
<script>
import { postComment, featchComments } from '@/api'
import { browserMatch } from '@/utils/index'
import Reply from './Reply.vue'
export default {
    components: {
        Reply
    },
    props: {
        commentList: {
            type: Array,
            default: () => [],
        },
        total: {
            type: Number,
            default: 0,
        },
        articleUserId: {
            type: String,
            default: "",
        }
    },
    data() {
        return {
            chooseEmoji: false,
            emojiList: null,
            commentContent: "",
            opacity: 0,
            pageNo: 1,
            show: null,
            user: this.$store.state.userInfo,
            articleId: this.$route.query.articleId,
        }
    },

    created() {
        this.emojiList = require('@/assets/emoji.json');
    },
    methods: {
        replyLeave(index, isChilderen) {
            if (isChilderen) {
                this.$refs[`childrenBtn${index}`][0].style.opacity = 0
            } else {
                this.$refs[`replyBtn${index}`][0].style.opacity = 0

            }
        },
        replyEnter(index, isChilderen) {
            if (isChilderen) {
                this.$refs[`childrenBtn${index}`][0].style.opacity = 1
            } else {
                this.$refs[`replyBtn${index}`][0].style.opacity = 1

            }
        },
        replyComment(item, parentId, isChilderen) {
            if (this.user == null) {
                this.$store.state.loginFlag = true;
                return
            }
            if (isChilderen) {
                this.$refs['replys' + item.id][0].showBox = !this.$refs['replys' + item.id][0].showBox
                //传值给回复框
                this.$refs['replys' + item.id][0].commentContent = "";
                this.$refs['replys' + item.id][0].nickname = item.nickname;
                this.$refs['replys' + item.id][0].replyUserId = item.userId;
                this.$refs['replys' + item.id][0].parentId = parentId;
                this.$refs['replys' + item.id][0].index = item.id;
            } else {
                this.$refs['reply' + item.id][0].showBox = !this.$refs['reply' + item.id][0].showBox
                //传值给回复框
                this.$refs['reply' + item.id][0].commentContent = "";
                this.$refs['reply' + item.id][0].nickname = item.nickname;
                this.$refs['reply' + item.id][0].replyUserId = item.userId;
                this.$refs['reply' + item.id][0].parentId = parentId;
                this.$refs['reply' + item.id][0].index = item.id;
            }
        },
        reloadReply(index) {
            let query = {
                pageNo: this.pageNo,
                pageSize: 5,
                articleId: this.articleId
            }
            featchComments(query).then(res => {
                this.commentList = res.data.records
            })
        },
        addComment() {
          this.user = this.$store.state.userInfo
            if (this.user == null) {
                this.$store.state.loginFlag = true;
                return
            }
            if (!this.commentContent) {
                this.$message.error('评论不能为空');
                return;
            }
            let browser = browserMatch()
            let comment = {
                articleId: this.articleId,
                avatar: "https://sdn.geekzu.org/avatar/eeb4fe09a1aaad7964b055f331f72608?s=256&d=monsterid",
                content: this.commentContent,
                browser: browser.browser.toLowerCase(),
                browserVersion: browser.browser + " " + browser.version,
            }
            postComment(comment).then(res => {
                this.$emit("reloadComment");
                this.$message({
                    message: '评论成功',
                    type: 'success'
                });
                this.commentContent = ""
                this.pageNo = 1
            }).catch(err => {
                this.$message.error(err.message);
            })

        },
        moreComment() {
            this.pageNo++;
            let query = {
                pageNo: this.pageNo,
                pageSize: 5,
                articleId: this.articleId
            }
            featchComments(query).then(res => {
                res.data.records.forEach(item => {
                    this.commentList.push(item);
                })
            })
        },
        addEmoji(obj) {
            this.commentContent += obj.emoji;
        },
        splitIpAddress(address) {
            return address.split("|")[1]
        },
        formatDate: function (value, args) {
            var dt = new Date(value);
            let year = dt.getFullYear();
            let month = (dt.getMonth() + 1).toString().padStart(2, '0');
            let date = dt.getDate().toString().padStart(2, '0');
            if (args === "MM/dd") {
                return `${month}/${date}`;
            }
            return `${year}-${month}-${date}`;
        },
    },
}
</script>
<style lang="scss" scoped>
.comment-main {
    .comment-item {
        border: 1px solid rgba(144, 147, 153, .31);
        border-radius: 4px;
        padding: 10px;
        margin: 10px 0 10px;
        background-color: var(--comment-backgroud-color);

        .box {
            display: flex;

            .avatar {
                line-height: normal;
                position: relative;
                vertical-align: middle;

                img {
                    width: 40px;
                    height: 40px;
                    overflow: hidden;
                    border-radius: 45%;
                }
            }

            .ml-3 {
                margin-left: 12px !important;
                width: 100%;

                .comment-input {
                    position: relative;

                    .comment-textarea {
                        font-size: .875rem;
                        color: var(--article-content-color) !important;
                        outline: none;
                        padding: 10px 5px;
                        min-height: 122px;
                        resize: none;
                        width: 100%;
                        border-radius: 4px;
                        background-color: transparent;
                        border-style: none;

                    }
                }

                .emoji-container {
                    display: flex;
                    align-items: center;
                    margin: 10px 0;

                    .emoji-btn,
                    .emoji-btn-active {
                        cursor: pointer;

                        .iconfont {
                            font-size: 1.3rem;
                            color: var(--comment-emoji-color);
                        }
                    }

                    .emoji-btn-active {
                        .iconfont {
                            color: var(--theme-color);
                        }
                    }

                    .v-comment-btn {
                        border: 1px solid var(--border-line);
                        border-radius: 4px;
                        text-align: center;
                        padding: 6px 16px;
                        font-size: 14px;
                        transition: all .3s;
                        outline: none;
                        cursor: pointer;
                    }

                    @media screen and (max-width: 767px) {
                        .upload-btn {

                            margin-left: auto;
                            color: #fff;
                            background-color: var(--theme-color);
                            position: relative;
                        }
                    }

                    @media screen and (min-width: 768px) {
                        .upload-btn {

                            margin-left: auto;
                            color: #fff;
                            background-color: var(--theme-color);
                            overflow: hidden;
                            transition: all .5s;
                            position: relative;

                            &:hover {
                                background-color: #00C853;
                            }

                            &:hover::before {
                                left: 120%;
                                transition: all .5s;
                            }

                            &::before {
                                content: '';
                                position: absolute;
                                top: 0;
                                left: -40%;
                                width: 20%;
                                height: 100%;
                                background: linear-gradient(90deg, transparent, #fff, transparent);
                                transform: skew(-45deg);
                            }
                        }
                    }

                }

                .emoji-wrapper {
                    max-height: 150px;
                    overflow-y: auto;

                    .emoji-item {
                        cursor: pointer;
                        display: inline-block;

                        .emoji {
                            font-size: 20px;
                            padding: 3px;
                            border-radius: 10px;

                            &:hover {
                                background-color: rgb(221, 221, 221)
                            }
                        }
                    }
                }
            }
        }
    }

    .commentwrap {
        width: 100%;
        padding: 0;
        list-style: none;


        .comment-wrp {
            padding: 10px 0 16px 0;

            .more-btn {
                background-color: var(--theme-color);
                border-radius: 5px;
                text-align: center;
                line-height: 30px;
                padding: 3px 0;
                height: 30px;
                width: 100px;
                margin: auto;
                cursor: pointer;
                color: #fff;
            }

            .ul-item {
                clear: both;
                margin: 0;
                padding: 0;
                overflow: hidden;
                list-style: none;
                margin-bottom: 20px;
                border-bottom: 1px solid var(--border-line);

                &:last-child {
                    border: 0;
                    padding-bottom: 0;
                }

                .comment {
                    width: 100%;
                    padding-top: 10px;
                    float: left;

                    .main {
                        float: right;
                        width: 100%;
                        padding: 0;

                        &:last-child {
                            border-bottom: 0;
                        }

                        .profile {
                            float: left;
                            margin-right: 10px;
                            margin-top: 6px;

                            a {
                                text-decoration: none;
                                color: var(--theme-color);

                                img {
                                    width: 100%;
                                    max-width: 40px;
                                    height: 40px;
                                    border-radius: 100%;
                                    box-shadow: 0 1px 10px -6px rgba(0, 0, 0, 0.5);
                                    background-color: #f5f5f5;
                                    transition: transform .75s;

                                    &:hover {
                                        transform: rotate(360deg);
                                    }
                                }

                            }
                        }

                        .commentinfo {
                            .commeta {
                                font-size: 16px;
                                margin-bottom: 5px;
                                text-transform: uppercase;
                                color: #9499a8;
                                margin-left: 50px;

                                section {
                                    display: block;
                                }

                                .left {
                                    .author {
                                        font-size: 24px;
                                        font-weight: 400;
                                        margin: 0;
                                        letter-spacing: 0px;
                                        text-transform: none;
                                        line-height: 20px;

                                        a {
                                            text-decoration: none;
                                            color: var(--theme-color);
                                            font-size: 14px;
                                            font-weight: 600;

                                            .bozhu {
                                                position: relative;
                                                top: -1px;
                                                display: inline-block;
                                                min-width: 30px;
                                                text-align: center;
                                                font-size: 12px;
                                                color: #fb7299;
                                                font-weight: 400;
                                                -webkit-transform: scale(0.9);
                                                transform: scale(0.9);
                                                border: 1px solid #fb7299;
                                                border-radius: 4px;
                                            }
                                        }
                                    }

                                }

                                .comment-reply-link {
                                    text-decoration: none;
                                    font-size: 12px;
                                    margin-left: 10px;
                                    float: right;
                                    text-transform: uppercase;
                                    color: #fff;
                                    background-color: var(--theme-color);
                                    line-height: 20px;
                                    padding: 0 6px;
                                    border-radius: 3px;
                                    opacity: 0;
                                }

                                .right {
                                    .info {
                                        margin-top: 2px;
                                        font-size: 12px;
                                        letter-spacing: 0px;
                                        text-transform: none;
                                        color: rgba(0, 0, 0, 0.35);

                                        .comment-time {
                                            display: inline-block;
                                            margin-top: 6px;
                                            font-size: 12px;
                                            color: #657786;
                                        }

                                        .useragent-info {
                                            color: #657786;

                                            svg {
                                                vertical-align: -2px;
                                                width: 13px;
                                                height: 13px;
                                            }

                                        }

                                        .ipAddress {
                                            color: #657786;
                                        }
                                    }
                                }
                            }

                            .body {
                                color: #63686d;
                                position: relative;
                                margin-bottom: 15px;
                                line-height: 1;
                                white-space: pre-line;
                                word-break: break-all;
                                font-size: 14px !important;
                                word-wrap: break-word;

                                .markdown-content {
                                    padding: 10px;
                                    white-space: pre-line;
                                    word-break: break-all;
                                    background: var(--comment-box);
                                    border-radius: 0 8px 8px;
                                }
                            }
                        }


                    }

                    .showd {
                        padding-left: 40px;
                    }
                }

                .children {
                    padding: 0;
                    list-style: none;
                    margin-left: 40px;

                    .comment-wrp {
                        padding: 10px 0 16px 0;

                        li {
                            clear: both;
                            margin: 0;
                            padding: 0;
                            overflow: hidden;
                            list-style: none;
                            margin-bottom: 20px;
                            border-bottom: 1px solid var(--border-line);

                            &:last-child {
                                border: 0;
                                margin-bottom: 0;
                            }

                            .comment {
                                width: 100%;
                                padding-top: 10px;
                                float: left;

                                .main {
                                    float: right;
                                    width: 100%;
                                    padding: 0;

                                    &:last-child {
                                        border-bottom: 0;
                                    }

                                    .profile {
                                        float: left;
                                        margin-right: 10px;
                                        margin-top: 6px;

                                        a {
                                            text-decoration: none;

                                            img {
                                                width: 100%;
                                                max-width: 40px;
                                                height: 40px;
                                                border-radius: 100%;
                                                box-shadow: 0 1px 10px -6px rgba(0, 0, 0, 0.5);
                                                background-color: #f5f5f5;
                                                transition: transform .75s;

                                                &:hover {
                                                    transform: rotate(360deg);
                                                }
                                            }

                                        }
                                    }

                                    .commentinfo {
                                        .commeta {
                                            font-size: 16px;
                                            margin-bottom: 5px;
                                            text-transform: uppercase;
                                            color: #9499a8;
                                            margin-left: 50px;

                                            section {
                                                display: block;
                                            }

                                            .left {
                                                .author {
                                                    font-size: 24px;
                                                    font-weight: 400;
                                                    margin: 0;
                                                    letter-spacing: 0px;
                                                    text-transform: none;
                                                    line-height: 20px;

                                                    a {
                                                        text-decoration: none;
                                                        color: var(--theme-color);
                                                        font-size: 14px;
                                                        font-weight: 600;

                                                        .bozhu {
                                                            position: relative;
                                                            top: -1px;
                                                            display: inline-block;
                                                            min-width: 30px;
                                                            text-align: center;
                                                            font-size: 12px;
                                                            color: #fb7299;
                                                            font-weight: 400;
                                                            -webkit-transform: scale(0.9);
                                                            transform: scale(0.9);
                                                            border: 1px solid #fb7299;
                                                            border-radius: 4px;
                                                        }
                                                    }
                                                }

                                            }

                                            .comment-reply-link {
                                                text-decoration: none;
                                                font-size: 12px;
                                                display: block;
                                                margin-left: 10px;
                                                float: right;
                                                text-transform: uppercase;
                                                color: #fff;
                                                background-color: var(--theme-color);
                                                line-height: 20px;
                                                padding: 0 6px;
                                                border-radius: 3px;
                                            }

                                            .right {
                                                .info {
                                                    margin-top: 2px;
                                                    font-size: 12px;
                                                    letter-spacing: 0px;
                                                    text-transform: none;
                                                    color: rgba(0, 0, 0, 0.35);

                                                    .comment-time {
                                                        display: inline-block;
                                                        margin-top: 6px;
                                                        font-size: 12px;
                                                        color: #657786;
                                                    }

                                                    .useragent-info {
                                                        color: #657786;

                                                        svg {
                                                            vertical-align: -2px;
                                                            width: 13px;
                                                            height: 13px;
                                                        }

                                                    }

                                                    .ipAddress {
                                                        color: #657786;
                                                    }
                                                }
                                            }
                                        }

                                        .body {
                                            color: #63686d;
                                            position: relative;
                                            margin-bottom: 15px;
                                            line-height: 1;
                                            white-space: pre-line;
                                            word-break: break-all;
                                            font-size: 14px !important;
                                            word-wrap: break-word;

                                            .markdown-content {
                                                padding: 10px;
                                                white-space: pre-line;
                                                word-break: break-all;
                                                background: var(--comment-box);
                                                border-radius: 0 8px 8px;
                                            }
                                        }
                                    }

                                }

                                .showd {
                                    padding-left: 40px;
                                }
                            }
                        }
                    }
                }
            }

        }

    }

}
</style>
