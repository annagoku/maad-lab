<template>
<div id="courses" class="cointainer mx-auto text-center">
      <h1 >Attività</h1>
      <div class="row">
        <p id="citazione" class="text-primary">&ldquo;E' impossibile sprecare un'ora di vita se si è in sella" (W. Churchill)&rdquo; </p>
      </div>
      <div class="row">
        <div class="p-2"></div>
      </div>
      <div class="row" >
        <template v-for="(course, index) in courses" :key="index">
          <div class="card col-xs-12 col-md-3  mx-auto">
            <div class="card-title"><h3 class="text-primary">{{course.name}}</h3></div>
            <div class="card-body">{{course.text}}</div>
          </div>
        </template>

      </div>
</div>
</template>
<script>
export default {
  name: "Corsi",
  data() {
    return {
     courses: []
    };
  },
  props: {
     
  },
  mounted : function () {
     this.courses = this.getCourses();

  },
  methods: {
      getCourses : function () {
        console.log("eseguo getCourses");
        const thecourses = [
          {name: "Scuola di Equitazione", text: "Il Circolo offre lezioni di equitazione, monta inglese, per ogni esigenza e livello: bambini, principianti o esperti."}, 
          {name: "Attività agonistiche", text:"Il circolo e gli istruttori seguono e aiutano gli allievi che vogliono svolgere attività agonistiche o che vogliono intraprendere un percorso di formazione, pianificando gli allenamenti e il percorso di crescita nelle categorie, seguendo ogni gara e analizzando il lavoro svolto."},
          {name: "Pensione Cavalli", text: "Offriamo un servizio di pensione completa per cavalli con possibilità di box interni alla scuderia, box esterni e ampi paddok per la bella stagione."}
        ];
        
        return thecourses;
      }
  },
};
</script>