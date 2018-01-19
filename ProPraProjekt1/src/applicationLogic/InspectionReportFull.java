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
		return examinationResultDefectDate.format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
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
		return subsequentExaminationDate.format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
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
}
