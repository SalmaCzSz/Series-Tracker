<template>
  <Navbar v-if="mostrarTemplates" @abrirPerfil="mostrarModal = true"/>
  <router-view />

  <!-- Modal Usuario -->
  <Modal v-if="mostrarModal" @close="mostrarModal = false">
    <template #titulo>
      <h3 class="h4 text-gray-900 mb-4">¡Hola, {{ correoUsuario }}!</h3>
    </template>
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
            <input v-model="password" type="password" placeholder="Password"/>
          </div>
          <div class="col-sm-6 col-12">
            <input v-model="repetirPassword" type="password" placeholder="Repetir password"/>
          </div>
        </div>
      </form>
    </template>
    <template #botones>
      <div>
        <div class="botones-footer">
          <button type="button" class="btn-eliminar" @click.prevent="confirmarAccion('eliminar')">ELIMINAR</button>
          <button type="button" class="btn-actualizar" @click="confirmarAccion('actualizar')">ACTUALIZAR</button>
        </div>
      </div>
      <div class="mensaje-feedback">
        <hr>
        <div class="text-center">
          <p v-if="mensaje" :class="{'error-msg': error, 'success-msg': !error}">
            {{ mensaje }}
          </p>
        </div>
      </div>
    </template>
  </Modal>

  <!-- Modal Confirmar Acción -->
  <Modal v-if="mostrarConfirmacion" @close="mostrarConfirmacion = false" claseExtra="modal-confirmacion">
    <template #titulo >
      <h3 class="h4 text-gray-900 mb-4"> Confirmar acción </h3>
    </template>
    <template #contenido>
      <p>
        Estás a punto de <strong> {{ accionConfirmar === 'eliminar' ? 'eliminar tu cuenta' : 'actualizar tu perfil' }} </strong> <br>
        ¿Deseas continuar?
      </p>
    </template>
    <template #botones>
      <div class="botones-footer">
        <button type="submit" class="btn-cancelar" @click="mostrarConfirmacion = false">CANCELAR</button>
        <button type="submit" class="btn-confirmar" @click.prevent="ejecutarAccion">
          {{ loading ? (accionConfirmar === 'eliminar' ? 'Eliminando...' : (accionConfirmar === 'actualizar' ? 'Actualizando...' : 'Procesando...')) : (accionConfirmar ? accionConfirmar.toUpperCase() : 'CONFIRMAR') }}
        </button>
      </div>
    </template>
  </Modal>

  <Footer v-if="mostrarTemplates"/>
</template>

<script setup>
  import { useRoute, useRouter } from 'vue-router'
  import { ref, computed, onMounted, watch } from 'vue'
  import Navbar from './components/Navbar.vue'
  import Footer from './components/Footer.vue'
  import Modal from './components/Modal.vue'

  // mostrar navbar y footer
  const route = useRoute()
  const router = useRouter()

  const mostrarTemplates = computed(() => {
    return route?.path && !['/', '/registrarUsuario'].includes(route.path)
  })

  // control del modal
  const mostrarModal = ref(false)

  // mostrar usuario en saludo
  const correoUsuario = ref('Usuario')
  onMounted(() => {
    const correoUser = localStorage.getItem('correo');

    if(correoUser){
      let usuario = correoUser.split("@");
      correoUsuario.value = usuario[0];
    }
  });

  // cargar formulario
  const nombre = ref('')
  const apellido = ref('')
  const correo = ref('')
  const password = ref('')
  const repetirPassword = ref('')
  const userId = localStorage.getItem('userId');
  const token = localStorage.getItem('token');

  const cargarDatos = async () => {
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
    }catch(error){
      console.error("Error cargando usuario:", error)
    }
  }
  
  watch(mostrarModal, (abierto) => {
    if (abierto) {
      nombre.value = ''
      apellido.value = ''
      correo.value = ''
      password.value = ''
      repetirPassword.value = ''
      mensaje.value = ''
      error.value = false

      cargarDatos()
    }
  })

  const mensaje = ref('')
  const error = ref(false)
  const loading = ref(false)

  async function actualizarUsuario(){
    try{
      const body = {
        nombre: nombre.value,
        apellido: apellido.value,
        correo: correo.value
      }

      if(password.value){
        body.password = password.value
      }

      const response = await fetch(`http://localhost:8080/api/usuarios/${userId}`, {
        method: 'PUT',
        headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
      })

      if(!response.ok){
        const errData = await response.json()
        mensaje.value = errData.mensaje || 'Error actualizando usuario'
        error.value = true
        return
      }

      const data = await response.json()
      mensaje.value = 'Usuario actualizado correctamente'
      error.value = false
    }catch(error){
      mensaje.value = 'Error ' + error.message
      error.value = true
    }finally{
      loading.value = false
    }
  }

  async function eliminarUsuario(){
    try{
      const response = await fetch(`http://localhost:8080/api/usuarios/${userId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': 'Bearer ' + token,
        }
      })

      if(!response.ok){
        const errData = await response.json()
        mensaje.value = errData.mensaje || 'Error eliminando usuario'
        error.value = true
        return
      }

      mensaje.value = 'Cuenta eliminada correctamente. Cerrando sesión...'
      error.value = false
    }catch(error){
      mensaje.value = 'Error ' + error.message
      error.value = true
    }finally{
      loading.value = false
    }
  }

  const mostrarConfirmacion = ref(false)
  const accionConfirmar = ref(null)

  function confirmarAccion(accion){
    mensaje.value = ''
    error.value = false

    if(accion === 'actualizar'){
      if(!nombre.value.trim() || !apellido.value.trim()){
        mensaje.value = 'Por favor, completa todos los campos obligatorios.'
        error.value = true
        return
      }

      if(password.value && password.value !== repetirPassword.value){
        mensaje.value = 'Las contraseñas no coinciden'
        error.value = true
        return
      }
    }

    accionConfirmar.value = accion
    mostrarConfirmacion.value = true
  }

  async function ejecutarAccion(){
    loading.value = true
    mensaje.value = ''
    error.value = false

    if(accionConfirmar.value === 'actualizar'){
      await actualizarUsuario()

      setTimeout(() => {
        mostrarConfirmacion.value = false
      }, 1000)

      setTimeout(() => {
        mensaje.value = ''
        mostrarModal.value = false
      }, 3500)
    } else if(accionConfirmar.value === 'eliminar'){
      await eliminarUsuario()

      setTimeout(() => {
        mostrarConfirmacion.value = false
      }, 1000)

      setTimeout(() => {
        mostrarModal.value = false 
        localStorage.clear()
        router.push('/')
      }, 3500)
    }      
  }
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
  flex-direction: row;
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

 .btn-confirmar{
  background-color: var(--color-exito);
 }

 .btn-actualizar,
 .btn-cancelar{
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
  .mensaje-feedback {
    text-align: center;
    margin-top: 1rem;
    width: 100%;
  }
}

.mensaje-feedback {
  text-align: center;
  margin-top: 1rem;
}
</style>
