package dataStorageAccess;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import applicationLogic.Branch;
import applicationLogic.Diagnosis;
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
				System.out.println(preview.getNiceDate() + " - " + preview.getCompanyName());
			}
			//Get Preview of all Diagnoses
			ArrayList<DiagnosisPreview> Allpreviews = DiagnosisController.getDiagnosesPreview();
			for (DiagnosisPreview preview: Allpreviews) {
				System.out.println(preview.getCompanyName() + " - " + preview.getNiceDate());
			}
			//Get Diagnosis
			Diagnosis test = DiagnosisController.getDiagnosis(1);
			System.out.println("Diagnois : " + test.isExaminationResultNoDefect());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
