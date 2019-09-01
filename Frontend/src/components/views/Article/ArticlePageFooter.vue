<template>
  <div class="article-page-footer">
    <social-section :articleId="articleId"></social-section>
  </div>
</template>

<script type="text/ecmascript-6">
import LicenseTag from '@/components/views/LicenseTag'
import SocialSection from '@/components/views/Comment/SocialSection'

export default {
  components: {
    'license-tag': LicenseTag,
    'social-section': SocialSection
  },
  props: {
      articleId: {
          type: String
      }
  }
}
</script>
