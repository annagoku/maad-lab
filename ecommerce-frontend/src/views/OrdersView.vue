<script setup>

</script>
<template >
  <div class="container">
      <Card>
        <template #title>
          <div class="row w-100">
            <span class="col">Ordini</span>
            <span class="relative col "><Button class="absolute right-0" @click="refresh()" icon="pi pi-refresh" aria-label="Refresh" /></span>
          </div>
        </template>
        <template #content>
          <DataTable :value="orders" v-model:filters="filters"  dataKey="order.orderId"
                       :loading="loadingTable"  filterLocale="it"
                        @rowExpand="onRowExpand" @rowCollapse="onRowCollapse" v-model:expandedRows="expandedRows"
                      resizableColumns columnResizeMode="fit" >
            <template #header>
                    
                <div v-if="!store.smartphone" class="flex justify-content-end">
                  <Button type="button" class="mr-5" icon="pi pi-filter-slash" label="Annulla filtro" outlined @click="initFilters()" />
                  <span class="p-input-icon-left">
                        <i class="pi pi-search" />
                        <InputText v-model="filters['global'].value" placeholder="Cerca..." />
                    </span>
                </div>
            </template>
            <template #empty> Nessun Ordine trovato </template>
            <Column class="smartcol" expander style="width: 5rem" />   
            <Column class="smartcol" field="order.orderId" header="N. Ordine" sortable ></Column>
            <Column class="smartcol" field="order.timestampFormatted" header="Data"  sortable ></Column>
            <Column class="smartcol" v-if="store.user.isAdmin" field="user.nominativo" header="Utente" sortable></Column>
            <Column class="smartcol" field="order.price" header="Totale €" sortable ></Column>
            <template #expansion="slotProps">
              <div class="p-3">
                  <h4>Dettaglio ordine {{ slotProps.data.order.orderId }}</h4>
                  <h5 v-if="slotProps.data.order.subscriptions.length >0">Abbonamenti</h5>
                  <DataTable v-if="slotProps.data.order.subscriptions.length >0" :value="slotProps.data.order.subscriptions" >
                      <Column field="subscriptionId" header="Id" sortable></Column>
                      <Column field="startDate" header="Data Inizio" sortable></Column>
                      <Column field="endDate" header="Data Fine" sortable></Column>
                      <Column field="subscriptionType.subscriptionName" header="Tipo" sortable></Column>
                      <Column field="subscriptionType.price" header="Prezzo" sortable></Column>
                  </DataTable>
                  <h5 class="mt-1" v-if="slotProps.data.order.items.length >0">Articoli</h5>
                  <DataTable v-if="slotProps.data.order.items.length >0" :value="slotProps.data.order.items" >
                      <Column field="storeItemId" header="Id" sortable></Column>
                      <Column field="name" header="Nome Articolo" sortable></Column>
                      <Column field="size" header="Taglia" sortable></Column>
                      <Column field="quantity" header="Quantità" sortable></Column>
                      <Column field="unitPrice" header="Prezzo Unitario" sortable></Column>
                      <Column field="price" header="Prezzo Totale" sortable></Column>
                  </DataTable>
              </div>
            </template>
          </DataTable>
        </template>
    </Card>
        


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
import {orderService} from '@/stores/orderService';
import InputSwitch from 'primevue/inputswitch';
import MultiSelect from 'primevue/multiselect';




export default {
  name: "OrdersView",
  data() {
    return {
      orders:[],
      expandedRows:[],
      filters: null,
      loadingTable:true,
      
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
    
  },
  methods: {
    
    initFilters: function () {
      this.filters = {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
      };
    },
    onRowExpand: function(event) {
      return;
    },
    onRowCollapse: function(event) {
      return;
    },
    
    refresh: function(){
      if(this.store.user.isAdmin) {
        orderService().getAdminOrders().then((data) => {
          this.orders=data;
          this.orders.forEach((el) => {
            el.order["timestampFormatted"]=format(new Date(el.order.timeStamp), 'dd/MM/yyyy HH:mm');
          })
          this.loadingTable=false;
          this.initFilters();
        }).catch(e => {
            
        });
      }
      else {
        orderService().getOrders().then((data) => {
          this.orders=data;
          this.orders.forEach((el) => {
            el.order["timestampFormatted"]=format(new Date(el.order.timeStamp), 'dd/MM/yyyy HH:mm');
          })
          this.loadingTable=false;
          this.initFilters();
        }).catch(e => {
            
        });
      }
       

    },
    


   
        
  },
};
</script>

