import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios';
import { faRupiahSign } from '@fortawesome/free-solid-svg-icons';
import { cartService } from '@/stores/cartService'

export const useStore = defineStore('store', {
  state: () => ({ 
    showLoginDialog: false,
    user: null,
    loading: false,
    alerts: [],
    messages: [],
    showCartDialog: false,
    cart: {
      items: [],
      price: 0.0,
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
    showLogin() {
      console.log("mostro il login")
      this.showLoginDialog = true;
    },
    hideLogin() {
      this.showLoginDialog = false;
    },
    addItemToCart(item) {
      this.cart.items.push(item);
      this.cart.itemNumber+=item.quantity;
      this.cart.price += (item.storeItem.price*item.quantity);
      cartService().updateCart(this.cart);
    },
    removeItemFromCart(index, item){
      this.cart.items.splice(index,1);
      this.cart.itemNumber=this.cart.itemNumber-(item.quantity);
      this.cart.price=this.cart.price-(item.storeItem.price*item.quantity);
      if(this.cart.items.length == 0) {
        cartService().deleteCart();
      }
      else {
        cartService().updateCart(this.cart);
      }
      
    },
    clearCart() {
      this.cart = {
        items: [],
        price: 0.0,
        itemNumber: 0
      };
      cartService().deleteCart();
    },
    clearCartAfterOrder() {
      this.cart = {
        items: [],
        price: 0.0,
        itemNumber: 0
      };
      
    },
  },
})
