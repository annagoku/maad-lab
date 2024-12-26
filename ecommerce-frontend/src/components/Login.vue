<template>
<Card id="login">
  <template #title>Login</template>
  <template #content>
  <div class="row mx-6">
    <InputText name="username" type="text" placeholder="Username" fluid />
  </div>
  <div class="row mt-2 mx-6">
    <InputText name="password" type="password" placeholder="Password" fluid />
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

      // Dummy login validation (replace with your actual API call)
      try {
        if (this.username === "admin" && this.password === "password123") {
          // Simulate successful login
          this.$router.push("/dashboard"); // Redirect to dashboard or home page
        } else {
          this.errorMessage = "Invalid username or password.";
        }
      } catch (error) {
        // Handle error (e.g., from API call)
        this.errorMessage = "Something went wrong. Please try again later.";
      } finally {
        this.isSubmitting = false; // Re-enable the submit button
      }
    },
  },
};
</script>