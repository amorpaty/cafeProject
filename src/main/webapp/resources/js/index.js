

const defaultOptions = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(37.450701, 127.170667), //지도의 중심좌표.
    level: 11 //지도의 레벨(확대, 축소 정도)
};

let map;

$(window).ready(function(){
    //map 영역
    var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
    //map 설정
    map = new kakao.maps.Map(container, defaultOptions); //지도 생성 및 객체 리턴

   //로고 클릭 시 main 화면 이동
    $("#logo").on('click', function(){
        window.location.href = '/main';
    })

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

    //GPS 위치 버튼 클릭 시 내 위치 조회
    $("#myGpsBtn").on("click", function(){
        moveMyGpsLoaction();
    })
})

// 사용자의 현재 위치로 이동
function moveMyGpsLoaction(){
    navigator.geolocation.getCurrentPosition((position) => {
        if(position) {
            const positionObj = {
                latitude: position.coords.latitude,
                longitude: position.coords.longitude
            }
            // 줌레벨 설정 / 해당 좌표로 이동
            map.setLevel(4, {anchor: new kakao.maps.LatLng(positionObj.latitude, positionObj.longitude)});
            map.setCenter(new kakao.maps.LatLng(positionObj.latitude, positionObj.longitude));
        }
    });
}