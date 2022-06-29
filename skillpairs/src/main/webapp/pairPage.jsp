<%@page import="java.util.LinkedList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html lang="en">

<head>
<title>配對</title>

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
<!-- 繁體中文 -->

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
<link rel="stylesheet" href="./pairPage/style.css">

<!-- 中間區塊css -->
<link rel="stylesheet" href="./pairPage/pairPage.css">
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
					<a href="pairPage.jsp"><i class="fa-solid fa-elevator">&emsp;</i>配對主頁</a>
				</div>
				<div class="page">
					<a href="MemberP.html"><i class="fa-solid fa-circle-user">&emsp;</i>個人檔案</a>
				</div>

				<div class="page">
					<a href="DatabaseAccess"><i class="fa-solid fa-comment-dots">&emsp;</i>聊天室</a>
				</div>
				<div class="page">
					<a href="about.html"><i class="fa-solid fa-circle-info">&emsp;</i>關於</a>
				</div>

			</div>

			<!-- 排版用 -->
			<div class="col-sm-2 main-content"></div>

			<!-- 中區塊 -->
			<div div class="col-sm-4 main-content text-left">

				<h2>
					&emsp;&nbsp;<i class="fa-solid fa-heart-circle-check"></i>&nbsp;&nbsp;&nbsp;配對選擇
				</h2>
				
				<br><br>
				
				<div class="uName" >
					&emsp;&emsp;&nbsp;
					<i class="fa-solid fa-user"></i>&nbsp;<strong>暱稱 :</strong>
					&nbsp;
					<%= request.getAttribute("UserName") %>
				</div>
					
					
				<div class="uAge">
					&emsp;&emsp;&nbsp;
					<i class="fa-solid fa-tags"></i>&nbsp;<strong>年齡 :</strong>
					&nbsp;
					<%= request.getAttribute("UserAge") %>
				</div>
				
				
				<div class="uCity">
					&emsp;
					<i class="fa-solid fa-building-flag"></i>&nbsp;<strong>居住地 :</strong>
					&nbsp;
					<%= request.getAttribute("city") %>
				</div>
				
				
				<div class="uJob">
					&emsp;&emsp;&nbsp;
					<i class="fa-solid fa-briefcase"></i>&nbsp;<strong>職業 :</strong>
					&nbsp;
					<%= request.getAttribute("job") %>
				</div>



				<div class="uSkill">
					&emsp;&emsp;&nbsp;
					<i class="fa-solid fa-kitchen-set"></i>&nbsp;<strong>技能 :</strong>
				&nbsp;
				<%
				LinkedList<String> skillsforsql=(LinkedList<String>)request.getAttribute("skill");
				for(String skill:skillsforsql)
				{%><span> <%= skill.toString() + "、" %></span>
				<%} %>
				<%= request.getAttribute("lang") %>				
				</div>
				
				

				<textarea id="introduction1" name="introduction" readonly="readonly"><%= request.getAttribute("introduction") %></textarea>

				<br><br>
	
				<form id="form" action="PairMain" method="post">
					&emsp;&emsp;&emsp;&emsp;&emsp;<button  onclick="PairMain.java" border:none;
					style=border:2px blue none;background-color:white;>
					<img src="https://upload.cc/i1/2022/06/24/mToSxa.png" style=width:60px ></button>
				</form>
			
				
			
				<form id="form" action="PairMainInsert" method="post">
					<input type="text" style="display: none" name="SecondUserID"
						value="<%= request.getAttribute("UserID") %>" />
					&emsp;&emsp;&emsp;&emsp;&emsp;<button id="form" onclick="PairMainInsert.java";
					 style=border:2px blue none;background-color:white;>
					<img src="https://upload.cc/i1/2022/06/23/6zTOBs.png" style=width:60px ></button>
				</form>
			

			</div>

			<!-- 排版用 -->
			<div class="col-sm-2 main-content"></div>

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

	<!-- Boostrap JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8"
		crossorigin="anonymous"></script>

</body>

</html>