import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios';
import { faRupiahSign } from '@fortawesome/free-solid-svg-icons';

export const useStore = defineStore('store', {
  state: () => ({ 
    user: null,
    loading: false,
    alerts: [],
    messages: [],
    showCartDialog: false,
    cart: {
      items: [],
      globalTotalPrice: 0.0,
      itemNumber: 0
    },
  }),
  getters: {
    showDialog: (state) => (state.alerts != null && state.alerts.length > 0 ) || (state.messages != null && state.messages.length > 0 ),

  },
  actions: {
    setUser(u) {
      this.user = u;
    },
    dismissMessages() {
      this.alerts = [];
      this.messages = [];
      
    },
    addMessage(m) {
      this.messages.push(m);
    },
    addError(err) {
      this.alerts.push(err);
    },
    showCart() {
      this.showCartDialog = true;
    },
    hideCart() {
      this.showCartDialog = false;
    },
    addItemToCart(item) {
      this.cart.items.push(item);
      this.cart.itemNumber+=item.quantity;
      this.cart.globalTotalPrice += (item.storeItemType.price*item.quantity);
    },
    removeItemFromCart(index, item){
      this.cart.items.splice(index,1);
      this.cart.itemNumber=this.cart.itemNumber-(item.quantity);
      this.cart.globalTotalPrice=this.cart.globalTotalPrice-(item.storeItemType.price*item.quantity);
    },
    clearCart() {
      this.cart = {
        items: [],
        globalTotalPrice: 0.0,
        itemNumber: 0
      };
    }
  },
})
