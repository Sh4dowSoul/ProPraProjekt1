package applicationLogic;

import de.schnettler.AutocompleteSuggestion;

public class CompanyPlant extends AutocompleteSuggestion {

	private String street;
	private int zipCode;
	private String city;
	private Company company;
	
	public CompanyPlant(int internalId, String street, int zipCode, String city, Company company) {
		super(internalId, internalId, street + ", " +zipCode + " " + city);
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
		this.company = company;
	}

	public String getStreet() {
		return street;
	}

	public int getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}
	
	public Company getCompany() {
		return company;
	}

}