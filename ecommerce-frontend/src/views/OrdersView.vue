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
          <DataTable :value="orders"   dataKey="orderId"
                       :loading="loadingTable"  
                        @rowExpand="onRowExpand" @rowCollapse="onRowCollapse" v-model:expandedRows="expandedRows"
                      resizableColumns columnResizeMode="fit" >
            <template #empty> Nessun Ordine trovato </template>
            <Column class="smartcol" expander style="width: 5rem" />   
            <Column class="smartcol" field="orderId" header="N. Ordine" sortable ></Column>
            <Column class="smartcol" field="timestampFormatted" header="Data"  sortable ></Column>
            <Column class="smartcol" field="price" header="Totale €" sortable ></Column>
            <template #expansion="slotProps">
              <div class="p-3">
                  <h4>Dettaglio ordine {{ slotProps.data.orderId }}</h4>
                  <DataTable v-if="slotProps.data.items.length >0" :value="slotProps.data.items" >
                      <Column field="storeItem.storeItemId" header="Id" sortable></Column>
                      <Column field="storeItem.name" header="Nome Articolo" sortable></Column>
                      <Column field="size" header="Taglia" sortable></Column>
                      <Column field="quantity" header="Quantità" sortable></Column>
                      <Column field="storeItem.price" header="Prezzo Unitario" sortable></Column>
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
      loadingTable:true,
      
    };    
      
   
  },
  setup() {
    const store = useStore();
    return {store};
    
  },
  created() {
        
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
    
    onRowExpand: function(event) {
      return;
    },
    onRowCollapse: function(event) {
      return;
    },
    
    refresh: function(){
      orderService().getOrders().then((data) => {
        this.orders=data;
        this.orders.forEach((el) => {
          el["timestampFormatted"]=format(new Date(el.timeStamp), 'dd/MM/yyyy HH:mm');
        })
        this.loadingTable=false;
        
      }).catch(e => {
          
      });
      

    },
    


   
        
  },
};
</script>

