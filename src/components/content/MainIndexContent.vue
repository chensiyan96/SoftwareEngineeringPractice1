<template>
  <div class="main-index-content">
    <iv-row>
      <iv-col :lg="17" :md="17" :sm="17" :xl="17" :xs="17">
        <div class="layout-left">
          <section-title :mainTitle="'文章'" :tipHref="'/articles'" :tipText="'查看更多'">
            <title-menu-filter :menu-filter-list="defaultFilterList" @filterByMenu="refreshArticle"
                               slot="menu"></title-menu-filter>
          </section-title>
          <article-list-cell :article="article" :key="article.title" :type="'article'"
                             v-for="article in articleList"></article-list-cell>
        </div>
      </iv-col>
      <iv-col :lg="7" :md="0" :sm="0" :xs="0">
        <div class="layout-right">
          <author-recommend></author-recommend>
          <br>
          <br>
          <hot-read></hot-read>
        </div>
      </iv-col>
    </iv-row>
  </div>
</template>

<script type="text/ecmascript-6">
import ArticleListCell from '@/components/views/Article/ArticleListCell'
import SectionTitle from '@/components/views/SectionTitle/SectionTitle'
import TitleMenuFilter from '@/components/views/SectionTitle/TitleMenuFilter'
import ArticlePageHeader from '@/components/views/Article/ArticlePageHeader'
import ArticlePageContent from '@/components/views/Article/ArticlePageContent'
import ArchiveListTimeTitle from '@/components/views/Archive/ArchiveListTimeTitle'
import ArchiveListCell from '@/components/views/Archive/ArchiveListCell'
import About from '@/components/views/About'
import FriendLinks from '@/components/views/FriendLinks'
import TagWall from '@/components/views/TagWall'
import Recommend from '@/components/views/Recommend'
import AuthorRecommend from '@/components/views/AuthorRecommend'
import HotRead from '@/components/views/HotRead'
import SideToc from '@/components/views/SideToc'
import merge from 'lodash/merge' // 合并对象工具
import {DefaultFilterList, DefaultLimitSize} from '@/common/js/const'

export default {
  data () {
    return {
      articleList: [],
      defaultFilterList: DefaultFilterList,
      pageParam: {
        page: 1,
        limit: DefaultLimitSize
      }
    }
  },
  components: {
    'article-list-cell': ArticleListCell,
    'section-title': SectionTitle,
    'title-menu-filter': TitleMenuFilter,
    'article-page-header': ArticlePageHeader,
    'article-page-content': ArticlePageContent,
    'archive-list-time-title': ArchiveListTimeTitle,
    'archive-list-cell': ArchiveListCell,
    'about': About,
    'friend-links': FriendLinks,
    'side-toc': SideToc,
    'tag-wall': TagWall,
    'recommend': Recommend,
    'author-recommend': AuthorRecommend,
    'hot-read': HotRead
  },
  created: function () {
    let param = {}
    param.latest = true
    this.refreshArticle(param)
  },
  methods: {
    refreshArticle (param) {
      let params = merge(param, this.pageParam)
      this.$http({
        url: this.$http.adornUrl('/articles'),
        params: this.$http.adornParams(params, false),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 200) {
          this.articleList = data.page.list
        }
      })
    }
  }
}
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .main-index-content
    width auto
    @media only screen and (max-width: 768px)
      margin 5px 5px 10px 5px
    @media screen and (min-width: 768px)
      margin 10px 10px 20px 10px
    @media screen and (min-width: 992px)
      margin 15px 35px 50px 35px
    @media screen and (min-width: 1200px)
      width 1200px
      margin 15px auto 0
      .layout-left, .layout-right
        padding 0
        @media only screen and (max-width: 768px)
          padding 0
        @media screen and (min-width: 768px)
          padding 0
        @media screen and (min-width: 992px)
          padding 0 10px
        @media screen and (min-width: 1200px)
          padding 0 10px
</style>
