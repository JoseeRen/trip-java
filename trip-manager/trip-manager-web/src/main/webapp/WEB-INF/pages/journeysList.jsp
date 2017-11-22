<%--
  Created by IntelliJ IDEA.
  User: Kim
  Date: 2017/11/8
  Time: 17:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<c:url var="serverUrl" value="https://journey.xiaokuango.com/" />
<c:url var="baseUrl" value="https://journey.xiaokuango.com/manager" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>游记列表页</title>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/layui/css/layui.css">
    <style>
        .layui-nav{
            border-radius: 0;
        }
        .content .layui-table{
            margin-top: 0;
        }
        .layui-table-view{
            margin-top: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body>
    <!--header start-->
    <ul class="layui-nav" lay-filter="">
        <li class="layui-nav-item"><a href="${baseUrl}">控制台</a></li>
    </ul>
    <!--end header-->

    <div class="content">
        <table class="layui-table" lay-data="{height:660, url:'/api/manager/journey/list', page:true, id:'test'}" lay-filter="test">
            <thead>
            <tr>
                <th lay-data="{field:'id', width:80, sort: true}">ID</th>
                <th lay-data="{field:'name', width:180}">游记标题</th>
                <th lay-data="{field:'type', width:80, templet: '#typeTpl'}">类型</th>
                <th lay-data="{field:'dayNum', width:90, sort: true}">天数</th>
                <th lay-data="{field:'img', width:100, templet: '#imgTpl'}">封面</th>
                <th lay-data="{field:'imgThumb', width:100, templet: '#imgThumbTpl'}">封面缩略</th>
                <th lay-data="{field:'createTime', width:180, sort: true, templet: '#timeTpl'}">创建时间</th>
                <th lay-data="{field:'collegeCid', width:120, sort: true}">出发学校</th>
                <th lay-data="{field:'status', width:120, sort: true, templet: '#statusTpl'}">审核状态</th>
                <th lay-data="{fixed: 'right', width:220, align:'center', toolbar: '#barDemo'}">操作</th>
            </tr>
            </thead>
        </table>
    </div>
    <c:url var="layui" value="${baseUrl}/layui/layui.js" />
    <script src="${layui}"></script>
    <c:url var="jqueryResource" value="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js" />
    <script src="${jqueryResource}"></script>
    <script>
        // layui设置
        layui.use(['table', 'element'], function(){
            let table = layui.table;

            //监听工具条
            table.on('tool(test)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值
                var tr = obj.tr; //获得当前行 tr 的DOM对象

                if(layEvent === 'check'){ // 提交审核
                    //do somehing
                    layer.confirm('确认审核操作？', function(index){
                        console.log(index)
                        //向服务端发送更新指令
                        $.ajax({
                            url : '/api/manager/journey/check?journeyId='+data.id,
                            success: function (res) {
                                console.log(res.status)
                                window.location.reload();
                            },
                            error: function () {
                                console.log("err")
                            }
                        });
                        layer.close(index)

                    });
                }
            });
        });

    </script>

    <script type="text/html" id="statusTpl">
        {{#  if (d.status == 1 || d.status == 2){ }}
        已通过
        {{#  } else { }}
        未通过
        {{#  } }}
    </script>

    <script type="text/html" id="typeTpl">
        {{#  if(d.type == '0'){ }}
        短途游
        {{#  } else { }}
        周边游
        {{#  } }}
    </script>

    <script type="text/html" id="imgTpl">
        {{#  if (d.img == undefined || d.img == 'default' || d.img == '') { }}
        无封面图片
        {{# } else { }}
        <a target="_blank" href="${serverUrl}{{d.img}}" class="layui-table-link">查看图片</a>
        {{# } }}
    </script>

    <script type="text/html" id="imgThumbTpl">
        {{#  if (d.imgThumb == undefined || d.imgThumb == 'default' || d.imgThumb == '') { }}
        无封面图片
        {{# } else { }}
        <a target="_blank" href="${serverUrl}{{d.imgThumb}}" class="layui-table-link">查看图片</a>
        {{# } }}
    </script>

    <script type="text/html" id="timeTpl">
        {{# var createTime = d.createTime;}}
        {{# var date =  new Date(createTime); }}
        {{# var y = 1900+date.getYear(); }}
        {{# var m = "0"+(date.getMonth()+1); }}
        {{# var d = "0"+date.getDate(); }}
        {{# var h = "0"+date.getHours();}}
        {{# var M = "0"+date.getMinutes()}}
        {{# var time = y+"/"+m.substring(m.length-2,m.length)+"/"+d.substring(d.length-2,d.length) + "&nbsp;&nbsp;" + h.substring(h.length-2, h.length) + ":" + M.substring(M.length-2, M.length);}}
        {{time}}
    </script>

    <script type="text/html" id="barDemo">

        <!-- 这里同样支持 laytpl 语法，如： -->
        {{#  if(d.status == 2 || d.status == 1){ }}
        <a class="layui-btn layui-btn-mini layui-btn-danger" lay-event="check">取消通过</a>
        {{#  } else { }}
        <a class="layui-btn layui-btn-mini" lay-event="check">通过审核</a>
        {{# } }}
        <a class="layui-btn layui-btn-mini" lay-event="view" href="${serverUrl}manager/journey/detail?journeyId={{d.id}}">查看详情</a>
    </script>

</body>
</html>
