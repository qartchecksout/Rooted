package com.rooted;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

/**
 * Embedded Tomcat 10.1 launcher so anyone can run Rooted with one command
 * (via run.bat / run.ps1 / mvnw exec:java) without installing Tomcat or MySQL.
 */
public class EmbeddedServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            port = Integer.parseInt(envPort.trim());
        }

        String webappDir = new File("src/main/webapp").getAbsolutePath();
        String classesDir = new File("target/classes").getAbsolutePath();
        File baseDir = Files.createTempDirectory("rooted-tomcat-").toFile();
        baseDir.deleteOnExit();

        ClassLoader appClassLoader = EmbeddedServer.class.getClassLoader();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        tomcat.setPort(port);
        tomcat.getHost().setParentClassLoader(appClassLoader);
        tomcat.getConnector(); // triggers HTTP connector initialization

        // Mount main app at /rooted
        Context appCtx = tomcat.addWebapp("/rooted", webappDir);
        appCtx.setParentClassLoader(appClassLoader);

        WebResourceRoot resources = new StandardRoot(appCtx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", classesDir, "/"));
        appCtx.setResources(resources);

        // Redirect http://localhost:8080/ -> http://localhost:8080/rooted/
        Context rootCtx = tomcat.addContext("", baseDir.getAbsolutePath());
        rootCtx.setParentClassLoader(appClassLoader);
        Tomcat.addServlet(rootCtx, "rootRedirect", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
                resp.sendRedirect("/rooted/");
            }
        });
        rootCtx.addServletMappingDecoded("/*", "rootRedirect");

        tomcat.start();

        String url = "http://localhost:" + port + "/rooted/";
        System.out.println();
        System.out.println("==========================================================");
        System.out.println("  Rooted is running!");
        System.out.println("  Open in your browser: " + url);
        System.out.println("  Press Ctrl+C to stop the server.");
        System.out.println("==========================================================");
        System.out.println();

        // Try opening the browser automatically when running on a desktop session
        if (!"true".equalsIgnoreCase(System.getenv("ROOTED_NO_BROWSER"))) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                }
            } catch (Exception ignored) {
            }
        }

        tomcat.getServer().await();
    }
}
