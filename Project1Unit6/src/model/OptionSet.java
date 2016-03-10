package model;

/* JSPHDEV Project 1 - Unit 1
 * The OptionSet class details what an optionset contains: name of the optionset
 * and an array of options.
 * Options are an inner class of optionset.
 * 
 * @author Kelly Cheng 
 * Andrew ID: kuangchc
 */

public class OptionSet implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Option opt[];
	private Option choice;
	
	/* Constructors */
	protected OptionSet() {}
	protected OptionSet(String n, int size) {
		name = n;
		opt = new Option[size];
		for(int i=0;i<size;i++) //initialize array
			opt[i] = new Option();
		choice = opt[0]; //default choice 
	}
	
	/* Get optionset name */
	protected String getName() {
		return name;
	}
	/* Get number of options in optionset */
	protected int getSize() {
		return opt.length;
	}
	/* Get option name with option index*/
	protected String getOpName(int index) {
		return opt[index].name;
	}
	/* Get option price with option index */
	protected float getOpPrice(int index) {
		return opt[index].price;
	}
	/* Get the chosen option for this optionset*/
	protected Option getOptionChoice() {
		return choice;
	}
	/* Get the name of chosen option for this optionset*/
	protected String getOptionChoiceName() {
		return choice.getName();
	}
	/* Set optionset name*/
	protected void setName(String n) {
		name = n;
	}
	/* Set option name and price with option index */
	protected void setOption(int index, String n, float p) {
		opt[index].name = n;
		opt[index].price = p;
	}
	/* Set option name with option index */
	protected void setOpName(int index, String n) {
		opt[index].name = n;
	}
	/* Given the name of an option, save that choice */
	protected void setOptionChoice(String optionName) {
		for(int i=0;i<opt.length;i++) {
			if (optionName.equals(opt[i].name)) 
				choice = opt[i];
		}
	}
	/* Deletes option of name n by setting it to null. */
	protected void deleteOption(String n) {
		for(int i=0;i<opt.length;i++) {
			if (n.equals(opt[i].name)) 
				opt[i] = null;
		}
	}
	/* Prints out all the option names and prices */
	protected void printOptions() {
		for (int j=0;j<opt.length;j++) {
			if (opt[j] == null)
				System.out.printf("-deleted option-\n");
			else {
				System.out.printf("%s  ", opt[j].name);
				System.out.printf("$%.2f\n", opt[j].price);
			}
		}
	}

	/* Option class - inner class. Stores the option name and the price */
	protected class Option implements java.io.Serializable {
		private String name;
		private float price;
		
		/* Constructors */
		Option() {}
		Option(String n, float p) {
			name = n;
			price = p;
		}
		/* Get option name */
		protected String getName() {
			return name;
		}
		/* Set option name */
		protected float getPrice() {
			return price;
		}
		/* Set option name */
		protected void setName(String name) {
			this.name = name;
		}
		/* Set option price */
		protected void setPrice(float price) {
			this.price = price;
		}
		
		
	}
	


}
