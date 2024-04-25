package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Affiliations;

@Mapper
public interface AffiliationsMapper {

    // 全件取得
    public List<Affiliations> findAll();

    // IDを基に所属名取得
    public String findNameById(int affiliationId);
}
