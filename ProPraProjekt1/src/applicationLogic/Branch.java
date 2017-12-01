package applicationLogic;

/**
 * The Class Branch
 * 
 * @author name
 */

public class Branch {

	private int id;
	private String description;

	public Branch(int id, String description) {
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
	public String toString() { 
		return description;
	}

}
