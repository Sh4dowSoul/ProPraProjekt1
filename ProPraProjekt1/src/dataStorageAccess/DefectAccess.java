package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.DefectAtomic;
import applicationLogic.DefectResult;
import applicationLogic.DefectStatistic;
import dataStorageAccess.controller.DefectController;
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
	public static ArrayList<DefectStatistic> getFrequentDefectsCompany(boolean singleCompany, int companyId) throws SQLException{
		if (singleCompany) {
			return StatisticController.getMostFrequentDefectCompany(companyId);
		} else {
			return StatisticController.getMostFrequentDefectAllCompanies();
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
	public static ArrayList<DefectStatistic> getFrequentDefectsBranch(boolean singleBranch, int branchId) throws SQLException{
		if (singleBranch) {
			return StatisticController.getMostFrequentDefectBranch(branchId);
		} else {
			return StatisticController.getMostFrequentDefectAllBranches();
		}
	}
	
	/**
	 * Get all Defects (DefectStatistic - for Statistic purposes) of an Inspection Result
	 * 
	 * @param inspectionResultId
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<DefectStatistic> getDefectsForStatistic(int inspectionResultId) throws SQLException{
		return StatisticController.getDefectsOfDiagnosis(inspectionResultId);
	}
	
	/**
	 * Get a List of all Defects (id, description)
	 * 
	 * @return a List of DefectAtomics
	 * @throws SQLException
	 */
	public static ArrayList<DefectAtomic> getDefects() throws SQLException{
		return DefectController.getAllDefects();
	}
	
	/**
	 * Save a DefectListElement in the Database
	 * 
	 * @param defect - a defect from a defectlist (of an inspectionResult)
	 * @param diagnosisId - the id of the inspectionResult
	 * @throws SQLException
	 */
	public static void insertDefect(DefectResult defect, int diagnosisId) throws SQLException {
		DefectController.insertDefect(defect, diagnosisId);
	}
}