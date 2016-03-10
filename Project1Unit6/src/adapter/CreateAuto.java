package adapter;

import java.io.ObjectInputStream;
import java.util.Properties;

import exception.AutoException;

public interface CreateAuto {
	/* Given a text file name and filetype builds an instance of Automobile*/
	public void buildAuto(String filename, int fileType) throws AutoException;
	/* Searches and prints the properties of a given Automodel*/
	public void printAuto(String autoName);
	public void deserialize(ObjectInputStream in);
	public void buildFromProp(Properties props) throws AutoException;
}
