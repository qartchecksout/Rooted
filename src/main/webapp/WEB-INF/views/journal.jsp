<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${journal.title}"/> — Rooted</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .journal-container {
            background: #fffdf9;
            border: 1px solid #d2cfbd;
            padding: 28px 32px;
            margin-top: 16px;
        }
        .journal-meta {
            border-bottom: 1px dotted #a8ad91;
            padding-bottom: 12px;
            margin-bottom: 20px;
            color: #656b5c;
            font-size: 0.95rem;
        }
        .journal-body {
            white-space: pre-wrap;
            line-height: 1.8;
            font-size: 1.05rem;
            color: #2e3326;
        }
    </style>
</head>
<body>

    <header>
        <h1>rooted<span>.garden</span></h1>
        <p>A little corner of the internet where things grow.</p>

        <nav>
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/forums">Discussions</a>
            <a href="${pageContext.request.contextPath}/journals"><strong>Garden Journals</strong></a>
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
            <a href="${pageContext.request.contextPath}/journals">&larr; Back to all Journals</a>
        </p>

        <div class="journal-container">
            <h2 style="margin-top: 0; font-size: 2rem; border-bottom: none; padding-bottom: 0;">
                <c:out value="${journal.title}"/>
            </h2>

            <div class="journal-meta">
                Written by 
                <strong>
                    <a href="${pageContext.request.contextPath}/profile?id=${journal.userId}">
                        <c:out value="${journal.authorName}"/>
                    </a>
                </strong> &middot; 
                <c:out value="${journal.createdAt}"/>
            </div>

            <div class="journal-body"><c:out value="${journal.body}"/></div>
        </div>

        <p style="margin-top: 24px; font-size: 0.95rem;">
            <a href="${pageContext.request.contextPath}/journals">&larr; Return to Garden Journals</a>
        </p>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
