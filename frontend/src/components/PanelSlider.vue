<template>
  <div class="container-fluid px-0 p-5">
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
    <Modal v-if="mostrarModal" @close="cerrarInfo">
      <template #titulo>
        <h4>{{ itemSeleccionado?.titulo }}</h4>
      </template>

      <template #contenido>
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
</style>