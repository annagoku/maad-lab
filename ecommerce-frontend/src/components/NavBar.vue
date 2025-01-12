<template>
  <header>
  
  
    <Menubar id="navbar" class="fixed-top bg-light py-3">
    <template #start>
      <span class="pointer" @click="goToHomePublic()"><img :src="logoInverted" style="zoom: 20%">  {{title}}</span>
    </template>
    <template #end>
        <div class="flex align-items-center gap-2">
          <i v-if="store.user && store.cart.items.length>0" v-badge="store.cart.items.length" @click="store.showCart()" class="pi pi-shopping-cart mx-2" style="font-size: 2rem" />
          <i v-if="store.user && store.cart.items.length==0" class="pointer pi pi-shopping-cart mx-2" @click="store.showCart()" style="font-size: 2rem" />
          <a v-if="store.user" class="nav-link " id="dropdownMenuUser" @click="toggleMenuUser" aria-haspopup="true" aria-controls="overlay_menu" href="#" >
            <Avatar icon="pi pi-user" class="mr-2" size="large" shape="circle"
                  :aria-label="store.user.name +' '+store.user.surname" />
          </a>
                <Menu ref="menu" id="overlay_menu" :model="userMenuItems" :popup="true">
                  <template #start>
                    <button class="w-full p-link flex align-items-center p-2 pl-4 text-color hover:surface-200 border-noround">
                      <span class="font-bold">{{store.user.name+' '+store.user.surname}}</span><span v-if="store.user.isAdmin">(admin)</span>
                    </button>
                      
                  </template>
                
                </Menu>
          <Button v-if="!store.user" label="Login" icon-pos="right" icon="pi pi-sign-in"  raised rounded  @click="store.showLogin()" ></Button>
          
        </div>
    </template>
</Menubar>
  </header>


</template>

<script>
import logoInverted from '@/assets/img/logo.svg' // => or relative path
import { useStore } from '@/stores/store'
import Avatar from 'primevue/avatar';
import Button from 'primevue/button';
import Menu from 'primevue/menu';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import Menubar from 'primevue/menubar';



export default {
  name: 'NavBar',
  data() {
    return {
        showNavbar: false,
        userMenuItems:  [
            { separator: true },
            {
                label: 'Ordini',
                icon: 'pi pi-fw pi-lock',
                command: () => {
                    this.$router.push({name: "orders"});
                }
            },
            { separator: true },
            {
                label: 'Logout',
                icon: 'pi pi-sign-out',
                command: () => {
                    this.logout();
                }
            }
        ]
    };
  },
  setup () {
    const store = useStore();
    return {store, logoInverted};
  },

  components: {
    Avatar, Menu, Menubar, Dialog, Button, InputText, Message
  },
  props: {
    title: String
  },
  methods: {
    toggleNavBar() {
      console.log("navbar -> "+this.showNavbar);
      this.showNavbar = !this.showNavbar;
      var theNav = document.getElementById("navbarCollapse");
      console.log(theNav);
          
      if(!this.showNavbar) {
        console.log("rimuovo la classe show");
        theNav.classList.remove("show");
        theNav.classList.remove("collapsing");
        theNav.classList.add("collapse");
      }
      else {
        theNav.classList.add("show");
      }

    },
    toggleMenuUser (event) {
      //console.log(this.$refs.menu)
      this.$refs.menu.toggle(event);
    },
    goToHomePublic() {
      this.$router.push({name: "home"});
    },
    goToProfile() {
      this.$router.push({name: "profile"});
    },
    goToOrders() {
      this.$router.push({name: "orders"});
    },    
    scrollTo(idElem) {
      var elem = document.getElementById(idElem);
      var y = elem.getBoundingClientRect().top-50+window.scrollY;
      console.log("scroll to y = "+y)
      window.scroll(0, y);
    },
    showLogin () {
      console.log("Mostro il login");
      useStore().showLogin = true;
    },
    logout ()  {

      console.log('logout')
      this.store.user = null;
      this.$router.push({name: "home"});
      
    }
  },
  computed: {
    
  }
}
</script>

