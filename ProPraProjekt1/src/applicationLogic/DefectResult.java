package applicationLogic;

public class DefectResult extends DefectStatistic{
	private int danger;
	private String building;
	private String dangerString;
	private String room;
	private String machine;
	private String defectCustomDescription;
	
	public DefectResult(int defectId, int branchId, int danger, String building, String room, String machine,
			String defectCustomDescription) {
		super(defectId, branchId);
		this.danger = danger;
		this.building = building;
		this.room = room;
		this.machine = machine;
		this.defectCustomDescription = defectCustomDescription;
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
			return "0";
		case 3: 
			return "X,0";
		}
	}

	public String getBuilding() {
		return building;
	}

	public String getRoom() {
		return room;
	}

	public String getMachine() {
		return machine;
	}

	public String getDefectCustomDescription() {
		return defectCustomDescription;
	}
}
