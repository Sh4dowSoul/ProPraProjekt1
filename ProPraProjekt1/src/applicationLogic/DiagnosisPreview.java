package applicationLogic;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * The Class Diagnosis
 * 
 * @author Niklas Schnettler
 */

public class DiagnosisPreview {

	@XStreamAlias("ESNummer")
	private int id;
	@XStreamAlias("datei")
	private String fileName = "TODO";
	@XStreamAlias("Jahr")
	private String year;
	@XStreamAlias("GZA")
	private String gza ="TODO";
	@XStreamAlias("Branche")
	private String branch ="TODO";
	@XStreamOmitField
	private Date date;
	@XStreamOmitField
	private int companyId;
	@XStreamOmitField
	private String companyName;
	
	public DiagnosisPreview(int diagnosis_id, Date diagnosis_date, int company_id, String company_name) {
		this.id = diagnosis_id;
		this.date = diagnosis_date;
		this.companyId = company_id;
		this.companyName = company_name;
		this.year = new SimpleDateFormat("YYYY").format(date);
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
