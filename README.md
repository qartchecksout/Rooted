# Rooted



A small gardening community website with an old-internet forum look, built with

Java Servlets, JSP, JDBC and MySQL for our Java course.



## Requirements



- JDK 17 or newer

- Apache Maven 3.9+

- Apache Tomcat 10.1 (download the **zip**, not the installer)

- Git

- MySQL (not used yet)



## One-time setup (Windows)



1. Make sure `JAVA\_HOME` points to your JDK folder (no trailing backslash), and

&#x20;  that `java -version` and `mvn -v` both work in a new terminal.

2. Extract Tomcat to a path without spaces, e.g. `C:\\Tools\\apache-tomcat-10.1.60`.

3. Point `CATALINA\_HOME` at that folder (no trailing backslash), then open a

&#x20;  **new** terminal:

```

&#x20;  setx CATALINA\_HOME "C:\\Tools\\apache-tomcat-10.1.60"

```

4. Clone the repo:

```

&#x20;  git clone https://github.com/qartchecksout/Rooted.git

&#x20;  cd Rooted

```



## Build and run



From the project folder, in PowerShell:



```

powershell -ExecutionPolicy Bypass -File .\\deploy.ps1

```



This builds the WAR, copies it into Tomcat and restarts Tomcat. Then open

http://localhost:8080/rooted/ (the Servlet test page is at /rooted/hello).

Run the script again every time you change code.



## Team workflow



- Never commit directly to `main`.

- Start every task from fresh code: `git checkout main`, then `git pull`.

- Create a branch for your task: `git checkout -b feature/short-name`.

- Commit small and often, push the branch, and open a Pull Request on GitHub.

- Another teammate looks it over before it is merged into `main`.



## Project structure



- `src/main/java/com/rooted/...` - Java code (Servlets, and later DAOs/models)

- `src/main/webapp` - HTML, CSS and JSP pages

- `pom.xml` - Maven build configuration

- `deploy.ps1` - one-command build and deploy to local Tomcat

