package tr.com.srdc.cda2fhir.util;

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

public final class Constants {

  // DAF Profile URLs (based on FHIR DSTU2)
  public static final String PROFILE_DAF_ALLERGY_INTOLERANCE = "http://hl7.org/fhir/StructureDefinition/daf-allergyintolerance";
  public static final String PROFILE_DAF_ALLERGY_LIST = "http://hl7.org/fhir/StructureDefinition/daf-allergylist";
  public static final String PROFILE_DAF_CONDITION = "http://hl7.org/fhir/StructureDefinition/daf-condition";
  public static final String PROFILE_DAF_DIAGNOSTIC_ORDER = "http://hl7.org/fhir/StructureDefinition/daf-diagnosticorder";
  public static final String PROFILE_DAF_DIAGNOSTIC_REPORT = "http://hl7.org/fhir/StructureDefinition/daf-diagnosticreport";
  public static final String PROFILE_DAF_ENCOUNTER = "http://hl7.org/fhir/StructureDefinition/daf-encounter";
  public static final String PROFILE_DAF_ENCOUNTER_LIST = "http://hl7.org/fhir/StructureDefinition/daf-encounterlist";
  public static final String PROFILE_DAF_FAMILY_MEMBER_HISTORY = "http://hl7.org/fhir/StructureDefinition/daf-familymemberhistory";
  public static final String PROFILE_DAF_IMMUNIZATION = "http://hl7.org/fhir/StructureDefinition/daf-immunization";
  public static final String PROFILE_DAF_IMMUNIZATION_LIST = "http://hl7.org/fhir/StructureDefinition/daf-immunizationlist";
  public static final String PROFILE_DAF_LOCATION = "http://hl7.org/fhir/StructureDefinition/daf-location";
  public static final String PROFILE_DAF_MEDICATION = "http://hl7.org/fhir/StructureDefinition/daf-medication";
  public static final String PROFILE_DAF_MEDICATION_ADMINISTRATION = "http://hl7.org/fhir/StructureDefinition/daf-medicationadministration";
  public static final String PROFILE_DAF_MEDICATION_DISPENSE = "http://hl7.org/fhir/StructureDefinition/daf-medicationdispense";
  public static final String PROFILE_DAF_MEDICATION_LIST = "http://hl7.org/fhir/StructureDefinition/daf-medicationlist";
  public static final String PROFILE_DAF_MEDICATION_ORDER = "http://hl7.org/fhir/StructureDefinition/daf-medicationorder";
  public static final String PROFILE_DAF_MEDICATION_STATEMENT = "http://hl7.org/fhir/StructureDefinition/daf-medicationstatement";
  public static final String PROFILE_DAF_ORGANIZATION = "http://hl7.org/fhir/StructureDefinition/daf-organization";
  public static final String PROFILE_DAF_PATIENT = "http://hl7.org/fhir/StructureDefinition/daf-patient";
  public static final String PROFILE_DAF_PRACTITIONER = "http://hl7.org/fhir/StructureDefinition/daf-pract";
  public static final String PROFILE_DAF_PROBLEM_LIST = "http://hl7.org/fhir/StructureDefinition/daf-problemlist";
  public static final String PROFILE_DAF_PROCEDURE = "http://hl7.org/fhir/StructureDefinition/daf-procedure";
  public static final String PROFILE_DAF_PROCEDURE_LIST = "http://hl7.org/fhir/StructureDefinition/daf-procedurelist";
  public static final String PROFILE_DAF_RELATED_PERSON = "http://hl7.org/fhir/StructureDefinition/daf-relatedperson";
  public static final String PROFILE_DAF_RESULT_LIST = "http://hl7.org/fhir/StructureDefinition/daf-resultlist";
  public static final String PROFILE_DAF_RESULT_OBS = "http://hl7.org/fhir/StructureDefinition/daf-resultobs";
  public static final String PROFILE_DAF_SMOKING_STATUS = "http://hl7.org/fhir/StructureDefinition/daf-smokingstatus";
  public static final String PROFILE_DAF_SPECIMEN = "http://hl7.org/fhir/StructureDefinition/daf-spec";
  public static final String PROFILE_DAF_SUBSTANCE = "http://hl7.org/fhir/StructureDefinition/daf-substance";
  public static final String PROFILE_DAF_VITAL_SIGNS = "http://hl7.org/fhir/StructureDefinition/daf-vitalsigns";

  // US CORE Profile URLs (based on FHIR DSTU3)
  public static final String PROFILE_USCORE_ALLERGY_INTOLERANCE = "http://hl7.org/fhir/StructureDefinition/us-core-allergyintolerance";
  public static final String PROFILE_USCORE_ALLERGY_LIST = "http://hl7.org/fhir/StructureDefinition/us-core-allergylist";
  public static final String PROFILE_USCORE_CONDITION = "http://hl7.org/fhir/StructureDefinition/us-core-condition";
  public static final String PROFILE_USCORE_DIAGNOSTIC_ORDER = "http://hl7.org/fhir/StructureDefinition/us-core-diagnosticorder";
  public static final String PROFILE_USCORE_DIAGNOSTIC_REPORT = "http://hl7.org/fhir/StructureDefinition/us-core-diagnosticreport";
  public static final String PROFILE_USCORE_ENCOUNTER = "http://hl7.org/fhir/StructureDefinition/us-core-encounter";
  public static final String PROFILE_USCORE_ENCOUNTER_LIST = "http://hl7.org/fhir/StructureDefinition/us-core-encounterlist";
  public static final String PROFILE_USCORE_FAMILY_MEMBER_HISTORY = "http://hl7.org/fhir/StructureDefinition/us-core-familymemberhistory";
  public static final String PROFILE_USCORE_IMMUNIZATION = "http://hl7.org/fhir/StructureDefinition/us-core-immunization";
  public static final String PROFILE_USCORE_IMMUNIZATION_LIST = "http://hl7.org/fhir/StructureDefinition/us-core-immunizationlist";
  public static final String PROFILE_USCORE_LOCATION = "http://hl7.org/fhir/StructureDefinition/us-core-location";
  public static final String PROFILE_USCORE_MEDICATION = "http://hl7.org/fhir/StructureDefinition/us-core-medication";
  public static final String PROFILE_USCORE_MEDICATION_ADMINISTRATION = "http://hl7.org/fhir/StructureDefinition/us-core-medicationadministration";
  public static final String PROFILE_USCORE_MEDICATION_DISPENSE = "http://hl7.org/fhir/StructureDefinition/us-core-medicationdispense";
  public static final String PROFILE_USCORE_MEDICATION_LIST = "http://hl7.org/fhir/StructureDefinition/us-core-medicationlist";
  public static final String PROFILE_USCORE_MEDICATION_ORDER = "http://hl7.org/fhir/StructureDefinition/us-core-medicationorder";
  public static final String PROFILE_USCORE_MEDICATION_STATEMENT = "http://hl7.org/fhir/StructureDefinition/us-core-medicationstatement";
  public static final String PROFILE_USCORE_ORGANIZATION = "http://hl7.org/fhir/StructureDefinition/us-core-organization";
  public static final String PROFILE_USCORE_PATIENT = "http://hl7.org/fhir/StructureDefinition/us-core-patient";
  public static final String PROFILE_USCORE_PRACTITIONER = "http://hl7.org/fhir/StructureDefinition/us-core-pract";
  public static final String PROFILE_USCORE_PROBLEM_LIST = "http://hl7.org/fhir/StructureDefinition/us-core-problemlist";
  public static final String PROFILE_USCORE_PROCEDURE = "http://hl7.org/fhir/StructureDefinition/us-core-procedure";
  public static final String PROFILE_USCORE_PROCEDURE_LIST = "http://hl7.org/fhir/StructureDefinition/us-core-procedurelist";
  public static final String PROFILE_USCORE_RELATED_PERSON = "http://hl7.org/fhir/StructureDefinition/us-core-relatedperson";
  public static final String PROFILE_USCORE_RESULT_LIST = "http://hl7.org/fhir/StructureDefinition/us-core-resultlist";
  public static final String PROFILE_USCORE_RESULT_OBS = "http://hl7.org/fhir/StructureDefinition/us-core-resultobs";
  public static final String PROFILE_USCORE_SMOKING_STATUS = "http://hl7.org/fhir/StructureDefinition/us-core-smokingstatus";
  public static final String PROFILE_USCORE_SPECIMEN = "http://hl7.org/fhir/StructureDefinition/us-core-spec";
  public static final String PROFILE_USCORE_SUBSTANCE = "http://hl7.org/fhir/StructureDefinition/us-core-substance";
  public static final String PROFILE_USCORE_VITAL_SIGNS = "http://hl7.org/fhir/StructureDefinition/us-core-vitalsigns";

  // Extension URLs
  public static final String URL_EXTENSION_BIRTHPLACE = "http://hl7.org/fhir/StructureDefinition/birthPlace";
  public static final String URL_EXTENSION_DATA_ABSENT_REASON = "http://hl7.org/fhir/StructureDefinition/data-absent-reason";
  public static final String URL_EXTENSION_ETHNICITY = "http://hl7.org/fhir/StructureDefinition/us-core-ethnicity";
  public static final String URL_EXTENSION_RACE = "http://hl7.org/fhir/StructureDefinition/us-core-race";
  public static final String URL_EXTENSION_RELIGION = "http://hl7.org/fhir/StructureDefinition/us-core-religion";

  // Code Systems STU3
  public static final String ENCOUNTER_CLASS_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-ActCode";
  public static final String MARITAL_STATUS_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus";
  public static final String PARTICIPANT_TYPE_SYSTEM = "http://hl7.org/fhir/v3/ParticipationType";
  public static final String CONDITION_CATEGORY_SYSTEM = "http://terminology.hl7.org/CodeSystem/condition-category";
  public static final String CONTACT_RELATIONSHIP_SYSTEM = "http://terminology.hl7.org/CodeSystem/v2-0131";
  public static final String IMMUNIZATION_PROVIDER_ROLE_SYSTEM = "http://hl7.org/fhir/v2/0443";
  public static final String CARETEAM_CATEGORY_SYSTEM = "http://hl7.org/fhir/care-team-category";

  // Resource Default System Identifiers
  public static final String CARETEAM_IDENTIFIER_SYSTEM = "CDA.CARETEAM.OID";
  public static final String PRACTITIONER_ROLE_IDENTIFIER_SYSYETM = "PRACTITIONER.ROLE.OID";
  public static final String DOCUMENTREF_IDENTIFIER_SYSTEM = "CDA.DOCUMENT.OID";
  public static final String PROVENANCE_IDENTIFIER_SYSTEM = "CDA.PROVENANCE.OID";

}
