/**
 * 로그인 관련 js
 */
$(window).ready(function(){
    //로고 클릭 시 main 화면 이동
    $("#logo").on('click', function(){
        window.location.href = '/';
    })

    //login 버튼 클릭 시 로그인 팝업 표출
    $(".loginBtn").on("click", function(){
        $("#loginPopup").css('display', 'block');
    })
})
