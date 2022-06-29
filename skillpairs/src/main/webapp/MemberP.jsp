<%@page import="java.util.LinkedList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>查看個人檔案</title>

<meta charset="UTF-8">
    <script src="./personal/MemberJS/jquery-M.js"></script>
    <script src="./personal/MemberJS/Member.js"></script>
    <script src="./personal/MemberJS/jquery.js"></script>
    <script src="./personal/MemberJS/jquery-ui.js"></script>
    <script src="./personal/MemberJS/datepicker-zh-TW.js"></script>

    <link rel="stylesheet" href="./personal/MemberCSS/jquery-ui.css">
    <link rel="stylesheet" href="./personal/MemberCSS/jquery-ui.structure.css">
    <link rel="stylesheet" href="./personal/MemberCSS/jquery-ui.theme.css">

    <!-- 響應式網站 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- 載入 Bootstrap 的CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-wEmeIV1mKuiNpC+IOBjI7aAzPcEZeedi5yW5f2yOq55WWLwNGmvvx4Um1vskeMj0" crossorigin="anonymous">

    <!-- 小圖示連結FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">

    <!-- 連結導覽列CSS-navbar.css -->
    <link rel="stylesheet" href="./personal/navbar.css">

    <!-- 連結內容CSS-style.css -->
    <link rel="stylesheet" href="./personal/style.css">

    <!-- 上傳照片區CSS-img.css -->
    <link rel="stylesheet" href="./personal/img.css">



    <!-- 上傳照片區JS -->
    <script src="./personal/img.js"></script>
</head>
<body>

   <!-- 最上層-logo條  -->
    <nav class="navbar navbar-expand-lg navbar-light">

        <div class="container-fluid">
            <!-- 品牌logo -->
            <a class="navbar-brand" href="#"><img width="100px" height="40px" src="./img/Spairs-removebg.png"></a>

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



            <!-- 單純排版用 -->
            <div class="col-sm-2 main-content"></div>

            <!-- 中欄位 -->
            <div class="col-sm-4 main-content text-left">

 				<h2>
					&emsp;&nbsp;<i class="fa-solid fa-user-check"></i>&nbsp;&nbsp;</i>個人檔案
				</h2>

                <br><br>
                       
                    <label for=""><i class="fa-solid fa-photo-film"></i>　頭像：</label>
                     <br><br>
                     &emsp;&emsp;&emsp;&emsp;&emsp;
                    <img src="view.jsp" width="200" height="200" border="3"><br>
                    <br>	    
                    <label for="userName"><i class="fa-solid fa-user"></i>&nbsp;&nbsp;&nbsp;&nbsp;暱稱：</label>
                      <%= request.getAttribute("UserName") %> <br><br>

                    <label for="date"><i class="fa-solid fa-calendar-days"></i>&nbsp;&nbsp;&nbsp;&nbsp;生日：</label>
                     <%= request.getAttribute("UserDate") %> <br><br>

                    <label for=""><i class="fa-solid fa-venus-mars"></i>&nbsp;&nbsp;&nbsp;性別：</label>
					 <%= request.getAttribute("UserGender") %> <br><br>
   
                    <label for="city"><i class="fa-solid fa-location-dot"></i>&nbsp;&nbsp;居住地：</label>
                     <%= request.getAttribute("city") %> <br><br>

                    <label for="job"><i class="fa-solid fa-briefcase"></i>&nbsp;&nbsp;&nbsp;&nbsp;職業：</label>
                     <%= request.getAttribute("job") %> <br><br>

                    <label for=""><i class="fa-solid fa-kitchen-set"></i>&nbsp;&nbsp;&nbsp;&nbsp;技能：</label>
			    	 <% LinkedList<String> skillsforsql=(LinkedList<String>)request.getAttribute("skill");
					for(String skill:skillsforsql){
					%>
					<span> <%= skill.toString() %></span>
					<%} %> 
 					
 					<br>
					<br>
    				<label for="job">自我介紹：</label>
    				<textarea id="introduction" name="introduction" placeholder="自我介紹" readonly="readonly"><%= request.getAttribute("introduction") %> </textarea>
    				
    				
 					<br> <br>
					
					 &emsp; &emsp; &emsp; &emsp; &emsp; &emsp; &emsp;
					
                    <button id="btnGet"  onclick="window.location.href='MemberP.html'">修改個人檔案</button>

                   
            </div>
            
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
							<img src="./img/no-stopping.png"
								class="d-block w-100" alt="...">
							<div class="carousel-caption">去除廣告</div>
						</div>
						<div class="carousel-item" data-bs-interval="2000">
							<img src="./img/heart1.png" class="d-block w-100"
								alt="...">
							<div class="carousel-caption">無限按讚</div>
						</div>
						<div class="carousel-item" data-bs-interval="2000">
							<img src="./img/returning-visitor.png"
								class="d-block w-100" alt="...">
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
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8"
        crossorigin="anonymous"></script>



</body>
</html>