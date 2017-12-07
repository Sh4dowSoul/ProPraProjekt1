package applicationLogic;

import java.time.LocalDate;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class ResultPreview extends ResultBase{
	
	@XStreamOmitField
	private int companyId;
	@XStreamOmitField
	private String companyName;
	
	public ResultPreview(int id, LocalDate date, int companyId, String companyName) {
		super(id, date);
		this.companyId = companyId;
		this.companyName = companyName;
	}

	public int getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}
}
