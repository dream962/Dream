
window.ajax = function(url, params, back) {
	
    params = params || {}
    params.token = $.cookie("token")

    var data = "params=" + JSON.stringify(params);

    $.ajax({
        type: 'POST',
        url: url,
        data: data,
        dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
        success: function (data) {
            if(back){
                if(data.code == 0){
                    data.msg = eval("("+data.msg+")")
                }else if(data.code == -1){
                    
                }else if(data.code == -2){
                }
                back(data)
            }
        },
        error : function (data) {
            if(data.readyState == 0 && data.status == 0){
                if($.cookie("token")){
                }
            }else if(data.status == 404){
                /*window.location.href = "404.html"*/
            }else if(back){
                back(null)
            }
        }
    });
}

