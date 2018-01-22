package dataStorageAccess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
	
	public static ArrayList<Flaw> getCustomFlaws() throws SQLException{
		return FlawController.getCustomFlaws();
	}
	
	public static ArrayList<String> getFlawDescriptions(int externalFlawId) throws SQLException {
		return FlawController.getFlawDescription(externalFlawId);
	}
	
	public static int insertCustomFlaw(Flaw flaw) throws SQLException{
		return FlawController.insertCustomFlaw(flaw);
	}
	
	public static void exportFlawToCsv(File file) throws SQLException, IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		for (Flaw flaw : FlawAccess.getCustomFlaws()){
			StringBuffer line = new StringBuffer();
			line.append(flaw.getExternalId());
			line.append(";");
			line.append("\""+ flaw.getDescription() +"\"");
			bw.write(line.toString());
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
}