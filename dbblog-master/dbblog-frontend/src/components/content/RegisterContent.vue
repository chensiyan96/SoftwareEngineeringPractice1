<template>
  <div>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon
             label-width="80px">
      <!--        prop属性绑定-->
      <el-form-item label="邮箱" prop="email" ref="email" :rules="emailRule">
        <el-input v-model="dataForm.email" placeholder="邮箱"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button class="register-btn-get-captcha" type="primary" @click="getCaptcha()" :disabled="disabled">
          {{btn_text}}
        </el-button>
      </el-form-item>

      <el-form-item label="用户名" prop="userName">
        <el-input v-model="dataForm.userName" placeholder="用户名"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="password" :class="{ 'is-required': !dataForm.id }">
        <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
      </el-form-item>

      <el-form-item label="确认密码" prop="password_check" :class="{ 'is-required': !dataForm.id }">
        <el-input v-model="dataForm.confirmPassword" type="password" placeholder="请确认您的密码"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button class="register-btn-submit" type="primary" @click="dataFormSubmit()">注册</el-button>
      </el-form-item>
    </el-form>
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
        userName: '',
        password: '',
        confirmPassword: '',
        salt: '',
        email: ''
      },
      dataRule: {
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
    getCaptcha () {
      this.$ref['email'].resetFields()
      console.log(this.$ref)
      this.$ref['email'].resetFields()
      this.$ref['email'].validate((valid) => {
        console.log(1)
        if (valid) {
          this.dataForm.uuid = getUUID()
          this.$http({
            url: this.$http.adornUrl('/register-captcha'), // 请求的地址
            method: 'post', // 请求的方式
            data: this.$http.adornData({// 请求的数据
              'email': this.dataForm.email,
              'uuid': this.dataForm.password
            })
          }).then(res => {
            console.info('后台返回的数据', res.data)
            this.time = 60
            this.disabled = true
            this.email_timer()
          }).catch(err => {
            console.info('报错的信息', err.response.message)
          })
        } else {
          this.$Message.error(this.msg)
        }
      })
    },
    email_timer () {
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
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // this.$http 服务可用于发送 HTTP 请求
          this.$http({
            url: this.$http.adornUrl('/register'), // 请求的地址
            method: 'post', // 请求的方式
            data: this.$http.adornData({// 请求的数据
              'userId': this.dataForm.id || undefined,
              'username': this.dataForm.userName,
              'password': this.dataForm.password,
              'salt': this.dataForm.salt,
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
