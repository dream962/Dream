//如果没有cookie，则跳转到登录页
var a = document.cookie;
var tokenVal = $.cookie('token');
if(document.cookie.indexOf("token=") < 0 || $.cookie('token') == "null") {
	window.location.href = port + "/login.html";
}