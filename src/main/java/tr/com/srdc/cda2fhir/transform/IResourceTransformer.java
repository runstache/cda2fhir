package tr.com.srdc.cda2fhir.transform;

import org.hl7.fhir.dstu3.model.Age;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Composition.SectionComponent;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.FamilyMemberHistory;
import org.hl7.fhir.dstu3.model.Group;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient.ContactComponent;
import org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.dstu3.model.Substance;
import org.openhealthtools.mdht.uml.cda.AssignedAuthor;
import org.openhealthtools.mdht.uml.cda.AssignedEntity;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.Entity;
import org.openhealthtools.mdht.uml.cda.LanguageCommunication;
import org.openhealthtools.mdht.uml.cda.ManufacturedProduct;
import org.openhealthtools.mdht.uml.cda.ParticipantRole;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.Performer2;
import org.openhealthtools.mdht.uml.cda.Section;
import org.openhealthtools.mdht.uml.cda.consol.AllergyProblemAct;
import org.openhealthtools.mdht.uml.cda.consol.FamilyHistoryOrganizer;
import org.openhealthtools.mdht.uml.cda.consol.ImmunizationActivity;
import org.openhealthtools.mdht.uml.cda.consol.Indication;
import org.openhealthtools.mdht.uml.cda.consol.MedicationActivity;
import org.openhealthtools.mdht.uml.cda.consol.MedicationInformation;
import org.openhealthtools.mdht.uml.cda.consol.ProblemConcernAct;
import org.openhealthtools.mdht.uml.cda.consol.ProblemObservation;
import org.openhealthtools.mdht.uml.cda.consol.ReactionObservation;
import org.openhealthtools.mdht.uml.cda.consol.ResultObservation;
import org.openhealthtools.mdht.uml.cda.consol.ResultOrganizer;
import org.openhealthtools.mdht.uml.cda.consol.ServiceDeliveryLocation;
import org.openhealthtools.mdht.uml.cda.consol.VitalSignObservation;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;

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

public interface IResourceTransformer {

  /**
   * Transforms a CDA AgeObservation instance to a FHIR AgeDt composite datatype
   * instance.
   * 
   * @param cdaAgeObservation A CDA AgeObservation instance
   * @return A FHIR AgeDt composite datatype instance
   */
  Age transformAgeObservation2AgeDt(
        org.openhealthtools.mdht.uml.cda.consol.AgeObservation cdaAgeObservation);

  /**
   * Transforms a CDA AllergyProblemAct instance to a FHIR AllergyIntolerance
   * resource.
   * 
   * @param cdaAllergyProblemAct A CDA AllergyProblemAct instance
   * @return A FHIR Bundle that contains the AllergyIntolerance as the first
   *         entry, which can also include other referenced resources such as
   *         Practitioner
   */
  Bundle transformAllergyProblemAct2AllergyIntolerance(
        AllergyProblemAct cdaAllergyProblemAct);

  /**
   * Transforms a CDA AssignedAuthor instance to a FHIR Practitioner resource.
   * 
   * @param cdaAssignedAuthor A CDA AssignedAuthor instance
   * @return A FHIR Bundle that contains the Practitioner as the first entry,
   *         which can also include other referenced resources such as
   *         Organization
   */
  Bundle transformAssignedAuthor2Practitioner(
      AssignedAuthor cdaAssignedAuthor);

  /**
   * Transforms a CDA AssignedEntity instance to a FHIR Practitioner resource.
   * 
   * @param cdaAssignedEntity A CDA AssignedEntity instance
   * @return A FHIR Bundle that contains the Practitioner as the first entry,
   *         which can also include other referenced resources such as
   *         Organization
   */
  Bundle transformAssignedEntity2Practitioner(
        AssignedEntity cdaAssignedEntity);

  /**
   * Transforms a CDA Author instance to a FHIR Practitioner resource.
   * 
   * @param cdaAuthor A CDA Author instance
   * @return A FHIR Bundle that contains the Practitioner as the first entry,
   *         which can also include other referenced resources such as
   *         Organization
   */
  Bundle transformAuthor2Practitioner(
        org.openhealthtools.mdht.uml.cda.Author cdaAuthor);

  /**
   * Transforms a CDA CD instance to a FHIR Substance resource.
   * 
   * @param cdaSubstanceCode A CDA CD instance
   * @return A FHIR Substance resource
   */
  Substance transformCD2Substance(CD cdaSubstanceCode);

  /**
   * Transforms a CDA ClicinalDocument instance to a FHIR Composition resource.
   * 
   * @param cdaClinicalDocument A CDA ClicinalDocument instance
   * @return A FHIR Bundle that contains the Composition as the first entry, which
   *         can also include other referenced resources such as Patient,
   *         Practitioner, Organization
   */
  Bundle transformClinicalDocument2Composition(ClinicalDocument cdaClinicalDocument);

  /**
   * Transforms a CDA CustodianOrganization instance to a FHIR Organization
   * resource.
   * 
   * @param cdaCustodianOrganization A CDA CustodianOrganization instance
   * @return A FHIR Organization resource
   */
  Organization transformCustodianOrganization2Organization(
      org.openhealthtools.mdht.uml.cda.CustodianOrganization cdaCustodianOrganization);

  /**
   * Transforms a CDA Encounter instance to a FHIR Encounter resource.
   * 
   * @param cdaEncounter A CDA Encounter instance
   * @return A FHIR Bundle that contains the Encounter as the first entry, which
   *         can also include other referenced resources such as Practitioner,
   *         Location
   */
  Bundle transformEncounter2Encounter(org.openhealthtools.mdht.uml.cda.Encounter cdaEncounter);

  /**
   * Transforms a CDA EncounterActivity instance to a FHIR Encounter resource.
   * 
   * @param cdaEncounterActivity A CDA EncounterActivity instance
   * @return A FHIR Bundle that contains the Encounter as the first entry, which
   *         can also include other referenced resources such as Practitioner,
   *         Location
   */
  Bundle transformEncounterActivity2Encounter(
        org.openhealthtools.mdht.uml.cda.consol.EncounterActivities cdaEncounterActivity);

  /**
   * Transforms a CDA Entity instance to a FHIR Group resource.
   * 
   * @param cdaEntity A CDA Entity instance
   * @return A FHIR Group resource
   */
  Group transformEntity2Group(Entity cdaEntity);

  /**
   * Transforms a CDA FamilyHistoryOrganizer instance to a FHIR
   * FamilyMemberHistory resource.
   * 
   * @param cdaFamilyHistoryOrganizer A CDA FamilyHistoryOrganizer instance
   * @return A FHIR FamilyMemberHistory resource
   */
  FamilyMemberHistory transformFamilyHistoryOrganizer2FamilyMemberHistory(
        FamilyHistoryOrganizer cdaFamilyHistoryOrganizer);

  /**
   * Transforms a CDA Observation instance that is included in Functional Status
   * Section to a FHIR Observation resource.
   * 
   * @param cdaObservation A CDA Observation instance that is included in
   *                       Functional Status Section
   * @return A FHIR Bundle that contains the Observation as the first entry, which
   *         can also include other referenced resources such as Practitioner
   */
  Bundle transformFunctionalStatus2Observation(
        org.openhealthtools.mdht.uml.cda.Observation cdaObservation);

  /**
   * Transforms a CDA Guardian instance to a FHIR Patient.Contact resource.
   * 
   * @param cdaGuardian A CDA Guardian instance
   * @return A FHIR Patient.Contact resource
   */
  ContactComponent transformGuardian2Contact(org.openhealthtools.mdht.uml.cda.Guardian cdaGuardian);

  /**
   * Transforms a CDA ImmunizationActivity instance to a FHIR Immunization
   * resource.
   * 
   * @param cdaImmunizationActivity A CDA ImmunizationActivity instance
   * @return A FHIR Bundle that contains the Immunization as the first entry,
   *         which can also include other referenced resources such as
   *         Organization, Practitioner
   */
  Bundle transformImmunizationActivity2Immunization(ImmunizationActivity cdaImmunizationActivity);

  /**
   * Transforms a CDA Indication instance to a FHIR Condition resource.
   * 
   * @param cdaIndication A CDA Indication instance
   * @return A FHIR Condition resource
   */
  Condition transformIndication2Condition(Indication cdaIndication);

  /**
   * Transforms a CDA LanguageCommunication instance to a FHIR Communication
   * resource.
   * 
   * @param cdaLanguageCommunication A CDA LanguageCommunication instance
   * @return A FHIR Communication resource
   */

  PatientCommunicationComponent transformLanguageCommunication2Communication(
        LanguageCommunication cdaLanguageCommunication);

  /**
   * Transforms a CDA ManufacturedProduct instance to a FHIR Medication resource.
   * 
   * @param cdaManufacturedProduct A CDA ManufacturedProduct instance
   * @return A FHIR Bundle that contains the Medication as the first entry, which
   *         can also include other referenced resources such as Substance,
   *         Organization
   */
  Bundle transformManufacturedProduct2Medication(ManufacturedProduct cdaManufacturedProduct);

  /**
   * Transforms a CDA MedicationActivity instance to a FHIR MedicationStatement
   * resource.
   * 
   * @param cdaMedicationActivity A CDA MedicationActivity instance
   * @return A FHIR Bundle that contains the MedicationStatement as the first
   *         entry, which can also include other referenced resources such as
   *         Practitioner, Medication, Condition
   */
  Bundle transformMedicationActivity2MedicationStatement(
        MedicationActivity cdaMedicationActivity);

  /**
   * Transforms a CDA MedicationDispense instance to a FHIR MedicationDispense
   * resource.
   * 
   * @param cdaMedicationDispense A CDA MedicationDispense instance
   * @return A FHIR Bundle that contains the MedicationDispense as the first
   *         entry, which can also include other referenced resources such as
   *         Medication, Practitioner
   */
  Bundle transformMedicationDispense2MedicationDispense(
      org.openhealthtools.mdht.uml.cda.consol.MedicationDispense cdaMedicationDispense);

  /**
   * Transforms a CDA MedicationInformation instance to a FHIR Medication
   * resource.
   * 
   * @param cdaMedicationInformation A CDA MedicationInformation instance
   * @return A FHIR Bundle that contains the Medication as the first entry, which
   *         can also include other referenced resources such as Substance,
   *         Organization
   */
  Bundle transformMedicationInformation2Medication(MedicationInformation cdaMedicationInformation);

  /**
   * Transforms a CDA Observation instance to a FHIR Observation resource.
   * 
   * @param cdaObservation A CDA Observation instance
   * @return A FHIR Bundle that contains the Observation as the first entry, which
   *         can also include other referenced resources such as Encounter,
   *         Practitioner
   */
  Bundle transformObservation2Observation(
        org.openhealthtools.mdht.uml.cda.Observation cdaObservation);

  /**
   * Transforms a CDA Organization instance to a FHIR Organization resource.
   * 
   * @param cdaOrganization A CDA Organization instance
   * @return A FHIR Organization resource
   */
  Organization transformOrganization2Organization(
        org.openhealthtools.mdht.uml.cda.Organization cdaOrganization);

  /**
   * Transforms a CDA ParticipantRole instance to a FHIR Location resource.
   * 
   * @param cdaParticipantRole A CDA ParticipantRole instance
   * @return A FHIR Location Resource
   */
  Location transformParticipantRole2Location(ParticipantRole cdaParticipantRole);

  /**
   * Transforms a CDA PatientRole instance to a FHIR Patient resource.
   * 
   * @param cdaPatientRole A CDA PatientRole instance
   * @return A FHIR Bundle that contains the PatientRole as the first entry, which
   *         can also include other referenced resources such as Organization
   */
  Bundle transformPatientRole2Patient(PatientRole cdaPatientRole);

  /**
   * Transforms a CDA Performer2 instance to a FHIR Practitioner resource.
   * 
   * @param cdaPerformer2 A CDA Performer2 instance
   * @return A FHIR Bundle that contains the Practitioner as the first entry,
   *         which can also include other referenced resources such as
   *         Organization
   */
  Bundle transformPerformer22Practitioner(Performer2 cdaPerformer2);

  /**
   * Transforms a CDA ProblemConcernAct instance to FHIR Condition resource(s). A
   * ProblemConcernAct might include several Problem Observations, and each
   * Problem Observation corresponds to a FHIR Condition. Therefore, the returning
   * Bundle can contain several FHIR Conditions.
   * 
   * @param cdaProblemConcernAct A CDA ProblemConcernAct instance
   * @return A FHIR Bundle that contains the corresponding Condition(s), and
   *         further referenced resources such as Encounter, Practitioner
   */
  Bundle transformProblemConcernAct2Condition(ProblemConcernAct cdaProblemConcernAct);

  /**
   * Transforms a CDA ProblemObservation instance to FHIR Condition resource.
   * 
   * @param cdaProbObs A CDA ProblemObservation instance
   * @return A FHIR Bundle that contains the Condition as the first entry, which
   *         can also include other referenced resources such as Encounter,
   *         Practitioner
   */
  Bundle transformProblemObservation2Condition(ProblemObservation cdaProbObs);

  /**
   * Transforms a CDA Procedure instance to a FHIR Procedure resource.
   * 
   * @param cdaProcedure A CDA Procedure instance
   * @return A FHIR Bundle that contains the Procedure as the first entry, which
   *         can also include other referenced resources such as Practitioner
   */
  Bundle transformProcedure2Procedure(org.openhealthtools.mdht.uml.cda.Procedure cdaProcedure);

  /**
   * Transforms a CDA ReactionObservation instance to a FHIR Observation resource.
   * 
   * @param cdaReactionObservation A CDA ReactionObservation instance
   * @return A FHIR Bundle that contains the Observation as the first entry, which
   *         can also include other referenced resources such as Encounter,
   *         Practitioner
   */
  Bundle transformReactionObservation2Observation(ReactionObservation cdaReactionObservation);

  /**
   * Transforms a CDA ReferenceRange instance to a FHIR Observation.ReferenceRange
   * resource.
   * 
   * @param cdaReferenceRange A CDA ReferenceRange instance
   * @return A FHIR Observation.ReferenceRange resource
   */
  ObservationReferenceRangeComponent transformReferenceRange2ReferenceRange(
      org.openhealthtools.mdht.uml.cda.ReferenceRange cdaReferenceRange);

  /**
   * Transforms a CDA ResultObservation instance to a FHIR Observation resource.
   * 
   * @param cdaResultObservation A CDA ResultObservation instance
   * @return A FHIR Bundle that contains the Observation as the first entry, which
   *         can also include other referenced resources such as Encounter,
   *         Practitioner
   */
  Bundle transformResultObservation2Observation(ResultObservation cdaResultObservation);

  /**
   * Transforms a CDA ResultOrganizer instance to a FHIR DiagnosticReport
   * resource.
   * 
   * @param cdaResultOrganizer A CDA ResultOrganizer instance
   * @return A FHIR Bundle that contains the DiagnosticReport as the first entry,
   *         which can also include other referenced resources such as
   *         Practitioner, Observation
   */
  Bundle transformResultOrganizer2DiagnosticReport(ResultOrganizer cdaResultOrganizer);

  /**
   * Transforms a CDA Section instance to a FHIR Composition.Section resource.
   * 
   * @param cdaSection A CDA Section instance
   * @return A FHIR Composition.Section resource
   */
  SectionComponent transformSection2Section(Section cdaSection);

  /**
   * Transforms a CDA ServiceDeliveryLocation instance to a FHIR Location
   * resource.
   * 
   * @param cdaSdloc A CDA ServiceDeliveryLocation instance
   * @return A FHIR Location resource
   */
  Location transformServiceDeliveryLocation2Location(
        ServiceDeliveryLocation cdaSdloc);

  /**
   * Transforms a CDA Supply instance to a FHIR Device resource.
   * 
   * @param cdaSupply A CDA Supply instance
   * @return A FHIR Device resource
   */
  Device transformSupply2Device(org.openhealthtools.mdht.uml.cda.Supply cdaSupply);

  /**
   * Transforms a CDA VitalSignObservation to a FHIR Observation resource.
   * 
   * @param cdaVitalSignObservation A CDA VitalSignObservation instance
   * @return A FHIR Bundle that contains the Observation as the first entry, which
   *         can also include other referenced resources such as Encounter,
   *         Practitioner
   */
  Bundle transformVitalSignObservation2Observation(VitalSignObservation cdaVitalSignObservation);
}
