import request from '@/utils/request'

// 访问日志记录接口
export function report() {
    return request({
        url: '/v1/report',
        method: 'get',
    })
}

// 系统登出接口
export function logout() {
    return request({
        url: '/logout',
        method: 'get'
    })
}

// 获取首页数据
export function featchHomeData() {
    return request({
        url: '/v1/',
        method: 'get',
    })
}

// 获取文章list数据
export function fetchArticleList(params) {
    return request({
        url: '/v1/article/list',
        method: 'get',
        params: params
    })
}

// 获取分类
export function featchCategory() {
    return request({
        url: '/v1/category/list',
        method: 'get'
    })
}

// 获取网站信息
export function getWebSiteInfo() {
    return request({
        url: '/v1/webSiteInfo',
        method: 'get',
    })
}

// 获取归档
export function archive() {
    return request({
        url: '/v1/article/archive',
        method: 'get',
    })
}

// 标签接口
export function fetchTagList() {
    return request({
        url: '/v1/tag/',
        method: 'get',

    })
}

// 登录相关
export function emailLogin(data) {
    return request({
        url: '/oauth/emailLogin',
        method: 'post',
        data
    })
}

export function getWecahtLoginQr() {
    return request({
        url: '/oauth/wxQr',
        method: 'get'
    })
}

export function wxIsLogin(id) {
    return request({
        url: '/oauth/wx/is_login',
        method: 'get',
        params: {
            tempUserId: id
        }
    })
}

export function openAuthUrl(source) {
    return request({
        url: '/oauth/render/' + source,
        method: 'get'
    })
}

// 根据token获取用户信息
export function selectUserInfoByToken(token) {
    return request({
        url: '/v1/user/selectUserInfoByToken',
        method: 'get',
        params: {
            token: token
        }
    })
}

// 留言信息接口
export function listMessage() {
    return request({
        url: '/v1/message/list',
        method: 'get',
    })
}
export function addMessage(data) {
    return request({
        url: '/v1/message/',
        method: 'post',
        data
    })
}

// 友链接口
export function featchLinks() {
    return request({
        url: '/v1/link/selectLinkList',
        method: 'get',
    })
}
export function addLink(data) {
    return request({
        url: '/v1/link/',
        method: 'post',
        data
    })
}

export function articleInfo(id) {
    return request({
        url: '/v1/article/info',
        method: 'get',
        params: {
            id: id
        }
    })
}

export function articleLike(id) {
    return request({
        url: '/v1/article/like',
        method: 'get',
        params: {
            articleId: id
        }
    })
}

export function featchComments(params) {
    return request({
        url: '/v1/comment/selectCommentByArticleId',
        method: 'get',
        params: params
    })
}

export function postComment(data) {
    return request({
        url: '/v1/comment/',
        method: 'post',
        data
    })
}

export function upload(data) {
    return request({
        url: '/file/upload',
        method: 'POST',
        headers: { 'Content-Type': 'multipart/articles-data' },
        data
    })
}

export function insertArticle(data) {
    return request({
        url: '/v1/article/insert',
        method: 'post',
        data
    })
}

export function getMyArticleInfo(id) {
    return request({
        url: '/v1/article/publicSelectMyArticleInfo',
        method: 'get',
        params: {
            id: id
        }
    })
}

export function searchArticle(params) {
    return request({
        url: '/v1/article/search',
        method: 'get',
        params: params
    })
}

export function updateUserInfo(data) {
    return request({
        url: '/v1/user/',
        method: 'put',
        data
    })
}
export function getUserInfo() {
    return request({
        url: '/v1/user/info',
        method: 'get'
    })
}

export function updatePassword(data) {
    return request({
        url: '/system/user/updatePassword',
        method: 'post',
        data
    })
}

export function getMyArticle(params) {
    return request({
        url: '/v1/article/publicSelectMyArticle',
        method: 'get',
        params: params
    })
}

export function deleteMyArticle(id) {
    return request({
        url: '/v1/article/publicDeleteMyArticle',
        method: 'delete',
        params: {
            id: id
        }
    })
}

// 聊天室接口
export function getImHistory(params) {
    return request({
        url: '/v1/im/history',
        method: 'get',
        params: params
    })
}
export function getUserImHistoryList(params) {
    return request({
        url: '/v1/im/userImHistory',
        method: 'get',
        params: params
    })
}
