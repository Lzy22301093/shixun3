import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import elementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import  { createPinia } from 'pinia'

router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title;
    } else {
        document.title = 'Smart Course'; // 如果没有设置 meta.title，使用默认标题
    }
    next();
});

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(elementPlus)
app.mount('#app')