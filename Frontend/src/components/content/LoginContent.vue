<template>
  <div class="login-content">
    <div class="login-main">
      <h1 class="login-title">请登录</h1><br><br>
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()">
        <!--prop属性绑定-->
        <el-form-item prop="userName">
          <el-input v-model="dataForm.userName" placeholder="邮箱/手机号"></el-input>
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
              <img :src="captchaPath" @click="getCaptcha()" alt="验证码">
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
        created() {
            // this.login();
            this.getCaptcha()
        },
        data: function () {
            return {
                dataForm: {
                    userName: '',
                    password: '',
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
            login() {
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
            dataFormSubmit() {
                this.$refs['dataForm'].validate((valid) => {
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
                                this.$message.success('登录成功');
                                localStorage.setItem('blog_username', this.dataForm.userName);
                                this.$cookie.set('blog-token', data.token);
                                this.$router.replace({name: 'index'})
                            } else {
                                this.getCaptcha();
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
            getCaptcha() {
                this.dataForm.uuid = getUUID();
                this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${this.dataForm.uuid}`)
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
    padding: 150px 60px 180px;
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

  .login-content
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
