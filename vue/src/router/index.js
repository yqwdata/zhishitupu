import {createRouter, createWebHistory} from 'vue-router'
import Layout from "@/layout/Layout.vue";


const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: '/login',
        children: [
            {
                path: 'user',
                name: 'User',
                component: () => import("@/views/UserView.vue")
            },
            {
                path: 'person',
                name: 'Person',
                component: () => import("@/views/PersonView.vue")
            },
            {
                path: 'todo',
                name: 'ToDo',
                component: () => import("@/views/ToDo.vue")
            },
            {
                path: 'chat',
                name: 'Chat',
                component: () => import("@/views/ChatView.vue")
            },
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/LoginView.vue")
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import("@/views/RegisterView.vue")
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router