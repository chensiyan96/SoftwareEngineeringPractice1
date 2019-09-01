<template>
  <div class="recommend">
    <panel :title="'推荐作者'">
      <div class="content" slot="content">
        <ul class="others">
          <li v-for="recommend in recommendList">
            <div>
              <el-col :span="16" :offset="0" class="author-name">
                <a :href="'/user/' + recommend.userId">
                  <p style="color: black;font-size: 17px">{{recommend.username}}</p>
                </a>
              </el-col>
              <iv-button type="primary" class="author-focus" @click="focusAuthor(recommend)">
                {{recommend.text}}
              </iv-button>
            </div>
            <div class="info" style="color: gray;font-size: 13px">
              <iv-icon type="compose"></iv-icon>
              写了{{recommend.articleNum}}篇文章
              <iv-icon class="likes" type="heart"></iv-icon>
              {{recommend.likeNum}}喜欢
            </div>
          </li>
        </ul>
      </div>
    </panel>
  </div>
</template>

<script type="text/ecmascript-6">
    import {mixin} from '@/utils/index'
    import Panel from '@/components/utils/Panel'

    export default {
      inject:['reload'],
        data() {
            return {
                recommendList: []
            }
        },
        mixins: [mixin],
        created() {
            this.listRecommend()
        },
        methods: {
            listRecommend() {
                this.$http({
                    url: this.$http.adornUrl('/followUsers'), // 请求的地址
                    method: 'get', // 请求的方式
                }).then(({data}) => {
                    if (data && data.success) {
                        this.followUsers = data.data
                    } else {
                        this.$Message.error(data.msg)
                    }
                    this.$http({
                        url: this.$http.adornUrl('/recommendUsers'),
                        method: 'get',
                        params: this.$http.adornParams()
                    }).then(({data}) => {
                            if (data && data.success) {
                                this.recommendList = data.data
                                this.recommendList.forEach(value => {
                                        this.followUsers.forEach(value2 => {
                                            if (value.userId === value2.userId) {
                                                value.follow = true
                                                value.text = '取消关注'
                                            }
                                        })
                                        if (value.follow === undefined) {
                                            value.follow = false
                                            value.text = '+关注'
                                        }
                                    }
                                )
                            } else {
                                this.$Message.error(data.msg)
                            }
                        }
                    )
                })

            },
            focusAuthor(item) {
                if (item.follow) {
                    this.$http({
                        url: this.$http.adornUrl('/cancelFollow/' + item.userId), // 请求的地址
                        method: 'delete', // 请求的方式
                    }).then(({data}) => {
                        if (data && data.success) {
                            this.$Message.success('已取消关注')
                            item.text = '+关注'
                            item.follow = false
                            this.reload()
                        } else {
                            this.$Message.error(data.msg)
                        }
                    })
                } else {
                    this.$http({
                        url: this.$http.adornUrl('/followUser/' + item.userId), // 请求的地址
                        method: 'put', // 请求的方式
                    }).then(({data}) => {
                        if (data && data.success) {
                            this.$Message.success('关注成功')
                            item.text = '取消关注'
                            item.follow = true
                            this.reload()
                        } else {
                            this.$Message.error(data.msg)
                        }
                    })
                }
            }
        },
        components: {
            'panel':
            Panel
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import "../../common/stylus/index.styl";

  .recommend
    .content
      padding 5px 20px
      /*border black solid 1px*/

      .top, .others
        a
          display block
          overflow hidden
          /*border black solid 1px*/
          /*.tags*/
          /*	margin-bottom 10px*/
          /*.ivu-btn-primary*/
          /*    color #fff*/
          /*    background-color #2d8cf0*/
          /*    border-color #2d8cf0*/
          /*    float right*/
          /*    width 80px*/
          /*    border black solid 1px*/

          .author-name
            text-align justify
            color $color-gradually-gray-41
            font-size 16px
            line-height 23px
            margin-bottom 5px
            width 65%
            border black solid 1px

          .author-focus
            width 30%
            float right
            border black solid 1px

      /*.info
        margin 5px 0 0px
        color gray*/
      /*  color $color-gradually-gray-41*/

      /*span
        font-size 13px
        line-height 18px
        font-weight 100
        color $color-secondary-info

        + span
          float right
          margin-left 10px*/

      a
        display inline-block
        color: #777
        cursor pointer

        &:hover
          color $color-main-primary
          text-decoration underline

      .img
        padding-bottom: 40%
        width: 100%
        height: 0
        margin 5px 0
        overflow hidden

        img
          width 100%
          transition: All 0.4s ease-in-out
          transform: scale(1.0)
          zoom: 1.0

      .desc
        text-align justify
        color $color-secondary-info
        font-size 13px
        line-height 20px
        margin 5px 0 0

      &:hover
        .title
          color $color-main-primary

        img
          transition: All 0.4s ease-in-out
          transform: scale(1.05)
          zoom: 1.05

    .others
      li
        list-style-type none
        margin-top 10px
        padding-top 10px
        border-top 1px solid $color-border

</style>
