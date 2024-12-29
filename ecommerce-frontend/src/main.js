import { createApp } from 'vue'
import { createPinia } from 'pinia'
import PrimeVue from 'primevue/config';


import App from './App.vue'
import router from './router'

import { library } from '@fortawesome/fontawesome-svg-core'
import { far } from '@fortawesome/free-regular-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { fas } from '@fortawesome/free-solid-svg-icons'

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import vue3GoogleLogin from 'vue3-google-login'

//import VueGoogleMaps from '@fawmi/vue-google-maps'

import 'bootstrap'
import '@popperjs/core'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.js'

//theme
import "primevue/resources/themes/md-light-indigo/theme.css";     
//core
import "primevue/resources/primevue.min.css";
//icons
import "primeicons/primeicons.css";
import 'primeflex/primeflex.css';

import './assets/main.css'
import Tooltip from 'primevue/tooltip';

// import as directive
import BadgeDirective from 'primevue/badgedirective';


library.add(fas, far, fab);

//Vue.component('font-awesome-icon', FontAwesomeIcon)

const app = createApp(App)
app.use(PrimeVue);
app.component('font-awesome-icon', FontAwesomeIcon);
app.use(createPinia());
app.use(router);
app.directive('tooltip', Tooltip);


app.directive('badge', BadgeDirective);
app.mount('#app')
