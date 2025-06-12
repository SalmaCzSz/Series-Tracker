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
    <Modal v-if="mostrarModal" @close="cerrarInfo" claseExtra="modal-detalle" class="text-center">
      <template #titulo>
        <h3>{{ itemSeleccionado?.titulo }} &nbsp;&nbsp;|&nbsp;&nbsp;{{ formateaEnum(itemSeleccionado.genero) }}&nbsp;&nbsp;|&nbsp;&nbsp;{{ itemSeleccionado.pais }} - {{ itemSeleccionado.anioEmision }}</h3>
      </template>

      <template #contenido>
        <div class="row gx-4 gy-4 d-flex align-items-stretch p-5"  style="min-height: 100%;">
          <div class="col-12 col-md-4 d-flex flex-column justify-content-between h-100">
            <img :src="itemSeleccionado.imagen"
                  class="img-fluid rounded mb-3 flex-grow-1"
                  style="max-height: 300px; width: 100%; object-fit: contain;"/>

            <div class="text-center">
              <span v-for="i in 5" :key="i" class="star" :class="{ filled: i <= calcularPromedio(itemSeleccionado) }"> ★ </span>
              <small class="text-muted d-block mt-1"> {{ calcularPromedio(itemSeleccionado).toFixed(1) }} </small>
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
        
      </template>
    </Modal>
  </div>
</template>

<script setup>
  import { ref } from 'vue';
  import PanelItem from './PanelItem.vue';
  import Modal from './Modal.vue'

  const props = defineProps({
    items: {
        type: Array,
        required: true
    }
  })

  const activarIndex = ref(0)
  const mostrarModal = ref(false)
  const itemSeleccionado = ref(null)

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
</style>