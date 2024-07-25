/**
 * 詳細画面遷移
 */
function showDetail(id) {
    let form = $('#detailForm');
    $('<input>').attr({
        type: 'hidden',
        name: 'manualId',
        value: id
    }).appendTo(form);
    form.submit();
}

/**
 * 編集画面遷移
 */
function edit(id) {
    let form = $('#editForm');
    $('<input>').attr({
        type: 'hidden',
        name: 'manualId',
        value: id
    }).appendTo(form);
    form.submit();
}

/**
 * 登録・更新処理
 */
function submitForm(id) {
    if ($('#startYearList').val() === '' || $('#startMonthList').val() === '' || $('#startDayList').val() === '') {
        if (!confirm('掲載開始日は本日日付になりますが、よろしいでしょうか')) {
            return false;
        }
    }
    let form = $('#form');
    $('<input>').attr({
        type: 'hidden',
        name: 'manualId',
        value: id
    }).appendTo(form);
    // 更新の場合
    if (id !== '0') {
        form.attr('action', '/manual/update');
    }
    form.submit();
}