<template>
  <div class="container-fluid px-0">
    <div class="d-flex justify-content-center">
        <div class="slider-wrapper w-100">
            <div class="d-flex flex-nowrap overflow-auto" style="max-width: 100%; gap: 1rem;">
            <PanelItem v-for="(item, index) in items"
                        :key="index"
                        :background="item.imagen"
                        :titulo="item.titulo"
                        :activo="activarIndex === index"
                        @clic="activarItem(index)"
                        @info="abrirInformacion(item)" />
            </div>
        </div>
    </div>

    <!-- Modal info -->
    <Modal v-if="mostrarModal" @close="cerrarInfo" claseExtra="modal-detalle mi-modal-scroll" class="text-center">
      <template #titulo>
        <h3>{{ itemSeleccionado.titulo }} &nbsp;&nbsp;|&nbsp;&nbsp;{{ formateaEnum(itemSeleccionado.genero) }}&nbsp;&nbsp;|&nbsp;&nbsp;{{ itemSeleccionado.pais }} - {{ itemSeleccionado.anioEmision }}</h3>
      </template>

      <template #contenido>
        <div class="row gx-4 gy-4 d-flex align-items-stretch p-5"  style="min-height: 100%;">
          <div class="col-12 col-md-4 d-flex flex-column justify-content-between h-100">
            <img :src="itemSeleccionado.imagen"
                  class="img-fluid rounded mb-3 flex-grow-1"
                  style="max-height: 300px; width: 100%; object-fit: contain;"/>

            <div class="text-center mb-0">
              <span v-for="i in 5" :key="i" class="star" :class="{ filled: i <= calcularPromedio(itemSeleccionado) }"> ★ </span>
              <small class="text-muted d-block mb-0"> {{ calcularPromedio(itemSeleccionado).toFixed(1) }} </small>
            </div>  
          </div>

          <div class="col-12 col-md-8 d-flex flex-column justify-content-between h-100 pb-3">
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
                <input type="text" :value="'Fecha inicio: ' + itemSeleccionado.fechaInicio" readonly/>
              </div>
              <div class="col-6">
                <input type="text" :value="'Fecha fin: ' + itemSeleccionado.fechaFin" readonly/>
              </div>
            </div>
            <div>
              <textarea class="textarea-ajustable" :value="'Frase favorita:\n' + itemSeleccionado.fraseFavorita" readonly></textarea>
            </div>
            <div>
              <input  type="text" :value="'Canción favorita: ' + itemSeleccionado.cancionFavorita" readonly/>
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
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import PanelItem from './PanelItem.vue';
  import Modal from './Modal.vue'

  const props = defineProps({
    items: {
        type: Array,
        required: true
    }
  })

  const router = useRouter()
  const activarIndex = ref(0)
  const mostrarModal = ref(false)
  const itemSeleccionado = ref(null)
  const loading = ref(false)
  const mensaje = ref('')
  const error = ref(false)

  const activarItem = (index) => {
    activarIndex.value = index
  }

  const abrirInformacion = (item) => {
    itemSeleccionado.value = item
    mostrarModal.value = true
  }

  const cerrarInfo = () => {
    mostrarModal.value = false
    itemSeleccionado.value = null
  }

  const formateaEnum = (valor) => {
    return valor.toLowerCase()
                .replace(/_/g, ' ')
                .replace(/\b\w/g, c => c.toUpperCase());
  }

  function calcularPromedio(item) {
    const valores = [item.calificacionOst,
                     item.calificacionHistoria,
                     item.calificacionEscenografia,].filter(v => typeof v === 'number');
    
    if (valores.length === 0) return 0;
    
    return valores.reduce((a, b) => a + b, 0) / valores.length;
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
  .slider-wrapper {
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
    display: flex;
    justify-content: center;
  }

  .slider-wrapper > div {
    display: flex;
    justify-content: center;
    flex-wrap: nowrap;
    overflow-x: hidden;
    gap: 1rem;
  }

  .star {
    font-size: 1.5rem;
    color: #ccc;
    margin-bottom: 0 !important;
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