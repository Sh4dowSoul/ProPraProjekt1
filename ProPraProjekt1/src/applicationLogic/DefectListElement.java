package applicationLogic;

import java.util.Date;

/**
 * The Class DefectListElement
 * 
 * @author name
 */

public class DefectListElement {

	private String danger;
	private Date resolvedOn;
	private String responsiblePerson;
	
	public DefectListElement(String danger, Date resolvedOn, String responsiblePerson) {
		this.danger = danger;
		this.resolvedOn = resolvedOn;
		this.responsiblePerson = responsiblePerson;
	}

	public String getDanger() {
		return danger;
	}

	public void setDanger(String danger) {
		this.danger = danger;
	}

	public Date getResolvedOn() {
		return resolvedOn;
	}

	public void setResolvedOn(Date resolvedOn) {
		this.resolvedOn = resolvedOn;
	}

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

}
