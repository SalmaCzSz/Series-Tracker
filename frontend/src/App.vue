<template>
  <Navbar v-if="mostrarTemplates"/>
  <router-view />
  <Footer v-if="mostrarTemplates"/>
</template>

<script setup>
  import { useRoute } from 'vue-router'
  import { computed } from 'vue'
  import Navbar from './components/Navbar.vue'
  import Footer from './components/Footer.vue'

  const route = useRoute()

  const mostrarTemplates = computed(() => {
    return route?.path && !['/', '/registrarUsuario'].includes(route.path)
  })
</script>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
