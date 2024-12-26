<script setup>
import TheWelcome from '../components/TheWelcome.vue'
import Contatti from '../components/Contatti.vue'
import fascia from '@/assets/img/fascia-federazioni.png' // => or relative path
import { useStore } from '@/stores/store'
</script>

<template>
    <TheWelcome />
    <div id="start-change"></div>
    <div class="w-100 py-7"></div>
    <div class="container w-100 mx-auto py-4 text-center">
      <img :src="fascia" style="width: 70vw">
    </div>
    <div class="w-100 py-4"></div>
    <Contatti />
  
</template>
<script>
export default {
  name: "HomeView",
  data() {
    return {
      scroll_start: 0
    };
  },

  setup () {
    const store = useStore();
    return store;
  },

  mounted : function () {
    window.scrollTo(0,0);  
    this.setScrollingLogic();
      
  },
  methods: {
      
      setScrollingLogic : function () {
        console.log("area "+useStore().area);
        



          this.scroll_start = 0;
          var startchange = document.getElementById('start-change');
          var offset = startchange.getBoundingClientRect().top;
          //console.log("offset "+offset);
          var offsetT = courses.getBoundingClientRect().top;
          var contacts = document.getElementById('contacts');
          var offsetI = teachers.getBoundingClientRect().top;
          var offsetC = teachers.getBoundingClientRect().top;
          console.log("offset "+offset+", offsetT "+offsetT+", offsetI "+offsetI);
            //console.log("offsetT "+offsetT);
        //si attiva al resize della finestra
          window.addEventListener("resize", function(){
              if(useStore().area == 'public'){
                  startchange = document.getElementById('start-change');
                  offset = startchange.getBoundingClientRect().top;
                  offsetT = courses.getBoundingClientRect().top;
                  contacts = document.getElementById('contacts');
                  offsetI = teachers.getBoundingClientRect().top;
                  offsetC = teachers.getBoundingClientRect().top;
                  console.log("offset "+offset+", offsetT "+offsetT+", offsetI "+offsetI);
              }
            }
          );
            
          window.addEventListener("scroll", function(){
            if(useStore().area == 'public'){
                this.scroll_start = document.documentElement.scrollTop || document.body.scrollTop;
                console.log("scroll_start "+this.scroll_start)
                if(this.scroll_start > offset-250 && this.scroll_start < offsetT+150) {
                    document.getElementById("navlink-home").classList.remove("active");
                    document.getElementById("navlink-contacts").classList.remove("active");
                    console.log("primo");
                }
                else if(this.scroll_start >= offsetT+150 && this.scroll_start < offsetI+250) {
                    document.getElementById("navlink-home").classList.remove("active");
                    document.getElementById("navlink-contacts").classList.remove("active");
                    console.log("secondo");
                }
                else if(this.scroll_start >= offsetI+250 ) {
                    document.getElementById("navlink-home").classList.remove("active");
                    //document.getElementById("navlink-contacts").classList.remove("active");
                    document.getElementById("navlink-contacts").classList.add("active");
                    
                    console.log("terzo");
                }
                else{
                    document.getElementById("navlink-home").classList.add("active");
                    document.getElementById("navlink-contacts").classList.remove("active");
                    console.log("quarto");
                }
              
            }
          })
        
      },  
    },
};
</script>

