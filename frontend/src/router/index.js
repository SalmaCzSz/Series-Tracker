import { createRouter, createWebHistory } from 'vue-router';
import Inicio from '../views/Inicio.vue';
import Login from '../views/Login.vue'
import RegistrarUsuario from '../views/RegistrarUsuario.vue'

const routes = [
  { path: '/', component: Login },
  { path: '/inicio', component: Inicio },
  { path: '/registrarUsuario', component: RegistrarUsuario }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
