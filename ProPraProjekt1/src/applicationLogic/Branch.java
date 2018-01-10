package applicationLogic;

import de.schnettler.AutocompleteSuggestion;

public class Branch extends AutocompleteSuggestion {

	public Branch(int externalId, String description) {
		super(externalId, externalId, description);
	}
}
