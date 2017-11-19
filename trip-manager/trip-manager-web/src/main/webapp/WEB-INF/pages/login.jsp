<%--
  Created by IntelliJ IDEA.
  User: Kim
  Date: 2017/11/18
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <title>大学生旅行 - 后台管理</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <style type="text/css">
        .container{
            margin-top: 30px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <h1>管理员登录</h1>
            <c:url var="loginAction" value="/api/manager/login" />
            <form method="post" action="${loginAction}">
                <div class="form-group">
                    <label for="username">用户名：</label>
                    <input class="form-control" type="text" name="username" placeholder="用户名" id="username" />
                </div>
                <div class="form-group">
                    <label for="password">密码：</label>
                    <input class="form-control" type="password" name="password" placeholder="密码" id="password" />
                </div>
                <input class="form-control btn btn-primary" type="button" id="submitBtn" value="登录" />
            </form>
            <p class="text-danger" id="errorMsg"></p>
        </div>
    </div>
</div>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script>
    // 登录
    $(function () {

        // 登录方法
        function login() {
            var username = $('#username').val();
            var password = $('#password').val();


            $.ajax({
                url: "https://journey.xiaokuango.com/api/manager/login",
                method: "POST",
                data: {
                    username: username,
                    password: password
                },
                success: function (data) {
                    if (data.status === 200) {
                        // 登录成功
                        $.cookie('manager_token',data.data.manager_token,{domain: 'journey.xiaokuango.com',path:'/'})
                        window.location.href = 'https://journey.xiaokuango.com/manager/journey/list'
                    } else {
                        // 登录失败
                        $('#errorMsg').text(data.message)
                    }
                }
            })
        }

        // 绑定提交按钮事件
        function bindSubmitEvent() {
            var submitBtn = $('#submitBtn')
            var btnEvt = $._data(submitBtn, 'events');
            if (!btnEvt || !btnEvt['click']) {
                submitBtn.on('click', login);
            }
        }

        $(document).ready(function () {
            bindSubmitEvent()
        })
    });
</script>
</body>
</html>
