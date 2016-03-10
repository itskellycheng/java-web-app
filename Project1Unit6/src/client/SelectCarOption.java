package client;

/* JSPHDEV Project 1
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class SelectCarOption {
	private BufferedReader commandIn;
	private CarModelOptionsIO carIO;
	public void runSelect(Socket sock, BufferedReader reader, PrintWriter writer){
		try {
			commandIn = new BufferedReader(new InputStreamReader(System.in));
			String messageFromServer = "";
			String command = "";
			String filename = "";
			while(true) {
				System.out.println("commend: s(send), f(get all models),\n"
						+ "	c(choose model and option to show):");
				command = commandIn.readLine();
				if(command.equals("s")) {
					writer.println("s");
					System.out.println("Enter Properties File Name:");
					filename = commandIn.readLine();
					///////////////
					Properties props= new Properties();
					ObjectOutputStream oos = null;
					try {
						System.out.println("in propToServer");
						FileInputStream in = new FileInputStream("prius.properties");
						props.load(in); //This loads the entire file in memory.
						in.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
					
					//send to server
					try {
						oos = new ObjectOutputStream(sock.getOutputStream());
						//props.store(oos, filename);
						oos.writeObject(props);
						oos.flush();
						oos.close();
						messageFromServer = reader.readLine();
						System.out.println(messageFromServer);
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
					///////////////
					//carIO.propToServer(filename, sock, reader);
				} else if(command.equals("f")) {
					writer.println("f");
					System.out.println("All available models are:");
					messageFromServer = reader.readLine();
					//this.models = messageFromServer;
					System.out.println(messageFromServer);
				} else if(command.equals("c")) {
					writer.println("c");
					writer.println("FordZTW");
					messageFromServer = reader.readLine();
					System.out.println(messageFromServer);
					if(messageFromServer.equals("No such Model!")) {
						continue;
					}
					System.out.println("\nThe option sets are:\n");
					messageFromServer = reader.readLine();
					System.out.println(messageFromServer);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

