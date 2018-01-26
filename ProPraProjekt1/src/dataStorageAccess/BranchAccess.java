package dataStorageAccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import applicationLogic.Branch;
import dataStorageAccess.controller.BranchController;

/**
 * @author Niklas Schnettler
 *
 */
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
	
	public static ArrayList <Branch> importBranchesFromXml() throws FileNotFoundException {
		XStream xs = new XStream(new DomDriver());
        FileInputStream fis = new FileInputStream("ressources/Branches.xml");
        return (ArrayList<Branch>) xs.fromXML(fis);
	}
}
