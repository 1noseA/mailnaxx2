package com.mailnaxx2.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mailnaxx2.constants.NoticeConstants;
import com.mailnaxx2.entity.NoticeTargets;
import com.mailnaxx2.entity.Notices;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.form.NoticesForm;
import com.mailnaxx2.mapper.NoticeTargetsMapper;
import com.mailnaxx2.mapper.NoticesMapper;
import com.mailnaxx2.security.LoginUserDetails;

@Service
public class NoticesService {

    @Autowired
    NoticesMapper noticesMapper;

    @Autowired
    NoticeTargetsMapper noticeTargetsMapper;

    // 全件取得
    public List<Notices> findAll() {
        List<Notices> noticeList  = noticesMapper.findAll();
        return noticeList;
    }

    // 登録処理
    @Transactional
    public void insert(NoticesForm noticesForm,
                       @AuthenticationPrincipal LoginUserDetails loginUser) {
        // エンティティにセットする
        Notices notice = setEntity(new Notices(), noticesForm);

        // 作成者はセッションの社員番号
        notice.setCreatedBy(loginUser.getLoginUser().getUserNumber());

        // お知らせ登録
        noticesMapper.insert(notice);

        // 表示範囲が個人だったらお知らせ表示対象テーブルに登録する
        if (noticesForm.getDisplayRange().equals(NoticeConstants.DISPLAY_RANGE_INDIVIDUAL)) {
            List<NoticeTargets> noticeTargetList = new ArrayList<>();
            for (int i = 0; i <  noticesForm.getUserId().size(); i++) {
                NoticeTargets noticeTarget = new NoticeTargets();
                Users user = new Users();
                user.setUserId(noticesForm.getUserId().get(i));
                noticeTarget.setUser(user);
                noticeTarget.setNoticeId(notice.getNoticeId());
                noticeTarget.setCreatedBy(loginUser.getLoginUser().getUserNumber());
                noticeTargetList.add(noticeTarget);
            }
            // お知らせ表示対象登録
            noticeTargetsMapper.insert(noticeTargetList);
        }
    }

    // エンティティにセットする
    private Notices setEntity(Notices notice, NoticesForm noticesForm) {
        // 掲載開始日
        if (noticesForm.getStartYear() != "" &&
            noticesForm.getStartMonth() != "" &&
            noticesForm.getStartDay() != "") {
            String startYear = "%2s".formatted(noticesForm.getStartYear()).replace(" ", "0");
            String startMonth = "%2s".formatted(noticesForm.getStartMonth()).replace(" ", "0");
            String startDay = "%2s".formatted(noticesForm.getStartDay()).replace(" ", "0");
            LocalDate startDate = LocalDate.parse(startYear + startMonth + startDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
            notice.setStartDate(startDate);
        } else {
            notice.setStartDate(LocalDate.now());
        }

        // 掲載終了日
        if (noticesForm.getEndYear() != "" &&
            noticesForm.getEndMonth() != "" &&
            noticesForm.getEndDay() != "") {
            String endYear = "%2s".formatted(noticesForm.getEndYear()).replace(" ", "0");
            String endMonth = "%2s".formatted(noticesForm.getEndMonth()).replace(" ", "0");
            String endDay = "%2s".formatted(noticesForm.getEndDay()).replace(" ", "0");
            LocalDate endDate = LocalDate.parse(endYear + endMonth + endDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
            notice.setEndDate(endDate);
        }

        // 表示範囲
        if (StringUtils.isEmpty(noticesForm.getDisplayRange())) {
            notice.setDisplayRange(NoticeConstants.DISPLAY_RANGE_ALL);
        } else {
            notice.setDisplayRange(noticesForm.getDisplayRange());
        }

        // 内容
        notice.setContent(noticesForm.getContent());

        // リンク
        notice.setLink(noticesForm.getLink());

        return notice;
    }
}
