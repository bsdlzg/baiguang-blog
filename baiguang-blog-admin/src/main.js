import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en' // lang i18n

// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar'
import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

// Vue markdown editor
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

import VueParticles from 'vue-particles'
// 标签云
import tagCloud from '@/utils/tag-cloud'
// 基于vue的代码编辑器插件
import codeEditor from 'bin-code-editor'

Vue.use(tagCloud)
// RightToolbar注册为全局组件
Vue.component('RightToolbar',RightToolbar)
Vue.use(mavonEditor)
Vue.use(VueParticles)
Vue.use(codeEditor)


/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

// set ElementUI lang to EN
// Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
