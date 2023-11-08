<template>
  <div id="app">
    <router-view :key="$route.fullPath" />
  </div>
</template>

<script>

import { report } from '@/api'
import { setSkin, getSkin } from '@/utils/auth'
export default {
  name: 'App',

  created() {
    report();
  },
  beforeCreate() {
    if (getSkin() == null) {
      setSkin("shallow")
    } else {
      setSkin(getSkin())
    }
  },
  mounted() {
    this.$setSkin()
  },

}
</script>

<style lang="scss" scoped>
#app {
  background: var(--body-color);
}

/deep/ .el-card__body {
  padding: 0;
}

/deep/ .el-card {
  border: 0;
}


@media screen and (max-width: 1118px) {
  .dialog {
    width: 100%;

    /deep/ .el-dialog {
      width: 100% !important;
    }
  }
}
</style>
