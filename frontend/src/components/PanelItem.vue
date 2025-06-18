<template>
  <div class="panel d-flex align-items-end position-relative mr-3"
       :class="{ active: activo }" 
       :style="{ backgroundImage: `url(${ background })` }"
       @click="$emit('clic')">

    <div class="panel-footer w-100 d-flex justify-content-between align-items-center px-3 py-2" @click.stop>
        <h3 v-if="activo" class="mb-0 text-black-50">{{ titulo }}</h3>
        <button v-if="activo" class="boton-info" @click="$emit('info')">
          <i class="fa fa-info"></i>
        </button>
    </div>
  </div>
</template>

<script setup>
  defineProps({
    titulo: String,
    background: String,
    activo: Boolean
  })

  defineEmits(['clic', 'info'])
</script>

<style scoped>
  .panel {
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    height: 80vh;
    border-radius: 20px;
    color: white;
    cursor: pointer;
    flex: 0 0 5%;
    overflow: hidden;
    transition: all 0.5s ease-in-out;
    min-width: 60px;
  }

  .panel.active {
    flex: 0 0 40%;
    min-width: 300px;
  }

  .panel-footer {
    background: linear-gradient(to top, var(--color-secundario), transparent);
    border-bottom-left-radius: 20px;
    border-bottom-right-radius: 20px;
  }

  .boton-info{
    border-radius: 0.25rem;
    width: 32px;
    height: 32px;
    padding: 0;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    background-color: var(--color-secundario);
    border: 1px solid var(--color-primario);
    box-shadow: 0 1px 2px rgba(0,0,0,0.05);
  
    cursor: pointer;
    transition: background-color 0.3s ease, box-shadow 0.3s ease;
    line-height: 1;               
    font-size: 16px;              
    vertical-align: middle; 
  }

  .boton-info i {
    color: var(--color-fondo);
  }
</style>