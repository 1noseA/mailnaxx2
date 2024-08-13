/**
 * 現場社員取得
 */
$(function() {
    getColleague();
    $('#projectId').change(function(){
        getColleague();
    });
});

/**
 * 現場社員登録
 */
$(function() {
    $('#saveColleague').click(function(){
        $.ajax({
            url: '/weekly-report/saveColleague',
            type: 'POST',
            data : {
                colleagueId: $('#colleagueId').val(),
                colleagueUserId: $('#colleagueUserId').val(),
                colleagueDifficulty: $('#colleagueDifficulty').val(),
                colleagueSchedule: $('#colleagueSchedule').val(),
                colleagueImpression: $('#colleagueImpression').val(),
                _csrf: $('*[name=_csrf]').val()
            },
            dataType: 'json',
        })
        .done(function(data) {
            $('#colleagueId').val(data.colleagueId);
            alert('登録完了しました');
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            alert('送信に失敗しました');
            console.log('jqXHR          : ' + jqXHR.status); // HTTPステータス
            console.log('textStatus     : ' + textStatus);    // タイムアウト、パースエラー
            console.log('errorThrown    : ' + errorThrown.message); // 例外情報
        })
    });
});


function getColleague() {
    $.ajax({
        url: '/weekly-report/getColleague',
        type: 'POST',
        data: {
            userNumber: $('#loginUserNumber').text(),
            projectId: $('#projectId').val(),
            _csrf: $('*[name=_csrf]').val()
        },
        dataType: 'json'
    })
    .done(function(data) {
        // 現在選択されている値を取得
        let selectedValue = $('#colleagueUserId').val();

        // 現場社員プルダウン作成
        $('#colleagueUserId').empty();

        // 選択されている値がない場合のみ「選択してください」を追加
        if (!selectedValue) {
            $('#colleagueUserId').append($('<option>').val(0).text('選択してください'));
        }

        for (let i = 0; i < data.length; i++) {
            let option = $('<option>').val(data[i].userId).text(data[i].userName);
            if (data[i].userId == selectedValue) {
                option.attr('selected', 'selected');
            }
            $('#colleagueUserId').append(option);
        }
    })
    .fail(function() {
        alert('送信に失敗しました');
    })
}
