/**
 * 로그인 관련 js
 */
$(window).ready(function(){
    //프로필 클릭 시 프로필 영역 팝업 표출
    $("#profileArea").on('click', function(){
        let profilePopAreaDisplay = $("#profilePopArea").css("display");

        if(profilePopAreaDisplay == 'none'){
            $("#profilePopArea").show();
            $("#profilePopAngcle").show();
        }else{
            $("#profilePopArea").hide();
            $("#profilePopAngcle").hide();
        }
    })

    //로그아웃 진행
    $("#logout").on('click', function(){
        window.location.href= "/logout"
    })
})
