<template>
<Card id="login">
  <template #title>Login</template>
  <template #content>
  <div class="row mx-6">
    <InputText v-model="username" name="username" type="text" placeholder="Username" fluid />
  </div>
  <div class="row mt-2 mx-6">
    <InputText v-model="password" name="password" type="password" placeholder="Password" fluid />
  </div>
  <div class="row mt-2 mx-6">
    <Message v-if="errorMessage" severity="error" size="small" variant="simple">{{ errorMessage }}</Message>
  </div>
  <div class="row mt-2 mx-6">
    <Button :disabled="isSubmitting" type="submit"  class="btn-primary rounded" label="Accedi" @click.stop.prevent="handleLogin"/>
  </div>
</template>               
</Card>
</template>
<script>
import { useStore } from '@/stores/store'
import { userService } from '@/stores/userService'
import { cartService } from '@/stores/cartService'
import Card from 'primevue/card';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

export default {
  name: "Login",
  data() {
    return {
        username: null,
        password: null,
        errorMessage: null,
        isSubmitting: false,
      
    };
  },
  setup () {
    const store = useStore();

    return {
      store, userService
    };
  },
  components: {
    Card, InputText, Button
  },
  props: {
     
  },
  mounted : function () {
     

  },
  methods: {
    async handleLogin() {
      // Clear previous error message
      this.errorMessage = "";
      this.isSubmitting = true;
      this.store.loading = true;
      if(this.username == "" || this.password == "") {
        this.errorMessage = "Credenziali errate";
      }
      console.log("CHIAMO IL SERVIZIO con "+this.username + " "+this.password);
      userService().login(this.username, this.password).then(() => {
        cartService().getCart().then((data) => {
          this.store.cart=data;
          
        }).catch((error) => {
          this.store.clearCart();
        });
          
      });
          
      this.isSubmitting = false; // Re-enable the submit button
      this.store.loading = false;
    },
  },
};
</script>