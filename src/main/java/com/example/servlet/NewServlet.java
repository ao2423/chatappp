package com.example.servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.util.DBUtil;

/**
 * Servlet implementation class New
 */
@WebServlet("/new")
public class NewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/view/Member.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String newusername = request.getParameter("newname");
		String newpassword = request.getParameter("newpassword");
		
		
		                                                                                                                        
		try(Connection conn = DBUtil.getConnection()){
		String sql = "INSERT INTO userinformation (name, password) VALUES (?, ?)";
		String testsql = "select name from userinformation where name = ? ";
		PreparedStatement preparedStatement;
		
		
		preparedStatement = conn.prepareStatement(testsql);
		preparedStatement.setString(1, newusername);
		ResultSet resultSet =  preparedStatement.executeQuery();
		
		if(!resultSet.next()) {
		conn.setAutoCommit(false);
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, newusername);
		preparedStatement.setString(2, newpassword);
		preparedStatement.executeUpdate();
		conn.commit();
		request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
		}else{
			String errormsg = "そのユーザー名は既に登録されています";
			request.setAttribute("errormsg",errormsg);
			request.getRequestDispatcher("/WEB-INF/view/Member.jsp").forward(request, response);
		}
		
		}catch (Exception e) {
		  e.printStackTrace();
		  String errorMessage = "登録中にエラーが発生しました: " + e.getMessage(); 
          request.setAttribute("errorMessage", errorMessage); 
          request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response); 
		}
	}

}
