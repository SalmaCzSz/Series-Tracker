<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-xl-10 col-lg-12 col-md-9 col-10">
        <div class="card o-hidden border-0 shadow-lg my-5">
          <div class="card-body p-0">
            <div class="row">
              <div class="col-lg-6 d-none d-lg-block imagen"></div>
                <div class="col-lg-6 col-12 formulario">
                  <div class="p-5">
                    <div class="text-center">
                      <h1 class="h4 text-gray-900 mb-4">¡Bienvenido!</h1>
                    </div>
                    <form class="login" @submit.prevent="iniciarSesion">
                      <div class="form-group">
                        <input v-model="correo" type="email" placeholder="Correo" id="txtCorreo" required />
                      </div>
                      <div class="form-group">
                        <input v-model="password" type="password" placeholder="Password" id="txtPassword" required />
                      </div>
                      <button type="submit" :disabled="loading">
                        {{ loading ? "Iniciando Sesión..." : "INICIAR SESIÓN" }}
                      </button>
                    </form>
                    <hr>
                    <div class="text-center">
                      <router-link class="registro-link" to="/registrarUsuario">
                        ¿No tienes una cuenta? ¡Registrate!
                      </router-link>
                      <p v-if="mensaje" :class="{'error-msg': error, 'success-msg': !error}">
                        {{ mensaje }}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'

  const correo = ref('')
  const password = ref('')

  const mensaje = ref('')
  const error = ref(false)
  const loading = ref(false)
  const router = useRouter()

  async function iniciarSesion(){
    mensaje.value = ''
    loading.value = true

    try{
      if(!correo.value || !password.value){
        mensaje.value = 'Por favor, completa todos los campos.'
        error.value = true
        loading.value = false
        return
      }

      const requestLogin = await fetch('http://localhost:8080/api/login',{
        method: 'POST',
        headers: { 
          'Accept': 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          correo: correo.value,
          password: password.value
        })
      })

      if (!requestLogin.ok) {
        mensaje.value = 'Las credenciales son incorrectas'
        error.value = true
        loading.value = false
        return
      } 

      const respuesta = await requestLogin.json()
      localStorage.setItem('token', respuesta.token);
      localStorage.setItem('userId', respuesta.userId);
      localStorage.setItem('correo', correo.value);

      mensaje.value = 'Inicio de sesión exitoso :)'
      error.value = false

      setTimeout(() => {
        router.push('/inicio')
      }, 3500)
    }catch(e){
      mensaje.value = 'Error ' + e.message
      error.value = true
    }finally{
      loading.value = false
    }
  }
</script>

<style scoped>
 .imagen {
   background-image: url('/portada.PNG');
   background-position: center;
   background-size: cover;
 }

 .formulario {
   flex: 1;
   background-color: var(--color-primario);
   padding: 2rem;
   display: flex;
   flex-direction: column;
   justify-content: center;
 }

 .formulario h1 {
   margin-bottom: 1rem;
   text-align: center;
   font-weight: 200;
 }

 button {
   margin-top: 0.75rem;
   padding: 0.5rem;
   width: 100%;
   border-radius: 20px;
   border: none;
   background-color: var(--color-secundario);
   font-weight: bold;
   cursor: pointer;
 }

 .registro-link {
   text-align: center;
   margin-top: 1rem;
   font-size: 0.9rem;
   font-weight: bold;
   color: #3b3b3b;
 }
</style>
