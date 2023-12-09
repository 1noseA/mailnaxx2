package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Notices;

@Mapper
public interface NoticesMapper {

    public List<Notices> findAll();

}
