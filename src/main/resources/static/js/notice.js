/**
 * 登録処理
 */
function submitForm() {
    if ($('#startYearList').val() === '' || $('#startMonthList').val() === '' || $('#startDayList').val() === '') {
        if (!confirm('掲載開始日は本日日付になりますが、よろしいでしょうか')) {
            return false;
        }
    }
    let form = $('#form');
    form.submit();
}