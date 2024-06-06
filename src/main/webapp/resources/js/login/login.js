/**
 * 로그인 관련 js
 */
$(window).ready(function(){
    //로그인팝업
    getloginPopup();

    //로그인 후 프로필 영역
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


function getloginPopup(){
    //login 버튼 클릭 시 로그인 화면 이동
    $(".loginBtn").on("click", function(){
        //window.location.href='/auth/login';
        $("#loginPopup").show();

        //로그인 닫기 ESC키 활성화
        $(document).on("keydown.lp_keydown", function(event) {
            // Esc키 : 레이어 닫기
            var keyType = event.keyCode || event.which;

            if (keyType === 27 && !$("#loginPopup").is('display')) {
                $("#loginPopup").hide();
                $(document).off("keydown.lp_keydown");
            }
        });
    })

    //loginPopup 닫기
    $("#closeLoginPopupImg").on("click", function(){
        $("#loginPopup").hide();
        $(document).off("keydown.lp_keydown");
    })
}