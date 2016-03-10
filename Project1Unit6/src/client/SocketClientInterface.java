package client;

/* JSPHDEV Project 1
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

public interface SocketClientInterface {
	boolean openConnection();
    void handleSession();
	void closeSession();
}
