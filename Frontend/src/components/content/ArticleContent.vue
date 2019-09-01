<template>
  <div class="article-content" v-cloak>
    <iv-row>
      <iv-col :xs="24" :sm="24" :md="24" :lg="17">
        <div class="layout-left">
          <article-page-header :article="article"></article-page-header>
          <article-page-content>
            <article id="article-main-page" class="typo container" slot="content" ref="article"
                     v-html="article.contentFormat">
            </article>
          </article-page-content>
          <el-button v-if="article.author === this.name" @click="deleteArticle">
            删除
          </el-button>
          <el-button v-if="this.article.appendix" type="primary" @click="downloadFile()">
            点击下载附件<i class="el-icon-download"></i></el-button>
          <article-page-footer :articleId="this.$route.params.articleId"></article-page-footer>
        </div>
      </iv-col>
      <iv-col :xs="0" :sm="0" :md="0" :lg="7">
        <div class="layout-right">
          <about></about>
          <hot-read></hot-read>
          <iv-affix :offset-top="60">
            <side-toc style="margin-top: 15px;"></side-toc>
          </iv-affix>
        </div>
      </iv-col>
    </iv-row>
  </div>
</template>
<script type="text/ecmascript-6">
    import ArticlePageHeader from '@/components/views/Article/ArticlePageHeader'
    import ArticlePageContent from '@/components/views/Article/ArticlePageContent'
    import ArticlePageFooter from '@/components/views/Article/ArticlePageFooter'
    import About from '@/components/views/About'
    import FriendLinks from '@/components/views/FriendLinks'
    import SideToc from '@/components/views/SideToc'
    import TOC from '@/common/js/MarkdownToc'
    // TOC滚动监听
    import TocScrollSpy from '@/common/js/TocScrollSpy'
    import HotRead from "../views/HotRead";

    export default {
        inject: ['reload'],
        data() {
            return {
                article: {},
                name: localStorage.getItem('blog_username')
            }
        },
        components: {
            'article-page-header': ArticlePageHeader,
            'article-page-content': ArticlePageContent,
            'article-page-footer': ArticlePageFooter,
            'about': About,
            'friend-links': FriendLinks,
            'side-toc': SideToc,
            'hot-read': HotRead
        },
        created: function () {
            this.getArticle(this.$route.params.articleId)
        },
        methods: {
            addCodeLineNumber() {
                // 添加行号
                let blocks = this.$refs.article.querySelectorAll('pre code')
                blocks.forEach((block) => {
                    window.hljs.highlightBlock(block)
                    // 去前后空格并添加行号
                    block.innerHTML = '<ul><li>' + block.innerHTML.replace(/(^\s*)|(\s*$)/g, '').replace(/\n/g, '\n</li><li>') + '\n</li></ul>'
                })
            },
            getArticle(articleId) {
                this.$http({
                    url: this.$http.adornUrl('/article/' + articleId),
                    method: 'get'
                }).then(({data}) => {
                    if (data && data.code === 200) {
                        this.article = data.article
                        // 更新目录、高亮代码
                        this.$nextTick(function () {
                            this.addCodeLineNumber()
                            this.refreshDiectory()
                            this.refreshMobileDirectory()
                            document.title = this.article.title + ' | 博客 | 小学期'
                        })
                    }
                })
                // if(this.article.appendix === undefined){
                //     console.log('没有附件')
                // }
                // else{
                //     console.log('有附件')
                // }

            },
            deleteArticle() {
                const h = this.$createElement;
                this.$msgbox({
                title: '提醒',
                message: h('p', null, [
                  h('span', null, '此操作不可撤销，确定要删除吗？'),
                ]),
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                beforeClose: (action, instance, done) => {
                if (action === 'confirm') {
                  instance.confirmButtonLoading = true;
                  instance.confirmButtonText = '执行中...';
                  this.$http({
                    url: this.$http.adornUrl('/deleteArticle/' + this.article.articleId),
                    method: 'delete'
                  }).then(({data}) => {
                      if (data && data.success) {
                          this.$router.replace({name: 'index'})
                      } else {
                          this.$message.error(data.msg)
                      }
                  })
                  setTimeout(() => {
                    done();
                    setTimeout(() => {
                      instance.confirmButtonLoading = false;
                    }, 300);
                  }, 30);
                } else {
                  done();
                }
              }
            }).then(action => {
              this.$message.success('删除文章成功')
            });
            },
            refreshDiectory() {
                /* eslint-disable*/
                new TOC('article-main-page', {
                    'level': 5,
                    'top': 200,
                    'class': 'list',
                    'targetId': 'side-toc'
                })
                /* eslint-disable */
                new TocScrollSpy('article-main-page', 'side-toc', {
                    'spayLevel': 5,
                    'articleMarginTop': 0
                })
            },
            refreshMobileDirectory() {
                /* eslint-disable */
                new TOC('article-main-page', {
                    'level': 5,
                    'top': 200,
                    'class': 'list',
                    'targetId': 'sidebar-toc'
                })
                new TocScrollSpy('article-main-page', 'sidebar-toc', {
                    'spayLevel': 5,
                    'articleMarginTop': 15
                })
            },
            downloadFile() {
                var str = this.article.appendix
                var index = str.lastIndexOf(".");
                var suffix = str.substr(index+1);
                console.log(suffix)

                fetch(this.article.appendix).then(res => res.blob().then(blob => {
                var a = document.createElement('a');
                var url = window.URL.createObjectURL(blob);
                var filename = 'myfile.' + suffix;

                a.href = url;
                a.download = filename;
                a.click();
                window.URL.revokeObjectURL(url);
            }))
                //window.open("download=" + this.article.appendix)
            },
        }

    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .article-content
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
