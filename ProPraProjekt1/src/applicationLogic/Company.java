package applicationLogic;

import java.util.ArrayList;

import de.schnettler.AutocompleteSuggestion;

public class Company extends AutocompleteSuggestion{

	private String street;
	private int zipCode;
	private String city;
	
	
	public Company(int internalId, String name, String street, int zipCode, String city) {
		super(internalId, internalId, name);
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
	}
	
	//Dummy Company
	public Company(int internalId, String name) {
		super(internalId, internalId, name);
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
}
