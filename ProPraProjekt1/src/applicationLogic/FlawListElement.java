package applicationLogic;

/**
 * @author Niklas Schnettler
 *
 */
public class FlawListElement {
	
	private int elementId;	//ListElementId in Database
	private Flaw flaw;
	private int branchId;
	private int danger;
	private String building;
	private String room;
	private String machine;
	
	public FlawListElement(int elementId, Flaw flaw, int branchId, int danger, String building, String room, String machine) {
		this.elementId = elementId;
		this.flaw = flaw;
		this.branchId = branchId;
		this.danger = danger;
		this.building = building;
		this.room = room;
		this.machine = machine;
	}
	
	public int getExternalFlawId() {
		return flaw.getExternalId();
	}
	
	public String getFlawDescription() {
		return flaw.getDescription();
	}
	
	public int getBranchId() {
		return branchId;
	}

	public int getElementId() {
		return elementId;
	}

	public int getDanger() {
		return danger;
	}
	
	public String getDangerString() {
		switch (this.danger) {
		default: 
			return "";
		case 1: 
			return "X";
		case 2: 
			return "O";
		case 3: 
			return "X,O";
		}
	}

	public String getBuilding() {
		return Util.getCheckedNull(building);
	}

	public String getRoom() {
		return Util.getCheckedNull(room);
	}

	public String getMachine() {
		return Util.getCheckedNull(machine);
	}
	
	public Flaw getFlaw() {
		return flaw;
	}
}