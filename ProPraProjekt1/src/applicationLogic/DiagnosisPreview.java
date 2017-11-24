package applicationLogic;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Diagnosis
 * 
 * @author name
 */

public class DiagnosisPreview {

	private int diagnosis_id;
	private Date diagnosis_date;
	private int company_id;
	private String company_name;
	
	public DiagnosisPreview(int diagnosis_id, Date diagnosis_date, int company_id, String company_name) {
		this.diagnosis_id = diagnosis_id;
		this.diagnosis_date = diagnosis_date;
		this.company_id = company_id;
		this.company_name = company_name;
	}

	public int getDiagnosis_id() {
		return diagnosis_id;
	}

	public Date getDiagnosis_date() {
		return diagnosis_date;
	}

	public int getCompany_id() {
		return company_id;
	}

	public String getCompany_name() {
		return company_name;
	}
	
	public String getNiceDiagnosis_date() {
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/YYYY");
		return ft.format(diagnosis_date);
	}


}
