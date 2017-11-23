package applicationLogic;

/**
 * The Class DefectList
 * 
 * @author name
 */

public class DefectList {

	private int id;
	private int diagnosisId;
	
	public DefectList(int id, int diagnosisId) {
		this.id = id;
		this.diagnosisId = diagnosisId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(int diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

}
