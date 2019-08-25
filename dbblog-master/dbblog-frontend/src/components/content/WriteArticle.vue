<template>
  <div class="writeArticle-content">
    <div class="write-main">
      <h1 class="write-title">写博文</h1><br><br>
      <el-form :model="article" label-width="80px" :rules="rules" ref="articleForm">
<!--        <el-form-item label="文章标题" prop="title">-->
<!--        </el-form-item>-->
        <el-form-item prop="title">
          <el-col class="little-title">文章标题</el-col>
          <el-col :span="12">
            <el-input placeholder="文章标题" v-model="article.title" clearable="clearable"></el-input>
          </el-col>
        </el-form-item>
        <el-row>
          <el-col :span="6">
            <el-form-item>
              <el-col class="little-title">文章分类</el-col>
              <el-select
                      style="width: 100%"
                      v-model="categoryOptionsSelect"
                      multiple
                      allow-create
                      filterable
                      default-first-option
                      placeholder="请选择文章分类" @change="filterTagList" value="">
                <el-option
                        v-for="item in categoryOptions"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-col class="little-title">文章标签</el-col>
              <el-select
                      style="width: 100%"
                      v-model="tagListSelect"
                      multiple
                      allow-create
                      filterable
                      default-first-option
                      placeholder="请选择文章标签" @change="filterTagList" value="">
                <el-option
                        v-for="item in tagList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-col class="little-title">上传封面</el-col>
          <el-col :span="12">
            <el-upload
                    drag
                    :action="url"
                    list-type="picture"
                    :multiple="false"
                    :before-upload="beforeUploadHandle"
                    :file-list="file"
                    :on-remove="handleRemove"
                    :on-success="successHandle">
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">只支持jpg、png、gif格式的图片！</div>
            </el-upload>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col class="little-title">文章描述</el-col>
          <el-col :span="12">
            <el-input type="textarea" v-model="article.description" placeholder="文章描述" clearable="clearable"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col class="little-title">文章内容</el-col>
          <mavon-editor ref=md v-model="article.content" @imgAdd="imgAdd" @change="mavonChangeHandle"></mavon-editor>
        </el-form-item>
        <el-row>
          <el-col :span="6">
            <el-form-item>
              <el-col class="little-title">文章类型</el-col>
              <el-select v-model="categoryTypeSelect" placeholder="请选择" value="">
                <el-option
                        v-for="item in categoryType"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-col class="little-title">发布形式</el-col>
          <el-radio-group v-model="article.public" class="public-or-private">
            <el-radio :label="true">公开</el-radio>
            <el-radio :label="false">私密</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item class="issue-button">
          <el-button type="primary" @click="saveArticle()" class="write-issue">
            发&nbsp;&nbsp;&nbsp;布
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import MavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import marked from 'marked'
import {treeDataTranslate} from '../../utils'

export default {
  components: {
    'mavon-editor': MavonEditor.mavonEditor
  },
  data () {
    return {
      clearable: true,
      article: {
        recommend: false,
        tagList: [],
        type: 0,
        coverType: 2 // 默认无图片
      },
      url: '',
      file: [],
      rules: {
        title: {required: true, message: '请输入文章标题', trigger: 'change'}
      },
      tagList: [],
      tagListSelect: [],
      tagListNew: [],
      categoryType: [{
        value: 1,
        label: '原创'
      }, {
        value: 2,
        label: '转载'
      }, {
        value: 3,
        label: '翻译'
      }
      ],
      categoryTypeSelect: [],
      categoryOptions: [],
      categoryOptionsSelect: [],
      categoryListTreeProps: {
        label: 'name',
        children: 'children',
        value: 'id'
      }
    }
  },
  created () {
    this.init()
  },
  methods: {
    init () {
      // 获取文章分类
      this.$http({
        url: this.$http.adornUrl('/admin/operation/category/list'),
        method: 'get',
        params: this.$http.adornParams({type: 0})
      }).then(({data}) => {
        if (data && data.code === 200) {
          this.categoryOptions = data.categoryList
        }
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/admin/operation/tag/select'),
          method: 'get',
          params: this.$http.adornParams({type: 0})
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.tagList = data.tagList
          }
        })
      }).then(() => {
        this.url = this.$http.adornUrl(`/admin/oss/resource/upload?token=${this.$cookie.get('token')}`)
        let id = this.$route.params.id
        if (id) {
          this.$http({
            url: this.$http.adornUrl('/admin/article/info/' + id),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 200) {
              this.article = data.article
              this.file = [{url: data.article.cover}]
              // 转换tagList
              this.tagListSelect = this.article.tagList.map(tag => {
                return tag.id
              })
              // 转换categoryId
              this.categoryOptionsSelect = this.article.categoryId.map((data) => {
                return data.id
              })
            }
          })
        }
      })
    },
    // 过滤标签
    filterTagList (selectValueList) {
      let tagList = []
      selectValueList.forEach(value => {
        let isInput = true
        for (let i = 0; i < this.tagList.length; i++) {
          let tag = this.tagList[i]
          if (tag.id === value || value.id) {
            isInput = false
            tagList.push({id: tag.id, name: tag.name, type: 0})
          }
        }
        if (isInput) {
          tagList.push({name: value, type: 0})
        }
      })
      this.article.tagList = tagList
    },
    // 上传之前
    beforeUploadHandle (file) {
      if (file.type !== 'image/jpg' && file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
        this.$message.error('只支持jpg、png、gif格式的图片！')
        return false
      }
    },
    // 上传成功
    successHandle (response) {
      if (response && response.code === 200) {
        this.article.cover = response.resource.url
        this.file = [response.resource]
        this.$message.success('上传成功！')
      }
    },
    // 移除上传文件
    handleRemove (file, fileList) {
      this.file = []
      this.article.cover = ''
    },
    // 保存文章
    saveArticle () {
      this.$refs['articleForm'].validate((valid) => {
        if (valid) {
          // 转化categoryId
          this.article.categoryId = this.categoryOptionsSelect
          this.$http({
            url: this.$http.adornUrl(`/admin/article/${!this.article.id ? 'save' : 'update'}`),
            method: !this.article.id ? 'post' : 'put',
            data: this.$http.adornData(this.article)
          }).then(({data}) => {
            if (data && data.code === 200) {
              this.$message.success('保存文章成功')
              // 关闭当前标签
              this.$emit('closeCurrentTabs')
              // 跳转到list
              this.$router.push('/article-article')
            } else {
              this.$message.error(data.msg)
            }
          })
        } else {
          return false
        }
      })
    },
    // 文章内容图片上传
    imgAdd (pos, $file) {
      // 第一步.将图片上传到服务器.
      let formData = new FormData()
      formData.append('file', $file)
      this.$http({
        url: this.url,
        method: 'post',
        data: formData,
        headers: {'Content-Type': 'multipart/form-data'}
      }).then(({data}) => {
        this.$refs.md.$img2Url(pos, data.resource.url)
      })
    },
    mavonChangeHandle (context, render) {
      this.article.contentFormat = marked(context)
    }
  }
}
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .write-main {
    position: relative;
    margin: 0 auto;
    /*border: black solid 1px;*/
    /*position: absolute;*/
    text-align: center;
    top: 0;
    right: 0;
    padding: 50px 50px 0px 0px;
    width: 1100px;
    min-height: 100%;
    background-color: #fff;
  }
  .little-title{
    float : left;
    text-align :left;
    /*border: black solid 1px;*/
  }

  .write-title {
    font-size: 30px;
  }

  .public-or-private{
    float : left;
    padding-top :8px;
    /*border :black solid 1px;*/
  }
  .issue-button{
    /*border : black solid 1px;*/
  }
  .write-issue{
    width :120px;

  }
  .writeArticle-content
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
