package applicationLogic;

public class DefectResult extends DefectStatistic{
	private int elementId;
	private int danger;
	private String building;
	private String dangerString;
	private String room;
	private String machine;
	private String defectCustomDescription;
	
	public DefectResult(int elementId, int defectId, int branchId, int danger, String building, String room, String machine,
			String defectCustomDescription) {
		super(defectId, branchId);
		this.elementId = elementId;
		this.danger = danger;
		this.building = building;
		this.room = room;
		this.machine = machine;
		this.defectCustomDescription = defectCustomDescription;
	}
	
	public int getElementId() {
		return elementId;
	}

	public DefectResult(int defectId) {
		super(defectId,-1);
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

	public String getDefectCustomDescription() {
		return Util.getCheckedNull(defectCustomDescription);
	}
}
