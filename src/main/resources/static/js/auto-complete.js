/**
 *
 */
 $(function(){
    /** プロジェクト名が変更になっても修正が必要なくなるよう実装**/
    var pathname = location.pathname.split("/")[1];

    $.ajax({
        /** オートコンプリート用のリストを取得するURL*/
        url: "/" + pathname + "/auto-complete",
        dataType: "json",
        type: 'GET'
    }).then(function(searchResult){ /** 検索結果のリストは引数に自動で入る*/
        $('#userName').autocomplete({ /** 実装したい入力欄のidを指定*/
            source : searchResult, /** 候補リスト(必須)*/
            autoFocus: true, /**リスト表示に際してリスト先頭にフォーカスするか*/
            delay: 500, /**キー入力からリスト表示までの遅延時間（ミリ秒）*/
            minLength: 1 /**オートコンプリート機能が働く最小文字数*/
        });
        },function(){
    });
});
