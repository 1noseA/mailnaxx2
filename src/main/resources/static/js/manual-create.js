$(function() {
    // 掲載日_年プルダウン設定
    setYearList('#startYearList', $('#startYearList').val());
    setYearList('#endYearList', $('#endYearList').val());

    // 掲載日_月プルダウン設定
    setMonthList('#startMonthList', $('#startMonthList').val());
    setMonthList('#endMonthList', $('#endMonthList').val());

    // 掲載日_日プルダウン設定
    setDayList('#startDayList', $('#startDayList').val());
    setDayList('#endDayList', $('#endDayList').val());

    // 年プルダウンか月プルダウンが変更されたら
    $('#startYearList, #startMonthList').on('change', function() {
        createDayList2('#startYearList', '#startMonthList', '#startDayList');
    });
    $('#endYearList, #endMonthList').on('change', function() {
        createDayList2('#endYearList', '#endMonthList', '#endDayList');
    });
});

// 掲載日_年プルダウン設定
function setYearList(id, val) {
    // 現在
    const nowYear = new Date().getFullYear();
    createYearList(id, nowYear, nowYear+1, nowYear);
    if (val != '') {
        $(id).val(val);
    }
}

// 掲載日_月プルダウン設定
function setMonthList(id, val) {
    createMonthList();
    if (val != '') {
        $(id).val(val);
    }
}

// 掲載日_日プルダウン設定
function setDayList(id, val) {
    if (id.indexOf('start') !== -1) {
        createDayList2('#startYearList', '#startMonthList', '#startDayList');
    } else if (id.indexOf('end') !== -1) {
        createDayList2('#endYearList', '#endMonthList', '#endDayList');
    }

    if (val != '') {
        $(id).val(val);
    }
}

/**
 * 掲載日_日プルダウン作成
 */
function createDayList2(yearId, monthId, dayId) {
    const year = $(yearId).val();
    const month = $(monthId).val();
    if (month === '') {
        return;
    }

    const daysInMonth = new Date(year, month, 0).getDate();
    $(dayId).empty();
    $(dayId).append($('<option>').val('').text(''));
    for (let i = 1; i <= daysInMonth; i++) {
        $(dayId).append($('<option>').val(i).text(i));
    }
}
