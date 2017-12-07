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
	private String gza ="TODO";
	@XStreamAlias("Branche")
	private String branch ="TODO";
	@XStreamImplicit
	private ArrayList<DefectStatistic> defects;
	
	public StatisticResult(int id, LocalDate date, String fileName, String gza, String branch) {
		super(id, date);
		this.fileName = fileName;
		this.year = date.getYear();
		this.gza = gza;
		this.branch = branch;
	}

	public void setDefects(ArrayList<DefectStatistic> arrayList) {
		this.defects = arrayList;
	}
}