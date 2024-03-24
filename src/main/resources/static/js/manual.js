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
