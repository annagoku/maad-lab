<script setup>
import { useStore } from '@/stores/store'
import { userService } from '@/stores/userService'
import { cartService } from '@/stores/cartService'

import {RouterView } from 'vue-router'
import NavBar from './components/NavBar.vue'
import Footer from './components/Footer.vue'
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import InlineMessage from 'primevue/inlinemessage';
import Button from 'primevue/button';
import ProgressSpinner from 'primevue/progressspinner';
const store = useStore()
</script>

<template>
  <div v-if="store.loading" class="overlay">
    <ProgressSpinner/>
  </div>
  <NavBar :title="title" />
  <!-- LOGIN DIALOG-->
  <Dialog v-model:visible="store.showLoginDialog" modal header="Login" :style="{ width: '40vw' }" >
  <div class="row mx-6">
    <InputText v-model="username" name="username" type="text" placeholder="Username" fluid />
  </div>
  <div class="row mt-2 mx-6">
    <InputText v-model="password" name="password" type="password" placeholder="Password" fluid />
  </div>
  <div class="row mt-2 mx-6">
    <Message v-if="loginErrorMessage" severity="error" size="small" variant="simple">{{ loginErrorMessage }}</Message>
  </div>
  <div class="row mt-2 mx-6">
    <Button :disabled="isLoginSubmitting" type="submit"  class="btn-primary rounded" label="Accedi" @click.stop.prevent="handleLogin"/>
  </div>
</Dialog>
  <!-- MESSAGE DIALOG-->
  <Dialog id="messageDialog" v-model:visible="store.showDialog" modal :closable="closable" header="Attenzione" :style="{ width: '50vw' }" :breakpoints="{ '960px': '75vw', '641px': '100vw' }">
    <InlineMessage severity="info" v-for="message in store.messages">
        {{ message }}
    </InlineMessage>
    <InlineMessage severity="error" v-for="alert in store.alerts">
        {{ alert }}
    </InlineMessage>
    <template #footer>
        <Button label="OK" icon="pi pi-check" @click="store.dismissMessages()" />
    </template>
  </Dialog>
  <main>
      <RouterView />
  </main>
  <Footer />
</template>

<script>

export default {
  name: "App",
  data() {
    return {
      title: "Scuderia Online E-commerce",
      closable: false,
      username: null,
      password: null,
      loginErrorMessage: null,
      isLoginSubmitting: false,
    };
  },
  mounted : function () {
    console.log("APP mounted");
    this.calculateViewPort(); 
    this.handleResize();

  },
  methods: {
      handleResize: function() {
        
        window.addEventListener("resize", 
          this.calculateViewPort
        );
      },
      calculateViewPort: function() {
        var viewport_w = window.innerWidth || document.documentElement.clientWidth;
        console.log("viewport -> "+viewport_w);
        if(viewport_w < 601) {
          useStore().setSmartPhone(true);
        }
      },
      
      async handleLogin() {
        // Clear previous error message
        this.loginErrorMessage = "";
        this.isLoginSubmitting = true;
        useStore().loading = true;
        if(this.username == "" || this.password == "") {
          this.errorMessage = "Credenziali errate";
        }
        console.log("CHIAMO IL SERVIZIO con "+this.username + " "+this.password);
        userService().login(this.username, this.password).then(() => {
          cartService().getCart().then((data) => {
            useStore().cart=data;
            this.isLoginSubmitting = false;
        
          }).catch((error) => {
            
          });
            
        });
            
        this.isLoginSubmitting = false; // Re-enable the submit button
        useStore().loading = false;
        useStore().hideLogin();

      },
  },
};
</script>

