<template>
  <div id="article-page-content">
    <slot name="content"></slot>
    <go-top></go-top>
  </div>

</template>

<script type="text/ecmascript-6">
  import GoTop from '../../gotop/GoTop'
export default {
    components:{
      'go-top':GoTop
    }
}
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import "../../../common/stylus/article.styl";

  #article-page-content
    padding 5px
</style>
