<script setup>
import TheWelcome from '../components/TheWelcome.vue'
import Contatti from '../components/Contatti.vue'
import Store from '../components/Store.vue'
import fascia from '@/assets/img/fascia-federazioni.png' // => or relative path
import { useStore } from '@/stores/store'
</script>

<template>
    <TheWelcome />
    <div id="start-change"></div>
    <Store />
    <div class="w-100 py-7"></div>
    <div class="container w-100 mx-auto py-4 text-center">
      <img :src="fascia" style="width: 70vw">
    </div>
    <div class="w-100 py-4"></div>
    <Contatti />
  
</template>
<script>
export default {
  name: "HomeView",
  data() {
    return {
    };
  },

  setup () {
    const store = useStore();
    return {store};
  },

  mounted : function () {
      
  },
  methods: {
      
  },
};
</script>

