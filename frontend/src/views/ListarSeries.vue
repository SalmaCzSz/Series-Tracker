<template>
  <div class="page-wrapper d-flex flex-column min-vh-100">
    <main class="flex-grow-1 container-fluid px-3 px-md-5">
      <input v-model="filtro" type="text" placeholder="Filtrar serie" class="mb-3 input-filtro"/>
      <div class="mb-5 min-vh-">
        <PanelCard :items="seriesFiltradas" />
      </div>
    </main>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from 'vue'
  import PanelCard from '../components/PanelCard.vue';

  const series = ref([])
  const filtro = ref('')
  const loading = ref(true)
  const error = ref(false)

  function calcularPromedio(item){
    const valores = [
        item.calificacionOst,
        item.calificacionHistoria,
        item.calificacionEscenografia
    ].filter(v => typeof v === 'number')

    return valores.length ? valores.reduce((a, b) => a + b, 0) / valores.length : 0
  }

  const seriesFiltradas = computed(() => {
    const texto = filtro.value.toLowerCase()

    return series.value.filter(serie => {
        const promedio = calcularPromedio(serie).toFixed(1)

        return (
            serie.titulo.toLowerCase().includes(texto) || 
            serie.estado.toLowerCase().includes(texto) || 
            promedio.includes(texto)
        )
    })
  })

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
  .input-filtro{
    background-color: var(--color-secundario);
  }

  .page-wrapper{
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }

  main{
    flex: 1;
  }
</style>
