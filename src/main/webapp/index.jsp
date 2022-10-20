<%@ page import="service.HistoryService" %>
<%@ page import="service.WifiService" %>
<%@ page import="dto.Wifi" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
</head>
<style>
    header .sub-menu {
        display: flex;
        margin-top: -20px;
    }

    header .sub-menu ul.menu {
        list-style: none;
        padding-left: 0;
        font-family: Arial, sans-serif;
        display: flex; /* 수평 */
    }

    header .sub-menu ul.menu li {
        position: relative;
    }

    header .sub-menu ul.menu li::before {
        /* 가상요소 선택자, 앞쪽에 넣기 가능, 인라인요소*/
        content: "";
        width: 1px;
        height: 12px;
        background-color: #504E48;
        position: absolute;
        /* position 명명시, 자동으로 block요소 됨 */
        top: 0;
        bottom: 0;
        margin: auto;
    }

    header .sub-menu ul.menu li:first-child::before {
        display: none;
    }

    header .sub-menu ul.menu li a {
        font-size: 12px;
        padding: 11px 16px; /* 선택시 폭 주기 */
        display: block;
        color: #000;
    }

    table {
        width: 100%;
        margin-top: 20px;
        border-collapse: collapse;
    }

    th, td {
        border: solid 1px #000;
    }

    th {
        background-color: #04AA6D;
        color: white;
    }

    td {
        height: 50px;
        padding-left: 5px;
        text-align: center;
    }

    tr:nth-child(odd) {
        background-color: #f2f2f2;
    }
</style>

<script type="text/javascript">
    function success({coords}) {
        document.getElementById('lat').value = coords.latitude;  // 위도
        document.getElementById('lnt').value = coords.longitude;  // 경도
    }

    function getLocation() {
        if (!navigator.geolocation) {
            alert('해당 브라우저는 GPS를 지원하지 않습니다');
        } else navigator.geolocation.getCurrentPosition(success);
    }
</script>

<body>

<!-- HEADER -->
<header>
    <h2 style="padding-left: 10px;">와이파이 정보 구하기</h2>
    <div class="sub-menu">
        <ul class="menu">
            <li><a href="index.jsp">홈</a></li>
            <li><a href="history.jsp">위치 히스토리 목록</a></li>
            <li><a href="load-wifi.jsp" onclick="this.form.action='load-wifi.jsp';">Open API 와이파이 정보가져오기</a></li>
        </ul>
    </div>

    <form action="index.jsp" method="get">
        <%
            String latitude = request.getParameter("lat");
            String longitude = request.getParameter("lnt");
        %>
        <div class="location" style="padding-left: 10px; display: flex;">
            LAT : <input type="text" name="lat" id="lat"  value=<%=latitude%>/>
            , LNT : <input type="text" name="lnt" id="lnt"  value=<%=longitude%>/>
            <input type="button" value="내 위치 가져오기" style="margin-left: 10px;" onclick="getLocation()">
            <input type="submit" value="근처 WIFI 정보 보기" style="margin-left: 10px;">
        </div>
    </form>
</header>

<table>
    <tbody>
    <tr>
        <th>거리(Km)</th>
        <th>관리번호</th>
        <th>자치구</th>
        <th>와이파이명</th>
        <th>도로명주소</th>
        <th>상세주소</th>
        <th>설치위치(층)</th>
        <th>설치유형</th>
        <th>설치기관</th>
        <th>서비스구분</th>
        <th>망종류</th>
        <th>설치년도</th>
        <th>실내외구분</th>
        <th>WIFI접속환경</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>작업일자</th>
    </tr>

        <%
        if (latitude == null || longitude == null) {
        %>
    <tr>
        <td colspan="17">위치 정보를 입력한 후에 조회해 주세요.</td>
    </tr>
        <%
        } else {
            HistoryService historyService = new HistoryService();
            historyService.saveHistory(latitude, longitude); // 위치히스토리 목록에 저장하기

            WifiService wifiService = new WifiService();
            List<Wifi> wifiList = wifiService.list(latitude, longitude); // 가까운 20개 정보 가져오기
            for (Wifi wifi : wifiList){
            %>
    <tr>
        <td><%=wifi.getDistance()%>
        </td>
        <td><%=wifi.getWifiNum()%>
        </td>
        <td><%=wifi.getGu()%>
        </td>
        <td><%=wifi.getName()%>
        </td>
        <td><%=wifi.getAddress1()%>
        </td>
        <td><%=wifi.getAddress2()%>
        </td>
        <td><%=wifi.getFloor()%>
        </td>
        <td><%=wifi.getType()%>
        </td>
        <td><%=wifi.getOrg()%>
        </td>
        <td><%=wifi.getSvc()%>
        </td>
        <td><%=wifi.getMangType()%>
        </td>
        <td><%=wifi.getInstallYear()%>
        </td>
        <td><%=wifi.getInout()%>
        </td>
        <td><%=wifi.getWifiEnv()%>
        </td>
        <td><%=wifi.getLat()%>
        </td>
        <td><%=wifi.getLnt()%>
        </td>
        <td><%=wifi.getWorkDay()%>
        </td>
    </tr>
        <%
        }
            }
%>
</table>
</body>
</html>