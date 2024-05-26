package com.cafe.cafeproject.sigunguCodeDataPotalApiTest;

import com.cafe.cafeproject.common.dto.BubjdinfoDto;
import com.cafe.cafeproject.common.dto.SidoinfoDto;
import com.cafe.cafeproject.common.dto.SigguinfoDto;
import com.cafe.cafeproject.common.repository.BubjdCodeRepository;
import com.cafe.cafeproject.common.repository.SidoCodeRepository;
import com.cafe.cafeproject.common.repository.SigunguCodeRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.geolatte.geom.Geometry;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * CSV GEOM Data 읽기
 * @apiNote 시군구 동까지의 데이터를 가공하여 DB에 적재하기 위한 Test / CSV
 * @author 남은주
 * @since  2024.05.19
 */
@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GeomDataExelReadApiTest {

//    @Value("${vworkd.apiKey}")
//    public String vworldApiKey;
//
//    //광역시도 정보 Url
//    @Value("${vworld.sidoApiUrl}")
//    public String sidoApiUrl;

    @Autowired
    private SidoCodeRepository sidoCodeRepository;

    @Autowired
    private SigunguCodeRepository sigunguCodeRepository;

    @Autowired
    private BubjdCodeRepository bubjdCodeRepository;

    /**
     * 시군구 API 연동
     */
    @Test
    public void sidoDataExelReadGeomApiTest() {

        String fileName = "시도_geometry 정보까지의 데이터.xlsx";

        File file = new File(Paths.get("C:\\Users\\은주\\OneDrive\\바탕 화면\\eunju\\유일무이\\시도, 시군구, 법정동 코드", fileName).toString());

        if (file.exists()) {
            //파일읽어 데이터 가공
            List<SidoinfoDto> sidoCodeDtoList = getReadGeomData(file);
            //List<SigguinfoDto> sigguinfoDtoList = getReadSigunguData(file);

        }
    }

    /**
     * 엑셀 파일 읽기
     * @param file
     * @return List<SidoCodeDto>
     */
    public List<SidoinfoDto> getReadGeomData(File file) {

        List<SidoinfoDto> sidoCodeDtoList = new ArrayList<>();
        WKTReader wktReader =  new WKTReader();
        try {
            FileInputStream fis = new FileInputStream(file);

            //파일을 읽어서 excel 형식으로 읽어들이기
            //확장자 .xlsx 파일 읽음
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            //시트 갯수 추출
            int sheetCnt = workbook.getNumberOfSheets();

            // 세번째 시트 추출 : 시도 데이터 세번째 시트에 있음
            XSSFSheet curSheet = workbook.getSheetAt(0);

            //Row를 iterator로 받는다
            Iterator<Row> rowIterator = curSheet.iterator();

            // 해당 시트에 row가 있을떄까지 for
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                // 첫번째 행은 지나친다.
                if (row.getRowNum() == 0) {
                    continue;
                }

                SidoinfoDto sidoCodeDto = new SidoinfoDto();
                double sidoCode = row.getCell(0).getNumericCellValue();
                int sidoCode1 = (int) sidoCode ;
                String geom = row.getCell(3).getStringCellValue();

                //sidoCodeRepository.findById(sidoCode);
                sidoCodeDto.setSidoCode(String.valueOf(sidoCode1));

                sidoCodeDto = sidoCodeRepository.findById(String.valueOf(sidoCode1)).get();
                sidoCodeDto.setGeom(geom);

                System.out.println(sidoCodeDto.toString());
                sidoCodeRepository.save(sidoCodeDto);

                sidoCodeDtoList.add(sidoCodeDto);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sidoCodeDtoList;
    }
        /**
         * sigungu 엑셀 파일 읽기
         * @param file
         * @return List<SidoCodeDto>
         */
        public List<SigguinfoDto> getReadSigunguData(File file) {

            List<SigguinfoDto> sigunguList = new ArrayList<>();
            try {
                FileInputStream fis = new FileInputStream(file);

                //파일을 읽어서 excel 형식으로 읽어들이기
                //확장자 .xlsx 파일 읽음
                XSSFWorkbook workbook = new XSSFWorkbook(fis);

                //시트 갯수 추출
                int sheetCnt = workbook.getNumberOfSheets();

                // 세번째 시트 추출 : 시도 데이터 세번째 시트에 있음
                XSSFSheet curSheet = workbook.getSheetAt(3);

                //Row를 iterator로 받는다
                Iterator<Row> rowIterator = curSheet.iterator();

                // 해당 시트에 row가 있을떄까지 for
                while(rowIterator.hasNext()){

                    Row row = rowIterator.next();

                    // 첫번째 행은 지나친다.
                    if(row.getRowNum() == 0){
                        continue;
                    }

                    SigguinfoDto sigguinfoDto = new SigguinfoDto();
                    String sidoCode = row.getCell(0).getStringCellValue();
                    String sigguCode = row.getCell(1).getStringCellValue();
                    String sigguName = row.getCell(2).getStringCellValue();

                    sigguinfoDto.setSiggCode(sigguCode);
                    sigguinfoDto.setSiggName(sigguName);

                    SidoinfoDto sidoinfoDto  = new SidoinfoDto();
                    sidoinfoDto.setSidoCode(sidoCode);
                    sigguinfoDto.setSidoinfoDto(sidoinfoDto);

                    sigunguList.add(sigguinfoDto);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return sigunguList;
    }

    /**
     * 엑셀 파일 읽기
     * @param file
     * @return List<BubjdinfoDto>
     */
    public List<BubjdinfoDto> getReadBubjdData(File file) {

        List<BubjdinfoDto> bubjdinfoDtoList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);

            //파일을 읽어서 excel 형식으로 읽어들이기
            //확장자 .xlsx 파일 읽음
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            //시트 갯수 추출
            int sheetCnt = workbook.getNumberOfSheets();

            // 세번째 시트 추출 : 시도 데이터 세번째 시트에 있음
            XSSFSheet curSheet = workbook.getSheetAt(0);

            //Row를 iterator로 받는다
            Iterator<Row> rowIterator = curSheet.iterator();

            // 해당 시트에 row가 있을떄까지 for
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                // 첫번째 행은 지나친다.
                if (row.getRowNum() == 0) {
                    continue;
                }

                BubjdinfoDto bubjdinfoDto = new BubjdinfoDto();
                String sidoCode = row.getCell(0).getStringCellValue();
                String sigguCode = row.getCell(1).getStringCellValue();
                String bubjdCode = row.getCell(2).getStringCellValue();
                String bubjdName = row.getCell(3).getStringCellValue();


                bubjdinfoDto.setBubjdCode(bubjdCode);
                bubjdinfoDto.setBubjdName(bubjdName);
                bubjdinfoDto.setSidoCode(sidoCode);
                bubjdinfoDto.setSiggCode(sigguCode);

                System.out.println(bubjdinfoDto.toString());

                bubjdinfoDtoList.add(bubjdinfoDto);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bubjdinfoDtoList;
    }

    /**
     * 시도 데이터 DB 저장
     * @param sidoCodeDtoList
     */
    private void save(List<SidoinfoDto> sidoCodeDtoList){
       sidoCodeRepository.saveAll(sidoCodeDtoList);
    }

    /**
     * 시군구 데이터 DB 저장
     * @param sigguinfoDtoList
     */
    private void saveSigg(List<SigguinfoDto> sigguinfoDtoList){

        for (SigguinfoDto sigguinfoDto : sigguinfoDtoList) {
            System.out.println(sigguinfoDto.toString());
        }

        sigunguCodeRepository.saveAll(sigguinfoDtoList);
    }

    /**
     * 법정동 데이터 DB 저장
     * @param bubjdinfoDtoList
     */
    private void saveBubjd(List<BubjdinfoDto> bubjdinfoDtoList){

        bubjdCodeRepository.saveAll(bubjdinfoDtoList);
    }
}
