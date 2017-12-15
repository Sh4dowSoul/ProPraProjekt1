package applicationLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Short preview of an Result, used for main resultList
 * 
 * @author Niklas Schnettler
 *
 */
public class ResultPreview extends ResultBase{
	
	@XStreamOmitField
	private int companyId;
	@XStreamOmitField
	private String companyName;
	@XStreamOmitField
	private LocalDate lastEdited;
	
	public ResultPreview(int id, LocalDate date, int companyId, String companyName, LocalDate lastEdited) {
		super(id, date);
		this.companyId = companyId;
		this.companyName = companyName;
		this.lastEdited = lastEdited;
	}

	public int getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	public String getLastEditedNice() {
		return lastEdited.format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
	}
}
