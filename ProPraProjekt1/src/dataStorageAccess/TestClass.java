package dataStorageAccess;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import applicationLogic.Branch;
import applicationLogic.DiagnosisPreview;
import dataStorageAccess.controller.BranchController;
import dataStorageAccess.controller.DiagnosisController;

public class TestClass {
	public static void main(String[] args) {
		try {
			//Get Branches starting with 'TA'
			ArrayList<Branch> branches = BranchController.filterBranch("Ta");
			for (Branch branch: branches) {
				System.out.println(branch.getDescription());
				System.out.println(branch.getId());
			}
			//Get Preview of 3 last edited Diagnoses
			ArrayList<DiagnosisPreview> previews = DiagnosisController.getLastEditedDiagnosesPreview(3);
			for (DiagnosisPreview preview: previews) {
				System.out.println(preview.getNiceDiagnosis_date() + " - " + preview.getCompany_name());
			}
			//Get Preview of all Diagnoses
			ArrayList<DiagnosisPreview> Allpreviews = DiagnosisController.getDiagnosesPreview();
			for (DiagnosisPreview preview: Allpreviews) {
				System.out.println(preview.getCompany_name() + " - " + preview.getNiceDiagnosis_date());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
