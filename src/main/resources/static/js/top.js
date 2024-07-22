$.widget.bridge('uitooltip', $.ui.tooltip);
$(function () {
    $('[data-bs-toggle="tooltip"]').tooltip();
});

/**
 * 詳細画面遷移
 */
function showDetail(id) {
    let form = $('#detailForm');
    $('<input>').attr({
        type: 'hidden',
        name: 'noticeId',
        value: id
    }).appendTo(form);
    form.submit();
}
