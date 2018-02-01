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
	@XStreamOmitField
	private boolean dontShowAsSuggestion;
	
	public Flaw(int externalFlawId,  int internalFlawId, boolean isCustomFlaw, String flawDescription, boolean dontShowAsSuggestion) {
		super(externalFlawId, internalFlawId, flawDescription);
		this.isCustomFlaw = isCustomFlaw;
	}
	
	public Flaw(int externalFlawId, boolean isCustomFlaw, String flawDescription) {
		super(externalFlawId, flawDescription);
		this.isCustomFlaw = isCustomFlaw;
	}
	
	public Flaw(int externalFlawId, String flawDescription) {
		super(externalFlawId, flawDescription);
	}

	public boolean isCustomFlaw() {
		return isCustomFlaw;
	}

	public boolean isDontShowAsSuggestion() {
		return dontShowAsSuggestion;
	}

	public void setDontShowAsSuggestion(boolean dontShowAsSuggestion) {
		this.dontShowAsSuggestion = dontShowAsSuggestion;
	}
}