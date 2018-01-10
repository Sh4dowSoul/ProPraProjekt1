package applicationLogic;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Used as Wrapper Class for the XML Export of a list of Diagnoses
 * 
 * @author Niklas Schnettler
 *
 */
@XStreamAlias("Befundscheine")
public class Statistic {
	@XStreamImplicit
	private ArrayList<InspectionReportStatistic> inspectionReports;
	
	public Statistic(ArrayList<InspectionReportStatistic> inspectionReports) {
		this.inspectionReports = inspectionReports;
	};
}
