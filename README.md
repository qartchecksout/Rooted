# 🌱 Rooted

A small gardening community website with an old-internet forum aesthetic, built with **Java Servlets, JSP, JDBC, and MySQL** (with automatic embedded local database & embedded Tomcat support so anyone can run it in seconds).

---

## ⚡ Quick Start (1 Command — No Tomcat or MySQL Setup Needed!)

Anyone cloning this repo only needs **Java 17+** installed:

```bash
git clone https://github.com/qartchecksout/Rooted.git
cd Rooted
```

Then run:
- **Windows (Command Prompt / Double-click):** `run.bat`
- **Windows (PowerShell):** `.\run.ps1`
- **Mac / Linux:** `./run.sh`

Wait a few seconds for it to start, then open:
👉 **[http://localhost:8080/rooted/](http://localhost:8080/rooted/)**

> **How it works automatically:**
> - Uses the included **Maven Wrapper** (`mvnw` / `mvnw.cmd`) and **Embedded Tomcat 10.1** — no manual Maven or Tomcat installation required.
> - Automatically uses **MySQL** if configured, or seamlessly falls back to an **Embedded Local Database (H2 in MySQL mode)** and auto-creates all tables & starter posts on first launch.
> - A starter account (`FernAndFound` / `garden123`) is pre-seeded on a fresh database, or you can register your own account right on `/register`.

---

## 🌻 Features

- **Discussion Boards (Forums)**: Browse categorized boards (*The Potting Shed*, *Plant SOS*, *Show Your Patch*) and recent discussions across all boards with dynamic topic & reply counts.
- **Topics & Replies**: Start discussions with a board selector; reply to ongoing conversations with chronological message ordering and Original Poster (OP) badges.
- **Garden Journals**: Write, browse, and read personal garden logs, harvest notes, and planting observations with formatted excerpts.
- **Member Authentication**: Secure registration and login using **BCrypt** password hashing (cost factor 12) and session fixation protection.
- **Route Authorization (`AuthFilter`)**: Redirects guests attempting to post or write journals to the login page and returns them after sign-in.
- **Member Directory & Profiles**: Public member directory with join dates and bios; member profile pages displaying their discussions and journals, plus bio editing.
- **Custom Themed Error Pages**: Styled 404 (Not Found) and 500 (Internal Server Error) views configured via `web.xml`.

---

## 🛠️ Tech Stack

- **Java**: 17+ (tested with JDK 21)
- **Web Layer**: Jakarta Servlet 6.0, Jakarta Server Pages (JSP) & JSTL 3.0
- **Database**: MySQL 8.0+ via JDBC (`mysql-connector-j 8.3.0`) with automatic H2 2.2 fallback
- **Security**: BCrypt (`at.favre.lib:bcrypt 0.10.2`)
- **Server**: Apache Tomcat 10.1 (embedded via `run.bat` or external WAR deploy)
- **Build Tool**: Apache Maven 3.9+ (included via `mvnw`)

---

## 🗄️ Optional: Using External MySQL & External Tomcat (Course Submission Setup)

If you want to run with your own local **MySQL 8.0** and external **Apache Tomcat 10.1**:

### 1. Database Setup
1. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties`:
   ```powershell
   cp src/main/resources/db.properties.example src/main/resources/db.properties
   ```
2. Edit `src/main/resources/db.properties` with your MySQL password:
   ```properties
   db.driver=com.mysql.cj.jdbc.Driver
   db.url=jdbc:mysql://localhost:3306/rooted_db?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
   db.username=root
   db.password=YOUR_MYSQL_PASSWORD
   ```
   *(The app automatically creates `rooted_db`, tables, and seed forums on startup if they don't exist yet — or you can run [`database/schema.sql`](database/schema.sql) manually in MySQL Workbench.)*

### 2. Deploy WAR to External Tomcat 10.1
Make sure `CATALINA_HOME` points to your Tomcat 10.1 folder, then run:
```powershell
powershell -ExecutionPolicy Bypass -File .\deploy.ps1
```
Then open **[http://localhost:8080/rooted/](http://localhost:8080/rooted/)**.

---

## 🌐 Application URLs

| Page | URL |
|---|---|
| **Homepage** | [http://localhost:8080/rooted/](http://localhost:8080/rooted/) |
| **Discussion Boards** | [http://localhost:8080/rooted/forums](http://localhost:8080/rooted/forums) |
| **Garden Journals** | [http://localhost:8080/rooted/journals](http://localhost:8080/rooted/journals) |
| **Community Members** | [http://localhost:8080/rooted/members](http://localhost:8080/rooted/members) |
| **Register** | [http://localhost:8080/rooted/register](http://localhost:8080/rooted/register) |
| **Login** | [http://localhost:8080/rooted/login](http://localhost:8080/rooted/login) |

---

## 📁 Project Structure

```
Rooted/
├── database/
│   └── schema.sql                    # MySQL schema definition & seed data
├── src/main/
│   ├── java/com/rooted/
│   │   ├── EmbeddedServer.java       # Embedded Tomcat 10.1 launcher for 1-command run
│   │   ├── dao/                      # Data Access Objects (UserDAO, ForumDAO, ForumThreadDAO, PostDAO, JournalDAO)
│   │   ├── exception/                # AppException base custom exception
│   │   ├── filter/                   # AuthFilter (route authorization & redirect)
│   │   ├── model/                    # POJO domain models (User, Forum, ForumThread, Post, Journal)
│   │   ├── servlet/                  # HTTP controllers (Home, Forum, Thread, Journal, Auth, Profile)
│   │   └── util/                     # DBUtil (MySQL + auto H2 fallback), AppBootstrapListener, PasswordUtil
│   ├── resources/
│   │   └── db.properties.example     # Template MySQL credentials file
│   └── webapp/
│       ├── css/style.css             # Earthy, old-internet forum aesthetic stylesheet
│       ├── WEB-INF/
│       │   ├── web.xml               # Servlet 6.0 deployment descriptor, UTF-8, sessions & error pages
│       │   └── views/                # Server-side JSP templates
│       │       ├── error/            # Custom 404 and 500 error views
│       │       └── ...               # home, forums, forum, thread, journal, login, register, profile views
│       └── index.jsp                 # Root entrypoint forwarding to /home
├── run.bat / run.ps1 / run.sh        # 1-command local launchers (zero extra installs)
├── deploy.ps1                        # PowerShell WAR deployment script for external Tomcat
├── mvnw / mvnw.cmd                   # Maven Wrapper (no manual Maven install needed)
└── pom.xml                           # Maven project configuration & dependencies
```

---

## 👥 Team Workflow

1. Pull latest before starting a task:
   ```bash
   git checkout main
   git pull origin main
   ```
2. Create a feature branch:
   ```bash
   git checkout -b feature/short-name
   ```
3. Commit small and often, push your branch, and open a Pull Request on GitHub.
