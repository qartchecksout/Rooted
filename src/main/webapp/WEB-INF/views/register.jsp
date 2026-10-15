<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register — Rooted</title>
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
            <a href="${pageContext.request.contextPath}/login">Login</a>
            <a href="${pageContext.request.contextPath}/register"><strong>Register</strong></a>
        </nav>
    </header>

    <main>
        <section>
            <h2>Join Our Garden</h2>
            <p>Pull up a chair and find your plot. Create an account to join discussions and start your journal.</p>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    <strong>Error:</strong> <c:out value="${error}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST" style="max-width: 500px; margin-top: 20px;">
                <div class="form-group">
                    <label for="username">Username (3–30 characters, letters & numbers):</label>
                    <input type="text" id="username" name="username" class="form-control"
                           value="<c:out value='${username}'/>" required autofocus>
                </div>

                <div class="form-group">
                    <label for="email">Email Address:</label>
                    <input type="email" id="email" name="email" class="form-control"
                           value="<c:out value='${email}'/>" required>
                </div>

                <div class="form-group">
                    <label for="password">Password (minimum 6 characters):</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="bio">A Little About Your Garden (Optional):</label>
                    <textarea id="bio" name="bio" class="form-control" rows="3"
                              placeholder="e.g. Balcony herbs and cherry tomatoes in sunny windowsills..."><c:out value='${bio}'/></textarea>
                </div>

                <div style="margin-top: 20px;">
                    <button type="submit" class="btn">Register Account</button>
                </div>
            </form>

            <p style="margin-top: 24px; font-size: 0.95rem;">
                Already have an account? <a href="${pageContext.request.contextPath}/login">Click here to log in</a>.
            </p>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
