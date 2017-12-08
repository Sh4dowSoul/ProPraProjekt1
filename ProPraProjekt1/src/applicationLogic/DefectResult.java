package applicationLogic;

public class DefectResult extends DefectStatistic{
	private int ifdNR;
	private int danger;
	private String building;
	private String room;
	private String machine;
	private String defectCustomDescription;
	
	public DefectResult(int defectId, int branchId, int ifdNR, int danger, String building, String room, String machine,
			String defectCustomDescription) {
		super(defectId, branchId);
		this.ifdNR = ifdNR;
		this.danger = danger;
		this.building = building;
		this.room = room;
		this.machine = machine;
		this.defectCustomDescription = defectCustomDescription;
	}

	public int getIfdNR() {
		return ifdNR;
	}

	public int getDanger() {
		return danger;
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