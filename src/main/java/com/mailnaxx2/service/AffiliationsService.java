package com.mailnaxx2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mailnaxx2.entity.Affiliations;
import com.mailnaxx2.mapper.AffiliationsMapper;

@Service
public class AffiliationsService {

    @Autowired
    AffiliationsMapper affiliationsMapper;

    // 全件取得
    public List<Affiliations> findAll() {
        List<Affiliations> affiliationList = affiliationsMapper.findAll();
        return affiliationList;
    }
}
