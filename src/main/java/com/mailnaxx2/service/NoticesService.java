package com.mailnaxx2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mailnaxx2.entity.Notices;
import com.mailnaxx2.mapper.NoticesMapper;

@Service
public class NoticesService {

    @Autowired
    NoticesMapper noticesMapper;

    // 全件取得
    public List<Notices> findAll() {
        List<Notices> noticeList  = noticesMapper.findAll();
        return noticeList;
    }
}
