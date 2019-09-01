<!--不包含空白-->
<template>
  <div class="login-content">
    <div class="login-main">
      <h1 class="login-title">请先登录</h1><br><br>
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()">
        <!--prop属性绑定-->
        <el-form-item prop="userName">
          <el-input v-model="dataForm.userName" placeholder="邮箱"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
        </el-form-item>

        <el-form-item prop="captcha">
          <el-row :gutter="20">
            <el-col :span="10">
              <el-input v-model="dataForm.captcha" placeholder="验证码">
              </el-input>
            </el-col>
            <el-col :span="13" class="login-captcha">
              <img width="100%" height="100%" :src="captchaPath" @click="getCaptcha()" alt="验证码">
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item>
          <el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
        </el-form-item>

        <el-form-item>
          <router-link to="/register">没有账号？点击注册</router-link>
        </el-form-item>
      </el-form>
    </div>
  </div>

</template>

<script type="text/ecmascript-6">

    import {getUUID} from '@/utils'

    export default {
        inject: ['reload'],
        created() {
            this.login();
            this.getCaptcha()
        },
        data: function () {
            return {
                dataForm: {
                    userName: this.$cookie.get('blog-email'),
                    password: this.$cookie.get('blog-password'),
                    uuid: '',
                    captcha: '',
                    msg: '账号或密码错误！'
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
            url: this.$http.adornUrl('/getPublicKey'),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.success) {
              this.uuid = data.data.uuid
              this.key = data.data.key
              this.getCaptcha()
            }
          })
        },
        dataFormSubmit () {
          this.$refs['dataForm'].validate((valid) => {
            if (valid) {
              // this.$http 服务可用于发送 HTTP 请求
              this.$http({
                url: this.$http.adornUrl('/login'), // 请求的地址
                method: 'post', // 请求的方式
                data: this.$http.encryptData({// 请求的数据
                  'email': this.dataForm.userName,
                  'password': this.dataForm.password,
                  'captcha': this.dataForm.captcha
                }, this.key, this.uuid)
              }).then(({data}) => {
                if (data && data.success) {
                  this.$message.success('登录成功')
                  localStorage.setItem('blog_userId', data.data.userId)
                  localStorage.setItem('blog_username', data.data.username)
                  localStorage.setItem('blog_introduce', data.data.info)
                  localStorage.setItem('blog_email', this.dataForm.userName)

                  this.$cookie.set('blog-token', data.data.token)
                  if (this.checked) {
                    this.$cookie.set('blog-email', this.dataForm.userName)
                    this.$cookie.set('blog-password', this.dataForm.password)
                  }
                  this.$router.replace({name: 'index'}) // 跳转到主页
                  this.reload()
                } else {
                  this.getCaptcha()
                  this.$Message.error(data.msg)
                }
              })
            } else {
              this.$Message.error(this.msg)
            }
          })
        },
        // 获取验证码
        getCaptcha () {
          this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${this.uuid + '&useless=' + Math.random()}`)
        }
      }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .login-main {
    position: relative;
    margin: 0 auto;
    /*border: black solid 1px;*/
    /*position: absolute;*/
    text-align: center;
    top: 0;
    right: 0;
    padding: 0px 60px 10px;
    width: 470px;
    min-height: 100%;
    background-color: #fff;
  }

  .login-title {
    font-size: 25px;
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
    margin-top: 18px;
  }

</style>
