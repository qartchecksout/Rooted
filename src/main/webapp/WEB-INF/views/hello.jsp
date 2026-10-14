<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hello - Rooted</title>
</head>
<body>
    <h1><c:out value="${greeting}"/></h1>

    <c:choose>
        <c:when test="${empty plants}">
            <p>No plants yet.</p>
        </c:when>
        <c:otherwise>
            <ul>
                <c:forEach var="plant" items="${plants}">
                    <li><c:out value="${plant}"/></li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
</body>
</html>