import { defineStore } from 'pinia'
import axios from 'axios';
import { useStore } from '@/stores/store'

const serverBaseUrl = "http://localhost:8080/api/ecommerce/v1";
axios.defaults.withCredentials = false


export const orderService = defineStore('orderService', {
  state: () => ({ 
  }),
  getters: {

  },
  actions: {

    async sendOrder(cart, paymentMethod, shippingDetails){
      const store = useStore();
      const url = serverBaseUrl+"/order/"+store.user.email;
      store.loading = true;
      try {
        const response = await axios.post(url, {
          userMail: store.user.email,
          cart: cart,
          paymentMethod: paymentMethod,
          shippingAddress: shippingDetails.address,
          shippingCity: shippingDetails.city,
          shippingProvince: shippingDetails.province,
          shippingZipCode: shippingDetails.zipcode
        });
        
        console.log(response.data);
        store.loading = false;
        
        return response.data;
      } catch (error) {
        store.loading = false;
        store.alerts = ["Impossibile salvare l'ordine. "+error.response.data.errorMessage];
        throw error;
      }
    },
    async getOrders(){
      const store = useStore();
      const url = serverBaseUrl+"/order/"+store.user.email;
      
      try {
        const response = await axios.get(url);
        
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.alerts = ["Impossibile recuperare gli ordini. Riprovare più tardi"];
        throw error;
      }
    }





  },
})
