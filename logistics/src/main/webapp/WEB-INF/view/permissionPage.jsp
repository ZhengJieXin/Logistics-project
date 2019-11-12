<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib uri="http://shiro.apache.org/tags"   prefix="shiro"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
/* http://localhost:8080/logistics/ */
%>

<!DOCTYPE HTML>
<html>
<head>
<!-- 设置页面的 基本路径，页面所有资源引入和页面的跳转全部基于 base路径 -->
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,permission-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="Bookmark" href="/favicon.ico" >
<link rel="Shortcut Icon" href="/favicon.ico" />
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css" />
<link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="lib/bootstrap-table/bootstrap-table.min.css" />


<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>权限列表</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 权限管理 <span class="c-gray en">&gt;</span> 权限列表 </nav>
<div class="page-container">
	 <span id="toolbar" class="l">
	 <shiro:hasPermission name="permission:delete">
	 	<a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a> 
	 	</shiro:hasPermission>
	 	 <shiro:hasPermission name="permission:insert">
	 	<a href="javascript:;" onclick="permission_add()" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加权限</a>
	 	</shiro:hasPermission>
	 </span>
	<!-- 数据表格，以供BootStrap-table插件使用 -->
	<table   id="dataTable">
	
	</table>
</div>
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



<script type="text/javascript">


//在文档记载完毕以后执行匿名函数的代码

$(function(){
	$('#dataTable').bootstrapTable({
		url: 'permission/list.do',//ajax请求的url地址
		/*
			ajax请求以后回调函数的处理
			后台使用返回的PageInfo对象中的 结果 级的key是list，总条数是total
			而前台bootstrapTable插件需要的数据的key叫做rows ，总条数也是叫做total
			那么出现一个问题 : 总条数的key能对上，结果集对不上，就需要在ajax请求完成回调
			responseHandler 这个函数方法处理一下
			并且在自定义一个 json,rows做为key，返回json的 list作为值
				total：还是total
			这样才能满足 bootstrapTable插件数据的需要
		*/
		responseHandler: function(res) { 
			/*
				res: 后台分页对象PageInfo返回对应的json对象
				res.list : 结果集
				res.total : 总记录数
			*/
			var data =  {rows: res.list,total: res.total};
		
			return data;
		},
		pagination: true,
		toolbar: "#toolbar",//顶部显示的工具条（添加和批量删除的）
		contentType: 'application/x-www-form-urlencoded',//条件搜索的时候ajax请求给后台数据的数据类型（条件搜索post提交必须设置）
		search: true,//是否显示搜索框
		pageNumber: 1,//默认的页面 第一页
		pageSize: 10,//默认的每页条数
		pageList:[10,25,50,100],//每页能显示的条数
		sidePagination: "server",//是否是服务器分页，每次请求都是对应的10条数据，下一页发送ajax请求
		paginationHAlign: 'right', //底部分页条
		showToggle: false, //是否显示详细视图和列表视图的切换按钮
		cardView: false, //是否显示详细视图
		showColumns: false, //是否显示所有的列
		showRefresh: true, //是否显示刷新按钮
		columns: [ //表格显示数据对应的表头设置，
			{ checkbox: true},//是否显示前台的复选框（多选）
			/*
				每列数据的表头的设置
				filed:返回json数据对应数据的key
				title:表头要显示的名
			*/
			{field: 'permissionId',title: '编号'}, 
			{field: 'name',title: '权限名称'}, 
			{field: 'url',title: '权限地址'},
			{field: 'expression',title: '权限表达式'},
			{field: 'parentName',title: '父权限'},
			{field: 'type',title: '权限类型',formatter:menuTypeFormatter},
			//操作列的设置（删除，修改）
			/*
			formatter: 格式化这一行，回调一个函数
			*/
			{
				field:'permissionId',
				title:'操作',
				align:'center',
				formatter:operationFormatter
			}
		],
		/*发送请求的参数，
			params: bootstrapTable的插件内部参数对象包含如下参数
			limit, offset, search, sort
			limit：每页条数
			offset：每页的结束位置
			search:搜索框对应的值
			sort：排序
		*/
		 queryParams: function(params) { 
			//此方法在用户分页或者搜索的时候回自动发送ajax请求调用，并把对应的参数传递给后台
			console.log(params);
			
			var paramsData = {
					pageSize:params.limit, //每页条数
					//页码
					pageNum:params.offset / params.limit + 1,
					keyword:params.search}//关键字
			return paramsData;
			
		}, 
	})
	
});

/*
 * value : 对应field 属性的值
 	row ：当前行的json数据
 	index ：每一行的索引，从0开始
 	
 	此方法返回一个 html字符串，对应的解析渲染成 删除 和修改图标的html字符串即可
 */
function operationFormatter(value,row,index){
	 var html = "";
	 html +=" <shiro:hasPermission name='permission:update'>"
	 html +="<span onclick='permission_edit("+value+");' style='color: green;cursor: pointer;' class='glyphicon glyphicon-pencil'></span>&nbsp;&nbsp;&nbsp;";
	 html +="</shiro:hasPermission>"
	 html +="<shiro:hasPermission name='permission:delete'>"
	 html +="<span onclick='permission_del("+value+");' style='color: red;cursor: pointer;' class='glyphicon glyphicon-trash'></span>";
	 html +="</shiro:hasPermission>"
	 return html;
	
}


function menuTypeFormatter(value,row,index){
	var html = "<span style='color:green'>普通权限<span>";
	if(value == "menu"){
		html = "<span style='color:red'>菜单权限<span>";
	}
	return html;
}




/*
	参数解释：
	title	标题
	url		请求的url
	id		需要操作的数据id
	w		弹出层宽度（缺省调默认值）
	h		弹出层高度（缺省调默认值）
*/
/*权限-增加*/
function permission_add(){
	layer_show("新增权限","permission/edit.do",800,500);
}

/*权限-编辑*/
function permission_edit(permissionId){
	layer.open({
	  type:2,
	  title: '修改权限',
	  maxmin :true,
	  area: ['800px', '500px'],
	  content: 'permission/edit.do?permissionId='+permissionId
	});
}

/*权限-删除*/
function permission_del(permissionId){
	layer.confirm('确认要删除吗？',{icon: 3},function(index){
		
		//使用ajax 执行删除操作
		//$.get("admin/delete.do",{permissionId:permissionId},function(data){})
		$.get("permission/delete.do?permissionId="+permissionId,function(data){
			
			//弹出一个提示消息
			layer.msg(data.msg, {time: 2000, icon:data.code});
				//删除成功，刷新一下表格
				if(data.code == 1){
					refreshTable();
				}
				
			});
		
	});
}

//刷新表格的函数
function refreshTable(){
	$('#dataTable').bootstrapTable('refresh');
}



</script>
</body>
</html>