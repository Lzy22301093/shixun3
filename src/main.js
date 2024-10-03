import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import elementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import  { createPinia } from 'pinia'
import persist from 'pinia-plugin-persistedstate'

const app = createApp(App)

const pinia = createPinia()
pinia.use(persist)

app.use(pinia)
app.use(router)
app.use(elementPlus)
app.mount('#app')