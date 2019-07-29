package tr.com.srdc.cda2fhir.transform;

import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceClinicalStatus;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCriticality;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceVerificationStatus;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
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

//import ca.uhn.fhir.model.dstu2.valueset.*;

import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.vocab.EntityClassRoot;
import org.openhealthtools.mdht.uml.hl7.vocab.EntityNameUse;
import org.openhealthtools.mdht.uml.hl7.vocab.NullFlavor;
import org.openhealthtools.mdht.uml.hl7.vocab.PostalAddressUse;
import org.openhealthtools.mdht.uml.hl7.vocab.TelecommunicationAddressUse;

//import ca.uhn.fhir.model.dstu2.composite.CodeableConcept;
//import ca.uhn.fhir.model.dstu2.composite.Coding;

public interface IValueSetsTransformer {


  /**
   * Transforms a Cda Code Value to the Provided FHIR 
   * Code/Coding/Codeable Concept utilizing a Concept Map.
   * @param <T> Return Type (Code Enum/Coding/Codeable Concept)
   * @param cdaCodeValue Cda Code value to transform.
   * @param map Concept Map to apply
   * @param clazz Return Class Type
   * @return Code/Coding/Codeable Concept
   */
  <T> T transformCdaValueToFhirCodeValue(
        final String cdaCodeValue, ConceptMap map, Class<T> clazz);

  /**
   * Transforms a CDA AdministrativeGenderCode string to a FHIR
   * AdministrativeGender.
   * 
   * @param cdaAdministrativeGenderCode A CDA AdministrativeGenderCode string
   * @return A value from the FHIR valueset AdministrativeGender
   */
  Enumerations.AdministrativeGender transformAdministrativeGenderCode2AdministrativeGender(
        String cdaAdministrativeGenderCode);

  /**
   * Transforms a CDA AgeObservationUnit string to a FHIR AgeUnit string.
   * 
   * @param cdaAgeObservationUnit A CDA AgeObservationUnit string
   * @return A FHIR AgeUnit string
   */
  String transformAgeObservationUnit2AgeUnit(String cdaAgeObservationUnit);

  /**
   * Transforms a CDA AllergyCategoryCode string to a FHIR
   * AllergyIntoleranceCategory.
   * 
   * @param cdaAllergyCategoryCode A CDA AllergyCategoryCode string
   * @return A value from the FHIR valueset AllergyIntoleranceCategory
   */
  AllergyIntoleranceCategory transformAllergyCategoryCode2AllergyIntoleranceCategory(
        String cdaAllergyCategoryCode);

  /**
   * Transforms a CDA CriticalityObservation's value's code string to a FHIR
   * AllergyIntoleranceCriticality.
   * 
   * @param cdaCriticalityObservationValue A CDA CriticalityObservation's value's
   *                                       code string
   * @return A value from the FHIR valueset AllergyIntolerancecriticality
   */
  AllergyIntoleranceCriticality transformCriticalityObservationValue2AllergyIntoleranceCriticality(
      String cdaCriticalityObservationValue);

  /**
   * Transforms a CDA EncounterCode string to a FHIR EncounterClass.
   * 
   * @param cdaEncounterCode A CDA EncounterCode string
   * @return A value from the FHIR valueset EncounterClass
   */

  Coding transformEncounterCode2EncounterClass(String cdaEncounterCode);

  /**
   * Transforms a CDA EntityClassRoot vocable to a value from the FHIR valueset
   * GroupType.
   * 
   * @param cdaEntityClassRoot A CDA EntityClassRoot vocable
   * @return A value from the FHIR valueset GroupType
   */
  GroupType transformEntityClassRoot2GroupType(EntityClassRoot cdaEntityClassRoot);

  /**
   * Transforms a CDA EntityNameUse vocable to a value from the FHIR valueset
   * NameUse.
   * 
   * @param cdaEntityNameUse A CDA EntityNameUse vocable
   * @return A value from the FHIR valueset NameUse
   */
  NameUse transformEntityNameUse2NameUse(EntityNameUse cdaEntityNameUse);

  /**
   * Transforms a CDA FamilyHistoryOrganizerStatusCode string to a value from the
   * FHIR valueset FamilyHistoryStatus.
   * 
   * @param cdaFamilyHistoryOrganizerStatusCode A CDA
   *                                            FamilyHistoryOrganizerStatusCode
   *                                            string
   * @return A value from the FHIR valueset FamilyHistoryStatus
   */
  FamilyHistoryStatus transformFamilyHistoryOrganizerStatusCode2FamilyHistoryStatus(
      String cdaFamilyHistoryOrganizerStatusCode);

  /**
   * Transforms a CDA MaritalStatusCode string to a value from the FHIR valueset
   * MaritalStatusCodes.
   * 
   * @param cdaMaritalStatusCode A CDA MaritalStatusCode string
   * @return A value from the FHIR valueset MaritalStatusCodes
   */
  CodeableConcept transformMaritalStatusCode2MaritalStatusCodes(String cdaMaritalStatusCode);

  /**
   * Transforms a CDA NullFlavor vocable to a FHIR Coding composite datatype
   * which includes the code about DataAbsentReason.
   * 
   * @param cdaNullFlavor A CDA NullFlavor vocable
   * @return A FHIR Coding composite datatype which includes the code about
   *         DataAbsentReason.
   */
  Coding transformNullFlavor2DataAbsentReasonCode(NullFlavor cdaNullFlavor);

  /**
   * Transforms a CDA Observation Interpretation Code to a FHIR CodeableConcept
   * composite datatype which includes the code about Observation Interpretation.
   * 
   * @param cdaObservationInterpretationCode A CDA Observation Interpretation Code
   * @return A FHIR CodeableConcept composite datatype which includes the code
   *         about Observation Interpretation
   */
  CodeableConcept transformObservationInterpretationCode2ObservationInterpretationCode(
      CD cdaObservationInterpretationCode);

  /**
   * Transforms a CDA ObservationStatusCode string to a value from the FHIR
   * valueset ObservationStatus.
   * 
   * @param cdaObservationStatusCode A CDA ObservationStatusCode string
   * @return A value from the FHIR valueset ObservationStatus
   */
  ObservationStatus transformObservationStatusCode2ObservationStatus(
        String cdaObservationStatusCode);

  /**
   * Transforms a CodeSystem string to a URL string.
   * 
   * @param codeSystem a CodeSystem string
   * @return A URL string
   */
  String transformOid2Url(String codeSystem);

  /**
   * Transforms a CDA ParticipationType vocable to a FHIR Coding composite
   * datatype which includes the code about ParticipationType.
   * 
   * @param cdaParticipationType A CDA ParticipationType vocable
   * @return A FHIR Coding composite datatype which includes the code about
   *         ParticipationType
   */
  CodeableConcept transformParticipationType2ParticipationTypeCode(
      org.openhealthtools.mdht.uml.hl7.vocab.ParticipationType cdaParticipationType);

  /**
   * Transforms a CDA PeriodUnit string to a value from the FHIR valueset
   * UnitsOfTime.
   * 
   * @param cdaPeriodUnit A CDA PeriodUnit string
   * @return A value from the FHIR valueset UnitsOfTime.
   */
  UnitsOfTime transformPeriodUnit2UnitsOfTime(String cdaPeriodUnit);

  /**
   * Transforms a CDA PostalAddressUse vocable to a value from the FHIR valueset
   * AddressType.
   * 
   * @param cdaPostalAddressUse A CDA PostalAddressUse vocable
   * @return A value from the FHIR valueset AddressType
   */
  AddressType transformPostalAddressUse2AddressType(PostalAddressUse cdaPostalAddressUse);

  /**
   * Transforms a CDA PostalAddressUse vocable to a value from the FHIR valueset
   * AddressUse.
   * 
   * @param cdaPostalAddressUse A CDA PostalAddressUse vocable
   * @return A value from the FHIR valueset AddressUse
   */
  AddressUse transformPostalAdressUse2AddressUse(PostalAddressUse cdaPostalAddressUse);

  /**
   * Transforms a CDA ProblemType string to a value from the FHIR valuset
   * ConditionCategoryCodes.
   * 
   * @param cdaProblemType A CDA ProblemType string
   * @return A value from the FHIR valuset ConditionCategoryCodes
   */
  CodeableConcept transformProblemType2ConditionCategoryCode(String cdaProblemType);

  /**
   * Transforms a CDA ResultOrganizer StatusCode string to a value from the FHIR
   * valueset DiagnosticReportStatus.
   * 
   * @param cdaResultOrganizerStatusCode A CDA ResultOrganizer StatusCode string
   * @return A value from the FHIR valueset DiagnosticReportStatus
   */
  DiagnosticReportStatus transformResultOrganizerStatusCode2DiagnosticReportStatus(
        String cdaResultOrganizerStatusCode);

  /**
   * Transforms a CDA RoleCode string to a FHIR Coding composite datatype which
   * includes the code about PatientContactRelationship.
   * 
   * @param cdaRoleCode A CDA RoleCode string
   * @return A FHIR Coding composite datatype which includes the code about
   *         PatientContactRelationship
   */
  CodeableConcept transformRoleCode2PatientContactRelationshipCode(String cdaRoleCode);

  /**
   * Transforms a CDA SeverityCode string to a value from the FHIR valueset
   * AllergyIntoleranceSeverity.
   * 
   * @param cdaSeverityCode A CDA SeverityCode string
   * @return A value from the FHIR valueset AllergyIntoleranceSeverity.
   */
  AllergyIntoleranceSeverity transformSeverityCode2AllergyIntoleranceSeverity(
        String cdaSeverityCode);

  /**
   * Transforms a CDA StatusCode string to a value from the FHIR valueset
   * AllergyIntoleranceStatus.
   * 
   * @param cdaStatusCode A CDA StatusCode string
   * @return A value from the FHIR valueset AllergyIntoleranceStatus
   */
  AllergyIntoleranceClinicalStatus transformStatusCode2AllergyClinicalStatus(
        String cdaStatusCode);
  
  /**
   * Converts the CDA Status Code to the Allergy Intollerance Verification Status Code.
   * @param cdaStatusCode CDA Status Code.
   * @return Allergy Intollerance Verification Status.
   */
  AllergyIntoleranceVerificationStatus transformStatusCode2AllergyVerificationStatus(
        String cdaStatusCode);

  /**
   * Transforms a CDA StatusCode string to a value from the FHIR valueset
   * ConditionClinicalStatusCodes.
   * 
   * @param cdaStatusCode A CDA StatusCode string
   * @return A value from the FHIR valueset ConditionClinicalStatusCodes
   */
  ConditionClinicalStatus transformStatusCode2ConditionClinicalStatusCodes(String cdaStatusCode);

  /**
   * Transforms a CDA StatusCode string to a value from the FHIR valueset
   * EncounterStatus.
   * 
   * @param cdaStatusCode A CDA StatusCode string
   * @return A value from the FHIR valueset EncounterStatus
   */
  EncounterStatus transformStatusCode2EncounterStatus(String cdaStatusCode);

  /**
   * Transforms a CDA StatusCode string to a value from the FHIR valueset
   * MedicationDispenseStatus.
   * 
   * @param cdaStatusCode A CDA StatusCode string
   * @return A value from the FHIR valueset MedicationDispenseStatus
   */
  MedicationDispenseStatus transformStatusCode2MedicationDispenseStatus(String cdaStatusCode);

  /**
   * Transforms a CDA StatusCode string to a value from the FHIR valueset
   * MedicationStatementStatus.
   * 
   * @param cdaStatusCode A CDA StatusCode string
   * @return A value from the FHIR valueset MedicationStatementStatus
   */
  MedicationStatementStatus transformStatusCode2MedicationStatementStatus(String cdaStatusCode);

  /**
   * Transforms a CDA StatusCode string to a value from the FHIR valueset
   * ProcedureStatus.
   * 
   * @param cdaStatusCode A CDA StatusCode string
   * @return A value from the FHIR valueset ProcedureStatus
   */
  ProcedureStatus transformStatusCode2ProcedureStatus(String cdaStatusCode);

  /**
   * Transforms a CDA TelecommunicationAddressUse vocable to a value from the FHIR
   * valueset ContactPointUse.
   * 
   * @param cdaTelecommunicationAddressUse A CDA TelecommunicationAddressUse
   *                                       vocable
   * @return A value from the FHIR valueset ContactPointUse
   */
  ContactPointUse transformTelecommunicationAddressUse2ContactPointUse(
      TelecommunicationAddressUse cdaTelecommunicationAddressUse);

  /**
   * Transforms a CDA TelValue string to a value from the FHIR valueset
   * ContactPointSystem.
   * 
   * @param cdaTelValue A CDA TelValue string
   * @return A value from the FHIR valueset ContactPointSystem.
   */
  ContactPointSystem transformTelValue2ContactPointSystem(String cdaTelValue);

  /**
   * Uses a Concept Map and CDA code to map to the appropriate Coding.
   * @param cdaCodeValue Code Value from the CDA
   * @param map Concept Map to apply to produce the correct coding.
   * @return Fhir Coding.
   */
  Coding transformValueToCoding(String cdaCodeValue, ConceptMap map);
}