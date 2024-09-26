import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import elementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import  { createPinia } from 'pinia'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(elementPlus)
app.mount('#app')