package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.Company;
import applicationLogic.CompanyPlant;
import dataStorageAccess.controller.CompanyController;

/**
 * @author Niklas Schnettler
 *
 */
public class CompanyAccess {
	/**
	 * Get a List of Companies
	 * 
	 * @param needsDefect - wether only companies which had atleast one defect should be returned
	 * @return a List of Companies
	 * @throws SQLException
	 */
	public static ArrayList<Company> getCompanies(boolean needsDefect) throws SQLException {
		if (needsDefect) {
			return CompanyController.getCompaniesWithDefect();
		} else {
			return CompanyController.getCompanies();
		}
	}
	
	/**
	 * Get a List of Plants of a given company
	 * 
	 * @param company - A company
	 * @return A List of CompanyPlants
	 * @throws SQLException
	 */
	public static ArrayList<CompanyPlant> getPlantsOfCompany(Company company) throws SQLException {
		return CompanyController.getPlantsOfcompany(company);
	}
	
	/**
	 * Get a List of all Plants
	 * 
	 * @return A List of CompanyPlants
	 * @throws SQLException
	 */
	public static ArrayList<CompanyPlant> getCompanyPlants() throws SQLException {
		return CompanyController.getCompanyPlants();
	}
	
	/**
	 * Inserts a new company into the Database
	 * 
	 * @param company - A Company
	 * @throws SQLException
	 */
	public static int insertCompany(Company company) throws SQLException {
		return CompanyController.insertCompany(company);
	}
	
	/**
	 * Inserts a new Company Plant into the Database
	 * 
	 * @param companyPlant - A Company Plant
	 * @throws SQLException
	 */
	public static int insertCompanyPlant (CompanyPlant companyPlant) throws SQLException {
		return CompanyController.insertCompanyPlant(companyPlant);
	}
}