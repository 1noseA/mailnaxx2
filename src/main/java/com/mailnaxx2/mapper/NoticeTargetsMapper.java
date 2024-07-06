package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Users;

@Mapper
public interface NoticeTargetsMapper {

    // 登録
    public int insert(int noticeId, List<Users> userList);
}
