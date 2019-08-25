<template>
  <div class="register-content">
    <div class="register-main">
      <h1 class="register-title">请注册</h1><br><br>
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
        <!--        prop属性绑定-->
        <el-form-item prop="email">
          <el-input v-model="dataForm.email" placeholder="邮箱"></el-input>
        </el-form-item>

        <el-form-item prop="captcha">
          <el-input v-model="dataForm.captcha" placeholder="邮箱中收到的验证码" class="email-captcha"></el-input>
          <el-button class="register-btn-get-captcha" type="primary" @click="getCaptcha()" :disabled=disabled>
            {{btn_text}}
          </el-button>
        </el-form-item>

        <el-form-item prop="userName">
          <el-input v-model="dataForm.userName" placeholder="用户名"></el-input>
        </el-form-item>

        <el-form-item prop="password" :class="{ 'is-required': !dataForm.id }">
          <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
        </el-form-item>

        <el-form-item prop="password_check" :class="{ 'is-required': !dataForm.id }">
          <el-input v-model="dataForm.confirmPassword" type="password" placeholder="请确认您的密码"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="register-btn-submit" type="primary" @click="dataFormSubmit()">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import {isEmail} from '../utils/validate'
  import {getUUID} from '../../utils'

  export default {
    created () {
      this.register()
    },
    data () {
      const validateCaptcha = (rule, value, callback) => {
        if (!this.dataForm.captcha && !/\S/.test(value)) {
          callback(new Error('验证码不能为空'))
        } else {
          callback()
        }
      }
      const validatePassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          callback(new Error('密码不能为空'))
        } else {
          callback()
        }
      }
      const validateConfirmPassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          callback(new Error('确认密码不能为空'))
        } else if (this.dataForm.password !== value) {
          callback(new Error('确认密码与密码输入不一致'))
        } else {
          callback()
        }
      }
      const validateEmail = (rule, value, callback) => {
        if (!isEmail(value)) {
          callback(new Error('邮箱格式错误'))
        } else {
          callback()
        }
      }
      return {
        btn_text: '获取验证码',
        disabled: false,
        dataForm: {
          id: 0,
          captcha: '',
          userName: '',
          password: '',
          confirmPassword: '',
          // salt: '',
          email: ''
        },
        dataRule: {
          captcha: [
            {required: true, message: '验证码不能为空', trigger: 'blur'}
          ],
          userName: [
            {required: true, message: '用户名不能为空', trigger: 'blur'}
          ],
          password: [
            {validator: validatePassword, trigger: 'blur'}
          ],
          confirmPassword: [
            {validator: validateConfirmPassword, trigger: 'blur'}
          ],
          email: [
            {required: true, message: '邮箱不能为空', trigger: 'blur'},
            {validator: validateEmail, trigger: 'blur'}
          ]
        },
        emailRule: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'},
          {validator: validateEmail, trigger: 'blur'}
        ]
      }
    },
    methods: {
      register () {
        this.$http({
          url: this.$http.adornUrl('/register'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 200) {
            console.log(data)
          }
        })
      },
      getCaptcha () {//获取验证码
        if (isEmail(this.dataForm.email)) {
          this.dataForm.uuid = getUUID()
          this.$http({
            url: this.$http.adornUrl('/register-captcha'), // 请求的地址
            method: 'post', // 请求的方式
            data: this.$http.adornData({// 请求的数据
              'email': this.dataForm.email,
              'uuid': this.dataForm.password
            })
          }).then(res => {
            this.time = 60
            this.disabled = true
            this.email_timer()
          }).catch(err => {
            console.info('报错的信息', err.response.message)
          })
        } else {
          alert('请输入正确的邮箱')
        }
      },
      email_timer () {//邮件发送时间间隔
        if (this.time > 0) {
          --this.time
          this.btn_text = this.time + '秒后重新获取验证码'
          setTimeout(this.email_timer, 1000)
        } else {
          this.time = 0
          this.btn_text = '获取验证码'
          this.disabled = false
        }
      },
      dataFormSubmit () {//提交信息
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // this.$http 服务可用于发送 HTTP 请求
            this.$http({
              url: this.$http.adornUrl('/register'), // 请求的地址
              method: 'post', // 请求的方式
              data: this.$http.adornData({// 请求的数据
                'captcha': this.dataForm.captcha,
                'userId': this.dataForm.id || undefined,
                'username': this.dataForm.userName,
                'password': this.dataForm.password,
                // 'salt': this.dataForm.salt,
                'email': this.dataForm.email
              })
            }).then(res => {
              console.info('后台返回的数据', res.data)
            }).catch(err => {
              console.info('报错的信息', err.response.message)
            })
          } else {
            this.$Message.error(this.msg)
          }
        })
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .register-main {
    position: relative;
    margin: 0 auto;
    /*border: black solid 1px;*/
    /*position: absolute;*/
    text-align: center;
    top: 0;
    right: 0;
    padding: 100px 60px 180px;
    width: 470px;
    min-height: 100%;
    background-color: #fff;
  }

  .register-title {
    font-size: 25px;
  }

  .email-captcha {
    width: 60%;
    float: left;
    padding-right: 0px;
  }

  .register-btn-get-captcha {
    width: 35%;
    float: right;
    overflow: hidden;
  }

  > img {
    width: 100%;
    cursor: pointer;
  }

  .register-btn-submit {
    width: 100%;
    margin-top: 18px;
  }

  .register-content
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
