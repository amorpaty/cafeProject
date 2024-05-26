package com.cafe.cafeproject;

import com.cafe.cafeproject.common.dto.CafeMenuInfoDto;
import com.cafe.cafeproject.common.dto.CafeinfoDto;
import com.cafe.cafeproject.common.repository.CafeMenuInfoRepository;
import com.cafe.cafeproject.common.repository.DaumOpenApiRepository;
import com.cafe.cafeproject.daumOpenApi.DaumOpenApiTest;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.Duration;
import java.util.List;

/**
 * 카페 메뉴 크롤링 - 네이버 지도 API
 * @apiNote 네이버 지도에서 카페 메뉴 얻기 API
 * @author 남은주
 * @since  2024.05.26
 */
@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest
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

    private static final String requestUrl = "https://map.naver.com/v5/search";

    /**
     * 네이버 크롤링
     */
    @Test
    public void cafeCralling() throws Exception {


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");

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
}
