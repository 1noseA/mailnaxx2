// 表示範囲が「個人」だったら入力欄を増やす
$(document).ready(function() {

    // ラジオボタンの選択でプルダウンの表示・非表示を切り替え
    $("input[name='displayRange']").change(function() {
        if ($(this).val() === "2") {
            $("#userNameForm").show();
        } else {
            $("#userNameForm").hide();
        }
    });

    // 「+」ボタンで選択肢を追加
    $('.add').click(function() {
        let row = $(this).closest("tr");
        let newRow = row.clone(true);
        newRow.insertAfter(row);
    });

    // 「-」ボタンで選択肢を削除
    $('.remove').click(function() {
        let row = $(this).closest("tr").remove();
        row.remove();
    });
});

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