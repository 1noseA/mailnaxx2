package com.mailnaxx2.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Colleagues;

@Mapper
public interface ColleaguesMapper {

    // 登録
    public int insert(Colleagues colleague);

    // 週報ID追加
    public void addWeeklyReportId(int colleagueId, int weeklyReportId);
}
