package tr.com.srdc.cda2fhir.transform;

/*
 * #%L
 * CDA to FHIR Transformer Library
 * %%
 * Copyright (C) 2016 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. A.S.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.io.Serializable;

import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCriticality;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.dstu3.model.Encounter.EncounterStatus;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyHistoryStatus;
import org.hl7.fhir.dstu3.model.Group.GroupType;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus;
import org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus;
import org.hl7.fhir.dstu3.model.Timing.UnitsOfTime;

import org.hl7.fhir.dstu3.model.codesystems.AllergyIntoleranceStatus;

import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.vocab.EntityClassRoot;
import org.openhealthtools.mdht.uml.hl7.vocab.EntityNameUse;
import org.openhealthtools.mdht.uml.hl7.vocab.NullFlavor;
import org.openhealthtools.mdht.uml.hl7.vocab.PostalAddressUse;
import org.openhealthtools.mdht.uml.hl7.vocab.TelecommunicationAddressUse;

import tr.com.srdc.cda2fhir.util.Constants;

public class ValueSetsTransformerImpl implements IValueSetsTransformer, Serializable {

  public static final long serialVersionUID = 1L;

  /**
   * Transforms a CDA Administrative Gender to a FHIR Administrative Gender.
   */
  public Enumerations.AdministrativeGender transformAdministrativeGenderCode2AdministrativeGender(
      String cdaAdministrativeGenderCode) {
    switch (cdaAdministrativeGenderCode.toLowerCase()) {
      case "f":
        return Enumerations.AdministrativeGender.FEMALE;
      case "m":
        return Enumerations.AdministrativeGender.MALE;
      case "un":
        return Enumerations.AdministrativeGender.UNKNOWN;
      default:
        return Enumerations.AdministrativeGender.UNKNOWN;
    }
  }

  /**
   * Converts Cda Observation Unit to Age Unit.
   */
  public String transformAgeObservationUnit2AgeUnit(String cdaAgeObservationUnit) {
    if (cdaAgeObservationUnit == null || cdaAgeObservationUnit.isEmpty()) {
      return null;
    }

    switch (cdaAgeObservationUnit.toLowerCase()) {
      case "a":
        return "Year";
      case "mo":
        return "Month";
      case "wk":
        return "Week";
      case "d":
        return "Day";
      case "h":
        return "Hour";
      case "min":
        return "Minute";
      default:
        return null;
    }
  }

  /**
   * Transforms the Cda Allergy Category to FHIR Allergy Intolerance Category.
   */
  public AllergyIntoleranceCategory transformAllergyCategoryCode2AllergyIntoleranceCategory(
      String cdaAllergyCategoryCode) {
    if (cdaAllergyCategoryCode == null) {
      return null;
    }
    switch (cdaAllergyCategoryCode) {
      case "416098002":
      case "59037007":
      case "419511003":
        return AllergyIntoleranceCategory.MEDICATION;
      case "414285001":
      case "235719002":
      case "418471000":
        return AllergyIntoleranceCategory.FOOD;
      case "232347008":
        return AllergyIntoleranceCategory.ENVIRONMENT;
      case "420134006":
      case "418038007":
      case "419199007":
      default:
        return null;
    }
  }

  /**
   * Transforms the Cda Criticality to FHIR Allergy Intolerance Criticality.
   * @param cdaCriticalityObservationValue Cda Criticality.
   * @return Allergy Intolerance Criticality.
   */
  public AllergyIntoleranceCriticality 
      transformCriticalityObservationValue2AllergyIntoleranceCriticality(
      String cdaCriticalityObservationValue) {
    if (cdaCriticalityObservationValue == null || cdaCriticalityObservationValue.isEmpty()) {
      return null;
    }      
    switch (cdaCriticalityObservationValue.toLowerCase()) {
      case "critl":
        return AllergyIntoleranceCriticality.LOW;
      case "crith":
        return AllergyIntoleranceCriticality.HIGH;
      case "critu":
        return AllergyIntoleranceCriticality.UNABLETOASSESS;
      default:
        return null;
    }
  }

  /**
   * Transforms Encounter Code to Encounter Class.
   */
  public Coding transformEncounterCode2EncounterClass(String cdaEncounterCode) {
    if (cdaEncounterCode == null) {
      return null;
    }
    switch (cdaEncounterCode.toLowerCase()) {
      case "amb":
        return new Coding(
              Constants.ENCOUNTER_CLASS_SYSTEM, 
              cdaEncounterCode.toUpperCase(), "ambulatory");       
      case "ambulatory":
        return new Coding(
              Constants.ENCOUNTER_CLASS_SYSTEM, "AMB", cdaEncounterCode.toLowerCase());  
      case "out":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "AMB", "ambulatory");  
      case "outpatient":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "AMB", "ambulatory");
      case "in":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "IMP", "inpatient encounter");
      case "inp":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "IMP", "inpatient encounter");
      case "inpatient":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "IMP", "inpatient encounter");
      case "day":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "SS", "short stay");
      case "daytime":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "SS", "short stay");
      case "em":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "EMER", "emergency");
      case "eme":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "EMER", "emergency");
      case "emergency":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "EMER", "emergency");
      case "hom":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "HH", "home health");
      case "home":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "HH", "home health"); 
      case "vir":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "VR", "virtual");
      case "virtual":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "VR", "virtual");
      case "fie":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "FLD", "field");
      case "field":
        return new Coding(Constants.ENCOUNTER_CLASS_SYSTEM, "FLD", "field");
      case "other":
      case "oth":
      default:
        return null;

    }
  }

  /**
   * Transforms a CDA Entity Class Root to a Fhir Group Type.
   */
  public GroupType transformEntityClassRoot2GroupType(EntityClassRoot cdaEntityClassRoot) {    
    switch (cdaEntityClassRoot) {
      case PSN:
        return GroupType.PERSON;
      case ANM:
        return GroupType.ANIMAL;
      case DEV:
        return GroupType.DEVICE;
      case MMAT:
        return GroupType.MEDICATION;
      default:
        return null;
    }
  }

  /**
   * Transforms a Cda Entity Name Use to a Fhir Name Use.
   * @param cdaEntityNameUse Cda Name Use.
   * @return Fhir Name Use
   */
  public NameUse transformEntityNameUse2NameUse(EntityNameUse cdaEntityNameUse) {
    switch (cdaEntityNameUse) {
      case C:
        return NameUse.USUAL;
      case P:
        return NameUse.NICKNAME;
      default:
        return NameUse.USUAL;
    }
  }

  /**
   * Transforms CDA Family History Organizer Status to Fhir Family history Status.
   * @param cdaFamilyHistoryOrganizerStatusCode CDA Organization status.
   * @return Fhir Family history Family Status.
   */
  public FamilyHistoryStatus transformFamilyHistoryOrganizerStatusCode2FamilyHistoryStatus(
      String cdaFamilyHistoryOrganizerStatusCode) {
    switch (cdaFamilyHistoryOrganizerStatusCode.toLowerCase()) {
      case "completed":
        return FamilyHistoryStatus.COMPLETED;
      case "error":
        return FamilyHistoryStatus.ENTEREDINERROR;
      case "un":
        return FamilyHistoryStatus.HEALTHUNKNOWN;
      case "part":
        return FamilyHistoryStatus.PARTIAL;
      default:
        return null;
    }
  }

  /**
   * Transforms the Marital Status from CDA to FHIR.
   * @param cdaMaritalStatusCode CDA Marital Status Code.
   * @return FHIR Marital Status.
   */
  public CodeableConcept transformMaritalStatusCode2MaritalStatusCodes(
        String cdaMaritalStatusCode) {

    
    switch (cdaMaritalStatusCode.toUpperCase()) {
      case "A":
        return new CodeableConcept().addCoding(
              new Coding(
                Constants.MARITAL_STATUS_SYSTEM,cdaMaritalStatusCode.toUpperCase(), "Annulled"));
      case "D":
        return new CodeableConcept().addCoding(
              new Coding(
                Constants.MARITAL_STATUS_SYSTEM,cdaMaritalStatusCode.toUpperCase(), "Divorced"));
      case "I":
        return new CodeableConcept().addCoding(
              new Coding(
                Constants.MARITAL_STATUS_SYSTEM,
                cdaMaritalStatusCode.toUpperCase(), "Interlocutory"));
      case "L":
        return new CodeableConcept().addCoding(
            new Coding(
              Constants.MARITAL_STATUS_SYSTEM,
              cdaMaritalStatusCode.toUpperCase(), "Legally Seperated"));
      case "M":
        return new CodeableConcept().addCoding(
            new Coding(Constants.MARITAL_STATUS_SYSTEM,
              cdaMaritalStatusCode.toUpperCase(), "Married"));
      case "P":
        return new CodeableConcept().addCoding(
            new Coding(Constants.MARITAL_STATUS_SYSTEM,
              cdaMaritalStatusCode.toUpperCase(), "Polygamous"));
      case "S":
        return new CodeableConcept().addCoding(
            new Coding(Constants.MARITAL_STATUS_SYSTEM,
              cdaMaritalStatusCode.toUpperCase(), "Never Married"));
      case "T":
        return new CodeableConcept().addCoding(
            new Coding(Constants.MARITAL_STATUS_SYSTEM,
              cdaMaritalStatusCode.toUpperCase(), "Domestic partner"));
      case "W":
        return new CodeableConcept().addCoding(
            new Coding(Constants.MARITAL_STATUS_SYSTEM,
              cdaMaritalStatusCode.toUpperCase(), "Widowed"));
      case "UN":
        return new CodeableConcept().addCoding(
            new Coding(Constants.MARITAL_STATUS_SYSTEM,
              cdaMaritalStatusCode.toUpperCase(), "unknown"));
      default:
        return new CodeableConcept().addCoding(
            new Coding(Constants.MARITAL_STATUS_SYSTEM,"UNK", "unknown"));
    }
  }

  /**
   * Converts the NullFlavor to a FHIR Coding.
   */
  public Coding transformNullFlavor2DataAbsentReasonCode(NullFlavor cdaNullFlavor) {
    Coding dataAbsentReasonCode = new Coding();
    String code = null;
    String display = null;

    switch (cdaNullFlavor) {
      case UNK:
        code = "unknown";
        display = "Unkown";
        break;
      case ASKU:
        code = "asked";
        display = "Asked";
        break;
      case MSK:
        code = "masked";
        display = "Masked";
        break;
      case NA:
        code = "unsupported";
        display = "Unsupported";
        break;
      case NASK:
        code = "not-asked";
        display = "Not Asked";
        break;
      case NAV:
        code = "temp";
        display = "Temp";
        break;
      case NI:
        code = "error";
        display = "Error";
        break;
      case NINF:
        code = "NaN";
        display = "Not a Number";
        break;
      case NP:
        code = "unknown";
        display = "Unkown";
        break;
      case OTH:
        code = "error";
        display = "Error";
        break;
      case PINF:
        code = "NaN";
        display = "Not a Number";
        break;
      case TRC:
        code = "NaN";
        display = "Not a Number";
        break;
      default:
        break;
    }

    dataAbsentReasonCode.setSystem("http://hl7.org/fhir/data-absent-reason");
    dataAbsentReasonCode.setCode(code);
    dataAbsentReasonCode.setDisplay(display);

    return dataAbsentReasonCode;
  }

  /**
   * Converts Cda Observation Interpretation to Codeable Concept.
   * @param cdaObservationInterpretationCode Interpretation Code.
   * @return Codeable Concept.
   */
  public CodeableConcept transformObservationInterpretationCode2ObservationInterpretationCode(
      CD cdaObservationInterpretationCode) {
    if (cdaObservationInterpretationCode == null) {
      return null;
    }
    Coding obsIntCode = new Coding();
    obsIntCode.setSystem("http://hl7.org/fhir/v2/0078");

    String code = null;
    String display = null;

    // init code and display with the CDA incomings
    if (cdaObservationInterpretationCode.getCode() != null) {
      code = cdaObservationInterpretationCode.getCode();
    }    
    if (cdaObservationInterpretationCode.getDisplayName() != null) {
      display = cdaObservationInterpretationCode.getDisplayName();
    }
    
    // if a different code is found, change it
    if (cdaObservationInterpretationCode.getCode() != null) {
      switch (cdaObservationInterpretationCode.getCode().toUpperCase()) {
        case "AC":
          code = "IE";
          display = "Insufficient evidence";
          break;
        case "EX":
          code = "IND";
          display = "Indeterminate";
          break;
        case "HX":
          code = "H";
          display = "High";
          break;
        case "LX":
          code = "L";
          display = "Low";
          break;
        case "QCF":
          code = "IND";
          display = "Indeterminate";
          break;
        case "TOX":
          code = "IND";
          display = "Indeterminate";
          break;
        case "CAR":
          code = "DET";
          display = "Detected";
          break;
        case "H>":
          code = "HU";
          display = "Very high";
          break;
        case "L<":
          code = "LU";
          display = "Very low";
          break;
        default:
          break;
      }
    }
    obsIntCode.setCode(code);
    obsIntCode.setDisplay(display);
    return new CodeableConcept().addCoding(obsIntCode);
  }

  /**
   * Converts the ObservationStatusCode To an Observation Status.
   */
  public ObservationStatus transformObservationStatusCode2ObservationStatus(
        String cdaObservationStatusCode) {
    switch (cdaObservationStatusCode.toLowerCase()) {
      // TODO: https://www.hl7.org/fhir/valueset-observation-status.html and pdf page
      // 476
      // Check the following mapping
      case "new":
        return ObservationStatus.REGISTERED;
      case "held":
        return ObservationStatus.REGISTERED;
      case "normal":
        return ObservationStatus.PRELIMINARY;
      case "active":
        return ObservationStatus.PRELIMINARY;
      case "completed":
        return ObservationStatus.FINAL;
      case "error":
        return ObservationStatus.ENTEREDINERROR;
      case "cancelled":
        return ObservationStatus.CANCELLED;
      case "aborted":
        return ObservationStatus.CANCELLED;
      case "nullified":
        return ObservationStatus.CANCELLED;
      case "suspended":
        return ObservationStatus.CANCELLED;
      case "obsolete":
      default:
        return ObservationStatus.UNKNOWN;
    }
  }

  /**
   * Tranforms OID Value to URL.
   */
  public String transformOid2Url(String codeSystem) {
    String system = null;
    switch (codeSystem) {
      case "2.16.840.1.113883.6.96":
        system = "http://snomed.info/sct";
        break;
      case "2.16.840.1.113883.6.88":
        system = "http://www.nlm.nih.gov/research/umls/rxnorm";
        break;
      case "2.16.840.1.113883.6.1":
        system = "http://loinc.org";
        break;
      case "2.16.840.1.113883.6.8":
        system = "http://unitsofmeasure.org";
        break;
      case "2.16.840.1.113883.3.26.1.2":
        system = "http://ncimeta.nci.nih.gov";
        break;
      case "2.16.840.1.113883.6.12":
        system = "http://www.ama-assn.org/go/cpt";
        break;
      case "2.16.840.1.113883.6.209":
        system = "http://hl7.org/fhir/ndfrt";
        break;
      case "2.16.840.1.113883.4.9":
        system = "http://fdasis.nlm.nih.gov";
        break;
      case "2.16.840.1.113883.12.292":
        system = "http://www2a.cdc.gov/vaccines/iis/iisstandards/vaccines.asp?rpt=cvx";
        break;
      case "1.0.3166.1.2.2":
        system = "urn:iso:std:iso:3166";
        break;
      case "2.16.840.1.113883.6.301.5":
        system = "http://www.nubc.org/patient-discharge";
        break;
      case "2.16.840.1.113883.6.256":
        system = "http://www.radlex.org";
        break;
      case "2.16.840.1.113883.6.3":
        system = "http://hl7.org/fhir/sid/icd-10";
        break;
      case "2.16.840.1.113883.6.4":
        system = "http://www.icd10data.com/icd10pcs";
        break;
      case "2.16.840.1.113883.6.42":
        system = "http://hl7.org/fhir/sid/icd-9";
        break;
      case "2.16.840.1.113883.6.73":
        system = "http://www.whocc.no/atc";
        break;
      case "2.16.840.1.113883.6.24":
        system = "urn:std:iso:11073:10101";
        break;
      case "1.2.840.10008.2.16.4":
        system = "http://nema.org/dicom/dicm";
        break;
      case "2.16.840.1.113883.6.281":
        system = "http://www.genenames.org";
        break;
      case "2.16.840.1.113883.6.280":
        system = "http://www.ncbi.nlm.nih.gov/nuccore";
        break;
      case "2.16.840.1.113883.6.282":
        system = "http://www.hgvs.org/mutnomen";
        break;
      case "2.16.840.1.113883.6.284":
        system = "http://www.ncbi.nlm.nih.gov/projects/SNP";
        break;
      case "2.16.840.1.113883.3.912":
        system = "http://cancer.sanger.ac.uk/cancergenome/projects/cosmic";
        break;
      case "2.16.840.1.113883.6.283":
        system = "http://www.hgvs.org/mutnomen";
        break;
      case "2.16.840.1.113883.6.174":
        system = "http://www.omim.org";
        break;
      case "2.16.840.1.113883.13.191":
        system = "http://www.ncbi.nlm.nih.gov/pubmed";
        break;
      case "2.16.840.1.113883.3.913":
        system = "http://www.pharmgkb.org";
        break;
      case "2.16.840.1.113883.3.1077":
        system = "http://clinicaltrials.gov";
        break;
      default:
        system = "urn:oid:" + codeSystem;
        break;
    }
    return system;
  }

  /**
   * Transforms the Participant Type to FHIR Coding.
   */
  public CodeableConcept transformParticipationType2ParticipationTypeCode(
      org.openhealthtools.mdht.uml.hl7.vocab.ParticipationType cdaParticipationType) {
    Coding fhirParticipationType = new Coding();
    fhirParticipationType.setSystem(Constants.PARTICIPANT_TYPE_SYSTEM);
    String code = null;
    String display = null;

    switch (cdaParticipationType) {
      case PRF:
        code = "PRF";
        display = "performer";
        break;
      case SBJ:
        code = "SBJ";
        display = "subject";
        break;
      case ADM:
        code = "ADM";
        display = "admitter";
        break;
      case ATND:
        code = "ATND";
        display = "attender";
        break;
      case AUT:
        code = "AUT";
        display = "author";
        break;
      case AUTHEN:
        code = "AUTHEN";
        display = "authenticator";
        break;
      case BBY:
        code = "BBY";
        display = "baby";
        break;
      case BEN:
        code = "BEN";
        display = "beneficiary";
        break;
      case CALLBCK:
        code = "CALLBCK";
        display = "callback contact";
        break;
      case CON:
        code = "CON";
        display = "consultant";
        break;
      case COV:
        code = "COV";
        display = "coverage target";
        break;
      case CSM:
        code = "CSM";
        display = "consumable";
        break;
      case CST:
        code = "CST";
        display = "custodian";
        break;
      case DEV:
        code = "DEV";
        display = "device";
        break;
      case DIR:
        code = "DIR";
        display = "direct target";
        break;
      case DIS:
        code = "DIS";
        display = "discharger";
        break;
      case DIST:
        code = "DIST";
        display = "distributor";
        break;
      case DON:
        code = "DON";
        display = "donor";
        break;
      case DST:
        code = "DST";
        display = "destination";
        break;
      case ELOC:
        code = "ELOC";
        display = "entry location";
        break;
      case ENT:
        code = "ENT";
        display = " data entry person";
        break;
      case ESC:
        code = "ESC";
        display = "escort";
        break;
      case HLD:
        code = "HLD";
        display = "holder";
        break;
      case IND:
        code = "IND";
        display = "indirect target";
        break;
      case INF:
        code = "INF";
        display = "informant";
        break;
      case IRCP:
        code = "IRCP";
        display = "information recipient";
        break;
      case LA:
        code = "LA";
        display = "legal authenticator";
        break;
      case LOC:
        code = "LOC";
        display = "location";
        break;
      case NOT:
        code = "NOT";
        display = "ugent notification contact";
        break;
      case NRD:
        code = "NRD";
        display = "non-reuseable device";
        break;
      case ORG:
        code = "ORG";
        display = "origin";
        break;
      case PPRF:
        code = "PPRF";
        display = "primary performer";
        break;
      case PRCP:
        code = "PRCP";
        display = "primary information recipient";
        break;
      case PRD:
        code = "PRD";
        display = "product";
        break;
      case RCT:
        code = "RCT";
        display = "record target";
        break;
      case RCV:
        code = "RCV";
        display = "receiver";
        break;
      case RDV:
        code = "RDV";
        display = "reusable device";
        break;
      case REF:
        code = "REF";
        display = "referrer";
        break;
      case REFB:
        code = "REFB";
        display = "Referred By";
        break;
      case REFT:
        code = "REFT";
        display = "Referred to";
        break;
      case RESP:
        code = "RESP";
        display = "responsible party";
        break;
      case RML:
        code = "RML";
        display = "remote";
        break;
      case SPC:
        code = "SPC";
        display = "specimen";
        break;
      case SPRF:
        code = "SPRF";
        display = "secondary performer";
        break;
      case TRC:
        code = "TRC";
        display = "tracker";
        break;
      case VIA:
        code = "VIA";
        display = "via";
        break;
      case VRF:
        code = "VRF";
        display = "verifier";
        break;
      case WIT:
        code = "WIT";
        display = "witness";
        break;
      default:
        break;
    }
    if (code != null && display != null) {
      fhirParticipationType.setCode(code);
      fhirParticipationType.setDisplay(display);
    }
    return new CodeableConcept().addCoding(fhirParticipationType);
  }


  /**
   * Transforms the Cda Period Units to a Period Unit of Time.
   * @param cdaPeriodUnit Cda Unit of time.
   * @return Fhir Period Unit of Time.
   */
  public UnitsOfTime transformPeriodUnit2UnitsOfTime(String cdaPeriodUnit) {
    switch (cdaPeriodUnit.toLowerCase()) {
      case "a":
        return UnitsOfTime.A;
      case "d":
        return UnitsOfTime.D;
      case "h":
        return UnitsOfTime.H;
      case "min":
        return UnitsOfTime.MIN;
      case "mo":
        return UnitsOfTime.MO;
      case "s":
        return UnitsOfTime.S;
      case "wk":
        return UnitsOfTime.WK;
      default:
        return null;
    }
  }

  /**
   * Transforms the CDA Postal Address Use to FHIR Address Type.
   * @param cdaPostalAddressUse CDA Postal Address Use.
   * @return FHIR Address Use.
   */
  public AddressType transformPostalAddressUse2AddressType(PostalAddressUse cdaPostalAddressUse) {
    switch (cdaPostalAddressUse) {
      case PHYS:
        return AddressType.PHYSICAL;
      case PST:
        return AddressType.POSTAL;
      default:
        return null;
    }
  }

  /**
   * Transforms the Cda Postal Address Use to the FHIR Address Use.
   * @param cdaPostalAddressUse Cda Postal Address Use.
   * @return FHIR Address Use.
   */
  public AddressUse transformPostalAdressUse2AddressUse(PostalAddressUse cdaPostalAddressUse) {
    switch (cdaPostalAddressUse) {
      case HP:
        return AddressUse.HOME;
      case H:
        return AddressUse.HOME;
      case WP:
        return AddressUse.WORK;
      case TMP:
        return AddressUse.TEMP;
      case BAD:
        return AddressUse.OLD;
      default:
        return AddressUse.TEMP;
    }
  }

  /**
   * Transforms the Cda Problem Type to the Condition Category.
   * Recommend using a Concept Map to translate this operation better.
   * @param cdaProblemType CDA Problem Type.
   * @return FHIR Condition. 
   */
  public CodeableConcept transformProblemType2ConditionCategoryCode(String cdaProblemType) {
    if (cdaProblemType == null) {
      return null;
    }

    switch (cdaProblemType) {
      case "248536006":
      case "373930000":
      case "404684003":
      case "75321-0":
      case "75312-9":
        return new CodeableConcept().addCoding(
            new Coding(
                Constants.CONDITION_CATEGORY_SYSTEM, "encounter-diagnosis", "Encounter Diagnosis"));
      case "409586006":
      case "75322-8":
      case "75313-7":
        return new CodeableConcept().addCoding(
            new Coding(
              Constants.CONDITION_CATEGORY_SYSTEM, "problem-list-item", "Problem List Item"));
      case "282291009":
      case "29308-4":
      case "75314-5":
      case "55607006": // problem
        return new CodeableConcept().addCoding(
            new Coding(
              Constants.CONDITION_CATEGORY_SYSTEM, "problem-list-item", "Problem List Item"));
      case "75318-6":
      case "75323-6": // condition
        return new CodeableConcept().addCoding(
          new Coding(
            Constants.CONDITION_CATEGORY_SYSTEM, "encounter-diagnosis", "Encounter Diagnosis"));
      case "75315-2":
      case "64572001":
        return new CodeableConcept().addCoding(
          new Coding(
            Constants.CONDITION_CATEGORY_SYSTEM, "encounter-diagnosis", "Encounter Diagnosis"));
      case "418799008":
      case "75325-1":
      case "75317-8":
        return new CodeableConcept().addCoding(
          new Coding(
            Constants.CONDITION_CATEGORY_SYSTEM, "problem-list-item", "Problem List Item"));
      default:
        return null;
    }
  }

  /**
   * Transforms a Result Organizer Status to a Diagnostic Report Status.
   * @param cdaResultOrganizerStatusCode Cda Organizer status.
   * @return Diagnostic Report Status
   */
  public DiagnosticReportStatus transformResultOrganizerStatusCode2DiagnosticReportStatus(
      String cdaResultOrganizerStatusCode) {
    if (cdaResultOrganizerStatusCode == null) {
      return null;
    }

    switch (cdaResultOrganizerStatusCode.toLowerCase()) {
      case "aborted":
        return DiagnosticReportStatus.CANCELLED;
      case "active":
        return DiagnosticReportStatus.PARTIAL;
      case "cancelled":
        return DiagnosticReportStatus.CANCELLED;
      case "completed":
        return DiagnosticReportStatus.FINAL;
      case "held":
        return DiagnosticReportStatus.REGISTERED;
      case "suspended":
        return DiagnosticReportStatus.ENTEREDINERROR;
      default:
        return null;
    }
  }

  /**
   * Transforms the Cda Role Code into a Contact Relationship Codeable Concept.
   * This would be best to use a Concept Map to define the Coding.
   */
  public CodeableConcept transformRoleCode2PatientContactRelationshipCode(String cdaRoleCode) {
    if (cdaRoleCode == null) {
      return null;
    }

    Coding fhirPatientContactRelationshipCode = new Coding();
    fhirPatientContactRelationshipCode.setSystem(Constants.CONTACT_RELATIONSHIP_SYSTEM);
    String code = null;
    String display = null;

    switch (cdaRoleCode.toLowerCase()) {
      case "econ": // emergency contact
        code = "emergency";
        display = "Emergency";
        break;
      case "ext": // extended family member
      case "fammemb": // family member
        code = "family";
        display = "Family";
        break;
      case "guard": // guardian
        code = "guardian";
        display = "Guardian";
        break;
      case "frnd": // friend
        code = "friend";
        display = "Friend";
        break;
      case "sps": // spouse
      case "dompart": // domestic partner
      case "husb": // husband
      case "wife": // wife
        code = "partner";
        display = "Partner";
        break;
      case "work":
        code = "work";
        display = "Work";
        break;
      case "gt":
        code = "guarantor";
        display = "Guarantor";
        break;
      case "prn": // parent
      case "fth": // father
      case "mth": // mother
      case "nprn": // natural parent
      case "nfth": // natural father
      case "nmth": // natural mother
      case "prinlaw": // parent in-law
      case "fthinlaw": // father in-law
      case "mthinlaw": // mother in-law
      case "stpprn": // step parent
      case "stpfth": // stepfather
      case "stpmth": // stepmother
        code = "parent";
        display = "Parent";
        break;
      case "powatt":
        code = "agent";
        display = "Agent";
        break;
      default:
        return null;
    }
    fhirPatientContactRelationshipCode.setCode(code);
    fhirPatientContactRelationshipCode.setDisplay(display);
    return new CodeableConcept().addCoding(fhirPatientContactRelationshipCode);
  }

  /**
   * Transforms Cda Severity Code to Allergy Intolerance Severity.
   * This should probably move to a support Concept Maps.
   */
  public AllergyIntoleranceSeverity transformSeverityCode2AllergyIntoleranceSeverity(
      String cdaSeverityCode) {
    if (cdaSeverityCode == null) {
      return null;
    }
  
    switch (cdaSeverityCode) {
      case "255604002":
        return AllergyIntoleranceSeverity.MILD;
      case "371923003":
        return AllergyIntoleranceSeverity.MILD;
      case "6736007":
        return AllergyIntoleranceSeverity.MODERATE;
      case "371924009":
        return AllergyIntoleranceSeverity.MODERATE;
      case "24484000":
        return AllergyIntoleranceSeverity.SEVERE;
      case "399166001":
        return AllergyIntoleranceSeverity.SEVERE;
      default:
        return null;
    }
  }

  /**
   * Transforms Status Code to Allergy Intolerance Status.
   */
  public AllergyIntoleranceStatus transformStatusCode2AllergyIntoleranceStatus(
        String cdaStatusCode) {
    switch (cdaStatusCode.toLowerCase()) {
      case "active":
        return AllergyIntoleranceStatus.ACTIVE;
      case "nullified":
      case "error":
        return AllergyIntoleranceStatus.ENTEREDINERROR;
      case "confirmed":
        return AllergyIntoleranceStatus.ACTIVECONFIRMED;
      case "unconfirmed":
        return AllergyIntoleranceStatus.ACTIVE;
      case "refuted":
        return AllergyIntoleranceStatus.REFUTED;
      case "inactive":
        return AllergyIntoleranceStatus.INACTIVE;
      case "resolved":
        return AllergyIntoleranceStatus.RESOLVED;
      default:
        return null;
    }
  }

  /**
   * Tranforms a Status Code to a Condition Clinical Status.
   * @param cdaStatusCode Cda Status
   * @return Condition Clinical Status
   */
  public ConditionClinicalStatus transformStatusCode2ConditionClinicalStatusCodes(
        String cdaStatusCode) {
    switch (cdaStatusCode.toLowerCase()) {
      // semantically not the same, but at least outcome-wise it is similar
      case "aborted":
        return ConditionClinicalStatus.RESOLVED;
      case "active":
        return ConditionClinicalStatus.ACTIVE;
      case "completed":
        return ConditionClinicalStatus.RESOLVED;
      case "suspended":
        return ConditionClinicalStatus.REMISSION;
      default:
        return null;
    }
  }

  /**
   * Transforms Status Code to an Encounter Status Code.
   * @param cdaStatusCode CDA Status Code.
   * @return Encounter Status Code
   */
  public EncounterStatus transformStatusCode2EncounterStatus(String cdaStatusCode) {
    switch (cdaStatusCode.toLowerCase()) {
      case "in-progress":
        return EncounterStatus.INPROGRESS;
      case "active":
        return EncounterStatus.INPROGRESS;
      case "onleave":
        return EncounterStatus.ONLEAVE;
      case "finished":
        return EncounterStatus.FINISHED;
      case "completed":
        return EncounterStatus.FINISHED;
      case "cancelled":
        return EncounterStatus.CANCELLED;
      case "planned":
        return EncounterStatus.PLANNED;
      case "arrived":
        return EncounterStatus.ARRIVED;
      default:
        return null;
    }
  }

  /**
   * Transforms Status Code into FHIR Medication Dispense Status.
   * @param cdaStatusCode Cda Status
   * @return Medication Dispense Status.
   */
  public MedicationDispenseStatus transformStatusCode2MedicationDispenseStatus(
        String cdaStatusCode) {
    switch (cdaStatusCode.toLowerCase()) {
      case "active":
        return MedicationDispenseStatus.INPROGRESS;
      case "in-progress":
        return MedicationDispenseStatus.INPROGRESS;
      case "inprogress":
        return MedicationDispenseStatus.INPROGRESS;
      case "on-hold":
        return MedicationDispenseStatus.ONHOLD;
      case "onhold":
        return MedicationDispenseStatus.ONHOLD;
      case "suspended":
        return MedicationDispenseStatus.ONHOLD;
      case "completed":
        return MedicationDispenseStatus.COMPLETED;
      case "nullified":
        return MedicationDispenseStatus.ENTEREDINERROR;
      case "error":
        return MedicationDispenseStatus.ENTEREDINERROR;
      case "entered-in-error":
        return MedicationDispenseStatus.ENTEREDINERROR;
      case "stopped":
        return MedicationDispenseStatus.STOPPED;
      default:
        return null;
    }
  }

  /**
   * Transforms Status Code into a Medication Statement Status.
   */
  public MedicationStatementStatus transformStatusCode2MedicationStatementStatus(
        String cdaStatusCode) {
    switch (cdaStatusCode.toLowerCase()) {
      case "active":
        return MedicationStatementStatus.ACTIVE;
      case "intended":
        return MedicationStatementStatus.INTENDED;
      case "completed":
        return MedicationStatementStatus.COMPLETED;
      case "nullified":
        return MedicationStatementStatus.ENTEREDINERROR;
      default:
        return null;
    }
  }

  /**
   * Tranforms a Status Code into a Procedure Status.
   * @param cdaStatusCode Cda Status Code.
   * @return FHIR Procedure Status.
   */
  public ProcedureStatus transformStatusCode2ProcedureStatus(String cdaStatusCode) {
    switch (cdaStatusCode.toLowerCase()) {
      case "active":
        return ProcedureStatus.INPROGRESS;
      case "completed":
        return ProcedureStatus.COMPLETED;
      case "aborted":
        return ProcedureStatus.ABORTED;
      case "aboted":
        return ProcedureStatus.ABORTED;
      case "error":
        return ProcedureStatus.ENTEREDINERROR;
      default:
        return null;
    }
  }

  /**
   * Tranforms Telecommunication Address Use to Contact Point Use.
   */
  public ContactPointUse transformTelecommunicationAddressUse2ContactPointUse(
      TelecommunicationAddressUse cdaTelecommunicationAddressUse) {
    switch (cdaTelecommunicationAddressUse) {
      case H:
        return ContactPointUse.HOME;
      case HP:
        return ContactPointUse.HOME;
      case WP:
        return ContactPointUse.WORK;
      case TMP:
        return ContactPointUse.TEMP;
      case BAD:
        return ContactPointUse.OLD;
      case MC:
        return ContactPointUse.MOBILE;
      default:
        return ContactPointUse.TEMP;
    }

  }

  /**
   * Tranforms a TelValue to a Contact Point System.
   * @param cdaTelValue Cda Tel Value.
   * @return FHIR Contact Point System.
   */
  public ContactPointSystem transformTelValue2ContactPointSystem(String cdaTelValue) {
    if (cdaTelValue == null) {
      return null;
    }    

    switch (cdaTelValue.toLowerCase()) {
      case "phone":
        return ContactPointSystem.PHONE;
      case "tel":
        return ContactPointSystem.PHONE;
      case "email":
        return ContactPointSystem.EMAIL;
      case "mailto":
        return ContactPointSystem.EMAIL;
      case "fax":
        return ContactPointSystem.FAX;
      case "http":
        return ContactPointSystem.URL;
      case "https":
        return ContactPointSystem.URL;
      default:
        return null;
    }
  }

  /**
   * Utilizes a Concept Map to translate a given Code into a Fhir Coding Element.
   */
  public Coding transformValueToCoding(String codeValue, ConceptMap map) {
    Coding coding = new Coding();
    for (ConceptMapGroupComponent group : map.getGroup()) {
      for (SourceElementComponent element : group.getElement()) {
        if (element.getCode().equals(codeValue)) {
          for (TargetElementComponent target : element.getTarget()) {
            coding.setSystem(group.getTarget());
            coding.setCode(target.getCode());
            coding.setDisplay(target.getDisplay());
          }
        }
      }
    }
    return coding;
  }
}