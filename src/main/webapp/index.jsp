<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rooted — A Gardening Community</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <header>
        <h1>rooted<span>.garden</span></h1>
        <p>A little corner of the internet where things grow.</p>

        <nav>
            <a href="home">Home</a>
            <a href="forums">Discussions</a>
            <a href="journals">Garden Journals</a>
            <a href="members">Members</a>
            <c:choose>
                <c:when test="${not empty sessionScope.userId}">
                    <a href="profile?id=${sessionScope.userId}">My Profile</a>
                    <a href="logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="login">Login</a>
                    <a href="register">Register</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </header>

    <main>
        <section class="welcome">
            <h2>Welcome to Rooted, fellow gardener!</h2>
            <p>A place to ask questions, share what you're growing,
               and learn from fellow gardeners.</p>
            <p><em>New here? Pull up a chair and say hello.</em></p>
        </section>

        <section id="discussions">
            <h2>The Garden Gate — Discussion Boards</h2>

            <table>
                <thead>
                    <tr>
                        <th>Forum</th>
                        <th>Topics</th>
                        <th>About</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- Dynamic forum rows will be loaded here by HomeServlet --%>
                    <c:choose>
                        <c:when test="${not empty forums}">
                            <c:forEach var="forum" items="${forums}">
                                <tr>
                                    <td><a href="forums?id=${forum.id}">${forum.name}</a></td>
                                    <td>${forum.threadCount}</td>
                                    <td>${forum.description}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3">No forums yet — check back soon!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </section>

        <section id="journals">
            <h2>From the Garden Journals</h2>
            <c:choose>
                <c:when test="${not empty recentJournals}">
                    <c:forEach var="journal" items="${recentJournals}">
                        <article>
                            <h3><a href="journals?id=${journal.id}">${journal.title}</a></h3>
                            <p>Posted by ${journal.authorName} &middot; ${journal.createdAt}</p>
                            <p>${journal.excerpt}</p>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>No journal entries yet. <a href="journals/new">Be the first to write one!</a></p>
                </c:otherwise>
            </c:choose>
        </section>

        <section id="members">
            <h2>A note from the gardeners</h2>
            <p>Everyone starts somewhere. Be kind, stay curious,
               and help someone else's garden grow.</p>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
