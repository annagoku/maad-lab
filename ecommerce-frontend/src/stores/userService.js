import { defineStore } from 'pinia'
import axios from 'axios';
import { useStore } from '@/stores/store'

const serverBaseUrl = "http://localhost:8080/api/ecommerce/v1";
axios.defaults.withCredentials = false


export const userService = defineStore('userService', {
  state: () => ({ 
  }),
  getters: {
    
  },
  actions: {
    
    async login(email, pwd) {
      const store = useStore();
      var u = {
        email: email,
        password: pwd
      };
      const url = serverBaseUrl+"/user/"+email+"/login";
      
      console.log("Chiamo "+url+" passando");
      console.log(u);
      try {
        const response = await axios.post(url, u);
        console.log("response");
        console.log(response);
        store.setUser(response.data);  
      } catch (error) {
        store.alerts = ["Si è verificato un errore durante il login. Riprovare più tardi."];
      }
      
    },
    async createNewUser (user){
      const url = serverBaseUrl+"/user/create";
      const store = useStore();
      try {
        console.log("Chiamo "+url+" passando");
        console.log(user);
        const response = await axios.post(url, user);
        store.addMessage("Salvataggio avvenuto con successo")
      } catch (error) {
        store.alerts =["Errore nel salvataggio dell'utente: "+error.response.data.errorMessage];
      }
    }
  },

})
