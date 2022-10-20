<%@ page import="service.HistoryService" %>
<%@ page import="dto.History" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>와이파이 정보 구하기</title>
<meta charset="UTF-8">
</head>
<style>
header .sub-menu {
	display: flex;
	margin-top: -20px;
}

header .sub-menu ul.menu {
	list-style: none;
	padding-left: 0;
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
	margin-top: 10px;
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
	padding-left: 10px;
	padding-top: 5px;
	padding-bottom: 5px;
}

tr:nth-child(odd) {
	background-color: #f2f2f2;
}

td:nth-child(5) {
	padding-left: 0;
	text-align: center;
}
</style>
<body>
	<!-- HEADER -->
	<header>
		<h2 style="padding-left: 10px;">위치 히스토리 목록</h2>
		<div class="sub-menu">
			<ul class="menu">
				<li><a href="index.jsp">홈</a></li>
				<li><a href="history.jsp">위치 히스토리 목록</a></li>
				<li><a href="javascript:void(0)">Open API 와이파이 정보 가져오기</a></li>
			</ul>
		</div>

	</header>

	<table>
		<colgroups>
		<col style="width: 10%;" />
		<col style="width: 25%;" />
		<col style="width: 25%;" />
		<col style="width: 30%;" />
		<col style="width: 10%;" />
		</colgroups>
		<tbody>
			<tr>
				<th>ID</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>조회일자</th>
				<th>비고</th>
			</tr>

			<%
				HistoryService historyService = new HistoryService();
				int N = historyService.cntHistory();
				List<History> historyList = historyService.showHistory(N);

				for (History history : historyList){
			%>
			<tr>
				<td><%=history.getId()%></td>
				<td><%=history.getxCoordinate()%></td>
				<td><%=history.getyCoordinate()%></td>
				<td><%=history.getWriteDate()%></td>
				<td style="text-align: center"><input type="submit" value="삭제"></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>

</body>
</html>