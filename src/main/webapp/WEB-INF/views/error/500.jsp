<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Server Error (500) — Rooted</title>
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
        </nav>
    </header>

    <main>
        <section style="text-align: center; padding: 40px 20px;">
            <h2>🥀 500 — Something Withered</h2>
            <p style="font-size: 1.1rem; color: #555e4b; max-width: 600px; margin: 15px auto;">
                Our garden servers encountered an unexpected problem while handling this request. We're tending to the soil and fixing it.
            </p>
            <div style="margin-top: 25px;">
                <a href="${pageContext.request.contextPath}/home" class="btn">
                    &larr; Return to Garden Gate (Home)
                </a>
            </div>
        </section>
    </main>

    <footer>
        <p>Made with care and a little dirt. &copy; Rooted</p>
    </footer>

</body>
</html>
