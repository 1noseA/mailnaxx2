/**
 * 現場社員取得
 */
$(function() {
	$('#projectId').change(function(){
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
			// 現場社員プルダウン作成
			$('#colleagueName').empty();
		    $('#colleagueName').append($('<option>').val(0).text('選択してください'));
		    for (let i = 0; i < data.length; i++) {
		        $('#colleagueName').append($('<option>').val(data[i].userId).text(data[i].userName));
		    }
	    })
	    .fail(function() {
	    	alert('送信に失敗しました');
	    })
	});
});

/**
 * 現場社員登録
 */
$(function() {
	$('#saveColleague').click(function(){
		let colleagueForm = {
		    colleagueName : $('#colleagueName').val(),
		    colleagueDifficulty : $('#colleagueDifficulty').val(),
		    colleagueSchedule : $('#colleagueSchedule').val(),
			colleagueImpression : $('#colleagueImpression').val()
		};
	    $.ajax({
	        url: '/weekly-report/saveColleague',
	        type: 'POST',
	        data: JSON.stringify(colleagueForm),
			contentType : 'application/json'
	    })
	    .done(function(data) {
			alert('登録完了しました');
	    })
	    .fail(function(jqXHR, textStatus, errorThrown) {
	    	alert('送信に失敗しました');
			console.log("jqXHR          : " + jqXHR.status); // HTTPステータスが取得
            console.log("textStatus     : " + textStatus);    // タイムアウト、パースエラー
            console.log("errorThrown    : " + errorThrown.message); // 例外情報
	    })
	});
});

