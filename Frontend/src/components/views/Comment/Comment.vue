<!--评论模块-->
<template>
  <div class="container">
    <div @click="showCommentInput()" class="write-reply">
      <a>
        <i class="el-icon-edit"></i>
        <!--        <span class="add-comment">添加新评论</span>-->
        <el-button type="text" @click="dialogFormVisible = true">添加新评论</el-button>
        <el-dialog v-if="!isLogin" :visible.sync="dialogFormVisible" width="500px">
          <el-form>
            <login-page-content></login-page-content>
          </el-form>
        </el-dialog>
      </a>
    </div>

    <div class="reply">
      <transition name="fade">
        <div class="input-wrapper" v-if="showArticleReplyId === true && isLogin">
          <el-input
            :rows="3"
            autofocus
            class="gray-bg-input"
            placeholder="写下你的评论"
            type="textarea"
            v-model="inputComment"
          ></el-input>
          <div class="btn-control">
            <span @click="cancel" class="cancel">
              <el-button class="btn" round size="mini" type="info">取消</el-button>
            </span>
            <el-button @click="commitComment" class="btn" round size="mini" type="primary">确定</el-button>
          </div>
        </div>
      </transition>
    </div>

    <div class="comment" v-for="item in comments">
      <div class="info">
        <el-avatar class="avatar" :src="item.avatar"></el-avatar>
<!--        <el-avatar :src="item.avatar" class="avatar" height="36" width="36" alt="头像"/></el-avatar>-->
        <div class="right">
          <div class="name">{{item.userName}}</div>
          <div class="date">{{item.createTime | socialDate}}</div>
        </div>
      </div>
      <div class="content">{{item.content}}</div>

      <div class="control">
        <span @click="showCommentInput(item), dialogFormVisible = true" class="comment-reply">
          <i class="iconfont el-icon-thirdxiaoxi"></i>
          <span>回复</span>
        </span>
        <el-dialog v-if="!isLogin" :visible.sync="dialogFormVisible" width="500px">
          <el-form>
            <login-page-content></login-page-content>
          </el-form>
        </el-dialog>
        <span v-if="showDelete(item)" v-show="true" class="comment-reply" @click="deleteComment(item)">
          <i class="icon-comment el-icon-thirdshangchu"></i>
          <span>删除</span>
        </span>
      </div>

      <div class="reply">
        <transition name="fade">
          <div class="input-wrapper" v-if="showSecondReplyId === item.commentId && isLogin">
            <el-input
              :rows="3"
              autofocus
              class="gray-bg-input"
              placeholder="写下你的评论"
              type="textarea"
              v-model="inputComment"
            ></el-input>
            <div class="btn-control">
              <span @click="cancel" class="cancel">
              <el-button class="btn" round size="mini" type="info">取消</el-button>
            </span>
              <el-button @click="commitComment(item)" class="btn" round size="mini" type="primary">确定</el-button>
            </div>
          </div>
        </transition>
      </div>

      <div class="reply">
        <div class="item" v-for="reply in item.commentList">
          <div class="reply-content">
            <span class="from-name">{{reply.userName}}</span>
            <span>:</span>
            <span>{{reply.content}}</span>
          </div>
          <div class="reply-bottom">
            <span>{{reply.createTime | socialDate}}</span>
            <span @click="showCommentInput(item, reply), dialogFormVisible = true" class="reply-text">
             <i class="icon-comment el-icon-thirdxiaoxi"></i>
              <span>回复</span>
            </span>

            <el-dialog v-if="!isLogin" :visible.sync="dialogFormVisible" width="500px">
              <el-form>
                <login-page-content></login-page-content>
              </el-form>
            </el-dialog>

            <span v-if="showDelete(reply)" class="reply-text" @click="deleteComment(reply)">
              <i class="icon-comment el-icon-thirdshangchu"></i>
              <span>删除</span>
            </span>

          </div>

          <transition name="fade">
            <div class="input-wrapper" v-if="showThirdReplyId === reply.commentId && isLogin">
              <el-input
                :rows="3"
                autofocus
                class="gray-bg-input"
                placeholder="写下你的评论"
                type="textarea"
                v-model="inputComment"
              ></el-input>
              <div class="btn-control">
              <span @click="cancel" class="cancel">
              <el-button class="btn" round size="mini" type="info">取消</el-button>
            </span>
                <el-button @click="commitComment(reply)" class="btn" round size="mini" type="primary">确定</el-button>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LoginPageContent from '@/components/content/LoginPageContent'
import SocialSection from './SocialSection'
import {mixin} from '@/utils'
import default_avatar from '../../../assets/circle_avatar.png'
import {clearLoginInfo} from '../../../utils'

export default {
  props: {
    articleId: String
  },
  mixins: [mixin],
  inject: ['reload'],
  components: {
    'login-page-content': LoginPageContent,
    'social-section': SocialSection
  },
  data () {
    return {
      comments: null,
      dialogFormVisible: false,
      inputComment: '',
      showArticleReplyId: false, // 控制文章评论的展示
      showSecondReplyId: '', // 控制二级评论的显示
      showThirdReplyId: '', // 控制三级评论的显示
      isLogin: false,
      userName: '',
      avatar: ''
      // formLabelWidth: '120px',
    }
  },
  computed: {},
  methods: {
    /**
             * 点击取消按钮
             */
    cancel () {
      this.inputComment = ''
      this.showArticleReplyId = false
      this.showSecondReplyId = ''
      this.showThirdReplyId = ''
    },
    getAvatar (item) {

    },
    selectStyle (item) {

    },
    showDelete (item) {
      return this.userName === item.userName
    },
    deleteComment (item) {
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
                        url: this.$http.adornUrl('/deleteComment/' + item.commentId),
                        method: 'delete'
                      }).then(({data}) => {
                        if (data && data.success) {
                          this.$message.success('删除评论成功')
                          this.reload()
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
      })
    },
    /**
             * 提交评论
             */
    commitComment (item) {
      // const reg = /(^@.*?)\s(.*$)/
      // this.inputComment = this.inputComment.replace(reg, '$2')
      if (this.inputComment) {
        if (item.commentId) {
          this.$http({
            url: this.$http.adornUrl('/createComment'), // 请求的地址
            method: 'post', // 请求的方式
            data: this.$http.adornData({
              // 请求的数据
              parentId: item.commentId,
              content: this.inputComment
            })
          }).then(({data}) => {
            if (data && data.success) {
              this.$message.success('发送成功')
            } else {
              this.$Message.error(data.msg)
            }
          })
        } else {
          this.$http({
            url: this.$http.adornUrl('/createComment'), // 请求的地址
            method: 'post', // 请求的方式
            data: this.$http.adornData({
              // 请求的数据
              articleId: this.articleId,
              content: this.inputComment
            })
          }).then(({data}) => {
            if (data && data.success) {
              this.$message.success('发送成功')
            } else {
              this.$Message.error(data.msg)
            }
          })
        }
        this.reload()
      }
      this.cancel() // 点击确定后取消评论框的显示
    },

    /**
             * 点击评论按钮显示输入框
             * item: 当前大评论
             * reply: 当前回复的评论
             */
    showCommentInput (item, reply) {
      // let token = Vue.cookie.get('blog-token')
      // if (!token || !/\S/.test(token)) { // 正则：非空白就匹配
      //   clearLoginInfo()
      //   this.$router.replace({name: 'login'})
      // }
      this.cancel()
      if (!this.isLogin) {
        return
      }
      if (reply) {
        this.showThirdReplyId = reply.commentId
        this.inputComment = '@' + reply.userName + ' '
      } else if (item) {
        this.cancel()
        this.showSecondReplyId = item.commentId
        this.inputComment = '@' + item.userName + ' '
      } else {
        this.cancel()
        this.showArticleReplyId = true
      }
    }
  },
  created () {
    this.$http({
      url: this.$http.adornUrl('/showComments/' + this.articleId),
      method: 'get'
    }).then(({data}) => {
      if (data && data.success) {
        this.comments = data.data
        this.comments.forEach(value => {
          if(!value.avatar) {
            value.avatar = default_avatar
          }
        })
      }
    })
    this.userName = localStorage.getItem('blog_username')
    this.isLogin = this.userName != null
  }
}
</script>

<style lang="scss" scoped>
  @import "../../../common/scss/index";

  .container {
    padding: 0 10px;
    box-sizing: border-box;

    .comment {
      display: flex;
      flex-direction: column;
      padding: 10px;
      border-bottom: 1px solid $border-fourth;

      .info {
        display: flex;
        align-items: center;

        .avatar {
          border-radius: 50%;
        }

        .right {
          display: flex;
          flex-direction: column;
          margin-left: 10px;

          .name {
            font-size: 16px;
            color: $text-main;
            margin-bottom: 5px;
            font-weight: 500;
          }

          .date {
            font-size: 12px;
            color: $text-minor;
          }
        }
      }

      .content {
        font-size: 16px;
        color: $text-main;
        line-height: 20px;
        padding: 10px 0;
      }

      .control {
        display: flex;
        align-items: center;
        font-size: 14px;
        color: $text-minor;

        .like {
          display: flex;
          align-items: center;
          margin-right: 20px;
          cursor: pointer;

          &.active,
          &:hover {
            color: $color-main;
          }

          .iconfont {
            font-size: 14px;
            margin-right: 5px;
          }
        }

        .comment-reply {
          display: flex;
          align-items: center;
          margin-right: 20px;
          cursor: pointer;

          &:hover {
            color: $text-333;
          }

          .iconfont {
            font-size: 16px;
            margin-right: 5px;
          }
        }
      }

      .reply {
        margin: 5px 0;
        border-left: 2px solid $border-first;

        .item {
          margin: 0 10px;
          padding: 10px 0;
          border-bottom: 1px dashed $border-third;

          .reply-content {
            display: flex;
            align-items: center;
            font-size: 14px;
            color: $text-main;

            .from-name {
              color: $color-main;
            }

            .to-name {
              color: $color-main;
              margin-left: 5px;
              margin-right: 5px;
            }
          }

          .reply-bottom {
            display: flex;
            align-items: center;
            margin-top: 6px;
            font-size: 12px;
            color: $text-minor;

            .reply-text {
              display: flex;
              align-items: center;
              margin-left: 10px;
              cursor: pointer;

              &:hover {
                color: $text-333;
              }

              .icon-comment {
                margin-right: 5px;
              }
            }
          }
        }

        .write-reply {
          display: flex;
          align-items: center;
          font-size: 14px;
          color: $text-minor;
          padding: 10px;
          cursor: pointer;

          &:hover {
            color: $text-main;
          }

          .el-icon-edit {
            margin-right: 5px;
          }
        }

        .fade-enter-active,
        fade-leave-active {
          transition: opacity 0.5s;
        }

        .fade-enter,
        .fade-leave-to {
          opacity: 0;
        }

        .input-wrapper {
          padding: 10px 0;

          .gray-bg-input,
          .el-input__inner {
            /*background-color: #67C23A;*/
          }

          .btn-control {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            padding-top: 10px;

            .cancel {
              font-size: 16px;
              color: $text-normal;
              margin-right: 20px;
              cursor: pointer;

              &:hover {
                color: $text-333;
              }
            }

            .confirm {
              font-size: 16px;
            }
          }
        }
      }
    }
  }
</style>
