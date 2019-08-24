<template>
  <div class="login-main">
    <h3 class="login-title">欢迎使用小学期博客网！</h3>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" status-icon>
      <!--        prop属性绑定-->
      <el-form-item prop="userName">
        <el-input v-model="dataForm.userName" placeholder="帐号" auto-complete="on"></el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input v-model="dataForm.password" type="password" placeholder="密码" auto-complete="off"></el-input>
      </el-form-item>

      <el-form-item prop="captcha">
        <el-row :gutter="20">
          <el-col :span="14">
            <el-input v-model="dataForm.captcha" placeholder="验证码" @keyup.enter.native="dataFormSubmit()">
            </el-input>
          </el-col>
          <el-col :span="10" class="login-captcha">
            <img :src="captchaPath" @click="getCaptcha()" alt="">
          </el-col>
        </el-row>
      </el-form-item>

      <div>
        <el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
      </div>

      <div>
        <a :href="dataForm.signInUrl">没有账号？点击注册</a>
      </div>

    </el-form>
  </div>
</template>

<script>

import {getUUID} from '@/utils'

export default {
  created () {
    // this.login()
    this.getCaptcha()
  },
  data: function () {
    return {
      dataForm: {
        userName: '',
        password: '',
        uuid: '',
        captcha: '',
        msg: '账号或密码错误！',
        signInUrl: '/register'
      },
      dataRule: {
        userName: [
          {required: true, message: '帐号不能为空', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'}
        ],
        captcha: [
          {required: true, message: '验证码不能为空', trigger: 'blur'}
        ]
      },
      captchaPath: ''
    }
  },
  methods: {
    login () {
      this.$http({
        url: this.$http.adornUrl('/login'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 200) {
          console.log(data)
        }
      })
    },
    dataFormSubmit () {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          // this.$http 服务可用于发送 HTTP 请求
          this.$http({
            url: this.$http.adornUrl('/admin/sys/login'), // 请求的地址
            method: 'post', // 请求的方式
            data: this.$http.adornData({// 请求的数据
              'username': this.dataForm.userName,
              'password': this.dataForm.password,
              'uuid': this.dataForm.uuid,
              'captcha': this.dataForm.captcha
            })
          }).then(({data}) => {
            if (data && data.code === 200) {
              this.$cookie.set('token', data.token)
              this.$router.replace({name: 'home'})
            } else {
              this.getCaptcha()
              this.$Message.error(data.msg)
            }
          })
          // }).then(res => {
          //   console.info('后台返回的数据', res.data)
          // }).catch(err => {
          //   console.info('报错的信息', err.response.message)
          // })
        } else {
          this.$Message.error(this.msg)
        }
      })
    },
    // 获取验证码
    getCaptcha () {
      this.dataForm.uuid = getUUID()
      this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${this.dataForm.uuid}`)
    }
  }
}
</script>

<style lang="scss">
  .login-main {
    position: absolute;
    top: 0;
    right: 0;
    padding: 150px 60px 180px;
    width: 470px;
    min-height: 100%;
    background-color: #fff;
  }

  .login-title {
    font-size: 16px;
  }

  .login-captcha {
    overflow: hidden;
  }

  > img {
    width: 100%;
    cursor: pointer;
  }

  .login-btn-submit {
    width: 100%;
    margin-top: 38px;
  }

</style>
