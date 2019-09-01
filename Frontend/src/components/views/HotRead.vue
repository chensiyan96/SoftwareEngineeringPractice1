<template>
  <div class="hotRead">
    <panel :title="'最热阅读'">
      <div slot="content" class="content">
        <ul class="others">
          <li v-for="hotRead in hotReadList" :key="hotRead.articleId">
            <a :href="'/article' + '/' +hotRead.articleId">
              <p class="title">{{hotRead.title}}</p>
              <p class="info">
                <span class="time">{{hotRead.updateTime | socialDate }}</span>
                <span class="likes"><iv-icon type="heart"></iv-icon> {{hotRead.likeNum}} </span>
                <span class="readings"><iv-icon type="eye"></iv-icon> {{hotRead.readNum}} </span>
              </p>
            </a>
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
        props: {
            userId: String
        },
        data() {
            return {
                hotReadList: []
            }
        },
        mixins: [mixin],
        created() {
            this.userId = this.$route.params.id
            this.listHotRead(this.userId)
        },
        methods: {
            listHotRead(userId) {
                if (userId !== undefined) {
                    this.$http({
                        url: this.$http.adornUrl('/hotArticles/' + userId),
                        method: 'get',
                        params: this.$http.adornParams()
                    }).then(({data}) => {
                        if (data && data.success) {
                            this.hotReadList = data.data
                        }
                    })
                } else {
                    this.$http({
                        url: this.$http.adornUrl('/hotArticles'),
                        method: 'get',
                        params: this.$http.adornParams()
                    }).then(({data}) => {
                        if (data && data.success) {
                            this.hotReadList = data.data
                        }
                    })
                }
            }
        },
        components: {
            'panel': Panel
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import "../../common/stylus/index.styl";

  .hotRead
    .content
      padding 5px 20px

    .top, .others
      a
        display block
        overflow hidden
        text-decoration none

        .tags
          margin-bottom 10px

        .title
          text-align justify
          color $color-gradually-gray-41
          font-size 16px
          line-height 23px
          margin-bottom 5px

        .info
          margin 5px 0 0

          span
            font-size 13px
            line-height 18px
            font-weight 100
            color $color-secondary-info

            + span
              float right
              margin-left 10px

          a
            display inline-block
            color: #777
            cursor pointer

            &:hover
              color $color-main-primary
              text-decoration none

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
