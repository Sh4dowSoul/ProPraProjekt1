package applicationLogic;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * The Class Defect
 * 
 * @author Niklas Schnettler
 */

public class Flaw extends AutocompleteSuggestion {
	@XStreamOmitField
	private boolean isCustomFlaw;
	
	public Flaw(int externalFlawId,  int internalFlawId, boolean isCustomFlaw, String flawDescription) {
		super(externalFlawId, internalFlawId, flawDescription);
		this.isCustomFlaw = isCustomFlaw;
	}
	
	public Flaw(int externalFlawId, boolean isCustomFlaw, String flawDescription) {
		super(externalFlawId, 0, flawDescription);
		this.isCustomFlaw = isCustomFlaw;
	}

	public boolean isCustomFlaw() {
		return isCustomFlaw;
	}
}