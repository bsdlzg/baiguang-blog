import Vue from 'vue'
import App from './App.vue'

// 引入qq、微博个人信息
import config from "@/assets/js/config";
// 引入iconfont
import './assets/font/iconfont.css'
import './assets/font/iconfont.js'

import 'wowjs/css/libs/animate.css'
import wow from 'wowjs'

// 引入md编辑器
import VMdPreview from '@kangc/v-md-editor/lib/preview'
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';

import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

// 引入icon图标
import "../src/icons";

// 一个轻量级、可扩展的语法高亮显示工具
import Prism  from 'prismjs'
// 剪贴板
import Clipboard from 'clipboard'
// 弹出组件
import { vueBaberrage } from 'vue-baberrage'
import { setSkin } from '@/utils/skin'
import jsCookie from 'js-cookie'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import VueImageSwipe from 'vue-image-swipe'
import 'vue-image-swipe/dist/vue-image-swipe.css'

// vue 原型配置
Vue.prototype.Clipboard = Clipboard
Vue.prototype.$wow = wow
Vue.prototype.config = config;
Vue.config.productionTip = false
Vue.prototype.$cookie = jsCookie;  // 在页面里可直接用 this.$cookie 调用
Vue.prototype.$setSkin = setSkin;

// vue插件配置
// 设置md为插件
VueMarkdownEditor.use(vuepressTheme, {
  Prism,
});

Vue.use(VueMarkdownEditor);
VMdPreview.use(vuepressTheme, {
  Prism
});
VMdPreview.use(createLineNumbertPlugin())
VMdPreview.use(createCopyCodePlugin())
Vue.use(VMdPreview)
Vue.use(vueBaberrage)
Vue.use(VueImageSwipe)
Vue.use(ElementUI)

import router from './router'
import store from './store'



new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app')


