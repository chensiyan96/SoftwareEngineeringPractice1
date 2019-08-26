<template>
  <div class="common-footer">
    <p class="copyright">
      {{siteInfo.icp}}
      <span>|</span>
      {{ siteInfo.copyright }}
    </p>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  data () {
    return {
      siteInfo: {
        icp: '小学期小组',
        copyright: '版权所有 © 2019',
      }
    }
  }
}
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import "../../common/stylus/theme.styl"
  .common-footer
    font-weight 300
    line-height 25px
    text-align center
    margin 15px 0
    color $default-title-color
    border-top 1px solid $default-border-color
    background $default-background-color
    @media only screen and (max-width: $responsive-sm)
      font-size 13px
    @media screen and (min-width: $responsive-sm)
      font-size 14px
    @media screen and (min-width: $responsive-md)
      font-size 15px
    @media screen and (min-width: $responsive-lg)
      font-size 15px
    .copyright
      margin 10px
      color $default-title-color
      a
        color $default-link-color
        &:hover
          color $default-link-hover-color
</style>
