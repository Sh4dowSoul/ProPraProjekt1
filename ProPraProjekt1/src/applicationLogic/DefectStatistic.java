package applicationLogic;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Mangel")
public class DefectStatistic extends DefectAtomic{
	@XStreamAlias("Betriebsbereich")
	private int branchId;
	@XStreamAlias("MangelNr")
	private int id;
	@XStreamAlias("Anzahl")
	private int numberOccurrence = 1;
	
	//For Export to XML
	public DefectStatistic(int defectId, int branchId) {
		super(defectId, null);
		this.branchId = branchId;
		this.id = defectId;
	}
	
	//For Display in GUI
	public DefectStatistic(int defectId, String defectDescription, int numberOccurrence) {
		super(defectId, defectDescription);
		this.numberOccurrence = numberOccurrence;
	}
	
	public int getNumberOccurrence() {
		return numberOccurrence;
	}
	
	public int getBranchId() {
		return branchId;
	}
}
