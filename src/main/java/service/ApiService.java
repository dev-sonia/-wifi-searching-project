package service;

import dto.Wifi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

	// GSON : JSON구조를 띄는 직렬 데이터를 JAVA의 객체로 역직렬화 / 직렬화 해주는 Library
    private static final String KEY = "59744c58466c736135346b734e5255";
    private static final String API_URL = "http://openapi.seoul.go.kr:8088/";

    public long totalCount;
    public JSONObject wifiResult;

    Wifi wifi;
    List<Wifi> wifiList = new ArrayList<>();
    WifiService wifiService = new WifiService();

    // JSONObject를 반환하는 OpenAPi 연결 함수
    public JSONObject ApiExplorer() {
        try {

            URL url = new URL(API_URL + KEY + "/json/TbPublicWifiInfo/1/5/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");
            // System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = br.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            wifiResult = (JSONObject) jsonObject.get("TbPublicWifiInfo");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiResult;
    }

    // getTotalCount - 공공 API가 보유한 wifi 수 확인하는 함수
    public long getTotalCount() {
        ApiExplorer();
        totalCount = (Long) wifiResult.get("list_total_count");
        return totalCount;
    }

    // load - 1000개씩 데이터 로드하는 함수
    public void load() {
        // api의 END 부분을 1000이상으로 설정하니 계속해서 에러가 발생
        // 1000개씩 불러오는게 가능한 상태
        // 1000개씩 파싱후, 저장하는 내용을 DB 코드쪽에서 실행하니 COLUM IS NOT UNIQUE 에러 발생하기 때문에 해당 함수에서 wifiService.saveDB() 실행
        long curCnt = totalCount / 1000;
        long curRest = totalCount % 1000;


        for (int i = 0; i < curCnt; i++) {
            long now = 1 + (i * 1000);
            parsingApi( now, now+999);
        }
        parsingApi(totalCount - curRest + 1, totalCount);
        // 디비 등록
        wifiService.saveDB(wifiList);
    }

    // parsingApi -  파싱 데이터 저장하는 함수
    public void parsingApi(long start, long end) {

        try {
            URL url = new URL(API_URL + KEY + "/json/TbPublicWifiInfo/"+ start +"/"+ end +"/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");
            // System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = br.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            wifiResult = (JSONObject) jsonObject.get("TbPublicWifiInfo");
            JSONArray parse_listArr = (JSONArray) wifiResult.get("row");

            for (int i = 0; i < parse_listArr.size(); i++) {
                JSONObject wifiObject = (JSONObject) parse_listArr.get(i);

                wifi = new Wifi();

                // 16개 정보 저장
                wifi.setWifiNum((String) wifiObject.get("X_SWIFI_MGR_NO")); // 넘버
                wifi.setGu((String) wifiObject.get("X_SWIFI_WRDOFC")); // 구
                wifi.setName((String) wifiObject.get("X_SWIFI_MAIN_NM")); // 이름
                wifi.setAddress1((String) wifiObject.get("X_SWIFI_ADRES1")); // 주소1
                wifi.setAddress2((String) wifiObject.get("X_SWIFI_ADRES2")); // 주소2
                wifi.setFloor((String) wifiObject.get("X_SWIFI_INSTL_FLOOR")); // 설치 층
                wifi.setType((String) wifiObject.get("X_SWIFI_INSTL_TY")); // 설치 타입
                wifi.setOrg((String) wifiObject.get("X_SWIFI_INSTL_MBY")); // 설치 기관
                wifi.setSvc((String) wifiObject.get("X_SWIFI_SVC_SE")); // 서비스
                wifi.setMangType((String) wifiObject.get("X_SWIFI_CMCWR")); // 망타입
                wifi.setInstallYear((String) wifiObject.get("X_SWIFI_CNSTC_YEAR")); // 설치년도
                wifi.setInout((String) wifiObject.get("X_SWIFI_INOUT_DOOR")); // 실내외
                wifi.setWifiEnv((String) wifiObject.get("X_SWIFI_REMARS3")); // 환경
                wifi.setLat((String) wifiObject.get("LAT")); // 위도
                wifi.setLnt((String) wifiObject.get("LNT")); // 경도
                wifi.setWorkDay((String) wifiObject.get("WORK_DTTM")); // 작업일자

                wifiList.add(wifi);
            }

            // 디비 등록 - 해당 부분에서 진행하면 sqlite Error 발생
            //wifiService.saveDB(wifiList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}