<!--包含空白-->
<template>
  <div class="login-content">
    <blank-content></blank-content>
    <div class="login-main">
      <el-tabs :tab-position="tabPosition" style="height: 400px;">
        <el-tab-pane label="修改个人信息">
          <el-form :model="dataForm" :rules="infoRule" @keyup.enter.native="infoFormSubmit()" ref="dataForm">

            <el-form-item prop="userName">
              <label>用户名</label>
              <el-input :placeholder="dataForm.userName" id="input_username"
                        v-model="dataForm.userName"></el-input>
            </el-form-item>

            <el-form-item prop="email">
              <label>邮箱</label>
              <el-input :disabled="true" :placeholder="dataForm.email"
                        v-model="dataForm.email"></el-input>
            </el-form-item>

            <el-form-item prop="info">
              <label>个人简介</label>
              <el-input placeholder="写一段话介绍一下你自己吧" type="textarea" v-model="dataForm.info"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button @click="infoFormSubmit()" class="login-btn-submit" type="primary">保存</el-button>
            </el-form-item>
          </el-form>

        </el-tab-pane>

        <el-tab-pane label="修改密码">
          <el-form :model="passForm" :rules="dataRule" @keyup.enter.native="passwordFormSubmit()" ref="passForm">

            <el-form-item prop="old_password">
              <label>请输入旧密码</label>
              <el-input type="password" v-model="passForm.old_password"></el-input>
            </el-form-item>

            <el-form-item prop="new_password">
              <label>请输入新密码</label>
              <el-input type="password" v-model="passForm.new_password"></el-input>
            </el-form-item>

            <el-form-item prop="confirm_password">
              <label>确认密码</label>
              <el-input type="password" v-model="passForm.confirm_password"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button @click="passwordFormSubmit()" class="login-btn-submit" type="primary">保存</el-button>
            </el-form-item>
          </el-form>

        </el-tab-pane>

        <el-tab-pane label="修改头像">
          <el-form :model="dataForm" @keyup.enter.native="infoFormSubmit()" ref="dataForm">

            <el-upload
              class="avatar-uploader"
              :action="url"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="dataForm.avatar" :src="dataForm.avatar" class="avatar" alt="头像">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>

            <el-form-item>
              <el-button @click="infoFormSubmit()" class="login-btn-submit" type="primary">保存</el-button>
            </el-form-item>
          </el-form>

        </el-tab-pane>

      </el-tabs>
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
import BlankContent from '@/components/content/BlankContent'
import {isPassword, isUsername} from '../utils/validate'

export default {
  inject: ['reload'],
  components: {
    'blank-content': BlankContent
  },
  created () {
    this.login()
    this.getUserInfo()
  },
  data: function () {
    const validateOldPassword = (rule, value, callback) => {
      if (value.length < 6 || value.length > 20 || !isPassword(value)) {
        callback(new Error('请检查您的密码是否正确'))
      } else {
        callback()
      }
    }
    const validateUsername = (rule, value, callback) => {
      if (!isUsername(value)) {
        callback(new Error('用户名需要4-20个字符，只能包含英文中文下划线'))
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
      } else if (this.dataForm.new_password !== value) {
        callback(new Error('确认密码与密码输入不一致'))
      } else {
        callback()
      }
    }
    return {
      url: '',
      dataForm: {
        userName: '',
        email: '',
        avatar: '',
        uuid: '',
        captcha: '',
        info: '',
        msg: '账号或密码错误！'
      },
      passForm: {
        old_password: '',
        new_password: '',
        confirm_password: ''
      },
      captchaPath: '',
      tabPosition: 'left',
      siteInfo: {
        icp: '筒书',
        copyright: '版权所有 © 2019'
      },
      infoRule: {
        userName: [
          {required: true, message: '用户名不能为空', trigger: 'blur'},
          {validator: validateUsername, trigger: 'blur'}
        ]
      },
      dataRule: {
        old_password: [
          {required: true, message: '旧密码不能为空', trigger: 'blur'},
          {validator: validateOldPassword, trigger: 'blur'}
        ],
        new_password: [
          {required: true, message: '新密码不能为空', trigger: 'blur'},
          {validator: validatePassword, trigger: 'blur'}
        ],
        confirm_password: [
          {required: true, message: '确认密码不能为空', trigger: 'blur'},
          {validator: validateCheckPassword, trigger: 'blur'}
        ]
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
        }
      })
      this.url = this.$http.adornUrl(`/upload?token=${this.$cookie.get('token')}`)
    },
    getUserInfo () {
      this.$http({
        url: this.$http.adornUrl('/getInfo'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.success) {
          this.dataForm.userName = data.data.username
          this.dataForm.email = data.data.email
          this.dataForm.avatar = data.data.avatar
          this.dataForm.info = data.data.info
        }
      })
    },
    passwordFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // this.$http 服务可用于发送 HTTP 请求
          this.$http({
            url: this.$http.adornUrl('/changePassword'), // 请求的地址
            method: 'post', // 请求的方式
            data: this.$http.encryptData({// 请求的数据
              'old': this.dataForm.old_password,
              'new': this.dataForm.new_password
            }, this.key, this.uuid)
          }).then(({data}) => {
            if (data && data.success) {
              this.$message.success('修改成功')
              this.reload()
            } else {
              this.$Message.error(data.msg)
            }
          })
        } else {
          this.$Message.error(this.msg)
        }
      })
    },
    infoFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // this.$http 服务可用于发送 HTTP 请求
          this.$http({
            url: this.$http.adornUrl('/changeInfo'), // 请求的地址
            method: 'post', // 请求的方式
            data: this.$http.adornData({// 请求的数据
              'username': this.dataForm.userName,
              'info': this.dataForm.info,
              'avatar': this.dataForm.avatar
            })
          }).then(({data}) => {
            if (data && data.success) {
              this.$message.success('修改成功')
              localStorage.setItem('blog_username', this.dataForm.userName)
              localStorage.setItem('blog_avatar', data.data.avatar)
              localStorage.setItem('blog_introduce', data.data.info)
              this.reload()
            } else {
              this.$Message.error(data.msg)
            }
          })
        } else {
          this.$Message.error(this.msg)
        }
      })
    },
    handleAvatarSuccess (response) {
      if (response && response.code === 200) {
        this.dataForm.avatar = response.resource.url
        this.$message.success('上传成功！')
      } else {
        this.$message.error('上传失败')
      }
    },
    beforeAvatarUpload (file) {
      const isLt2M = file.size / 1024 / 1024 < 2

      if (file.type !== 'image/jpg' && file.type !== 'image/jpeg' && file.type !== 'image/png') {
        this.$message.error('只支持jpg、png格式的图片！')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
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
    padding: 50px 60px 10px;
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

  .login-content {
    background-image: url("../../assets/bg.png");
    background-repeat: no-repeat;
    background-size: cover;
    background-color: #b8e5f8;
  }

  @import "../../common/stylus/theme.styl"
  .common-footer
    font-weight 300
    line-height 25px
    border-top 0px
    margin 0px
    text-align center
    background rgba(0, 0, 0, 0)

    .copyright
      margin 0px
      color white

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }

</style>
