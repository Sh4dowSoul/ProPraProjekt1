package applicationLogic;

/**
 * @author Niklas Schnettler
 * 
 * Class used for AutoCompleteSuggestions (external Library)
 *
 */
public class AutocompleteSuggestion implements Comparable<AutocompleteSuggestion>, de.schnettler.AutocompleteSuggestion {
	
	private Integer externalId;
	private int internalId;
	private String description;

	public AutocompleteSuggestion(Integer externalId, int internalId, String description) {
		this.externalId = externalId;
		this.internalId = internalId;
		this.description = description;
	}

	public Integer getExternalId() {
		return externalId;
	}

	public int getInternalId() {
		return internalId;
	}

	public String getDescription() {
		return description;
	}


	public void setInternalId(int internalId) {
		this.internalId = internalId;
	}

	@Override
	public int compareTo(AutocompleteSuggestion arg0) {
		return this.getDescription().compareTo(arg0.getDescription());

	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AutocompleteSuggestion other = (AutocompleteSuggestion) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (externalId == null) {
			if (other.externalId != null)
				return false;
		} else if (!externalId.equals(other.externalId))
			return false;
		if (internalId != other.internalId)
			return false;
		return true;
	}
}
