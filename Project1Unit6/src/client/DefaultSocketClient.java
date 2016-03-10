package client;

/* JSPHDEV Project 1
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.io.*;
import java.net.*;
import java.util.Properties;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants{
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket sock;
	private String strHost; //Host IP
	private int iPort;
	private Properties props;
	//private String models;
	
	/*constructors */
	public DefaultSocketClient() { //does nothing
	}
	public DefaultSocketClient(String strHost, int iPort) {       
        setPort (iPort);
        setHost (strHost);
	}
	
	//getters and setters
	public Socket getSock() {
		return sock;
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
	public String getPort() {
		String strI = String.valueOf(iPort);
		return strI;
	}
	public void setPort(int iPort) {
		this.iPort = iPort;
	}
	public void setHost(String strHost) {
		this.strHost = strHost;
	}
	
	//run
	public void run(){
		if (openConnection()){
			handleSession();
			closeSession();
		}
	}
	
	public boolean openConnection() {
		try {
			sock = new Socket(this.strHost, this.iPort);                    
		}
		catch(IOException socketError){
			if (DEBUG) System.err.println
			("Unable to connect to " + strHost);
			return false;
		}
		try {
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			writer = new PrintWriter(sock.getOutputStream(), true);
		}
		catch (Exception e){
			if (DEBUG) System.err.println
			("Unable to obtain stream to/from " + strHost);
			return false;
		}
		return true;
	}
	
	public void handleSession(){
		String strInput = "";
		if (DEBUG) System.out.println ("Handling session with "
				+ strHost + ":" + iPort);
		try {
			while ( (strInput = reader.readLine()) != null)
				handleInput (strInput);
		}
		catch (IOException e){
			if (DEBUG) System.out.println ("Handling session with "
					+ strHost + ":" + iPort);
		}
	}  
	
	public void handleInput(String strInput){
		System.out.println(strInput);
	}       
	
	public void closeSession(){
    	try {
    	   writer = null;
    	   reader = null;
    	   //command = null;
    	   System.out.println("Client socket closing...");
    	   sock.close();
    	}
    	catch (IOException e){
         if (DEBUG) System.err.println
          ("Error closing socket to " + strHost);
    	}
    }
	
	

	
}
