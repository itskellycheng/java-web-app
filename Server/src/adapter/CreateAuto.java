package adapter;

import exception.AutoException;

public interface CreateAuto {
	/* Given a text file name and filetype builds an instance of Automobile*/
	public void buildAuto(String filename, int fileType) throws AutoException;
	/* Searches and prints the properties of a given Automodel*/
	public void printAuto(String autoName);
	/* Given an auto name, saves Automobile from LHM to database */
	public void buildAutoDB(String autoName);
}
