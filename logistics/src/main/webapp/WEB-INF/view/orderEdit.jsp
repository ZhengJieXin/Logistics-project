<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>    
    
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE HTML>
<html>
<head>

<base href="<%=basePath%>">

<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<style type="text/css">
	.order_detail{
		background-color: durkgray;
		border: 1px solid black;
		border-right: 0; 
		height: 40px;
		line-height: 40px;
	}
	.order_detail input{
		height: 30px;
		text-align: center;
	}
	.order_add{
		border: 1px solid black;
	}
	#order_detail{
		text-align: center;
	}

</style>
<link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css" />
<link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript" src="lib/jquery/1.11.3/jquery.min.js"></script> 
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" method="post" action="${empty customer ? 'customer/insert.do' : 'customer/update.do'}" id="orderForm">
	<!-- 隐藏域 ，订单状态-->
	<input type="hidden" name="orderStatus" value="1">
	
	<div class="row cl">
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">业务员：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<select name="userId">
					<c:forEach items="${users}" var="obj">
						<option value="${obj.userId}">${obj.realname}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">客户：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<select name="customerId" id="customer">
					<c:forEach items="${customers}" var="obj">
						<!-- 
							html5 可以自定义 属性
							
							语法 ： data-xxx
							data-：固定语法
							xxx ：具体属性，见名知意
							如 ：
							data-customer_id : 客户id
							data-custoemr_name :客户名称
							
						 -->
					
						<option data-base-id="${obj.baseId}" value="${obj.customerId}">${obj.customerName}</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">到达区域：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<select id="interval" name="intervalId">
					<c:forEach items="${intervals}" var="obj">
						<option  value="${obj.baseId}">${obj.baseName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>

	
	<div class="row cl">
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">发货地址：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value="" required="required"  placeholder="" id="shippingAddress" name="shippingAddress">
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">发货人：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value="" required="required"   placeholder="" id="shippingName" name="shippingName">
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">发货联系电话：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value="" required="required"  placeholder="" id="shippingPhone" name="shippingPhone">
			</div>
		</div>
	</div>
	<div class="row cl">
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">付款方式：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<select name="paymentMethodId">
					<c:forEach items="${payments}" var="obj">
						<option value="${obj.baseId}">${obj.baseName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">货运方式：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<select name="freightMethodId">
					<c:forEach items="${freights}" var="obj">
						<option value="${obj.baseId}">${obj.baseName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
	<div class="row cl">
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">取件方式：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<select name="takeMethodId">
					<c:forEach items="${fetchTypes}" var="obj">
						<option value="${obj.baseId}">${obj.baseName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">物流公司：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value="xx物流公司"  disabled="disabled"  placeholder="" id="customerName" name="customerName">
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">物流单号：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value="243242343" disabled="disabled"   placeholder="" id="customerName" name="customerName">
			</div>
		</div>
	</div>
	<div class="row cl">
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">收件人：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value="张三" disabled="disabled"   placeholder="" id="customerName" name="customerName">
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">收货地址：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value="广州市天河区xxx" disabled="disabled"   placeholder="" id="customerName" name="customerName">
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">联系电话：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value="135xxxxx"  disabled="disabled"  placeholder="" id="customerName" name="customerName">
			</div>
		</div>
	</div>
	<div class="row cl">
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">取件地址：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value=""   placeholder="" id="customerName" name="takeAddress">
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">联系电话：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value=""   placeholder="" id="takePhone" name="takePhone">
			</div>
		</div>
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">取件联系人：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input type="text" class="input-text" value=""   placeholder="" id="takeName" name="takeName">
			</div>
		</div>
	</div>
	<div class="row cl">
		<div class="col-xs-4 col-sm-4">
			<label class="form-label col-xs-4 col-sm-4">订单备注：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<textarea name="orderRemark" cols="" rows="" class="textarea"  placeholder="" ></textarea>
			</div>
		</div>
	</div>
	
	
	
	<div  id="order_detail" class="row cl">
		<div>
			<div  class="col-xs-2 col-sm-2 order_detail">货物名称</div>
			<div  class="col-xs-2 col-sm-1 order_detail">数量</div>
			<div  class="col-xs-2 col-sm-1 order_detail">单位</div>
			<div  class="col-xs-2 col-sm-2 order_detail">单价</div>
			<div  class="col-xs-2 col-sm-2 order_detail">总价值</div>
			<div  class="col-xs-2 col-sm-3 order_detail">备注</div>
			<div  class="col-xs-2 col-sm-1 order_detail order_add">
				<span style="font: 30px;cursor: pointer;color: green" 
					class="glyphicon glyphicon-plus"
					onclick="add_goods_detail();"
					></span>
				</div>
		</div>
		
		<div id="goods_detail">
			<div  class="col-xs-2 col-sm-2 order_detail">
				<input type="text" name="orderDetails[][goodsName]">
			</div>
			<div  class="col-xs-2 col-sm-1 order_detail">
				<input type="text" size="3" onkeyup="operationTotlPrice(this);" name="orderDetails[][goodsNumber]">
			</div>
			<div  class="col-xs-2 col-sm-1 order_detail">
				<select name="orderDetails[][goodsUnit]">
					<c:forEach items="${units}" var="obj">
						<option value="${obj.baseId}">${obj.baseName}</option>
					</c:forEach>
				</select>
			</div>
			<div  class="col-xs-2 col-sm-2 order_detail">
				<input type="text"  onkeyup="operationTotlPrice(this);" name="orderDetails[][goodsUnitPrice]">
			
			</div>
			<div  class="col-xs-2 col-sm-2 order_detail">
				<input type="text" readonly="readonly" name="orderDetails[][goodsTotal]">
			</div>
			<div  class="col-xs-2 col-sm-3 order_detail">
				<input type="text" name="orderDetails[][goodsRemark]">
			</div>
			<div  class="col-xs-2 col-sm-1 order_detail order_add">
				<span style="font: 30px;cursor: pointer;color: red" 
				class="glyphicon glyphicon-remove"
				onclick="remove_goods_detail(this);"
				></span>
			</div>
		</div>
		
		
		
	</div>
	
	
	<div class="row cl">
		<div class="col-xs-12 col-sm-12 col-xs-offset-4 col-sm-offset-3">
			<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
		</div>
	</div>
	</form>
</article>

<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="lib/jquery/1.11.3/jquery.min.js"></script> 
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="lib/My97DatePicker/4.8/WdatePicker.js"></script> 
<script type="text/javascript" src="lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="lib/bootstrap-table/bootstrap-table.min.js"></script>
<script type="text/javascript" src="lib/bootstrap-table/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript" src="lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript" src="lib/jquery/jquery.serializejson.min.js"></script>
<script type="text/javascript">



//数量和单价文本框键盘按下弹起时候计算总计
function operationTotlPrice(obj){
	//1.获取最外层的 div
	var divBox = $(obj).parent().parent();
	console.log(divBox);
	//2.获取div中所有input 文本框
	var inputs = divBox.find("input");
	console.log(inputs);
	
	//3.获取数量和单价的文本框的值
	//原生 Js 的DOM
	//var goodsNumber = inputs[1].value;
	//Jquery 
	var goodsNumber = inputs.eq(1).val();
	
	var unitPrice = inputs[2].value;
	
	if(goodsNumber !="" && unitPrice !=""){
		//计算总价
		inputs[3].value = goodsNumber * unitPrice;
	}
	
}




$(function(){
	/* 
		为客户选择下拉框的绑定一个 onchange事假
		
		当客户变化是，自动选择客户对应的 区域
		
		思路
		 1，客户已经拥有区间的id
		 
		 2，当客户下拉框chanage时候
		 	2.1获取当前对应的客户的 区间id （base_id）
		 	2.2获取所有 区间管理下拉框的  <option>的value 值 （base_id）
		 3, 循环 第 2.2步骤的所有option，把option的 值和 2.1步的 值进行比较
		 	如果某一个值相等，说明当前客户对应就是此区间，让其<option> 选中即可
	*/
	$("#customer").change(function(){
		changeCustomerInterval();
	});
		 	
	changeCustomerInterval();
	
});

//根据客户的区间自动选中右边对应的区间
function changeCustomerInterval(){
	//获取客户的区间id（基础数据id）
	//获取当前选中的 option 的 base-id值
	var baseId =  $("#customer option:selected").data("base-id");
	
	//获取所有的区间 option并且循环
	var intervalsOptions = $("#interval option");
	//循环区间选项
	for(var i = 0;i<intervalsOptions.length;i++){
		var option = intervalsOptions[i];
		
		var intervalId = option.value;
		
		//将客户的区间id和循环区间id比较
		if(baseId == intervalId){
			//让其对应的节点选中
			option.selected = true;
		}
	}	
}




//添加货品详情
function add_goods_detail(){
	//克隆商品详情
	var goodsDetail = $("#goods_detail").clone();
	
	//清除表单的数据
	goodsDetail.find("input").val("");
	
	var order_detail = $("#order_detail");
	
	//将克隆的商品详情追加到订单明细div中
	order_detail.append(goodsDetail);
}
//删除商品详情
function remove_goods_detail(obj){
	$(obj).parent().parent().remove();
}



$(function(){
	$("#orderForm").validate({
	
		submitHandler:function(form){
			
			/* 
				提交表单数据思路
				
				将表单数据序列化成JSON数据，提交给后台
				
				订单基本信息都是 key-value
				把订单明细当做一个数组，数组中一个个表单明细对象
			*/
			//将表单序列化成JSON对象
			var obj = $('#orderForm').serializeJSON();
			//将JSON对象数据转换成字符串
			var jsonString = JSON.stringify(obj);
			console.log(typeof obj,obj);
			console.log(typeof jsonString,jsonString);
			
			
			//发送ajax请求
			 $.ajax({
				   type: "POST",
				   url: "order/insert.do",
				   contentType: "application/json; charset=utf-8",
				   data: jsonString,
				   success: function(data){
					   layer.msg(data.msg,{icon:data.code,time:2000},function(){
							//让父层页面重新刷新一下（重新加载一下）
							window.parent.refreshTable();
							//关闭当前弹出层
							parent.layer.closeAll();
						});
				   }
			}); 
		}
	});
});
</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>