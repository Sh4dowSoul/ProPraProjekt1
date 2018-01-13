package applicationLogic;

import de.schnettler.AutocompleteSuggestion;

public class Branch extends AutocompleteSuggestion {

	public Branch(int externalId, String description) {
		super(externalId, externalId, description);
	}
	
	public Branch(int externalId) {
		super(externalId, 0, null);
	}
	
	public Branch(Integer id) {
		super(id, 0, null);
	}
}
