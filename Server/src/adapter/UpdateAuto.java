package adapter;

public interface UpdateAuto {
	/* Searches the Model for a given OptionSet and sets the name of OptionSet 
	 * to newName */
	public void updateOptionSetName(String modelName, 
			String optionSetName, String newName);
	/* searches the Model for a given OptionSet and Option name, and sets the 
	 * price to newPrice */
	public void updateOptionPrice(String modelName, String optionSet, String
			option, float newPrice);
	/* searches the DB for a given OptionSet and Option name, and sets the 
	 * price to newPrice */
	public void updateOptionPriceDB(String modelName, String optionSet, String
			option, float newPrice);
	/* Given key name, deletes entry from LHM */
	public void delete(String name);
	/* Given key name, deletes entry from DB */
	public void deleteDB(String name);
}
