<template>
  <!-- Ultimos agregados -->
  <div class="my-3 ultimos-agregados">
    <div class="row">
      <div class="col-xl-12 col-lg-12 col-md-9 col-10">
        <h3>Últimos agregados</h3>
      </div>
    </div>
    <div v-if="loading">
      <p>Cargando series...</p>
    </div>
    <div v-else-if="error">
      <p class="text-danger">Ocurrió un error al cargar los datos.</p>
    </div>
    <div v-else-if="series.length === 0">
      <p class="text-muted">Sin información disponible</p>
    </div>
    <div v-else>
      <PanelSlider :items="series">
        <template #modal="{ show, item, close }">
          <MyModal v-if="show" @close="close">
            <template #titulo>{{ item.nombre }} | {{ item.genero }} | {{ item.pais }} - {{ item.anioEmision }}</template>
            <template #contenido />
          </MyModal>
        </template>
      </PanelSlider>
    </div>
  </div>

  <!-- Mejor evaluados -->
  <div class="my-3 mejor-evaluados">
    <div class="row">
      <div class="col-xl-10 col-lg-12 col-md-9 col-10">
        <h3>Mejor evaluados</h3>
      </div>
    </div>
    <div v-if="loading">
      <p>Cargando series...</p>
    </div>
    <div v-else-if="error">
      <p class="text-danger">Ocurrió un error al cargar los datos.</p>
    </div>
    <div v-else-if="series.length === 0">
      <p class="text-muted">Sin información disponible</p>
    </div>
    <div v-else>
      <ul>
        <li v-for="serie in series" :key="serie.id">{{ serie.nombre }}</li>
      </ul>
    </div>
  </div>

  <!-- Peor evaluados -->
  <div class="my-3 peor-evaluados">
    <div class="row">
      <div class="col-xl-10 col-lg-12 col-md-9 col-10">
        <h3>Peor evaluados</h3>
      </div>
    </div>
    <div v-if="loading">
      <p>Cargando series...</p>
    </div>
    <div v-else-if="error">
      <p class="text-danger">Ocurrió un error al cargar los datos.</p>
    </div>
    <div v-else-if="series.length === 0">
      <p class="text-muted">Sin información disponible</p>
    </div>
    <div v-else>
      <ul>
        <li v-for="serie in series" :key="serie.id">{{ serie.nombre }}</li>
      </ul>
    </div>
  </div>

  <!-- Graficas -->
  <div class="my-3 graficas">
    <div class="container-graf graf-pais">
      <div class="row justify-content-center">
        <div class="col-xl-5 col-md-6 text-center">
          <h3>Series por país</h3>
        </div>
      </div>
      <div v-if="loading">
        <p>Cargando series...</p>
      </div>
      <div v-else-if="error">
        <p class="text-danger">Ocurrió un error al cargar los datos.</p>
      </div>
      <div v-else-if="series.length === 0">
        <p class="text-muted">Sin información disponible</p>
      </div>
      <div v-else>
        <ul>
          <li v-for="serie in series" :key="serie.id">{{ serie.nombre }}</li>
        </ul>
      </div>
    </div>
    <div class="container-graf graf-estatus">
      <div class="row justify-content-center">
        <div class="col-xl-5 col-md-6 text-center">
          <h3>Series por estatus</h3>
        </div>
      </div>
      <div v-if="loading">
        <p>Cargando series...</p>
      </div>
      <div v-else-if="error">
        <p class="text-danger">Ocurrió un error al cargar los datos.</p>
      </div>
      <div v-else-if="series.length === 0">
        <p class="text-muted">Sin información disponible</p>
      </div>
      <div v-else>
        <ul>
          <li v-for="serie in series" :key="serie.id">{{ serie.nombre }}</li>
        </ul>
      </div>
    </div>
  </div>

  <!-- Frase -->
  <div class="frase-container my-3">
    <div class="linea-decorativa">
      <span>♡</span>
    </div>
    <h3>
        “El tiempo que pasamos viendo series es un regalo para la mente y <br>
        el corazón, donde la aventura y la emoción nos esperan”
    </h3>
    <div class="linea-decorativa">
      <span>♡</span>
    </div>
  </div>
</template>

<script setup>
  import { ref, onMounted } from 'vue'
  import PanelSlider from '../components/PanelSlider.vue'
  import Modal from '../components/Modal.vue'

  const series = ref([])
  const loading = ref(true)
  const error = ref(false)

  onMounted(async () => {
    try {
      const token = localStorage.getItem('token')
      const idUsuario = localStorage.getItem('userId')

      const response = await fetch(`http://localhost:8080/api/usuarios/series/${idUsuario}`, {
        method: 'GET',
        headers: { 'Authorization': 'Bearer ' + token },
      });

      if (!response.ok) throw new Error(`Error HTTP: ${response.status}`)

      const data = await response.json()
      //series.value = data

      const sorted = data.sort((a, b) => b.id - a.id).slice(0, 5)

      series.value = sorted.map(item => ({
        id: item.id,
        nombre: item.serie.nombre,
        imagen: `data:image/jpeg;base64,${item.serie.imagenPortada}`,
        titulo: item.serie.nombre,
        genero: item.serie.genero,
        pais: item.serie.pais,
        anioEmision: item.serie.anioEmision,
        episodios: item.serie.episodios,
        duracionMinutos: item.serie.duracionMinutos,
        protagonistasHistoria: item.serie.protagonistasHistoria,
        plataforma: item.plataforma,
        estado: item.estado,
        fechaInicio: item.fecha_inicio,
        fechaFin: item.fecha_fin,
        fraseFavorita: item.fraseFavorita,
        cancionFavorita: item.cancionFavorita,
        calificacionHistoria: item.calificacionHistoria,
        calificacionOst: item.calificacionOst,
        calificacionEscenografia: item.calificacionEscenografia
      }))
    } catch (err) {
      console.error('Error al obtener series: ', err)
      error.value = true
    } finally {
      loading.value = false
    }
  })
</script>


<style scoped>
  .ultimos-agregados{
    padding: 2rem;
  }

  .mejor-evaluados{
    background-color: var(--color-secundario);
    padding: 2rem;
  }

  .peor-evaluados{
    padding: 2rem;
  }

  .graficas {
    display: flex;
    width: 100%;
  }

  .container-graf {
    flex: 1;
    padding: 2rem;
    justify-content: center;
    align-items: center;
    text-align: center;
  }

  .graf-pais{
    background-color: var(--color-error);
  }

  .graf-estatus{
    background-color: var(--color-advertencia);
  }

  .frase-container {
    text-align: center;
    padding: 2rem;
  }

  .linea-decorativa {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 2rem 0;
  }

  .linea-decorativa::before,
  .linea-decorativa::after {
    content: "";
    flex: 1;
    border-bottom: 1px dashed black;
    margin: 0 10px;
  }
</style>