package servlet;

/* JSPHDEV Project 1
 * Choose - This is a servlet that interacts with the client to 
 * get data (optionsets and options available) of the chosen model.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import adapter.BuildAuto;
import client.Client;

@WebServlet("/configure-model")
public class ConfigureModel extends HttpServlet{
	private Client client;
	private String modelName;
	private BuildAuto myCar = new BuildAuto();
	
	/* The last page had a form which was sent with GET method */
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		modelName = request.getParameter("carlist");
		
		System.out.println("About to start client!");
		//starting a client, the extra int arg invokes the special constructor
		client = new Client("128.237.172.6", 4442, 1); 
		if (client.connectionOpen()) {
			System.out.println("Checking connection...Client connected");
		}
		else {
			System.out.println("Checking connection...No connection");
		}
		
		System.out.println("In ConfigureModel");
		System.out.println("About to load data for "+modelName);
		client.getAuto(modelName);
		ArrayList<String> opSetList = myCar.getOpSetList(modelName); //list of optionsets
		
		//write out basic html page headers etc.
		out.println
		("<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head><title>Basic Car Choice</title></head>\n" +
				"<body>\n" +
				"<h1>Basic Car Choice</h1>\n" +
				"<p>" + modelName + "</p>\n" +
				"</body></html>" +
				"<form action=\"result.jsp\" id=\"carform\" method=\"POST\">\n" +
				"<input type=\"hidden\" value=\""+ modelName + "\" name=\"modelName\">"
		);//end out.println
		
		//write out each select list
		for(int i=0; i<opSetList.size(); i++) {
			out.println(opSetList.get(i) + ":\n" +
					"<select name=\"" + opSetList.get(i) + "\" form=\"carform\">\n");
			ArrayList<String> optionList = myCar.getOptionList(modelName, i);
			System.out.println("optionList size=" + optionList.size());
			for (int j = 0; j < optionList.size(); j++) {
				System.out.println("option "+ j + " " + optionList.get(j));
				out.println("<option value=\"" +
						optionList.get(j) +
						"\">" + 
						optionList.get(j) +
						"</option>\n"
				);//end out.println
			}
			out.println("</select>\n<br>\n");
		}
		//write out basic html page footers etc.
		out.println (
				"<input type=\"submit\" value=\"Done\">\n</form>\n" +
				"</body></html>\n"
		);//end out.println
	}
	
	/*For doPost, do same thing as doGet*/
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	@Override
	public void destroy() {
		client.closeSession();
		super.destroy();
	}

}
