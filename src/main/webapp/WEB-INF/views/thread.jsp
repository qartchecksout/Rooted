<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${thread.title}"/> — Rooted</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .post-card {
            border: 1px solid #d2cfbd;
            background: #fffdf9;
            margin-bottom: 18px;
            padding: 16px 20px;
        }
        .post-header {
            display: flex;
            justify-content: space-between;
            border-bottom: 1px dotted #a8ad91;
            padding-bottom: 8px;
            margin-bottom: 12px;
            font-size: 0.9rem;
            color: #656b5c;
        }
        .post-body {
            white-space: pre-wrap;
            line-height: 1.6;
            color: #383d30;
        }
        .op-badge {
            background: #617b50;
            color: #fff;
            padding: 2px 7px;
            font-size: 0.75rem;
            border-radius: 2px;
            margin-left: 6px;
        }
    </style>
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
        <p style="font-size: 0.95rem; margin-bottom: 14px;">
            <a href="${pageContext.request.contextPath}/home">&larr; Home</a> &middot;
            <c:if test="${not empty forum}">
                <a href="${pageContext.request.contextPath}/forums?id=${forum.id}"><c:out value="${forum.name}"/></a> &gt;
            </c:if>
            <strong><c:out value="${thread.title}"/></strong>
        </p>

        <h2 style="margin-top: 0;"><c:out value="${thread.title}"/></h2>

        <c:forEach var="post" items="${posts}" varStatus="status">
            <div class="post-card">
                <div class="post-header">
                    <div>
                        <strong>
                            <a href="${pageContext.request.contextPath}/profile?id=${post.userId}">
                                <c:out value="${post.authorName}"/>
                            </a>
                        </strong>
                        <c:if test="${status.first}">
                            <span class="op-badge">Original Post</span>
                        </c:if>
                    </div>
                    <div>
                        <c:out value="${post.createdAt}"/>
                    </div>
                </div>
                <div class="post-body"><c:out value="${post.body}"/></div>
            </div>
        </c:forEach>

        <%-- Reply Section --%>
        <section style="margin-top: 30px;">
            <h3>Join this Discussion</h3>
            <c:choose>
                <c:when test="${not empty sessionScope.userId}">
                    <form action="${pageContext.request.contextPath}/reply" method="POST">
                        <input type="hidden" name="threadId" value="${thread.id}">
                        <div class="form-group">
                            <label for="replyBody">Your Reply:</label>
                            <textarea id="replyBody" name="body" class="form-control" rows="5"
                                      placeholder="Share your experience, advice, or thoughts..." required></textarea>
                        </div>
                        <div>
                            <button type="submit" class="btn">Post Reply</button>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <p>
                        Want to reply to this thread?
                        <a href="${pageContext.request.contextPath}/login?redirect=/threads?id=${thread.id}"><strong>Log in</strong></a>
                        or <a href="${pageContext.request.contextPath}/register"><strong>Register</strong></a> to join the community.
                    </p>
                </c:otherwise>
            </c:choose>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
