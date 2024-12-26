import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProfileView from '../views/ProfileView.vue'
import EcommerceView from '../views/EcommerceView.vue'
import UsersView from '../views/UsersView.vue'
import OrdersView from '../views/OrdersView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,

    },
    {
      path: '/ecommerce',
      name: 'ecommerce',
      component: EcommerceView,

    },
    {
      path: '/users',
      name: 'users',
      component: UsersView,

    },
    {
      path: '/orders',
      name: 'orders',
      component: OrdersView,

    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    }
  ]
})

export default router
