import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LoginPage from "../views/LoginPage.vue";
import CoursePage from "../views/CoursePage.vue";
import CourseIntro from "../views/CourseInfo/CourseIntro.vue";
import CourseNotice from "../views/CourseInfo/CourseNotice.vue";
import CourseOutline from "../views/CourseInfo/CourseOutline.vue";
import TeacherInfo from "../views/CourseInfo/TeacherInfo.vue";
import TeachingCalendar from "../views/CourseInfo/TeachingCalendar.vue";
import Courseware from "../views/CourseResource/Courseware.vue";
import VideoReplay from "../views/CourseVideo/VideoReplay.vue";
import CourseReport from "../views/CourseExam/CourseReport.vue";
import Experiment from "../views/CourseExam/Experiment.vue";
import FinalAssessment from "../views/CourseExam/FinalAssessment.vue";
import HomeWork from "../views/CourseExam/HomeWork.vue";
import RegularTest from "../views/CourseExam/RegularTest.vue";
import DiscussArea from "../views/CourseDiscuss/DiscussArea.vue";
import HomeItems from "../views/HomeItem/HomeItems.vue";
import UserInfo from "../views/UserInfo/UserInfo.vue";

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: LoginPage
    },
    {
        path: '/',
        name: 'Home',
        component: Home,
        redirect: '/home',
        children: [
            {
                path: 'home', name: 'HomeItems', component: HomeItems,
            },
            {
                path: 'userInfo', name: 'UserInfo', component: UserInfo,
            },
        ]
    },
    {
        path: '/course/courseId=:id&courseNumber=:courseNumber',
        name: 'CoursePage',
        component: CoursePage,
        redirect: to => {
            return `/course/courseId=${to.params.id}&courseNumber=${to.params.courseNumber}/courseIntro`;
        },
        props: true, // 是为了传上面的path: '/course/:id'的id
        children: [
            {
                path: 'courseIntro', name: 'CourseIntro', component: CourseIntro,
            },
            {
                path: 'courseNotice', name: 'CourseNotice', component: CourseNotice,
            },
            {
                path: 'courseOutline', name: 'CourseOutline', component: CourseOutline,
            },
            {
                path: 'teacherInfo', name: 'TeacherInfo', component: TeacherInfo,
            },
            {
                path: 'teachingCalendar', name: 'TeachingCalendar', component: TeachingCalendar,
            },
            {
                path: 'courseware', name: 'Courseware', component: Courseware,
            },
            {
                path: 'videoReplay', name: 'VideoReplay', component: VideoReplay,
            },
            {
                path: 'courseReport', name: 'CourseReport', component: CourseReport,
            },
            {
                path: 'experiment', name: 'Experiment', component: Experiment,
            },
            {
                path: 'finalAssessment', name: 'FinalAssessment', component: FinalAssessment,
            },
            {
                path: 'homeWork', name: 'HomeWork', component: HomeWork,
            },
            {
                path: 'regularTest', name: 'RegularTest', component: RegularTest,
            },
            {
                path: 'discussArea', name: 'DiscussArea', component: DiscussArea,
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

export default router
