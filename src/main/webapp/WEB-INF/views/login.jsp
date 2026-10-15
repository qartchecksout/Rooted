<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — Rooted</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header>
        <h1>rooted<span>.garden</span></h1>
        <p>A little corner of the internet where things grow.</p>

        <nav>
            <a href="${pageContext.request.contextPath}/">Home</a>
            <a href="${pageContext.request.contextPath}/forums">Discussions</a>
            <a href="${pageContext.request.contextPath}/journals">Garden Journals</a>
            <a href="${pageContext.request.contextPath}/members">Members</a>
            <a href="${pageContext.request.contextPath}/login"><strong>Login</strong></a>
            <a href="${pageContext.request.contextPath}/register">Register</a>
        </nav>
    </header>

    <main>
        <section>
            <h2>Welcome Back, Gardener</h2>
            <p>Enter your credentials to manage your journals and post to the discussion boards.</p>

            <c:if test="${param.loggedOut eq 'true'}">
                <div class="alert-success">
                    You have been safely logged out. See you next planting season!
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    <strong>Error:</strong> <c:out value="${error}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="POST" style="max-width: 450px; margin-top: 20px;">
                <c:if test="${not empty redirect}">
                    <input type="hidden" name="redirect" value="<c:out value='${redirect}'/>">
                </c:if>

                <div class="form-group">
                    <label for="usernameOrEmail">Username or Email Address:</label>
                    <input type="text" id="usernameOrEmail" name="usernameOrEmail" class="form-control"
                           value="<c:out value='${usernameOrEmail}'/>" required autofocus>
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                </div>

                <div style="margin-top: 20px;">
                    <button type="submit" class="btn">Log In</button>
                </div>
            </form>

            <p style="margin-top: 24px; font-size: 0.95rem;">
                Don't have an account yet? <a href="${pageContext.request.contextPath}/register">Register here</a> to join our community.
            </p>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
