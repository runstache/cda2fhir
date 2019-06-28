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
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Age;
import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCriticality;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode;
import org.hl7.fhir.dstu3.model.Composition.CompositionAttesterComponent;
import org.hl7.fhir.dstu3.model.Composition.DocumentConfidentiality;
import org.hl7.fhir.dstu3.model.Composition.SectionComponent;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportPerformerComponent;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Encounter.EncounterParticipantComponent;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.FamilyMemberHistory;
import org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent;
import org.hl7.fhir.dstu3.model.Group;
import org.hl7.fhir.dstu3.model.Group.GroupType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationExplanationComponent;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationPractitionerComponent;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationReactionComponent;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Medication.MedicationIngredientComponent;
import org.hl7.fhir.dstu3.model.MedicationDispense;
import org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispensePerformerComponent;
import org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus;
import org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementTaken;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Patient.ContactComponent;
import org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.PractitionerRole;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.dstu3.model.Procedure.ProcedurePerformerComponent;
import org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Substance;
import org.hl7.fhir.dstu3.model.Timing;

import org.openhealthtools.mdht.uml.cda.AssignedAuthor;
import org.openhealthtools.mdht.uml.cda.AssignedEntity;
import org.openhealthtools.mdht.uml.cda.Author;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.Entity;
import org.openhealthtools.mdht.uml.cda.EntryRelationship;
import org.openhealthtools.mdht.uml.cda.Guardian;
import org.openhealthtools.mdht.uml.cda.LanguageCommunication;
import org.openhealthtools.mdht.uml.cda.ManufacturedProduct;
import org.openhealthtools.mdht.uml.cda.Material;
import org.openhealthtools.mdht.uml.cda.Participant2;
import org.openhealthtools.mdht.uml.cda.ParticipantRole;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.Performer2;
import org.openhealthtools.mdht.uml.cda.ReferenceRange;
import org.openhealthtools.mdht.uml.cda.RelatedSubject;
import org.openhealthtools.mdht.uml.cda.Section;
import org.openhealthtools.mdht.uml.cda.SubjectPerson;
import org.openhealthtools.mdht.uml.cda.Supply;
import org.openhealthtools.mdht.uml.cda.consol.AllergyObservation;
import org.openhealthtools.mdht.uml.cda.consol.AllergyProblemAct;
import org.openhealthtools.mdht.uml.cda.consol.EncounterActivities;
import org.openhealthtools.mdht.uml.cda.consol.FamilyHistoryObservation;
import org.openhealthtools.mdht.uml.cda.consol.FamilyHistoryOrganizer;
import org.openhealthtools.mdht.uml.cda.consol.ImmunizationActivity;
import org.openhealthtools.mdht.uml.cda.consol.Indication;
import org.openhealthtools.mdht.uml.cda.consol.MedicationActivity;
import org.openhealthtools.mdht.uml.cda.consol.MedicationInformation;
import org.openhealthtools.mdht.uml.cda.consol.NonMedicinalSupplyActivity;
import org.openhealthtools.mdht.uml.cda.consol.ProblemConcernAct;
import org.openhealthtools.mdht.uml.cda.consol.ProblemObservation;
import org.openhealthtools.mdht.uml.cda.consol.ProductInstance;
import org.openhealthtools.mdht.uml.cda.consol.ReactionObservation;
import org.openhealthtools.mdht.uml.cda.consol.ResultObservation;
import org.openhealthtools.mdht.uml.cda.consol.ResultOrganizer;
import org.openhealthtools.mdht.uml.cda.consol.ServiceDeliveryLocation;
import org.openhealthtools.mdht.uml.cda.consol.SeverityObservation;
import org.openhealthtools.mdht.uml.cda.consol.VitalSignObservation;
import org.openhealthtools.mdht.uml.hl7.datatypes.AD;
import org.openhealthtools.mdht.uml.hl7.datatypes.ANY;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.ED;
import org.openhealthtools.mdht.uml.hl7.datatypes.EN;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVL_PQ;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVL_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVXB_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.ON;
import org.openhealthtools.mdht.uml.hl7.datatypes.PIVL_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;
import org.openhealthtools.mdht.uml.hl7.datatypes.PQ;
import org.openhealthtools.mdht.uml.hl7.datatypes.RTO;
import org.openhealthtools.mdht.uml.hl7.datatypes.ST;
import org.openhealthtools.mdht.uml.hl7.datatypes.SXCM_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.TEL;
import org.openhealthtools.mdht.uml.hl7.datatypes.TS;
//import org.openhealthtools.mdht.uml.cda.consol.*;
//import org.openhealthtools.mdht.uml.hl7.datatypes.*;
import org.openhealthtools.mdht.uml.hl7.vocab.EntityDeterminer;
import org.openhealthtools.mdht.uml.hl7.vocab.ParticipationType;
import org.openhealthtools.mdht.uml.hl7.vocab.RoleClassRoot;

/*
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance.Reaction;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.Patient.Communication;
import ca.uhn.fhir.model.dstu2.resource.Procedure.Performer;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.IdDt;
*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.com.srdc.cda2fhir.conf.Config;
import tr.com.srdc.cda2fhir.util.Constants;

public class ResourceTransformerImpl implements IResourceTransformer, Serializable {

  public static final long serialVersionUID = 3L;

  private IDataTypesTransformer dtt;
  private IValueSetsTransformer vst;
  private ICDATransformer cdat;
  private Reference defaultPatientRef;

  private final Logger logger = LoggerFactory.getLogger(ResourceTransformerImpl.class);

  /**
   * Resource Transformer Implementation Constructor.
   */
  public ResourceTransformerImpl() {
    dtt = new DataTypesTransformerImpl();
    vst = new ValueSetsTransformerImpl();
    cdat = null;
    // This is a default patient reference to be used when IResourceTransformer is
    // not initiated with a ICDATransformer
    
    defaultPatientRef = new Reference(new IdType("Patient", 0L));
  }

  public ResourceTransformerImpl(ICDATransformer cdaTransformer) {
    this();
    cdat = cdaTransformer;
  }

  protected String getUniqueId() {
    if (cdat != null) {
      return cdat.getUniqueId();
    } else {
      return UUID.randomUUID().toString();
    }      
  }

  protected Reference getPatientRef() {
    if (cdat != null) {
      return cdat.getPatientRef();
    } else {
      return defaultPatientRef;
    }      
  }

  /**
   * Transforms an Age Observation to a Fhir Age Type.
   */
  public Age transformAgeObservation2AgeDt(
        org.openhealthtools.mdht.uml.cda.consol.AgeObservation cdaAgeObservation) {
    if (cdaAgeObservation == null || cdaAgeObservation.isSetNullFlavor()) {
      return null;
    }
    Age fhirAge = new Age();

    // value-> age
    if (cdaAgeObservation != null && !cdaAgeObservation.getValues().isEmpty()) {
      for (ANY value : cdaAgeObservation.getValues()) {
        if (value != null && !value.isSetNullFlavor()) {
          if (value instanceof PQ) {
            if (((PQ) value).getValue() != null) {
              // value.value -> value
              fhirAge.setValue(((PQ) value).getValue());
              // value.unit -> unit
              fhirAge.setUnit(((PQ) value).getUnit());
              fhirAge.setSystem("http://unitsofmeasure.org");
            }
          }
        }
      }
    }
    return fhirAge;
  }

  /**
   * Transforms an Allergy Problem act to Allergy Intolerance FHIR Resources in a Fhir Bundle.
   */
  public Bundle transformAllergyProblemAct2AllergyIntolerance(AllergyProblemAct cdaAllergyProbAct) {
    if (cdaAllergyProbAct == null || cdaAllergyProbAct.isSetNullFlavor()) {
      return null;
    }
      
    AllergyIntolerance fhirAllergyIntolerance = new AllergyIntolerance();
        
    Bundle allergyIntoleranceBundle = new Bundle();
    allergyIntoleranceBundle.addEntry(
          new BundleEntryComponent().setResource(fhirAllergyIntolerance));

    // resource id
    IdType resourceId = new IdType("AllergyIntolerance", getUniqueId());
    fhirAllergyIntolerance.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirAllergyIntolerance.getMeta().addProfile(Constants.PROFILE_DAF_ALLERGY_INTOLERANCE);
    }
      
    // id -> identifier
    for (II ii : cdaAllergyProbAct.getIds()) {
      if (!ii.isSetNullFlavor()) {
        fhirAllergyIntolerance.addIdentifier(dtt.transformII2Identifier(ii));
      }
    }

    // patient
    fhirAllergyIntolerance.setPatient(getPatientRef());

    // author -> recorder
    if (cdaAllergyProbAct.getAuthors() != null && !cdaAllergyProbAct.getAuthors().isEmpty()) {
      for (org.openhealthtools.mdht.uml.cda.Author author : cdaAllergyProbAct.getAuthors()) {
        // Asserting that at most one author exists
        if (author != null && !author.isSetNullFlavor()) {
          Practitioner fhirPractitioner = null;
          Bundle fhirPractitionerBundle = transformAuthor2Practitioner(author);

          for (BundleEntryComponent entry : fhirPractitionerBundle.getEntry()) {
            allergyIntoleranceBundle.addEntry(
                  new BundleEntryComponent().setResource(entry.getResource()));
            if (entry.getResource() instanceof Practitioner) {
              fhirPractitioner = (Practitioner) entry.getResource();
            }
          }
          fhirAllergyIntolerance.setRecorder(new Reference(fhirPractitioner.getId()));
        }
      }
    }

    // statusCode -> status
    if (cdaAllergyProbAct.getStatusCode() != null 
        && !cdaAllergyProbAct.getStatusCode().isSetNullFlavor()) {
      if (cdaAllergyProbAct.getStatusCode().getCode() != null
          && !cdaAllergyProbAct.getStatusCode().getCode().isEmpty()) {
        //TODO: Update Status to Clinical/Verification Status
      }
    }

    // effectiveTime -> onset
    if (cdaAllergyProbAct.getEffectiveTime() != null 
        && !cdaAllergyProbAct.getEffectiveTime().isSetNullFlavor()) {

      // low(if not exists, value) -> onset
      if (cdaAllergyProbAct.getEffectiveTime().getLow() != null
          && !cdaAllergyProbAct.getEffectiveTime().getLow().isSetNullFlavor()) {
        fhirAllergyIntolerance.setOnset(
              dtt.transformTS2DateTime(cdaAllergyProbAct.getEffectiveTime().getLow()));
      } else if (cdaAllergyProbAct.getEffectiveTime().getValue() != null
          && !cdaAllergyProbAct.getEffectiveTime().getValue().isEmpty()) {
        fhirAllergyIntolerance.setOnset(
              dtt.transformString2DateTime(cdaAllergyProbAct.getEffectiveTime().getValue()));
        
      }
    }

    // getting allergyObservation
    if (cdaAllergyProbAct.getAllergyObservations() != null 
        && !cdaAllergyProbAct.getAllergyObservations().isEmpty()) {
      for (AllergyObservation cdaAllergyObs : cdaAllergyProbAct.getAllergyObservations()) {
        if (cdaAllergyObs != null && !cdaAllergyObs.isSetNullFlavor()) {

          // allergyObservation.participant.participantRole.playingEntity.code ->
          // substance
          if (cdaAllergyObs.getParticipants() != null 
              && !cdaAllergyObs.getParticipants().isEmpty()) {
            for (Participant2 participant : cdaAllergyObs.getParticipants()) {
              if (participant != null && !participant.isSetNullFlavor()) {
                if (participant.getParticipantRole() != null 
                    && !participant.getParticipantRole().isSetNullFlavor()) {
                  if (participant.getParticipantRole().getPlayingEntity() != null
                      && !participant.getParticipantRole().getPlayingEntity().isSetNullFlavor()) {
                    if (participant.getParticipantRole().getPlayingEntity().getCode() != null
                        && !participant.getParticipantRole()
                              .getPlayingEntity()
                              .getCode().isSetNullFlavor()) {
                      
                      fhirAllergyIntolerance.setCode(
                          dtt.transformCD2CodeableConcept(participant.getParticipantRole()
                          .getPlayingEntity().getCode()));
                    }
                  }
                }
              }
            }
          }

          // allergyObservation.value[@xsi:type='CD'] -> category
          if (cdaAllergyObs.getValues() != null && !cdaAllergyObs.getValues().isEmpty()) {
            for (ANY value : cdaAllergyObs.getValues()) {
              if (value != null && !value.isSetNullFlavor()) {
                if (value instanceof CD) {
                  if (vst.transformAllergyCategoryCode2AllergyIntoleranceCategory(
                        ((CD) value).getCode()) != null) {
                          
                    fhirAllergyIntolerance.addCategory(
                          vst.transformAllergyCategoryCode2AllergyIntoleranceCategory(
                                ((CD) value).getCode()));
                  }
                }
              }
            }
          }

          // searching for reaction observation
          if (cdaAllergyObs.getEntryRelationships() != null 
              && !cdaAllergyObs.getEntryRelationships().isEmpty()) {
            for (EntryRelationship entryRelShip : cdaAllergyObs.getEntryRelationships()) {
              if (entryRelShip != null && !entryRelShip.isSetNullFlavor()) {
                if (entryRelShip.getObservation() != null && !entryRelShip.isSetNullFlavor()) {

                  // reaction observation
                  if (entryRelShip.getObservation() instanceof ReactionObservation) {

                    ReactionObservation cdaReactionObs = 
                        (ReactionObservation) entryRelShip.getObservation();
                    AllergyIntoleranceReactionComponent fhirReaction = 
                        fhirAllergyIntolerance.addReaction();

                    // reactionObservation/value[@xsi:type='CD'] -> reaction.manifestation
                    if (cdaReactionObs.getValues() != null 
                        && !cdaReactionObs.getValues().isEmpty()) {
                      for (ANY value : cdaReactionObs.getValues()) {
                        if (value != null && !value.isSetNullFlavor()) {
                          if (value instanceof CD) {
                            fhirReaction.addManifestation(
                                  dtt.transformCD2CodeableConcept((CD) value));
                          }
                        }
                      }
                    }

                    // reactionObservation/low -> reaction.onset
                    if (cdaReactionObs.getEffectiveTime() != null
                        && !cdaReactionObs.getEffectiveTime().isSetNullFlavor()) {
                      if (cdaReactionObs.getEffectiveTime().getLow() != null
                          && !cdaReactionObs.getEffectiveTime().getLow().isSetNullFlavor()) {
                        fhirReaction.setOnsetElement(
                            dtt.transformString2DateTime(
                              cdaReactionObs.getEffectiveTime().getLow().getValue()));
                      }
                    }

                    // severityObservation/value[@xsi:type='CD'].code -> severity
                    if (cdaReactionObs.getSeverityObservation() != null
                        && !cdaReactionObs.getSeverityObservation().isSetNullFlavor()) {
                      SeverityObservation cdaSeverityObs = 
                          cdaReactionObs.getSeverityObservation();
                      if (cdaSeverityObs.getValues() != null 
                          && !cdaSeverityObs.getValues().isEmpty()) {
                        for (ANY value : cdaSeverityObs.getValues()) {
                          if (value != null && !value.isSetNullFlavor()) {
                            if (value instanceof CD) {
                              if (vst.transformSeverityCode2AllergyIntoleranceSeverity(
                                    ((CD) value).getCode()) != null) {
                                fhirReaction.setSeverity(
                                    vst.transformSeverityCode2AllergyIntoleranceSeverity(
                                          ((CD) value).getCode()));
                              }
                            }
                          }
                        }
                      }
                    }
                  }

                  // criticality observation. found by checking the templateId
                  // entryRelationship.observation[templateId/@root=
                  // '2.16.840.1.113883.10.20.22.4.145'].value[CD].code
                  // -> criticality
                  if (entryRelShip.getObservation().getTemplateIds() != null
                      && !entryRelShip.getObservation().getTemplateIds().isEmpty()) {
                    for (II templateId : entryRelShip.getObservation().getTemplateIds()) {
                      if (templateId.getRoot() != null
                          && templateId.getRoot().equals("2.16.840.1.113883.10.20.22.4.145")) {
                        org.openhealthtools.mdht.uml.cda.Observation cdaCriticalityObservation = 
                            entryRelShip.getObservation();
                        for (ANY value : cdaCriticalityObservation.getValues()) {
                          if (value != null && !value.isSetNullFlavor()) {
                            if (value instanceof CD) {
                              AllergyIntoleranceCriticality allergyIntoleranceCriticality = 
                                  vst
                                    .transformCriticalityObservationValue2AllergyIntoleranceCriticality(
                                      ((CD) value).getCode());
                              if (allergyIntoleranceCriticality != null) {
                                fhirAllergyIntolerance
                                    .setCriticality(allergyIntoleranceCriticality);
                              }
                            }
                          }
                        }
                        // since we already found the desired templateId, 
                        // we may break the searching for
                        // templateId to avoid containing duplicate observations
                        break;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return allergyIntoleranceBundle;
  }

  /**
   * Transforms an Assigner Author to FHIR Practitioner.
   */
  public Bundle transformAssignedAuthor2Practitioner(AssignedAuthor cdaAssignedAuthor) {
    if (cdaAssignedAuthor == null || cdaAssignedAuthor.isSetNullFlavor()) {
      return null;
    }
      

    Practitioner fhirPractitioner = new Practitioner();

    // bundle
    Bundle fhirPractitionerBundle = new Bundle();
    fhirPractitionerBundle.addEntry(new BundleEntryComponent().setResource(fhirPractitioner));

    // resource id
    IdType resourceId = new IdType("Practitioner", getUniqueId());
    fhirPractitioner.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirPractitioner.getMeta().addProfile(Constants.PROFILE_DAF_PRACTITIONER);
    }

    // id -> identifier
    if (cdaAssignedAuthor.getIds() != null && !cdaAssignedAuthor.getIds().isEmpty()) {
      for (II ii : cdaAssignedAuthor.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirPractitioner.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // assignedPerson.name -> name
    if (cdaAssignedAuthor.getAssignedPerson() != null 
        && !cdaAssignedAuthor.getAssignedPerson().isSetNullFlavor()) {
      if (cdaAssignedAuthor.getAssignedPerson().getNames() != null
          && !cdaAssignedAuthor.getAssignedPerson().getNames().isEmpty()) {
        for (PN pn : cdaAssignedAuthor.getAssignedPerson().getNames()) {
          if (pn != null && !pn.isSetNullFlavor()) {
            // Asserting that at most one name exists
            fhirPractitioner.addName(dtt.transformEN2HumanName(pn));
          }
        }
      }
    }

    // addr -> address
    if (cdaAssignedAuthor.getAddrs() != null && !cdaAssignedAuthor.getAddrs().isEmpty()) {
      for (AD ad : cdaAssignedAuthor.getAddrs()) {
        if (ad != null && !ad.isSetNullFlavor()) {
          fhirPractitioner.addAddress(dtt.transformAD2Address(ad));
        }
      }
    }

    // telecom -> telecom
    if (cdaAssignedAuthor.getTelecoms() != null && !cdaAssignedAuthor.getTelecoms().isEmpty()) {
      for (TEL tel : cdaAssignedAuthor.getTelecoms()) {
        if (tel != null && !tel.isSetNullFlavor()) {
          fhirPractitioner.addTelecom(dtt.transformTel2ContactPoint(tel));
        }
      }
    }

    // Adding a practitionerRole
    PractitionerRole fhirPractitionerRole = new PractitionerRole();

    // code -> practitionerRole.role
    if (cdaAssignedAuthor.getCode() != null && !cdaAssignedAuthor.isSetNullFlavor()) {
      fhirPractitionerRole.addCode(dtt.transformCD2CodeableConcept(cdaAssignedAuthor.getCode()));
    }

    // representedOrganization -> practitionerRole.managingOrganization
    if (cdaAssignedAuthor.getRepresentedOrganization() != null
        && !cdaAssignedAuthor.getRepresentedOrganization().isSetNullFlavor()) {
      Organization fhirOrganization = 
          transformOrganization2Organization(cdaAssignedAuthor.getRepresentedOrganization());
      fhirPractitionerRole.setOrganization(new Reference(fhirOrganization.getId()));
      fhirPractitionerBundle.addEntry(new BundleEntryComponent().setResource(fhirOrganization));
    }
    //Add the Linkage to the Fhir Practitioner
    fhirPractitionerRole.setPractitioner(new Reference(fhirPractitioner.getId()));
    //Add the Practitioner Role to the Bundle.
    fhirPractitionerBundle.addEntry(new BundleEntryComponent().setResource(fhirPractitionerRole));

    return fhirPractitionerBundle;
  }

  /**
   * Transforms an Assigned Entity into a Practitioner.
   */
  public Bundle transformAssignedEntity2Practitioner(AssignedEntity cdaAssignedEntity) {
    if (cdaAssignedEntity == null 
        || cdaAssignedEntity.isSetNullFlavor()) {
      return null;
    }

    Practitioner fhirPractitioner = new Practitioner();

    // bundle
    Bundle fhirPractitionerBundle = new Bundle();
    fhirPractitionerBundle.addEntry(new BundleEntryComponent().setResource(fhirPractitioner));

    // resource id
    IdType resourceId = new IdType("Practitioner", getUniqueId());
    fhirPractitioner.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirPractitioner.getMeta().addProfile(Constants.PROFILE_DAF_PRACTITIONER);
    }

    // id -> identifier
    if (cdaAssignedEntity.getIds() != null && !cdaAssignedEntity.getIds().isEmpty()) {
      for (II id : cdaAssignedEntity.getIds()) {
        if (id != null && !id.isSetNullFlavor()) {
          fhirPractitioner.addIdentifier(dtt.transformII2Identifier(id));
        }
      }
    }

    // assignedPerson.name -> name
    if (cdaAssignedEntity.getAssignedPerson() != null 
        && !cdaAssignedEntity.getAssignedPerson().isSetNullFlavor()) {
      for (PN pn : cdaAssignedEntity.getAssignedPerson().getNames()) {
        if (pn != null && !pn.isSetNullFlavor()) {
          // asserting that at most one name exists
          fhirPractitioner.addName(dtt.transformEN2HumanName(pn));
        }
      }
    }

    // addr -> address
    if (cdaAssignedEntity.getAddrs() != null && !cdaAssignedEntity.getAddrs().isEmpty()) {
      for (AD ad : cdaAssignedEntity.getAddrs()) {
        if (ad != null && !ad.isSetNullFlavor()) {
          fhirPractitioner.addAddress(dtt.transformAD2Address(ad));
        }
      }
    }

    // telecom -> telecom
    if (cdaAssignedEntity.getTelecoms() != null && !cdaAssignedEntity.getTelecoms().isEmpty()) {
      for (TEL tel : cdaAssignedEntity.getTelecoms()) {
        if (tel != null && !tel.isSetNullFlavor()) {
          fhirPractitioner.addTelecom(dtt.transformTel2ContactPoint(tel));
        }
      }
    }

    PractitionerRole fhirPractitionerRole = new PractitionerRole();

    // code -> practitionerRole.role
    if (cdaAssignedEntity.getCode() != null && !cdaAssignedEntity.isSetNullFlavor()) {
      fhirPractitionerRole.addCode(dtt.transformCD2CodeableConcept(cdaAssignedEntity.getCode()));
    }

    // representedOrganization -> practitionerRole.organization
    // NOTE: we skipped multiple instances of representated organization; we just
    // omit apart from the first
    if (!cdaAssignedEntity.getRepresentedOrganizations().isEmpty()) {
      if (cdaAssignedEntity.getRepresentedOrganizations().get(0) != null
          && !cdaAssignedEntity.getRepresentedOrganizations().get(0).isSetNullFlavor()) {
        Organization fhirOrganization = transformOrganization2Organization(
            cdaAssignedEntity.getRepresentedOrganizations().get(0));
        fhirPractitionerRole.setOrganization(new Reference(fhirOrganization.getId()));
        fhirPractitionerBundle.addEntry(new BundleEntryComponent().setResource(fhirOrganization));
        
      }
    }
    fhirPractitionerRole.setPractitioner(new Reference(fhirPractitioner.getId()));
    fhirPractitionerBundle.addEntry(new BundleEntryComponent().setResource(fhirPractitionerRole));

    return fhirPractitionerBundle;
  }

  /**
   * Transforms a CDA Author to a Practitioner.
   */
  public Bundle transformAuthor2Practitioner(org.openhealthtools.mdht.uml.cda.Author cdaAuthor) {
    if (cdaAuthor == null || cdaAuthor.isSetNullFlavor()) {
      return null;
    } else if (cdaAuthor.getAssignedAuthor() == null 
        || cdaAuthor.getAssignedAuthor().isSetNullFlavor()) {
      return null;
    } else {
      return transformAssignedAuthor2Practitioner(cdaAuthor.getAssignedAuthor());
    }
  }

  /**
   * Transforms a CDA CD Entity to a FHIR Substance.
   */
  public Substance transformCD2Substance(CD cdaSubstanceCode) {
    if (cdaSubstanceCode == null || cdaSubstanceCode.isSetNullFlavor()) {
      return null;
    }    

    Substance fhirSubstance = new Substance();

    // resource id
    fhirSubstance.setId(new IdType("Substance", getUniqueId()));

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirSubstance.getMeta().addProfile(Constants.PROFILE_DAF_SUBSTANCE);
    }

    // code -> code
    fhirSubstance.setCode(dtt.transformCD2CodeableConcept(cdaSubstanceCode));

    return fhirSubstance;
  }

  /**
   * Transforms a Clinical Document into a Fhir Bundle. 
   * @param cdaClinicalDocument Clinical Document
   * @return Fhir Bundle.
   */
  public Bundle transformClinicalDocument2Composition(ClinicalDocument cdaClinicalDocument) {
    if (cdaClinicalDocument == null 
        || cdaClinicalDocument.isSetNullFlavor()) {
      return null;
    }    

    // create and init the global bundle and the composition resources
    Bundle fhirCompBundle = new Bundle();
    Composition fhirComp = new Composition();
    fhirComp.setId(new IdType("Composition", getUniqueId()));
    fhirCompBundle.addEntry(new BundleEntryComponent().setResource(fhirComp));

    // id -> identifier
    if (cdaClinicalDocument.getId() != null && !cdaClinicalDocument.getId().isSetNullFlavor()) {
      fhirComp.setIdentifier(dtt.transformII2Identifier(cdaClinicalDocument.getId()));
    }

    // status
    fhirComp.setStatus(Config.DEFAULT_COMPOSITION_STATUS);

    // effectiveTime -> date
    if (cdaClinicalDocument.getEffectiveTime() != null 
        && !cdaClinicalDocument.getEffectiveTime().isSetNullFlavor()) {
      fhirComp.setDateElement(dtt.transformTS2DateTime(cdaClinicalDocument.getEffectiveTime()));
    }

    // code -> type
    if (cdaClinicalDocument.getCode() != null 
        && !cdaClinicalDocument.getCode().isSetNullFlavor()) {
      fhirComp.setType(dtt.transformCD2CodeableConcept(cdaClinicalDocument.getCode()));
    }

    // title.text -> title
    if (cdaClinicalDocument.getTitle() != null 
        && !cdaClinicalDocument.getTitle().isSetNullFlavor()) {
      if (cdaClinicalDocument.getTitle().getText() != null 
          && !cdaClinicalDocument.getTitle().getText().isEmpty()) {
        fhirComp.setTitle(cdaClinicalDocument.getTitle().getText());
      }
    }

    // confidentialityCode -> confidentiality
    if (cdaClinicalDocument.getConfidentialityCode() != null
        && !cdaClinicalDocument.getConfidentialityCode().isSetNullFlavor()) {
      if (cdaClinicalDocument.getConfidentialityCode().getCode() != null
          && !cdaClinicalDocument.getConfidentialityCode().getCode().isEmpty()) {
        //TODO: Update this to utilize Concept Map possibly.
        fhirComp.setConfidentiality(
            DocumentConfidentiality.fromCode(
              cdaClinicalDocument.getConfidentialityCode().getCode()));
      }
    }

    // transform the patient data and assign it to Composition.subject.
    // patient might refer to additional resources such as organization; hence the
    // method returns a bundle.
    Bundle subjectBundle = 
        transformPatientRole2Patient(
            cdaClinicalDocument.getRecordTargets().get(0).getPatientRole());
    for (BundleEntryComponent entry : subjectBundle.getEntry()) {
      fhirCompBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));
      if (entry.getResource() instanceof Patient) {
        fhirComp.setSubject(new Reference(entry.getResource().getId()));
      }
    }

    // author.assignedAuthor -> author
    if (cdaClinicalDocument.getAuthors() != null && !cdaClinicalDocument.getAuthors().isEmpty()) {
      for (Author author : cdaClinicalDocument.getAuthors()) {
        // Asserting that at most one author exists
        if (author != null && !author.isSetNullFlavor()) {
          if (author.getAssignedAuthor() != null && !author.getAssignedAuthor().isSetNullFlavor()) {
            Bundle practBundle = transformAuthor2Practitioner(author);
            for (BundleEntryComponent entry : practBundle.getEntry()) {
              // Add all the resources returned from the bundle to the main bundle
              fhirCompBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));
              if (entry.getResource() instanceof Practitioner) {
                fhirComp.addAuthor().setReference((entry.getResource()).getId());
              }
            }
          }
        }
      }
    }

    // legalAuthenticator -> attester[mode = legal]
    if (cdaClinicalDocument.getLegalAuthenticator() != null
        && !cdaClinicalDocument.getLegalAuthenticator().isSetNullFlavor()) {
      CompositionAttesterComponent attester = fhirComp.addAttester();
      attester.addMode(CompositionAttestationMode.LEGAL);
      attester.setTimeElement(
            dtt.transformTS2DateTime(cdaClinicalDocument.getLegalAuthenticator().getTime()));
      Bundle practBundle = transformAssignedEntity2Practitioner(
          cdaClinicalDocument.getLegalAuthenticator().getAssignedEntity());
      for (BundleEntryComponent entry : practBundle.getEntry()) {
        // Add all the resources returned from the bundle to the main bundle
        fhirCompBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));
        if (entry.getResource() instanceof Practitioner) {
          attester.setParty(new Reference((entry.getResource()).getId()));
        }
      }
    }

    // authenticator -> attester[mode = professional]
    for (org.openhealthtools.mdht.uml.cda.Authenticator authenticator : 
        cdaClinicalDocument.getAuthenticators()) {
      if (!authenticator.isSetNullFlavor()) {
        CompositionAttesterComponent attester = fhirComp.addAttester();
        attester.addMode(CompositionAttestationMode.PROFESSIONAL);
        attester.setTimeElement(dtt.transformTS2DateTime(authenticator.getTime()));
        Bundle practBundle = 
            transformAssignedEntity2Practitioner(authenticator.getAssignedEntity());
        for (BundleEntryComponent entry : practBundle.getEntry()) {
          // Add all the resources returned from the bundle to the main bundle
          fhirCompBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));
          if (entry.getResource() instanceof Practitioner) {
            attester.setParty(new Reference((entry.getResource()).getId()));
          }
        }
      }
    }

    // custodian.assignedCustodian.representedCustodianOrganization -> custodian
    if (cdaClinicalDocument.getCustodian() != null 
        && !cdaClinicalDocument.getCustodian().isSetNullFlavor()) {
      if (cdaClinicalDocument.getCustodian().getAssignedCustodian() != null
          && !cdaClinicalDocument.getCustodian().getAssignedCustodian().isSetNullFlavor()) {
        if (cdaClinicalDocument.getCustodian()
            .getAssignedCustodian()
            .getRepresentedCustodianOrganization() != null
            && !cdaClinicalDocument.getCustodian()
                .getAssignedCustodian()
                .getRepresentedCustodianOrganization()
                .isSetNullFlavor()) {
          Organization fhirOrganization = transformCustodianOrganization2Organization(
              cdaClinicalDocument.getCustodian()
                  .getAssignedCustodian()
                  .getRepresentedCustodianOrganization());
          fhirComp.setCustodian(new Reference(fhirOrganization.getId()));
          fhirCompBundle.addEntry(new BundleEntryComponent().setResource(fhirOrganization));
        }
      }
    }

    return fhirCompBundle;
  }

  /**
   * Transforms a CDA Custodian Organization to a FHIR Organization.
   * @param cdaOrganization CDA Custodian
   * @return FHIR Organization
   */
  public Organization transformCustodianOrganization2Organization(
      org.openhealthtools.mdht.uml.cda.CustodianOrganization cdaOrganization) {
    if (cdaOrganization == null || cdaOrganization.isSetNullFlavor()) {
      return null;
    }

    Organization fhirOrganization = new Organization();

    // resource id
    IdType resourceId = new IdType("Organization", getUniqueId());
    fhirOrganization.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirOrganization.getMeta().addProfile(Constants.PROFILE_DAF_ORGANIZATION);
    }

    // id -> identifier
    if (cdaOrganization.getIds() != null && !cdaOrganization.getIds().isEmpty()) {
      for (II ii : cdaOrganization.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirOrganization.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // name.text -> name
    if (cdaOrganization.getName() != null && !cdaOrganization.getName().isSetNullFlavor()) {
      fhirOrganization.setName(cdaOrganization.getName().getText());
    }

    // telecom -> telecom
    if (cdaOrganization.getTelecoms() != null && !cdaOrganization.getTelecoms().isEmpty()) {
      for (TEL tel : cdaOrganization.getTelecoms()) {
        if (tel != null && !tel.isSetNullFlavor()) {
          fhirOrganization.addTelecom(dtt.transformTel2ContactPoint(tel));
        }
      }
    }

    // addr -> address
    if (cdaOrganization.getAddrs() != null && !cdaOrganization.getAddrs().isEmpty()) {
      for (AD ad : cdaOrganization.getAddrs()) {
        if (ad != null && !ad.isSetNullFlavor()) {
          fhirOrganization.addAddress(dtt.transformAD2Address(ad));
        }
      }
    }

    return fhirOrganization;
  }

  /**
   * Transforms a CDA Encounter into a FHIR Bundle of Encounter resources.
   * @param cdaEncounter CDA Encounter
   * @return FHIR Bundle
   */
  public Bundle transformEncounter2Encounter(
        org.openhealthtools.mdht.uml.cda.Encounter cdaEncounter) {
    if (cdaEncounter == null || cdaEncounter.isSetNullFlavor()) {
      return null;
    }

    Encounter fhirEncounter = new Encounter();

    Bundle fhirEncounterBundle = new Bundle();
    fhirEncounterBundle.addEntry(new BundleEntryComponent().setResource(fhirEncounter));

    // NOTE: hospitalization.period not found. However, daf requires it being mapped

    // resource id
    IdType resourceId = new IdType("Encounter", getUniqueId());
    fhirEncounter.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {  
      fhirEncounter.getMeta().addProfile(Constants.PROFILE_DAF_ENCOUNTER);
    }

    // patient
    fhirEncounter.setSubject(getPatientRef());

    // id -> identifier
    if (cdaEncounter.getIds() != null && !cdaEncounter.getIds().isEmpty()) {
      for (II id : cdaEncounter.getIds()) {
        if (id != null && !id.isSetNullFlavor()) {
          fhirEncounter.addIdentifier(dtt.transformII2Identifier(id));
        }
      }
    }

    // statusCode -> status
    if (cdaEncounter.getStatusCode() != null && !cdaEncounter.getStatusCode().isSetNullFlavor()) {
      if (vst.transformStatusCode2EncounterStatus(cdaEncounter.getStatusCode().getCode()) != null) {
        fhirEncounter.setStatus(
              vst.transformStatusCode2EncounterStatus(cdaEncounter.getStatusCode().getCode()));
      }
    } else {
      fhirEncounter.setStatus(Config.DEFAULT_ENCOUNTER_STATUS);
    }

    // code -> type
    if (cdaEncounter.getCode() != null && !cdaEncounter.getCode().isSetNullFlavor()) {
      fhirEncounter.addType(dtt.transformCD2CodeableConcept(cdaEncounter.getCode()));
    }

    // code.translation -> classElement
    if (cdaEncounter.getCode() != null && !cdaEncounter.getCode().isSetNullFlavor()) {
      if (cdaEncounter.getCode().getTranslations() != null 
          && !cdaEncounter.getCode().getTranslations().isEmpty()) {
        for (CD cd : cdaEncounter.getCode().getTranslations()) {
          if (cd != null && !cd.isSetNullFlavor()) {
            Coding encounterClass = vst.transformEncounterCode2EncounterClass(cd.getCode());
            if (encounterClass != null) {
              fhirEncounter.setClass_(encounterClass);
            }
          }
        }
      }
    }

    // priorityCode -> priority
    if (cdaEncounter.getPriorityCode() != null 
        && !cdaEncounter.getPriorityCode().isSetNullFlavor()) {
      fhirEncounter.setPriority(dtt.transformCD2CodeableConcept(cdaEncounter.getPriorityCode()));
    }

    // performer -> participant.individual
    if (cdaEncounter.getPerformers() != null && !cdaEncounter.getPerformers().isEmpty()) {
      for (Performer2 cdaPerformer : cdaEncounter.getPerformers()) {
        if (cdaPerformer != null && !cdaPerformer.isSetNullFlavor()) {
          EncounterParticipantComponent fhirParticipant = new  EncounterParticipantComponent();

          // default encunter participant type code
          fhirParticipant.addType().addCoding(Config.DEFAULT_ENCOUNTER_PARTICIPANT_TYPE_CODE);

          Practitioner fhirPractitioner = null;
          Bundle fhirPractitionerBundle = transformPerformer22Practitioner(cdaPerformer);
          for (BundleEntryComponent entry : fhirPractitionerBundle.getEntry()) {
            if (entry.getResource() instanceof Practitioner) {
              fhirPractitioner = (Practitioner) entry.getResource();
              fhirEncounterBundle.addEntry(
                    new Bundle().addEntry().setResource(entry.getResource()));
            }
          }

          fhirParticipant.setIndividual(new Reference(fhirPractitioner.getId()));
          fhirEncounter.addParticipant(fhirParticipant);
        }
      }
    }

    // effectiveTime -> period
    if (cdaEncounter.getEffectiveTime() != null 
        && !cdaEncounter.getEffectiveTime().isSetNullFlavor()) {
      fhirEncounter.setPeriod(dtt.transformIvl_TS2Period(cdaEncounter.getEffectiveTime()));
    }

    // participant[@typeCode='LOC'].participantRole[SDLOC] -> location.location
    if (cdaEncounter.getParticipants() != null && !cdaEncounter.getParticipants().isEmpty()) {
      for (Participant2 cdaParticipant : cdaEncounter.getParticipants()) {
        if (cdaParticipant != null && !cdaParticipant.isSetNullFlavor()) {
          // checking if the participant is location
          if (cdaParticipant.getTypeCode() == ParticipationType.LOC) {
            if (cdaParticipant.getParticipantRole() != null 
                && !cdaParticipant.getParticipantRole().isSetNullFlavor()) {
              if (cdaParticipant.getParticipantRole().getClassCode() != null
                  && cdaParticipant.getParticipantRole().getClassCode() == RoleClassRoot.SDLOC) {
                // We first make the mapping to a resource.location
                // then, we create a resource.encounter.location
                // then, we add the resource.location to resource.encounter.location

                // usage of ParticipantRole2Location
                Location fhirLocation = transformParticipantRole2Location(
                    cdaParticipant.getParticipantRole());

                fhirEncounterBundle.addEntry(new BundleEntryComponent().setResource(fhirLocation));
                fhirEncounter.addLocation().setLocation(new Reference(fhirLocation.getId()));
              }
            }
          }
        }
      }
    }

    // entryRelationship[@typeCode='RSON'].observation[Indication] -> indication
    if (cdaEncounter.getEntryRelationships() != null 
        && !cdaEncounter.getEntryRelationships().isEmpty()) {
      for (EntryRelationship entryRelShip : cdaEncounter.getEntryRelationships()) {
        if (entryRelShip != null && !entryRelShip.isSetNullFlavor()) {
          if (entryRelShip.getObservation() != null && !entryRelShip.isSetNullFlavor()) {
            if (entryRelShip.getObservation() instanceof Indication) {
              Indication cdaIndication = (Indication) entryRelShip.getObservation();
              Condition fhirIndication = transformIndication2Condition(cdaIndication);
              //TODO: Update this Section to keep the Condition seperate 
              //or create a Diagnosis Component.
              /*
              fhirEncounterBundle.addEntry(new BundleEntryComponent().setResource(fhirIndication));
              Reference indicationRef = fhirEncounter.addIndication();
              indicationRef.setReference(fhirIndication.getId());
              */
            }
          }
        }
      }
    }

    return fhirEncounterBundle;
  }

  /**
   * Transforms a CDA Encounter Activity to a FHIR Encounter.
   * @param cdaEncounterActivity CDA Encounter Activity
   * @return FHIR Encounter.
   */
  public Bundle transformEncounterActivity2Encounter(
      EncounterActivities cdaEncounterActivity) {
    /*
     * EncounterActivity2Encounter and Encounter2Encounter are nearly the same
     * methods. Since EncounterActivity class has neater methods, we may think of
     * using EncounterActivity2Encounter instead of Encounter2Encounter in later
     * times Also, notice that some of those methods are not working properly, yet.
     * Therefore, those of methods that are not working properly but seems to be
     * neater hasn't used in this implementation.
     */

    if (cdaEncounterActivity == null || cdaEncounterActivity.isSetNullFlavor()) {
      return null;
    }

    Encounter fhirEncounter = new Encounter();

    Bundle fhirEncounterBundle = new Bundle();
    fhirEncounterBundle.addEntry(new BundleEntryComponent().setResource(fhirEncounter));

    // NOTE: hospitalization.period not found. However, daf requires it being mapped

    // resource id
    IdType resourceId = new IdType("Encounter", getUniqueId());
    fhirEncounter.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirEncounter.getMeta().addProfile(Constants.PROFILE_DAF_ENCOUNTER);
    }

    // patient
    fhirEncounter.setSubject(getPatientRef());

    // id -> identifier
    if (cdaEncounterActivity.getIds() != null && !cdaEncounterActivity.getIds().isEmpty()) {
      for (II id : cdaEncounterActivity.getIds()) {
        if (id != null && !id.isSetNullFlavor()) {
          fhirEncounter.addIdentifier(dtt.transformII2Identifier(id));
        }
      }
    }

    // statusCode -> status
    if (cdaEncounterActivity.getStatusCode() != null 
        && !cdaEncounterActivity.getStatusCode().isSetNullFlavor()) {
      if (vst.transformStatusCode2EncounterStatus(
            cdaEncounterActivity.getStatusCode().getCode()) != null) {
        fhirEncounter.setStatus(
              vst.transformStatusCode2EncounterStatus(
                cdaEncounterActivity.getStatusCode().getCode()));
      }
    } else {
      fhirEncounter.setStatus(Config.DEFAULT_ENCOUNTER_STATUS);
    }

    // code -> type
    if (cdaEncounterActivity.getCode() != null 
        && !cdaEncounterActivity.getCode().isSetNullFlavor()) {
      fhirEncounter.addType(dtt.transformCD2CodeableConcept(cdaEncounterActivity.getCode()));
    }

    // code.translation -> classElement
    if (cdaEncounterActivity.getCode() != null 
        && !cdaEncounterActivity.getCode().isSetNullFlavor()) {
      if (cdaEncounterActivity.getCode().getTranslations() != null
          && !cdaEncounterActivity.getCode().getTranslations().isEmpty()) {
        for (CD cd : cdaEncounterActivity.getCode().getTranslations()) {
          if (cd != null && !cd.isSetNullFlavor()) {
            Coding encounterClass = vst.transformEncounterCode2EncounterClass(cd.getCode());
            if (encounterClass != null) {
              fhirEncounter.setClass_(encounterClass);
            }
          }
        }
      }
    }

    // priorityCode -> priority
    if (cdaEncounterActivity.getPriorityCode() != null 
        && !cdaEncounterActivity.getPriorityCode().isSetNullFlavor()) {
      fhirEncounter.setPriority(
            dtt.transformCD2CodeableConcept(cdaEncounterActivity.getPriorityCode()));
    }

    // performer -> participant.individual
    if (cdaEncounterActivity.getPerformers() != null 
        && !cdaEncounterActivity.getPerformers().isEmpty()) {
      for (Performer2 cdaPerformer : cdaEncounterActivity.getPerformers()) {
        if (cdaPerformer != null && !cdaPerformer.isSetNullFlavor()) {
          EncounterParticipantComponent fhirParticipant = new EncounterParticipantComponent();

          // default encounter participant type code
          fhirParticipant.addType().addCoding(Config.DEFAULT_ENCOUNTER_PARTICIPANT_TYPE_CODE);

          Practitioner fhirPractitioner = null;
          Bundle fhirPractitionerBundle = transformPerformer22Practitioner(cdaPerformer);
          for (BundleEntryComponent entry : fhirPractitionerBundle.getEntry()) {
            if (entry.getResource() instanceof Practitioner) {
              fhirPractitioner = (Practitioner) entry.getResource();
              fhirEncounterBundle.addEntry(
                  new BundleEntryComponent().setResource(entry.getResource()));
            }
          }
          fhirParticipant.setIndividual(new Reference(fhirPractitioner.getId()));
          fhirEncounter.addParticipant(fhirParticipant);
        }
      }
    }

    // effectiveTime -> period
    if (cdaEncounterActivity.getEffectiveTime() != null 
        && !cdaEncounterActivity.getEffectiveTime().isSetNullFlavor()) {
      fhirEncounter.setPeriod(dtt.transformIvl_TS2Period(cdaEncounterActivity.getEffectiveTime()));
    }

    // indication -> indication
    for (Indication cdaIndication : cdaEncounterActivity.getIndications()) {
      if (!cdaIndication.isSetNullFlavor()) {
        Condition fhirIndication = transformIndication2Condition(cdaIndication);
        fhirEncounterBundle.addEntry(new BundleEntryComponent().setResource(fhirIndication));
        //TODO: Will need to figure out whether to map this to a 
        // Diagnosis component or a Condition with reference to the Encounter.
        /*
        Reference indicationRef = fhirEncounter.addIndication();
        indicationRef.setReference(fhirIndication.getId());
        */
      }
    }

    // serviceDeliveryLocation -> location.location
    // Although encounter contains serviceDeliveryLocation,
    // getServiceDeliveryLocation method returns empty list
    // Therefore, get the location information from
    // participant[@typeCode='LOC'].participantRole
    // if(cdaEncounterActivity.getServiceDeliveryLocations() != null &&
    // !cdaEncounterActivity.getServiceDeliveryLocations().isEmpty()) {
    // for(ServiceDeliveryLocation SDLOC :
    // cdaEncounterActivity.getServiceDeliveryLocations()) {
    // if(SDLOC != null && !SDLOC.isSetNullFlavor()) {
    // ca.uhn.fhir.model.dstu2.resource.Location fhirLocation =
    // tServiceDeliveryLocation2Location(SDLOC);
    // fhirEncounterBundle.addEntry(new Bundle.Entry().setResource(fhirLocation));
    // fhirEncounter.addLocation().setLocation(new
    // ResourceReferenceDt(fhirLocation.getId()));
    // }
    // }
    // }

    // participant[@typeCode='LOC'].participantRole[SDLOC] -> location
    if (cdaEncounterActivity.getParticipants() != null 
        && !cdaEncounterActivity.getParticipants().isEmpty()) {
      for (Participant2 cdaParticipant : cdaEncounterActivity.getParticipants()) {
        if (cdaParticipant != null && !cdaParticipant.isSetNullFlavor()) {

          // checking if the participant is location
          if (cdaParticipant.getTypeCode() == ParticipationType.LOC) {
            if (cdaParticipant.getParticipantRole() != null 
                && !cdaParticipant.getParticipantRole().isSetNullFlavor()) {
              if (cdaParticipant.getParticipantRole().getClassCode() != null
                  && cdaParticipant.getParticipantRole().getClassCode() == RoleClassRoot.SDLOC) {
                // We first make the mapping to a resource.location
                // then, we create a resource.encounter.location
                // then, we add the resource.location to resource.encounter.location

                // usage of ParticipantRole2Location
                Location fhirLocation = transformParticipantRole2Location(
                    cdaParticipant.getParticipantRole());
                fhirEncounterBundle.addEntry(new BundleEntryComponent().setResource(fhirLocation));
                fhirEncounter.addLocation().setLocation(new Reference(fhirLocation.getId()));
              }
            }
          }
        }
      }
    }

    return fhirEncounterBundle;
  }

  /**
   * Transforms a CDA Entity into a FHIR Group.
   * @param cdaEntity CDA Entity. 
   * @return FHIR Group
   */
  public Group transformEntity2Group(Entity cdaEntity) {
    // never used
    if (cdaEntity == null || cdaEntity.isSetNullFlavor()) {
      return null;
    } else if (cdaEntity.getDeterminerCode() 
        != org.openhealthtools.mdht.uml.hl7.vocab.EntityDeterminer.KIND) {
      return null;
    }
    Group fhirGroup = new Group();

    // id -> identifier
    if (cdaEntity.getIds() != null && !cdaEntity.getIds().isEmpty()) {
      for (II id : cdaEntity.getIds()) {
        if (id != null && !id.isSetNullFlavor()) {
          if (id.getDisplayable()) {
            // unique
            fhirGroup.addIdentifier(dtt.transformII2Identifier(id));
          }
        }
      }
    }

    // classCode -> type
    if (cdaEntity.getClassCode() != null) {
      GroupType groupTypeEnum = vst.transformEntityClassRoot2GroupType(cdaEntity.getClassCode());
      if (groupTypeEnum != null) {
        fhirGroup.setType(groupTypeEnum);
      }

    }

    // deteminerCode -> actual
    if (cdaEntity.isSetDeterminerCode() && cdaEntity.getDeterminerCode() != null) {
      if (cdaEntity.getDeterminerCode() == EntityDeterminer.KIND) {
        fhirGroup.setActual(false);
      } else {
        fhirGroup.setActual(true);
      }
    }

    // code -> code
    if (cdaEntity.getCode() != null && !cdaEntity.getCode().isSetNullFlavor()) {
      fhirGroup.setCode(dtt.transformCD2CodeableConcept(cdaEntity.getCode()));
    }

    return fhirGroup;
  }

  /**
   * Transforms a CDA Family History Organizer into a FHIR FamilyMemeber History.
   * @param cdaFho CDA Familty history Organizer
   * @return FHIR Family Member History
   */
  public FamilyMemberHistory transformFamilyHistoryOrganizer2FamilyMemberHistory(
        FamilyHistoryOrganizer cdaFho) {
    if (cdaFho == null || cdaFho.isSetNullFlavor()) {
      return null;
    }

    FamilyMemberHistory fhirFmh = new FamilyMemberHistory();

    // resource id
    IdType resourceId = new IdType("FamilyMemberHistory", getUniqueId());
    fhirFmh.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirFmh.getMeta().addProfile(Constants.PROFILE_DAF_FAMILY_MEMBER_HISTORY);
    }

    // patient
    fhirFmh.setPatient(getPatientRef());

    // id -> identifier
    for (II id : cdaFho.getIds()) {
      if (id != null && !id.isSetNullFlavor()) {
        fhirFmh.addIdentifier(dtt.transformII2Identifier(id));
      }
    }

    // statusCode -> status
    if (cdaFho.getStatusCode() != null && !cdaFho.getStatusCode().isSetNullFlavor()) {
      fhirFmh.setStatus(
          vst.transformFamilyHistoryOrganizerStatusCode2FamilyHistoryStatus(
              cdaFho.getStatusCode().getCode()));
    }

    // condition <-> familyHistoryObservation
    // also, deceased value is set by looking at
    // familyHistoryObservation.familyHistoryDeathObservation
    if (cdaFho.getFamilyHistoryObservations() != null 
        && !cdaFho.getFamilyHistoryObservations().isEmpty()) {
      for (FamilyHistoryObservation familyHistoryObs : cdaFho
          .getFamilyHistoryObservations()) {
        if (familyHistoryObs != null && !familyHistoryObs.isSetNullFlavor()) {

          // adding a new condition to fhirFMH
          FamilyMemberHistoryConditionComponent condition = fhirFmh.addCondition();

          // familyHistoryObservation.value[@xsi:type='CD'] -> code
          for (ANY value : familyHistoryObs.getValues()) {
            if (value != null && !value.isSetNullFlavor()) {
              if (value instanceof CD) {
                condition.setCode(dtt.transformCD2CodeableConcept((CD) value));
              }
            }
          }

          // NOTE: An alternative is to use the relatedSubject/subject/sdtc:deceasedInd
          // and relatedSubject/subject/sdtc:deceasedTime values
          // deceased
          if (familyHistoryObs.getFamilyHistoryDeathObservation() != null
              && !familyHistoryObs.getFamilyHistoryDeathObservation().isSetNullFlavor()) {
            // if deathObservation exists, set fmh.deceased true
            fhirFmh.setDeceased(new BooleanType(true));

            // familyHistoryDeathObservation.value[@xsi:type='CD'] -> condition.outcome
            for (ANY value : familyHistoryObs.getFamilyHistoryDeathObservation().getValues()) {
              if (value != null && !value.isSetNullFlavor()) {
                if (value instanceof CD) {
                  condition.setOutcome(dtt.transformCD2CodeableConcept((CD) value));
                }
              }
            }
          }

          // familyHistoryObservation.ageObservation -> condition.onset
          if (familyHistoryObs.getAgeObservation() != null 
              && !familyHistoryObs.getAgeObservation().isSetNullFlavor()) {
            Age onset = transformAgeObservation2AgeDt(familyHistoryObs.getAgeObservation());
            if (onset != null) {
              condition.setOnset(onset);
            }
          }
        }
      }
    }

    // info from subject.relatedSubject
    if (cdaFho.getSubject() != null 
        && !cdaFho.isSetNullFlavor() 
        && cdaFho.getSubject().getRelatedSubject() != null
        && !cdaFho.getSubject().getRelatedSubject().isSetNullFlavor()) {
      RelatedSubject cdaRelatedSubject = cdaFho.getSubject().getRelatedSubject();

      // subject.relatedSubject.code -> relationship
      if (cdaRelatedSubject.getCode() != null && !cdaRelatedSubject.getCode().isSetNullFlavor()) {
        fhirFmh.setRelationship(dtt.transformCD2CodeableConcept(cdaRelatedSubject.getCode()));
      }

      // info from subject.relatedSubject.subject
      if (cdaRelatedSubject.getSubject() != null 
          && !cdaRelatedSubject.getSubject().isSetNullFlavor()) {
        SubjectPerson subjectPerson = cdaRelatedSubject.getSubject();

        // subject.relatedSubject.subject.name.text -> name
        for (EN en : subjectPerson.getNames()) {
          if (en != null && !en.isSetNullFlavor()) {
            if (en.getText() != null) {
              fhirFmh.setName(en.getText());
            }
          }
        }

        // subject.relatedSubject.subject.administrativeGenderCode -> gender
        if (subjectPerson.getAdministrativeGenderCode() != null
            && !subjectPerson.getAdministrativeGenderCode().isSetNullFlavor()
            && subjectPerson.getAdministrativeGenderCode().getCode() != null) {   
              
          fhirFmh.setGender(
              Enumerations.AdministrativeGender.fromCode(
                vst.transformAdministrativeGenderCode2AdministrativeGender(
                  subjectPerson.getAdministrativeGenderCode().getCode()).toCode()));
        }

        // subject.relatedSubject.subject.birthTime -> born
        if (subjectPerson.getBirthTime() != null 
            && !subjectPerson.getBirthTime().isSetNullFlavor()) {
          fhirFmh.setBorn(dtt.transformTS2Date(subjectPerson.getBirthTime()));
        }
      }
    }

    return fhirFmh;
  }


  /*
   * Functional Status Section contains: 1- Functional Status Observation 2-
   * Self-Care Activites Both of them have single Observation which needs mapping.
   * Therefore, the parameter for the following
   * method(tFunctionalStatus2Observation) chosen to be generic(Observation) .. to
   * cover the content of the section. Also, notice that the transformation of
   * those Observations are different from the generic Observation transformation
   */
  /**
   * Transforms a Functional Status to an FHIR Observation.
   * @param cdaObservation CDA Functional Observation.
   * @return FHIR Observation
   */
  public Bundle transformFunctionalStatus2Observation(
        org.openhealthtools.mdht.uml.cda.Observation cdaObservation) {
    if (cdaObservation == null || cdaObservation.isSetNullFlavor()) {
      return null;
    }

    Observation fhirObs = new Observation();

    Bundle fhirObsBundle = new Bundle();
    fhirObsBundle.addEntry(new BundleEntryComponent().setResource(fhirObs));

    // resource id
    IdType resourceId = new IdType("Observation", getUniqueId());
    fhirObs.setId(resourceId);

    // subject
    fhirObs.setSubject(getPatientRef());

    // statusCode -> status
    if (cdaObservation.getStatusCode() != null 
        && !cdaObservation.getStatusCode().isSetNullFlavor()) {
      if (cdaObservation.getStatusCode().getCode() != null 
          && !cdaObservation.getStatusCode().getCode().isEmpty()) {
        fhirObs.setStatus(
            vst.transformObservationStatusCode2ObservationStatus(
                cdaObservation.getStatusCode().getCode()));
      }
    }

    // id -> identifier
    if (cdaObservation.getIds() != null && !cdaObservation.getIds().isEmpty()) {
      for (II ii : cdaObservation.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirObs.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // code -> category
    if (cdaObservation.getCode() != null && !cdaObservation.isSetNullFlavor()) {
      fhirObs.addCategory(dtt.transformCD2CodeableConcept(cdaObservation.getCode()));
    }

    // value[@xsi:type='CD'] -> code
    if (cdaObservation.getValues() != null && !cdaObservation.getValues().isEmpty()) {
      for (ANY value : cdaObservation.getValues()) {
        if (value != null && !value.isSetNullFlavor()) {
          if (value instanceof CD) {
            fhirObs.setCode(dtt.transformCD2CodeableConcept((CD) value));
          }
        }
      }
    }

    // author -> performer
    if (cdaObservation.getAuthors() != null && !cdaObservation.getAuthors().isEmpty()) {
      for (org.openhealthtools.mdht.uml.cda.Author author : cdaObservation.getAuthors()) {
        if (author != null && !author.isSetNullFlavor()) {
          Practitioner fhirPractitioner = null;
          Bundle fhirPractitionerBundle = transformAuthor2Practitioner(author);

          for (BundleEntryComponent entry : fhirPractitionerBundle.getEntry()) {
            fhirObsBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));

            if (entry.getResource() instanceof Practitioner) {
              fhirPractitioner = (Practitioner) entry.getResource();
            }
          }
          fhirObs.addPerformer().setReference(fhirPractitioner.getId());
        }
      }
    }

    // effectiveTime -> effective
    if (cdaObservation.getEffectiveTime() != null 
        && !cdaObservation.getEffectiveTime().isSetNullFlavor()) {
      fhirObs.setEffective(dtt.transformIvl_TS2Period(cdaObservation.getEffectiveTime()));
    }

    // non-medicinal supply activity -> device
    if (cdaObservation.getEntryRelationships() != null 
        && !cdaObservation.getEntryRelationships().isEmpty()) {
      for (EntryRelationship entryRelShip : cdaObservation.getEntryRelationships()) {
        if (entryRelShip != null && !entryRelShip.isSetNullFlavor()) {
          // supply
          Supply cdaSupply = entryRelShip.getSupply();
          if (cdaSupply != null && !cdaSupply.isSetNullFlavor()) {
            if (cdaSupply instanceof NonMedicinalSupplyActivity) {
              // Non-Medicinal Supply Activity
              Device fhirDev = transformSupply2Device(cdaSupply);
              fhirObs.setDevice(new Reference(fhirDev.getId()));
              fhirObsBundle.addEntry(new BundleEntryComponent().setResource(fhirDev));
            }
          }
        }
      }
    }

    return fhirObsBundle;
  }

  /**
   * Transforms a Guardian to a Contact Component.
   * @param cdaGuardian CDA Guardian
   * @return FHIR Contact Component.
   */
  public ContactComponent transformGuardian2Contact(Guardian cdaGuardian) {
    if (cdaGuardian == null || cdaGuardian.isSetNullFlavor()) {
      return null;
    }

    ContactComponent fhirContact = new ContactComponent();

    // addr -> address
    if (cdaGuardian.getAddrs() != null && !cdaGuardian.getAddrs().isEmpty()) {
      fhirContact.setAddress(dtt.transformAD2Address(cdaGuardian.getAddrs().get(0)));
    }

    // telecom -> telecom
    if (cdaGuardian.getTelecoms() != null && !cdaGuardian.getTelecoms().isEmpty()) {
      for (TEL tel : cdaGuardian.getTelecoms()) {
        if (tel != null && !tel.isSetNullFlavor()) {
          fhirContact.addTelecom(dtt.transformTel2ContactPoint(tel));
        }
      }
    }

    // guardianPerson/name -> name
    if (cdaGuardian.getGuardianPerson() != null 
        && !cdaGuardian.getGuardianPerson().isSetNullFlavor()) {
      for (PN pn : cdaGuardian.getGuardianPerson().getNames()) {
        if (!pn.isSetNullFlavor()) {
          fhirContact.setName(dtt.transformEN2HumanName(pn));
        }
      }
    }

    // code -> relationship
    if (cdaGuardian.getCode() != null && !cdaGuardian.getCode().isSetNullFlavor()) {
      // try to use IValueSetsTransformer method
      // tRoleCode2PatientContactRelationshipCode
      CodeableConcept relationshipCoding = null;
      if (cdaGuardian.getCode().getCode() != null 
          && !cdaGuardian.getCode().getCode().isEmpty()) {
        relationshipCoding = 
            vst.transformRoleCode2PatientContactRelationshipCode(cdaGuardian.getCode().getCode());
      }
      // if tRoleCode2PatientContactRelationshipCode returns non-null value, add as
      // coding
      // otherwise, add relationship directly by making code
      // transformation(tCD2CodeableConcept)
      if (relationshipCoding != null) {
        fhirContact.addRelationship(relationshipCoding);
      } else {
        fhirContact.addRelationship(dtt.transformCD2CodeableConcept(cdaGuardian.getCode()));
      }
    }
    return fhirContact;
  }

  /**
   * Transforms a CDA Immunization Activity to a FHIR Immunization.
   * @param cdaImmunizationActivity CDA Immunization Activity.
   * @return FHIR Bundle of Immunications
   */
  public Bundle transformImmunizationActivity2Immunization(
        ImmunizationActivity cdaImmunizationActivity) {
          
    if (cdaImmunizationActivity == null || cdaImmunizationActivity.isSetNullFlavor()) {
      return null;
    }

    Immunization fhirImmunization = new Immunization();

    Bundle fhirImmunizationBundle = new Bundle();
    fhirImmunizationBundle.addEntry(new BundleEntryComponent().setResource(fhirImmunization));

    // resource id
    IdType resourceId = new IdType("Immunization", getUniqueId());
    fhirImmunization.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirImmunization.getMeta().addProfile(Constants.PROFILE_DAF_IMMUNIZATION);
    }

    // patient
    fhirImmunization.setPatient(getPatientRef());

    // id -> identifier
    if (cdaImmunizationActivity.getIds() != null && !cdaImmunizationActivity.getIds().isEmpty()) {
      for (II ii : cdaImmunizationActivity.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirImmunization.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // negationInd -> wasNotTaken
    if (cdaImmunizationActivity.getNegationInd() != null) {
      fhirImmunization.setNotGiven(cdaImmunizationActivity.getNegationInd());
    }

    // effectiveTime -> date
    if (cdaImmunizationActivity.getEffectiveTimes() != null 
        && !cdaImmunizationActivity.getEffectiveTimes().isEmpty()) {
      for (SXCM_TS effectiveTime : cdaImmunizationActivity.getEffectiveTimes()) {
        if (effectiveTime != null && !effectiveTime.isSetNullFlavor()) {
          // Asserting that at most one effective time exists
          fhirImmunization.setDateElement(dtt.transformTS2DateTime(effectiveTime));
        }
      }
    }

    // lotNumber, vaccineCode, organization
    if (cdaImmunizationActivity.getConsumable() != null 
        && !cdaImmunizationActivity.getConsumable().isSetNullFlavor()) {
      if (cdaImmunizationActivity.getConsumable().getManufacturedProduct() != null
          && !cdaImmunizationActivity.getConsumable()
              .getManufacturedProduct().isSetNullFlavor()) {
        ManufacturedProduct manufacturedProduct = 
            cdaImmunizationActivity.getConsumable().getManufacturedProduct();

        if (manufacturedProduct.getManufacturedMaterial() != null
            && !manufacturedProduct.getManufacturedMaterial().isSetNullFlavor()) {
          Material manufacturedMaterial = manufacturedProduct.getManufacturedMaterial();

          // consumable.manufacturedProduct.manufacturedMaterial.code -> vaccineCode
          if (manufacturedProduct.getManufacturedMaterial().getCode() != null
              && !manufacturedProduct.getManufacturedMaterial().getCode().isSetNullFlavor()) {
            fhirImmunization.setVaccineCode(
                  dtt.transformCD2CodeableConcept(manufacturedMaterial.getCode()));
          }

          // consumable.manufacturedProduct.manufacturedMaterial.lotNumberText ->
          // lotNumber
          if (manufacturedMaterial.getLotNumberText() != null
              && !manufacturedMaterial.getLotNumberText().isSetNullFlavor()) {
            fhirImmunization.setLotNumber(
                  dtt.transformST2String(manufacturedMaterial.getLotNumberText()).asStringValue());
          }
        }

        // consumable.manufacturedProduct.manufacturerOrganization -> manufacturer
        if (manufacturedProduct.getManufacturerOrganization() != null
            && !manufacturedProduct.getManufacturerOrganization().isSetNullFlavor()) {

          Organization fhirOrganization = transformOrganization2Organization(
              manufacturedProduct.getManufacturerOrganization());

          fhirImmunization.setManufacturer(new Reference(fhirOrganization.getId()));
          fhirImmunizationBundle.addEntry(new BundleEntryComponent().setResource(fhirOrganization));
        }
      }
    }

    // performer -> performer
    if (cdaImmunizationActivity.getPerformers() != null 
        && !cdaImmunizationActivity.getPerformers().isEmpty()) {
      for (Performer2 performer : cdaImmunizationActivity.getPerformers()) {
        if (performer.getAssignedEntity() != null 
            && !performer.getAssignedEntity().isSetNullFlavor()) {
          Bundle practBundle = transformPerformer22Practitioner(performer);
          for (BundleEntryComponent entry : practBundle.getEntry()) {
            // Add all the resources returned from the bundle to the main bundle
            fhirImmunizationBundle.addEntry(
                new BundleEntryComponent().setResource(entry.getResource()));
            // Add a reference to performer attribute only for Practitioner resource.
            // Further resources can include Organization.
            if (entry.getResource() instanceof Practitioner) {
              ImmunizationPractitionerComponent component = new ImmunizationPractitionerComponent();
              component.setActor(new Reference(entry.getResource().getId()));
              component.setRole(
                  new CodeableConcept().addCoding(
                      new Coding(Constants.IMMUNIZATION_PROVIDER_ROLE_SYSTEM,
                        "AP", 
                        "Administering Provider")));
              fhirImmunization.addPractitioner(component);
            }
          }
        }
      }
    }

    // approachSiteCode -> site
    for (CD cd : cdaImmunizationActivity.getApproachSiteCodes()) {
      fhirImmunization.setSite(dtt.transformCD2CodeableConcept(cd));
    }

    // routeCode -> route
    if (cdaImmunizationActivity.getRouteCode() != null 
        && !cdaImmunizationActivity.getRouteCode().isSetNullFlavor()) {
      fhirImmunization.setRoute(
            dtt.transformCD2CodeableConcept(cdaImmunizationActivity.getRouteCode()));
    }

    // doseQuantity -> doseQuantity
    if (cdaImmunizationActivity.getDoseQuantity() != null
        && !cdaImmunizationActivity.getDoseQuantity().isSetNullFlavor()) {
      fhirImmunization.setDoseQuantity(
            dtt.transformPQ2SimpleQuantity(cdaImmunizationActivity.getDoseQuantity()));
    }

    // statusCode -> status
    if (cdaImmunizationActivity.getStatusCode() != null 
        && !cdaImmunizationActivity.getStatusCode().isSetNullFlavor()) {
      if (cdaImmunizationActivity.getStatusCode().getCode() != null
          && !cdaImmunizationActivity.getStatusCode().getCode().isEmpty()) {
            
        fhirImmunization.setStatus(
              ImmunizationStatus.fromCode(cdaImmunizationActivity.getStatusCode().getCode()));
      }
    }

    // wasNotGiven == true
    if (fhirImmunization.getNotGiven()) {
      // immunizationRefusalReason.code -> explanation.reasonNotGiven
      if (cdaImmunizationActivity.getImmunizationRefusalReason() != null
          && !cdaImmunizationActivity.getImmunizationRefusalReason().isSetNullFlavor()) {
        if (cdaImmunizationActivity.getImmunizationRefusalReason().getCode() != null
            && !cdaImmunizationActivity
                  .getImmunizationRefusalReason().getCode().isSetNullFlavor()) {
          fhirImmunization.setExplanation(new ImmunizationExplanationComponent().addReasonNotGiven(
              dtt.transformCD2CodeableConcept(
                    cdaImmunizationActivity.getImmunizationRefusalReason().getCode())));
        }
      }
    } else if (!fhirImmunization.getNotGiven()) {
      // wasNotGiven == false
      // indication.value -> explanation.reason
      if (cdaImmunizationActivity.getIndication() != null
          && !cdaImmunizationActivity.getIndication().isSetNullFlavor()) {
        if (!cdaImmunizationActivity.getIndication().getValues().isEmpty()
            && cdaImmunizationActivity.getIndication().getValues().get(0) != null
            && !cdaImmunizationActivity.getIndication().getValues().get(0).isSetNullFlavor()) {
          fhirImmunization.setExplanation(new ImmunizationExplanationComponent()
              .addReason(dtt.transformCD2CodeableConcept(
                    (CD) cdaImmunizationActivity.getIndication().getValues().get(0))));
        }
      }
    }

    // reaction (i.e.
    // entryRelationship/observation[templateId/@root="2.16.840.1.113883.10.20.22.4.9"]
    // -> reaction
    if (cdaImmunizationActivity.getReactionObservation() != null
        && !cdaImmunizationActivity.getReactionObservation().isSetNullFlavor()) {
      Bundle reactionBundle = 
          transformReactionObservation2Observation(
                cdaImmunizationActivity.getReactionObservation());
      Observation fhirReactionObservation = null;
      for (BundleEntryComponent entry : reactionBundle.getEntry()) {
        fhirImmunizationBundle.addEntry(
              new BundleEntryComponent().setResource(entry.getResource()));
        if (entry.getResource() instanceof Observation) {
          fhirReactionObservation = (Observation) entry.getResource();
        }
        ImmunizationReactionComponent fhirReaction = fhirImmunization.addReaction();
        // reaction -> reaction.detail[ref=Observation]
        fhirReaction.setDetail(new Reference(fhirReactionObservation.getId()));

        // reaction/effectiveTime/low -> reaction.date
        if (fhirReactionObservation.getEffective() != null) {
          Period reactionDate = (Period) fhirReactionObservation.getEffective();
          if (reactionDate.getStart() != null) {
            fhirReaction.setDateElement(reactionDate.getStartElement());
          }
        }
      }
    }

    // primary source
    fhirImmunization.setPrimarySource(Config.DEFAULT_IMMUNIZATION_REPORTED);

    return fhirImmunizationBundle;

  }

  /**
   * Transforms a CDA Indication to a FHIR Condition.
   * @param cdaIndication CDA Indication
   * @return FHIR Condition
   */
  public Condition transformIndication2Condition(Indication cdaIndication) {
    if (cdaIndication == null || cdaIndication.isSetNullFlavor()) {
      return null;
    }

    Condition fhirCond = new Condition();

    // resource id
    IdType resourceId = new IdType("Condition", getUniqueId());
    fhirCond.setId(resourceId);

    // patient
    fhirCond.setSubject(getPatientRef());

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirCond.getMeta().addProfile(Constants.PROFILE_DAF_CONDITION);
    }

    // id -> identifier
    if (cdaIndication.getIds() != null && !cdaIndication.getIds().isEmpty()) {
      for (II ii : cdaIndication.getIds()) {
        fhirCond.addIdentifier(dtt.transformII2Identifier(ii));
      }
    }

    // code -> category
    if (cdaIndication.getCode() != null && !cdaIndication.getCode().isSetNullFlavor()) {
      if (cdaIndication.getCode().getCode() != null) {
        CodeableConcept conditionCategory = vst
            .transformProblemType2ConditionCategoryCode(cdaIndication.getCode().getCode());
        if (conditionCategory != null) {
          fhirCond.addCategory(conditionCategory);
        }
      }
    }

    // effectiveTime -> onset & abatement
    if (cdaIndication.getEffectiveTime() != null 
        && !cdaIndication.getEffectiveTime().isSetNullFlavor()) {

      IVXB_TS low = cdaIndication.getEffectiveTime().getLow();
      IVXB_TS high = cdaIndication.getEffectiveTime().getHigh();
      String value = cdaIndication.getEffectiveTime().getValue();

      // low and high are both empty, and only the @value exists -> onset
      if (low == null && high == null && value != null && !value.equals("")) {
        fhirCond.setOnset(dtt.transformString2DateTime(value));
      } else {
        // low -> onset
        if (low != null && !low.isSetNullFlavor()) {
          fhirCond.setOnset(dtt.transformTS2DateTime(low));
        }
        // high -> abatement
        if (high != null && !high.isSetNullFlavor()) {
          fhirCond.setAbatement(dtt.transformTS2DateTime(high));
        }
      }
    }

    // effectiveTime info -> clinicalStatus
    if (cdaIndication.getEffectiveTime() != null 
        && !cdaIndication.getEffectiveTime().isSetNullFlavor()) {
      // high & low is present -> resolved
      if (cdaIndication.getEffectiveTime().getLow() != null
          && !cdaIndication.getEffectiveTime().getLow().isSetNullFlavor()
          && cdaIndication.getEffectiveTime().getHigh() != null
          && !cdaIndication.getEffectiveTime().getHigh().isSetNullFlavor()) {
        
        fhirCond.setClinicalStatus(ConditionClinicalStatus.RESOLVED);
      } else if (cdaIndication.getEffectiveTime().getLow() != null
          && !cdaIndication.getEffectiveTime().getLow().isSetNullFlavor()) {
        // low is present, high is not present -> active
        fhirCond.setClinicalStatus(ConditionClinicalStatus.ACTIVE);
      } else if (cdaIndication.getEffectiveTime().getValue() != null) {
        // value is present, low&high is not present -> active
        fhirCond.setClinicalStatus(ConditionClinicalStatus.ACTIVE);
      }
    }

    // value[CD] -> code
    if (cdaIndication.getValues() != null && !cdaIndication.getValues().isEmpty()) {
      // There is only 1 value, but anyway...
      for (ANY value : cdaIndication.getValues()) {
        if (value != null && !value.isSetNullFlavor()) {
          if (value instanceof CD) {
            fhirCond.setCode(dtt.transformCD2CodeableConcept((CD) value));
          }
        }
      }
    }

    // NOTE: A default value is assigned to verificationStatus attribute, as it is
    // mandatory but cannot be mapped from the CDA side
    fhirCond.setVerificationStatus(Config.DEFAULT_CONDITION_VERIFICATION_STATUS);

    return fhirCond;
  }

  /**
   * Transforms a CDA Language to a Communication Component.
   * @param cdaLanguageCommunication CDA Language
   * @return Communication Component
   */
  public PatientCommunicationComponent transformLanguageCommunication2Communication(
        LanguageCommunication cdaLanguageCommunication) {
    if (cdaLanguageCommunication == null 
        || cdaLanguageCommunication.isSetNullFlavor()) {
      return null;
    }
    
    PatientCommunicationComponent fhirCommunication = new PatientCommunicationComponent();

    // languageCode -> language
    if (cdaLanguageCommunication.getLanguageCode() != null
        && !cdaLanguageCommunication.getLanguageCode().isSetNullFlavor()) {
      fhirCommunication.setLanguage(
            dtt.transformCD2CodeableConcept(cdaLanguageCommunication.getLanguageCode()));
      // urn:ietf:bcp:47 -> language.codeSystem      
      fhirCommunication.getLanguage()
          .getCodingFirstRep()
          .setSystem(Config.DEFAULT_COMMUNICATION_LANGUAGE_CODE_SYSTEM);
    }

    // preferenceInd -> preferred
    if (cdaLanguageCommunication.getPreferenceInd() != null
        && !cdaLanguageCommunication.getPreferenceInd().isSetNullFlavor()) {
      fhirCommunication.setPreferred(
          dtt.transformBL2Boolean(
            cdaLanguageCommunication.getPreferenceInd()).booleanValue());
    }

    return fhirCommunication;
  }

  /**
   * Transforms a CDA Manufactured Product to a FHIR Medication.
   * @param cdaManufacturedProduct CDA Manufactured Product
   * @return FHIR Medication
   */
  public Bundle transformManufacturedProduct2Medication(
        ManufacturedProduct cdaManufacturedProduct) {
    if (cdaManufacturedProduct == null || cdaManufacturedProduct.isSetNullFlavor()) {
      return null;
    }

    Medication fhirMedication = new Medication();

    Bundle fhirMedicationBundle = new Bundle();
    fhirMedicationBundle.addEntry(new BundleEntryComponent().setResource(fhirMedication));

    // resource id
    IdType resourceId = new IdType("Medication", getUniqueId());
    fhirMedication.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirMedication.getMeta().addProfile(Constants.PROFILE_DAF_MEDICATION);
    }

    // manufacturedMaterial -> code and ingredient
    if (cdaManufacturedProduct.getManufacturedMaterial() != null
        && !cdaManufacturedProduct.getManufacturedMaterial().isSetNullFlavor()) {
      if (cdaManufacturedProduct.getManufacturedMaterial().getCode() != null
          && !cdaManufacturedProduct.getManufacturedMaterial().isSetNullFlavor()) {
        // manufacturedMaterial.code -> code
        fhirMedication.setCode(
            dtt.transformCD2CodeableConceptExcludingTranslations(
                  cdaManufacturedProduct.getManufacturedMaterial().getCode()));
        // translation -> ingredient
        for (CD translation : cdaManufacturedProduct
                .getManufacturedMaterial().getCode().getTranslations()) {
          if (!translation.isSetNullFlavor()) {
            MedicationIngredientComponent fhirIngredient = fhirMedication.addIngredient();
            Substance fhirSubstance = transformCD2Substance(translation);
            fhirIngredient.setItem(new Reference(fhirSubstance.getId()));
            fhirMedicationBundle.addEntry(new BundleEntryComponent().setResource(fhirSubstance));
          }
        }
      }
    }

    // manufacturerOrganization -> manufacturer
    if (cdaManufacturedProduct.getManufacturerOrganization() != null
        && !cdaManufacturedProduct.getManufacturerOrganization().isSetNullFlavor()) {
      Organization org = transformOrganization2Organization(
            cdaManufacturedProduct.getManufacturerOrganization());
      fhirMedication.setManufacturer(new Reference(org.getId()));
      fhirMedicationBundle.addEntry(new BundleEntryComponent().setResource(org));
    }

    return fhirMedicationBundle;
  }

  /**
   * Transforms Medication Activity to a FHIR Resource Bundle.
   * @param cdaMedicationActivity CDA Medication Activity
   * @return FHIR Bundle
   */
  public Bundle transformMedicationActivity2MedicationStatement(
        MedicationActivity cdaMedicationActivity) {
    if (cdaMedicationActivity == null || cdaMedicationActivity.isSetNullFlavor()) {
      return null;
    }

    MedicationStatement fhirMedSt = new MedicationStatement();
    Dosage fhirDosage = fhirMedSt.addDosage();

    // bundle
    Bundle medStatementBundle = new Bundle();
    medStatementBundle.addEntry(new BundleEntryComponent().setResource(fhirMedSt));

    // resource id
    IdType resourceId = new IdType("MedicationStatement", getUniqueId());
    fhirMedSt.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirMedSt.getMeta().addProfile(Constants.PROFILE_DAF_MEDICATION_STATEMENT);
    }

    // patient
    fhirMedSt.setSubject(getPatientRef());

    // id -> identifier
    if (cdaMedicationActivity.getIds() != null && !cdaMedicationActivity.getIds().isEmpty()) {
      for (II ii : cdaMedicationActivity.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirMedSt.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // statusCode -> status
    if (cdaMedicationActivity.getStatusCode() != null 
        && !cdaMedicationActivity.getStatusCode().isSetNullFlavor()) {
      if (cdaMedicationActivity.getStatusCode().getCode() != null
          && !cdaMedicationActivity.getStatusCode().getCode().isEmpty()) {
        MedicationStatementStatus statusCode = 
            vst.transformStatusCode2MedicationStatementStatus(
                cdaMedicationActivity.getStatusCode().getCode());
        if (statusCode != null) {
          fhirMedSt.setStatus(statusCode);
        }
      }
    }

    // author[0] -> informationSource
    if (!cdaMedicationActivity.getAuthors().isEmpty()) {
      if (!cdaMedicationActivity.getAuthors().get(0).isSetNullFlavor()) {
        Bundle practBundle = 
            transformAuthor2Practitioner(cdaMedicationActivity.getAuthors().get(0));
        for (BundleEntryComponent entry : practBundle.getEntry()) {
          // Add all the resources returned from the bundle to the main bundle
          medStatementBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));
          // Add a reference to informationSource attribute only for Practitioner
          // resource. Further resources can include Organization.
          if (entry.getResource() instanceof Practitioner) {
            fhirMedSt.setInformationSource(new Reference(entry.getResource().getId()));
          }
        }
      }
    }

    // consumable.manufacturedProduct -> medication
    if (cdaMedicationActivity.getConsumable() != null 
        && !cdaMedicationActivity.getConsumable().isSetNullFlavor()) {
      if (cdaMedicationActivity.getConsumable().getManufacturedProduct() != null
          && !cdaMedicationActivity.getConsumable().getManufacturedProduct().isSetNullFlavor()) {
        Bundle fhirMedicationBundle = transformManufacturedProduct2Medication(
            cdaMedicationActivity.getConsumable().getManufacturedProduct());
        for (BundleEntryComponent entry : fhirMedicationBundle.getEntry()) {
          medStatementBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));
          if (entry.getResource() instanceof Medication) {
            fhirMedSt.setMedication(new Reference(entry.getResource().getId()));
          }
        }
      }
    }

    // getting info from effectiveTimes
    if (cdaMedicationActivity.getEffectiveTimes() != null 
        && !cdaMedicationActivity.getEffectiveTimes().isEmpty()) {
      for (SXCM_TS ts : cdaMedicationActivity.getEffectiveTimes()) {
        if (ts != null && !ts.isSetNullFlavor()) {
          // effectiveTime[@xsi:type='IVL_TS'] -> effective
          if (ts instanceof IVL_TS) {
            fhirMedSt.setEffective(dtt.transformIvl_TS2Period((IVL_TS) ts));
          }
          // effectiveTime[@xsi:type='PIVL_TS'] -> dosage.timing
          if (ts instanceof PIVL_TS) {
            fhirDosage.setTiming(dtt.transformPivl_TS2Timing((PIVL_TS) ts));
          }
        }
      }
    }

    // doseQuantity -> dosage.quantity
    if (cdaMedicationActivity.getDoseQuantity() != null 
        && !cdaMedicationActivity.getDoseQuantity().isSetNullFlavor()) {
      fhirDosage.setDose(dtt.transformPQ2SimpleQuantity(cdaMedicationActivity.getDoseQuantity()));
    }

    // routeCode -> dosage.route
    if (cdaMedicationActivity.getRouteCode() != null 
        && !cdaMedicationActivity.getRouteCode().isSetNullFlavor()) {
      fhirDosage.setRoute(dtt.transformCD2CodeableConcept(cdaMedicationActivity.getRouteCode()));
    }

    // rateQuantity -> dosage.rate
    if (cdaMedicationActivity.getRateQuantity() != null 
        && !cdaMedicationActivity.getRateQuantity().isSetNullFlavor()) {
      fhirDosage.setRate(dtt.transformIvl_PQ2Range(cdaMedicationActivity.getRateQuantity()));
    }

    // maxDoseQuantity -> dosage.maxDosePerPeriod
    if (cdaMedicationActivity.getMaxDoseQuantity() != null
        && !cdaMedicationActivity.getMaxDoseQuantity().isSetNullFlavor()) {
      // cdaDataType.RTO does nothing but extends cdaDataType.RTO_PQ_PQ
      fhirDosage.setMaxDosePerPeriod(
            dtt.transformRto2Ratio((RTO) cdaMedicationActivity.getMaxDoseQuantity()));
    }

    // negationInd -> wasNotTaken
    if (cdaMedicationActivity.getNegationInd() != null) {
      fhirMedSt.setTaken(MedicationStatementTaken.fromCode("n"));
    }

    // indication -> reason
    for (Indication indication : cdaMedicationActivity.getIndications()) {
      // First, to set reasonForUse, we need to set wasNotTaken to false
      fhirMedSt.setTaken(MedicationStatementTaken.fromCode("y"));

      Condition cond = transformIndication2Condition(indication);
      medStatementBundle.addEntry(new BundleEntryComponent().setResource(cond));
      fhirMedSt.addReasonReference(new Reference(cond.getId()));
    }

    return medStatementBundle;
  }

  /**
   * Transforms CDA Medication Dispense to a FHIR Medication Dispense.
   * @param cdaMedicationDispense CDA Medication Dispense
   * @return FHIR Medication Dispense
   */
  public Bundle transformMedicationDispense2MedicationDispense(
      org.openhealthtools.mdht.uml.cda.consol.MedicationDispense cdaMedicationDispense) {

    if (cdaMedicationDispense == null || cdaMedicationDispense.isSetNullFlavor()) {
      return null;
    }

    // NOTE: Following mapping doesn't really suit the mapping proposed by daf

    MedicationDispense fhirMediDisp = new MedicationDispense();
    Bundle fhirMediDispBundle = new Bundle();
    fhirMediDispBundle.addEntry(new BundleEntryComponent().setResource(fhirMediDisp));

    // patient
    fhirMediDisp.setSubject(getPatientRef());

    // resource id
    IdType resourceId = new IdType("MedicationDispense", getUniqueId());
    fhirMediDisp.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirMediDisp.getMeta().addProfile(Constants.PROFILE_DAF_MEDICATION_DISPENSE);
    }

    // id -> identifier
    if (cdaMedicationDispense.getIds() != null & !cdaMedicationDispense.getIds().isEmpty()) {
      for (II ii : cdaMedicationDispense.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          // Asserting at most one identifier exists
          fhirMediDisp.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // statusCode -> status
    if (cdaMedicationDispense.getStatusCode() != null 
        && !cdaMedicationDispense.getStatusCode().isSetNullFlavor()) {
      if (cdaMedicationDispense.getStatusCode().getCode() != null
          && !cdaMedicationDispense.getStatusCode().getCode().isEmpty()) {
        MedicationDispenseStatus mediDispStatEnum = vst
            .transformStatusCode2MedicationDispenseStatus(
                  cdaMedicationDispense.getStatusCode().getCode());
        if (mediDispStatEnum != null) {
          fhirMediDisp.setStatus(mediDispStatEnum);
        }
      }
    }

    // code -> type
    if (cdaMedicationDispense.getCode() != null 
        && !cdaMedicationDispense.getCode().isSetNullFlavor()) {
      fhirMediDisp.setType(dtt.transformCD2CodeableConcept(cdaMedicationDispense.getCode()));
    }

    // product.manufacturedProduct(MedicationInformation) -> medication
    if (cdaMedicationDispense.getProduct() != null 
        && !cdaMedicationDispense.getProduct().isSetNullFlavor()) {
      if (cdaMedicationDispense.getProduct().getManufacturedProduct() != null
          && !cdaMedicationDispense.getProduct()
                .getManufacturedProduct().isSetNullFlavor()) {
        if (cdaMedicationDispense.getProduct()
                .getManufacturedProduct() instanceof MedicationInformation) {
          MedicationInformation cdaMedicationInformation = 
              (MedicationInformation) cdaMedicationDispense.getProduct()
              .getManufacturedProduct();
          Medication fhirMedication = null;
          Bundle fhirMedicationBundle = 
              transformMedicationInformation2Medication(cdaMedicationInformation);

          for (BundleEntryComponent entry : fhirMedicationBundle.getEntry()) {
            fhirMediDispBundle.addEntry(
                  new BundleEntryComponent().setResource(entry.getResource()));
            if (entry.getResource() instanceof Medication) {
              fhirMedication = (Medication) entry.getResource();
            }
          }
          fhirMediDisp.setMedication(new Reference(fhirMedication.getId()));
        }
      }
    }

    // performer -> dispenser
    if (cdaMedicationDispense.getPerformers() != null 
        && !cdaMedicationDispense.getPerformers().isEmpty()) {
      for (Performer2 cdaPerformer : cdaMedicationDispense.getPerformers()) {
        if (cdaPerformer != null && !cdaPerformer.isSetNullFlavor()) {
          // Asserting that at most one performer exists
          Practitioner fhirPractitioner = null;
          Bundle fhirPractitionerBundle = transformPerformer22Practitioner(cdaPerformer);

          for (BundleEntryComponent entry : fhirPractitionerBundle.getEntry()) {
            fhirMediDispBundle.addEntry(
                  new BundleEntryComponent().setResource(entry.getResource()));
            if (entry.getResource() instanceof Practitioner) {
              fhirPractitioner = (Practitioner) entry.getResource();
            }
          }
          MedicationDispensePerformerComponent performer = 
              new MedicationDispensePerformerComponent();
          performer.setActor(new Reference(fhirPractitioner.getId()));

          fhirMediDisp.addPerformer(performer);
        }
      }
    }

    // quantity -> quantity
    if (cdaMedicationDispense.getQuantity() != null 
        && !cdaMedicationDispense.getQuantity().isSetNullFlavor()) {
      fhirMediDisp.setQuantity(dtt.transformPQ2SimpleQuantity(cdaMedicationDispense.getQuantity()));
    }

    // whenPrepared and whenHandedOver
    // effectiveTime[0] -> whenPrepared, effectiveTime[1] -> whenHandedOver
    int effectiveTimeCount = 0;
    if (cdaMedicationDispense.getEffectiveTimes() != null 
        && !cdaMedicationDispense.getEffectiveTimes().isEmpty()) {
      for (SXCM_TS ts : cdaMedicationDispense.getEffectiveTimes()) {
        if (effectiveTimeCount == 0) {
          // effectiveTime[0] -> whenPrepared
          if (ts != null && !ts.isSetNullFlavor()) {
            fhirMediDisp.setWhenPrepared(dtt.transformTS2DateTime(ts).getValue());
          }
          effectiveTimeCount++;
        } else if (effectiveTimeCount == 1) {
          // effectiveTime[1] -> whenHandedOver
          if (ts != null && !ts.isSetNullFlavor()) {
            fhirMediDisp.setWhenHandedOver(dtt.transformTS2DateTime(ts).getValue());
          }
          effectiveTimeCount++;
        }
      }
    }

    // Adding dosageInstruction
    Dosage fhirDosageInstruction = fhirMediDisp.addDosageInstruction();

    // TODO: The information used for dosageInstruction is used for different
    // fields, too.
    // Determine which field the information should fit

    // effectiveTimes -> dosageInstruction.timing.event
    if (cdaMedicationDispense.getEffectiveTimes() != null 
        && !cdaMedicationDispense.getEffectiveTimes().isEmpty()) {
      Timing fhirTiming = new Timing();

      // adding effectiveTimes to fhirTiming
      for (SXCM_TS ts : cdaMedicationDispense.getEffectiveTimes()) {
        if (ts != null && !ts.isSetNullFlavor()) {
          fhirTiming.addEvent(dtt.transformTS2DateTime(ts).getValue());
        } else if (ts.getValue() != null && !ts.getValue().isEmpty()) {
          fhirTiming.addEvent(dtt.transformString2DateTime(ts.getValue()).getValue());
        }
      }

      // setting fhirTiming for dosageInstruction if it is not empty
      if (!fhirTiming.isEmpty()) {
        fhirDosageInstruction.setTiming(fhirTiming);
      }
    }

    // quantity -> dosageInstruction.dose
    if (cdaMedicationDispense.getQuantity() != null 
        && !cdaMedicationDispense.getQuantity().isSetNullFlavor()) {
      fhirDosageInstruction.setDose(
            dtt.transformPQ2SimpleQuantity(cdaMedicationDispense.getQuantity()));
    }

    return fhirMediDispBundle;
  }

  /**
   * Transforms CDA Medication Information to a FHIR Bundle.
   * @param cdaMedicationInformation CDA Medication Information
   * @return FHIR Bundle
   */
  public Bundle transformMedicationInformation2Medication(
        MedicationInformation cdaMedicationInformation) {
    /*
     * Since MedicationInformation is a ManufacturedProduct instance with a specific
     * templateId, tManufacturedProduct2Medication should satisfy the required
     * mapping for MedicationInformation
     */
    return transformManufacturedProduct2Medication(cdaMedicationInformation);
  }

  /**
   * Transforms a CDA Observation to a FHIR Observation.
   * @param cdaObservation CDA Observation
   * @return FHIR Observation
   */
  public Bundle transformObservation2Observation(
        org.openhealthtools.mdht.uml.cda.Observation cdaObservation) {
    if (cdaObservation == null || cdaObservation.isSetNullFlavor()) {
      return null;
    }

    Observation fhirObs = new Observation();

    // bundle
    Bundle fhirObsBundle = new Bundle();
    fhirObsBundle.addEntry(new BundleEntryComponent().setResource(fhirObs));

    // resource id
    IdType resourceId = new IdType("Observation", getUniqueId());
    fhirObs.setId(resourceId);

    // subject
    fhirObs.setSubject(getPatientRef());

    // id -> identifier
    if (cdaObservation.getIds() != null && !cdaObservation.getIds().isEmpty()) {
      for (II ii : cdaObservation.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirObs.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // code -> code
    if (cdaObservation.getCode() != null && !cdaObservation.getCode().isSetNullFlavor()) {
      fhirObs.setCode(dtt.transformCD2CodeableConcept(cdaObservation.getCode()));
    }

    // statusCode -> status
    if (cdaObservation.getStatusCode() != null 
        && !cdaObservation.getStatusCode().isSetNullFlavor()) {
      if (cdaObservation.getStatusCode().getCode() != null) {
        fhirObs.setStatus(
              vst.transformObservationStatusCode2ObservationStatus(
                  cdaObservation.getStatusCode().getCode()));
      }
    }

    // effectiveTime -> effective
    if (cdaObservation.getEffectiveTime() != null 
        && !cdaObservation.getEffectiveTime().isSetNullFlavor()) {
      fhirObs.setEffective(dtt.transformIvl_TS2Period(cdaObservation.getEffectiveTime()));
    }

    // targetSiteCode -> bodySite
    for (CD cd : cdaObservation.getTargetSiteCodes()) {
      if (cd != null && !cd.isSetNullFlavor()) {
        fhirObs.setBodySite(dtt.transformCD2CodeableConcept(cd));
      }
    }

    // value or dataAbsentReason
    if (cdaObservation.getValues() != null && !cdaObservation.getValues().isEmpty()) {
      // We traverse the values in cdaObs
      for (ANY value : cdaObservation.getValues()) {
        if (value == null) {
          continue; // If the value is null, continue
        } else if (value.isSetNullFlavor()) {
          // If a null flavor exists, then we set dataAbsentReason by looking at the
          // null-flavor value
          Coding dataAbsentReasonCode = 
              vst.transformNullFlavor2DataAbsentReasonCode(value.getNullFlavor());
          if (dataAbsentReasonCode != null) {
            if (fhirObs.getDataAbsentReason() == null || fhirObs.getDataAbsentReason().isEmpty()) {
              // If DataAbsentReason was not set, create a new CodeableConcept and add our
              // code into it
              fhirObs.setDataAbsentReason(new CodeableConcept().addCoding(dataAbsentReasonCode));
            } else {
              // If DataAbsentReason was set, just get the CodeableConcept and add our code
              // into it
              fhirObs.getDataAbsentReason().addCoding(dataAbsentReasonCode);
            }
          }
        } else {
          // If a non-null value which has no null-flavor exists, then we can get the
          // value
          // Checking the type of value
          if (value instanceof CD) {
            fhirObs.setValue(dtt.transformCD2CodeableConcept((CD) value));
          } else if (value instanceof PQ) {
            fhirObs.setValue(dtt.transformPQ2Quantity((PQ) value));
          } else if (value instanceof ST) {
            fhirObs.setValue(dtt.transformST2String((ST) value));
          } else if (value instanceof IVL_PQ) {
            fhirObs.setValue(dtt.transformIvl_PQ2Range((IVL_PQ) value));
          } else if (value instanceof RTO) {
            fhirObs.setValue(dtt.transformRto2Ratio((RTO) value));
          } else if (value instanceof ED) {
            fhirObs.setValue(dtt.transformED2Attachment((ED) value));
          } else if (value instanceof TS) {
            fhirObs.setValue(dtt.transformTS2DateTime((TS) value));
          }
        }
      }
    }

    // encounter -> encounter
    if (cdaObservation.getEncounters() != null && !cdaObservation.getEncounters().isEmpty()) {
      for (org.openhealthtools.mdht.uml.cda.Encounter cdaEncounter 
          : cdaObservation.getEncounters()) {
        if (cdaEncounter != null && !cdaEncounter.isSetNullFlavor()) {
          // Asserting at most one encounter exists
          Encounter fhirEncounter = null;
          Bundle fhirEncounterBundle = transformEncounter2Encounter(cdaEncounter);
          for (BundleEntryComponent entity : fhirEncounterBundle.getEntry()) {
            if (entity.getResource() instanceof Encounter) {
              fhirEncounter = (Encounter) entity.getResource();
            }
          }

          if (fhirEncounter != null) {
            Reference fhirEncounterReference = new Reference();
            fhirEncounterReference.setReference(fhirEncounter.getId());
            fhirObs.setContext(fhirEncounterReference);
            fhirObsBundle.addEntry(new BundleEntryComponent().setResource(fhirEncounter));
          }
        }
      }
    }

    // author -> performer
    for (org.openhealthtools.mdht.uml.cda.Author author : cdaObservation.getAuthors()) {
      if (author != null && !author.isSetNullFlavor()) {
        Bundle fhirPractitionerBundle = transformAuthor2Practitioner(author);
        for (BundleEntryComponent entry : fhirPractitionerBundle.getEntry()) {
          fhirObsBundle.addEntry(entry);
          if (entry.getResource() instanceof Practitioner) {
            fhirObs.addPerformer().setReference(entry.getResource().getId());
          }
        }
      }
    }

    // methodCode -> method
    for (CE method : cdaObservation.getMethodCodes()) {
      if (method != null && !method.isSetNullFlavor()) {
        // Asserting that only one method exists
        fhirObs.setMethod(dtt.transformCD2CodeableConcept(method));
      }
    }

    // author.time -> issued
    if (cdaObservation.getAuthors() != null && !cdaObservation.getAuthors().isEmpty()) {
      for (org.openhealthtools.mdht.uml.cda.Author author : cdaObservation.getAuthors()) {
        if (author != null && !author.isSetNullFlavor()) {
          // get time from author
          if (author.getTime() != null && !author.getTime().isSetNullFlavor()) {
            fhirObs.setIssued(dtt.transformTS2Instant(author.getTime()).getValue());
          }
        }
      }
    }

    // interpretationCode -> interpretation
    if (cdaObservation.getInterpretationCodes() != null 
        && !cdaObservation.getInterpretationCodes().isEmpty()) {
      for (CE cdaInterprCode : cdaObservation.getInterpretationCodes()) {
        if (cdaInterprCode != null && !cdaInterprCode.isSetNullFlavor()) {
          // Asserting that only one interpretation code exists
          fhirObs.setInterpretation(
                vst.transformObservationInterpretationCode2ObservationInterpretationCode(
                    cdaInterprCode));
        }
      }
    }

    // referenceRange -> referenceRange
    if (cdaObservation.getReferenceRanges() != null 
        && !cdaObservation.getReferenceRanges().isEmpty()) {
      for (ReferenceRange cdaReferenceRange : cdaObservation.getReferenceRanges()) {
        if (cdaReferenceRange != null && !cdaReferenceRange.isSetNullFlavor()) {
          fhirObs.addReferenceRange(transformReferenceRange2ReferenceRange(cdaReferenceRange));
        }
      }
    }

    return fhirObsBundle;
  }

  /**
   * Transforms a CDA Organization into a FHIR Organization.
   * @param cdaOrganization CDA Organization
   * @return FHIR Organization
   */
  public Organization transformOrganization2Organization(
        org.openhealthtools.mdht.uml.cda.Organization cdaOrganization) {
    if (cdaOrganization == null || cdaOrganization.isSetNullFlavor()) {
      return null;
    }

    Organization fhirOrganization = new Organization();

    // resource id
    IdType resourceId = new IdType("Organization", getUniqueId());
    fhirOrganization.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirOrganization.getMeta().addProfile(Constants.PROFILE_DAF_ORGANIZATION);
    }

    // id -> identifier
    if (cdaOrganization.getIds() != null && !cdaOrganization.getIds().isEmpty()) {
      for (II ii : cdaOrganization.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirOrganization.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // name -> name
    if (cdaOrganization.getNames() != null && !cdaOrganization.isSetNullFlavor()) {
      for (ON name : cdaOrganization.getNames()) {
        if (name != null && !name.isSetNullFlavor() 
            && name.getText() != null && !name.getText().isEmpty()) {
          fhirOrganization.setName(name.getText());
        }
      }
    }

    // telecom -> telecom
    if (cdaOrganization.getTelecoms() != null && !cdaOrganization.getTelecoms().isEmpty()) {
      for (TEL tel : cdaOrganization.getTelecoms()) {
        if (tel != null && !tel.isSetNullFlavor()) {
          fhirOrganization.addTelecom(dtt.transformTel2ContactPoint(tel));
        }
      }
    }

    // addr -> address
    if (cdaOrganization.getAddrs() != null && !cdaOrganization.getAddrs().isEmpty()) {
      for (AD ad : cdaOrganization.getAddrs()) {
        if (ad != null && !ad.isSetNullFlavor()) {
          fhirOrganization.addAddress(dtt.transformAD2Address(ad));
        }
      }
    }

    return fhirOrganization;
  }

  /**
   * Transforms a CDA Participant Role to FHIR Location.
   * @param cdaParticipantRole CDA Participant Role
   * @return FHIR Location
   */
  public Location transformParticipantRole2Location(ParticipantRole cdaParticipantRole) {
    if (cdaParticipantRole == null || cdaParticipantRole.isSetNullFlavor()) {
      return null;
    }

    Location fhirLocation = new Location();

    // resource id
    IdType resourceId = new IdType("Location", getUniqueId());
    fhirLocation.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirLocation.getMeta().addProfile(Constants.PROFILE_DAF_LOCATION);
    }

    // id -> identifier
    if (cdaParticipantRole.getIds() != null && !cdaParticipantRole.getIds().isEmpty()) {
      for (II ii : cdaParticipantRole.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirLocation.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // code -> type
    // TODO: Requires huge mapping work from HL7 HealthcareServiceLocation value set
    // to http://hl7.org/fhir/ValueSet/v3-ServiceDeliveryLocationRoleType
    if (cdaParticipantRole.getCode() != null && !cdaParticipantRole.getCode().isSetNullFlavor()) {
      logger.info(
          "Found location.code in the CDA document, which can be mapped to Location.type on the FHIR side. But this is skipped for the moment, as it requires huge mapping work from HL7 HealthcareServiceLocation value set to http://hl7.org/fhir/ValueSet/v3-ServiceDeliveryLocationRoleType");
      // fhirLocation.setType();
    }

    // playingEntity.name.text -> name
    if (cdaParticipantRole.getPlayingEntity() != null 
        && !cdaParticipantRole.getPlayingEntity().isSetNullFlavor()) {
      if (cdaParticipantRole.getPlayingEntity().getNames() != null
          && !cdaParticipantRole.getPlayingEntity().getNames().isEmpty()) {
        for (PN pn : cdaParticipantRole.getPlayingEntity().getNames()) {
          // Asserting that at most one name exists
          if (pn != null && !pn.isSetNullFlavor()) {
            fhirLocation.setName(pn.getText());
          }
        }
      }
    }

    // telecom -> telecom
    if (cdaParticipantRole.getTelecoms() != null && !cdaParticipantRole.getTelecoms().isEmpty()) {
      for (TEL tel : cdaParticipantRole.getTelecoms()) {
        if (tel != null && !tel.isSetNullFlavor()) {
          fhirLocation.addTelecom(dtt.transformTel2ContactPoint(tel));
        }
      }
    }

    // addr -> address
    if (cdaParticipantRole.getAddrs() != null && !cdaParticipantRole.getAddrs().isEmpty()) {
      for (AD ad : cdaParticipantRole.getAddrs()) {
        // Asserting that at most one address exists
        if (ad != null && !ad.isSetNullFlavor()) {
          fhirLocation.setAddress(dtt.transformAD2Address(ad));
        }
      }
    }

    return fhirLocation;
  }

  /**
   * Transform CDA Patient Role to FHIR Patient.
   * @param cdaPatientRole CDA Patient Role.
   * @return FHIR Patient
   */
  public Bundle transformPatientRole2Patient(PatientRole cdaPatientRole) {
    if (cdaPatientRole == null || cdaPatientRole.isSetNullFlavor()) {
      return null;
    }

    Patient fhirPatient = new Patient();

    Bundle fhirPatientBundle = new Bundle();
    fhirPatientBundle.addEntry(new BundleEntryComponent().setResource(fhirPatient));

    // resource id
    IdType resourceId = new IdType("Patient", getUniqueId());
    fhirPatient.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirPatient.getMeta().addProfile(Constants.PROFILE_DAF_PATIENT);
    }

    // id -> identifier
    if (cdaPatientRole.getIds() != null && !cdaPatientRole.getIds().isEmpty()) {
      for (II id : cdaPatientRole.getIds()) {
        if (id != null && !id.isSetNullFlavor()) {
          fhirPatient.addIdentifier(dtt.transformII2Identifier(id));
        }
      }
    }

    // addr -> address
    for (AD ad : cdaPatientRole.getAddrs()) {
      if (ad != null && !ad.isSetNullFlavor()) {
        fhirPatient.addAddress(dtt.transformAD2Address(ad));
      }
    }

    // telecom -> telecom
    for (TEL tel : cdaPatientRole.getTelecoms()) {
      if (tel != null && !tel.isSetNullFlavor()) {
        fhirPatient.addTelecom(dtt.transformTel2ContactPoint(tel));
      }
    }

    // providerOrganization -> managingOrganization
    if (cdaPatientRole.getProviderOrganization() != null
        && !cdaPatientRole.getProviderOrganization().isSetNullFlavor()) {
      Organization fhirOrganization = transformOrganization2Organization(
          cdaPatientRole.getProviderOrganization());
      fhirPatientBundle.addEntry(new BundleEntryComponent().setResource(fhirOrganization));
      Reference organizationReference = new Reference(fhirOrganization.getId());
      fhirPatient.setManagingOrganization(organizationReference);
    }

    org.openhealthtools.mdht.uml.cda.Patient cdaPatient = cdaPatientRole.getPatient();

    if (cdaPatient != null && !cdaPatient.isSetNullFlavor()) {
      // patient.name -> name
      for (PN pn : cdaPatient.getNames()) {
        if (pn != null && !pn.isSetNullFlavor()) {
          fhirPatient.addName(dtt.transformEN2HumanName(pn));
        }
      }

      // patient.administrativeGenderCode -> gender
      if (cdaPatient.getAdministrativeGenderCode() != null
          && !cdaPatient.getAdministrativeGenderCode().isSetNullFlavor()
          && cdaPatient.getAdministrativeGenderCode().getCode() != null
          && !cdaPatient.getAdministrativeGenderCode().getCode().isEmpty()) {
        fhirPatient.setGender(
              vst.transformAdministrativeGenderCode2AdministrativeGender(
                  cdaPatient.getAdministrativeGenderCode().getCode()));
      }

      // patient.birthTime -> birthDate
      if (cdaPatient.getBirthTime() != null && !cdaPatient.getBirthTime().isSetNullFlavor()) {
        fhirPatient.setBirthDate(dtt.transformTS2Date(cdaPatient.getBirthTime()).getValue());
      }

      // patient.maritalStatusCode -> maritalStatus
      if (cdaPatient.getMaritalStatusCode() != null 
          && !cdaPatient.getMaritalStatusCode().isSetNullFlavor()) {
        if (cdaPatient.getMaritalStatusCode().getCode() != null
            && !cdaPatient.getMaritalStatusCode().getCode().isEmpty()) {
          fhirPatient.setMaritalStatus(
              vst.transformMaritalStatusCode2MaritalStatusCodes(
                    cdaPatient.getMaritalStatusCode().getCode()));
        }
      }

      // patient.languageCommunication -> communication
      for (LanguageCommunication lc : cdaPatient.getLanguageCommunications()) {
        if (lc != null && !lc.isSetNullFlavor()) {
          fhirPatient.addCommunication(transformLanguageCommunication2Communication(lc));
        }
      }

      // patient.guardian -> contact
      for (org.openhealthtools.mdht.uml.cda.Guardian guardian : cdaPatient.getGuardians()) {
        if (guardian != null && !guardian.isSetNullFlavor()) {
          fhirPatient.addContact(transformGuardian2Contact(guardian));
        }
      }

      // extensions

      // patient.raceCode -> extRace
      if (cdaPatient.getRaceCode() != null && !cdaPatient.getRaceCode().isSetNullFlavor()) {
        Extension extRace = new Extension();
        //extRace.setModifier(false);
        extRace.setUrl(Constants.URL_EXTENSION_RACE);
        CD raceCode = cdaPatient.getRaceCode();
        extRace.setValue(dtt.transformCD2CodeableConcept(raceCode));
        fhirPatient.addExtension(extRace);
      }

      // patient.ethnicGroupCode -> extEthnicity
      if (cdaPatient.getEthnicGroupCode() != null 
          && !cdaPatient.getEthnicGroupCode().isSetNullFlavor()) {
        Extension extEthnicity = new Extension();
        //extEthnicity.setModifier(false);
        extEthnicity.setUrl(Constants.URL_EXTENSION_ETHNICITY);
        CD ethnicGroupCode = cdaPatient.getEthnicGroupCode();
        extEthnicity.setValue(dtt.transformCD2CodeableConcept(ethnicGroupCode));
        fhirPatient.addExtension(extEthnicity);
      }

      // patient.religiousAffiliationCode -> extReligion
      if (cdaPatient.getReligiousAffiliationCode() != null
          && !cdaPatient.getReligiousAffiliationCode().isSetNullFlavor()) {
        Extension extReligion = new Extension();
        //extReligion.setModifier(false);
        extReligion.setUrl(Constants.URL_EXTENSION_RELIGION);
        CD religiousAffiliationCode = cdaPatient.getReligiousAffiliationCode();
        extReligion.setValue(dtt.transformCD2CodeableConcept(religiousAffiliationCode));
        fhirPatient.addExtension(extReligion);
      }

      // patient.birthplace.place.addr -> extBirthPlace
      if (cdaPatient.getBirthplace() != null && !cdaPatient.getBirthplace().isSetNullFlavor()
          && cdaPatient.getBirthplace().getPlace() != null 
          && !cdaPatient.getBirthplace().getPlace().isSetNullFlavor()
          && cdaPatient.getBirthplace().getPlace().getAddr() != null
          && !cdaPatient.getBirthplace().getPlace().getAddr().isSetNullFlavor()) {
        Extension extBirthPlace = new Extension();
        //extBirthPlace.setModifier(false);
        extBirthPlace.setUrl(Constants.URL_EXTENSION_BIRTHPLACE);
        extBirthPlace.setValue(
              dtt.transformAD2Address(cdaPatient.getBirthplace().getPlace().getAddr()));
        fhirPatient.addExtension(extBirthPlace);
      }
    }

    return fhirPatientBundle;
  }

  /**
   * Transforms CDA Performer to a FHIR Bundle.
   * @param cdaPerformer2 CDA Performer
   * @return FHIR Bundle
   */
  public Bundle transformPerformer22Practitioner(Performer2 cdaPerformer2) {
    if (cdaPerformer2 == null || cdaPerformer2.isSetNullFlavor()) {
      return null;
    } else {
      return transformAssignedEntity2Practitioner(cdaPerformer2.getAssignedEntity());
    }

  }

  /**
   * Transforms a CDA Problem Concern to a FHIR Condition.
   * @param cdaProblemConcernAct CDA Problem Concern.
   * @return FHIR Condition
   */
  public Bundle transformProblemConcernAct2Condition(ProblemConcernAct cdaProblemConcernAct) {
    if (cdaProblemConcernAct == null || cdaProblemConcernAct.isSetNullFlavor()) {
      return null;
    }

    Bundle fhirConditionBundle = new Bundle();

    // each problem observation instance -> FHIR Condition instance
    for (ProblemObservation cdaProbObs : cdaProblemConcernAct.getProblemObservations()) {
      Bundle fhirProbObsBundle = transformProblemObservation2Condition(cdaProbObs);
      if (fhirProbObsBundle == null) {
        continue;
      }

      for (BundleEntryComponent entry : fhirProbObsBundle.getEntry()) {
        fhirConditionBundle.addEntry(entry);
        if (entry.getResource() instanceof Condition) {
          Condition fhirCond = (Condition) entry.getResource();

          // act/statusCode -> Condition.clinicalStatus
          // NOTE: Problem status template is deprecated in C-CDA Release 2.1; hence
          // status data is not retrieved from this template.
          if (cdaProblemConcernAct.getStatusCode() != null 
              && !cdaProblemConcernAct.getStatusCode().isSetNullFlavor()) {
            fhirCond.setClinicalStatus(
                vst.transformStatusCode2ConditionClinicalStatusCodes(
                      cdaProblemConcernAct.getStatusCode().getCode()));
          }
        }
      }
    }

    return fhirConditionBundle;
  }

  /**
   * Transforms a CDA Problem Observation to a FHIR Condition. 
   * @param cdaProbObs CDA Problem Observation 
   * @return FHIR Condition
   */
  public Bundle transformProblemObservation2Condition(ProblemObservation cdaProbObs) {
    if (cdaProbObs == null || cdaProbObs.isSetNullFlavor()) {
      return null;
    }

    // NOTE: Although DAF requires the mapping for severity, this data is not
    // available on the C-CDA side.
    // NOTE: Problem status template is deprecated in C-CDA Release 2.1; hence
    // status data is not retrieved from this template.

    Bundle fhirConditionBundle = new Bundle();

    Condition fhirCondition = new Condition();
    fhirConditionBundle.addEntry(new BundleEntryComponent().setResource(fhirCondition));

    // resource id
    IdType resourceId = new IdType("Condition", getUniqueId());
    fhirCondition.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirCondition.getMeta().addProfile(Constants.PROFILE_DAF_CONDITION);
    }

    // patient
    fhirCondition.setSubject(getPatientRef());

    // id -> identifier
    for (II id : cdaProbObs.getIds()) {
      if (!id.isSetNullFlavor()) {
        fhirCondition.addIdentifier(dtt.transformII2Identifier(id));
      }
    }

    // code -> category
    if (cdaProbObs.getCode() != null && !cdaProbObs.getCode().isSetNullFlavor()) {
      if (cdaProbObs.getCode().getCode() != null) {
        CodeableConcept conditionCategory = vst
            .transformProblemType2ConditionCategoryCode(cdaProbObs.getCode().getCode());
        if (conditionCategory != null) {
          fhirCondition.addCategory(conditionCategory);
        }
      }
    }

    // value -> code
    if (cdaProbObs.getValues() != null && !cdaProbObs.getValues().isEmpty()) {
      for (ANY value : cdaProbObs.getValues()) {
        if (value != null && !value.isSetNullFlavor()) {
          if (value instanceof CD) {
            fhirCondition.setCode(dtt.transformCD2CodeableConcept((CD) value));
          }
        }
      }
    }

    // onset and abatement
    if (cdaProbObs.getEffectiveTime() != null && !cdaProbObs.getEffectiveTime().isSetNullFlavor()) {

      IVXB_TS low = cdaProbObs.getEffectiveTime().getLow();
      IVXB_TS high = cdaProbObs.getEffectiveTime().getHigh();

      // low -> onset (if doesn't exist, checking value)
      if (low != null && !low.isSetNullFlavor()) {
        fhirCondition.setOnset(dtt.transformTS2DateTime(low));
      } else if (cdaProbObs.getEffectiveTime().getValue() != null
          && !cdaProbObs.getEffectiveTime().getValue().isEmpty()) {
        fhirCondition.setOnset(
              dtt.transformString2DateTime(cdaProbObs.getEffectiveTime().getValue()));
      }

      // high -> abatement
      if (high != null && !high.isSetNullFlavor()) {
        fhirCondition.setAbatement(dtt.transformTS2DateTime(high));
      }
    }

    // author[0] -> asserter
    if (!cdaProbObs.getAuthors().isEmpty()) {
      if (cdaProbObs.getAuthors().get(0) != null 
          && !cdaProbObs.getAuthors().get(0).isSetNullFlavor()) {
        Author author = cdaProbObs.getAuthors().get(0);
        Bundle fhirPractitionerBundle = transformAuthor2Practitioner(author);
        for (BundleEntryComponent entry : fhirPractitionerBundle.getEntry()) {
          fhirConditionBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));
          if (entry.getResource() instanceof Practitioner) {
            fhirCondition.setAsserter(new Reference(entry.getResource().getId()));
          }
        }

        // author.time -> dateRecorded
        if (author.getTime() != null && !author.getTime().isSetNullFlavor()) {
          fhirCondition.setAssertedDate(dtt.transformTS2Date(author.getTime()).getValue());
        }
      }
    }

    // encounter -> encounter
    if (cdaProbObs.getEncounters() != null && !cdaProbObs.getEncounters().isEmpty()) {
      if (cdaProbObs.getEncounters().get(0) != null 
          && cdaProbObs.getEncounters().get(0).isSetNullFlavor()) {
        Bundle fhirEncounterBundle = 
            transformEncounter2Encounter(cdaProbObs.getEncounters().get(0));
        for (BundleEntryComponent entry : fhirEncounterBundle.getEntry()) {
          fhirConditionBundle.addEntry(new BundleEntryComponent().setResource(entry.getResource()));
          if (entry.getResource() instanceof Encounter) {
            fhirCondition.setContext(new Reference(entry.getResource().getId()));
          }
        }
      }
    }

    // NOTE: A default value is assigned to verificationStatus attribute, as it is
    // mandatory but cannot be mapped from the CDA side
    fhirCondition.setVerificationStatus(Config.DEFAULT_CONDITION_VERIFICATION_STATUS);

    return fhirConditionBundle;
  }

  /**
   * Transforms a CDA procedure to a FHIR Procedure.
   * @param cdaProcedure CDA Procedure.
   * @return FHIR Procedure
   */
  public Bundle transformProcedure2Procedure(
        org.openhealthtools.mdht.uml.cda.Procedure cdaProcedure) {
    if (cdaProcedure == null || cdaProcedure.isSetNullFlavor()) {
      return null;
    }

    Procedure fhirProc = new Procedure();
    Bundle fhirProcBundle = new Bundle();
    fhirProcBundle.addEntry(new BundleEntryComponent().setResource(fhirProc));

    // resource id
    IdType resourceId = new IdType("Procedure", getUniqueId());
    fhirProc.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirProc.getMeta().addProfile(Constants.PROFILE_DAF_PROCEDURE);
    }

    // subject
    fhirProc.setSubject(getPatientRef());

    // id -> identifier
    if (cdaProcedure.getIds() != null && !cdaProcedure.getIds().isEmpty()) {
      for (II id : cdaProcedure.getIds()) {
        if (id != null && !id.isSetNullFlavor()) {
          fhirProc.addIdentifier(dtt.transformII2Identifier(id));
        }
      }
    }

    // effectiveTime -> performed
    if (cdaProcedure.getEffectiveTime() != null 
        && !cdaProcedure.getEffectiveTime().isSetNullFlavor()) {
      fhirProc.setPerformed(dtt.transformIvl_TS2Period(cdaProcedure.getEffectiveTime()));
    }

    // targetSiteCode -> bodySite
    if (cdaProcedure.getTargetSiteCodes() != null && !cdaProcedure.getTargetSiteCodes().isEmpty()) {
      for (CD cd : cdaProcedure.getTargetSiteCodes()) {
        if (cd != null && !cd.isSetNullFlavor()) {
          fhirProc.addBodySite(dtt.transformCD2CodeableConcept(cd));
        }
      }
    }

    // performer -> performer
    for (Performer2 performer : cdaProcedure.getPerformers()) {
      if (performer.getAssignedEntity() != null 
          && !performer.getAssignedEntity().isSetNullFlavor()) {
        Bundle practBundle = transformPerformer22Practitioner(performer);
        for (BundleEntryComponent entry : practBundle.getEntry()) {
          // Add all the resources returned from the bundle to the main bundle
          fhirProcBundle.addEntry(entry);
          // Add a reference to performer attribute only for Practitioner resource.
          // Further resources can include Organization.
          if (entry.getResource() instanceof Practitioner) {
            ProcedurePerformerComponent fhirPerformer = new ProcedurePerformerComponent();
            fhirPerformer.setActor(new Reference(entry.getResource().getId()));
            fhirProc.addPerformer(fhirPerformer);
          }
        }
      }
    }

    // statusCode -> status
    if (cdaProcedure.getStatusCode() != null && !cdaProcedure.getStatusCode().isSetNullFlavor()
        && cdaProcedure.getStatusCode().getCode() != null) {
      ProcedureStatus status = 
          vst.transformStatusCode2ProcedureStatus(cdaProcedure.getStatusCode().getCode());
      if (status != null) {
        fhirProc.setStatus(status);
      }
    }

    // code -> code
    if (cdaProcedure.getCode() != null && !cdaProcedure.getCode().isSetNullFlavor()) {
      fhirProc.setCode(dtt.transformCD2CodeableConcept(cdaProcedure.getCode()));
    }

    // encounter[0] -> encounter
    if (!cdaProcedure.getEncounters().isEmpty()) {
      org.openhealthtools.mdht.uml.cda.Encounter cdaEncounter = cdaProcedure.getEncounters().get(0);
      if (cdaEncounter != null && !cdaEncounter.isSetNullFlavor()) {
        Bundle encBundle = transformEncounter2Encounter(cdaEncounter);
        for (BundleEntryComponent entry : encBundle.getEntry()) {
          fhirProcBundle.addEntry(entry);
          if (entry.getResource() instanceof Encounter) {
            fhirProc.setContext(new Reference(entry.getResource().getId()));
          }
        }
      }
    }

    return fhirProcBundle;
  }

  /**
   * Transforms CDA Reaction Observation to FHIR Observation.
   * @param cdaReactionObservation CDA Reaction Observation
   * @return FHIR Observation
   */
  public Bundle transformReactionObservation2Observation(
        ReactionObservation cdaReactionObservation) {
    return transformObservation2Observation(cdaReactionObservation);
  }

  /**
   * Transforms CDA Reference Range to FHIR Observation Reference Range.
   * @param cdaReferenceRange CDA Reference Range
   * @return FHIR Observation Reference Range.
   */
  public ObservationReferenceRangeComponent transformReferenceRange2ReferenceRange(
      ReferenceRange cdaReferenceRange) {
    if (cdaReferenceRange == null || cdaReferenceRange.isSetNullFlavor()) {
      return null;
    }

    ObservationReferenceRangeComponent fhirRefRange = new ObservationReferenceRangeComponent();

    // Notice that we get all the desired information from
    // cdaRefRange.ObservationRange
    // We may think of transforming ObservationRange instead of ReferenceRange
    if (cdaReferenceRange.getObservationRange() != null && !cdaReferenceRange.isSetNullFlavor()) {

      // low - high
      if (cdaReferenceRange.getObservationRange().getValue() != null
          && !cdaReferenceRange.getObservationRange().getValue().isSetNullFlavor()) {
        if (cdaReferenceRange.getObservationRange().getValue() instanceof IVL_PQ) {
          IVL_PQ cdaRefRangeValue = ((IVL_PQ) cdaReferenceRange.getObservationRange().getValue());
          // low
          if (cdaRefRangeValue.getLow() != null && !cdaRefRangeValue.getLow().isSetNullFlavor()) {
            fhirRefRange.setLow(dtt.transformPQ2SimpleQuantity(cdaRefRangeValue.getLow()));
          }
          // high
          if (cdaRefRangeValue.getHigh() != null && !cdaRefRangeValue.getHigh().isSetNullFlavor()) {
            fhirRefRange.setHigh(dtt.transformPQ2SimpleQuantity(cdaRefRangeValue.getHigh()));
          }
        }
      }

      // observationRange.interpretationCode -> meaning
      if (cdaReferenceRange.getObservationRange().getInterpretationCode() != null
          && !cdaReferenceRange.getObservationRange().getInterpretationCode().isSetNullFlavor()) {
        fhirRefRange.setType(
            dtt.transformCD2CodeableConcept(
              cdaReferenceRange.getObservationRange().getInterpretationCode()));
      }

      // text.text -> text
      if (cdaReferenceRange.getObservationRange().getText() != null
          && !cdaReferenceRange.getObservationRange().getText().isSetNullFlavor()) {
        if (cdaReferenceRange.getObservationRange().getText().getText() != null
            && !cdaReferenceRange.getObservationRange().getText().getText().isEmpty()) {
          fhirRefRange.setText(cdaReferenceRange.getObservationRange().getText().getText());
        }
      }
    }

    return fhirRefRange;
  }

  /**
   * Transforms a CDA Result Observation to the FHIR Bundle.
   * @param cdaResultObservation CDA Result Observation
   * @return FHIR Bundle
   */
  public Bundle transformResultObservation2Observation(ResultObservation cdaResultObservation) {
    Bundle fhirObservationBundle = transformObservation2Observation(cdaResultObservation);
    if (fhirObservationBundle == null) {
      return null;
    }

    // finding the observation resource and setting its meta.profile to result
    // observation's profile url
    if (Config.isGenerateDafProfileMetadata()) {
      for (BundleEntryComponent entry : fhirObservationBundle.getEntry()) {
        if (entry.getResource() instanceof Observation) {
          (entry.getResource()).getMeta().addProfile(Constants.PROFILE_DAF_RESULT_OBS);
        }
      }
    }

    return fhirObservationBundle;
  }

  /**
   * Transforms a Result Organizer to a Diagnostic Report.
   * @param cdaResultOrganizer CDA Result Organizer
   * @return FHIR Diagnostic Report
   */
  public Bundle transformResultOrganizer2DiagnosticReport(ResultOrganizer cdaResultOrganizer) {
    if (cdaResultOrganizer == null || cdaResultOrganizer.isSetNullFlavor()) {
      return null;
    }

    DiagnosticReport fhirDiagReport = new DiagnosticReport();

    // bundle
    Bundle fhirDiagReportBundle = new Bundle();
    fhirDiagReportBundle.addEntry(new BundleEntryComponent().setResource(fhirDiagReport));

    // resource id
    IdType resourceId = new IdType("DiagnosticReport", getUniqueId());
    fhirDiagReport.setId(resourceId);

    // meta.profile
    if (Config.isGenerateDafProfileMetadata()) {
      fhirDiagReport.getMeta().addProfile(Constants.PROFILE_DAF_DIAGNOSTIC_REPORT);
    }

    // subject
    fhirDiagReport.setSubject(getPatientRef());

    // Although DiagnosticReport.request(DiagnosticOrder) is needed by daf, no
    // information exists in CDA side to fill that field.

    // id -> identifier
    if (cdaResultOrganizer.getIds() != null && !cdaResultOrganizer.getIds().isEmpty()) {
      for (II ii : cdaResultOrganizer.getIds()) {
        if (ii != null && !ii.isSetNullFlavor()) {
          fhirDiagReport.addIdentifier(dtt.transformII2Identifier(ii));
        }
      }
    }

    // code -> code
    if (cdaResultOrganizer.getCode() != null && !cdaResultOrganizer.getCode().isSetNullFlavor()) {
      fhirDiagReport.setCode(dtt.transformCD2CodeableConcept(cdaResultOrganizer.getCode()));
    }

    // statusCode -> status
    if (cdaResultOrganizer.getStatusCode() != null && !cdaResultOrganizer.isSetNullFlavor()) {
      fhirDiagReport.setStatus(
          vst.transformResultOrganizerStatusCode2DiagnosticReportStatus(
              cdaResultOrganizer.getStatusCode().getCode()));
    }

    // effectiveTime -> effective
    if (cdaResultOrganizer.getEffectiveTime() != null 
        && !cdaResultOrganizer.getEffectiveTime().isSetNullFlavor()) {
      fhirDiagReport.setEffective(
            dtt.transformIvl_TS2Period(cdaResultOrganizer.getEffectiveTime()));
    }

    // author.time -> issued
    if (cdaResultOrganizer.getAuthors() != null && !cdaResultOrganizer.getAuthors().isEmpty()) {
      for (org.openhealthtools.mdht.uml.cda.Author author : cdaResultOrganizer.getAuthors()) {
        if (author != null && !author.isSetNullFlavor()) {
          if (author.getTime() != null && !author.getTime().isSetNullFlavor()) {
            fhirDiagReport.setIssued(dtt.transformTS2Instant(author.getTime()).getValue());
          }
        }
      }
    }

    // if DiagnosticReport.issued is not set, set the highest value of the
    // effectiveTime to DiagnosticReport.issued
    // effectiveTime.high, low or value -> issued
    if (fhirDiagReport.getIssued() == null) {
      if (cdaResultOrganizer.getEffectiveTime() != null 
          && !cdaResultOrganizer.getEffectiveTime().isSetNullFlavor()) {
        if (cdaResultOrganizer.getEffectiveTime().getHigh() != null
            && !cdaResultOrganizer.getEffectiveTime().getHigh().isSetNullFlavor()) {
          // effectiveTime.high -> issued
          fhirDiagReport.setIssued(
                dtt.transformTS2Instant(
                  cdaResultOrganizer.getEffectiveTime().getHigh()).getValue());
        } else if (cdaResultOrganizer.getEffectiveTime().getLow() != null
            && !cdaResultOrganizer.getEffectiveTime().getLow().isSetNullFlavor()) {
          // effectiveTime.low -> issued
          fhirDiagReport.setIssued(
                dtt.transformTS2Instant(cdaResultOrganizer.getEffectiveTime().getLow()).getValue());
        } else if (cdaResultOrganizer.getEffectiveTime().getValue() != null) {
          // effectiveTime.value -> issued
          TS ts = DatatypesFactory.eINSTANCE.createTS();
          ts.setValue(cdaResultOrganizer.getEffectiveTime().getValue());
          fhirDiagReport.setIssued(dtt.transformTS2Instant(ts).getValue());
        }
      }
    }

    // author -> performer
    if (cdaResultOrganizer.getAuthors() != null && !cdaResultOrganizer.getAuthors().isEmpty()) {
      for (org.openhealthtools.mdht.uml.cda.Author author : cdaResultOrganizer.getAuthors()) {
        // Asserting that at most one author exists
        if (author != null && !author.isSetNullFlavor()) {
          Bundle fhirPractitionerBundle = transformAuthor2Practitioner(author);
          for (BundleEntryComponent entry : fhirPractitionerBundle.getEntry()) {
            fhirDiagReportBundle.addEntry(entry);
            if (entry.getResource() instanceof Practitioner) {
              DiagnosticReportPerformerComponent component = 
                  new DiagnosticReportPerformerComponent();
              component.setActor(new Reference(entry.getResource().getId()));              
              fhirDiagReport.addPerformer(component);
            }
          }
        }
      }
    } else {
      // if there is no information about the performer in CDA side, assign an empty
      // Practitioner resource
      // which has data absent reason: unknown
      Practitioner fhirPerformerDataAbsent = new Practitioner();
      fhirPerformerDataAbsent.setId(new IdType("Practitioner", getUniqueId()));
      Extension extDataAbsentReason = new Extension();

      // meta.profile
      if (Config.isGenerateDafProfileMetadata()) {
        fhirPerformerDataAbsent.getMeta().addProfile(Constants.PROFILE_DAF_PRACTITIONER);
      }

      // setting dataAbsentReason extension
      //extDataAbsentReason.setModifier(false);
      extDataAbsentReason.setUrl(Constants.URL_EXTENSION_DATA_ABSENT_REASON);
      extDataAbsentReason.setValue(
            Config.DEFAULT_DIAGNOSTICREPORT_PERFORMER_DATA_ABSENT_REASON_CODE);

      // adding dataAbsentReason as undeclaredExtension to fhirPerformer
      fhirPerformerDataAbsent.addExtension(extDataAbsentReason);

      // setting the performer of DiagnosticReport
      fhirDiagReportBundle.addEntry(
            new BundleEntryComponent().setResource(fhirPerformerDataAbsent));
      DiagnosticReportPerformerComponent component = 
            new DiagnosticReportPerformerComponent();
      component.setActor(new Reference(fhirPerformerDataAbsent.getId()));
      fhirDiagReport.addPerformer(component);
    }

    // ResultObservation -> result
    for (ResultObservation cdaResultObs : cdaResultOrganizer.getResultObservations()) {
      if (!cdaResultObs.isSetNullFlavor()) {
        Bundle fhirObsBundle = transformResultObservation2Observation(cdaResultObs);
        for (BundleEntryComponent entry : fhirObsBundle.getEntry()) {
          fhirDiagReportBundle.addEntry(entry);
          if (entry.getResource() instanceof Observation) {
            fhirDiagReport.addResult().setReference(entry.getResource().getId());
          }
        }
      }
    }

    return fhirDiagReportBundle;
  }

  /**
   * Transforms a CDA Section to a Composition Section Component.
   */
  public SectionComponent transformSection2Section(Section cdaSection) {
    if (cdaSection == null || cdaSection.isSetNullFlavor()) {
      return null;
    }

    SectionComponent fhirSec = new SectionComponent();

    // title -> title.text
    if (cdaSection.getTitle() != null && !cdaSection.getTitle().isSetNullFlavor()) {
      if (cdaSection.getTitle().getText() != null && !cdaSection.getTitle().getText().isEmpty()) {
        fhirSec.setTitle(cdaSection.getTitle().getText());
      }
    }

    // code -> code
    if (cdaSection.getCode() != null && !cdaSection.getCode().isSetNullFlavor()) {
      fhirSec.setCode(dtt.transformCD2CodeableConcept(cdaSection.getCode()));
    }

    // text -> text
    if (cdaSection.getText() != null) {
      Narrative fhirText = dtt.transformStrucDocText2Narrative(cdaSection.getText());
      if (fhirText != null) {
        fhirSec.setText(fhirText);
      }
    }

    return fhirSec;
  }

  /**
   * Transforms a Service Delivery Location to a FHIR Location.
   * @param cdaSdloc CDA Service Delivery Location
   * @return FHIR Location
   */
  public Location transformServiceDeliveryLocation2Location(
        ServiceDeliveryLocation cdaSdloc) {
    /*
     * ServiceDeliveryLocation is a ParticipantRole instance with a specific
     * templateId Therefore, tParticipantRole2Location should satisfy the necessary
     * mapping
     */
    return transformParticipantRole2Location(cdaSdloc);
  }

  /**
   * Transforms a Supply to a Fhir Device. 
   * @param cdaSupply CDA Supply
   * @return FHIR Device
   */
  public Device transformSupply2Device(Supply cdaSupply) {
    if (cdaSupply == null || cdaSupply.isSetNullFlavor()) {
      return null;
    }

    ProductInstance productInstance = null;
    // getting productInstance from cdaSupply.participant.participantRole
    if (cdaSupply.getParticipants() != null && !cdaSupply.getParticipants().isEmpty()) {
      for (Participant2 participant : cdaSupply.getParticipants()) {
        if (participant != null 
            && !participant.isSetNullFlavor()) {
          if (participant.getParticipantRole() != null 
              && !participant.getParticipantRole().isSetNullFlavor()) {
            if (participant.getParticipantRole() instanceof ProductInstance) {
              productInstance = (ProductInstance) participant.getParticipantRole();
            }
          }
        }
      }
    }

    if (productInstance == null) {
      return null;
    }

    Device fhirDev = new Device();

    // resource id
    IdType resourceId = new IdType("Device", getUniqueId());
    fhirDev.setId(resourceId);

    // patient
    fhirDev.setPatient(getPatientRef());

    // productInstance.id -> identifier
    for (II id : productInstance.getIds()) {
      if (!id.isSetNullFlavor()) {
        fhirDev.addIdentifier(dtt.transformII2Identifier(id));
      }
    }

    // productInstance.playingDevice.code -> type
    if (productInstance.getPlayingDevice() != null 
        && !productInstance.getPlayingDevice().isSetNullFlavor()) {
      if (productInstance.getPlayingDevice().getCode() != null
          && !productInstance.getPlayingDevice().getCode().isSetNullFlavor()) {
        fhirDev.setType(
              dtt.transformCD2CodeableConcept(productInstance.getPlayingDevice().getCode()));
      }
    }

    return fhirDev;
  }

  /**
   * Transforms a Vital Signs observation to a FHIR Bundle.
   * @param cdaVitalSignObservation CDA Vital Signs Observation
   * @return FHIR Bundle
   */
  public Bundle transformVitalSignObservation2Observation(
        VitalSignObservation cdaVitalSignObservation) {
    Bundle fhirObservationBundle = transformObservation2Observation(cdaVitalSignObservation);
    if (fhirObservationBundle == null) {
      return null;
    }

    // finding the observation resource and setting its meta.profile to result
    // observation's profile url
    if (Config.isGenerateDafProfileMetadata()) {
      for (BundleEntryComponent entry : fhirObservationBundle.getEntry()) {
        if (entry.getResource() instanceof Observation) {
          (entry.getResource()).getMeta().addProfile(Constants.PROFILE_DAF_VITAL_SIGNS);
        }
      }
    }

    return fhirObservationBundle;
  }

}