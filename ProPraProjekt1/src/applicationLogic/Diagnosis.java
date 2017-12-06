package applicationLogic;

import java.util.Date;

/**
 * The Class Diagnosis
 * 
 * @author Niklas Schnettler
 */

public class Diagnosis {

	private int id;
	private String lastEdited;
	public int plantId;
	private String companion;
	private String surveyor;
	private int vdsApprovalNr;
	private String examinationDate;
	private double examinationDuration;
	boolean frequencyControlledUtilities;
	private boolean precautionsDeclared;
	private String precautionsDeclaredLocation;
	private boolean examinationComplete;
	private String subsequentExaminationDate;
	private String examinationIncompleteReason;
	private int changesSinceLastExamination;
	private int defectsLastExaminationFixed;
	private int dangerCategory;
	private String dangerCategoryDescription;
	private boolean examinationResultNoDefect;
	private boolean examinationResultDefect;
	private boolean examinationResultDanger;
	private int pages;
	private boolean isolationChecked;
	private boolean isolationMesasurementProtocols;
	private boolean isolationCompensationMeasures;
	private String isolationCompensationMeasuresAnnotation;
	private Boolean rcdAvailable;
	private int rcdAvailablePercent;
	private String rcdAnnotation;
	private boolean resistance;
	private int resistanceNumber;
	private String resistanceAnnotation;
	private boolean thermalAbnormality;
	private String thermalAbnormalityAnnotation;
	private boolean internalPortableUtilities;
	private int externalPortableUtilities;
	private int supplySystem;
	private int energyDemand;
	private int maxEnergyDemandExternal;
	private int maxEnergyDemandInternal;
	private int protectedCircuitsPercent;
	private int hardWiredLoads;
	private String additionalAnnotations;
	
	
	public Diagnosis(int id,
	 String lastEdited,
	 int plantId,
	 String companion,
	 String surveyor,
	 int vdsApprovalNr,
	 String examinationDate,
	 double examinationDuration,
	 boolean frequencyControlledUtilities,
	 boolean precautionsDeclared,
	 String precautionsDeclaredLocation,
	 boolean examinationComplete,
	 String subsequentExaminationDate,
	 String examinationIncompleteReason,
	 int changesSinceLastExamination,
	 int defectsLastExaminationFixed,
	 int dangerCategory,
	 String dangerCategoryDescription,
	 boolean examinationResultNoDefect,
	 boolean examinationResultDefect,
	 boolean examinationResultDanger,
	 int pages,
	 boolean isolationChecked,
	 boolean isolationMesasurementProtocols,
	 boolean isolationCompensationMeasures,
	 String isolationCompensationMeasuresAnnotation,
	 Boolean rcdAvailable,
	 int rcdAvailablePercent,
	 String rcdAnnotation,
	 boolean resistance,
	 int resistanceNumber,
	 String resistanceAnnotation,
	 boolean thermalAbnormality,
	 String thermalAbnormalityAnnotation,
	 boolean internalPortableUtilities,
	 int externalPortableUtilities,
	 int supplySystem,
	 int energyDemand,
	 int maxEnergyDemandExternal,
	 int maxEnergyDemandInternal,
	 int protectedCircuitsPercent,
	 int hardWiredLoads,
	 String additionalAnnotations) {
		this.id = id;
		this.lastEdited = lastEdited;
		this.plantId = plantId;
		this.companion = companion;
		this.surveyor = surveyor;
		this.vdsApprovalNr = vdsApprovalNr;
		this.examinationDate = examinationDate;
		this.examinationDuration = examinationDuration;
		this.frequencyControlledUtilities = frequencyControlledUtilities;
		this.precautionsDeclared = precautionsDeclared;
		this.precautionsDeclaredLocation = precautionsDeclaredLocation;
		this.examinationComplete = examinationComplete;
		this.subsequentExaminationDate = subsequentExaminationDate;
		this.examinationIncompleteReason = examinationIncompleteReason;
		this.changesSinceLastExamination = changesSinceLastExamination;
		this.defectsLastExaminationFixed = defectsLastExaminationFixed;
		this.dangerCategory = dangerCategory;
		this.dangerCategoryDescription = dangerCategoryDescription;
		this.examinationResultNoDefect = examinationResultNoDefect;
		this.examinationResultDefect = examinationResultDefect;
		this.examinationResultDanger = examinationResultDanger;
		this.pages = pages;
		this.isolationChecked = isolationChecked;
		this.isolationMesasurementProtocols = isolationMesasurementProtocols;
		this.isolationCompensationMeasures = isolationCompensationMeasures;
		this.isolationCompensationMeasuresAnnotation = isolationCompensationMeasuresAnnotation;
		this.rcdAvailable = rcdAvailable;
		this.rcdAvailablePercent = rcdAvailablePercent;
		this.rcdAnnotation = rcdAnnotation;
		this.resistance = resistance;
		this.resistanceNumber = resistanceNumber;
		this.resistanceAnnotation = resistanceAnnotation;
		this.thermalAbnormality = thermalAbnormality;
		this.thermalAbnormalityAnnotation = thermalAbnormalityAnnotation;
		this.internalPortableUtilities = internalPortableUtilities;
		this.externalPortableUtilities = externalPortableUtilities;
		this.supplySystem = supplySystem;
		this.energyDemand = energyDemand;
		this.maxEnergyDemandExternal = maxEnergyDemandExternal;
		this.maxEnergyDemandInternal = maxEnergyDemandInternal;
		this.protectedCircuitsPercent = protectedCircuitsPercent;
		this.hardWiredLoads = hardWiredLoads;
		this.additionalAnnotations = additionalAnnotations;
	}


	public String getLastEdited() {
		return lastEdited;
	}


	public int getId() {
		return id;
	}


	public int getPlantId() {
		return plantId;
	}


	public String getCompanion() {
		return companion;
	}


	public String getSurveyor() {
		return surveyor;
	}


	public int getVdsApprovalNr() {
		return vdsApprovalNr;
	}


	public String getExaminationDate() {
		return examinationDate;
	}


	public double getExaminationDuration() {
		return examinationDuration;
	}


	public boolean isFrequencyControlledUtilities() {
		return frequencyControlledUtilities;
	}


	public boolean isPrecautionsDeclared() {
		return precautionsDeclared;
	}


	public String getPrecautionsDeclaredLocation() {
		return precautionsDeclaredLocation;
	}


	public boolean isExaminationComplete() {
		return examinationComplete;
	}


	public String getSubsequentExaminationDate() {
		return subsequentExaminationDate;
	}


	public String getExaminationIncompleteReason() {
		return examinationIncompleteReason;
	}


	public int getChangesSinceLastExamination() {
		return changesSinceLastExamination;
	}


	public int getDefectsLastExaminationFixed() {
		return defectsLastExaminationFixed;
	}


	public int getDangerCategory() {
		return dangerCategory;
	}


	public String getDangerCategoryDescription() {
		return dangerCategoryDescription;
	}


	public boolean isExaminationResultNoDefect() {
		return examinationResultNoDefect;
	}


	public boolean isExaminationResultDefect() {
		return examinationResultDefect;
	}


	public boolean isExaminationResultDanger() {
		return examinationResultDanger;
	}


	public boolean isIsolationChecked() {
		return isolationChecked;
	}


	public boolean isIsolationMesasurementProtocols() {
		return isolationMesasurementProtocols;
	}


	public boolean isIsolationCompensationMeasures() {
		return isolationCompensationMeasures;
	}


	public String getIsolationCompensationMeasuresAnnotation() {
		return isolationCompensationMeasuresAnnotation;
	}


	public Boolean getRcdAvailable() {
		return rcdAvailable;
	}


	public int getRcdAvailablePercent() {
		return rcdAvailablePercent;
	}


	public String getRcdAnnotation() {
		return rcdAnnotation;
	}


	public boolean isResistance() {
		return resistance;
	}


	public int getResistanceNumber() {
		return resistanceNumber;
	}


	public String getResistanceAnnotation() {
		return resistanceAnnotation;
	}


	public boolean isThermalAbnormality() {
		return thermalAbnormality;
	}


	public String getThermalAbnormalityAnnotation() {
		return thermalAbnormalityAnnotation;
	}


	public boolean isInternalPortableUtilities() {
		return internalPortableUtilities;
	}


	public int getExternalPortableUtilities() {
		return externalPortableUtilities;
	}


	public int getSupplySystem() {
		return supplySystem;
	}


	public int getEnergyDemand() {
		return energyDemand;
	}


	public int getMaxEnergyDemandExternal() {
		return maxEnergyDemandExternal;
	}


	public int getMaxEnergyDemandInternal() {
		return maxEnergyDemandInternal;
	}


	public int getProtectedCircuitsPercent() {
		return protectedCircuitsPercent;
	}


	public int getHardWiredLoads() {
		return hardWiredLoads;
	}


	public String getAdditionalAnnotations() {
		return additionalAnnotations;
	}
}
