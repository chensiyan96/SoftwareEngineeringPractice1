<template>
  <div class="about">
    <ul class="social">
    </ul>
    <img alt="" class="background" src="../../assets/background.jpg">
    <!-- <img  src="../../assets/avatar.jpg" alt=""> -->
<!--     <el-avatar class="avatar" :src=avatar></el-avatar>-->
    <img :src=avatar alt="头像" class="avatar">
    <p class="name" style="padding-bottom: 10px">{{name}}</p>
    <el-button @click="jump()" circle icon="el-icon-message" type="info"></el-button>
    <p class="desc" v-if="showIntroduce">{{introduce}}</p>
    <ul class="social">
    </ul>
  </div>
</template>

<script type="text/ecmascript-6">
import default_avatar from '../../assets/circle_avatar.png'

export default {
  data: function () {
    return {
      name: '',
      avatar: '',
      introduce: 'cscsc',
      showIntroduce: false,
      email: '',
      size: 'large',
      userId: undefined
    }
  },
  created () {
    this.articleId = this.$route.params.articleId
    this.userId = this.$route.params.id
    this.getName()
  },
  methods: {
    getName () {
      if (this.userId) {
        this.$http({
          url: this.$http.adornUrl('/getInfo/' + this.userId),
          method: 'get'
        }).then(({data}) => {
          if (data && data.success) {
            this.name = data.data.username
            this.avatar = data.data.avatar
            this.introduce = data.data.info
            this.email = data.data.email
          } else {
            this.name = localStorage.getItem('blog_username')
            this.avatar = localStorage.getItem('blog_avatar')
            this.introduce = localStorage.getItem('blog_introduce')
            this.email = localStorage.getItem('blog_email')
          }
            if (!this.avatar) {
              this.avatar = default_avatar
            }
            this.showIntroduce = !!this.introduce

        }
        )


      } else {
        this.$http({
          url: this.$http.adornUrl('/getAuthor/' + this.articleId),
          method: 'get'
        }).then(({data}) => {
          if (data && data.success) {
            this.name = data.data.username
            this.avatar = data.data.avatar
            this.introduce = data.data.info
            this.email = data.data.email
          } else {
            this.name = localStorage.getItem('blog_username')
            this.avatar = localStorage.getItem('blog_avatar')
            this.introduce = localStorage.getItem('blog_introduce')
            this.email = localStorage.getItem('blog_email')
          }
          if (!this.avatar) {
            this.avatar = default_avatar
          }
        })
        this.showIntroduce = !!this.introduce
      }
    },
    jump () {
      window.location.href = 'mailto:' + this.email
    }
  }
}
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>
  @import "../../common/stylus/theme.styl";

  .about
    position relative
    text-align center
    border 1px solid $color-border
    padding-bottom 20px

    img.background
      position absolute
      top 0
      left 0
      right 0
      width 100%
      height 125px
      z-index 1

    .avatar
      position relative
      margin 75px auto 15px
      width 100px
      height 100px
      border 5px solid $color-gradually-gray-91
      border-radius 50%
      z-index 9

    .name
      font-size 22px
      color $color-typegraphy-title
      line-height 30px
      font-weight 700

    .desc
      font-size 15px
      color $color-secondary-info
      line-height 30px
      font-weight 100
      text-align center

    .social
      text-align center
      /*padding 0 20px*/
      margin-bottom 25px
      margin-top 25px

      > li
        padding 8px

        a
          display block
          width: 44px
          height: 44px
          margin: auto

          img
            width 100%

    .line
      height 1px
      background-color $color-gradually-gray-91
      margin 10px 20px

    h4
      font-size 19px
      margin 30px 0 20px
      font-weight 600

    .progresses
      padding 0 20px

      p.title
        height 38px
        line-height 38px
        text-align right

      .bar
        margin 10px 0
</style>
