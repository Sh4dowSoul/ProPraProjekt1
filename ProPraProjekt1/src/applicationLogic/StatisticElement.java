package applicationLogic;

public class StatisticElement {
	private int defectId;
	private int numberOccurrence;
	
	public StatisticElement(int defectId, int numberOccurrence) {
		super();
		this.defectId = defectId;
		this.numberOccurrence = numberOccurrence;
	}

	public int getDefectId() {
		return defectId;
	}

	public int getNumberOccurrence() {
		return numberOccurrence;
	}
}
