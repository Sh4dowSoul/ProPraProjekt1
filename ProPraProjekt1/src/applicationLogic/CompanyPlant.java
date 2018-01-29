package applicationLogic;

/**
 * @author Niklas Schnettler
 *
 */
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

	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyPlant other = (CompanyPlant) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zipCode != other.zipCode)
			return false;
		return true;
	}
}