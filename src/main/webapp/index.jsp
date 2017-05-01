<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/5/1 0001
  Time: 9:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>千度一下，你就知道</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
</head>

<body>
<div>
    <form action="/index.do" method="post">
        <input type="text" name="keywords" maxlength="50"/>
        <input type="submit" value="搜索一下"/>
    </form>
</div>
<div>
    <c:if test="${! empty results}">
        <p>百度为您找到相关结果约${totalHits}个</p>
    </c:if>

    <br>
    <c:forEach items="${results}" var="hb">
        <a href="${hb.url}">${hb.title}</a>
        <br>
        <p>${hb.content}</p>
        ${hb.url}
        <br>

    </c:forEach>
</div>
</body>
</html>

