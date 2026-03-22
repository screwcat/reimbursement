<template>
  <div class="home">

  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.9.1"
    }
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank")
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  background-image: url("../assets/images/welcome.png");
  height: 100%;
  background-repeat: no-repeat;
  background-position: center center; 
  background-size: cover; 
  background-attachment: local; 
}
</style>

