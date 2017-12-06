package applicationLogic;

import com.thoughtworks.xstream.annotations.XStreamAlias;


public class StatisticElement extends StatisticElementBase {
	private String defectDescription;
	
	public StatisticElement(int defectId, String defectDescription, int numberOccurrence, int branchId) {
		super(branchId, defectId );
		this.defectDescription = defectDescription;
		super.setNumberOccurrence(numberOccurrence);
	}


	public String getDefectDescription() {
		return defectDescription;
	}
}
