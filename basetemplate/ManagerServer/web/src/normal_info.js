(function(){
	var showNormalInfo = function () {
		var Page = React.createClass({
			onClickBuy : function () {
				var params = {snatchKeyID:$("#snatchID").val(), buyCount:$("#snatchCount").val()}
	            ajax("snatchHallParticipate", params, function (data) {
	                console.log(data)
	            })
			},
			render : function () {
				return(
					<form className="form-horizontal" role="form">
						<div className="form-group">
							<label htmlFor="snatchID" className="col-sm-2 control-label">snatchID</label>
							<div className="col-sm-4">
							  	<input type="text" className="form-control" id="snatchID" placeholder="夺宝ID"/>
							</div>
							</div>
							<div className="form-group">
							<label htmlFor="snatchCount" className="col-sm-2 control-label">count</label>
							<div className="col-sm-4">
							  	<input type="text" className="form-control" id="snatchCount" placeholder="数量"/>
							</div>
						</div>
						<div className="form-group">
							<div className="col-sm-offset-2 col-sm-10">
							  	<button id="participate" onClick={this.onClickBuy} type="submit" className="btn btn-default">参与</button>
							</div>
						</div>
					</form>
					)
			}
		})
		ReactDOM.render(<Page /> , document.getElementById('inner-content'))
	}

	$(document).ready(function () {
        $("#normal_info").click(function (event) {
            showNormalInfo()
            return false
        })
    })
}())