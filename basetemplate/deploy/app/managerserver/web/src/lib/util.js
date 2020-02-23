
Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

var history = []


var util = (function (u) {

    u.sortMessage = function (a, b) {
        var t1 = Date.parse(a.createDate).getTime()
        var t2 = Date.parse(b.createDate).getTime()
        return t2-t1
    }
    
    u.sortTask = function (a, b) {
        var s1 = a.status*1000
        var s2 = b.status*1000
        //var t1 = new Date(a.planStartTime).getTime()/(100000*1000000)
        //var t2 = new Date(b.planStartTime).getTime()/(100000*1000000)
        var t1 = Date.parse(a.planStartTime).getTime()/(100000*1000000)
        var t2 = Date.parse(b.planStartTime).getTime()/(100000*1000000)
        
        //console.log("sort:", t1, t2)

        if(a.status == 4){
            s1 = 1000000
        }

        if(b.status == 4){
            s2 = 1000000
        }

        //
        s1 += t1
        s2 += t2

        return s2 - s1
    }

    u.sortTask2 = function (a, b) {
        var s1 = a.status*1000000
        var s2 = b.status*1000000

        var t1 = Date.parse(a.planStartTime).getTime()/(100000*1000000)
        var t2 = Date.parse(b.planStartTime).getTime()/(100000*1000000)

        if(a.status == 4){
            s1 = 10000
        }

        if(b.status == 4){
            s2 = 10000
        }

        if(a.status == 3){
            s1 = 1000
        }

        if(b.status == 3){
            s2 = 1000
        }

        s1 += t1
        s2 += t2

        return s2 - s1
    }
    
    //  项目中心
    // 未开始、进行中>已完成
    // 同等级下，按照计划开始时间排序，计划开始时间越靠后，排序越靠前
    u.sortTask3 = function (a, b) {
        var s1 = a.status*1000000
        var s2 = b.status*1000000

        if(a.status >= 7){
            s1 = a.status * 10000
        }

        if(b.status >= 7){
            s2 = b.status * 10000
        }

        var t1 = Date.parse(a.planStartTime).getTime()/(100000*1000000)
        var t2 = Date.parse(b.planStartTime).getTime()/(100000*1000000)

        s1 += t1
        s2 += t2

        return s2 - s1
    }

    //  任务管理
    //  按照计划开始时间的先后顺序排列，计划开始时间越靠前
    u.sortTask4 = function (a, b) {
        var s1 = a.status*1000000
        var s2 = b.status*1000000

        var t1 = Date.parse(a.planStartTime).getTime()/(100000*1000000)
        var t2 = Date.parse(b.planStartTime).getTime()/(100000*1000000)

        s1 += t1
        s2 += t2

        return s2 - s1
    }
    
    //
    //   按照计划开始时间的先后顺序排列，计划开始时间越靠后
    u.sortTask5 = function (a, b) {
        var s1 = a.status*1000000
        var s2 = b.status*1000000

        var t1 = Date.parse(a.planStartTime).getTime()/(100000*1000000)
        var t2 = Date.parse(b.planStartTime).getTime()/(100000*1000000)

        s1 += t1
        s2 += t2

        return s1 - s2
    }

    u.showAlert = function (msg, onComfirm) {
        bootbox.confirm({
            size:"small",
            message:msg,
            callback:function (result) {
                if(result){
                    onComfirm()
                }
            },
            buttons:{cancel:{label:"取消"},confirm:{label:"确定"}}
        })
    },
    
    u.createChecks = function () {
        var val = {}
        
        var checks = []

        val.check = function (id) {
            for(var i=0; i<checks.length; ++i){
                if(checks[i] == id){
                    return;
                }
            }
            checks.push(id)
        }

        val.clear = function () {
            this.checks.splice(0, this.checks.length)
        }

        val.uncheck = function (id) {
            for(var i=0; i<checks.length; ++i){
                if(checks[i] == id){
                    checks.splice(i, 1)
                    return;
                }
            }
        }

        val.checks = checks

        return val
    }

    u.todayDateStr = function () {
        var d = new Date()
        return d.format("yyyy-MM-dd")
    }

    //show tip
    u.notify = function(title, text, type, delay_) {
        var d = 2000;
        if(delay_ > 0){
            d = delay_;
        }
        //PNotify.removeAll();
        $(function() {
            new PNotify({
                title: title,
                text: text,
                type: type || 'info',
                opacity: 1,
                delay: d,
                addclass:"small-notify",
                context: $(document.body),
                nonblock: {
                    nonblock: true,
                    nonblock_opacity: .2
                }
            });
        });
    }

    return u;

}(window.util || {}))
