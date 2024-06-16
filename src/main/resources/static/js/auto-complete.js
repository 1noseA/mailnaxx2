/**
 * オートコンプリート
 */
$(function(){
    $('#userName').on('keypress', function() {
        $.ajax({
            url: "/auto-complete",
            dataType: "json",
            type: 'GET'
        }).then(function(searchResult){
            $('.userName').autocomplete({
                source : searchResult,
                autoFocus: true,
                delay: 500,
                minLength: 1
            });
        }, function(error){
            console.error("エラーが発生しました: ", error);
        });
    });
});
