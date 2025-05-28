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
                      <h1 class="h4 text-gray-900 mb-4">Crea una cuenta</h1>
                    </div>
                    <form class="user">
                      <div class="form-group row">
                        <div class="col-12 col-sm-6 mb-3 mb-sm-0">
                          <input v-model="nombre" placeholder="Nombre" id="txtNombre" required />
                        </div>
                        <div class="col-sm-6 col-12">
                          <input v-model="apellido" placeholder="Apellido" id="txtApellido" required />
                        </div>
                      </div>
                      <div class="form-group">
                        <input v-model="correo" type="email" placeholder="Correo" id="txtCorreo" required />
                      </div>
                      <div class="form-group row">
                        <div class="col-12 col-sm-6 mb-3 mb-sm-0">
                          <input v-model="password" type="password" placeholder="Password" id="txtPassword" required />
                        </div>
                        <div class="col-sm-6 col-12">
                          <input v-model="repetirPassword" type="password" placeholder="Repetir password" id="txtRepetirPassword" required />
                        </div>
                      </div>
                      <button type="submit" :disabled="loading" onclick="registrarUsuario()">
                        {{ loading ? "Registrando..." : "CREAR CUENTA" }}
                      </button>
                    </form>
                    <hr>
                    <div class="text-center">
                      <!-- <a class="small" href="registrar.html">Crear cuenta</a> -->
                      <p v-if="message" :class="{'error-msg': error, 'success-msg': !error}">
                        {{ message }}
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
   margin-top: 0.75rem;
   padding: 0.5rem;
   width: 100%;
   border-radius: 20px;
   border: none;
   background-color: var(--color-secundario);
   font-weight: bold;
   cursor: pointer;
 }

 .error-msg {
   color: red;
   margin-top: 1rem;
   text-align: center;
 }

 .success-msg {
   color: green;
   margin-top: 1rem;
   text-align: center;
 }
</style>
