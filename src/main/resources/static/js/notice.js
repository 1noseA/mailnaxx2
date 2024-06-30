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
    $("#add").on("click", function() {
        var input = $("#userNameList").val();
        $("#addList").append(`<option value="${input}">${input}</option>`);
    });

    // 「-」ボタンで選択肢を削除
    $("#remove").on("click", function() {
        $("#test option:selected").remove();
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