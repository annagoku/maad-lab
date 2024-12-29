<script setup>
import TheWelcome from '../components/TheWelcome.vue'
import Contatti from '../components/Contatti.vue'
import Store from '../components/Store.vue'
import { useStore } from '@/stores/store'
</script>

<template>
    <TheWelcome />
    <div id="start-change"></div>
    <Store />
    <div class="w-100 py-7"></div>
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
  components: {
    TheWelcome,Store, Contatti
  },
  mounted : function () {
      
  },
  methods: {
      
  },
};
</script>

