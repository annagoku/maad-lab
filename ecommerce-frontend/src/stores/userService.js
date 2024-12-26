import { defineStore } from 'pinia'
import axios from 'axios';
import { useStore } from '@/stores/store'

const serverBaseUrl = "http://localhost:8079/api/anagrafiche/v1";
axios.defaults.withCredentials = false


export const userService = defineStore('userService', {
  state: () => ({ 
  }),
  getters: {
    
  },
  actions: {
    async callBackFromGoogle(googleData) {
      const store = useStore();
      var u = {
        name: googleData.given_name,
        surname: googleData.family_name,
        email:  googleData.email,
        picture:  googleData.picture
      };
      const url = serverBaseUrl+"/user/create";
      
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
    async updateUser (user){
      const url = serverBaseUrl+"/user/"+user.email+"/update";
      const store = useStore();
      try {
        console.log("Chiamo "+url+" passando");
        console.log(user);
        const response = await axios.post(url, user);
        store.setUser(response.data);
        store.addMessage("Salvataggio avvenuto con successo")
      } catch (error) {
        store.alerts = ["Si è verificato un errore durante l'aggiornamento. Riprovare più tardi."];
      }
    },

    async updateUserAdmin (user){
      const url = serverBaseUrl+"/user/"+user.email+"/update";
      const store = useStore();
      try {
        console.log("Chiamo "+url+" passando");
        console.log(user);
        const response = await axios.post(url, user);
        store.addMessage("Salvataggio avvenuto con successo")
      } catch (error) {
        store.alerts = ["Si è verificato un errore durante l'aggiornamento. Riprovare più tardi."];
      }
    },

    async createNewUserAdmin (user){
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
    },

    

    async getAllUsers(){
      const url = serverBaseUrl+"/users";
      const store = useStore();
      try {
        console.log("Chiamo "+url);
        
        const response = await axios.get(url);
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.alerts = ["Si è verificato un errore durante il recupero dati. Riprovare più tardi."];
      }

    },
    async getAllTeachers(){
      const url = serverBaseUrl+"/users/findByRoleId/1";
      const store = useStore();
      try {
        console.log("Chiamo "+url);
        
        const response = await axios.get(url);
        console.log(response.data)
        const result = response.data;

        result.forEach((t) => {
          t["nominativo"]=t.name+" "+t.surname
        })


        return result;
      } catch (error) {
        store.alerts = ["Si è verificato un errore durante il recupero dati. Riprovare più tardi."];
      }

    },
    async getAllRoles(){
      const url = serverBaseUrl+"/roles";
      const store = useStore();
      try {
        console.log("Chiamo "+url);
        
        const response = await axios.get(url);
        console.log(response.data)

        return response.data;
      } catch (error) {
        store.alerts = ["Si è verificato un errore durante il recupero dati. Riprovare più tardi."];
      }

    }
  },

})
