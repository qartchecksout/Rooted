# 🌱 Rooted

A small gardening community website with an old-internet forum aesthetic, built with **Java Servlets, JSP, JDBC, and MySQL**.

---

## 🌻 Features

- **Discussion Boards (Forums)**: Browse categorized boards (e.g. *The Potting Shed*, *Plant SOS*, *Show Your Patch*) with dynamic topic & reply counts.
- **Topics & Replies**: Start discussions with a board selector; reply to ongoing conversations with chronological message ordering and Original Poster (OP) badges.
- **Garden Journals**: Write, browse, and read personal garden logs, harvest notes, and planting observations with formatted excerpts.
- **Member Authentication**: Secure registration and login using **BCrypt** password hashing (cost factor 12) and session fixation protection.
- **Route Authorization (`AuthFilter`)**: Automatically redirects guests attempting to post or write journals to the login page and redirects them back after sign-in.
- **Member Directory & Profiles**: Public member directory with join dates and bios; member profile pages displaying their discussions and journals, plus self-bio editing.
- **Custom Themed Error Pages**: Styled 404 (Not Found) and 500 (Internal Server Error) views configured via `web.xml`.

---

## 🛠️ Tech Stack

- **Java**: 17+ (tested with JDK 21)
- **Web Layer**: Jakarta Servlet 6.0, Jakarta Server Pages (JSP) & JSTL 3.0
- **Database**: MySQL 8.0+ via JDBC (`mysql-connector-j 8.3.0`)
- **Security**: BCrypt (`at.favre.lib:bcrypt 0.10.2`)
- **Server**: Apache Tomcat 10.1
- **Build Tool**: Apache Maven 3.9+

---

## 📋 Prerequisites

Before running the project, make sure you have:
1. **JDK 17 or newer** (`java -version`)
2. **Apache Maven 3.9+** (`mvn -v`)
3. **Apache Tomcat 10.1** (Core `.zip`, e.g. `C:\tools\apache-tomcat-10.1.34`)
4. **MySQL 8.0+** running locally (e.g. on port 3306)

---

## ⚙️ One-Time Setup

### 1. Environment Variables
Make sure `JAVA_HOME` and `CATALINA_HOME` are set in your environment:

```powershell
# Set CATALINA_HOME to your Tomcat directory (no trailing backslash)
[Environment]::SetEnvironmentVariable("CATALINA_HOME", "C:\tools\apache-tomcat-10.1.34", "User")

# Set JAVA_HOME to your JDK directory
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-21.0.12", "User")
```

### 2. Database Setup
Create the database and tables using the provided schema:

- **Via MySQL Workbench / CLI**:
  Open and execute [`database/schema.sql`](database/schema.sql), or run:
  ```bash
  mysql -u root -p < database/schema.sql
  ```
  This creates the `rooted_db` database, all necessary tables (`users`, `forums`, `threads`, `posts`, `journals`), foreign key constraints, and default seed forums.

### 3. Database Credentials Configuration
Copy the template configuration file to `db.properties`:

```powershell
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edit `src/main/resources/db.properties` with your MySQL credentials:
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/rooted_db?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```
> **Note**: `src/main/resources/db.properties` is in `.gitignore` so your personal database credentials will never be committed to git.

---

## 🚀 Build and Run

### Option A — One-Command Deploy (PowerShell)
From the project root folder:

```powershell
powershell -ExecutionPolicy Bypass -File .\deploy.ps1
```

This compiles the code, packages `target/rooted.war`, copies it to `$CATALINA_HOME\webapps\`, and launches Tomcat.

### Option B — Manual Maven & Tomcat

1. Build the WAR package:
   ```powershell
   mvn clean package
   ```
2. Copy the resulting WAR into Tomcat's `webapps`:
   ```powershell
   Copy-Item .\target\rooted.war "$env:CATALINA_HOME\webapps\" -Force
   ```
3. Start Tomcat:
   ```powershell
   & "$env:CATALINA_HOME\bin\startup.bat"
   ```

---

## 🌐 Application URLs

Once Tomcat is running, open your browser:

| Page | URL |
|---|---|
| **Homepage** | [http://localhost:8080/rooted/](http://localhost:8080/rooted/) |
| **Discussion Boards** | [http://localhost:8080/rooted/forums](http://localhost:8080/rooted/forums) |
| **Garden Journals** | [http://localhost:8080/rooted/journals](http://localhost:8080/rooted/journals) |
| **Community Members** | [http://localhost:8080/rooted/members](http://localhost:8080/rooted/members) |
| **Register** | [http://localhost:8080/rooted/register](http://localhost:8080/rooted/register) |
| **Login** | [http://localhost:8080/rooted/login](http://localhost:8080/rooted/login) |
| **Servlet Diagnostic Page** | [http://localhost:8080/rooted/hello](http://localhost:8080/rooted/hello) |

---

## 📁 Project Structure

```
Rooted/
├── database/
│   └── schema.sql                    # MySQL schema definition & seed data
├── src/main/
│   ├── java/com/rooted/
│   │   ├── dao/                      # Data Access Objects (UserDAO, ForumDAO, ThreadDAO, PostDAO, JournalDAO)
│   │   ├── exception/                # AppException base custom exception
│   │   ├── filter/                   # AuthFilter (route authorization & redirect)
│   │   ├── model/                    # POJO domain models (User, Forum, ForumThread, Post, Journal)
│   │   ├── servlet/                  # HTTP controllers (Home, Forum, Thread, Journal, Auth, Profile)
│   │   └── util/                     # DBUtil (JDBC connection) & PasswordUtil (BCrypt)
│   ├── resources/
│   │   └── db.properties.example     # Template credentials file
│   └── webapp/
│       ├── css/style.css             # Earthy, old-internet forum aesthetic stylesheet
│       ├── WEB-INF/
│       │   ├── web.xml               # Servlet 6.0 deployment descriptor, sessions & error mappings
│       │   └── views/                # Secure server-side JSP templates
│       │       ├── error/            # Custom 404 and 500 error views
│       │       └── ...               # home, forum, thread, journal, login, register, profile views
│       └── index.jsp                 # Root entrypoint forwarding to /home
├── deploy.ps1                        # PowerShell deployment script for local Tomcat
└── pom.xml                           # Maven project configuration & dependencies
```

---

## 👥 Team Workflow

1. Always pull latest before starting a task:
   ```bash
   git checkout main
   git pull origin main
   ```
2. Create a feature branch:
   ```bash
   git checkout -b feature/short-name
   ```
3. Commit small and often with descriptive messages.
4. Push your branch and open a Pull Request on GitHub for peer review.
