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
	private int position;
	
	public FlawListElement(int elementId, Flaw flaw, int branchId, int danger, String building, String room, String machine, int position) {
		this.elementId = elementId;
		this.flaw = flaw;
		this.branchId = branchId;
		this.danger = danger;
		this.building = building;
		this.room = room;
		this.machine = machine;
		this.position = position;
	}
	
	public FlawListElement(Flaw flaw, int branchId, int danger, String building, String room, String machine, int position) {
		this.flaw = flaw;
		this.branchId = branchId;
		this.danger = danger;
		this.building = building;
		this.room = room;
		this.machine = machine;
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	public int getExternalFlawId() {
		return flaw.getExternalId();
	}
	
	public int getInternalFlawId() {
		return flaw.getInternalId();
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

	public void setFlaw(Flaw flaw) {
		this.flaw = flaw;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public void setDanger(int danger) {
		this.danger = danger;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public void setRoom(String room) {
		this.room = room;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlawListElement other = (FlawListElement) obj;
		if (branchId != other.branchId)
			return false;
		if (building == null) {
			if (other.building != null)
				return false;
		} else if (!building.equals(other.building))
			if (!(building.isEmpty() && other.building == null)) {
				return false;
			}
		if (danger != other.danger)
			return false;
		if (elementId != other.elementId)
			return false;
		if (flaw == null) {
			if (other.flaw != null)
				return false;
		} else if (!flaw.equals(other.flaw))
			return false;
		if (machine == null) {
			if (other.machine != null)
				return false;
		} else if (!machine.equals(other.machine))
			if (!(machine.isEmpty() && other.machine == null)) {
				return false;
			}
		System.out.println("ROOM " + room + " - " +other.room);
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room)) {
			if (!(room.isEmpty() && other.room == null)) {
				return false;
			}
		}
		return true;
	}
}