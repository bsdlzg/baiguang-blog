<template>
  <div class="sidebar">
    <SiteInfo v-show="this.$store.state.userInfo"/>
    <Group v-show="!this.$store.state.userInfo"/>
    <Notice />
  </div>
</template>
<script>
import SiteInfo from '../site/index.vue'
import Notice from '../notice/index.vue'
import Group from '../group/index.vue'


export default {
  name: 'Sidebar',
  components: {
    SiteInfo,
    Notice,
    Group,
  },
  data() {
    return {}
  }

}
</script>
<style lang="scss" scoped>
.sidebar {
  margin-left: 20px;
}
</style>
