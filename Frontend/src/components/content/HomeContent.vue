<template>
  <div class="article-list-content">
    <iv-row>
      <iv-col :xs="24" :sm="24" :md="24" :lg="17">
        <div class="layout-left">
          <article-list-header v-if="categoryList.length>0" @filterByMenu="filterByMenu"
                               @filterByCategory="filterByCategory"
                               :categorys="categoryList"
                               :defaultCategory="selected_category"
                               :mainTitle="'文章列表'" :sub-title="'Articles'"></article-list-header>
          <article-list-cell v-for="article in articleList" :article="article"
                             :key="article.articleId"></article-list-cell>
          <browse-more v-if="showData" @browseMore="browseMore" :noMoreData="noMoreData" ref="browseMore"></browse-more>
        </div>
      </iv-col>
      <iv-col :xs="0" :sm="0" :md="0" :lg="7">
        <div class="layout-right">
          <about></about>
          <hot-read></hot-read>
          <tag-wall style="margin-top: 15px;"></tag-wall>

        </div>
      </iv-col>
    </iv-row>
  </div>
</template>

<script type="text/ecmascript-6">
    import ArticleListHeader from '@/components/views/Article/ArticleListHeader'
    import ArticlePageContent from '@/components/views/Article/ArticlePageContent'
    import ArticlePageFooter from '@/components/views/Article/ArticlePageFooter'
    import ArticleListCell from '@/components/views/Article/ArticleListCell'
    import Recommend from '@/components/views/Recommend'
    import TagWall from '@/components/views/TagWall'
    import BrowseMore from '@/components/views/BrowseMore'
    import About from '@/components/views/About'
    import HotRead from '@/components/views/HotRead'
    import merge from 'lodash/merge'
    import {treeDataTranslate} from '@/utils'
    import {DefaultLimitSize} from '@/common/js/const'

    export default {
        data() {
            return {
                articleList: [],
                categoryList: [],
                selected_category: this.$route.params.id,
                currentPage: 1,
                pageSize: DefaultLimitSize,
                categoryId: this.$route.params.id,
                menuParams: {
                    latest: true
                },
                noMoreData: false,
                showData: false
            }
        },
        created() {
            this.userId = this.$route.params.id
            this.listArticle()
            this.listCategory()
        },
        methods: {
            listArticle() {
                let params = {
                    categoryId: this.categoryId,
                    limit: this.pageSize,
                    page: this.currentPage
                }
                params = merge(params, this.menuParams)
                this.$http({
                    url: this.$http.adornUrl('/articles/' + this.userId),
                    params: this.$http.adornParams(params),
                    method: 'get'
                }).then(({data}) => {
                    if (data && data.code === 200) {
                        this.showData = data.page.currPage < data.page.totalPage;
                        this.noMoreData = data.page.totalPage <= data.page.currPage;
                        this.articleList = data.page.list
                    }
                })
            },
            listCategory() {
                let params = {}
                params.type = 0
                this.$http({
                    url: this.$http.adornUrl('/categories'),
                    method: 'get',
                    params: this.$http.adornParams(params)
                }).then(({data}) => {
                    if (data && data.code === 200) {
                        this.categoryList = treeDataTranslate(data.categoryList)
                    }
                })
            },
            filterByMenu(params) {
                this.resetCurrentPage()
                this.menuParams = params
                this.listArticle()
            },
            filterByCategory(params) {
                this.resetCurrentPage()
                this.categoryId = params
                this.listArticle()
            },
            resetCurrentPage() {
                this.currentPage = 1
            },
            browseMore() {
                this.currentPage++
                let params = {
                    categoryId: this.categoryId,
                    limit: this.pageSize,
                    page: this.currentPage
                }
                params = merge(params, this.menuParams)
                this.$http({
                    url: this.$http.adornUrl('/articles/' + this.userId),
                    params: this.$http.adornParams(params),
                    method: 'get'
                }).then(({data}) => {
                    if (data && data.code === 200) {
                        this.noMoreData = data.page.totalPage <= data.page.currPage;
                        this.articleList = this.articleList.concat(data.page.list)
                    }
                }).then(response => {
                    this.$refs.browseMore.stopLoading()
                }).catch(error => {
                    this.$refs.browseMore.stopLoading()
                    console.log(error)
                })
            }
        },
        components: {
            'article-list-header': ArticleListHeader,
            'article-page-content': ArticlePageContent,
            'article-page-footer': ArticlePageFooter,
            'article-list-cell': ArticleListCell,
            'recommend': Recommend,
            'tag-wall': TagWall,
            'browse-more': BrowseMore,
            'about': About,
            'hot-read': HotRead
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .article-list-content
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
      margin-bottom 50px

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
