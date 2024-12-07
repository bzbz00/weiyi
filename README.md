# 微医 App - - 期末课设

## 项目文档
http://weiyi.zhangang.top/

## 项目简介
微医是一个面向医疗领域的Android应用，旨在打造专业的医学交流论坛和便捷的医学助手平台。该应用为医生和用户提供了一个专业的交流分享平台，同时集成了AI助手等智能功能。

## 主要功能

### 1. 用户系统
- 登录/注册功能
- 指纹认证登录
- 个人资料管理
- 用户身份认证(医生/普通用户)

### 2. 论坛交流
- 帖子发布与管理
- 分类导航(故事、热点、推荐、关注)
- 评论互动系统
- 点赞和分享功能
- 实时消息通知

### 3. AI助手
- 悬浮窗服务
- 智能问答系统
- 多模型支持(GPT-3.5/GPT-4/GPT-4-32K)
- 实时对话功能

### 4. 个人中心
- 订单管理
- 医生管理
- 账户管理
- 工具与服务
- 就诊记录

## 技术特点

### 1. 架构设计
- 基于Fragment + Activity的架构
- EventBus消息总线
- 单例模式管理全局状态
- MVP架构模式

### 2. UI设计
- Material Design设计规范
- 响应式布局
- 自定义动画效果
- 沉浸式体验

### 3. 核心技术
- EventBus事件处理
- OkHttp网络请求
- Gson数据解析
- 生物识别认证

## 开发环境
- Android Studio 最新版
- JDK 8+
- Android SDK API 31 (Android 12)
- Gradle 7.0+

## 项目结构
app/
├── activity/ // 活动类
├── adapter/ // 适配器类
├── event/ // 事件类
├── fragment/ // 片段类
├── manager/ // 管理器类
├── method/ // 工具类
├── model/ // 数据模型类
├── service/ // 服务类
└── res/ // 资源文件

## 依赖说明
- androidx.appcompat:appcompat
- com.google.android.material:material
- androidx.constraintlayout:constraintlayout
- com.android.volley:volley
- androidx.biometric:biometric
- com.squareup.okhttp3:okhttp
- com.google.code.gson:gson
- org.greenrobot:eventbus

## 安装说明
1. 克隆项目到本地
2. 在Android Studio中打开项目
3. 同步Gradle文件
4. 运行项目

## 权限要求
- 网络访问权限
- 悬浮窗权限
- 指纹识别权限
- 网络状态权限

## 未来规划
1. 完善AI助手功能
2. 增加医疗知识库
3. 添加在线问诊功能
4. 优化用户体验
5. 增加社交功能
6. 加强数据安全性

## 贡献指南
欢迎提交Issue和Pull Request，一起完善项目。

## 联系方式
- 项目维护者：lhp，ZhangAng,LvLinYu,KongWeiShu
- 邮箱：2816077796@qq.com
