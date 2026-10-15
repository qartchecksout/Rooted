<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Write Journal Entry — Rooted</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
        <p style="font-size: 0.95rem; margin-bottom: 12px;">
            <a href="${pageContext.request.contextPath}/journals">&larr; Back to Journals</a>
        </p>

        <section>
            <h2>Write in Your Garden Journal</h2>
            <p>Document your seedlings, seasonal changes, harvests, and pest battles.</p>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    <strong>Error:</strong> <c:out value="${error}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/journals/new" method="POST" style="max-width: 700px; margin-top: 20px;">
                <div class="form-group">
                    <label for="title">Journal Entry Title:</label>
                    <input type="text" id="title" name="title" class="form-control"
                           placeholder="e.g. Week 4: First cherry tomato blossoms appearing"
                           value="<c:out value='${title}'/>" required autofocus>
                </div>

                <div class="form-group">
                    <label for="body">Your Observations & Notes:</label>
                    <textarea id="body" name="body" class="form-control" rows="12"
                              placeholder="Describe your weather, watering schedule, soil mixtures, growth progress..." required><c:out value='${body}'/></textarea>
                </div>

                <div style="margin-top: 20px;">
                    <button type="submit" class="btn">Publish to Community Journals</button>
                    <a href="${pageContext.request.contextPath}/journals" style="margin-left: 14px; font-size: 0.95rem;">Cancel</a>
                </div>
            </form>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
