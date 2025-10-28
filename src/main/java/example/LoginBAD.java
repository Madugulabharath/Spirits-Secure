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

    // ✅ Render-ready DB credentials
   private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
   private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
   



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = "SELECT * FROM register22 WHERE username = ? AND passwords = ?";

            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, username);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        response.getWriter().println("<h2>Login Successful! Welcome " + username + "</h2>");
                    } else {
                        response.getWriter().println("<h3>Invalid Username or Password. Try Again.</h3>");
                    }
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
        response.getWriter().println("<h3>Please use POST method to login.</h3>");
    }
}
