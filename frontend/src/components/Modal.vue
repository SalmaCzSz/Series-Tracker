<template>
  <div class="mi-modal">
    <div class="mi-modal-inner mi-modal-scroll" :class="claseExtra">
      <div class="mi-modal-close" @click="$emit('close')">
        &times;
      </div>
      <div class="mi-modal-content">
        <slot name="titulo" />
        <slot name="contenido" />
      </div>
      <div class="mi-modal-footer">
        <slot name="botones" />
      </div>
    </div>
  </div>
</template>

<script setup>
  defineEmits(['close'])

  const props = defineProps({
    claseExtra: {
      type: String,
      default: ''
    }
  })
</script>

<style scoped>
  .mi-modal{
    position: fixed;
    top: 0;
    left: 0;
    z-index: 10;
    height: 100vh;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .mi-modal-close{
    position: absolute;
    height: 52px;
    width: 52px;
    display: flex;
    justify-content: center;
    align-items: center;
    top: 0;
    right: 0;
    font-size: 3rem;
    cursor: pointer;
  }

  .mi-modal-inner{
    position: relative;
    width: 90%;
    max-width: 400px;
    padding: 1.5rem;
    border-radius: 1rem;
    box-shadow: 0 5px 5px rgba(0, 0, 0, 0.2);
    background-color: var(--color-primario);
    margin: 2rem auto;
  }

  .mi-modal-content{
    justify-content: center;
    text-align: center;
  }

  .mi-modal-footer{
    margin-top: 0.75rem;
  }

  .modal-detalle{
    width: 90vw;         
    max-width: 1200px; 
    height: auto;
  }

  .mi-modal-scroll {
    max-height: 90vh; 
    overflow-y: auto;
    overflow-x: hidden; 
    padding-right: 0.5rem;
    
    scrollbar-width: thin;
    scrollbar-color: var(--color-exito) transparent;
  }

  .modal-confirmacion{
    background-color: var(--color-advertencia);
  }
</style>