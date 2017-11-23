package dataStorageAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.Branch;

public class TestClass {
	public static void main(String[] args) {
		try {
			ArrayList<Branch> branches = BranchController.filterBranch("Ta");
			for (Branch branch: branches) {
				System.out.println(branch.getDescription());
				System.out.println(branch.getId());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
