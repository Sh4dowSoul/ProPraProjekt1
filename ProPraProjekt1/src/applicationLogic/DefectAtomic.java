package applicationLogic;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The Class Defect
 * 
 * @author name
 */

public class DefectAtomic extends AutocompleteSuggestion {
	
	public DefectAtomic(int defectId, String defectDescription) {
		super(defectId, defectDescription);
	}
}
