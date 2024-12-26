import { defineStore } from 'pinia'
import axios from 'axios';
import { useStore } from '@/stores/store'

const serverBaseUrl = "http://localhost:8079/api/ordini/v1";
axios.defaults.withCredentials = false


export const orderService = defineStore('orderService', {
  state: () => ({ 
  }),
  getters: {

  },
  actions: {

    async sendOrder(order){
      const store = useStore();
      const url = serverBaseUrl+"/user/"+store.user.email+"/newOrder";
      store.loading = true;
      try {
        const response = await axios.post(url, order);
        
        console.log(response.data);
        store.loading = false;
        order["orderId"] = response.data.orderId;
        return response.data;
      } catch (error) {
        store.loading = false;
        store.alerts = ["Impossibile salvare l'ordine. "+error.response.data.errorMessage];
        throw error;
      }
    },
    async orderPayment(orderId, paymentMethod){
      const store = useStore();
      const url = serverBaseUrl+"/user/"+store.user.email+"/order/"+orderId+"/payment";
      store.loading = true;
      try {
        const response = await axios.post(url, {
          paymentMethod: paymentMethod
        });
        store.loading = false;
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.loading = false;
        store.alerts = ["Attenzione errore nel pagamento dell'ordine: "+error.response.data.errorMessage];
        throw error;
      }
    },
    async getOrders(){
      const store = useStore();
      const url = serverBaseUrl+"/user/"+store.user.email+"/orders";
      
      try {
        const response = await axios.get(url);
        
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.alerts = ["Impossibile recuperare gli ordini. Riprovare più tardi"];
        throw error;
      }
    },   
    async getAdminOrders(){
      const store = useStore();
      const url = serverBaseUrl+"/admin/orders";
     
      try {
        const response = await axios.get(url);
        
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.alerts = ["Impossibile recuperare gli ordini. Riprovare più tardi"];
        throw error;
      }
    },
    





  },
})
