package applicationLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

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
	private int supply = 5000;
	@XStreamImplicit
	private ArrayList<DefectStatistic> defects;
	
	public StatisticResult(int id, LocalDate date, String fileName, String gza, int branch, String companyName) {
		super(id, date);
		this.fileName = fileName;
		this.year = date.getYear();
		this.gza = "B";
		this.branch = branch;
		this.fileName = "BS_" + companyName + "_" + id;
	}

	public void setDefects(ArrayList<DefectStatistic> arrayList) {
		this.defects = arrayList;
	}
}