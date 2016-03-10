package exception;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JSPHDEV Project 1
 * AutoException - a custom exception class.
 * Tracks error no and error message. Logs with timestamps into 
 * a log file.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

public class AutoException extends Exception{
	private int errno;
	private String returnStr;
	private static String logFile = "mylog.txt";
    private final static DateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy  hh:mm:ss a");
	
	/* Constructors */
	public AutoException() {}
	public AutoException(int errno, String message) {
		//overload Exception class constructor
		super(message);
		this.errno = errno;
	}
	
	public int getErrno() {
		return errno;
	}
	public String getReturnStr() {
		return returnStr;
	}
	/* fix - accessible through FixAuto interface */
	public void fix(int errno) {
		Fix1to10 f1 = new Fix1to10();
		switch(errno/10) { //grouped by 10
		case 0:
			System.out.printf("Trying to fix...\n");
			returnStr = f1.fixMe(errno);
			break;
		}
		writeToLog();
	}
	
	/* writeToLog - writes exception error msg to logFile */
	public void writeToLog() {
		Date now = new Date();
		String currentTime = sdf.format(now);
		try {
			FileWriter aWriter = new FileWriter(logFile, true);
			aWriter.write(currentTime + " " + this.getMessage() + "\n");
			aWriter.flush();
            aWriter.close();
		} catch(IOException e) {
			System.out.println("IOException" + e.getMessage());
		}
	}

}
