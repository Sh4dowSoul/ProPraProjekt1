package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.FlawStatistic;
import dataStorageAccess.controller.StatisticController;

/**
 * @author Niklas Schnettler
 *
 */
public class DefectAccess {
	/**
	 * Get the most frequent defects of a Company / all Companies
	 * 
	 * @param singleCompany - Weather you want the defects of a) one or b) all companies
	 * @param companyId - the id off a company (in case of a)) 
	 * @return A List of the most frequent defects
	 * @throws SQLException
	 */
	public static ArrayList<FlawStatistic> getFrequentDefectsCompany(boolean singleCompany, int companyId) throws SQLException{
		if (singleCompany) {
			return StatisticController.getMostFrequentDefectCompany(companyId);
		} else {
			return StatisticController.getMostFrequentDefects();
		}
	}
	
	/**
	 * Get the most frequent defects of a Branch / all Branches
	 * 
	 * @param singleCompany - Weather you want the defects of a) one or b) all companies
	 * @param companyId - the id off a company (in case of a)) 
	 * @return A List of the most frequent defects
	 * @throws SQLException
	 */
	public static ArrayList<FlawStatistic> getFrequentDefectsBranch(int branchId) throws SQLException{
		return StatisticController.getMostFrequentDefectBranch(branchId);
	}
}