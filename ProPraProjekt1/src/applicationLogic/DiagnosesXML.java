package applicationLogic;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Befundscheine")
public class DiagnosesXML {
	@XStreamImplicit
	private ArrayList<DiagnosisXML> diagnosesList;
	
	public DiagnosesXML(ArrayList<DiagnosisXML> diagnosesList) {
		this.diagnosesList = diagnosesList;
	};
}
