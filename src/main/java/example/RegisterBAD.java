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

@WebServlet("/RegisterBAD")
public class RegisterBAD extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ✅ Database credentials from environment variables (Aiven / Render)
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phnumber = request.getParameter("phone");

        response.setContentType("text/html;charset=UTF-8");

        try {
            // ✅ Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ SQL insert query
            String sql = "INSERT INTO register22 (username, email, passwords, phnumber) VALUES (?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, phnumber);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    // ✅ Success response page
                    response.getWriter().println(
                        "<html><body style='font-family:Arial;background-color:#e0ffe0;text-align:center;padding-top:50px;'>" +
                        "<h2 style='color:green;'>Registration Successful!</h2>" +
                        "<a href='LoginBAD.html'>Click here to Login</a>" +
                        "</body></html>"
                    );
                } else {
                    // ❌ Failure message
                    response.getWriter().println(
                        "<html><body style='font-family:Arial;background-color:#ffe0e0;text-align:center;padding-top:50px;'>" +
                        "<h3 style='color:red;'>Failed to register. Please try again.</h3>" +
                        "<a href='RegisterBAD.html'>Back to Register</a>" +
                        "</body></html>"
                    );
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
            "<h3>Please submit the form using POST method.</h3>" +
            "</body></html>"
        );
    }
}
