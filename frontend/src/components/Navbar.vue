<template>
  <nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/inicio">Series Tracker</router-link>

      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarContent">
        <ul class="navbar-nav ml-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link class="navbar-brand" to="/">+ Serie</router-link>
          </li>
          <li class="nav-item">
            <router-link class="navbar-brand" to="/">Ver todo</router-link>
          </li>
          <li class="nav-item d-none d-lg-block mx-3">
            <div class="topbar-divider"></div>
          </li>
          <li class="nav-item dropdown">
            <a class="navbar-brand dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"> {{ correoUsuario }} </a>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><a class="dropdown-item" href="#" @click.prevent="$emit('abrirPerfil')">Perfil</a></li>
              <li><a class="dropdown-item" href="#" @click="cerrarSesion">Salir</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';

  const correoUsuario = ref('Usuario')
  const router = useRouter()

  onMounted(() => {
    const correo = localStorage.getItem('correo');

    if(correo){
      correoUsuario.value = correo;
    }
  });

  defineEmits(['abrirPerfil'])

  const cerrarSesion = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('correo');
    localStorage.removeItem('userId');

    router.push('/');
  };
</script>

<style scoped>
 .navbar{
   background-color: var(--color-primario);
   font-family: 'Quicksand';
   position: relative;
 }

 .navbar-brand{
   color: black;
   font-weight: lighter;
 }

 .navbar-brand:hover {
   color: var(--color-exito) !important;
 }

 .navbar-toggler-icon {
   background-image: url("data:image/svg+xml;charset=utf8,%3Csvg viewBox='0 0 30 30' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath stroke='black' stroke-width='2' stroke-linecap='round' stroke-miterlimit='10' d='M4 7h22M4 15h22M4 23h22'/%3E%3C/svg%3E");
 }

 .topbar-divider {
   width: 1px;
   height: 30px;
   background-color: black;
   align-self: center;
 }

 .dropdown-menu {
   padding: 0.5rem 0.75rem;
   margin-top: 10px;
   left: auto;
   right: 0;
   margin-top: 0.5rem;
 }
</style>