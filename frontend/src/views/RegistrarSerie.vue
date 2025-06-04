<template>
  <div class="p-5">
    <div class="col-xl-12 col-lg-12 col-md-9 col-10">
      <h3>+ Serie</h3>
    </div>
  </div>
  <div class="container">
    <form @submit.prevent="guardarSerie">
      <div class="row">
        <!-- Imagen -->
        <div class="col-md-3 text-center mb-4">
          <div class="mb-2">
            <input type="file" accept="image/*" @change="vistaPrevia" class="form-control-file" />
          </div>
          <img v-if="imgPreview" :src="imgPreview" class="img-fluid rounded shadow" />
          <div v-else class="text-muted"> Selecciona una imagen </div>
        </div>

        <!-- Ficha técnica -->
        <div class="col-md-9">
          <div class="row form-ficha-tecnica p-3 mb-3">
            <div class="form-group col-md-6">
              <input v-model="nombre" placeholder="Nombre" required />
            </div>  
            <div class="form-group col-md-6">
              <select v-model="genero" required>
                <option disabled value=""> Genero </option>
                <option>Romance</option>
                <option>Drama</option>
                <option>Comedia</option>
                <option>Acción</option>
              </select>
            </div>
            <div class="form-group col-md-3">
              <input v-model="pais" placeholder="Pais" required />
            </div>
            <div class="form-group col-md-3">
              <input v-model="anioEmision" placeholder="Año emisión" required type="number" />
            </div>
            <div class="form-group col-md-3">
              <input v-model="noEpisodios" placeholder="Episodios" required type="number" />
            </div>
            <div class="form-group col-md-3">
              <input v-model="duracionMinutos" placeholder="Duración mn" required type="number" />
            </div>
            <div class="form-group col-md-12">
              <input v-model="protagonistas" placeholder="Protagonistas" required  />
            </div>
            <div class="form-group col-md-6">
              <select v-model="plataforma" required>
                <option disabled value=""> Plataforma </option>
                <option>Netflix</option>
                <option>Viki</option>
                <option>Prime Video</option>
              </select>
            </div>
            <div class="form-group col-md-6">
              <select v-model="estatus" required>
                <option disabled value=""> Estatus </option>
                <option>Finalizada</option>
                <option>En emisión</option>
                <option>Pendiente</option>
              </select>
            </div>
            <div class="form-group col-md-6">
              <label class="m-0 text-secondary">Fecha Inicio</label>
              <input v-model="fechaInicio" required type="date" />
            </div>
            <div class="form-group col-md-6">
              <label class="m-0 text-secondary">Fecha Fin</label>
              <input v-model="fechaFin" required type="date" />
            </div>
          </div>
        </div>
      </div>

      <div class="row py-4">
        <!-- Frase favorita -->
        <div class="col-12 col-md-6">
          <div class="row form-frase p-3">
            <div class="form-group col-md-12 mt-0 mb-0">
              <textarea v-model="fraseFavorita" placeholder="Frase favorita" required rows="10" />
            </div>
          </div>
        </div>

        <!-- Calificaciones -->
        <div class="col-12 col-md-6">
          <div class="row form-calificaciones p-3">
            <div class="form-group col-md-12">
              <input v-model="calificacionHistoria" placeholder="Calificación historia" required type="number" min="0" max="10" />
            </div>
            <div class="form-group col-md-12">
              <input v-model="calificacionOST" placeholder="Calificación OST" required type="number" min="0" max="10" />
            </div>
            <div class="form-group col-md-12">
              <input v-model="calificacionEscenografia" placeholder="Calificación escenografía" required type="number" min="0" max="10"/>
            </div>
            <div class="form-group col-md-12">
              <input placeholder="Promedio" required type="number" disabled :value="promedio.toFixed(1)"/>
            </div>
          </div>
        </div> 
      </div>

      <div class="row py-4">
        <!-- Cancion favorita -->
        <div class="col-12 col-md-6">
          <div class="row form-cancion p-3">
            <div class="form-group col-md-12">
              <textarea v-model="cancionFavorita" placeholder="Canción favorita" required rows="3" />
            </div>
          </div>
        </div>

        <!-- Estrellas -->
        <div class="col-12 col-md-6">
          <div class="row form-estrellas p-3">
            <div class="form-group col-md-12 d-flex justify-content-center align-items-center">
              <div class="rating-star">
                <div class="rating-star-back">
                  <i v-for="n in 5" :key="'bg-' + n" class="fa fa-star" ></i>
                </div>
                <div class="rating-star-foreground" :style="{ width: estrellasLlenas * 20 + '%' }">
                  <i v-for="n in 5" :key="'fg-' + n" class="fa fa-star" ></i>
                </div>
              </div>
            </div>
          </div>
        </div> 
      </div>
      
      <!-- Botones -->
      <div class="row">
        <div class="col-md-12 text-right">
          <button type="submit" :disabled="loading">
            {{ loading ? "Registrando..." : "GUARDAR" }}
          </button>
        </div>
      </div>
    </form>
  </div>
</template>

<script setup>
  import { ref, reactive, computed } from 'vue'

  const imgPreview = ref(null)

  const nombre = ref('');
  const genero = ref('');
  const pais = ref('');
  const anioEmision = ref('');
  const noEpisodios = ref('');
  const duracionMinutos = ref('');
  const protagonistas = ref('');
  const plataforma = ref('');
  const estatus = ref('');
  const fechaInicio = ref('');
  const fechaFin = ref('');
  const fraseFavorita = ref('');
  const cancionFavorita = ref('');
  const calificacionHistoria = ref('');
  const calificacionOST = ref('');
  const calificacionEscenografia = ref('');

  const promedio = computed(() => {
    const total = Number(calificacionHistoria.value) + Number(calificacionOST.value) + Number(calificacionEscenografia.value)
    return total ? total / 3 : 0;
  });

  const estrellasLlenas = computed(() => promedio.value / 2);
  console.log('Estrellas llenas:', estrellasLlenas.value);

  function vistaPrevia(event){
    const file = event.target.files[0];

    if(file && file.type.startsWith('image/')){
        imgPreview.value = URL.createObjectURL(file);
    } else {
        imgPreview.value = null;
    }
  }
</script>

<style scoped>
  @import "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css";

  .form-ficha-tecnica,
  .form-frase,
  .form-calificaciones,
  .form-cancion,
  .form-estrellas {
    border-radius: 1rem;
    padding: 1.5rem;
    margin-bottom: 2rem;
  }

  .form-ficha-tecnica {
    background-color: var(--color-secundario);
  }

  .form-frase {
    background-color: var(--color-exito);
  }

  .form-calificaciones{
    background-color: var(--color-primario);
  }

  .form-cancion {
    background-color: var(--color-advertencia);
  }

  .form-estrellas {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 150px;
    background-color: var(--color-advertencia);
    border-radius: 1rem;
  }

  .rating-star{
    position: relative;
    display: inline-flex;
    gap: 1rem;
    font-size: 2.5rem;
    line-height: 1;
    overflow: hidden;
    width: calc(5em + 4rem + 0.5rem);
    height: 1em;
  }

  .rating-star i {
    margin: 0;
    padding: 0;
    flex-shrink: 0;
    width: 1em;
    height: 1em;
  }

  .rating-star-back,
  .rating-star-foreground{
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    white-space: nowrap;
    overflow: hidden;
    display: flex;
    gap: 1rem;
  }

  .rating-star-back {
    color: gray;
    z-index: 1;
  }

  .rating-star-foreground{
    color: var(--color-fondo);
    pointer-events: none;
    transition: width 0.3s ease;
    z-index: 2;
  }

  input, 
  select,
  textarea {
    width: 100%;
    padding: 0.5rem;
    margin-top: 0.75rem;
    border-radius: 20px;
    border: 0;
    background-color: var(--color-fondo);
  }

  button {
    margin-top: 1rem;
    padding: 0.5rem 1rem;
    width: 100%;
    max-width: 200px;
    border-radius: 20px;
    border: none; 
    font-weight: bold;
    background-color: var(--color-exito);
    cursor: pointer;
  }

  @media (min-width: 768px) {
    .form-frase,
    .form-cancion {
      margin-right: 1rem;
    }

    .form-calificaciones,
    .form-estrellas {
      margin-left: 1rem;
    }
  }

  @media (max-width: 768px) {
    .form-ficha-tecnica,
    .form-frase,
    .form-calificaciones,
    .form-cancion,
    .form-estrellas {
      margin: 0 auto 2rem auto;
      padding: 1rem;
    }

    .img-fluid {
      max-width: 80%;
      margin: 0 auto;
      display: block;
    }

    button {
      width: 100%;
      display: block;
    }
  }

  .row > [class*='col-'] {
    margin-bottom: 1.5rem;
  }

  @media (min-width: 768px) {
    .row > .col-md-6 {
      display: flex;
      flex-direction: column;
    }

    .form-frase,
    .form-calificaciones {
      flex-grow: 1;
      height: 100%;
    }

    .form-cancion,
    .form-estrellas {
      flex-grow: 1;
      height: 100%;
    }
  }
</style>
