package com.mailnaxx2.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Colleagues;

@Mapper
public interface ColleaguesMapper {

    // 仮登録
    public void tempInsert(Colleagues Colleague);
}
