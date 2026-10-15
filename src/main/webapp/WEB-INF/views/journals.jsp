<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Garden Journals — Rooted</title>
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
        <div style="display: flex; justify-content: space-between; align-items: baseline; flex-wrap: wrap; gap: 10px;">
            <div>
                <h2 style="margin-bottom: 4px;">Garden Journals</h2>
                <p style="margin-top: 0; color: #555e4b;">
                    Personal notes, season logs, and photos from community plots.
                </p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/journals/new" class="btn">
                    + Write Journal Entry
                </a>
            </div>
        </div>

        <section style="margin-top: 18px;">
            <c:choose>
                <c:when test="${not empty journals}">
                    <c:forEach var="journal" items="${journals}">
                        <article>
                            <h3>
                                <a href="${pageContext.request.contextPath}/journals?id=${journal.id}">
                                    <c:out value="${journal.title}"/>
                                </a>
                            </h3>
                            <p>
                                Written by 
                                <strong>
                                    <a href="${pageContext.request.contextPath}/profile?id=${journal.userId}">
                                        <c:out value="${journal.authorName}"/>
                                    </a>
                                </strong> &middot; 
                                <c:out value="${journal.createdAt}"/>
                            </p>
                            <p style="color: #383d30;"><c:out value="${journal.excerpt}"/></p>
                            <p style="margin-top: 8px;">
                                <a href="${pageContext.request.contextPath}/journals?id=${journal.id}" style="font-size: 0.9rem;">
                                    Read complete entry &rarr;
                                </a>
                            </p>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="text-align: center; padding: 25px;">
                        No garden journals have been written yet.
                        <a href="${pageContext.request.contextPath}/journals/new">
                            Start the first garden journal!
                        </a>
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
