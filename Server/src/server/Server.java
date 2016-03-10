package server;

/* JSPHDEV Project 1 U5
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import adapter.BuildAuto;

public class Server extends DefaultSocketClient {
	private static int port = 4442;
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private boolean open = false;
	
	/* Constructor: Building a server */
	public Server() {
		System.out.println("Starting Server...");
		//initializing server
		try {
			serverSocket = new ServerSocket(port);
			System.out.printf("Server listening on port %d...\n", port);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.printf("Could not listen on port: %d\n", port);
			System.exit(1);
		}
	}
	
	
	/* Take incoming request and process request in its own thread utilizing 
	 * superclass DefaultSocketClient  */
	public void runServer() {
		DefaultSocketClient defaultClientSocket = null;
		try {
			while(true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Connected with client");
				//Spawn thread to handle session
				defaultClientSocket = new DefaultSocketClient(clientSocket);
	            defaultClientSocket.start();
			}
        } catch (IOException e) {
        		e.printStackTrace();
        		System.err.println("Accept failed");
    			System.exit(1);
        }
		
	}
	
	/* Main method */
	public static void main(String[] args) throws IOException {
		Server server = new Server(); //server is listening
		server.runServer();
      
    }
}
