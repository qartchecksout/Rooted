<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Discussion Boards — Rooted</title>
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
                    <a href="${pageContext.request.contextPath}/profile?id=${sessionScope.userId}">My Profile (<c:out value="${sessionScope.username}"/>)</a>
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
        <div style="display: flex; justify-content: space-between; align-items: baseline; flex-wrap: wrap; gap: 10px;">
            <div>
                <h2 style="margin-bottom: 4px;">The Garden Gate — All Discussion Boards</h2>
                <p style="margin-top: 0; color: #555e4b;">Pick a board to browse topics or start a new conversation.</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/threads/new" class="btn">+ Start New Discussion</a>
            </div>
        </div>

        <section style="margin-top: 18px;">
            <h3>Boards</h3>
            <table>
                <thead>
                    <tr>
                        <th style="width: 35%;">Forum</th>
                        <th style="width: 15%;">Topics</th>
                        <th style="width: 50%;">About</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty forums}">
                            <c:forEach var="forum" items="${forums}">
                                <tr>
                                    <td>
                                        <strong>
                                            <a href="${pageContext.request.contextPath}/forums?id=${forum.id}">
                                                <c:out value="${forum.name}"/>
                                            </a>
                                        </strong>
                                    </td>
                                    <td><c:out value="${forum.threadCount}"/></td>
                                    <td><c:out value="${forum.description}"/></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3">No discussion boards available yet.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </section>

        <section>
            <h3>Recent Discussions Across All Boards</h3>
            <table>
                <thead>
                    <tr>
                        <th style="width: 42%;">Topic</th>
                        <th style="width: 22%;">Board</th>
                        <th style="width: 16%;">Started By</th>
                        <th style="width: 10%;">Replies</th>
                        <th style="width: 10%;">Created</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty recentThreads}">
                            <c:forEach var="thread" items="${recentThreads}">
                                <tr>
                                    <td>
                                        <strong>
                                            <a href="${pageContext.request.contextPath}/threads?id=${thread.id}">
                                                <c:out value="${thread.title}"/>
                                            </a>
                                        </strong>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/forums?id=${thread.forumId}">
                                            <c:out value="${thread.forumName}"/>
                                        </a>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/profile?id=${thread.userId}">
                                            <c:out value="${thread.authorName}"/>
                                        </a>
                                    </td>
                                    <td><c:out value="${thread.postCount > 1 ? thread.postCount - 1 : 0}"/></td>
                                    <td style="font-size: 0.85rem; color: #666;"><c:out value="${thread.createdAt}"/></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 20px;">
                                    No discussions started yet.
                                    <a href="${pageContext.request.contextPath}/threads/new">Start the first one!</a>
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
