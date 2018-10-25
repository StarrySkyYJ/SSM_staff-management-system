<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<<jsp:forward page="${APP_PATH }/json"></jsp:forward>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% pageContext.setAttribute("APP_PATH",request.getContextPath()); %>
<!-- web路径
	不以/开始  的相对路径，找资源以当前资源的路径为基准，经常容易出问题。
	以/开始  的相对路径 ，找资源 以服务器的路径为标准(http://localhost:3306);需要加上项目名
					http://localhost:3306/crud	
 -->
 
<!-- 引入jquery -->
<script 
	src="${APP_PATH }/static/js/jquery-1.12.4.min.js"></script>
	
<!-- 引入样式  -->
<link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script
	src="${APP_PATH }static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

</head>
<body>
	<h1>welcome src/index.jsp</h1>
	<!-- 搭建显示页面 -->
	<div class="container">
		<!-- 标题 -->
		<div class="row">
			<div class="col_md_12">
				<h1>SSM-CRUD</h1>
			</div>
		</div>
		<!-- 按钮 -->
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<button>新增</button>
				<button>删除</button>
			</div>
		</div>
		<!-- 显示表格数据 -->
		<div class="row"></div>
		<!-- 显示分页信息 -->
		<div class="row"></div>
	</div>

</body>
</html>