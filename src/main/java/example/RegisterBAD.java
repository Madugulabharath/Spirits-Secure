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

    // ✅ Read credentials safely from environment variables (not hardcoded)
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

        response.setContentType("text/html");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql = "INSERT INTO register22 (username, email, passwords, phnumber) VALUES (?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, phnumber);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    response.getWriter().println("<h2>Registration successful!</h2>");
                } else {
                    response.getWriter().println("<h3>Failed to register. Try again.</h3>");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("<h3>Please submit the form using POST method.</h3>");
    }
}
