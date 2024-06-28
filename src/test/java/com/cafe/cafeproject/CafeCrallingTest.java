package com.cafe.cafeproject;

import com.cafe.cafeproject.common.dto.CafeKeywordInfoDto;
import com.cafe.cafeproject.common.dto.CafeMenuInfoDto;
import com.cafe.cafeproject.common.dto.CafeinfoDto;
import com.cafe.cafeproject.common.dto.KeywordDto;
import com.cafe.cafeproject.common.repository.CafeKeywordInfoRepository;
import com.cafe.cafeproject.common.repository.CafeMenuInfoRepository;
import com.cafe.cafeproject.common.repository.DaumOpenApiRepository;
import com.cafe.cafeproject.common.repository.KeywordRepository;
import io.micrometer.common.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 카페 메뉴 크롤링 - 네이버 지도 API
 * @apiNote 네이버 지도에서 카페 메뉴 얻기 API
 * @author 남은주
 * @since  2024.05.26
 */
@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CafeCrallingTest {

    private WebDriver driver;

    /**
     * 카페 정보 repo
    */
    @Autowired
    DaumOpenApiRepository daumOpenApiRepository;

    /**
     * 카페 메뉴 정보 repo
     */
    @Autowired
    CafeMenuInfoRepository cafeMenuInfoRepository;

    /**
     * 키워드 repo
     */
    @Autowired
    KeywordRepository keywordRepository;

    /**
     * 카페의 키워드 목록 repo
     */
    @Autowired
    CafeKeywordInfoRepository cafeKeywordInfoRepository;

    private static final String requestUrl = "https://map.naver.com/v5/search";
    private static final String kakaoRequestURl = "https://map.kakao.com/";

    /**
     * 네이버 크롤링
     */
    @Test
    public void naverCafeCralling() throws Exception {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("headless"); // 창띄우지 않고 작업

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // (1) 브라우저에서 url로 이동한다.
        driver.get(requestUrl);

        // 브라우저 로딩될 때까지 잠시 기다린다.
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));


        List<CafeinfoDto> cafeinfoDtoList = daumOpenApiRepository.findAll();

        for(CafeinfoDto cafeinfoDto : cafeinfoDtoList) {

            WebElement inputSearch = driver.findElement(By.className("input_search"));

            String inputKey = cafeinfoDto.getPlace_name(); //카페명
            inputSearch.clear();
            inputSearch.click();
            inputSearch.sendKeys(Keys.CONTROL + "A");
            inputSearch.sendKeys(inputKey);
            Thread.sleep(2000);
            inputSearch.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            List<WebElement> list = driver.findElements(By.tagName("iframe"));

            for (WebElement webele : list){
                String idName = webele.getAttribute("id");

                if(!idName.equals("entryIframe")){
                    continue;
                }

                // (2) 검색결과 iframe으로 frame을 바꾼다.
                driver.switchTo().frame("entryIframe");

                // 홈, 메뉴 Tab 리스트 조회
                List<WebElement> elements = driver.findElements(By.className("place_fixed_maintab"));

                WebElement element = elements.get(0);
                List<WebElement> tabs = element.findElements(By.tagName("a"));

                // 두번째 탭 선택 : 메뉴 탭 클릭

                for (WebElement tab : tabs) {

                    WebElement ele = tab.findElement(By.tagName("span"));
                    if (ele.getText().equals("메뉴")) {
                        tab.sendKeys(Keys.ENTER);

                        List<WebElement> menuList = driver.findElements(By.className("E2jtL"));

                        //메뉴 리스트 for
                        for (WebElement menuInfo : menuList) {

                            CafeMenuInfoDto cafeMenuInfoDto =  new CafeMenuInfoDto();
                            cafeMenuInfoDto.setId(cafeinfoDto.getId());

                            // 1개 메뉴당 이미지, 이름, 섦명, 가격 데이터 조회
                            // 이미지
                            if( menuInfo.findElements(By.className("K0PDV")).size() > 0 ){
                                String style = menuInfo.findElement(By.className("K0PDV")).getAttribute("style");
                                String[] styleArr = style.split("background-image:");

                                if(styleArr.length >= 2){
                                    String img = styleArr[1].toString();
                                    cafeMenuInfoDto.setImg(img); //이미지
                                }
                            }


                            // 메뉴 설명
                            if( menuInfo.findElements(By.className("kPogF")).size() > 0 ){
                                WebElement detail = menuInfo.findElement(By.className("kPogF"));

                                cafeMenuInfoDto.setDitail(detail.getText());
                            }

                            // 이름
                            String menuTit = menuInfo.findElement(By.className("lPzHi")).getText();

                            if(menuInfo.findElements(By.className("GXS1X")).size() > 0){
                                WebElement price = menuInfo.findElement(By.className("GXS1X"));

                                if(price.findElements(By.tagName("em")).size() > 0){
                                    cafeMenuInfoDto.setPrice(price.getText());
                                }
                            }

                                
                            cafeMenuInfoDto.setTit(menuTit); //메뉴명

                            cafeMenuInfoRepository.save(cafeMenuInfoDto);

                            Thread.sleep(1000);
                        }
                    } else {
                        continue;
                    }
                }
            }
            driver.switchTo().parentFrame();
        }
    }

    /**
     * 카페 키워드 크롤링
     * @throws Exception
     */
    @Test
    public void kakaoCafeCralling() throws Exception {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));


        List<CafeinfoDto> cafeinfoDtoList = daumOpenApiRepository.findCafeInfoList();

        for(CafeinfoDto cafeinfoDto : cafeinfoDtoList) {
            // (1) 브라우저에서 url로 이동한다.
            driver.get(kakaoRequestURl);

            // 브라우저 로딩될 때까지 잠시 기다린다.
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(3000));

            // 검색 영역 찾기
            if(driver.findElements(By.id("search")).size() > 0){

                WebElement element = driver.findElement(By.className("IE6MIN"));

                WebElement search = element.findElement(By.id("search"));

                WebElement inputSearch  = search.findElement(By.id("search.keyword.query"));

                String searchKeyword = cafeinfoDto.getPlace_name();

                System.out.println("searchKeyword : " + searchKeyword );

                inputSearch.sendKeys(Keys.CONTROL + "A");
                inputSearch.sendKeys(searchKeyword);
                Thread.sleep(2000);
                inputSearch.sendKeys(Keys.ENTER);
                Thread.sleep(2000);

                WebElement placeListDiv = driver.findElement(By.id("info.search.place.list"));
                // 검색된 장소 리스트 중 첫번째 장소 정확도가 제일 높으므로
                // 첫번째 장소를 가져온다.
                if(placeListDiv.findElements(By.tagName("li")).size() > 0){
                    WebElement placeItem = placeListDiv.findElements(By.tagName("li")).get(0);
                    WebElement placeContact = placeItem.findElement(By.className("contact"));
                    WebElement moreView = placeContact.findElement(By.className("moreview"));

                    String moreViewUrl = moreView.getAttribute("href");
                    driver.get(moreViewUrl);

                    // 브라우저 로딩될 때까지 잠시 기다린다.
                    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(3000));

                    //상세화면 Content 영역 접근
                    if(driver.findElements(By.id("kakaoContent")).size() > 0){

                        WebElement moreViewContent = driver.findElement(By.id("kakaoContent"));

                        //상세정보 영역 카테고리 리스트
                        List<WebElement> detailinfo_default_list = moreViewContent.findElements(By.className("placeinfo_default"));

                        //해시태그는 5번째 있으면~
                        for(WebElement detailInfo : detailinfo_default_list){
                            if(detailInfo.findElements(By.className("txt_tag")).size() > 0){

                                WebElement span = detailInfo.findElement(By.tagName("span"));
                                List<WebElement> aList = detailInfo.findElements(By.tagName("a"));

                                for(WebElement a  : aList){
                                    String tagName = a.getText().replaceAll("#", "");        // 키워드
                                    int cafeId = cafeinfoDto.getId(); // 카페 ID

                                    if(StringUtils.isNotEmpty(tagName)){
                                        //DB에 키워드 목록이 있는지 여부 확인
                                        //DB 목록을 모두 가져와서 키워드 명이 같은지 유효성 검사를 진행
                                        //키워드 값이 같으면 카페 keyword info테이블만 insert
                                        //키워드 값이 같지 않으면 keyword 테이블과 카페 keyword info테이블 둘다 insert

                                        //키워드 명이랑 키워드 ID가 있어야되는 테이블이 있고
                                        //키워드 정보를 담고 있는 카페 keyword 테이블이 있어야 함.

                                        //키워드 목록 전체 가져오기
                                        List<KeywordDto> keywordDtoList = keywordRepository.findAll();

                                        List<KeywordDto> keywordIdDtos = keywordDtoList.stream().filter(s ->
                                                s.getKeywordName().replaceAll(" ", "")
                                                        .equals(tagName)).collect(Collectors.toList());

                                        CafeKeywordInfoDto cafeKeywordInfoDto = new CafeKeywordInfoDto();
                                        cafeKeywordInfoDto.setId(cafeId);

                                        KeywordDto keywordIdDto = new KeywordDto();
                                        //키워드가 이미 키워드 DB에 존재하면
                                        if(!CollectionUtils.isEmpty(keywordIdDtos) && keywordIdDtos.size() > 0){
                                            //카페 키워드 정보 insert
                                            cafeKeywordInfoDto.setKeywordId(String.valueOf(keywordIdDtos.get(0).getKeywordId()));
                                            cafeKeywordInfoRepository.save(cafeKeywordInfoDto);

                                        }else{
                                            //키워드 DB insert
                                            keywordIdDto.setKeywordName(tagName);
                                            keywordRepository.save(keywordIdDto);
                                            //카페키워드 정보 DB insert
                                            cafeKeywordInfoDto.setKeywordId(String.valueOf(keywordIdDto.getKeywordId()));
                                            cafeKeywordInfoRepository.save(cafeKeywordInfoDto);
                                        }
                                    }
                                }
                            }else{
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
}