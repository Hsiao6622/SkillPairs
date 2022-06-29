<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="org.javatuples.Tuple"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ page isELIgnored="false"%>
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script
	src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!DOCTYPE html>
<html lang="en">

<head>
<title>聊天列表</title>

<meta charset="UTF-8">
<script src="./personal/MemberJS/jquery-M.js"></script>
<script src="./personal/MemberJS/Member.js"></script>
<link rel="stylesheet" href="./personal/MemberCSS/jquery-ui.css">
<link rel="stylesheet"
	href="./personal/MemberCSS/jquery-ui.structure.css">
<link rel="stylesheet" href="./personal/MemberCSS/jquery-ui.theme.css">
<script src="./personal/MemberJS/jquery.js"></script>
<script src="./personal/MemberJS/jquery-ui.js"></script>
<script src="./personal/MemberJS/datepicker-zh-TW.js"></script>



<!-- 這段是websocket -->

<script type="text/javascript">
	var websocket = new WebSocket(
			"ws://localhost:8080/skillpairs/mutilChatroomServerEndpoint");

	var intID = getdata().roomID;
	var username = decodeURI(getdata().id);
	var connectdata = '{"ConnectToroomID":' + intID + ',"modify":true' + '}'
	function initWebSocket() {
		var websocket = new WebSocket(
				"ws://localhost:8080/skillpairs/mutilChatroomServerEndpoint");
		websocket.onopen = function() {
			//websocket.send(roomID);
			websocket.send(connectdata);
		}
		websocket.onmessage = function processMessage(message) {
			//alert("onprocess");
			var jsonData = JSON.parse(message.data);
			if (jsonData.message != null)
				messagesTextArea.value += jsonData.message + "\n";
		}
		websocket.onclose = function(obj) {
			initWebSocket();
			console.log("CLOSED!!");
		}
	}
	websocket.onopen = function() {
		//websocket.send(roomID);
		websocket.send(connectdata);
	}
	websocket.onmessage = function processMessage(message) {
		//alert("onprocess");
		var jsonData = JSON.parse(message.data);
		if (jsonData.message != null)
			messagesTextArea.value += jsonData.message + "\n";
	}
	websocket.onclose = function(obj) {
		websocket.send("haha");
		console.log("haha");
		initWebSocket();
		console.log("CLOSED!!");
	}
	function sendMessage() {

		//alert("hai");
		console.log("1")
		websocket.send('{"message":"' + username + ":" + messageText.value
				+ '",' + '"roomID":' + intID + "," + '"username":' + username
				+ "}");
		messageText.value = "";
	}

	function getdata() {
		var url = location.href;
		var obj = {};
		var str = url.split('?')[1].split('&');
		for (i = 0; i <= str.length - 1; i++) {
			a = str[i].split('=')
			obj[a[0]] = a[1]
		}
		return obj
	}

	function delete_friend() {
		//.....
	}
</script>



<meta charset="UTF-8">
<script src="./personal/MemberJS/jquery-M.js"></script>
<script src="./personal/MemberJS/Member.js"></script>
<link rel="stylesheet" href="./personal/MemberCSS/jquery-ui.css">
<link rel="stylesheet"
	href="./personal/MemberCSS/jquery-ui.structure.css">
<link rel="stylesheet" href="./personal/MemberCSS/jquery-ui.theme.css">
<script src="./personal/MemberJS/jquery.js"></script>
<script src="./personal/MemberJS/jquery-ui.js"></script>
<!-- 這段是websocket -->


<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css"
	type="text/css" rel="stylesheet" />
<!-- 繁體中文 -->
<!-- 這裡是聊天頁面的css們 -->
<style type="text/css">
.msg_send_btn {
	background: #05728f none repeat scroll 0 0;
	border: medium none;
	border-radius: 50%;
	color: #fff;
	cursor: pointer;
	font-size: 17px;
	height: 33px;
	position: absolute;
	right: 0;
	top: 11px;
	width: 33px;
}

.input_msg_write input {
	background: rgba(0, 0, 0, 0) none repeat scroll 0 0;
	border: medium none;
	color: #4c4c4c;
	font-size: 15px;
	min-height: 48px;
	width: 100%;
}

.type_msg {
	border-top: 1px solid #c4c4c4;
	position: relative;
}

.msg_history {
	height: 516px;
	overflow-y: auto;
	border: 0px;
}

.mesgs {
	float: left;
	padding: 30px 15px 0 25px;
	width: 60%;
}

.inbox_people {
	background: #f8f8f8 none repeat scroll 0 0;
	float: left;
	overflow: hidden;
	width: 40%;
	border-right: 1px solid #c4c4c4;
}

.chat_people {
	overflow: hidden;
	clear: both;
}

.chat_ib {
	float: left;
	padding: 0 0 0 15px;
	width: 88%;
}

.chat_list {
	border-bottom: 1px solid #c4c4c4;
	margin: 0;
	padding: 18px 16px 10px;
}

.inbox_chat {
	height: 550px;
	overflow-y: scroll;
}

.chat_img {
	float: left;
	width: 11%;
}
<!--
這裡是聊天頁面的css們

 

-->
</style>
<!-- 響應式網站 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- 載入 Bootstrap 的CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-wEmeIV1mKuiNpC+IOBjI7aAzPcEZeedi5yW5f2yOq55WWLwNGmvvx4Um1vskeMj0"
	crossorigin="anonymous">

<!-- 小圖示連結FontAwesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">

<!-- 連結導覽列CSS-navbar.css -->
<link rel="stylesheet" href="./personal/navbar.css">

<!-- 連結內容CSS-style.css -->
<link rel="stylesheet" href="./personal/style.css">

<!-- 中間區塊css -->
<link rel="stylesheet" href="./chatRoom/chatroom.css">
</head>

<body>


	<!-- 最上層-logo條  -->
	<nav class="navbar navbar-expand-lg navbar-light">

		<div class="container-fluid">
			<!-- 品牌logo -->
			<a class="navbar-brand" href="#"><img width="100px" height="50px"
				src="./img/Spairs-removebg.png"></a>

		</div>
	</nav>



	<!-- 中間區塊、主內容 -->
	<div class="container-fluid">
		<div class="row connect">

			<!-- 左欄位 -->
		<div class="col sidenav">
				<h4>Skill Pairs</h4>
				<div class="page">
					<a href="home.html"><i class="fa-solid fa-house-user">&emsp;</i>首頁</i></a>
				</div>
				
				<div class="page">
					<a href="MemberP.html"><i class="fa-solid fa-circle-user">&emsp;</i>修改個人檔案</a>
				</div>
				
				<div class="page">
					<a href="settings.html"><i class="fa-solid fa-file-circle-plus"></i></i>配對偏好設定</a>
				</div>
				
				<div class="page">
					<a href="DatabaseAccess"><i class="fa-solid fa-comment-dots">&emsp;</i>聊天室</a>
				</div>
				
				<div class="page">
					<a href="about.html"><i class="fa-solid fa-circle-info">&emsp;</i>關於</a>
				</div>
			</div>



			<!-- 中間區塊 -->


			<div class="col-sm-8 main-content ">
				<!-- 這裡是聊天列表的起點 -->
				<div class="inbox_people">
					<div class="inbox_chat">
						<c:forEach items="${roomsssss}" var="roomdata2">


							<c:set var="arrayofmsg" value="${fn:split(roomdata2,'=')}" />
							<c:set var="idid" value="${arrayofmsg[0]}" />
							<c:set var="namenmes" value="${arrayofmsg[1]}" />
							<c:set var="namenmes2" value="${fn:split(namenmes,',')}" />

							<sql:setDataSource var="snapshot"
								driver="com.mysql.cj.jdbc.Driver"
								url="jdbc:mysql://localhost:3306/skillpairs?useUnicode=true&characterEncoding=utf-8"
								user="root" password="root" />

							<sql:query dataSource="${snapshot}" var="result">
					SELECT FirstUserID from room r where r.RoomID=${arrayofmsg[0]};
					</sql:query>

							<c:forEach var="row" items="${result.rows}">
								<c:set var="ImgUserID" value="${row.FirstUserID}" />
							</c:forEach>

							<c:if test='${ImgUserID ==  sessionScope["UID"]}'>
								<sql:query dataSource="${snapshot}" var="result">
						SELECT SecondUserID from room r where r.RoomID=${arrayofmsg[0]};
						</sql:query>
								<c:forEach var="row" items="${result.rows}">
									<c:set var="ImgUserID" value="${row.SecondUserID}" />
								</c:forEach>

							</c:if>
							<sql:query dataSource="${snapshot}" var="result">
					SELECT UserImg from member m where m.UserID=${ImgUserID};
					</sql:query>
							<c:forEach var="row" items="${result.rows}">
								<c:set var="UserImg" value="${row.UserImg}" />
							</c:forEach>


							<!-- 第一條 -->
							<div class="chat_list">
								<div class="chat_people" onclick="javascript:location.href='http://localhost:8080/skillpairs/DatabaseAccess?id=<%=request.getAttribute("name") %>&roomID=${arrayofmsg[0]}'" />>
									<!-- 左 -->
									<div class="chat_img">
										<!--  -->
										<img src="chatroomview.jsp?otherimg=${ImgUserID}"
											height="43px" width="43px"
											style="display: block; margin: auto;"
											/>
									</div>
									<!-- 右 -->
									<div class="right chat_ib">
										<h5>${fn:replace(namenmes2[0], "[","")}
											<span class="chat_date" style="float: right;">
												<button onclick="javascript:location.href='http://localhost:8080/skillpairs/deleteFriend?deleteRoomID=${arrayofmsg[0]}'">X</button>

											</span>
										</h5>
										<p>${fn:replace(namenmes2[1], "]","")}</p>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<!-- 這裡是聊天列表的終點 -->
				<!-- 這裡是訊息欄的起點 -->
				<div class="mesgs">
					<div class="col-sm-6 main-content">

						<textarea id="messagesTextArea" readonly="readonly" rows="10"
							cols="45" class="msg_history"></textarea>
					</div>
					<div class="type_msg">
						<div class=".input_msg_write">
							<input type="text" id="messageText" size="50" class="write_msg" />
							<!-- <input type="button" value="Send" onClick="sendMessage();"/> -->
							<button class="msg_send_btn" type="button"
								onClick="sendMessage();">
								<i class="fa fa-paper-plane-o" aria-hidden="true"></i>
							</button>
						</div>
						<br />
					</div>
				</div>
				<!-- 這裡是訊息欄的終點 -->
			</div>


			<!-- 右欄位 -->
			<div class="col sidenav">
				<h4>升級付費版</h4>
				<div id="carouselExampleInterval" class="carousel slide"
					data-bs-ride="carousel">

					<div class="carousel-indicators">
						<button type="button" data-bs-target="#carouselExampleIndicators"
							data-bs-slide-to="0" class="active" aria-current="true"
							aria-label="Slide 1"></button>
						<button type="button" data-bs-target="#carouselExampleIndicators"
							data-bs-slide-to="1" aria-label="Slide 2"></button>
						<button type="button" data-bs-target="#carouselExampleIndicators"
							data-bs-slide-to="2" aria-label="Slide 3"></button>
					</div>

					<div class="carousel-inner">
						<div class="carousel-item active" data-bs-interval="2000">
							<img src="./img/no-stopping.png" class="d-block w-100" alt="...">
							<div class="carousel-caption">去除廣告</div>
						</div>
						<div class="carousel-item" data-bs-interval="2000">
							<img src="./img/heart1.png" class="d-block w-100" alt="...">
							<div class="carousel-caption">無限按讚</div>
						</div>
						<div class="carousel-item" data-bs-interval="2000">
							<img src="./img/returning-visitor.png" class="d-block w-100"
								alt="...">
							<div class="carousel-caption">倒回功能</div>
						</div>
					</div>

				</div>
				<br>
				<div class="d-grid gap-2 col-6 mx-auto">
					<button type="button" class="btn btn-warning btn-lg">前往</button>
				</div>
			</div>


		</div>
	</div>
	</div>

	<!-- Boostrap JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8"
		crossorigin="anonymous"></script>

</body>

</html>