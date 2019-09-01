package com.example.demo.service;

import com.example.demo.dao.ArticleMapper;
import com.example.demo.model.TimelineYear;
import com.example.demo.model.TimelineMonth;
import com.example.demo.model.TimelinePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimelineService {

    @Autowired
    ArticleMapper articleMapper;

    public List<TimelineYear> listTimeLine(int userId) {
        List<TimelinePost> timeLinePostList = articleMapper.getTimelinePost(userId);
        Map<Integer, Map<Integer, List<TimelinePost>>> timelineYearMap = new HashMap<>();
        for (TimelinePost timelinePost : timeLinePostList) {
            Date date = new Date(timelinePost.getUpdateTime());
            int year = date.getYear();
            Map<Integer, List<TimelinePost>> timelineMonthMap = timelineYearMap.get(year);
            if (timelineMonthMap == null) {
                timelineMonthMap = new HashMap<>();
                timelineYearMap.put(year, timelineMonthMap);
            }
            int month = date.getMonth();
            List<TimelinePost> timelinePostList = timelineMonthMap.get(month);
            if (timelinePostList == null) {
                timelinePostList = new ArrayList<>();
                timelineMonthMap.put(month, timelinePostList);
            }
            timelinePostList.add(timelinePost);
        }
        List<TimelineYear> timelineYearList = new ArrayList<>();
        for (Integer year : timelineYearMap.keySet()) {
            TimelineYear timelineYear = new TimelineYear();
            timelineYear.setYear(year + 1900);
            Map<Integer, List<TimelinePost>> timelineMonthMap = timelineYearMap.get(year);
            List<TimelineMonth> timelineMonthList = new ArrayList<>();
            for (Integer month : timelineMonthMap.keySet()) {
                TimelineMonth timelineMonth = new TimelineMonth();
                timelineMonth.setMonth(month + 1);
                List<TimelinePost> timelinePostList = timelineMonthMap.get(month);
                timelineMonth.setPosts(timelinePostList);
                timelineMonth.setCount(timelinePostList.size());
                timelineMonthList.add(timelineMonth);
            }
            timelineYear.setMonths(timelineMonthList);
            timelineYear.setCount(timelineMonthList.size());
            timelineYearList.add(timelineYear);
        }
        return timelineYearList;
    }

}
