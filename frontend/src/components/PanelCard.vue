<template>
  <div class="container-fluid px-0">
    <div class="w-100">
        <div v-if="mostrarItems.length === 0" class="text-center mt-3">
            <p>No hay series que mostrar.</p> 
        </div>
        <div v-else class="d-flex flex-nowrap overflow-auto gap-0.25 px-1 justify-content-center">
            <CardItem v-for="(item, index) in mostrarItems" 
                :key="item.id"
                :serie="item"
                :promedio="calcularPromedio(item)"
                :activo="activarIndex === index"
                @clic="activarItem(index)"
                @info="abrirInformacion(item)"/>
        </div>
    </div>

    <!-- Modal -->
    <Modal v-if="mostrarModal" @close="cerrarInfo" claseExtra="modal-detalle  mi-modal-scroll">
      <template #titulo>
        <h3>{{ itemSeleccionado?.titulo }} | {{ formateaEnum(itemSeleccionado.genero) }} | {{ itemSeleccionado.pais }} - {{ itemSeleccionado.anioEmision }}</h3>
      </template>

      <template #contenido>
        <div class="row gx-4 gy-4 d-flex align-items-stretch p-3">
          <div class="col-12 col-md-4 d-flex flex-column justify-content-between">
            <img :src="itemSeleccionado.imagen"
                class="img-fluid rounded mb-3"
                style="max-height: 300px; object-fit: contain;" />

            <div class="text-center">
              <span v-for="i in 5" :key="i" class="star" :class="{ filled: i <= calcularPromedio(itemSeleccionado) }">★</span>
              <small class="text-muted d-block mt-1">{{ calcularPromedio(itemSeleccionado).toFixed(1) }}</small>
            </div>
          </div>

          <div class="col-12 col-md-8 d-flex flex-column justify-content-between">
            <div class="row gx-3 gy-2">
              <div class="col-6 col-lg-3">
                <input type="text" :value="itemSeleccionado.episodios + ' episodios'" readonly/>
              </div>
              <div class="col-6 col-lg-3">
                <input type="text" :value="itemSeleccionado.duracionMinutos + ' mn'" readonly/>
              </div>
              <div class="col-6 col-lg-3">
                <input type="text" :value="'Disp. en ' + formateaEnum(itemSeleccionado.plataforma)" readonly/>
              </div>
              <div class="col-6 col-lg-3">
                <input type="text" :value="formateaEnum(itemSeleccionado.estado)" readonly/>
              </div>
            </div>

            <div class="my-2">
              <input type="text" :value="'Protagonistas: ' + itemSeleccionado.protagonistasHistoria" readonly/>
            </div>

            <div class="row gx-3 gy-2">
              <div class="col-6">
                <input type="text" :value="'Inicio: ' + itemSeleccionado.fechaInicio" readonly/>
              </div>
              <div class="col-6">
                <input type="text" :value="'Fin: ' + itemSeleccionado.fechaFin" readonly/>
              </div>
            </div>

            <div>
              <textarea class="textarea-ajustable" :value="'Frase favorita:\n' + itemSeleccionado.fraseFavorita" readonly></textarea>
            </div>

            <div>
              <input type="text" :value="'Canción favorita: ' + itemSeleccionado.cancionFavorita" readonly/>
            </div>
          </div>
        </div>
      </template>

      <template #botones>
        <div class="botones-footer">
          <button type="button" class="btn-eliminar" @click.prevent="confirmarAccion('eliminar')">ELIMINAR</button>
          <button type="button" class="btn-editar" @click="vistaEditar(itemSeleccionado)">EDITAR</button>
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
          Estás a punto de eliminar la series <strong> {{ itemSeleccionado.titulo }} </strong> <br>
          ¿Deseas continuar?
        </p>
      </template>
      <template #botones>
        <div>
          <div class="botones-footer">
            <button type="button" class="btn-cancelar" @click="mostrarConfirmacion = false">CANCELAR</button>
            <button type="submit" class="btn-confirmar" @click.prevent="eliminarSerie"> {{ loading ? 'Eliminando...' : 'CONFIRMAR' }} </button>
          </div>
          <div class="mensaje-feedback">
            <hr>
            <div class="text-center">
              <p v-if="mensaje" :class="{'error-msg': error, 'success-msg': !error}">
                {{ mensaje }}
              </p>
            </div>
          </div>
        </div>
      </template>
    </Modal>
  </div>
</template>

<script setup>
  import { ref, computed } from 'vue'
  import { useRouter } from 'vue-router';
  import CardItem from './CardItem.vue';
  import Modal from './Modal.vue'

  const props = defineProps({
    items: {
        type: Array,
        required: true
    },
    mostrarSeccion: {
        type: String,
        default: 'todos'
    }
  })

  const router = useRouter()
  const mostrarModal = ref(false)
  const itemSeleccionado = ref(null);
  const activarIndex = ref(-1);
  const loading = ref(false)
  const mensaje = ref('')
  const error = ref(false)

  const mostrarItems = computed(() => {
    const currentDate = new Date();
    const currentMonth = 4;//currentDate.getMonth()
    const currentYear = 2023;//currentDate.getFullYear()

    const seriesFiltradas = props.items.filter(item => {
        const fechaFin = new Date(item.fechaFin); 
        const estatus = item.estado; 
        
        return (estatus === 'COMPLETADO' || estatus === 'ABANDONADO') && fechaFin.getMonth() === currentMonth && fechaFin.getFullYear() === currentYear;
    });

    if(seriesFiltradas.length === 0) return [];

    const seriesPromedio = seriesFiltradas.map(s => ({
        ...s,
        promedio: calcularPromedio(s)
    }))

    const promedios = seriesPromedio.map(s => s.promedio)
    const maxCalificacion = Math.max(...promedios)
    const minCalificacion = Math.min(...promedios)

    const ordenadasDes = [...seriesPromedio].sort((a, b) => b.promedio - a.promedio)
    const mejores = ordenadasDes.slice(0, 3)

    const umbralMejores = mejores[mejores.length - 1] ?? maxCalificacion

    let peores = seriesPromedio.filter(s => !mejores.some(m => m.id === s.id) && s.promedio < umbralMejores)
                               .sort((a, b) => a.promedio - b.promedio)
                               .slice(0, 3)

    const mostrarPeores = peores.length > 0

    if(props.mostrarSeccion === 'mejores') return mejores
    if(props.mostrarSeccion === 'peores') return mostrarPeores ? peores : []

    return seriesFiltradas
  });

  const activarItem = (index) => {
    activarIndex.value = index
  };

  const abrirInformacion = (item) => {
    itemSeleccionado.value = item
    mostrarModal.value = true
  }

  const cerrarInfo = () => {
    mostrarModal.value = false
    itemSeleccionado.value = null
  }

  function calcularPromedio(item) {
    const valores = [item.calificacionOst,
                     item.calificacionHistoria,
                     item.calificacionEscenografia,].filter(v => typeof v === 'number');
    
    if (valores.length === 0) return 0;
    
    return valores.reduce((a, b) => a + b, 0) / valores.length;
  }

  const formateaEnum = (valor) => {
    return valor.toLowerCase()
                .replace(/_/g, ' ')
                .replace(/\b\w/g, c => c.toUpperCase());
  }

  const mostrarConfirmacion = ref(false)
  const accionConfirmar = ref(null)

  function confirmarAccion(accion){
    accionConfirmar.value = accion
    mostrarConfirmacion.value = true
  }

  const emit = defineEmits(['actualizarSeries'])

  async function eliminarSerie(){
    loading.value = true
    mensaje.value = ''
    error.value = false
    
    try{
      const token = localStorage.getItem('token')

      const response = await fetch(`http://localhost:8080/api/series/${itemSeleccionado.value.id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      })

      if(!response.ok){
        let data = {}

        try{
          data = await response.json()
        }catch(error){}

        mensaje.value = data.mensaje || 'Error eliminando serie'
        error.value = true
        return
      }

      emit('actualizarSeries', itemSeleccionado.value.id)

      mensaje.value = 'Serie eliminada correctamente'
      error.value = false

      setTimeout(() => {
        mostrarConfirmacion.value = false
      }, 10000)

      setTimeout(() => {
        mostrarModal.value = false 
      }, 35000)
    }catch(error){
      mensaje.value = error.message || 'Error inesperado al eliminar'
      error.value = true
    } finally {
      loading.value = false
    }
  }

  function vistaEditar(item){
    router.push( `/editarSerie/${item.id}`)
  }
</script>

<style scoped>
  .star {
    font-size: 1.5rem;
    color: #ccc;
  }

  .star.filled {
    color: var(--color-fondo);
  }

  .textarea-ajustable {
    resize: none;
    overflow-y: auto; 
    min-height: calc(1.5em * 3);
    max-height: calc(1.5em * 7); 
    line-height: 1.5em;  
    width: 100%;
    box-sizing: border-box;    
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
  }

  .botones-footer button{
    width: 95%;
    max-width: 200px;
  }

  .btn-eliminar{
    background-color: var(--color-error);
  }

  .btn-confirmar{
    background-color: var(--color-exito);
  }

  .btn-editar,
  .btn-cancelar{
    background-color: var(--color-secundario);
    margin-right: 0 !important;
  }
 
  @media (min-width: 768px) {
    .botones-footer {
      flex-direction: row;
      justify-content: flex-end;
      gap: 1rem;
      padding-right: 3rem;
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