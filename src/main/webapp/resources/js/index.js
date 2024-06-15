

const defaultOptions = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(37.450701, 127.170667), //지도의 중심좌표.
    level: 11 //지도의 레벨(확대, 축소 정도)
};

let map;
let searchKeywordListArr = [];
let markers = [];
let targetOverlay = null;
let imageSrc = "/resources/images/rocation/cafeMarker.png"

$(window).ready(function(){

    initMap();
    getKeywordList();

    //로고 클릭 시 main 화면 이동
    $("#logo").on('click', function(){
        window.location.href = '/main';
    })

    //GPS 현재 위치 이동
    $("#myGpsBtn").on('click', function (){
        moveMyGpsLoaction();
    })

    // 검색바 키워드 검색 카페 조회
    $(".searchInputArea .btn_search").on("click", function(){
        getSearchCafe();
    })

    // 검색바 키워드 검색 카페 Enter Key 이벤트
    $(".searchInputArea #searchKeyword").on("keyup", function(e){
        if(e.keyCode == 13){
            getSearchCafe();
        }
    })
    
    // 키워드 클릭 시 카페 조회
    $("#keywordDiv LI").on("click", function (){
        let targetLi = $(this);
        let classFlag = targetLi.hasClass("on");
        let data = targetLi.data();

        if(classFlag){
            targetLi.removeClass("on");
            searchKeywordListArr.forEach((d, idx) => {
                if(d === data.keywordId){
                    searchKeywordListArr.splice(idx, 1);
                };
            })
            getSearchCafe();
        }else{
            targetLi.addClass("on");
            searchKeywordListArr.push(data.keywordId);
            getSearchCafe();
        }
    })

    // 카페 상세 영역 닫기
    $(".detailCafeInfo .titleArea .close").on('click', function(){
        $(".detailCafeInfo").css("display", "none");
    })
})

//map Setting
function initMap(){
    //map 영역
    var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
    //map 설정
    map = new kakao.maps.Map(container, defaultOptions); //지도 생성 및 객체 리턴
}

//키워드 리스트 조회
function getKeywordList(){

    $.get("/main/getKeywordList", null, function(result){
        if(result && result.length > 0){
            let targetLiList = $(".searchInputArea #keywordDiv LI")

            for (let i = 0; i < result.length; i++) {
                let keywordData = result[i];
                $(targetLiList[i]).data(keywordData);
                $("P", targetLiList[i]).text(keywordData.keywordName);
            }
        }
    })
}

// 카페 검색
function getSearchCafe(){
    let params = {
        searchKeyword : $("#searchKeyword").val(),
        searchKeywordList : searchKeywordListArr.join()
    }
    setMarkers(null);

    if(targetOverlay){
        closeOverlay();
    }

    $.post("/main/getCafeList", params, function(result){
        if(result.length > 0){
            addMarkers(result);
        }
    })
}

//마커 clear
// 배열에 추가된 마커들을 지도에 표시하거나 삭제하는 함수입니다
function setMarkers(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

//마커 추가
function addMarkers(markerList){

    // 마커 이미지의 이미지 크기 입니다
    var imageSize = new kakao.maps.Size(40, 40);

    // 마커 이미지를 생성합니다
    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

    for (let i = 0; i < markerList.length; i++) {
        // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
        var marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(Number(markerList[i].y), Number(markerList[i].x)),
            title : markerList[i].place_name,
            image : markerImage // 마커 이미지
        });
        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map);
        markers.push(marker);

        //tooltip 생성
        kakao.maps.event.addListener(marker, 'click', function() {

            let param = {
                place_name :  markerList[i].place_name
            }

            if(targetOverlay){
                closeOverlay();
            }

            $.post("/main/getCafeThumnail", param, function(result){
                if(result){
                    let thumbnail =  result.thumbnail_url;
                    markerList[i].thumbnail = thumbnail;

                    targetOverlay = createOverlay(markerList[i]);
                    let content = targetOverlay.getContent();
                    content = content.split("replaceThumnail").join(thumbnail);

                    targetOverlay.setContent(content);
                    targetOverlay.setMap(map);

                    let overlayPosition = new kakao.maps.LatLng(Number(markerList[i].y) + 0.001 , markerList[i].x);

                    // 줌레벨 설정 / 해당 좌표로 이동
                    map.setLevel(3, {anchor: overlayPosition});
                    map.setCenter(overlayPosition);


                    setDetailContent(markerList[i]);

                }else {
                }
            })
        });
    }
}

//상세영역 셋팅
function setDetailContent(targetMarker){
    $(".placeName").text(targetMarker.place_name);
    $(".titleArea .thumnail").attr('src', targetMarker.thumbnail);
    $(".detailCafeInfo").css("display", "block");
}

//overlay 생성
function createOverlay(targetMarker){
    // 커스텀 오버레이가 표시될 위치입니다
    let overlayPosition = new kakao.maps.LatLng(targetMarker.y, targetMarker.x);

    // 커스텀 오버레이를 생성합니다
    var mapCustomOverlay = new kakao.maps.CustomOverlay({
        position: overlayPosition,
        content: getContent(targetMarker),
        xAnchor: 0.5, // 커스텀 오버레이의 x축 위치입니다. 1에 가까울수록 왼쪽에 위치합니다. 기본값은 0.5 입니다
        yAnchor: 2 // 커스텀 오버레이의 y축 위치입니다. 1에 가까울수록 위쪽에 위치합니다. 기본값은 0.5 입니다
    });

    return mapCustomOverlay;
}

// 커스텀 오버레이를 닫기 위해 호출되는 함수입니다
function closeOverlay() {
    targetOverlay.setMap(null);
}

//get overlay 영역 html
function getContent(targetMarker){

    let param = {
        place_name : targetMarker.place_name
    }

    var content = '<div class="wrap">';
    content += '     <div class="overlay_info">';
    content += '         <div class="title">';
    content += '            <div>' +  targetMarker.place_name   + '</div>';
    content += '            <div class="close" onclick="closeOverlay()" title="닫기"></div>';
    content += '         </div>';
    content += '         <div style="display:flex;">';
    content += '            <img class="thumbnail" src="replaceThumnail">';
    content += '            <div class="desc">';
    content += '              <span class="address">' + targetMarker.road_address_name + ' </span>';
    content += '            </div>';
    content += '         </div>';
    content += '     </div>';
    content += '</div>';

    return content;
}

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