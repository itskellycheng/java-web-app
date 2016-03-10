package server;

/* JSPHDEV Project 1
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;

import adapter.BuildAuto;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants{
	private BufferedReader reader;
	
	private PrintWriter writer;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	//private Socket sock;
	private Socket clientSocket = null;
	private String strHost; //Host IP
	private int iPort;
	private Properties props;
	//private String models;
	
	//constructor
	public DefaultSocketClient(){}
	public DefaultSocketClient(String strHost, int iPort) {       
        setPort (iPort);
        setHost (strHost);
	}
	public DefaultSocketClient(Socket clientSocket) {       
        this.clientSocket = clientSocket;
	}
	
	//getters and setters
	public Socket getSock() {
		return clientSocket;
	}
	public PrintWriter getWriter() {
		return this.writer;
	}
	public BufferedReader getReader() {
		return this.reader;
	}
	public String getStrHost() {
		return strHost;
	}
	public void setPort(int iPort) {
		this.iPort = iPort;
	}
	public void setHost(String strHost) {
		this.strHost = strHost;
	}
	
	//run
//	public void run(){
//		if (openConnection()){
//			handleSession();
//			closeSession();
//		}
//	}
	
	public void run(){
		if (openConnection()){
			handleSession();
			closeSession();
		}
	}
	
	
	public boolean openConnection() {
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					clientSocket.getInputStream()));
			
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			//ois = new ObjectInputStream(
					//new BufferedInputStream(clientSocket.getInputStream()));
			oos.flush();
			ois = new ObjectInputStream(clientSocket.getInputStream());
			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
//	public boolean openConnection() {
//		try {
//			clientSocket = new Socket(this.strHost, this.iPort);                    
//		}
//		catch(IOException socketError){
//			if (DEBUG) System.err.println
//			("Unable to connect to " + strHost);
//			return false;
//		}
//		try {
//			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			writer = new PrintWriter(clientSocket.getOutputStream(), true);
//		}
//		catch (Exception e){
//			if (DEBUG) System.err.println
//			("Unable to obtain stream to/from " + strHost);
//			return false;
//		}
//		return true;
//	}
	
//	public void handleSession(){
//		String strInput = "";
//		if (DEBUG) System.out.println ("Handling session with "
//				+ strHost + ":" + iPort);
//		try {
//			while ( (strInput = reader.readLine()) != null)
//				handleInput (strInput);
//		}
//		catch (IOException e){
//			if (DEBUG) System.out.println ("Handling session with "
//					+ strHost + ":" + iPort);
//		}
//	}  
	public void handleSession() {
//		try {
//			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//			BufferedReader in = new BufferedReader(
//					new InputStreamReader(
//					clientSocket.getInputStream()));
//			
//			oos = new ObjectOutputStream(clientSocket.getOutputStream());
//			//ois = new ObjectInputStream(
//					//new BufferedInputStream(clientSocket.getInputStream()));
//			oos.flush();
//			ois = new ObjectInputStream(clientSocket.getInputStream());
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		String inputLine = "";
		BuildCarModelOptions bcmo = new BuildCarModelOptions();
		while(true) {
			try {
				inputLine = (String)ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				continue;
			}
			
			while (inputLine != null) {
				System.out.println("Client: "+inputLine); //echo client
				
				if (inputLine.equals("s")) {
					System.out.println("building from prop");
					bcmo.buildFromProp(ois);
					try {
						oos.writeObject("Success");
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				if (inputLine.equals("a")) { //list all cars
					System.out.println("Listing all models"); 
					try {
						oos.writeObject("Listing all models...");
						BuildAuto myCar = new BuildAuto();
						ArrayList<String> autoList = new ArrayList<String>();
						autoList = myCar.listAll();
						oos.writeObject(autoList);
						System.out.println("List sent to client");

					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				}
				if (inputLine.equals("c")) { 
					try {
						String autoName = (String)ois.readObject();
						oos.writeObject("Found car! Preparing to send...");
						BuildAuto myCar = new BuildAuto();
						myCar.serializeAuto(autoName, oos);
						break;
					} catch (ClassNotFoundException | IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}

	}
	
	public void handleInput(String strInput){
		System.out.println(strInput);
	}       
	
	public void closeSession(){
		try {
			clientSocket.close();
			System.out.println("Server shutting down.");
			ois.close();
			oos.close();
		}
		catch (IOException e){
			if (DEBUG) System.err.println
			("Error closing socket");
		}
    }
	
//	public void closeSession(){
//    	try {
//    	   writer = null;
//    	   reader = null;
//    	   //command = null;
//    	   System.out.println("Client socket closing...");
//    	   clientSocket.close();
//    	}
//    	catch (IOException e){
//         if (DEBUG) System.err.println
//          ("Error closing socket to " + strHost);
//    	}
//    }
	
	

	
}
