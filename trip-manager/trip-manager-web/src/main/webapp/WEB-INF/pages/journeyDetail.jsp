<%--
  Created by IntelliJ IDEA.
  User: Kim
  Date: 2017/11/8
  Time: 21:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="serverUrl" value="https://journey.xiaokuango.com/" />
<c:url var="baseUrl" value="https://journey.xiaokuango.com/manager" />
<html>
<head>
    <meta charset="utf-8">
    <title>游记详情页</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/layui/css/layui.css">

    <style>
        .layui-nav{
            border-radius: 0;
        }
        h1{
            font-size: 36px;
        }
        h2{
            font-size: 20px;
        }
    </style>

</head>
<body>

    <!--header start-->
    <ul class="layui-nav" lay-filter="">
        <li class="layui-nav-item"><a href="${baseUrl}">控制台</a></li>
    </ul>
    <!--end header-->
    <div class="layui-container">
        <div class="layui-row">
            <div id="tripBox" class="layui-col-md9"></div>
        </div>
        <div class="layui-row">
            <ul class="layui-timeline" id="timeline"></ul>
        </div>
    </div>


    <c:url var="layui" value="${baseUrl}/layui/layui.js" />
    <script src="${layui}"></script>
    <c:url var="jqueryResource" value="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js" />
    <script src="${jqueryResource}"></script>
    <script>
        $(function () {
            $.ajax({
                url: '/api/manager/journey/view?journeyId=${journeyId}',
                method: 'GET',
                success: function (data) {
                    if(data.status == 200) {
                        callback(data.data)
                    } else {
                        callback({})
                    }

                }

            })

            function callback(data) {
                console.log(data)
                let trip = data.element

                let html = ''
                html += '<h1>游记标题：'+ trip.name +'</h1>'
                html += '<h2>游记天数：'+ trip.dayNum +'</h2>'
                html += '<img src="${serverUrl}'+ trip.img +'">'
                $('#tripBox').html(html);

                let days = data.subordinates
                console.log(days)
                if (days !== undefined && days.length > 0) {
                    let result = ''
                    for (let i = 0; i < days.length; i++) {
                        result += '<li class="layui-timeline-item">'
                        result += '<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'
                        result += '<div class="layui-timeline-content layui-text">'
                        result += '<h3 class="layui-timeline-title">第'+ (i+1) +'天</h3>'

                        let sites = days[i].subordinates
                        console.log('sites')

                        if (sites !== null && sites !== undefined && sites.length > 0) {
                            console.log(sites)
                            for (let j = 0; j < sites.length; j++) {
                                let site = sites[j].element
                                result += '<ul class="layui-timeline">'
                                result += '<li class="layui-timeline-item">'
                                result += '<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'
                                result += '<div class="layui-timeline-content layui-text">'
                                result += '<h3 class="layui-timeline-title">'+ site.name +'</h3>'
                                result += '<p>'
                                result += '<img src="${serverUrl}'+ site.img +'" />'
                                if (site.time != -1) {
                                    result += '<br>游玩时长： '+ site.time +'小时'
                                }
                                result += '<br>游玩攻略：' + site.tips
                                result += '</p>'
                                result += '</div>'
                                result += '</li>'
                                result += '</ul>'
                            }
                        }
                        result += '</div>'
                        result += '</li>'
                    }
                    $('#timeline').html(result)
                }

            }
        });
    </script>
</body>
</html>
