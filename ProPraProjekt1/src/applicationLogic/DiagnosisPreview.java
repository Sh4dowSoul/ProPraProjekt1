package applicationLogic;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Diagnosis
 * 
 * @author Niklas Schnettler
 */

public class DiagnosisPreview {

	private int id;
	private Date date;
	private int companyId;
	private String companyName;
	
	public DiagnosisPreview(int diagnosis_id, Date diagnosis_date, int company_id, String company_name) {
		this.id = diagnosis_id;
		this.date = diagnosis_date;
		this.companyId = company_id;
		this.companyName = company_name;
	}

	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public int getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	public String getNiceDate() {
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/YYYY");
		return ft.format(date);
	}

}
