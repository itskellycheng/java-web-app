package model;

/* JSPHDEV Project 1 - Client
 * The Automotive class holds the name of the model, base price and an array of
 * optionsets.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

import java.util.Iterator;
import java.util.List;

import exception.AutoException;
import java.util.ArrayList;

public class Automobile implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name = null;
	private String make = null;
	private String model = null;
	private float basePrice = 0;
	private ArrayList<OptionSet> opset;
	
	/* Constructors */
	public Automobile() {
		opset = new ArrayList<OptionSet>();
	}
	public Automobile(String name, float baseprice, int size) {
		this.name = name;
		this.basePrice = baseprice;
		opset = new ArrayList<OptionSet>(size);
		for (int i = 0; i < size; i++) {
			opset.add(new OptionSet());
		}
		// If no make and model name passed in, use full name
		this.make = name;
		this.model = name;
	}
	public Automobile(String name, String make, String model, 
			          float baseprice, int size) {
		this.name = name;
		this.make = make;
		this.model = model;
		this.basePrice = baseprice;
		opset = new ArrayList<OptionSet>(size);
		for (int i = 0; i < size; i++) {
			opset.add(new OptionSet());
		}
	}
	
	/* Get the automobile's full name */
	public String getName() {
		return name;
	}
	/* Get the automobile's make */
	public String getMake() {
		return make;
	}
	/* Get the automobile's model */
	public String getModel() {
		return model;
	}
	/* Get the base price */
	public float getBasePrice() {
		return basePrice;
	}
	/* Get optionset with index number */
	public OptionSet getOpSet(int index) {
		return opset.get(index);
	}
	/* Get list (in String) of option sets */
	public ArrayList<String> getOpSetList() {
		ArrayList<String> myList = new ArrayList<String>();
		for (int i = 0; i < opset.size(); i++) {
			myList.add(getOpSet(i).getName());
		}
		return myList;
	}
	/* Get list (in String) of available options in opset with opset index number */
	public ArrayList<String> getOptionList(int opSetIndex) {
		OptionSet o = getOpSet(opSetIndex);
		ArrayList<String> myList = new ArrayList<String>();
		for (int i = 0; i < o.getSize(); i++) {
			myList.add(o.getOpName(i));
		}
		return myList;
	}
	/* Get name of current option choice */
	public String getOptionChoice(String opsetName) throws AutoException {
		OptionSet o = findOpSet(opsetName);
		return o.getOptionChoice().getName();
	}
	/* Get what the chosen option will cost for specified optionset */
	public float getOptionChoicePrice(String opsetName) throws AutoException {
		OptionSet o = findOpSet(opsetName);
		System.out.printf("Chose:%s, which costs %f", o.getOptionChoiceName(), o.getOptionChoice().getPrice());
		return o.getOptionChoice().getPrice();
	}
	/* Get total price */
	public float getTotalPrice() {
		float sum = 0;
		Iterator<OptionSet> iterator = null;
	    iterator = opset.iterator();
	    while (iterator.hasNext()) {
	      OptionSet thisOpSet = iterator.next();
	      sum += thisOpSet.getOptionChoice().getPrice();
	    }
	    sum += basePrice;
	    return sum;
	}
	/* Set name */
	public void setName(String n) {
		name = n;
	}
	/* Set make */
	public void setMake(String n) {
		make = n;
	}
	/* Set model */
	public void setModel(String n) {
		model = n;
	}
	/* Set base price */
	public void setBasePrice(float price) {
		basePrice = price;
	}
	/* Set name of an optionset with known index */
	public void setOpSet(int index, String newName, int size) {
		OptionSet toSet = new OptionSet(newName, size);
		opset.set(index, toSet);
	}
	/* Set name and price of option[index] in optionset known by name */
	public void setOption(String opsetName, int optionIdx, String optionName, float p) throws AutoException {
		OptionSet o = findOpSet(opsetName);
		o.setOption(optionIdx, optionName, p);
	}
	/* Set name and price of option with known index in known optionset */
	public void setOption(OptionSet ops, int opIdx, String opName, float p) {
		ops.setOption(opIdx, opName, p);
	}
	/* Set option choice of optionset */
	public void setOptionChoice(String opsetName, String optionName) throws AutoException {
		OptionSet o = findOpSet(opsetName);
		o.setOptionChoice(optionName);
	}
	
	/* Find optionset with the name n. Returns the optionset if found and
	 *  null if not found.*/
	public OptionSet findOpSet(String n) throws AutoException {
		Iterator<OptionSet> iterator = null;
	    iterator = opset.iterator();
	    while (iterator.hasNext()) {
	      OptionSet thisOpSet = iterator.next();
	      if (n.equals(thisOpSet.getName())) {
	    	  return thisOpSet;
	      }
	    }
		System.out.printf("Could not find Optionset: %s\n", n);
		throw new AutoException(4, "findOpSet failed.");
	}
	/* Find option with the name m in the optionset n. Returns the index of 
	 * option if found, and -1 if not found. */
	public int findOption(String n, String m) throws AutoException {
		OptionSet o = findOpSet(n);
		for (int i=0;i<o.getSize();i++) {
			if (m.equals(o.getOpName(i)))
				return i;
		}
		System.out.printf("Option %s not found.\n", m);
		throw new AutoException(4, "findOption failed.");
	}
	/* Deletes optionset of name n by setting it to null. */
	public void deleteOpSet(String n) throws AutoException {
		OptionSet toDelete = findOpSet(n);
		toDelete = null;
	}
	/* Deletes option of name m in optionset n by setting it to null. */
	public void deleteOption(String n, String m) throws AutoException {
		OptionSet o = findOpSet(n);
		o.deleteOption(m);
	}
	/* Find optionset n and update optionset name */
	public void updateOpSet(String origName, String newName) throws AutoException {
		OptionSet o = findOpSet(origName);
		o.setName(newName);
	}
	/* Find option m in optionset n and update name and price for that option */
	public void updateOption(String n, String m, String newm, float newprice) throws AutoException {
		int index = findOption(n, m);
		OptionSet o = findOpSet(n);
		o.setOption(index, newm, newprice);
	}
	/* print - prints out all model information, including model name, base
	 * price, optionsets, options and price of options */
	public void print() {
		System.out.printf("-----Automotive-----\n");
		System.out.printf("%s\n", name);
		System.out.printf("Make:%s\n", make);
		System.out.printf("Model:%s\n", model);
		System.out.printf("Base price:%.2f\n", basePrice);
		
		Iterator<OptionSet> iterator = null;
	    iterator = opset.iterator();
	    while (iterator.hasNext()) {
	      OptionSet thisOpSet = iterator.next();
	      System.out.printf("%s:\n", thisOpSet.getName());
	      for (int i=0; i<thisOpSet.getSize(); i++) {
	    	  System.out.printf("%s, %.2f\n", thisOpSet.getOpName(i), thisOpSet.getOpPrice(i));
	      }
	    }
	
	}
	/* printm - wrapper function for print(). Does the same thing as print(),
	 * but prints a message first. */
	public void printm(String msg) {
		System.out.printf("\n%s\n", msg);
		print();
	}


}
