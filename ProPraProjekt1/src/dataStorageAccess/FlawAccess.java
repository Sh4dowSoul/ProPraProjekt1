package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.Flaw;
import dataStorageAccess.controller.FlawController;

public class FlawAccess {
	/**
	 * Get a List of all Flaws
	 * 
	 * @return a List of DefectAtomics
	 * @throws SQLException
	 */
	public static ArrayList<Flaw> getAllFlaws() throws SQLException{
		return FlawController.getAllFlaws();
	}
	
	public static ArrayList<String> getFlawDescriptions(int externalFlawId) throws SQLException {
		return FlawController.getFlawDescription(externalFlawId);
	}
	
	public static int insertCustomFlaw(Flaw flaw) throws SQLException{
		return FlawController.insertCustomFlaw(flaw);
	}
}