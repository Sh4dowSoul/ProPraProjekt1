package applicationLogic;

/**
 * The Class Company
 * 
 * @author Niklas Schnettler
 */

public class Company  extends AutocompleteSuggestion{

	private int id;
	private String name;
	private String hqStreet;
	private int hqZip;
	private String hqCity;
	
	public Company(int id, String name, String hqStreet, int hqZip, String hqCity) {
		super(id, name);
		this.id = id;
		this.name = name;
		this.hqStreet = hqStreet;
		this.hqZip = hqZip;
		this.hqCity = hqCity;
	}
	
	public Company(int id, String name) {
		super(id, name);
		this.id = id;
		this.name = name;
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

	public int getHqZip() {
		return hqZip;
	}
	
	public String getHqCity() {
		return hqCity;
	}
	
	public String getZipCity() {
		return hqZip + " " + hqCity;
	}
}