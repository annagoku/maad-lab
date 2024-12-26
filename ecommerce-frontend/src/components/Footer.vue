<template>
<footer v-if="!store.user" class="footer mt-auto py-3  w-100">
  <div class="row container-fluid ">
    <div class="col text-center">
      <p>&copy; 2023 - Annalisa Sabatelli</p>
      <nav>
        <p>Contatti:  <a href="mailto:help.administrator@scuderia.com">help.administrator@scuderia.com</a></p>
      </nav>
    </div>
      </div>
</footer>
</template>

<script>
import { useStore } from '@/stores/store'


export default {
  name: 'Footer',
  data() {
    return {
    };
  },
  setup () {
    const store = useStore();
    return {store};
  },

  components: {
    
  },
  props: {
    
  },
  methods: {
    
  },
  computed: {
    /*crumbs: function() {
      let pathArray = this.$route.path.split("/")
      pathArray.shift()
      let breadcrumbs = pathArray.reduce((breadcrumbArray, path, idx) => {
        breadcrumbArray.push({
          path: path,
          to: breadcrumbArray[idx - 1]
            ? "/" + breadcrumbArray[idx - 1].path + "/" + path
            : "/" + path,
          text: this.$route.matched[idx].meta.breadCrumb || path,
        });
        return breadcrumbArray;
      }, [])
      return breadcrumbs;
    }*/
  }
}
</script>

