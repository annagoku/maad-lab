import { defineStore } from 'pinia'
import axios from 'axios';
import { useStore } from '@/stores/store'

const serverBaseUrl = "http://localhost:8079/api/ordini/v1";
axios.defaults.withCredentials = false


export const storeItemService = defineStore('storeItemService', {
  state: () => ({ 
  }),
  getters: {

  },
  actions: {
  
    async getStoreItemType(){
      const url = serverBaseUrl+"/storeItemType";
      const store = useStore();
      store.loading = true;
      try {
        const response = await axios.get(url);
        store.loading = false;
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.loading = false;
        store.alerts = ["Impossibile recuperare i dati. Riprovare più tardi"];
        
      }
    }, 

    async getSizeForStoreItemType(name){
      const url = serverBaseUrl+"/storeItemType/"+name+"/size";
      const store = useStore();
      store.loading = true;
      try {
        const response = await axios.get(url);
        store.loading = false;
        return response.data;
      } catch (error) {
        store.loading = false;
        store.alerts = ["Impossibile recuperare i dati. Riprovare più tardi"];
        
      }
    }, 

    async getQuantityForStoreItemType(name,size){
      const url = serverBaseUrl+"/storeItemType/"+name+"/quantity/"+size;
      const store = useStore();
      store.loading = true;
      try {
        const response = await axios.get(url);
        store.loading = false;
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.loading = false;
        store.alerts = ["Impossibile recuperare i dati. Riprovare più tardi"];
        
      }
    }
  },
})
