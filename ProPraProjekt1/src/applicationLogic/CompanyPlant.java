package applicationLogic;

import de.schnettler.AutocompleteSuggestion;

public class CompanyPlant extends AutocompleteSuggestion {

	private String street;
	private int zipCode;
	private String city;
	private Company company;
	
	/*
	 * Id Guide:
	 *  0: CompanyPlant complete, not saved in Database yet
	 * -1: Dummy CompanyPlant, CompanyPlant just created, missing Data
	 *  else: CompanyPlant from Database
	 */
	
	public CompanyPlant(int internalId, String street, int zipCode, String city, Company company) {
		super(internalId, internalId, street + ", " +zipCode + " " + city);
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
		this.company = company;
	}
	
	public CompanyPlant(int internalId, String street, Company company) {
		super(internalId, internalId, street);
		this.street = street;
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

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

}