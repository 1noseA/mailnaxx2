package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Affiliations;

@Mapper
public interface AffiliationsMapper {

    public List<Affiliations> findAll();

}
