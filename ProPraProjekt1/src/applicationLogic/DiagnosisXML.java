package applicationLogic;

import java.util.ArrayList;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Befundschein")
public class DiagnosisXML extends DiagnosisPreview {
	@XStreamImplicit
	private ArrayList<StatisticElementBase> defects;
	
	public void setDefects(ArrayList<StatisticElementBase> arrayList) {
		this.defects = arrayList;
	}

	public DiagnosisXML(int diagnosis_id, Date diagnosis_date, int company_id, String company_name) {
		super(diagnosis_id, diagnosis_date, company_id, company_name);
		this.defects = defects;
	}

}