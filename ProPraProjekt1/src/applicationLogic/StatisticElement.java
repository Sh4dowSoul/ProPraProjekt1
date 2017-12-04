package applicationLogic;

public class StatisticElement {
	private int defectId;
	private String defectDescription;
	private int numberOccurrence;
	
	public StatisticElement(int defectId,String defectDescription, int numberOccurrence) {
		super();
		this.defectId = defectId;
		this.defectDescription = defectDescription;
		this.numberOccurrence = numberOccurrence;
	}

	public int getDefectId() {
		return defectId;
	}

	public int getNumberOccurrence() {
		return numberOccurrence;
	}

	public String getDefectDescription() {
		return defectDescription;
	}
}
