<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile — Rooted</title>
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
            <a href="${pageContext.request.contextPath}/members">Members</a>
            <a href="${pageContext.request.contextPath}/profile?id=${sessionScope.userId}"><strong>My Profile</strong></a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </header>

    <main>
        <p style="font-size: 0.95rem; margin-bottom: 12px;">
            <a href="${pageContext.request.contextPath}/profile?id=${user.id}">&larr; Return to My Profile</a>
        </p>

        <section>
            <h2>Edit Your Garden Bio</h2>
            <p>Tell fellow gardeners about your garden plot, what you like to grow, or your hardiness zone.</p>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    <strong>Error:</strong> <c:out value="${error}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/profile/edit" method="POST" style="max-width: 600px; margin-top: 20px;">
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" class="form-control" value="<c:out value='${user.username}'/>" disabled
                           style="background: #ece8dc; color: #666;">
                    <small style="color: #666;">Usernames cannot be changed.</small>
                </div>

                <div class="form-group">
                    <label for="email">Email Address:</label>
                    <input type="email" id="email" class="form-control" value="<c:out value='${user.email}'/>" disabled
                           style="background: #ece8dc; color: #666;">
                    <small style="color: #666;">Registered account email.</small>
                </div>

                <div class="form-group">
                    <label for="bio">Garden Bio / Plot Notes:</label>
                    <textarea id="bio" name="bio" class="form-control" rows="6"
                              placeholder="e.g. Raised bed gardening in zone 7b, focusing on heirloom tomatoes, herbs, and pollinator flowers..."><c:out value='${user.bio}'/></textarea>
                </div>

                <div style="margin-top: 20px;">
                    <button type="submit" class="btn">Save Profile Changes</button>
                    <a href="${pageContext.request.contextPath}/profile?id=${user.id}" style="margin-left: 14px; font-size: 0.95rem;">Cancel</a>
                </div>
            </form>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
