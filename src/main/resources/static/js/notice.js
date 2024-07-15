// 表示範囲が「個人」だったら入力欄を増やす
$(document).ready(function() {

    // ラジオボタンの選択でプルダウンの表示・非表示を切り替え
    $('input[name="displayRange"]').change(function() {
        if ($(this).val() === '2') {
            $('#userNameForm').show();
            $('select[name="userId"]').prop('disabled', false);
        } else {
            $('#userNameForm').hide();
            $('select[name="userId"]').prop('disabled', true);
        }
    });

    // 「+」ボタンで選択肢を追加
    $('.add').click(function() {
        let row = $(this).closest('tr');
        let newRow = row.clone(true);
        newRow.insertAfter(row);
    });

    // 「-」ボタンで選択肢を削除
    $('.remove').click(function() {
        let row = $(this).closest('tr');
        if ($('tr').length > 1) {
            row.remove();
        }
    });
});

/**
 * カテゴリー登録処理
 */
$(function() {
    $('#createCategory').click(function(){
        const categoriesForm = {
            categoryName: $('#categoryName').val(),
            color: $('#color').val(),
            _csrf: $('*[name=_csrf]').val()
        };

        $.ajax({
            url: '/notice/createCategory',
            type: 'POST',
            data : categoriesForm,
            dataType: 'json',
        })
        .done(function(data) {
            if (data.errorMessage) {
                alert('カテゴリー名を' + data.errorMessage);
            } else {
                // 入力をクリアする
                $('#categoryName').val('');
                $('#color').val('');
                // プルダウンに登録した値を追加する
                $('#categoryId').append($('<option>').val(data.categoryId).text(data.categoryName));
                alert('登録完了しました');
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            alert('送信に失敗しました');
            console.log('jqXHR          : ' + jqXHR.status); // HTTPステータス
            console.log('textStatus     : ' + textStatus);    // タイムアウト、パースエラー
            console.log('errorThrown    : ' + errorThrown.message); // 例外情報
        })
    });
});

/**
 * お知らせ登録処理
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