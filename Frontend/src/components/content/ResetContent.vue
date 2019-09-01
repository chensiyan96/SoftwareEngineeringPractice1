<template>
  <div class="register-content">
    <blank-content></blank-content>
    <div class="register-main">
      <h1 class="register-title">重置密码</h1><br><br>
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
        <!--prop属性绑定-->
        <el-form-item prop="email">
          <el-input v-model="dataForm.email" placeholder="邮箱" autocomplete="on" ></el-input>
        </el-form-item>

        <el-form-item prop="captcha">
          <el-col :span="12" :offset="0" class="email-captcha">
            <el-input :plain="true" v-model="dataForm.captcha" placeholder="收到的验证码"></el-input>
          </el-col>
          <el-button :plain="true" class="register-btn-get-captcha" type="primary" @click="getCaptcha()" :disabled=disabled>
            {{btn_text}}
          </el-button>
        </el-form-item>

        <el-form-item prop="password" :class="{ 'is-required': !dataForm.id }">
          <el-input v-model="dataForm.password" type="password" placeholder="密码" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item prop="password_check" :class="{ 'is-required': !dataForm.id }">
          <el-input v-model="dataForm.password_check" type="password" placeholder="请确认您的密码" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="register-btn-submit" type="primary" @click="dataFormSubmit()">确认</el-button>
        </el-form-item>

        <el-form-item>
          <router-link to="/login">返回登录</router-link>
        </el-form-item>

      </el-form>
    </div>
    <blank-content></blank-content>
    <div class="common-footer">
      <p class="copyright">
        {{siteInfo.icp}}
        <span>|</span>
        {{ siteInfo.copyright }}
      </p>
    </div>
  </div>
</template>

<script>
    import {isEmail, isPassword, isUsername} from '../utils/validate'
    import BlankContent from '@/components/content/BlankContent'
    import {getUUID} from '../../utils'

    export default {
        created () {
            this.register()
        },
        components: {
            'blank-content': BlankContent
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
                if (value.length < 6 || value.length > 20) {
                    callback(new Error('密码长度需要在6-20个字符'))
                } else if (!isPassword(value)) {
                    callback(new Error('密码至少应包含大小写英文字母、数字和特殊字符中的两项'))
                } else {
                    callback()
                }
            }
            const validateCheckPassword = (rule, value, callback) => {
                if (!this.dataForm.password_check && !/\S/.test(value)) {
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
                    password: '',
                    password_check: '',
                    email: ''
                },
                dataRule: {
                    captcha: [
                        {required: true, message: '验证码不能为空', trigger: 'blur'},
                        {validator: validateCaptcha, trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '密码不能为空', trigger: 'blur'},
                        {validator: validatePassword, trigger: 'blur'}
                    ],
                    password_check: [
                        {required: true, message: '确认密码不能为空', trigger: 'blur'},
                        {validator: validateCheckPassword, trigger: 'blur'}
                    ],
                    email: [
                        {required: true, message: '邮箱不能为空', trigger: 'blur'},
                        {validator: validateEmail, trigger: 'blur'}
                    ]
                },
                siteInfo: {
                    icp: '筒书',
                    copyright: '版权所有 © 2019'
                }
            }
        },
        methods: {
            register () {
                this.$http({
                    url: this.$http.adornUrl('/getPublicKey'),
                    method: 'get',
                    params: this.$http.adornParams()
                }).then(({data}) => {
                    if (data && data.success) {
                        this.uuid = data.data.uuid
                        this.key = data.data.key
                    }
                })
            },
            getCaptcha () { // 获取验证码
                if (isEmail(this.dataForm.email)) {
                    this.time = 60
                    this.disabled = true
                    this.email_timer()
                    this.$http({
                        url: this.$http.adornUrl('/sendEmailCaptcha'), // 请求的地址
                        method: 'post', // 请求的方式
                        data: this.$http.adornData({// 请求的数据
                            'email': this.dataForm.email
                        })
                    }).then(res => {
                    }).catch(err => {
                        console.info('报错的信息', err.response.message)
                    })
                } else {
                    this.$message.error('请输入正确的邮箱')
                }
            },
            email_timer () { // 邮件发送时间间隔
                if (this.time > 0) {
                    --this.time
                    this.btn_text = this.time + '秒后重新获取'
                    setTimeout(this.email_timer, 1000)
                } else {
                    this.time = 0
                    this.btn_text = '获取验证码'
                    this.disabled = false
                }
            },
            dataFormSubmit () { // 提交信息
                this.$refs['dataForm'].validate((valid) => {
                    if (valid) {
                        // this.$http 服务可用于发送 HTTP 请求
                        this.$http({
                            url: this.$http.adornUrl('/resetPassword'), // 请求的地址
                            method: 'post', // 请求的方式
                            data: this.$http.encryptData({// 请求的数据
                                'email': this.dataForm.email,
                                'password': this.dataForm.password,
                                'captcha': this.dataForm.captcha
                            }, this.key, this.uuid)
                        }).then(({data}) => {
                            if (data && data.success) {
                                this.$message.success('重置密码成功')
                                localStorage.setItem('blog_username', data.data.username)
                                this.$cookie.set('blog-token', data.data.token)
                                this.$router.replace({name: 'index'})
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
    padding: 50px 60px 10px;
    width: 450px;
    min-height: 100%;
    background-color: #fff;
  }

  .register-title {
    font-size: 25px;
  }

  .email-captcha {
    float: left;
    padding-right: 0;
  }

  .register-btn-get-captcha {
    width: 45%;
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

  .register-content {
    background-image: url("../../assets/bg.png");
    background-repeat: no-repeat;
    background-size: cover;
    background-color: #b8e5f8;
  }

  @import "../../common/stylus/theme.styl"
  .common-footer
    font-weight 300
    line-height 25px
    border-top 0
    margin 0
    text-align center
    background rgba(0, 0, 0, 0)

    .copyright
      margin 0
      color white
</style>
