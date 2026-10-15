<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${profileUser.username}"/>'s Garden Profile — Rooted</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .profile-card {
            background: #fffdf9;
            border: 1px solid #d2cfbd;
            padding: 24px;
            margin-bottom: 24px;
        }
        .profile-header {
            display: flex;
            justify-content: space-between;
            align-items: baseline;
            flex-wrap: wrap;
            gap: 12px;
            border-bottom: 1px dashed #a8ad91;
            padding-bottom: 12px;
            margin-bottom: 16px;
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
            <a href="${pageContext.request.contextPath}/journals">Garden Journals</a>
            <a href="${pageContext.request.contextPath}/members">Members</a>
            <c:choose>
                <c:when test="${not empty sessionScope.userId}">
                    <a href="${pageContext.request.contextPath}/profile?id=${sessionScope.userId}">
                        ${isOwnProfile ? '<strong>My Profile</strong>' : 'My Profile'}
                    </a>
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
            <a href="${pageContext.request.contextPath}/members">&larr; Community Members</a>
        </p>

        <c:if test="${param.updated eq 'true'}">
            <div class="alert-success">
                Your garden profile bio has been updated successfully!
            </div>
        </c:if>

        <div class="profile-card">
            <div class="profile-header">
                <div>
                    <h2 style="margin: 0; border: none; padding: 0;">
                        🌿 <c:out value="${profileUser.username}"/>
                    </h2>
                    <span style="color: #656b5c; font-size: 0.95rem;">
                        Member since <c:out value="${profileUser.createdAt}"/>
                    </span>
                </div>
                <c:if test="${isOwnProfile}">
                    <div>
                        <a href="${pageContext.request.contextPath}/profile/edit" class="btn">
                            Edit My Bio
                        </a>
                    </div>
                </c:if>
            </div>

            <div>
                <h4 style="margin-bottom: 6px; color: #354f3b;">Garden Notes & Bio:</h4>
                <p style="white-space: pre-wrap; line-height: 1.6; color: #2e3326;">
                    <c:choose>
                        <c:when test="${not empty profileUser.bio}">
                            <c:out value="${profileUser.bio}"/>
                        </c:when>
                        <c:otherwise>
                            <em>This gardener hasn't added any garden notes yet.</em>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>

        <%-- Discussions started by this user --%>
        <section>
            <h3>Discussions Started (<c:out value="${userThreads.size()}"/>)</h3>
            <c:choose>
                <c:when test="${not empty userThreads}">
                    <table>
                        <thead>
                            <tr>
                                <th style="width: 65%;">Topic</th>
                                <th style="width: 15%;">Replies</th>
                                <th style="width: 20%;">Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="thread" items="${userThreads}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/threads?id=${thread.id}">
                                            <strong><c:out value="${thread.title}"/></strong>
                                        </a>
                                    </td>
                                    <td><c:out value="${thread.postCount > 1 ? thread.postCount - 1 : 0}"/></td>
                                    <td style="font-size: 0.9rem; color: #666;"><c:out value="${thread.createdAt}"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p style="color: #777;">No forum discussions started yet.</p>
                </c:otherwise>
            </c:choose>
        </section>

        <%-- Garden journals by this user --%>
        <section style="margin-top: 24px;">
            <h3>Garden Journals (<c:out value="${userJournals.size()}"/>)</h3>
            <c:choose>
                <c:when test="${not empty userJournals}">
                    <c:forEach var="journal" items="${userJournals}">
                        <article>
                            <h4>
                                <a href="${pageContext.request.contextPath}/journals?id=${journal.id}">
                                    <c:out value="${journal.title}"/>
                                </a>
                            </h4>
                            <p style="font-size: 0.85rem; color: #666; margin-top: 0;">
                                Logged on <c:out value="${journal.createdAt}"/>
                            </p>
                            <p><c:out value="${journal.excerpt}"/></p>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="color: #777;">No garden journal entries published yet.</p>
                </c:otherwise>
            </c:choose>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
