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
}