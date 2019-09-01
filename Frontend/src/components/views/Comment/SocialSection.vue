<template>
  <comment :articleId="articleId"></comment>
</template>

<script>
    import comment from './Comment'
    import BrowseMore from '@/components/views/BrowseMore'

    export default {
        props: {
            articleId: {
                type: String
            }
        },
        data() {
            return {
                show: null,
                commentData: null
            }
        },
        components: {
            comment,
            'browse-more': BrowseMore
        },
        watch: {
            articleId(value) {
                this.show = value
            }
        }
    }
</script>
