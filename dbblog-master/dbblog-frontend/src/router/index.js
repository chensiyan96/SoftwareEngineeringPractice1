import Vue from 'vue'
import Router from 'vue-router'
import {LoadingBar} from 'iview'
import Index from '@/components/index/Index'

// 公共区域
// import CommonHeader from '@/components/header/CommonHeader';
import SimpleHeader from '@/components/header/SimpleHeader/SimpleHeader'
import CommonFooter from '@/components/footer/CommonFooter'

// 首页
import HomeContent from '@/components/content/HomeContent'
import ArticleContent from '@/components/content/ArticleContent'
import ArticleListContent from '@/components/content/ArticleListContent'
import TimeLineContent from '@/components/content/TimeLineContent'
import SearchResultContent from '@/components/content/SearchResultContent'
import LoginContent from '@/components/content/LoginContent'
import RegisterContent from '@/components/content/RegisterContent'
import MainIndexContent from '@/components/content/MainIndexContent'
import WriteArticle from '../components/content/WriteArticle'
import {clearLoginInfo} from '../utils'

Vue.use(Router)

const User = {
  template: '<div>User {{ $route.params.id }}</div>'
}

let router = new Router({
  mode: 'history',
  scrollBehavior: () => ({y: 0}),
  routes: [{
    path: '/',
    name: 'Index',
    component: Index,
    children: [{
      path: '/',
      name: 'index',
      components: {
        header: SimpleHeader,
        content: MainIndexContent,
        footer: CommonFooter
      },
      meta: {
        title: '博客 | 小学期'
      }
    },
      {
        path: '/user/:id',
        name: 'user',
        components: {
          header: SimpleHeader,
          content: HomeContent,
          footer: CommonFooter
        },
        component: User,
        meta: {
          title: '个人主页 | 博客 | 小学期'
        }
      },
      {
        path: 'login',
        name: 'login',
        components: {
          header: SimpleHeader,
          content: LoginContent,
          footer: CommonFooter
        },
        meta: {
          title: '登录 | 博客 | 小学期'
        }
      },
      {
        path: 'register',
        name: 'register',
        components: {
          header: SimpleHeader,
          content: RegisterContent,
          footer: CommonFooter
        },
        meta: {
          title: '注册 | 博客 | 小学期'
        }
      },
      {
        path: 'article/:articleId',
        name: 'article',
        components: {
          header: SimpleHeader,
          content: ArticleContent,
          footer: CommonFooter
        }
      },
      {
        path: 'articles',
        name: 'articles',
        components: {
          header: SimpleHeader,
          content: ArticleListContent,
          footer: CommonFooter
        },
        meta: {
          title: '文章列表 | 博客 | 小学期'
        }
      },
      {
        path: 'articles/category/:id',
        name: 'articles/category',
        components: {
          header: SimpleHeader,
          content: ArticleListContent,
          footer: CommonFooter
        },
        meta: {
          title: '文章列表 | 博客 | 小学期'
        }
      },
      {
        path: 'articles/search',
        name: 'search',
        components: {
          header: SimpleHeader,
          content: SearchResultContent,
          footer: CommonFooter
        },
        meta: {
          title: '文章搜索 | 博客 | 小学期'
        }
      },
      {
        path: 'articles/write',
        name: 'writeArticle',
        components: {
          header: SimpleHeader,
          content: WriteArticle,
          footer: CommonFooter
        },
        meta: {
          title: '写文章 | 博客 | 小学期'
        }
      },
      {
        path: 'timeline',
        name: 'timeline',
        components: {
          header: SimpleHeader,
          content: TimeLineContent,
          footer: CommonFooter
        },
        meta: {
          title: '时间轴 | 博客 | 小学期'
        }
      }
    ]
  }
  ]
})

// 配置加载进度条
LoadingBar.config({
  color: '#5cb85c',
  failedColor: '#f0ad4e',
  height: 2
})

router.beforeEach((to, from, next) => {
  let token = Vue.cookie.get('blog-token')
  if (!token || !/\S/.test(token)) { // 正则：非空白就匹配
    clearLoginInfo()
    if (to.name === 'writeArticle') {
      next({name: 'login'})
      return
    }
  }
  LoadingBar.start()
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

router.afterEach((to, from, next) => {
  LoadingBar.finish()
  window.scrollTo(0, 0)
})

export default router
