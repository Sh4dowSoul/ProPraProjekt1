package applicationLogic;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Mangel")
public class StatisticElementBase {
	@XStreamAlias("Betriebsbereich")
	private int branchId;
	@XStreamAlias("MangelNr")
	private int defectId;
	@XStreamAlias("Anzahl")
	private int numberOccurrence = 1;
	
	public StatisticElementBase(int branchId, int defectId) {
		this.branchId = branchId;
		this.defectId = defectId;
	}
	
	
	public int getDefectId() {
		return defectId;
	}

	public int getNumberOccurrence() {
		return numberOccurrence;
	}
	
	public int getBranchId() {
		return branchId;
	}


	public void setNumberOccurrence(int numberOccurrence) {
		this.numberOccurrence = numberOccurrence;
	}
}
