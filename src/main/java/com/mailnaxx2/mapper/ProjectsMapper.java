package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Projects;

@Mapper
public interface ProjectsMapper {

    // 全件取得
    public List<Projects> findAll();
}
