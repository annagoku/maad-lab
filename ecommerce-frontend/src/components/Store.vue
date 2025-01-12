
<template>
  <div class="container">
    <DataView :layout="layout" :value="storeItems" >
      <template #grid="slotProps">
        <div class="grid grid-nogutter">
        <div v-for="item in slotProps.items" class="col-4 p-2">
              <div class="p-4 border-1 surface-border surface-card border-round">
                <div class="flex flex-column align-items-center gap-3 py-5">
                     <div class="md:w-10rem relative"><img class="w-10rem h-10rem shadow-2 border-round" :src="item.picturePath"/></div>
                  </div>
                  <div class="flex flex-column align-items-center gap-3 py-5">
                      <div class="text-2xl font-bold">{{ item.name }}</div>
                      <div class="text-m ">{{ item.description }}</div>
                  </div>
                  <div class="flex align-items-center justify-content-between">
                      <span class="text-2xl font-semibold">€ {{ item.price }}</span>
                      
                      <Button :disabled="!store.user" icon="pi pi-shopping-cart" rounded @click.stop.prevent="createStoreItem(item)"></Button>
                  </div>
              </div>
        </div>
        </div>
      </template>
    </DataView>   

</div>


<!-- DIALOG SCELTA STORE ITEM -->
<Dialog v-model:visible="showDialogItem" modal header="Dettagli acquisto store" :style="{ width: '50vw' }" :breakpoints="{ '960px': '75vw', '641px': '100vw' }">
      <div class="row pt-3">
        <span class="p-float-label col-xs-6 col-md-4">
              <Image :src="selectedStoreItem.storeItem.picturePath" alt="Image" width="250" preview>
                <template #indicator>
                    <i class="pi pi-check"></i>
                </template>
              </Image>
        </span>
      </div>
      <div class="row py-5">
        <span class="p-float-label col-xs-12 col-md-4 mt-3">
            <InputText :class="'w-full'" id="storeItemTypeName"  disabled v-model="selectedStoreItem.storeItem.name" />
            <label for="storeItemTypeName">Articolo</label>
        </span>
        <span  class="p-float-label col-xs-12 col-md-4 mt-3">
          <Dropdown id="size" v-model="selectedStoreItem.size" @change="getQuantityForSize($event)" :options="storeItemSize" optionLabel="size"  placeholder="Seleziona una taglia" :class="'w-full'" ></Dropdown>
                 <label for="size">Taglie disponibili</label>   
        </span>
      </div>
      <div class="row py-0 py-md-5">
        <span class="p-float-label col-xs-12 col-md-4">
          <InputNumber :disabled="!selectedStoreItem.size" v-model="selectedStoreItem.quantity" inputId="minmax" :min="1" />
          <label for="size">Quantità</label>   
        </span>
        
      </div>
      <template #footer>
        <Button class="btn-primary" icon="pi pi-check" label="Salva" @click.stop.prevent="saveStoreItem()"></Button>
        <Button class="btn-cancel" icon="pi pi-times" label="Annulla" @click.stop.prevent="cancelStoreItem()" style="margin-left: 0.5em"></Button>
      </template>
</Dialog>



<!-- DIALOG CARRELLO -->
<Dialog v-model:visible="store.showCartDialog" modal header="Carrello acquisti" :style="{ width: '75vw' }" :breakpoints="{ '960px': '75vw', '641px': '100vw' }">
  <template #header>
    <div class="row">
      <div class="col-xs-12 ">
        <span class="font-bold text-2xl mr-4">Carrello Acquisti</span>
        <Button v-if="!cartConfirmed && store.cart.items.length> 0" icon="pi pi-trash" text raised rounded severity="danger" v-tooltip="'Svuota il carrello'" aria-label="Clear" @click="clearCart" ></Button>    
      </div>
      
    </div>
    
  </template>
  <InlineMessage severity="info" v-if="store.cart.items.length== 0">Il carrello è vuoto</InlineMessage>
  


<DataTable  v-if="store.cart.items.length>0" :value="store.cart.items" >
    <Column class="smartcol" field="storeItem.name" header="Nome"></Column>
    <Column class="smartcol"  header="Immagine">
      <template #body="slotProps">
        <img class="w-5rem shadow-2 border-round" :src="slotProps.data.storeItem.picturePath"/>
      </template>
      
    </Column>
    
    <Column class="smartcol" field="size.size" header="Taglia"></Column>
    <Column class="smartcol" field="storeItem.price" header="Prezzo">
      <template #body="slotProps">
           € {{ (slotProps.data.storeItem.price) }}
        </template>
    </Column>
    <Column class="smartcol" field="quantity" header="Quantità"></Column>
    <Column class="smartcol" header="Azioni">
        <template #body="slotProps">
            <Button v-if="!cartConfirmed" icon="pi pi-trash" raised severity="danger" rounded aria-label="Cancel" @click="removeItemFromCart(slotProps.index, slotProps.data)" ></Button>
        </template>
    </Column>
    <template #footer> Prezzo totale : € {{store.cart.price }}  </template>

</DataTable> 
<Card class="mt-2" v-if="cartConfirmed">
  <template #title>Scelta modalità di spedizione e pagamento</template>
  <template #content>
  <SelectButton :options="paymentMethods" v-model="paymentMethod"></SelectButton>

  <label for="shippingAddress">Indirizzo</label>
  <InputText id="shippingAddress" v-model="shippingDetails.address"  />
  <label for="shippingCity">Città</label>
  <InputText id="shippingCity" v-model="shippingDetails.city"  />
  <label for="shippingProvince">Provincia</label>
  <InputText id="shippingProvince" v-model="shippingDetails.province"  />
  <label for="shippingZipcode">CAP</label>
  <InputText id="shippingZipcode" v-model="shippingDetails.zipcode"  />
    

  </template>
  
</Card>

    <template #footer>
        <Button label="Chiudi"  @click="closeCart"  text />
        <Button v-if="!cartConfirmed && store.cart.items.length>0" label="Acquista" @click="confirmPurchase"  autofocus />
        <Button v-if="cartConfirmed" label="Conferma Acquisto" @click="sendOrder"  autofocus />
    </template>
</Dialog>







</template>
<script>
//import {DayPilot} from '@daypilot/daypilot-lite-vue'
import TabView from 'primevue/tabview';
import Tag from 'primevue/tag';
import TabPanel from 'primevue/tabpanel';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Dialog from 'primevue/dialog'; 
import SelectButton from 'primevue/selectbutton'; 
import Calendar from 'primevue/calendar';
import Badge from 'primevue/badge';
import DataView from 'primevue/dataview';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
//import DataViewLayoutOptions from 'primevue/dataviewlayoutoptions';
import Card from 'primevue/card';
import { useStore } from '@/stores/store';
import { orderService } from '@/stores/orderService';
import { storeItemService } from '@/stores/storeItemService';
import InputNumber from 'primevue/inputnumber';
//import {format, parse, add, lastDayOfMonth, startOfHour} from 'date-fns';
import InlineMessage from 'primevue/inlinemessage';
import Image from 'primevue/image';
import Dropdown from 'primevue/dropdown';


export default {
  name: "Store",
  data() {
    return {
      layout: 'grid',
      showDialog: false,
      showDialogItem: false,
      viewCart:false,
      cartConfirmed: false,
      itemToBuy: null,
      selectedStoreItem:{
        storeItem:null,
        size:null,
        quantity:null
      },
      storeItemSize: [],
      storeItems: [],
      paymentMethods: ['Carta di credito', 'Bonifico','In contrassegno'],
      paymentMethod: null,
      shippingDetails: {
        address: null,
        city: null,
        province: null,
        zipcode: null
      },
      

    };
  },
  setup() {
    const store = useStore();
    return {store};
  },
  components: {
    DataView,Card, InputNumber, TabPanel, TabView, Button, Dialog, InputText, Calendar, Badge, Tag, DataTable, Column , InlineMessage, SelectButton,Image,Dropdown
  },
  props: {
     
  },

  mounted : function () {
    
    
    storeItemService().getStoreItem().then((data)=> {
      this.storeItems = data;
      //this.storeItems = [{name: "Mug", picturePath: "/storeitems/mug.png", price: "8" }]
      
      console.log("this.storeItems");
      console.log(this.storeItems);
    });

    console.log("smartphone"+useStore().smartphone);
      
  },
  methods: {
    
    createStoreItem: function(s){
      if(!this.cartConfirmed) {
        storeItemService().getSizeForStoreItem(s.storeItemId).then((data)=>{
          this.storeItemSize=data;
        }).catch(e=>{});



        console.log("stampo l'item selezionato");
        console.log(s);
        console.log(s.name);
        this.selectedStoreItem={
          size:null,
          quantity: 1,
          storeItem: null
        };
        this.selectedStoreItem.storeItem=s;

        if(this.storeItemSize.length == 1) {
          this.selectedStoreItem.size = this.storeItemSize[0];
        }
        console.log(this.selectedStoreItem);
        this.showDialogItem=true;

      }else {
        useStore().alerts = ["Il carrello è confermato in attesa di pagamento"];
      }

    },


    getQuantityForSize: function(event){
      var size=this.selectedStoreItem.size;
      console.log("size selected ");
      console.log(size);
      if(size.stockQuantity==0) {
        this.selectedStoreItem.quantity =0;
      }
    },
    closeCart : function() {
      this.cartConfirmed = false;
      this.paymentMethod = null;
      this.shippingDetails= {
        address: null,
        city: null,
        province: null,
        zipcode: null
      };
      useStore().hideCart();
    },
   
    
    saveStoreItem : function(){
      console.log("SAVE ITEM TO CART");
      console.log(this.selectedStoreItem);
      
      var size=this.selectedStoreItem.size;
      console.log("size quantity "+size.stockQuantity);
      var quantity=this.selectedStoreItem.quantity;
      console.log("input quantity "+quantity);
      
      if(quantity > size.stockQuantity) {
        this.selectedStoreItem.quantity = size.stockQuantity;
        useStore().alerts = ["Attenzione la quantità massima ordinabile è "+size.stockQuantity];
        return;
      }
     
      this.showDialogItem = false;
      if(!this.cartConfirmed){
        useStore().addItemToCart(this.selectedStoreItem);
      }else {
        useStore().alerts = ["Il carrello è confermato in attesa di pagamento"];
      }
      this.selectedStoreItem={
          size:null,
          quantity: null,
          storeItem:null
        };
    },

    
    removeItemFromCart: function(index, item){
      console.log("REMOVE ITEM from CART index "+index);
      useStore().removeItemFromCart(index, item);
      
    },

    
    cancelStoreItem: function(){
      this.showDialogItem=false;
      this.newStoreItem={
          size:null,
          quantity: null,
          storeItem:null
        };

    },

    showCart: function() {
      console.log("CART");
      console.log(useStore().cart);
      this.viewCart = true;

    },

    hideCart: function() {
      this.viewCart = false;

    },
    clearCart: function() {

      useStore().clearCart();
      this.viewCart = false;

    },
    confirmPurchase: function () {
      console.log("confirm purchase");
      this.paymentMethod = null;
      this.cartConfirmed = true;
      this.shippingDetails= {
        address: null,
        city: null,
        province: null,
        zipcode: null
      };

    },

  

    sendOrder: function() {
      if(this.paymentMethod == null) {
        useStore().alerts = ["Selezionare un methodo di pagamento"];
        return;
      }
      orderService().sendOrder(useStore().cart, this.paymentMethod, this.shippingDetails).then((data) => {
        this.paymentMethod = null;
        this.cartConfirmed = false;
        this.shippingDetails= {
          address: null,
          city: null,
          province: null,
          zipcode: null
        };
        var msg="Pagamento andato a buon fine. L'ordine "+data.orderId+" è consultabile nella sezione Ordini";
        
        useStore().clearCartAfterOrder();
        useStore().hideCart();
        useStore().messages = [msg];
      });

    },

    getSeverity(storeItem) {
      var x=storeItem.availability;
      switch (true) {
          case (x>=10):
              return 'success';

          case (x<10):
              return 'warning';

          case 0:
              return 'danger';

          default:
              return null;
      }
    },   
        
  },
};
</script>

