package applicationLogic;

/**
 * The Class Company
 * 
 * @author name
 */

public class Company {

	private int id;
	private String name;
	private String hqStreet;
	private String hqZip;
	
	public Company(int id, String name, String hqStreet, String hqZip) {
		this.id = id;
		this.name = name;
		this.hqStreet = hqStreet;
		this.hqZip = hqZip;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getHqStreet() {
		return hqStreet;
	}

	public String getHqZip() {
		return hqZip;
	}
}
