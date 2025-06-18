<template>
  <div class="chart-wrapper">
    <Doughnut :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
  import { Doughnut } from 'vue-chartjs';
  import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale, plugins } from 'chart.js'

  ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale)

  defineProps(['chartData'])

  const chartOptions = {
    responsive: true,
    plugins: {
        legend: {
            position: 'bottom'
        }
    }
  }
</script>

<style scoped>
  .chart-wrapper{
    width: 100%;
    max-width: 300px;
    aspect-ratio: 1 / 1;
    margin: 0 auto;
    padding: 1rem;
    position: relative;
  }

  .chart-wrapper canvas{
    width: 100% !important;
    height: auto !important;
  }
</style>