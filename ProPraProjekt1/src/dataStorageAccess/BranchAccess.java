package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.Branch;
import dataStorageAccess.controller.BranchController;

public class BranchAccess {
	/**
	 * Get a List of Branches
	 * 
	 * @param needsDefect - wether only Branches which had atleast one defect should be returned
	 * @return a List of Branches
	 * @throws SQLException
	 */
	public static ArrayList<Branch> getBranches(boolean needsDefect) throws SQLException {
		if (needsDefect) {
			return BranchController.getAllBranchesWithDefect();
		} else {
			return BranchController.getAllBranches();
		}
	}
}
