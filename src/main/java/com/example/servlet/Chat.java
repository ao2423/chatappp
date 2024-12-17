package com.example.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/indexChat")
public class Chat extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String friendId = request.getParameter("itemId");
		String friendName = request.getParameter("itemName");
		
		System.out.println("friendId="+friendId+"friendName="+friendName);
		
        request.setAttribute("friendId", friendId);
        request.setAttribute("friendName", friendName);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/view/Chat.jsp");
        dispatcher.forward(request, response);
		
	}

}
