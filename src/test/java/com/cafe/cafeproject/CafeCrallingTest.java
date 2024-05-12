package com.cafe.cafeproject;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

@SpringBootTest
public class CafeCrallingTest {

    private WebDriver driver;
    private WebElement element;

    // 드라이버 설치 경로
    private static final String keyword = "카페";
    private static final String requestUrl = "https://map.naver.com/v5/search";
    /**
     * 네이버 크롤링
     */
    @Test
    public void cafeCralling(){

        try {

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);

            // (1) 브라우저에서 url로 이동한다.
            driver.get(requestUrl);
            // 브라우저 로딩될 때까지 잠시 기다린다.
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

            // (2) 검색결과 iframe으로 frame을 바꾼다.
            driver.switchTo().frame(driver.findElement(By.id("searchIframe")));

            // 검색 결과 장소 목록을 elements에 담는다.
            //List<WebElement> elements = driver.findElements(By.cssSelector(".Ryr1F>UL>LI"));
//
//            System.out.println("TestTest**********************************");
//            System.out.println("elements.size() = " + elements.size());

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
