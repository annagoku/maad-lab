import { defineStore } from 'pinia'
import axios from 'axios';
import { useStore } from '@/stores/store'

const serverBaseUrl = "http://localhost:8080/api/ecommerce/v1";
axios.defaults.withCredentials = false


export const cartService = defineStore('cartService', {
  state: () => ({ 
  }),
  getters: {

  },
  actions: {
  
    async getCart(){
      const store = useStore();
      const url = serverBaseUrl+"/cart/"+store.user.email;
      store.loading = true;
      try {
        const response = await axios.get(url);
        store.loading = false;
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.loading = false;
        if (error.response && error.response.status === 404) {
          
            console.log("carrello non trovato per l'utente");
          
   
        }
        else {
          store.alerts = ["Impossibile recuperare i dati del carrello. Riprovare più tardi"];
        
        } 
        throw error;
        
      }
    }, 
    async updateCart(cart){
      const store = useStore();
      const url = serverBaseUrl+"/cart/"+store.user.email;
      cart.userId = store.user.email;
      store.loading = true;
      try {
        const response = await axios.post(url, cart);
        
        console.log(response.data);
        store.loading = false;
        return response.data;
      } catch (error) {
        store.loading = false;
        store.alerts = ["Impossibile aggiornare il carrello "+error.response.data.errorMessage];
        throw error;
      }
    },
    

    
  },
})
