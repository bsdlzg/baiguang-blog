<template>
    <div class="userInfo-main">
        <el-drawer :append-to-body="true" title="我是标题" :with-header="false" :visible.sync="drawer" direction="rtl">
            <el-tabs v-model="activeName" tab-position="left" type="border-card" @tab-click="handeClike">
                <el-tab-pane name="user">
                    <span slot="label"><i class="el-icon-user-solid"></i> 个人中心</span>
                    <el-form label-position="left" label-width="60px" :model="form">
                        <el-form-item label="昵称：">
                            <el-upload class="avatar-uploader" :show-file-list="false" ref="upload" name="filedatas"
                                :action="uploadPictureHost" :before-upload="uploadBefore" :http-request="uploadSectionFile"
                                multiple>
                                <img v-if="form.avatar" style="width: 100%;height: 100%;" :src="form.avatar"
                                    class="imgAvatar">
                                <i v-else class="el-icon-plus avatar-img-icon"></i>
                            </el-upload>
                        </el-form-item>
                        <el-form-item label="昵称：">
                            <el-input v-model="form.nickname"></el-input>
                        </el-form-item>
                        <el-form-item label="简介：">
                            <el-input v-model="form.intro"></el-input>
                        </el-form-item>
                        <el-form-item label="站点：">
                            <el-input v-model="form.webSite"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱">
                            <el-input v-model="form.email"></el-input>
                        </el-form-item>
                    </el-form>
                    <el-button type="primary" @click="update" round>提交</el-button>
                </el-tab-pane>
                <!-- 修改密码 -->
                <el-tab-pane name="password">
                    <span slot="label"><i class="el-icon-unlock"></i> 修改密码</span>
                    <el-collapse value="1">
                        <el-collapse-item title="修改密码须知" name="1">
                            <div>此修改密码功能仅适用于账号和密码登录</div>
                            <div>对于第三方登录的账号，无法进行密码修改</div>
                        </el-collapse-item>
                    </el-collapse>
                    <el-form style="margin-top: 5px;" :rules="rules" ref="ruleForm" label-position="left"
                        label-width="100px" :model="form">
                        <el-form-item label="旧密码" prop="oldPassword">
                            <el-input v-model="form.oldPassword"></el-input>
                        </el-form-item>
                        <el-form-item label="新密码" prop="newPassword">
                            <el-input v-model="form.newPassword"></el-input>
                        </el-form-item>
                        <el-form-item label="确认密码" prop="new2Password">
                            <el-input v-model="form.new2Password"></el-input>
                        </el-form-item>
                    </el-form>
                    <el-button type="primary" @click="updatePassword" round>提交</el-button>
                </el-tab-pane>

                <!-- 我的文章 -->
                <el-tab-pane name="article">
                    <span slot="label"><i class="el-icon-tickets"></i> 我的文章</span>
                    <el-timeline v-if="articleList.length" style="overflow: scroll;height: 100%;">
                        <el-timeline-item :timestamp="item.createTime" placement="top" v-for="(item, index) in articleList"
                            :key="index">
                            <el-card class="myArticle">
                                <h4 @click="goArticleInfo(item.id)">{{ item.title }}</h4>
                                <div class="box">
                                    <div class="statu">
                                        <el-tag :type="statuTagStyle[item.isPublish]" size="small">{{
                                            statuTag[item.isPublish] }}</el-tag>
                                    </div>
                                    <div class="btn">
                                        <el-tooltip class="item" effect="dark" content="修改" placement="top-start">
                                            <el-button size="mini" @click="handleUpdateArticle(item.id)" type="primary"
                                                icon="el-icon-edit" circle></el-button>
                                        </el-tooltip>
                                        <el-tooltip class="item" effect="dark" content="删除" placement="top-start">
                                            <el-button size="mini" type="danger" icon="el-icon-delete"
                                                @click="deleteArticle(item.id)" circle></el-button>
                                        </el-tooltip>
                                    </div>
                                </div>
                            </el-card>
                        </el-timeline-item>
                        <el-pagination background style="text-align: center;" layout="prev, pager, next" :total="pageTotal"
                            @current-change="handlePage">
                        </el-pagination>
                    </el-timeline>

                    <el-empty v-else description="暂未发表任何文章"></el-empty>
                </el-tab-pane>
            </el-tabs>
        </el-drawer>
    </div>
</template>
<script>
import { updateUserInfo, getUserInfo, upload, updatePassword, getMyArticle, deleteMyArticle } from '@/api'
export default {
    data() {
        return {
            form: {},
            uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
            // 加载层信息
            loading: [],
            activeName: 'user',
            statuTag: ['下架', '发布', '待审批'],
            statuTagStyle: ['danger', 'success', 'info'],
            files: {},
            articleList: [],
            pageData: {
                pageNo: 1,
                pageSize: 5
            },
            pageTotal: 0,
            rules: {
                oldPassword: [
                    { required: true, message: '请输入旧密码', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
                ],
                newPassword: [
                    { required: true, message: '请输入新密码', trigger: 'blur' }
                ],
                new2Password: [
                    { required: true, message: '请输入确认密码', trigger: 'blur' }
                ],
            }
        };
    },
    computed: {
        drawer: {
            set(value) {
                this.$store.state.userInfoDrawer = value;
            },
            get() {
                if (this.$store.state.userInfoDrawer) {
                    this.activeName = 'user',
                        getUserInfo().then(res => {
                            this.form = res.data
                        })
                }
                return this.$store.state.userInfoDrawer;
            }
        },
    },
    methods: {
        goArticleInfo(id) {
            this.$router.push({ path: '/articleInfo', query: { articleId: id } })
        },
        handleUpdateArticle(id) {
            this.$store.state.userInfoDrawer = false;
            this.$store.state.articleDrawer.flag = true;
            this.$store.state.articleDrawer.id = id;

        },
        deleteArticle(id) {
            this.$confirm('确认删除吗？')
                .then(_ => {
                    deleteMyArticle(id).then(res => {
                        this.selectMyArticleList()
                        this.$message.success("删除成功")
                    }).catch(err => {
                        this.$message.error(err.message)
                    })
                })
                .catch(_ => {
                    this.$message.info("取消关闭")
                });

        },
        updatePassword() {
            this.$refs['ruleForm'].validate((valid) => {
                if (valid) {
                    if (this.form.newPassword != this.form.new2Password) {
                        this.$message.error("确认密码和新密码不一致")
                        return;
                    }
                    updatePassword(this.form).then(res => {
                        this.$message({
                            message: "修改成功",
                            type: 'success'
                        });
                    }).catch(err => {
                        this.$message.error(err.message)
                    })
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });

        },
        uploadBefore: function () {
            this.openLoading()
        },
        uploadSectionFile: function (param) {
            this.files = param.file
            // FormData 对象
            var formData = new FormData()
            // 文件对象
            formData.append('multipartFile', this.files)
            upload(formData).then(res => {
                this.form.avatar = res.data
            })
            this.loading.close()
        },
        // 打开加载层
        openLoading: function () {
            this.loading = this.$loading({
                lock: true,
                text: "正在加载中~",
                spinner: "el-icon-loading",
                background: "rgba(0, 0, 0, 0.7)"
            });
        },
        update() {
            updateUserInfo(this.form).then(res => {
                this.$message({
                    message: "修改成功",
                    type: 'success'
                });
              this.$store.state.userInfoDrawer = false
            }).catch(err => {
                this.$message.error(err.message);
            })
        },
        handlePage(e) {
            this.pageData.pageNo = e
            this.selectMyArticleList()
        },
        selectMyArticleList() {
            this.openLoading()
            getMyArticle(this.pageData).then(res => {
                this.articleList = res.data.records;
                this.pageTotal = res.data.total
            }).catch(err => {
                console.log(err)
            })
            this.loading.close()
        },
        handeClike(event) {
            console.log(event)
            const index = event.paneName
            // 修改密码
            if (index == "password") {
            }
            if (index == "article") {
                this.selectMyArticleList()
            }
        },
    }
};
</script>
<style lang="scss" scoped>
/deep/ .el-tabs {
    height: -webkit-fill-available;
}

/deep/ .el-button {
    margin: 0 auto;
    display: block;
}

/deep/ .el-upload {
    width: 100%;
    height: 100%;
}

/deep/ .avatar-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100px;
    height: 100px;
    text-align: center;
}

/deep/ .avatar-uploader:hover {
    border-color: #409EFF;
}

/deep/ .el-icon-plus {
    font-size: 28px;
    color: #8c939d;
    line-height: 100px;
}

/deep/ .avatar {
    width: 178px;
    height: 178px;
    display: block;
}

/deep/ .el-tabs__item {
    height: 50px;
    line-height: 50px;
    font-size: 1rem;
}

.myArticle {
    h4 {
        cursor: pointer;
    }

    .box {
        height: 40px;
        line-height: 40px;
        position: relative;
        margin-top: 20px;

        .statu {
            display: inline-block;
        }

        .btn {
            float: right;

            /deep/ .el-button {

                margin-left: 10px;
                display: inline-block;
            }
        }
    }


}
</style>
