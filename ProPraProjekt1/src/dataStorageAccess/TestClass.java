package dataStorageAccess;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class TestClass {
	public static void main(String[] args) {
		try {
			StatisticAccess.exportStatisticCompany(1);
		} catch (FileNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		
			
			/*
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
			
			//Get companies
			ArrayList<Company> AllCompanies = CompanyController.getCompanies();
			System.out.println("\n");
			for (Company company: AllCompanies) {
				System.out.println("COMPANY " +company.getName() + " " + company.getHqStreet());
				ArrayList<CompanyPlant> plants = CompanyController.getPlantsOfcompany(company.getId());
				for (CompanyPlant plant : plants) {
					System.out.println("PLANT " + plant.getPlantStreet());
				}
			}
			
			//Get statistic by company
			ArrayList<StatisticElement> statistics = StatisticController.getMostFrequentDefectCompany(1);
			System.out.println("\n");
			for (StatisticElement stat: statistics) {
				System.out.println("Stat " + stat.getDefectId() + " " + stat.getNumberOccurrence());
			}
			
			//Get statistic by branch
			ArrayList<StatisticElement> statisticsBranch = StatisticController.getMostFrequentDefectBranch(701);
			System.out.println("\n");
			for (StatisticElement stat: statisticsBranch) {
				System.out.println("Stat in Branch 701 " + stat.getDefectId() + " " + stat.getNumberOccurrence());
			}
			
			//Insert Diagnosis
			DiagnosisController.insertDiagnosis(test); */
			
			
			
		//} catch (SQLException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}
}
