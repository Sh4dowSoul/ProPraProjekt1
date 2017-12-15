package applicationLogic;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author Niklas Schnettler
 *
 */
public class AutocompleteSuggestion implements Comparable<AutocompleteSuggestion> {
	@XStreamOmitField
	private int id;
	private String description;

	public AutocompleteSuggestion(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int compareTo(AutocompleteSuggestion arg0) {
		return this.getDescription().compareTo(arg0.getDescription());

	}
}
