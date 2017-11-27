package applicationLogic;

public class CompanyPlant {
	private int id;
	private int companyId;
	private String plantStreet;
	private String plantZip;
	
	public CompanyPlant(int id, int companyId, String plantStreet, String plantZip) {
		this.id = id;
		this.companyId = companyId;
		this.plantStreet = plantStreet;
		this.plantZip = plantZip;
	}

	public int getId() {
		return id;
	}

	public int getCompanyId() {
		return companyId;
	}

	public String getPlantStreet() {
		return plantStreet;
	}

	public String getPlantZip() {
		return plantZip;
	}

}
