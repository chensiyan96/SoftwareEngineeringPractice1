package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.TagLinkMapper;
import com.example.demo.dao.TagMapper;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 标签 服务实现类
 */
@Service
public class TagService extends ServiceImpl<TagMapper, Tag> {

    @Autowired
    private TagLinkMapper tagLinkMapper;

    /**
     * 分页查询
     */
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Tag> page = baseMapper.selectPage(new Query<Tag>(params).getPage(),
                new QueryWrapper<Tag>().lambda());
        return new PageUtils(page);
    }

    /**
     * 根据关联Id获取列表
     */
    public List<Tag> listByLinkId(Integer linkId) {
        return this.baseMapper.listByLinkId(linkId);
    }

    /**
     * 添加所属标签，包含新增标签
     */
    public void saveTagAndNew(List<Tag> tagList, Integer linkId) {
        Optional.ofNullable(tagList)
                .ifPresent(tagListValue -> tagListValue.forEach(tag -> {
                    if (tag.getId() == null) {
                        this.baseMapper.insert(tag);
                    }
                    TagLink tagLink = new TagLink(linkId, tag.getId());
                    tagLinkMapper.insert(tagLink);
                }));
    }

    /**
     * 删除tagLink关联
     */
    public void deleteTagLink(Integer linkId) {
        tagLinkMapper.delete(new QueryWrapper<TagLink>().lambda()
                .eq(linkId != null, TagLink::getLinkId, linkId));
    }

    public List<TagVO> listTagVo() {
        return baseMapper.listTagVo();
    }

    public List<TagVO> listByUserId(int userId) {
        return baseMapper.listByUserId(userId);
    }

}
