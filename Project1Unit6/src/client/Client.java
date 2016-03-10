package client;

/* JSPHDEV Project 1 - U5
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;

import adapter.BuildAuto;
import util.Serialize;

public class Client extends DefaultSocketClient {
	private SelectCarOption sco;
	private Socket sock;
	private BufferedReader reader;
	private PrintWriter writer;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private ArrayList<String> autoList = null;
	private BuildAuto carToConfigure = new BuildAuto();
	
	public Client(String strHost, int iPort) {
		super(strHost, iPort);
		sock = super.getSock();
		reader = super.getReader();
		writer = super.getWriter();
	}
	
	/* Constructor for servlet */
	public Client(String serverIP, int iPort, int flag) {
		try {
			sock = new Socket(serverIP, iPort); // connected to server
			System.out.println("Connected to "+ serverIP);
		} catch (IOException socketError) {
			System.err.println("Unable to connect to " + serverIP);
			//return false;
		}
	}
	
	/* Main function */
	public static void main(String[] args) throws IOException {
		////////////////////////////////////////////
		/////////CHANGE IP ADDRESS!!!!//////////////
		Client client = new Client("10.232.5.222", 4442);
		////////////////////////////////////////////
		client.start();
		System.out.println("Client running...");
		
    }
	
	/* For servlet, check if connection is open */
	public boolean connectionOpen() {
		try {
			System.out.println("About to check connection...");
			oos = new ObjectOutputStream(sock.getOutputStream());
			System.out.println("HEEEEEE");
			oos.flush();
			ois = new ObjectInputStream(sock.getInputStream());
			System.out.println("HEEEERRREEE");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/* Getter */
	public ArrayList<String> getAutoList() {
		return autoList;
	}
	
	
	/* Override */
	public void handleSession(){
		sock = super.getSock();
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(
					new BufferedInputStream(sock.getInputStream()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		reader = super.getReader();
		writer = super.getWriter();

		if (DEBUG) System.out.println ("Handling session with "
				+ super.getStrHost() + ":" + super.getPort());
		
		BufferedReader commandIn = new BufferedReader(new InputStreamReader(System.in));
		String command = "";
		String filename = "";
		String strInput = "";
		while(true) {
			System.out.println("<<Commands>>: s(send properties), a(list all models),\n"
					+ "	c(choose a model to configure), q(quit):");
			Properties props= new Properties();

			try {
				command = commandIn.readLine();
				/* Send */
				if(command.equals("s")) {
					System.out.println("Enter File Name");
					System.out.println("HINT: try prius.properties or beetle.properties");
					filename = commandIn.readLine();
					oos.writeObject(command);//write to server
					CarModelOptionsIO cmoIO = new CarModelOptionsIO();
					cmoIO.propToServer(filename,ois,oos);
				}
				/* List all */
				if(command.equals("a")) {
					oos.writeObject("a");//write to server
					try {
						System.out.println("Server: "+(String) ois.readObject()); //print response
						autoList = (ArrayList<String>)ois.readObject();
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					System.out.println("List of Autos:");
					for (int i = 0; i < autoList.size(); i++) {
						System.out.println("Model " + i + " : "
								+ autoList.get(i));
					}
				}
				/* c for choose*/
				if(command.equals("c")) {
					oos.writeObject("c");//write to server
					System.out.println("Type name of model you would like to configure");
					String modelName = commandIn.readLine();
					oos.writeObject(modelName);//write to server
					try { //print server response: Found car! Preparing to send...
						System.out.println("Server: "+(String) ois.readObject());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} 
					
					System.out.println("Deserializing object...");
					BuildAuto myCar = new BuildAuto();
					myCar.deserialize(ois);
					myCar.printAuto();
					
					System.out.println("Type optionset you would like to change");
					String opsetName = commandIn.readLine();
					System.out.println("Type option you would like to change");
					String optionName = commandIn.readLine();
					System.out.println("Enter new price for this option");
					float newPrice = Float.parseFloat(commandIn.readLine());
					myCar.updateOptionPrice(modelName, opsetName, optionName, newPrice);
					System.out.println("Updated "+ modelName + "!");
				}
				if(command.equals("q")) {
					this.closeSession();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		}
	}
	
	/* listAll - Method for servlet to call. 
	 * Asks the server for a list of all available models. */
	public void listAll() {
		try {
			oos.writeObject("a");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//write to server
		
		//Receive response
		try {
			System.out.println("Server: "+(String) ois.readObject()); //print response
			autoList = (ArrayList<String>)ois.readObject(); //save list
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		//Print list of automobiles available
		System.out.println("List of Autos:");
		for (int i = 0; i < autoList.size(); i++) {
			System.out.println("Model " + i + " : "
					+ autoList.get(i));
		}
	}
	
	/* getAuto - Method for servlet to call. 
	 * Asks the server to send a serialized automobile of the name modelName . */
	public void getAuto(String modelName) {
		try {
			oos.writeObject("c");
			oos.writeObject(modelName);//write to server
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//write to server
		try { //print server response: Found car! Preparing to send...
			System.out.println("Server: "+(String) ois.readObject());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} 
		
		System.out.println("Deserializing object...");

		carToConfigure.deserialize(ois); //save in LHM?
		carToConfigure.printAuto();
		
	}
	

	public void closeSession(){
    	try {
    	   oos.close();
    	   ois.close();
    	   System.out.println("Client socket closing...");
    	   sock.close();
    	}
    	catch (IOException e){
         if (DEBUG) System.err.println
          ("Error closing socket to " );
    	}
    }
	
	

}
