package dataStorageAccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

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
	 * @param needsDefect - Whether only Branches which had at least one defect should be returned
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
		ClassLoader classLoader = BranchAccess.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("resources/Branches.xml");
        return (ArrayList<Branch>) xs.fromXML(is);
	}
}
