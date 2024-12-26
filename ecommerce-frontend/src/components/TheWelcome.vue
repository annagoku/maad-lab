<script setup>

import { useStore } from '@/stores/store'
import { userService } from '@/stores/userService'
import { decodeCredential } from 'vue3-google-login'  // npm install vue3-google-login - fornisce il componente GoogleLogin e https://yobaji.github.io/vue3-google-login/ fornisce tutte le istruzioni

const store = useStore()

</script>

<template>
  <div class="intro-section">
    <div class="intro-bglayer">
    
<div class="slide text-left" >
  <div class="row">
  <div class="col-md-6 col-xs-12  mx-auto slide-section">
    <h1 class="display-4 font-weight-normal">Scuderia online</h1>
    <br/>
    <h2 >Equitazione:</h2>
    <p id="citazione">&ldquo;L'arte di mantenere un cavallo tra te e il terreno&rdquo; </p>
    <GoogleLogin v-if="!store.user" :callback="callback"/> 
  </div>
  <div  class="col-md-6 col-xs-0  mx-auto slide-section"></div>
</div>
</div>
</div>
</div>
<div class="home-jumbotron-spacer"></div>
</template>
<script>

export default {
  methods: {
    //login with google button
    callback: function (response) {
      const userData = decodeCredential(response.credential)
      console.log("Handle the userData", userData)
      userService().callBackFromGoogle(userData);
    }

  }
}
</script>