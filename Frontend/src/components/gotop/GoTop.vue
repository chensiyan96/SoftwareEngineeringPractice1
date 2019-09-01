<template>
  <!--<transition name="el-zoom-in-center">-->
  <transition name="el-fade-in">
    <div @click="toTop" v-show="topShow" class="me-to-top"><el-icon class="el-icon-caret-top" style="color: darkgray" ></el-icon></div>
  </transition>
</template>

<script>
export default {
  name: 'GoTop',
  data () {
    return {
      topShow: false,
      scrollTime: 50,   //保证不论文章有多长，卷上去的时间是一致的
      id:'',
      scrollSpeed:''
    }
  },
  methods: {
    toTop() {
      this.scrollSpeed = this.getCurHeight() / this.scrollTime
      this.id = setInterval(this.toStepTop,1)
    },
    toStepTop () {
      if(document.body.scrollTop > 0){
        document.body.scrollTop -= this.scrollSpeed
      }else if(document.documentElement.scrollTop > 0){
        document.documentElement.scrollTop -= this.scrollSpeed
      }
      if(this.getCurHeight() <= 0){
        clearInterval(this.id)
      }
    },
    getCurHeight(){
      return document.documentElement.scrollTop + document.body.scrollTop
    },
    needToTop () {
      let curHeight = document.documentElement.scrollTop + document.body.scrollTop

      if (curHeight > 400) {
        this.topShow = true
      } else {
        this.topShow = false
      }
    }
  },
  mounted () {
    /**
       * 等到整个视图都渲染完毕
       */
    this.$nextTick(function () {
      window.addEventListener('scroll', this.needToTop)
    })
  }
}
</script>

<style>
  .me-to-top {
    background-color: #fff;
    position: fixed;
    right: 70px;
    bottom: 100px;
    width: 40px;
    height: 40px;
    border-radius: 20px;
    cursor: pointer;
    transition: .3s;
    box-shadow: 0 0 6px rgba(0, 0, 0, .12);
    z-index: 5;
    color: #2d8cf0
  }

  .me-to-top i {
    color: #00d1b2;
    display: block;
    line-height: 40px;
    text-align: center;
    font-size: 18px;
  }

</style>
