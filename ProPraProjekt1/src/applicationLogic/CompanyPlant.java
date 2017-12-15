package applicationLogic;

/**
 * @author Niklas Schnettler
 *
 */
public class CompanyPlant extends AutocompleteSuggestion{
	private int id;
	private String plantStreet;
	private int plantZip;
	private String plantCity;
	private Company company;
	
	public CompanyPlant(int id, String plantStreet, int plantZip, String plantCity, Company company) {
		super(id, plantStreet + ", " +plantZip + " " + plantCity);
		this.id = id;
		this.plantStreet = plantStreet;
		this.plantZip = plantZip;
		this.company = company;
		this.plantCity = plantCity;
	}

	public int getId() {
		return id;
	}

	public Company getCompany() {
		return company;
	}

	public String getPlantStreet() {
		return plantStreet;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlantZip() {
		return plantZip;
	}
	
	public String getPlantCity() {
		return plantCity;
	}
	
	public String getZipCity() {
		return plantZip + " " + plantCity;
	}
}