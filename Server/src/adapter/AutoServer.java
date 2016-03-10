package adapter;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import exception.AutoException;

/* JSPHDEV Project 1
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

public interface AutoServer {
	public void buildFromProp(Properties props) throws AutoException;
	public ArrayList listAll();
	public void serializeAuto(String autoName, ObjectOutputStream out);
}
