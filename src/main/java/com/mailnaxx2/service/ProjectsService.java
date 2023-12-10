package com.mailnaxx2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mailnaxx2.entity.Projects;
import com.mailnaxx2.mapper.ProjectsMapper;

@Service
public class ProjectsService {

    @Autowired
    ProjectsMapper projectsMapper;

    // 全件取得
    public List<Projects> findAll() {
        List<Projects> projectList = projectsMapper.findAll();
        return projectList;
    }
}
