<script setup>

</script>
<template >
  <div class="container">
      <Card>
        <template #title>
          <div class="row w-100">
            <span class="col">Dati Utenti</span>
            <span class="relative col "><Button class="absolute right-0" @click="refresh()" icon="pi pi-refresh" aria-label="Refresh" /></span>
          </div>
        
        </template>
        <template #content>
          <DataTable :value="users" v-model:filters="filters" 
                       :loading="loadingTable"  filterLocale="it"
                      resizableColumns columnResizeMode="fit"  tableStyle="min-width: 50rem" >
            <template #header>
                    
                <div class="flex justify-content-end">
                  <Button type="button" class="mr-5" icon="pi pi-filter-slash" label="Annulla filtro" outlined @click="initFilters()" />
                  <span class="p-input-icon-left">
                        <i class="pi pi-search" />
                        <InputText v-model="filters['global'].value" placeholder="Cerca..." />
                    </span>
                </div>
            </template>
            <template #empty> Nessun utente trovato </template>
                     
              <Column field="name" header="Nome" sortable ></Column>
              <Column field="surname" header="Cognome" sortable >           
              </Column>
              <Column field="birthdate" header="Data di nascita" sortable dateFormat="dd/mm/yy"></Column>
              <Column field="email" header="email"  ></Column>
              <Column field="qualification" header="Qualifica" sortable></Column>
              <Column v-if="store.user.isAdmin" header="Ruolo">
                <template #body="slotProps">
                    <span>
                      <Chip v-for="role in slotProps.data.roles" :label="role.roleName"></Chip>
                    </span>
                </template>
              </Column>
              <Column field="active" header="Stato" dataType="boolean" bodyClass="text-center" style="min-width: 8rem">
                <template #body="slotProps">
                    <i class="pi" :class="{ 'pi-check-circle text-green-500 ': slotProps.data.active, 'pi-times-circle text-red-500': !slotProps.data.active }"></i>
                </template>
              </Column>
              <Column header="Azioni">
                  <template #body="slotProps">
                      <Button type="button" icon="pi pi-pencil" rounded class="mr-2" @click.stop.prevent="showDialogToUpdate(slotProps.data)"></Button>
                  </template>
              </Column>
          </DataTable>
        </template>
        <template #footer>
          <div class="row w-100">
            <span class="relative col ">  <Button type="button" icon="pi pi-plus" severity="info"  @click.stop.prevent="showDialogToUpdate(newUser)" raised rounded  size="large" style="margin-left: 60em"> </Button></span>
          </div>
        </template>
    </Card>
        

            <Dialog v-model:visible="visible"  modal :header="this.dialogHeader" :style="{ width: '50vw' }">
                
                <div class="row pt-3">
                    <span class="p-float-label col-xs-6 col-md-4">
                        <InputText styleClass="w-full" id="name"  v-model="selectedUser.name" :disabled="selectedUser.name"  class="w-full md:w-14rem"/>
                        <label for="name">Nome</label>
                    </span>
                    <span class="p-float-label col-xs-6 col-md-4">
                        <InputText styleClass="w-full" id="surname"  v-model="selectedUser.surname" :disabled="selectedUser.surname"  class="w-full md:w-14rem"/>
                        <label for="surname">Cognome</label>
                    </span>
                    <span class="p-float-label col-xs-6 col-md-4">
                      <Calendar styleClass="w-full" id="birthdate" v-model="userBirthdate" showIcon dateFormat="dd/mm/yy" />
                        <label for="userBirthdate">Data di nascita</label>
                    </span>
                  </div>
                  <div class="row py-5">
                    <span class="p-float-label col-xs-6 col-md-4 ">
                        <InputText styleClass="w-full" id="email"  v-model="selectedUser.email" :disabled="selectedUser.email" class="w-full md:w-14rem"/>
                        <label for="email">Email</label>
                    </span>
                    <span class="p-float-label col-xs-6 col-md-4">
                      <Dropdown id="qualification" v-model="selectedUser.qualification" :options="qualificationFise" placeholder="Seleziona una qualifica FISE" class="w-full md:w-14rem" />
                      <label for="qualification">Qualifica</label>   
                    </span>
                    <span class="p-float-label col-xs-6 col-md-4">
                      <MultiSelect id="roles" v-model="selectedUser.roleValues"  :options="roles"
                       optionLabel="roleName" optionValue="roleId" placeholder="Seleziona i ruoli" class="w-full md:w-14rem" />
                      <label for="roles">Ruoli</label>   
                    </span>
                  </div>
                  <div class="row py-5">
                    <span class="p-float-label col-xs-6 col-md-4">
                      Non Attivo <InputSwitch v-model="selectedUser.active" class="ml-1 mr-1"></InputSwitch> Attivo
                    </span>           
                      
                  </div>
                  <div class="row py-5">
                    <span class="p-float-label col-xs-6 col-md-4">
                      <Textarea styleClass="w-full" id="descritpion" v-model="selectedUser.description" rows="5" cols="50" ></Textarea>
                        <label for="description">Descrizione</label>
                    </span>
         
                  </div>
                  <template #footer>
                    <Button class="btn-primary"  icon="pi pi-check" label="Salva" @click.stop.prevent="save()"></Button>
                    <Button class="btn-cancel" icon="pi pi-times" label="Annulla" @click="cancel()" style="margin-left: 0.5em"></Button>
                  </template>
            </Dialog>

  </div>
      

</template>
<script>
import Chip from 'primevue/chip';
import Card from 'primevue/card';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Textarea from 'primevue/textarea';
import { useStore } from '@/stores/store';
import Calendar from 'primevue/calendar';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import ColumnGroup from 'primevue/columngroup';   // optional
import Row from 'primevue/row';  
import Dialog from 'primevue/dialog'; 
import Dropdown from 'primevue/dropdown';
import {format, parse} from 'date-fns';
import { ref, onMounted } from 'vue';
import { FilterMatchMode} from 'primevue/api';
import {userService} from '@/stores/userService';
import InputSwitch from 'primevue/inputswitch';
import MultiSelect from 'primevue/multiselect';




export default {
  name: "UsersView",
  data() {
    return {
      users:[],
      roles : [],
      filters: null,
      dialogHeader:null,
      loadingTable:true,
      visible: false,
      selectedUser:null,
      userBirthdate:null,
      qualificationFise: ["Patente Ludica", "Brevetto 1° Livello", "Istruttore 1°  Livello", "Istruttore 2° Livello"],
      newUser:null
      
    };    
      
   
  },
  setup() {
    const store = useStore();
    return {store};
    
  },
  created() {
        this.initFilters();
  },
  components: {
    Card, InputText, Button, Textarea, Calendar, DataTable, Column, ColumnGroup, Row, Dialog, Dropdown, Chip, InputSwitch, MultiSelect
  },
  props: {
     
  },

  mounted : function () {
    this.refresh();
    this.getRoles();  
  },
  methods: {
    showDialogToUpdate: function(user) {
      this.selectedUser={...user};
      
      if(user){
            this.dialogHeader="Modifica dati utente"
            this.selectedUser["isNew"]=false;
            if(this.selectedUser.userBirthdate) {
              this.userBirthdate = parse(this.selectedUser.birthdate, 'dd/MM/yyyy', new Date());
            
            }
            if(this.selectedUser.roles && this.selectedUser.roles.length > 0) {
              this.selectedUser["roleValues"] = [];
              this.selectedUser.roles.forEach((r) => {
                this.selectedUser.roleValues.push(r.roleId);
              });
            }
       }
       else {
        this.selectedUser.isNew=true;
        this.dialogHeader="Inserisci un nuovo utente"
       }
            this.visible=true;
            console.log("Selected User: ");
            console.log(this.selectedUser);
      

    },



    initFilters: function () {
      this.filters = {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
      };
    },

    save: function () {
      console.log("SAVE USER");

    
      if(this.userBirthdate) {
        this.selectedUser.birthdate = format(this.userBirthdate, 'dd/MM/yyyy');
      }
      if(this.selectedUser.roleValues) {
        this.selectedUser.roles = [];
        this.selectedUser.roleValues.forEach((id) => {
          var role = {roleId: id};
          this.selectedUser.roles.push(role);
        });
      }
      console.log(this.selectedUser);
      
      if(!this.selectedUser.isNew){
      userService().updateUserAdmin (this.selectedUser).then(() => {
        this.visible=false;
        this.refresh(); 
      })
      }

      if(this.selectedUser.isNew){
        userService().createNewUserAdmin (this.selectedUser).then(()=>{
          
          this.visible=false;
          
          this.refresh(); 
         })
      }
         
    },
    cancel: function () {
      this.visible=false;
    },

    refresh: function(){
      userService().getAllUsers().then((data) => {
        this.users=data;
        this.loadingTable=false;
        this.initFilters();
      }).catch(e => {
          
      }); 

    },
    getRoles: function () {
      userService().getAllRoles().then((data) => {
        this.roles = data;


        console.log("Roles");
        console.log(this.roles);
      }).catch(e => {
          
      });
    }



   
        
    },
};
</script>

