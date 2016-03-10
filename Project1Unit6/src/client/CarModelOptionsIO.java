package client;

/* JSPHDEV Project 1 - Client
 * CarModelOptionsIO - Helper class for client. Reads data from Properties file 
 * and sends the properties object to server socket.
 * Called from handleSession of Client.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.io.*;
import java.net.Socket;
import java.util.Properties;

import adapter.BuildAuto;

public class CarModelOptionsIO {
	
	/*Reads data from Properties file and sends 
	 * the properties object to server socket.*/
	public void propToServer(String filename, ObjectInputStream ois, ObjectOutputStream oos) {

		Properties props= new Properties();

		try {
			System.out.println("in propToServer");
			FileInputStream in = new FileInputStream(filename);
			props.load(in); //This loads the entire file in memory.
			in.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		//send to server
		try {
			oos.writeObject(props);
			oos.flush();
			System.out.println("wrote to server");
			
			//reading server's response
			String strInput = (String)ois.readObject();
			while (!strInput.equals("Success"))
				System.out.println(strInput);
			System.out.println("Server: " + strInput);
		} catch (ClassNotFoundException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
