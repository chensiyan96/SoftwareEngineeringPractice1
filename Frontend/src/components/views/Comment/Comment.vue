<!--评论模块-->
<template>
	<div class="container">
		<div class="write-reply" @click="showCommentInput()">
			<i class="el-icon-edit"></i>
			<span class="add-comment"><a>添加新评论</a></span>
		</div>

		<div class="reply">
			<transition name="fade">
				<div class="input-wrapper" v-if="showArticleReplyId === true">
					<el-input class="gray-bg-input"
					          v-model="inputComment"
					          type="textarea"
					          :rows="3"
					          autofocus
					          placeholder="写下你的评论">
					</el-input>
					<div class="btn-control">
						<span class="cancel" @click="cancel">取消</span>
						<el-button class="btn" type="success" round @click="commitComment">确定</el-button>
					</div>
				</div>
			</transition>
		</div>


		<div class="comment" v-for="item in comments">
			<div class="info">
				<img class="avatar" :src="item.fromAvatar" width="36" height="36"/>
				<div class="right">
					<div class="name">{{item.fromName}}</div>
					<div class="date">{{item.date}}</div>
				</div>
			</div>
			<div class="content">{{item.content}}</div>

			<div class="control">
        <span class="like" :class="{active: item.isLike}" @click="likeClick(item)">
          <i class="iconfont icon-like"></i>
          <span class="like-num">{{item.likeNum > 0 ? item.likeNum + '人赞' : '赞'}}</span>
        </span>
				<span class="comment-reply" @click="showCommentInput(item)">
          <i class="iconfont icon-comment"></i>
          <span>回复</span>
        </span>
			</div>

			<div class="reply">
				<transition name="fade">
					<div class="input-wrapper" v-if="showSecondReplyId === item.id">
						<el-input class="gray-bg-input"
						          v-model="inputComment"
						          type="textarea"
						          :rows="3"
						          autofocus
						          placeholder="你的评论">
						</el-input>
						<div class="btn-control">
							<span class="cancel" @click="cancel">取消</span>
							<el-button class="btn" type="success" round @click="commitComment">确定</el-button>
						</div>
					</div>
				</transition>
			</div>

			<div class="reply">
				<div class="item" v-for="reply in item.reply">
					<div class="reply-content">
						<span class="from-name">{{reply.fromName}}</span><span>: </span>
						<span class="to-name">@{{reply.toName}}</span>
						<span>{{reply.content}}</span>
					</div>
					<div class="reply-bottom">
						<span>{{reply.date}}</span>
						<span class="reply-text" @click="showCommentInput(item, reply)">
              <i class="iconfont icon-comment"></i>
              <span>回复</span>
            </span>
					</div>
				</div>

				<transition name="fade">
					<div class="input-wrapper" v-if="showThirdReplyId === item.id">
						<el-input class="gray-bg-input"
						          v-model="inputComment"
						          type="textarea"
						          :rows="3"
						          autofocus
						          placeholder="写下你的评论">
						</el-input>
						<div class="btn-control">
							<span class="cancel" @click="cancel">取消</span>
							<el-button class="btn" type="success" round @click="commitComment">确定</el-button>
						</div>
					</div>
				</transition>

			</div>
		</div>
	</div>
</template>

<script>

  import Vue from 'vue'
  import {clearLoginInfo} from '../../../utils'

  export default {
    props: {
      comments: {
        type: Array,
        required: true
      }
    },
    components: {},
    data () {
      return {
        inputComment: '',
        showArticleReplyId: false, // 控制文章评论的展示
        showSecondReplyId: '', // 控制二级评论的显示
        showThirdReplyId: '' // 控制三级评论的显示
      }
    },
    computed: {},
    methods: {
      /**
       * 点赞
       */
      likeClick (item) {
        if (item.isLike === null) {
          Vue.$set(item, 'isLike', true)
          item.likeNum++
        } else {
          if (item.isLike) {
            item.likeNum--
          } else {
            item.likeNum++
          }
          item.isLike = !item.isLike
        }
      },
      getTime () { // 获取当前系统时间
        var date = new Date()
        var nowTime = new Array(6)
        nowTime[0] = date.getFullYear()
        nowTime[1] = date.getMonth() + 1
        nowTime[2] = date.getDate()
        nowTime[3] = date.getHours()
        nowTime[4] = date.getMinutes()
        nowTime[5] = date.getSeconds()

        for (let i = 0; i < nowTime.length; i++) {
          if (nowTime[i] < 10) {
            nowTime[i] = '0' + nowTime[i]
          }
        }
        return nowTime[0] + '-' +
          nowTime[1] + '-' +
          nowTime[2] + ' ' +
          nowTime[3] + ':' +
          nowTime[4] + ':' +
          nowTime[5]
      },

      /**
       * 点击取消按钮
       */
      cancel () {
        this.inputComment = ''
        this.showArticleReplyId = false
        this.showSecondReplyId = ''
        this.showThirdReplyId = ''
      },

      /**
       * 提交评论
       */
      commitComment () {
          this.comments.unshift({
            id: 'comment0002',
            date: this.getTime(),
            ownerId: 'talents100020',
            fromId: 'errhefe232213',
            fromName: '测试用户',
            fromAvatar: 'http://ww1.sinaimg.cn/bmiddle/006DLFVFgy1ft0j2q2p8pj30v90uzmzz.jpg',
            likeNum: 0,
            content: this.inputComment,
            reply: []
          })
        console.log(this.inputComment)
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
        if (reply) {
          this.showThirdReplyId = item.id
          this.inputComment = '@' + reply.fromName + ' '
        } else if (item) {
          this.showSecondReplyId = item.id
          this.inputComment = '@' + reply.fromName + ' '
        } else {
          this.showArticleReplyId = true
        }
      }
    },
    created () {
      console.log(this.comments)
    }
  }
</script>

<style scoped lang="scss">

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
					&.active, &:hover {
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
				margin: 10px 0;
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
				.fade-enter-active, fade-leave-active {
					transition: opacity 0.5s;
				}
				.fade-enter, .fade-leave-to {
					opacity: 0;
				}
				.input-wrapper {
					padding: 10px;
					.gray-bg-input, .el-input__inner {
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
