package driver;

/* JSPHDEV Project 1
 * Driver - tests database operations with Toyota Prius
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import adapter.BuildAuto;
import database.CreateDatabase;
import exception.AutoException;
import server.Server;
import java.io.IOException;

public class Driver {
	public static void main(String[] args) {
		//Create database
		CreateDatabase cdb = new CreateDatabase();
		cdb.connect();
		cdb.create();
		cdb.closeConn();
		
		//Populate database
		BuildAuto myCar = new BuildAuto();
		try {
			myCar.buildAuto("Toyota Prius.txt", 0);
			myCar.buildAuto("Ford_Focus_Wagon_ZTW.txt", 0);
		} catch (AutoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Update database (updating option price as an example)
		myCar.updateOptionPriceDB("Toyota Prius", "Brakes", "ABS", 350);
		
		//Delete from database
		myCar.delete("Toyota Prius");
		
    }
	
}
