<template>
  <div class="card h-100 shadow-sm card-item">
    <img :src="serie.imagen" class="card-img-top imagen-borde" />
    <div class="card-body d-flex flex-column justify-content-between">
      <h3 class="card-title"> {{ serie.titulo }} </h3>
      <div class="d-flex justify-content-between align-items-center mt-auto">
          <span class="badge badge-pill badge-color-estado px-3 py-2"> {{ serie.estado }} </span>
          <span class="badge badge-pill badge-color-calificacion px-3 py-2"> {{ promedio.toFixed(1) }} </span>
          <button class="boton-info" @click="$emit('info')">
            <i class="fa fa-info"></i>
          </button>
      </div>
    </div>
  </div>    
</template>

<script setup>
  defineProps({
    serie: {
        type: Object,
        required: true
    },
    promedio: {
        type: Number,
        required: true
    }
  });

  defineEmits(['info'])
</script>

<style scoped>
  .card-item{
    width: 100%;
    max-width: 280px;
    min-width: 220px;
    flex: 0 0 auto;
    margin: 0 0.5rem;
    border-radius: 0.5rem;
    overflow: hidden;
    background-color: var(--color-primario);
  }

  .badge-color-estado{
    background-color: var(--color-error);
  }

  .badge-color-calificacion{
    background-color: var(--color-advertencia);
  }

  .imagen-borde{
    border: 8px solid var(--color-primario);
    border-radius: 0.5rem;
    padding: 0.5rem;
    object-fit: cover;
  }

  .boton-info{
    border-radius: 0.25rem;
    width: 32px;
    height: 32px;
    padding: 0;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    background-color: var(--color-exito);
    border: 1px solid var(--color-primario);
    box-shadow: 0 1px 2px rgba(0,0,0,0.05);
  
    cursor: pointer;
    transition: background-color 0.3s ease, box-shadow 0.3s ease;
    line-height: 1;               
    font-size: 16px;              
    vertical-align: middle; 
  }
</style>