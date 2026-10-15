<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${forum.name}"/> — Rooted</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header>
        <h1>rooted<span>.garden</span></h1>
        <p>A little corner of the internet where things grow.</p>

        <nav>
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/forums"><strong>Discussions</strong></a>
            <a href="${pageContext.request.contextPath}/journals">Garden Journals</a>
            <a href="${pageContext.request.contextPath}/members">Members</a>
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
        <p style="font-size: 0.95rem; margin-bottom: 12px;">
            <a href="${pageContext.request.contextPath}/home">&larr; Return to Home</a> &middot;
            <a href="${pageContext.request.contextPath}/forums">All Boards</a> &gt; 
            <strong><c:out value="${forum.name}"/></strong>
        </p>

        <section>
            <div style="display: flex; justify-content: space-between; align-items: baseline; flex-wrap: wrap; gap: 10px;">
                <div>
                    <h2 style="margin-bottom: 4px;"><c:out value="${forum.name}"/></h2>
                    <p style="margin-top: 0; color: #555e4b;"><c:out value="${forum.description}"/></p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/threads/new?forumId=${forum.id}" class="btn">
                        + Start New Discussion
                    </a>
                </div>
            </div>

            <table style="margin-top: 18px;">
                <thead>
                    <tr>
                        <th style="width: 50%;">Topic</th>
                        <th style="width: 20%;">Started By</th>
                        <th style="width: 12%;">Replies</th>
                        <th style="width: 18%;">Created</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty threads}">
                            <c:forEach var="thread" items="${threads}">
                                <tr>
                                    <td>
                                        <strong>
                                            <a href="${pageContext.request.contextPath}/threads?id=${thread.id}">
                                                <c:out value="${thread.title}"/>
                                            </a>
                                        </strong>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/profile?id=${thread.userId}">
                                            <c:out value="${thread.authorName}"/>
                                        </a>
                                    </td>
                                    <td>
                                        <%-- Replies is total posts minus the original post --%>
                                        <c:out value="${thread.postCount > 1 ? thread.postCount - 1 : 0}"/>
                                    </td>
                                    <td style="font-size: 0.9rem; color: #666;">
                                        <c:out value="${thread.createdAt}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" style="text-align: center; padding: 25px;">
                                    No discussions started in this board yet. 
                                    <a href="${pageContext.request.contextPath}/threads/new?forumId=${forum.id}">
                                        Be the first to plant a seed!
                                    </a>
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
