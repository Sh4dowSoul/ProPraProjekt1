package applicationLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import javafx.collections.ObservableList;

/**
 * The Class Diagnosis
 * 
 * @author Niklas Schnettler
 */

public class InspectionReportFull extends InspectionReportMinimal {

	@NotNull(message = "Risikoanschrift (Firma)")
	private CompanyPlant companyPlant;
	//Wird erst später gesetzt
	private LocalDate lastEdited;
	//
	private String companion;
	@NotEmpty(message = "Sachverständiger")
	private String surveyor;
	@NotNull(message = "VdS-Anerk.Nr.")
	private Integer vdsApprovalNr;
	@NotNull(message = "Prüfungsdauer")
	private Double examinationDuration;
	@NotNull(message = "Art des bertiebs oder der Anlage")
	private Branch branch;
	@NotNull(message = "Frequenzgesteuerte Betriebsmittel")
	private Boolean frequencyControlledUtilities;
	@NotNull(message = "Bereiche mit besonderen Schutzmaßnahmen gekennzeichnet")
	private Boolean precautionsDeclared;
	//
	private String precautionsDeclaredLocation;
	@NotNull(message = "Alle bereiche des Standorts geprüft")
	private Boolean examinationComplete;
	//
	private LocalDate subsequentExaminationDate;
	//
	private String examinationIncompleteReason;
	@NotNull(message = "Anlage seit letzter Prüfung erweitert/erneuert/umgebaut")
	private Integer changesSinceLastExamination;
	@NotNull(message = "Mängel der letzten Prüfung beseitigt")
	private Integer defectsLastExaminationFixed;
	@NotNull(message = "Gesamtbeurteilung der Anlage")
	private Integer dangerCategory;
	//
	private String dangerCategoryDescription;
	private Boolean examinationResultNoDefect;
	private Boolean examinationResultDefect;
	private LocalDate examinationResultDefectDate;
	private Boolean examinationResultDanger;
	@NotNull(message = "Isolationswiderstand")
	private Boolean isolationChecked;
	@NotNull(message = "Messprotokolle über Isolationswiderstandsmessungen")
	private Boolean isolationMesasurementProtocols;
	@NotNull(message = "Ersatzmaßnahmen für Isolationswiderstandsmessungen")
	private Boolean isolationCompensationMeasures;
	//
	private String isolationCompensationMeasuresAnnotation;
	@NotNull(message = "Fehlstrom-Schutzeinrichtungen")
	private Boolean rcdAvailable;
	private Double rcdAvailablePercent;
	//
	private String rcdAnnotation;
	@NotNull(message = "Schleifenwiderstand")
	private Boolean resistance;
	private Integer resistanceNumber;
	//
	private String resistanceAnnotation;
	@NotNull(message = "Thermische Auffälligkeiten")
	private Boolean thermalAbnormality;
	//
	private String thermalAbnormalityAnnotation;
	@NotNull(message = "Eigene Ortsveränderliche Betriebsmittel")
	private Boolean internalPortableUtilities;
	@NotNull(message = "Fremde Ortsveränderliche Betriebsmittel")
	private Integer externalPortableUtilities;
	@NotNull(message = "Versorgungssystem")
	private Integer supplySystem;
	//
	private Integer energyDemand;
	private Integer maxEnergyDemandExternal;
	private Integer maxEnergyDemandInternal;
	private Integer protectedCircuitsPercent;
	@NotNull(message = "Fest angeschlossene Verbraucher")
	private Integer hardWiredLoads;
	//
	private String additionalAnnotations;
	private Boolean isValid;
	private ObservableList<FlawListElement> defects;
	public InspectionReportFull(Integer id, LocalDate date, LocalDate lastEdited, String companion, String surveyor, Integer vdsApprovalNr,
			Double examinationDuration, Branch branch, Boolean frequencyControlledUtilities, Boolean precautionsDeclared,
			String precautionsDeclaredLocation, Boolean examinationComplete, LocalDate subsequentExaminationDate,
			String examinationIncompleteReason, Integer changesSinceLastExamination, Integer defectsLastExaminationFixed,
			Integer dangerCategory, String dangerCategoryDescription, Boolean examinationResultNoDefect,
			Boolean examinationResultDefect, LocalDate examinationResultDefectDate,  Boolean examinationResultDanger, Boolean isolationChecked,
			Boolean isolationMesasurementProtocols, Boolean isolationCompensationMeasures,
			String isolationCompensationMeasuresAnnotation, Boolean rcdAvailable, Double rcdAvailablePercent,
			String rcdAnnotation, Boolean resistance, Integer resistanceNumber, String resistanceAnnotation,
			Boolean thermalAbnormality, String thermalAbnormalityAnnotation, Boolean internalPortableUtilities,
			Integer externalPortableUtilities, Integer supplySystem, Integer energyDemand, Integer maxEnergyDemandExternal,
			Integer maxEnergyDemandInternal, Integer protectedCircuitsPercent, Integer hardWiredLoads, String additionalAnnotations,
			CompanyPlant companyPlant, Boolean validated) {
		super(id, date);
		this.lastEdited = lastEdited;
		this.companion = companion;
		this.surveyor = surveyor;
		this.vdsApprovalNr = vdsApprovalNr;
		this.examinationDuration = examinationDuration;
		this.branch = branch;
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
		this.examinationResultDefectDate = examinationResultDefectDate;
		this.examinationResultDanger = examinationResultDanger;
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
		this.companyPlant = companyPlant;
	}
	
	public InspectionReportFull() {
		super();
	}


	public LocalDate getExaminationResultDefectDate() {
		return examinationResultDefectDate;
	}
	
	public String getExaminationResultDefectDateNice() {
		return examinationResultDefectDate != null ? examinationResultDefectDate.format(DateTimeFormatter.ofPattern("dd.MM.YYYY")) : "";
	}


	public LocalDate getLastEdited() {
		return lastEdited;
	}

	
	public CompanyPlant getCompanyPlant() {
		return companyPlant;
	}


	public String getCompanion() {
		return companion;
	}


	public String getSurveyor() {
		return surveyor;
	}


	public Integer getVdsApprovalNr() {
		return vdsApprovalNr;
	}


	public Double getExaminationDuration() {
		return examinationDuration;
	}
	
	public Branch getBranch() {
		return branch;
	}


	public Boolean isFrequencyControlledUtilities() {
		return frequencyControlledUtilities;
	}


	public void setLastEdited(LocalDate lastEdited) {
		this.lastEdited = lastEdited;
	}

	public void setCompanion(String companion) {
		this.companion = companion;
	}

	public void setSurveyor(String surveyor) {
		this.surveyor = surveyor;
	}

	public void setVdsApprovalNr(Integer vdsApprovalNr) {
		this.vdsApprovalNr = vdsApprovalNr;
	}

	public void setExaminationDuration(Double examinationDuration) {
		this.examinationDuration = examinationDuration;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public void setFrequencyControlledUtilities(Boolean frequencyControlledUtilities) {
		this.frequencyControlledUtilities = frequencyControlledUtilities;
	}

	public void setPrecautionsDeclared(Boolean precautionsDeclared) {
		this.precautionsDeclared = precautionsDeclared;
	}

	public void setPrecautionsDeclaredLocation(String precautionsDeclaredLocation) {
		this.precautionsDeclaredLocation = precautionsDeclaredLocation;
	}

	public void setExaminationComplete(Boolean examinationComplete) {
		this.examinationComplete = examinationComplete;
	}

	public void setSubsequentExaminationDate(LocalDate subsequentExaminationDate) {
		this.subsequentExaminationDate = subsequentExaminationDate;
	}

	public void setExaminationIncompleteReason(String examinationIncompleteReason) {
		this.examinationIncompleteReason = examinationIncompleteReason;
	}

	public void setChangesSinceLastExamination(Integer changesSinceLastExamination) {
		this.changesSinceLastExamination = changesSinceLastExamination;
	}

	public void setDefectsLastExaminationFixed(Integer defectsLastExaminationFixed) {
		this.defectsLastExaminationFixed = defectsLastExaminationFixed;
	}

	public void setDangerCategory(Integer dangerCategory) {
		this.dangerCategory = dangerCategory;
	}

	public void setDangerCategoryDescription(String dangerCategoryDescription) {
		this.dangerCategoryDescription = dangerCategoryDescription;
	}

	public void setExaminationResultNoDefect(Boolean examinationResultNoDefect) {
		this.examinationResultNoDefect = examinationResultNoDefect;
	}

	public void setExaminationResultDefect(Boolean examinationResultDefect) {
		this.examinationResultDefect = examinationResultDefect;
	}

	public void setExaminationResultDefectDate(LocalDate examinationResultDefectDate) {
		this.examinationResultDefectDate = examinationResultDefectDate;
	}

	public void setExaminationResultDanger(Boolean examinationResultDanger) {
		this.examinationResultDanger = examinationResultDanger;
	}

	public void setIsolationChecked(Boolean isolationChecked) {
		this.isolationChecked = isolationChecked;
	}

	public void setIsolationMesasurementProtocols(Boolean isolationMesasurementProtocols) {
		this.isolationMesasurementProtocols = isolationMesasurementProtocols;
	}

	public void setIsolationCompensationMeasures(Boolean isolationCompensationMeasures) {
		this.isolationCompensationMeasures = isolationCompensationMeasures;
	}

	public void setIsolationCompensationMeasuresAnnotation(String isolationCompensationMeasuresAnnotation) {
		this.isolationCompensationMeasuresAnnotation = isolationCompensationMeasuresAnnotation;
	}

	public void setRcdAvailable(Boolean rcdAvailable) {
		this.rcdAvailable = rcdAvailable;
	}

	public void setRcdAvailablePercent(Double rcdAvailablePercent) {
		this.rcdAvailablePercent = rcdAvailablePercent;
	}

	public void setRcdAnnotation(String rcdAnnotation) {
		this.rcdAnnotation = rcdAnnotation;
	}

	public void setResistance(Boolean resistance) {
		this.resistance = resistance;
	}

	public void setResistanceNumber(Integer resistanceNumber) {
		this.resistanceNumber = resistanceNumber;
	}

	public void setResistanceAnnotation(String resistanceAnnotation) {
		this.resistanceAnnotation = resistanceAnnotation;
	}

	public void setThermalAbnormality(Boolean thermalAbnormality) {
		this.thermalAbnormality = thermalAbnormality;
	}

	public void setThermalAbnormalityAnnotation(String thermalAbnormalityAnnotation) {
		this.thermalAbnormalityAnnotation = thermalAbnormalityAnnotation;
	}

	public void setInternalPortableUtilities(Boolean internalPortableUtilities) {
		this.internalPortableUtilities = internalPortableUtilities;
	}

	public void setExternalPortableUtilities(Integer externalPortableUtilities) {
		this.externalPortableUtilities = externalPortableUtilities;
	}

	public void setSupplySystem(Integer supplySystem) {
		this.supplySystem = supplySystem;
	}

	public void setEnergyDemand(Integer energyDemand) {
		this.energyDemand = energyDemand;
	}

	public void setMaxEnergyDemandExternal(Integer maxEnergyDemandExternal) {
		this.maxEnergyDemandExternal = maxEnergyDemandExternal;
	}

	public void setMaxEnergyDemandInternal(Integer maxEnergyDemandInternal) {
		this.maxEnergyDemandInternal = maxEnergyDemandInternal;
	}

	public void setProtectedCircuitsPercent(Integer protectedCircuitsPercent) {
		this.protectedCircuitsPercent = protectedCircuitsPercent;
	}

	public void setHardWiredLoads(Integer hardWiredLoads) {
		this.hardWiredLoads = hardWiredLoads;
	}

	public void setAdditionalAnnotations(String additionalAnnotations) {
		this.additionalAnnotations = additionalAnnotations;
	}

	public void setCompanyPlant(CompanyPlant companyPlant) {
		this.companyPlant = companyPlant;
	}

	public Boolean isPrecautionsDeclared() {
		return precautionsDeclared;
	}


	public String getPrecautionsDeclaredLocation() {
		return precautionsDeclaredLocation;
	}


	public Boolean isExaminationComplete() {
		return examinationComplete;
	}


	public LocalDate getSubsequentExaminationDate() {
		return subsequentExaminationDate;
	}

	public String getSubsequentExaminationDateNice() {
		return (subsequentExaminationDate != null ? subsequentExaminationDate.format(DateTimeFormatter.ofPattern("dd.MM.YYYY")) : "");
	}

	public String getExaminationIncompleteReason() {
		return examinationIncompleteReason;
	}


	public Integer getChangesSinceLastExamination() {
		return changesSinceLastExamination;
	}


	public Integer getDefectsLastExaminationFixed() {
		return defectsLastExaminationFixed;
	}


	public Integer getDangerCategory() {
		return dangerCategory;
	}


	public String getDangerCategoryDescription() {
		return dangerCategoryDescription;
	}


	public Boolean isExaminationResultNoDefect() {
		return examinationResultNoDefect;
	}


	public Boolean isExaminationResultDefect() {
		return examinationResultDefect;
	}


	public Boolean isExaminationResultDanger() {
		return examinationResultDanger;
	}


	public Boolean isIsolationChecked() {
		return isolationChecked;
	}


	public Boolean isIsolationMesasurementProtocols() {
		return isolationMesasurementProtocols;
	}


	public Boolean isIsolationCompensationMeasures() {
		return isolationCompensationMeasures;
	}


	public String getIsolationCompensationMeasuresAnnotation() {
		return isolationCompensationMeasuresAnnotation;
	}


	public Boolean getRcdAvailable() {
		return rcdAvailable;
	}


	public Double getRcdAvailablePercent() {
		return rcdAvailablePercent;
	}


	public String getRcdAnnotation() {
		return rcdAnnotation;
	}


	public Boolean isResistance() {
		return resistance;
	}


	public Integer getResistanceNumber() {
		return resistanceNumber;
	}


	public String getResistanceAnnotation() {
		return resistanceAnnotation;
	}


	public Boolean isThermalAbnormality() {
		return thermalAbnormality;
	}


	public String getThermalAbnormalityAnnotation() {
		return thermalAbnormalityAnnotation;
	}


	public Boolean isInternalPortableUtilities() {
		return internalPortableUtilities;
	}


	public Integer getExternalPortableUtilities() {
		return externalPortableUtilities;
	}


	public Integer getSupplySystem() {
		return supplySystem;
	}


	public Integer getEnergyDemand() {
		return energyDemand;
	}


	public Integer getMaxEnergyDemandExternal() {
		return maxEnergyDemandExternal;
	}


	public Integer getMaxEnergyDemandInternal() {
		return maxEnergyDemandInternal;
	}


	public Integer getProtectedCircuitsPercent() {
		return protectedCircuitsPercent;
	}


	public Integer getHardWiredLoads() {
		return hardWiredLoads;
	}


	public String getAdditionalAnnotations() {
		return additionalAnnotations;
	}
	
	public ObservableList<FlawListElement> getDefects() {
		return defects;
	}


	public void setDefects(ObservableList<FlawListElement> defects) {
		this.defects = defects;
	}

	public Boolean isValid() {
		return isValid;
	}

	public void setValid(Boolean isValid) {
		this.isValid = isValid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InspectionReportFull other = (InspectionReportFull) obj;
		if (additionalAnnotations == null) {
			if (other.additionalAnnotations != null)
				return false;
		} else if (!additionalAnnotations.equals(other.additionalAnnotations))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (changesSinceLastExamination == null) {
			if (other.changesSinceLastExamination != null)
				return false;
		} else if (!changesSinceLastExamination.equals(other.changesSinceLastExamination))
			return false;
		if (companion == null) {
			if (other.companion != null)
				return false;
		} else if (!companion.equals(other.companion))
			return false;
		if (companyPlant == null) {
			if (other.companyPlant != null)
				return false;
		} else if (!companyPlant.equals(other.companyPlant))
			return false;
		if (dangerCategory == null) {
			if (other.dangerCategory != null)
				return false;
		} else if (!dangerCategory.equals(other.dangerCategory))
			return false;
		if (dangerCategoryDescription == null) {
			if (other.dangerCategoryDescription != null)
				return false;
		} else if (!dangerCategoryDescription.equals(other.dangerCategoryDescription))
			return false;
		if (defectsLastExaminationFixed == null) {
			if (other.defectsLastExaminationFixed != null)
				return false;
		} else if (!defectsLastExaminationFixed.equals(other.defectsLastExaminationFixed))
			return false;
		if (energyDemand == null) {
			if (other.energyDemand != null)
				return false;
		} else if (!energyDemand.equals(other.energyDemand))
			return false;
		if (examinationComplete == null) {
			if (other.examinationComplete != null)
				return false;
		} else if (!examinationComplete.equals(other.examinationComplete))
			return false;
		if (examinationDuration == null) {
			if (other.examinationDuration != null)
				return false;
		} else if (!examinationDuration.equals(other.examinationDuration))
			return false;
		if (examinationIncompleteReason == null) {
			if (other.examinationIncompleteReason != null)
				return false;
		} else if (!examinationIncompleteReason.equals(other.examinationIncompleteReason))
			return false;
		if (examinationResultDanger == null) {
			if (other.examinationResultDanger != null)
				return false;
		} else if (!examinationResultDanger.equals(other.examinationResultDanger))
			return false;
		if (examinationResultDefect == null) {
			if (other.examinationResultDefect != null)
				return false;
		} else if (!examinationResultDefect.equals(other.examinationResultDefect))
			return false;
		if (examinationResultDefectDate == null) {
			if (other.examinationResultDefectDate != null)
				return false;
		} else if (!examinationResultDefectDate.equals(other.examinationResultDefectDate))
			return false;
		if (examinationResultNoDefect == null) {
			if (other.examinationResultNoDefect != null)
				return false;
		} else if (!examinationResultNoDefect.equals(other.examinationResultNoDefect))
			return false;
		if (externalPortableUtilities == null) {
			if (other.externalPortableUtilities != null)
				return false;
		} else if (!externalPortableUtilities.equals(other.externalPortableUtilities))
			return false;
		if (frequencyControlledUtilities == null) {
			if (other.frequencyControlledUtilities != null)
				return false;
		} else if (!frequencyControlledUtilities.equals(other.frequencyControlledUtilities))
			return false;
		if (hardWiredLoads == null) {
			if (other.hardWiredLoads != null)
				return false;
		} else if (!hardWiredLoads.equals(other.hardWiredLoads))
			return false;
		if (internalPortableUtilities == null) {
			if (other.internalPortableUtilities != null)
				return false;
		} else if (!internalPortableUtilities.equals(other.internalPortableUtilities))
			return false;
		if (isolationChecked == null) {
			if (other.isolationChecked != null)
				return false;
		} else if (!isolationChecked.equals(other.isolationChecked))
			return false;
		if (isolationCompensationMeasures == null) {
			if (other.isolationCompensationMeasures != null)
				return false;
		} else if (!isolationCompensationMeasures.equals(other.isolationCompensationMeasures))
			return false;
		if (isolationCompensationMeasuresAnnotation == null) {
			if (other.isolationCompensationMeasuresAnnotation != null)
				return false;
		} else if (!isolationCompensationMeasuresAnnotation.equals(other.isolationCompensationMeasuresAnnotation))
			return false;
		if (isolationMesasurementProtocols == null) {
			if (other.isolationMesasurementProtocols != null)
				return false;
		} else if (!isolationMesasurementProtocols.equals(other.isolationMesasurementProtocols))
			return false;
		if (maxEnergyDemandExternal == null) {
			if (other.maxEnergyDemandExternal != null)
				return false;
		} else if (!maxEnergyDemandExternal.equals(other.maxEnergyDemandExternal))
			return false;
		if (maxEnergyDemandInternal == null) {
			if (other.maxEnergyDemandInternal != null)
				return false;
		} else if (!maxEnergyDemandInternal.equals(other.maxEnergyDemandInternal))
			return false;
		if (precautionsDeclared == null) {
			if (other.precautionsDeclared != null)
				return false;
		} else if (!precautionsDeclared.equals(other.precautionsDeclared))
			return false;
		if (precautionsDeclaredLocation == null) {
			if (other.precautionsDeclaredLocation != null)
				return false;
		} else if (!precautionsDeclaredLocation.equals(other.precautionsDeclaredLocation))
			return false;
		if (protectedCircuitsPercent == null) {
			if (other.protectedCircuitsPercent != null)
				return false;
		} else if (!protectedCircuitsPercent.equals(other.protectedCircuitsPercent))
			return false;
		if (rcdAnnotation == null) {
			if (other.rcdAnnotation != null)
				return false;
		} else if (!rcdAnnotation.equals(other.rcdAnnotation))
			return false;
		if (rcdAvailable == null) {
			if (other.rcdAvailable != null)
				return false;
		} else if (!rcdAvailable.equals(other.rcdAvailable))
			return false;
		if (rcdAvailablePercent == null) {
			if (other.rcdAvailablePercent != null)
				return false;
		} else if (!rcdAvailablePercent.equals(other.rcdAvailablePercent))
			return false;
		if (resistance == null) {
			if (other.resistance != null)
				return false;
		} else if (!resistance.equals(other.resistance))
			return false;
		if (resistanceAnnotation == null) {
			if (other.resistanceAnnotation != null)
				return false;
		} else if (!resistanceAnnotation.equals(other.resistanceAnnotation))
			return false;
		if (resistanceNumber == null) {
			if (other.resistanceNumber != null)
				return false;
		} else if (!resistanceNumber.equals(other.resistanceNumber))
			return false;
		if (subsequentExaminationDate == null) {
			if (other.subsequentExaminationDate != null)
				return false;
		} else if (!subsequentExaminationDate.equals(other.subsequentExaminationDate))
			return false;
		if (supplySystem == null) {
			if (other.supplySystem != null)
				return false;
		} else if (!supplySystem.equals(other.supplySystem))
			return false;
		if (surveyor == null) {
			if (other.surveyor != null)
				return false;
		} else if (!surveyor.equals(other.surveyor))
			return false;
		if (thermalAbnormality == null) {
			if (other.thermalAbnormality != null)
				return false;
		} else if (!thermalAbnormality.equals(other.thermalAbnormality))
			return false;
		if (thermalAbnormalityAnnotation == null) {
			if (other.thermalAbnormalityAnnotation != null)
				return false;
		} else if (!thermalAbnormalityAnnotation.equals(other.thermalAbnormalityAnnotation))
			return false;
		if (vdsApprovalNr == null) {
			if (other.vdsApprovalNr != null)
				return false;
		} else if (!vdsApprovalNr.equals(other.vdsApprovalNr))
			return false;
		if (defects == null) {
			if (other.defects != null)
				return false;
		} else if (!defects.equals(other.defects))
			return false;
		return true;
	}
}
