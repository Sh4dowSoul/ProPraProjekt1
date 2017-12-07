package applicationLogic;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The Class Defect
 * 
 * @author name
 */

public class DefectAtomic {
	
	@XStreamAlias("MangelNr")
	private int defectId;
	private String defectDescription;

	public DefectAtomic(int defectId, String defectDescription) {
		this.defectId = defectId;
		this.defectDescription = defectDescription;
	}

	public int getDefectId() {
		return defectId;
	}

	public String getDefectDescription() {
		return defectDescription;
	}

	@Override
	public String toString() { 
		return defectDescription;
	}

}
