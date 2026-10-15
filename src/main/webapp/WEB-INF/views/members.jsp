<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Community Members — Rooted</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header>
        <h1>rooted<span>.garden</span></h1>
        <p>A little corner of the internet where things grow.</p>

        <nav>
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/forums">Discussions</a>
            <a href="${pageContext.request.contextPath}/journals">Garden Journals</a>
            <a href="${pageContext.request.contextPath}/members"><strong>Members</strong></a>
            <c:choose>
                <c:when test="${not empty sessionScope.userId}">
                    <a href="${pageContext.request.contextPath}/profile?id=${sessionScope.userId}">My Profile</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Login</a>
                    <a href="${pageContext.request.contextPath}/register">Register</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </header>

    <main>
        <h2>Gardeners Directory</h2>
        <p>Meet the gardeners who make up the Rooted community.</p>

        <section style="margin-top: 20px;">
            <table>
                <thead>
                    <tr>
                        <th style="width: 25%;">Gardener</th>
                        <th style="width: 20%;">Member Since</th>
                        <th style="width: 55%;">Garden Notes / Bio</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty members}">
                            <c:forEach var="member" items="${members}">
                                <tr>
                                    <td>
                                        <strong>
                                            <a href="${pageContext.request.contextPath}/profile?id=${member.id}">
                                                <c:out value="${member.username}"/>
                                            </a>
                                        </strong>
                                    </td>
                                    <td style="font-size: 0.9rem; color: #555e4b;">
                                        <c:out value="${member.createdAt}"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty member.bio}">
                                                <c:out value="${member.bio}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <em style="color: #888;">Tending to plants quietly...</em>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3" style="text-align: center; padding: 20px;">
                                    No members registered yet. 
                                    <a href="${pageContext.request.contextPath}/register">Be the first to join!</a>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
