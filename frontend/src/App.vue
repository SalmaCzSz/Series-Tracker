<template>
  <Navbar v-if="mostrarTemplates" @abrirPerfil="mostrarModal = true"/>
  <router-view />

  <!-- Modal Usuario -->
  <Modal v-if="mostrarModal" @close="mostrarModal = false">
    <template #titulo>
      <h3 class="h4 text-gray-900 mb-4">¡Hola, {{ correoUsuario }}!</h3>
    </Template>
    <template #contenido>
      <form class="user" id="form-editar-usuario">
        <div class="form-group row">
          <div class="col-12 col-sm-6 mb-3 mb-sm-0">
            <input v-model="nombre" placeholder="Nombre" required />
          </div>
          <div class="col-sm-6 col-12">
            <input v-model="apellido" placeholder="Apellido" required />
          </div>
        </div>
        <div class="form-group">
          <input v-model="correo" type="email" placeholder="Correo" required disabled/>
        </div>
        <div class="form-group row">
          <div class="col-12 col-sm-6 mb-3 mb-sm-0">
            <input v-model="password" type="password" placeholder="Password" required />
          </div>
          <div class="col-sm-6 col-12">
            <input v-model="repetirPassword" type="password" placeholder="Repetir password" required />
          </div>
        </div>
      </form>
    </template>
    <template #botones>
      <div class="botones-footer">
        <button type="submit" class="btn-eliminar">ELIMINAR</button>
        <button type="submit" class="btn-actualizar" form="form-editar-usuario">ACTUALIZAR</button>
      </div>
    </template>
  </Modal>

  <Footer v-if="mostrarTemplates"/>
</template>

<script setup>
  import { useRoute } from 'vue-router'
  import { ref, computed, onMounted, watch } from 'vue'
  import Navbar from './components/Navbar.vue'
  import Footer from './components/Footer.vue'
  import Modal from './components/Modal.vue'

  // mostrar navbar y footer
  const route = useRoute()

  const mostrarTemplates = computed(() => {
    return route?.path && !['/', '/registrarUsuario'].includes(route.path)
  })

  // control del modal
  const mostrarModal = ref(false)

  // mostrar usuario en saludo
  const correoUsuario = ref('Usuario')
  onMounted(() => {
    const correoUser = localStorage.getItem('correo');
    let usuario = correoUser.split("@");

    if(correoUser){
      correoUsuario.value = usuario[0];
    }
  });

  // cargar formulario
  const nombre = ref('')
  const apellido = ref('')
  const correo = ref('')
  const password = ref('')
  const repetirPassword = ref('')

  const cargarDatos = async () => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');

    try{
      const response = await fetch(`http://localhost:8080/api/usuarios/${userId}`, {
        headers: { 'Authorization': 'Bearer ' + token }
      });

      if (!response.ok) {
        throw new Error("No se pudo obtener la información del usuario")
      }

      const data = await response.json()

      nombre.value = data.nombre
      apellido.value = data.apellido
      correo.value = data.correo
      password.value = ''
      repetirPassword.value = ''
      correoUsuario.value = data.correo.split('@')[0]
    }catch(e){
      console.error("Error cargando usuario:", error)
    }
  }
  
  watch(mostrarModal, (abierto) => {
    if (abierto) {
      cargarDatos()
    }
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
 .formulario {
   flex: 1;
   background-color: var(--color-primario);
   padding: 2rem;
   display: flex;
   flex-direction: column;
   justify-content: center;
 }

 input {
   width: 100%;
   padding: 0.5rem;
   margin-top: 0.75rem;
   border: none;
   border-radius: 20px;
   outline: none;
   background-color: var(--color-fondo);
 }

 button {
   margin: 0.5rem 0;
   padding: 0.5rem;
   width: 100%;
   border-radius: 20px;
   border: none;
   font-weight: bold;
   cursor: pointer;
   min-width: 0;
   box-sizing: border-box;
 }

 .botones-footer {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  justify-content: center;
  align-items: center;
  margin-top: 1rem;
 }

 .botones-footer button{
  width: 95%;
  max-width: 200px;
  margin: 0 auto;
 }

 .btn-eliminar{
  background-color: var(--color-error);
 }

 .btn-actualizar{
  background-color: var(--color-secundario);
  margin-right: 0 !important;
 }
 
 @media (min-width: 768px) {
  .botones-footer {
    flex-direction: row;
    justify-content: flex-end;
    gap: 1rem;
  }
  .botones-footer button { 
    min-width: 120px;
    width: auto;
    margin: 0;
  }
}
</style>
