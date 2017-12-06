package applicationLogic;

public class CompanyPlant {
	private int id;
	private String plantStreet;
	private String plantZip;
	private Company company;
	
	public CompanyPlant(int id, String plantStreet, String plantZip, Company company) {
		this.id = id;
		this.plantStreet = plantStreet;
		this.plantZip = plantZip;
		this.company = company;
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

	public String getPlantZip() {
		return plantZip;
	}

}
