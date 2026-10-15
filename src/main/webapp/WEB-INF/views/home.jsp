<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rooted — A Gardening Community</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header>
        <h1>rooted<span>.garden</span></h1>
        <p>A little corner of the internet where things grow.</p>

        <nav>
            <a href="${pageContext.request.contextPath}/home"><strong>Home</strong></a>
            <a href="${pageContext.request.contextPath}/forums">Discussions</a>
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
        <section class="welcome">
            <h2>Welcome to Rooted, fellow gardener!</h2>
            <p>A place to ask questions, share what you're growing, and learn from fellow gardeners.</p>
            <c:choose>
                <c:when test="${not empty sessionScope.userId}">
                    <p><em>Welcome back, <strong><c:out value="${sessionScope.username}"/></strong>! Grab a spade and check the boards below.</em></p>
                </c:when>
                <c:otherwise>
                    <p><em>New here? <a href="${pageContext.request.contextPath}/register">Pull up a chair and say hello</a>.</em></p>
                </c:otherwise>
            </c:choose>
        </section>

        <c:if test="${not empty error}">
            <div class="alert-error">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <section id="discussions">
            <h2>The Garden Gate — Discussion Boards</h2>

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

        <section id="journals">
            <div style="display: flex; justify-content: space-between; align-items: baseline;">
                <h2>From the Garden Journals</h2>
                <c:if test="${not empty sessionScope.userId}">
                    <a href="${pageContext.request.contextPath}/journals/new" class="btn" style="font-size: 0.9rem;">+ Write Entry</a>
                </c:if>
            </div>

            <c:choose>
                <c:when test="${not empty recentJournals}">
                    <c:forEach var="journal" items="${recentJournals}">
                        <article>
                            <h3>
                                <a href="${pageContext.request.contextPath}/journals?id=${journal.id}">
                                    <c:out value="${journal.title}"/>
                                </a>
                            </h3>
                            <p>
                                Posted by <strong><c:out value="${journal.authorName}"/></strong> &middot; 
                                <c:out value="${journal.createdAt}"/>
                            </p>
                            <p><c:out value="${journal.excerpt}"/></p>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>No journal entries yet. 
                        <a href="${pageContext.request.contextPath}/journals/new">Be the first to write one!</a>
                    </p>
                </c:otherwise>
            </c:choose>
        </section>

        <section id="members">
            <h2>A note from the gardeners</h2>
            <p>Everyone starts somewhere. Be kind, stay curious, and help someone else's garden grow.</p>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
