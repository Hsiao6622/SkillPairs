window.fbAsyncInit = function() {
	FB.init({
		appId: '321679853474120', // 填入 FB APP ID
		cookie: true,
		xfbml: true,
		version: 'v14.0'
	});

	FB.getLoginStatus(function(response) {
		statusChangeCallback(response);
	});
};

// 處理各種登入身份
function statusChangeCallback(response) {
	console.log(response);
	var target = document.getElementById("FB_STATUS_1"), html = "";

	// 登入 FB 且已加入會員
	if (response.status === 'connected') {

		FB.api('/me?fields=id,name,email,birthday', function(response) {
			//存資料
			document.getElementById('x').value = response.id;
			document.getElementById('z').value = response.email;
			console.log(response.id);
			console.log(response.email);

			$.ajax({
				type: "POST",
				url: "Login",
				contentType: 'text/html; charset=UTF-8',
				data: JSON.stringify({
					"userID": response.id,
					"userName": response.name,
					"userEmail": response.email,
				}),
			})	 
	}),
		
   	 
   		 
		$.ajaxSetup({
			complete: function(obj) {	
					window.location.href =  obj.getResponseHeader("URL"); //將後端重定向的地址取出來,使用win.location.href去實現重定向的要求
			}
		});
		 
	}

	// 登入 FB, 未偵測到加入會員
	else if (response.status === "not_authorized") {
		//  target.innerHTML = "已登入 FB，但未加入 SkillPairs 應用程式";
	}

	// 未登入 FB
	else {
		//   target.innerHTML = "未登入 FB";
	}
}

function checkLoginState() {
	FB.getLoginStatus(function(response) {
		statusChangeCallback(response);
		swal("登入成功");
	});
	
};

// 載入 FB SDK
(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id))
		return;
	js = d.createElement(s);
	js.id = id;
	js.src = "https://connect.facebook.net/zh_TW/sdk.js";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));