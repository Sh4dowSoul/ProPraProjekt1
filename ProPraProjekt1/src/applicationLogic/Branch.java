package applicationLogic;


public class Branch extends AutocompleteSuggestion {
	
	private boolean isDummy;

	public Branch(int externalId, String description) {
		super(externalId, externalId, description);
	}
	
	public Branch(int externalId) {
		super(externalId, 0, null);
	}
	
	public Branch(Integer id) {
		super(id, 0, null);
	}
	
	public Branch(String description) {
		super(0,0, description);
		this.isDummy = true;
	}
	
	public boolean isDummy() {
		return isDummy;
	}
}