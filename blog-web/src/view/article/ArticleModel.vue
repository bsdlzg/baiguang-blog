<template>
    <el-dialog :fullscreen="true" title="发表文章" :visible.sync="drawer" :before-close="handleClose">
        <el-form :model="article" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-form-item label="文章标题" prop="title">
                        <el-input v-model="article.title"></el-input>
                    </el-form-item>
                    <el-form-item label="文章简介" prop="summary">
                        <el-input v-model="article.summary"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="封面图" prop="">
                        <el-upload class="avatar-uploader" :show-file-list="false" ref="upload" name="filedatas"
                            :action="uploadPictureHost" :before-upload="uploadBefore" :http-request="uploadSectionFile"
                            multiple>
                            <img v-if="article.avatar" style="width: 100%;height: 100%;" :src="article.avatar"
                                class="imgAvatar" />
                            <i v-else class="el-icon-plus avatar-img-icon"></i>
                        </el-upload>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-form-item label="文章标签" prop="tagList">
                        <el-select v-model="article.tagList" :multiple-limit="3" multiple placeholder="请选择标签">
                            <el-option v-for="item in $store.state.tagCloud" :key="item.id" :label="item.name"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="文章分类" prop="categoryId">
                        <el-select v-model="article.categoryId" placeholder="请选择分类">
                            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="创作类型" prop="isOriginal">
                        <el-radio v-model="article.isOriginal" border :label="1">原创</el-radio>
                        <el-radio v-model="article.isOriginal" border :label="0">转载</el-radio>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20" v-if="article.isOriginal == 0">
                <el-col :span="8">
                    <el-form-item label="文章地址" prop="originalUrl">
                        <el-input v-model="article.originalUrl"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <v-md-editor v-model="article.contentMd" @upload-image="handleUploadImage" :disabled-menus="[]"></v-md-editor>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="submit">提交审核</el-button>
        </span>
    </el-dialog>
</template>
<script>
import { upload, featchCategory, insertArticle, getMyArticleInfo } from '@/api'
export default {
    name: 'ArticleModel',
    data() {
        return {
            uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
            article: {},
            categoryList: [],
            // 加载层信息
            loading: [],
            rules: {
                title: [
                    { required: true, message: '请输入文章名称', trigger: 'blur' },
                ],
                avatar: [
                    { required: true, message: '请上传文章封面', trigger: 'change' },
                ],
                summary: [
                    { required: true, message: '请输入文章简介', trigger: 'blur' },
                ],
                tagList: [
                    { required: true, message: '请选择文章标签', trigger: 'blur' },
                ],
                categoryId: [
                    { required: true, message: '请选择文章分类', trigger: 'blur' },
                ],
                isOriginal: [
                    { required: true, message: '请选择创作类型', trigger: 'blur' },
                ],
                originalUrl: [
                    { required: true, message: '请输入转载地址', trigger: 'blur' },
                ],
            }
        };
    },
    computed: {
        drawer: {
            set(value) {
                this.$store.state.articleDrawer.flag = value;
            },
            get() {
                if (this.$store.state.articleDrawer.flag) {
                    this.article = {
                        isOriginal: 1,
                        avatar: null,
                    }
                    featchCategory().then(res => {
                        this.categoryList = res.data
                    })
                    if (this.$store.state.articleDrawer.id) {
                        this.openLoading()
                        getMyArticleInfo(this.$store.state.articleDrawer.id).then(res => {
                            this.article = res.data
                        })
                        this.loading.close(0)
                    }
                }
                return this.$store.state.articleDrawer.flag;
            }
        },
    },
    methods: {
        submit() {
            this.$refs['ruleForm'].validate((valid) => {
                if (valid) {
                    if (!this.article.contentMd) {
                        this.$message.error("请编写文章内容！")
                        return;
                    }
                    insertArticle(this.article).then(res => {
                        this.$store.state.articleDrawer.flag = false
                        this.$message.success("发表成功")
                    }).catch(err => {
                        this.$message.error(err.message)
                    })
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        handleClose() {
            this.$confirm('确认关闭吗？编写的文章不会保存哦')
                .then(_ => {
                    this.$store.state.articleDrawer.flag = false
                })
                .catch(_ => {
                    this.$message.info("取消关闭")
                });
        },
        handleUploadImage(event, insertImage, files) {
            // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
            this.openLoading()
            const file = files[0]
            // FormData 对象
            var formData = new FormData()
            // 文件对象
            formData.append('multipartFile', file)
            upload(formData).then(res => {
                insertImage({
                    url: res.data,
                    desc: file.name,
                    width: '100%',
                    height: '300px',
                });
            })
            this.loading.close()
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
                this.article.avatar = res.data
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
    }
};
</script>
<style lang="scss" scoped>
/deep/ .avatar-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 200px;
    height: 100px;
    text-align: center;
}

/deep/ .el-upload {
    width: 100%;
    height: 100%;
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
</style>
