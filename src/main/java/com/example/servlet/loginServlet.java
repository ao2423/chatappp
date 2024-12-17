package com.example.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.util.DBUtil;

@WebServlet("/login")
public class loginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("namea");
        String password = request.getParameter("password");
        
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        
        String sql = "SELECT name,id FROM userinformation WHERE name = ? AND password = ?";
        
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            System.out.println("Username: " + username);  // ログに出力
            System.out.println("Password: " + password);  // ログに出力

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                
            	if (resultSet.next()) {
            	    // ユーザーがデータベースに存在する場合
            	    String username1 = resultSet.getString("name");
            	    String id = resultSet.getString("id");
            	    System.out.println("id表示テスト"+id);

            	    // セッションにユーザー名をセット
            	    HttpSession session = request.getSession();
            	    session.setAttribute("username", username1);
            	    session.setAttribute("userid",id);

            	    // index.jspにフォワード
            	    request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
            	} else {
            	    // ユーザーが存在しない場合
            	    request.setAttribute("errorMessage", "ユーザー名またはパスワードが間違っています。");
            	    request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
            	}

            }
        } catch (SQLException e) {
            // SQL関連のエラーをキャッチ
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
        } catch (Exception e) {
            // その他の例外をキャッチ
            e.printStackTrace();
            request.setAttribute("errorMessage", "予期しないエラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
        }
    }
}


