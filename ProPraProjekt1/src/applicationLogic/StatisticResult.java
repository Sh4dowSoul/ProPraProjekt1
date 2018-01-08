package applicationLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author Niklas Schnettler
 *
 */
@XStreamAlias("Befundschein")
public class StatisticResult extends ResultBase {
	
	@XStreamAlias("datei")
	private String fileName = "TODO";
	@XStreamAlias("Jahr")
	private int year;
	@XStreamAlias("GZA")
	private String gza;
	@XStreamAlias("Branche")
	private int branch;
	@XStreamAlias("Betriebsmittel")
	private String technicalEquipment;
	@XStreamImplicit
	private ArrayList<DefectStatistic> defects;
	
	public StatisticResult(int id, LocalDate date, int gza, int branch, String companyName, int technicalEquipment) {
		super(id, date);
		this.year = date.getYear();
		this.branch = branch;
		this.fileName = "BS_" + companyName + "_" + id;
		switch (technicalEquipment) {
			case 0:
				this.technicalEquipment = "250";
				break;
			case 1:
				this.technicalEquipment = "500";
				break;
			case 2:
				this.technicalEquipment = "1000";
				break;
			case 3:
				this.technicalEquipment = "5000";
				break;
			case 4:
				this.technicalEquipment = ">5000";
				break;
		}
		switch (gza) {
			case 0:
				this.gza = "A";
				break;
			case 1:
				this.gza = "B";
				break;
			case 2:
				this.gza = "C";
				break;
			case 3:
				this.gza = "D";
				break;
		}
	}

	public void setDefects(ArrayList<DefectStatistic> arrayList) {
		this.defects = arrayList;
	}
}