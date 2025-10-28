package example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/LoginBAD")
public class LoginBAD extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ✅ Database credentials (fetched from environment variables)
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html;charset=UTF-8");

        try {
            // ✅ Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Query to check user credentials
            String query = "SELECT * FROM register22 WHERE username = ? AND passwords = ?";

            // ✅ Try-with-resources to auto-close connections
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, username);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        // ✅ Successful login page
                        response.getWriter().println(
                            "<html><body style='font-family:Arial;background-color:#e0ffe0;text-align:center;padding-top:50px;'>" +
                            "<h2 style='color:green;'>Login Successful!</h2>" +
                            "<h3>Welcome, " + username + "!</h3>" +
                            "</body></html>"
                        );
                    } else {
                        // ❌ Invalid login page
                        response.getWriter().println(
                            "<html><body style='font-family:Arial;background-color:#ffe0e0;text-align:center;padding-top:50px;'>" +
                            "<h3 style='color:red;'>Invalid Username or Password</h3>" +
                            "<a href='LoginBAD.html'>Try Again</a>" +
                            "</body></html>"
                        );
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println(
                "<html><body style='font-family:Arial;background-color:#ffe0e0;text-align:center;padding-top:50px;'>" +
                "<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>" +
                "</body></html>"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
            "<html><body style='font-family:Arial;text-align:center;padding-top:50px;'>" +
            "<h3>Please use POST method to login.</h3>" +
            "</body></html>"
        );
    }
}
