package servlet;

/* JSPHDEV Project 1
 * DisplayModels - This is a servlet that interacts with the client to 
 * get the list of available models.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */
import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import client.Client;

@WebServlet("/display-models") //the url where the servlet is
public class DisplayModels extends HttpServlet{
	private Client client;
	
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		System.out.println("About to start client!");
		//starting a client, the extra int arg invokes the special constructor
		Client client = new Client("128.237.172.6", 4442, 1);
		if (client.connectionOpen()) {
			System.out.println("Checking connection...Client connected");
		}
		else {
			System.out.println("Checking connection...No connection");
		}

		client.listAll(); //client sends command to server and gets list back
		System.out.println("Getting list");
		ArrayList<String> autoList = client.getAutoList();
		
		//Write out an html page
		out.println
		("<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head><title>A Test Servlet</title></head>\n" +
				"<body>\n" +
				"<h2>Select a model to configure</h2>\n" +
				"<form action=\"configure-model\" id=\"carform\" method=\"GET\">\n" +
				"<select name=\"carlist\" form=\"carform\">\n"

		);/* end out.println */
		for(int i=0; i<autoList.size(); i++) {
			out.println("<option value=\"" + 
					autoList.get(i) +
					"\">" + 
					autoList.get(i) + 
					"</option>\n"
			);
		}
		out.println
		(
				"</select>\n" +
				"<input type=\"submit\">\n</form>\n" +
				"</body></html>\n"
		);/* end out.println */
	}
	
	@Override
	public void destroy() {
		client.closeSession();
		super.destroy();
	}
}
