<template>
  <header>
  <nav id="navbar" class="navbar navbar-expand-md navbar-light fixed-top bg-light py-0">
    <div class="container-fluid">
      <a class="navbar-brand mx-2" href="/#" @click="goToHomePublic()"><img :src="logoInverted" style="zoom: 20%">  {{title}}</a>
      <button class="navbar-toggler" type="button"  
              aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation" @click="toggleNavBar">
              <span class="navbar-toggler-icon"></span>
          </button>
          <a v-if="store.user && store.smartphone" class="nav-link " id="dropdownMenuUser" @click="toggleMenuUser" aria-haspopup="true" aria-controls="overlay_menu" href="#" >
                <Avatar :image="store.user.picture" class="mr-2" size="large" shape="circle"
                  :aria-label="store.user.name +' '+store.user.surname" />
                 </a>
                <Menu ref="menu" id="overlay_menu" :model="userMenuItems" :popup="true">
                  <template #start>
                    <button class="w-full p-link flex align-items-center p-2 pl-4 text-color hover:surface-200 border-noround">
                      <span class="font-bold">{{store.user.name+' '+store.user.surname}}</span><span v-if="store.user.isAdmin">(admin)</span>
                    </button>
                      
                  </template>
                
                </Menu>

      <!--div v-if="store.area == 'public'" class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav me-auto mx-2">
         
          <li  class="nav-item mx-2"><a  id="navlink-home"  class="nav-link active" href="#"  @click="setMenuActive($event.target)">Home</a></li>
          
          <li class="nav-item mx-2"><a id="navlink-store" class="nav-link"   @click="setMenuActive($event.target), scrollTo('courses')">Attività</a></li>
          <li class="nav-item mx-2"><a id="navlink-teachers" class="nav-link"   @click="setMenuActive($event.target), scrollTo('teachers')">Insegnanti</a></li>
          <li class="nav-item mx-2"><a id="navlink-contacts" class="nav-link"   @click="setMenuActive($event.target), scrollTo('contacts')">Contatti</a></li>

        </ul>
      </div>
      <div v-if="store.area == 'private'" class="collapse navbar-collapse " id="navbarCollapse">
        <ul class="navbar-nav me-auto mx-2">
          <li v-if="store.user.isAdmin"  class="nav-item mx-2"><a  id="navlink-home" class="nav-link active" @click="setMenuActive($event.target),goToStable()">Maneggio</a></li>
          
          <li v-if="!store.user.isAdmin"  class="nav-item mx-2"><a  id="navlink-home" class="nav-link active" @click="setMenuActive($event.target),goToProfile()">Profilo</a></li>
          <li v-if="store.user.isAdmin"  class="nav-item mx-2"><a  id="navlink-home" class="nav-link" @click="setMenuActive($event.target),goToUsers()">Utenti</a></li>
          
          <li class="nav-item mx-2"><a id="navlink-reservation" class="nav-link "  @click="setMenuActive($event.target),goToLesson()">Lezioni</a></li>
          <li v-if="!store.smartphone" class="nav-item mx-2"><a id="navlink-subscription" class="nav-link "  @click="setMenuActive($event.target),goToSubscription()">Abbonamenti</a></li>
          <li v-if="!store.smartphone" class="nav-item mx-2"><a id="navlink-horse" class="nav-link "   @click="setMenuActive($event.target),goToHorse()">Cavalli</a></li>
          <li v-if="!store.smartphone" class="nav-item mx-2"><a id="navlink-order" class="nav-link "  @click="setMenuActive($event.target),goToOrders()">Ordini</a></li>
          <li class="nav-item mx-2"><a id="navlink-commerce" class="nav-link "  @click="setMenuActive($event.target), goToECommerce()">E-commerce</a></li>
        </ul>
      </div-->

      <a v-if="store.user && !store.smartphone" class="nav-link " id="dropdownMenuUser" @click="toggleMenuUser" aria-haspopup="true" aria-controls="overlay_menu" href="#" >
                <Avatar :image="store.user.picture" class="mr-2" size="large" shape="circle"
                  :aria-label="store.user.name +' '+store.user.surname" />
                 </a>
                <Menu ref="menu" id="overlay_menu" :model="userMenuItems" :popup="true">
                  <template #start>
                    <button class="w-full p-link flex align-items-center p-2 pl-4 text-color hover:surface-200 border-noround">
                      <span class="font-bold">{{store.user.name+' '+store.user.surname}}</span><span v-if="store.user.isAdmin">(admin)</span>
                    </button>
                      
                  </template>
                
                </Menu>

    </div>
      </nav>
    </header>
</template>

<script>
import logoInverted from '@/assets/img/logo.svg' // => or relative path
import { useStore } from '@/stores/store'
import Avatar from 'primevue/avatar';
import Menu from 'primevue/menu';


export default {
  name: 'NavBar',
  data() {
    return {
        showNavbar: false,
        userMenuItems:  [
            { separator: true },
            {
                label: 'Area Riservata',
                icon: 'pi pi-fw pi-lock',
                command: () => {
                    this.store.area = 'private';
                    if(this.store.user.isAdmin) {
                      this.$router.push({name: "stable"});
                    }
                    else {
                      this.$router.push({name: "profile"});
                    }
                    
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
    Avatar, Menu
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
    setMenuActive (el) {
      this.removeMenuActive();      
      el.classList.add('active');
      if(this.showNavbar) {
        this.toggleNavBar();
      }
    },
    removeMenuActive() {
      const elements = document.querySelectorAll("a.nav-link");

      for(var i=0; i< elements.length; i++) {
        elements[i].classList.remove('active');
      }
    },
    toggleMenuUser (event) {
      //console.log(this.$refs.menu)
      this.$refs.menu.toggle(event);
    },
    goToHomePublic() {
      this.store.area = 'public';
      this.$router.push({name: "home"});
    },
    goToHorse() {
      this.store.area = 'private';
      this.$router.push({name: "horse"});
    },
    goToSubscription() {
      this.store.area = 'private';
      this.$router.push({name: "subscription"});
    },
    goToECommerce() {
      this.store.area = 'private';
      this.$router.push({name: "ecommerce"});
    },
    goToLesson() {
      this.store.area = 'private';
      this.$router.push({name: "lesson"});
    },
    goToProfile() {
      this.store.area = 'private';
      this.$router.push({name: "profile"});
    },
    goToStable() {
      this.store.area = 'private';
      this.$router.push({name: "stable"});
    },
    goToOrders() {
      this.store.area = 'private';
      this.$router.push({name: "orders"});
    },    
    goToUsers() {
      this.store.area = 'private';
      this.$router.push({name: "users"});
    },    
    scrollTo(idElem) {
      var elem = document.getElementById(idElem);
      var y = elem.getBoundingClientRect().top-50+window.scrollY;
      console.log("scroll to y = "+y)
      window.scroll(0, y);
    },
    logout ()  {

      console.log('logout')
      this.store.area = 'public';
      this.store.user = null;
      this.removeMenuActive();
      this.$router.push({name: "home"});
      
    }
  },
  computed: {
    
  }
}
</script>

