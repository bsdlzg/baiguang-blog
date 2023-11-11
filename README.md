## 博客介绍

<p align=center>
  <a href="http://www.bsdlzg.cn">
    <img src="https://bsdlzg.com/upload/2023/10/106_164.png" alt="白光博客" style="border-radius: 50%">
  </a>
</p>

<p align="center">
   <a target="_blank" href="https://github.com/bsdlzg/baiguang-blog-admin">
   <img src="https://img.shields.io/hexpm/l/plug.svg"/>
   <img src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
   <img src="https://img.shields.io/badge/springboot-2.4.1.RELEASE-green"/>
   <img src="https://img.shields.io/badge/vue-2.5.17-green"/>
   <img src="https://img.shields.io/badge/mysql-5.7.0-green"/>
   <img src="https://img.shields.io/badge/mybatis--plus-3.4.0-green"/>
   <img src="https://img.shields.io/badge/redis-6.0.5-green"/>
   <img src="https://img.shields.io/badge/elasticsearch-7.9.2-green"/>
   </a>
</p>

[项目介绍](#项目介绍) |[在线地址](#在线地址) | [目录结构](#目录结构) | [项目特点](#项目特点) | [技术介绍](#技术介绍) | [运行环境](#运行环境)

#项目介绍
一直想做一个博客系统，看了很多优秀的开源项目但是发现没有合适的。于是利用空闲休息时间开始自己写了一套博客系统。如此有了白光博客。她包含后端管理系统、博客WEB端以及后端系统；前端主要采用vue+elementui技术路线，后端主要采用springboot单体，微服务系统请移驾。所有前端后台代码封装过后十分精简易上手，出错概率低。系统主要用于分享学习，也欢迎各位同学提出意见和建议，来一起学习、提升我们的技术，同时如果您对白光博客有什么意见和建议，欢迎提issues，我也会第一时间答疑，谢谢大家的支持!!!
## 在线地址

**项目链接：** [白光博客](https://www.bsdlzg.cn)

**后台链接：** [白光博客后台管理系统](https://www.bsdlzg.cn/admin)

账号:test 密码:123456

您的star是我坚持的动力，感谢大家的支持，欢迎提交pr共同改进项目。

## 后端目录结构

```
blog
├── annotation    --  自定义注解
├── aspect        --  aop模块
├── config        --  配置模块
├── common        --  常量模块
├── controller    --  控制器模块
├── entity        --  实体类模块
├── dto           --  dto模块
├── enums         --  枚举模块
├── exception     --  自定义异常模块
├── mapper        --  框架核心模块
├── service       --  服务模块
├── strategy      --  策略模块（用于扩展第三方登录，搜索模式，上传文件模式等策略）
├── utils         --  工具类模块
├── quartz        --  定时功能模块
└── vo            --  vo模块
```
## 项目特点

- 后台参考"vue-element-template"设计，侧边栏，历史标签，面包屑自动生成。
- 采用Markdown编辑器，写法简单。
- 评论支持表情输入回复等
- 前后端分离部署，适应当前潮流。
- 接入第三方登录，减少注册成本。
- 留言采用弹幕墙，更加炫酷。
- 支持代码高亮和复制，图片预览，深色模式等功能，提升用户体验。
- 搜索文章支持高亮分词，响应速度快。
- 新增文章目录、推荐文章等功能，优化用户体验。
- 新增aop注解实现日志管理
- 支持动态权限修改，采用RBAC模型，前端菜单和后台权限实时更新。
- 后台管理支持修改背景图片，博客配置等信息，操作简单，支持上传相册。
- 代码支持多种搜索模式（Elasticsearch或MYSQL），可支持配置。
- 代码支持多种文件上传模式（七牛云oss或本地上传），可支持配置。
- 代码遵循阿里巴巴开发规范，利于开发者学习。


## 技术介绍

**前端：** vue + vuex + vue-router + axios + vuetify + element + echarts

**后端：** SpringBoot + nginx + docker + sa-token + Swagger2 + MyBatisPlus + Mysql + Redis + elasticsearch

**其他：** 接入QQ、微博、码云第三方登录，接入七牛云对象存储

## 运行环境

**服务器：** linux 2核4G CentOS7.6

**最低配置：** 1核2G服务器（关闭ElasticSearch）

## 开发环境


| 开发工具                          | 说明               |
| ----------------------------- | ---------------- |
| IDEA                          | Java开发、前端工具IDE      |
| Navicat                       | MySQL远程连接工具      |
| QuickRedis | Redis远程连接工具      |
| xShell                    | Linux远程连接和文件上传工具 |

| 开发环境          | 版本    |
| ------------- | ----- |
| JDK           | 1.8   |
| MySQL         | 5.7.0 |
| Redis         | 6.0.5 |
| Elasticsearch | 7.9.2 |
