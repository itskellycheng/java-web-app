package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import model.Automobile;

public class Serialize {

	public void serializeAuto(Automobile a, String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(a);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in %s.ser\n", filename);
		}
		catch(IOException i) {
			i.printStackTrace();
		}
	}
	/* For Server - Writes out an automobile to ObjectOutputStream */
	public void serializeServer(Automobile a, ObjectOutputStream oos) {
		try {
			oos.writeObject(a);
			System.out.printf("Automobile has been serialized");
		}
		catch(IOException i) {
			i.printStackTrace();
		}
	}
	/* For Client - deserializes an automobile object that is passed to the client */
	public Automobile readFromServer(ObjectInputStream in) {
		try{
			Automobile a = null;
			a = (Automobile) in.readObject();
			return a;
		}
		catch(IOException i) {
			System.out.println("Failed in readFromServer");
			i.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException c) {
			System.out.println("Automotive class not found");
			c.printStackTrace();
			return null;
		}
	}
	public Automobile readSerialized(String filename) {
		try{
			Automobile a = null;
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			a = (Automobile) in.readObject();
			in.close();
			fileIn.close();
			return a;
		}
		catch(IOException i) {
			i.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException c) {
			System.out.println("Automotive class not found");
			c.printStackTrace();
			return null;
		}
	}

}
