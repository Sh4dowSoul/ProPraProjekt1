package applicationLogic;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Defect Class used for Statistic Export
 * 
 * @author Niklas Schnettler
 *
 */
@XStreamAlias("Mangel")
public class FlawStatistic extends Flaw{
	@XStreamAlias("Betriebsbereich")
	private int branchId;
	@XStreamAlias("MangelNr")
	private int externalFlawId;
	@XStreamAlias("Anzahl")
	private int numberOccurrence = 1;
	
	//For Export to XML
	public FlawStatistic(int externalFlawId, int branchId) {
		super(externalFlawId, 0, false, null, false);
		this.branchId = branchId;
		this.externalFlawId = externalFlawId;
	}
	
	//For Display in GUI
	public FlawStatistic(int externalFlawId, String flawDescription, int numberOccurrence) {
		super(externalFlawId, flawDescription);
		this.numberOccurrence = numberOccurrence;
	}
	
	public int getNumberOccurrence() {
		return numberOccurrence;
	}
	
	public int getBranchId() {
		return branchId;
	}
}
