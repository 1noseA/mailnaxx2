$(function() {
    // 現在
    const nowYear = new Date().getFullYear();

    // 入社年月_年プルダウン作成
    let targetHireYear = $('#hireYearList').val();
    createYearList('#hireYearList', nowYear, nowYear+1, nowYear);
    if (targetHireYear != '') {
        $('#hireYearList').val(targetHireYear);
    }

    // 生年月日_年プルダウン作成
    let targetBirthYear = $('#yearList').val();
    createYearList('#yearList', nowYear-70, nowYear-20, nowYear-30);
    if (targetBirthYear != '') {
        $('#yearList').val(targetBirthYear);
    }

    // 月プルダウン作成
    let targetHireMonth = $('#hireMonthList').val();
    let targetBirthMonth = $('#monthList').val();
    createMonthList();
    if (targetHireMonth != '') {
        $('#hireMonthList').val(targetHireMonth);
    }
    if (targetBirthMonth != '') {
        $('#monthList').val(targetBirthMonth);
    }

    // 日プルダウン作成
    let targetDay = $('#dayList').val();
    createDayList();
    if (targetDay != '') {
        $('#dayList').val(targetDay);
    }
    // 年プルダウンか月プルダウンが変更されたら
    $('#yearList, #monthList').on('change', function() {
        createDayList();
    });

    // パスワード表示非表示
    $(".togglePass").on("click", function() {
        $(this).toggleClass("fa-eye fa-eye-slash");
        let input = $(this).prev("input");
        if (input.attr("type") == "text") {
            input.attr("type", "password");
        } else {
            input.attr("type", "text");
        }
    });
});

