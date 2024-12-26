
<template>
  <div class="container">
    <DataView :layout="layout" :value="storeItems" >
      <template #grid="slotProps">
        
        <div class="col-12 sm:col-6 lg:col-12 xl:col-4 p-2">
              <div class="p-4 border-1 surface-border surface-card border-round">
                <div class="flex flex-column align-items-center gap-3 py-5">
                      <div class="text-2xl font-bold"><img class="w-10rem shadow-2 border-round" :src="slotProps.data.picturePath"/></div>
                  </div>
                  <div class="flex flex-column align-items-center gap-3 py-5">
                      <div class="text-2xl font-bold">{{ slotProps.data.name }}</div>
                  </div>
                  <div class="flex align-items-center justify-content-between">
                      <span class="text-2xl font-semibold">€ {{ slotProps.data.price }}</span>
                      
                      <Button icon="pi pi-shopping-cart" rounded @click.stop.prevent="createStoreItem(slotProps.data)"></Button>
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
              <Image :src="selectedItemType.picturePath" alt="Image" width="250" preview>
                <template #indicator>
                    <i class="pi pi-check"></i>
                </template>
              </Image>
        </span>
      </div>
      <div class="row py-5">
        <span class="p-float-label col-xs-12 col-md-4 mt-3">
            <InputText :class="'w-full'" id="storeItemTypeName"  disabled v-model="selectedItemType.name" />
            <label for="storeItemTypeName">Articolo</label>
        </span>
        <span  class="p-float-label col-xs-12 col-md-4 mt-3">
          <Dropdown id="size" @change="getQuantityForStoreItem($event)" v-model="newStoreItem.size"  :options="storeItemSize"  placeholder="Seleziona una taglia" :class="'w-full'" ></Dropdown>
                 <label for="size">Taglie disponibili</label>   
        </span>
      </div>
      <div class="row py-0 py-md-5">
        <span class="p-float-label col-xs-12 col-md-4">
          <Dropdown id="quantity" v-model="newStoreItem.quantity" :disabled="!newStoreItem.size" :options="storeItemQuantity" placeholder="Seleziona una quantità" :class="'w-full'"/>
                 <label for="size">Quantità</label>   
        </span>
        
      </div>
      <template #footer>
        <Button class="btn-primary" icon="pi pi-check" label="Salva" @click.stop.prevent="saveStoreItem()"></Button>
        <Button class="btn-cancel" icon="pi pi-times" label="Annulla" @click.stop.prevent="cancelStoreItem()" style="margin-left: 0.5em"></Button>
      </template>
</Dialog>

<!-- DIALOG CARRELLO -->
<Dialog v-model:visible="viewCart" modal header="Carrello acquisti" :style="{ width: '75vw' }" :breakpoints="{ '960px': '75vw', '641px': '100vw' }">
  <template #header>
    <div class="row">
      <div class="col-xs-12 ">
        <span class="font-bold text-2xl mr-4">Carrello Acquisti</span>
        <Button v-if="!cartConfirmed && store.cart.itemNumber> 0" icon="pi pi-trash" text raised rounded severity="danger" v-tooltip="'Svuota il carrello'" aria-label="Clear" @click="clearCart" ></Button>    
      </div>
      
    </div>
    
  </template>
  <InlineMessage severity="info" v-if="store.cart.itemNumber== 0">Il carrello è vuoto</InlineMessage>
  


<DataTable  v-if="store.cart.items.length>0" :value="store.cart.items" >
    <Column class="smartcol" v-if="!store.smartphone" field="storeItemType.name" header="Nome"></Column>
    <Column class="smartcol" v-if="!store.smartphone"  header="Immagine">
      <template #body="slotProps">
        <img class="w-5rem shadow-2 border-round" :src="slotProps.data.storeItemType.picturePath"/>
      </template>
      
    </Column>
    <Column class="smartcol" v-if="store.smartphone" header="Articolo">
      <template #body="slotProps">
        <img class="w-2rem shadow-2 border-round" :src="slotProps.data.storeItemType.picturePath"/><br/>
        {{ (slotProps.data.storeItemType.name) }} - taglia {{ (slotProps.data.size) }} - quantità {{ (slotProps.data.quantity) }}  
      </template>
      
    </Column>
    <Column class="smartcol" v-if="!store.smartphone" field="size" header="Taglia"></Column>
    <Column class="smartcol" field="storeItemType.price" header="Prezzo">
      <template #body="slotProps">
           € {{ (slotProps.data.storeItemType.price) }}
        </template>
    </Column>
    <Column class="smartcol" v-if="!store.smartphone" field="quantity" header="Quantità"></Column>
    <Column class="smartcol" header="Azioni">
        <template #body="slotProps">
            <Button v-if="!cartConfirmed" icon="pi pi-trash" raised severity="danger" rounded aria-label="Cancel" @click="removeItemFromCart(slotProps.index, slotProps.data)" ></Button>
        </template>
    </Column>
    <template #footer> Prezzo totale accessori e abbigliamento: € {{store.cart.totalPriceItem }}  </template>

</DataTable>

<Card v-if="store.cart.itemNumber> 0"><template #title> Totale carrello: <span class="text-2xl">€ {{store.cart.globalTotalPrice }}</span> </template></Card>
  
<Card class="mt-2" v-if="cartConfirmed">
  <template #title>Pagamento </template>
  <template #content>
  <SelectButton :options="paymentMethods" v-model="paymentMethod"></SelectButton>

  </template>
  
</Card>

    <template #footer>
        <Button label="Annulla"  @click="hideCart"  text />
        <Button v-if="!cartConfirmed && store.cart.itemNumber> 0" label="ACQUISTA" @click="confirmPurchase"  autofocus />
        <Button v-if="cartConfirmed" label="Conferma Pagamento" @click="confirmPayment"  autofocus />
    </template>
</Dialog>







</template>
<script>
import {DayPilot} from '@daypilot/daypilot-lite-vue'
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
import DataViewLayoutOptions from 'primevue/dataviewlayoutoptions';
import Card from 'primevue/card';
import { useStore } from '@/stores/store';
import { orderService } from '@/stores/orderService';
import { storeItemService } from '@/stores/storeItemService';
import {format, parse, add, lastDayOfMonth, startOfHour} from 'date-fns';
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
      newStoreItem:{
        storeItem:null,
        size:null,
        quantity:null
      },
      storeItemSize: [],
      storeItemQuantity: [],
      selectedType: null,
      selectedItem: null,
      storeItems: [],
      paymentMethods: ['Carta di credito', 'Bonifico','In contrassegno'],
      paymentMethod: null,
      orderId: null

    };
  },
  setup() {
    const store = useStore();
    return {store};
  },
  components: {
    DataView,Card, TabPanel, TabView, Button, Dialog, InputText, Calendar, Badge, Tag, DataTable, Column , InlineMessage, SelectButton,Image,Dropdown
  },
  props: {
     
  },

  mounted : function () {
    
    
    storeItemService().getStoreItem().then((data)=> {
      this.storeItems = data;
      console.log("this.storeItems");
      console.log(this.storeItems);
    });

    console.log("smartphone"+useStore().smartphone);
      
  },
  methods: {
    
    createStoreItem: function(s){
      if(!this.cartConfirmed) {
        storeItemService().getSizeForStoreItem(s.name).then((data)=>{
          this.storeItemSize=data;
        }).catch(e=>{});
        storeItemService().getSizeForStoreItem(s.name).then((data)=>{
          this.storeItemSize=data;
        }).catch(e=>{});


        console.log("stampo l'item selezionato");
        console.log(s);
        console.log(s.name);
        this.newStoreItem={
          size:null,
          quantity: null,
          storeItem:null
        };
        this.newStoreItem.storeItem=s;
        console.log(this.newStoreItem);
        this.selectedItem=s;
        this.showDialogItem=true;

      }else {
        useStore().alerts = ["Il carrello è confermato in attesa di pagamento"];
      }

    },

    getQuantityForStoreItemType: function(event){
      var name=this.selectedItem.name;
      var size=this.newStoreItem.size;
      storeItemService().getQuantityForStoreItem(name,size).then((data=>{
         this.storeItemQuantity=data;
         console.log(this.storeItemQuantity);
      })).catch(e=>{});

    },

    
    saveStoreItem : function(){
      console.log("SAVE ITEM TO CART");
      console.log(this.newStoreItem);
      this.showDialogItem = false;
      if(!this.cartConfirmed){
      useStore().addItemToCart(this.newStoreItem);
      }else {
        useStore().alerts = ["Il carrello è confermato in attesa di pagamento"];
      }
      this.newStoreItem={
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
      this.paymentMode = null;

      this.sendOrder();
      

    },

    sendOrder: function () {
      const store = useStore();

      var order = {
        user: {
          email: store.user.email
        },
        items: store.cart.items,
      };


      orderService().sendOrder(order).then((data) => {
        this.cartConfirmed = true;
        this.orderId = data.orderId;
      });
    },

    confirmPayment: function() {
      if(this.paymentMethod == null) {
        useStore().alerts = ["Selezionare un methodo di pagamento"];
        return;
      }
      orderService().orderPayment(this.orderId, this.paymentMethod).then(() => {
        this.paymentMethod = null;
        var msg="Pagamento andato a buon fine. L'ordine "+this.orderId+" è consultabile nella sezione Ordini";
        this.orderId=null;
        this.cartConfirmed=false;
        this.clearCart();
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

