package dataStorageAccess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
	
	/**
	 * 
	 * Get a List of all custom (created by the user) Flaws
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Flaw> getCustomFlaws() throws SQLException{
		return FlawController.getCustomFlaws();
	}
	
	/**
	 * Get the Description(s) of an External FlawId (can be multiple)
	 * 
	 * @param externalFlawId
	 * @return A List of Descriptions
	 * @throws SQLException
	 */
	public static ArrayList<String> getFlawDescriptions(int externalFlawId) throws SQLException {
		return FlawController.getFlawDescription(externalFlawId);
	}
	
	/**
	 * Insert Custom Flaw into Database
	 * 
	 * @param flaw - A custom Flaw
	 * @return The new Id (in DataBase) of the Flaw
	 * @throws SQLException
	 */
	public static int insertCustomFlaw(Flaw flaw) throws SQLException{
		return FlawController.insertCustomFlaw(flaw);
	}
	
	/**
	 * Exports all custom Flaws to CSV
	 * 
	 * @param file - Where it should be saved
	 * @throws SQLException
	 * @throws IOException
	 */
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
	
	/**
	 * Imports the default Flaws from XML
	 * 
	 * @return A List of Imported Flaws
	 * @throws FileNotFoundException
	 */
	public static ArrayList<Flaw> importFlawsFromXml() throws FileNotFoundException {
		XStream xs = new XStream(new DomDriver());
        FileInputStream fis = new FileInputStream("ressources/Flaws.xml");
        return (ArrayList<Flaw>) xs.fromXML(fis);
	}
}