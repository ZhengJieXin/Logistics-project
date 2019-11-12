<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
<!-- 设置页面的 基本路径，页面所有资源引入和页面的跳转全部基于 base路径 -->
<base href="<%=basePath%>">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,permission-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css" />
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" method="post" action="${empty permission ? 'permission/insert.do' : 'permission/update.do'}" id="permissionForm">
	<!-- 隐藏域，以供修改数据使用 -->
	<input type="hidden" name="permissionId" value="${permission.permissionId}">
	
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>权限名称：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<input type="text" class="input-text"  value="${permission.name}" placeholder="" id="name" name="name">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>权限url：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<input type="text" class="input-text" value="${permission.url}" placeholder="" id="url" name="url">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>权限表达式：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<input  class="input-text" autocomplete="off" value="${permission.expression}" placeholder="权限表达式" id="expression" name="expression">
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>权限类型：</label>
		<div class="formControls col-xs-8 col-sm-9 skin-minimal">
			<div class="radio-box">
				<input name="type" type="radio" id="sex-1" value="permission" ${permission.type eq 'permission' ?'checked':''}>
				<label for="sex-1">普通权限</label>
			</div>
			<div class="radio-box">
				<input type="radio" id="sex-2" name="type" value="menu" ${permission.type eq 'menu' ?'checked':''}>
				<label for="sex-2">菜单权限</label>
			</div>
		</div>
	</div>
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">父权限：</label>
		<div class="formControls col-xs-8 col-sm-9"> <span class="select-box" style="width:150px;">
			<select class="select" name="parentId" size="1">
				<option value="">请选择</option>
				<c:forEach items="${permissions}" var="p">
					<option value="${p.permissionId}" ${permission.parentId eq p.permissionId ? 'selected':''}>${p.name}</option>
				</c:forEach>
			</select>
			</span> </div>
	</div>
	<!-- <div class="row cl">
		<label class="form-label col-xs-4 col-sm-3">备注：</label>
		<div class="formControls col-xs-8 col-sm-9">
			<textarea name="" cols="" rows="" class="textarea"  placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="$.Huitextarealength(this,100)"></textarea>
			<p class="textarea-numberbar"><em class="textarea-length">0</em>/100</p>
		</div>
	</div> -->
	<div class="row cl">
		<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
			<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
		</div>
	</div>
	</form>
</article>

<!--_footer 作为公共模版分离出去--> 
<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript">
$(function(){
	/* 使用Jquery.validate 表单校验插件进行表单校验 */
	
	$("#permissionForm").validate({
		/* 规则 */
		rules:{
			name:{
				required:true,
				<c:if test='${empty permission}'>
				remote: { 
				    url: "permission/checkPermissionName.do",     //后台处理程序
				    type: "post",               //数据发送方式
				    dataType: "json",           //接受数据格式   
				    data: {                     //要传递的数据
				    	permissionname: function() {
				            return $("#name").val();
				        }
				    }
				}
				</c:if>
			},
			expression:{
				required:true,
			},
			type:{
				required:true
			}
		},
		/* 规则校验失败的错误提示信息 */
		messages:{
			name:{
				required:"权限名称不能为空",
				remote:"权限已经存在"
			},
			expression:{
				required:"权限表达式不能为空",
			},
			type:{
				required:"权限类型不能为空"
			}
		},
		/* 表单校验成功以后回调函数 */
		submitHandler:function(form){
			/* 
				form : 表单 ，是一个原生js的DOM对象
				
				提交表单思路，使用 Jquery的Ajax操作提交数据，提交成功以后，刷新 bootstrap-table 表格
				
				必须先将 form 原生的js的DOM对象转换成 Jquery对象才能使用Jquery方法
				
				原生DOM对象转Jquery对象
					form ---转--》 $(form)
				Jqeuery对象转原生DOM对象
					$(form) ----转-----》 $(form)[0]
			*/
			$(form).ajaxSubmit(function(data){
				//弹出一个提示消息
				layer.msg(data.msg, {time: 1000, icon:data.code},function(){
					//删除成功，刷新一下表格
					if(data.code == 1){
						//刷新父页面的表格
						parent.refreshTable();
						
						//关闭父页面弹出的模态框
						parent.layer.closeAll();
					}
				});
			})
		}
	});
	
	
	
});
</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>