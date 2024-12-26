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
    smartphone: false,
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
    isSmartPhone() {
      return this.smartphone;
    },
    setSmartPhone(isSmart) {
      this.smartphone = isSmart;
    },
    
    addItemToCart(item) {
      this.cart.items.push(item);
      this.cart.itemNumber+=item.quantity;
      this.cart.globalTotalPrice += (item.storeItemType.price*item.quantity);
    },
    addSubscriptionToCart(sub) {
      this.cart.itemNumber++;
      this.cart.globalTotalPrice = this.cart.globalTotalPrice + sub.subscriptionType.price;
    },
    removeSubscriptionFromCart(index, sub) {
      this.cart.itemNumber--;
      this.cart.globalTotalPrice = this.cart.globalTotalPrice - sub.subscriptionType.price;
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
