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
