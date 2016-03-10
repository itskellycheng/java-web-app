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
	/* saveOptionChoice - Saves which option was chosen */
	public void saveOptionChoice(String modelName, String opSet, String optionChoice);
	
}
