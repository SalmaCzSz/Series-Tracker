import { createRouter, createWebHistory } from 'vue-router';
import Inicio from '../views/Inicio.vue';
import Login from '../views/Login.vue'
import RegistrarUsuario from '../views/RegistrarUsuario.vue'
import RegistrarSerie from '../views/RegistrarSerie.vue';
import ListarSeries from '../views/ListarSeries.vue';
import NotFound from '../views/NotFound.vue';

const routes = [
  { path: '/', component: Login },
  { path: '/registrarUsuario', component: RegistrarUsuario },
  { path: '/inicio', component: Inicio, meta: { requiresAuth: true } },
  { path: '/registrarSerie', component: RegistrarSerie, meta: { requiresAuth: true } },
  { path: '/listarSeries', component: ListarSeries, meta: { requiresAuth: true } },
  { path: '/:pathMatch(.*)*', component: NotFound, name: 'NotFound', meta: { requiresAuth: true } }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.meta.requiresAuth && !token) {
    next('/');  
  } else {
    next();  
  }
});

export default router;
