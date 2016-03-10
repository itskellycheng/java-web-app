package scale;

/* JSPHDEV Project 1
 * EditOption extends Thread. There are two constructors where you can pass in 
 * the stuff you want to edit. You can either edit the options name or 
 * an option’s price, and  EditOption will differentiate between what you want 
 * to edit by which constructor you call.
 * EditOptions edits by accessing an automobile object through BuildAuto.
 * An instance of BuildAuto is instantiated and through that, the LHM of 
 * automobiles can be searched, and once the correct automobile is found, 
 * proxyAuto class will save it to the static automobile instance, on which
 * update methods will be called.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import adapter.BuildAuto;
import adapter.ProxyAutomobile;

public class EditOptions extends Thread{
	private String autoName;
	private String opsetName;
	private String optionName;
	private String newName;
	private float newPrice;
	private BuildAuto someAuto;
	
	public EditOptions() {}
	//version 1
	public EditOptions(String autoToEdit, String opsetToEdit, String newName) {
		autoName = autoToEdit;
		opsetName = opsetToEdit;
		this.newName = newName;
		optionName = null;
	}
	//version 2
	public EditOptions(String autoToEdit, String opsetToEdit, 
			String optionToEdit, float newPrice) {
		autoName = autoToEdit;
		opsetName = opsetToEdit;
		optionName = optionToEdit;
		this.newPrice = newPrice;
		this.newName = null;
	}
	
	@Override
	public void run() {
		someAuto = new BuildAuto();
		if (optionName == null)
			someAuto.updateOptionSetName(autoName, opsetName, newName);
		else if (newName == null)
			someAuto.updateOptionPrice(autoName, opsetName, optionName, newPrice);
	}
}
