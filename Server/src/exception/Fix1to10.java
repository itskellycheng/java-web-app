package exception;

/* JSPHDEV Project 1
 * A helper class for AutoException. Contains fix methods for errno 1 to 10.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Fix1to10 {
	private String returnStr = null;
	
	/* See which fix method to use */
	public String fixMe(int errno) {
		switch(errno) {
		case 1:
			fix1();
			break;
		case 2:
			fix2();
			break;
		case 3:
			fix3();
			break;
		case 4:
			fix4();
			break;
		}
		return returnStr;
	}

	/* fix1 - for AutoException (errno=1)
	 * Fixes: Missing price in text file.
	 * Fixes by: asks user for correct price and changes input text file */
	public void fix1() {
		BufferedReader br;
		String filename;
		String newbp;
		
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the file name to fix:\n");
			filename = br.readLine();
			System.out.println("Enter the baseprice:\n");
			newbp = br.readLine();
			replacebp(filename, newbp);
		} catch (Exception e) {
	         return;
	    }
	}
	
	/* fix2 - for AutoException (errno=2)
	 * Fixes: Format of first line is wrong.
	 * Fixes by: asks user for correct info and changes input text file */
	public void fix2() {
		BufferedReader br;
		String filename;
		String[] ar = new String[5];
		
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the file name to fix:\n");
			filename = br.readLine();
			System.out.println("Enter the make:\n");
			ar[1] = br.readLine();
			System.out.println("Enter the model:\n");
			ar[2] = br.readLine();
			System.out.println("Enter the baseprice:\n");
			ar[3] = br.readLine();
			System.out.println("Enter the number of optionsets:\n");
			ar[4] = br.readLine();
			ar[0] = ar[1] + " " +  ar[2]; //full name
			replaceFirstLine(filename, ar);
		} catch (Exception e) {
	         return;
	    }
	}
	
	/* fix3 - for AutoException (errno=3)
	 * Fixes: Missing filename or wrong filename
	 * Fixes by: asks user for correct filename */
	public void fix3() {
		BufferedReader br;
		String filename;
		
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the correct file name:\n");
			filename = br.readLine();
			returnStr =  filename;
		} catch (Exception e) {
	         //return null;
	    }
	}
	
	/* fix4 - for AutoException (errno=4)
	 * Fixes: Can't find optionset in automobile class
	 * Fixes by: asks user for correct optionset name */
	public void fix4() {
		BufferedReader br;
		String name;
		
		try {
			System.out.println("Enter the correct optionset name:\n");
			br = new BufferedReader(new InputStreamReader(System.in));
			name = br.readLine();
			returnStr = name;
		} catch (Exception e) {
	         //return null;
	    }
	}
	
	/* fix5 - for AutoException (errno=5)
	 * Fixes: Can't find option in automobile class
	 * Fixes by: asks user for correct option name */
	public void fix5() {
		BufferedReader br;
		String name;
		
		try {
			System.out.println("Enter the correct option name:\n");
			br = new BufferedReader(new InputStreamReader(System.in));
			name = br.readLine();
			returnStr = name;
		} catch (Exception e) {
	         //return null;
	    }
	}
	
	
	/* Helper method for modifying baseprice in text file.*/
	public void replacebp(String filename, String newbp) {
	      String oldFileName = filename;
	      String tmpFileName = "tmp_auto.txt";

	      BufferedReader br = null;
	      BufferedWriter bw = null;
	      try {
	         br = new BufferedReader(new FileReader(oldFileName));
	         bw = new BufferedWriter(new FileWriter(tmpFileName));
	         String line;
	         //read first line
	         line = br.readLine();
	         String[] ar=line.split(",");
	         ar[3] = newbp;
	         bw.write(ar[0]+","+ar[1]+","+ar[2]+","+ar[3]+","+ar[4]+"\n");
	         //read and write rest of file
	         while ((line = br.readLine()) != null) {
	            bw.write(line+"\n");
	         }
	      } catch (Exception e) {
	         return;
	      } finally {
	         try {
	            if(br != null)
	               br.close();
	         } catch (IOException e) {
	            //
	         }
	         try {
	            if(bw != null)
	               bw.close();
	         } catch (IOException e) {
	            //
	         }
	      }
	      // Once everything is complete, delete old file..
	      File oldFile = new File(oldFileName);
	      oldFile.delete();

	      // And rename tmp file's name to old file name
	      File newFile = new File(tmpFileName);
	      newFile.renameTo(oldFile);
	   }
	/* Helper method for modifying entire first line in text file.*/
	public void replaceFirstLine(String filename, String ar[]) {
	      String oldFileName = filename;
	      String tmpFileName = "tmp_auto.txt";

	      BufferedReader br = null;
	      BufferedWriter bw = null;
	      try {
	         br = new BufferedReader(new FileReader(oldFileName));
	         bw = new BufferedWriter(new FileWriter(tmpFileName));
	         String line;
	         //read first line
	         line = br.readLine(); //read and do nothing
	         bw.write(ar[0]+","+ar[1]+","+ar[2]+","+ar[3]+","+ar[4]+"\n");
	         //read and write rest of file
	         while ((line = br.readLine()) != null) {
	            bw.write(line+"\n");
	         }
	      } catch (Exception e) {
	         return;
	      } finally {
	         try {
	            if(br != null)
	               br.close();
	         } catch (IOException e) {
	            //
	         }
	         try {
	            if(bw != null)
	               bw.close();
	         } catch (IOException e) {
	            //
	         }
	      }
	      // Once everything is complete, delete old file..
	      File oldFile = new File(oldFileName);
	      oldFile.delete();

	      // And rename tmp file's name to old file name
	      File newFile = new File(tmpFileName);
	      newFile.renameTo(oldFile);
	   }
	

}
