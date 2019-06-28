package tr.com.srdc.cda2fhir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Composition.SectionComponent;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.FamilyMemberHistory;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Patient.ContactComponent;
import org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.dstu3.model.Resource;


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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openhealthtools.mdht.uml.cda.Guardian;
import org.openhealthtools.mdht.uml.cda.LanguageCommunication;
import org.openhealthtools.mdht.uml.cda.ManufacturedProduct;
import org.openhealthtools.mdht.uml.cda.Organizer;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.Performer2;
import org.openhealthtools.mdht.uml.cda.Section;

import org.openhealthtools.mdht.uml.cda.consol.AllergyProblemAct;
import org.openhealthtools.mdht.uml.cda.consol.ContinuityOfCareDocument;
import org.openhealthtools.mdht.uml.cda.consol.EncounterActivities;
import org.openhealthtools.mdht.uml.cda.consol.FamilyHistoryOrganizer;
import org.openhealthtools.mdht.uml.cda.consol.FunctionalStatusResultOrganizer;
import org.openhealthtools.mdht.uml.cda.consol.FunctionalStatusSection;
import org.openhealthtools.mdht.uml.cda.consol.ImmunizationActivity;
import org.openhealthtools.mdht.uml.cda.consol.ImmunizationsSectionEntriesOptional;

import org.openhealthtools.mdht.uml.cda.consol.MedicationActivity;
import org.openhealthtools.mdht.uml.cda.consol.MedicationsSection;
import org.openhealthtools.mdht.uml.cda.consol.ProblemConcernAct;
import org.openhealthtools.mdht.uml.cda.consol.ResultOrganizer;
import org.openhealthtools.mdht.uml.cda.consol.ResultsSection;
import org.openhealthtools.mdht.uml.cda.consol.SocialHistorySection;
import org.openhealthtools.mdht.uml.cda.consol.VitalSignObservation;
import org.openhealthtools.mdht.uml.cda.consol.VitalSignsOrganizer;
import org.openhealthtools.mdht.uml.cda.consol.VitalSignsSectionEntriesOptional;

import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.hl7.datatypes.EN;
import org.openhealthtools.mdht.uml.hl7.datatypes.ENXP;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;

//import tr.com.srdc.cda2fhir.transform.DataTypesTransformerImpl;
import tr.com.srdc.cda2fhir.transform.ResourceTransformerImpl;
import tr.com.srdc.cda2fhir.transform.ValueSetsTransformerImpl;
import tr.com.srdc.cda2fhir.util.FhirUtil;

public class ResourceTransformerTest {

  private static final ResourceTransformerImpl rt = new ResourceTransformerImpl();
  //private static final DataTypesTransformerImpl dtt = new DataTypesTransformerImpl();
  private static final ValueSetsTransformerImpl vsti = new ValueSetsTransformerImpl();
  private static FileInputStream fisCCD;
  private static FileWriter resultFW;
  private static ContinuityOfCareDocument ccd;
  private static final String resultFilePath = 
      "src/test/resources/output/ResourceTransformerTest.txt";
  private static final String transformationStartMsg = "\n# TRANSFORMATION STARTING..\n";
  private static final String transformationEndMsg = "# END OF TRANSFORMATION.\n";
  private static final String endOfTestMsg = "\n## END OF TEST\n";

  /**
   * Test Class Initialization Method.
   */
  @BeforeClass
  public static void init() {
    CDAUtil.loadPackages();

    // read the input test file
    try {
      fisCCD = new FileInputStream("src/test/resources/C-CDA_R2-1_CCD.xml");
      ccd = (ContinuityOfCareDocument) CDAUtil.load(fisCCD);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    // init the output file writer
    File resultFile = new File(resultFilePath);
    resultFile.getParentFile().mkdirs();
    try {
      resultFW = new FileWriter(resultFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Class Cleanup Method.
   */
  @AfterClass
  public static void finalise() {
    try {
      resultFW.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Most of the test methods just print the transformed object in JSON form.

  @Test
  public void testAllergyProblemAct2AllergyIntolerance() {

    appendToResultFile("## TEST: AllergyProblemAct2AllergyIntolerance\n");
    // null instance test
    AllergyProblemAct cdaNull = null;
    Bundle fhirNull = rt.transformAllergyProblemAct2AllergyIntolerance(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    for (AllergyProblemAct cdaApa : ResourceTransformerTest.ccd.getAllergiesSection()
        .getAllergyProblemActs()) {
      appendToResultFile(transformationStartMsg);
      Bundle allergyBundle = rt.transformAllergyProblemAct2AllergyIntolerance(cdaApa);
      appendToResultFile(transformationEndMsg);
      appendToResultFile(allergyBundle);
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testAssignedAuthor2Practitioner() {

    appendToResultFile("## TEST: AssignedAuthor2Practitioner\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.AssignedAuthor cdaNull = null;
    Bundle fhirNull = rt.transformAssignedAuthor2Practitioner(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getAuthors() != null) {
      for (org.openhealthtools.mdht.uml.cda.Author author : ResourceTransformerTest
          .ccd.getAuthors()) {
        // traversing authors
        if (author != null && author.getAssignedAuthor() != null) {
          appendToResultFile(transformationStartMsg);
          Bundle practitionerBundle = 
              rt.transformAssignedAuthor2Practitioner(author.getAssignedAuthor());
          appendToResultFile(transformationEndMsg);
          appendToResultFile(practitionerBundle);
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testAssignedEntity2Practitioner() {

    appendToResultFile("## TEST: AssignedEntity2Practitioner\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.AssignedEntity cdaNull = null;
    Bundle fhirNull = rt.transformAssignedEntity2Practitioner(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getProceduresSection() != null 
        && !ResourceTransformerTest.ccd.getProceduresSection().isSetNullFlavor()) {
      if (ResourceTransformerTest.ccd.getProceduresSection().getProcedures() != null
          && !ResourceTransformerTest.ccd.getProceduresSection().getProcedures().isEmpty()) {
        for (org.openhealthtools.mdht.uml.cda.Procedure procedure : 
            ResourceTransformerTest.ccd.getProceduresSection().getProcedures()) {
          // traversing procedures
          if (procedure.getPerformers() != null && !procedure.getPerformers().isEmpty()) {
            for (Performer2 performer : procedure.getPerformers()) {
              if (performer.getAssignedEntity() != null 
                  && !performer.getAssignedEntity().isSetNullFlavor()) {
                appendToResultFile(transformationStartMsg);
                Bundle fhirPractitionerBundle = 
                    rt.transformAssignedEntity2Practitioner(performer.getAssignedEntity());
                appendToResultFile(transformationEndMsg);
                appendToResultFile(fhirPractitionerBundle);
              }
            }
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testClinicalDocument2Composition() {
    
    appendToResultFile("## TEST: ClinicalDocument2Composition\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.consol.ContinuityOfCareDocument cdaNull = null;
    Bundle fhirNull = rt.transformClinicalDocument2Composition(cdaNull);
    Assert.assertNull(fhirNull);

    // instance from file
    if (ResourceTransformerTest.ccd != null 
        && !ResourceTransformerTest.ccd.isSetNullFlavor()) {
      appendToResultFile(transformationStartMsg);
      Bundle fhirComp = rt.transformClinicalDocument2Composition(ResourceTransformerTest.ccd);
      appendToResultFile(transformationEndMsg);
      appendToResultFile(fhirComp);
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testEncounterActivity2Encounter() {
    
    appendToResultFile("## TEST: EncounterActivity2Encounter\n");
    // null instance test
    EncounterActivities cdaNull = null;
    Bundle fhirNull = rt.transformEncounterActivity2Encounter(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getEncountersSection() != null 
        && !ResourceTransformerTest.ccd.getEncountersSection().isSetNullFlavor()) {
      if (ResourceTransformerTest.ccd.getEncountersSection().getEncounterActivitiess() != null
          && !ResourceTransformerTest.ccd.getEncountersSection()
              .getEncounterActivitiess().isEmpty()) {
        for (EncounterActivities encounterActivity : ResourceTransformerTest.ccd
            .getEncountersSection().getEncounterActivitiess()) {
          if (encounterActivity != null && !encounterActivity.isSetNullFlavor()) {
            appendToResultFile(transformationStartMsg);
            Bundle fhirEncounterBundle = rt.transformEncounterActivity2Encounter(encounterActivity);
            appendToResultFile(transformationEndMsg);
            appendToResultFile(fhirEncounterBundle);
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testFamilyHistoryOrganizer2FamilyMemberHistory() {

    appendToResultFile("## TEST: FamilyHistoryOrganizer2FamilyMemberHistory\n");
    // null instance test
    FamilyHistoryOrganizer cdaNull = null;
    FamilyMemberHistory fhirNull = rt.transformFamilyHistoryOrganizer2FamilyMemberHistory(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getFamilyHistorySection() != null 
        && ResourceTransformerTest.ccd.getFamilyHistorySection().getFamilyHistories() != null) {
      for (FamilyHistoryOrganizer familyHistoryOrganizer : ResourceTransformerTest.ccd
          .getFamilyHistorySection().getFamilyHistories()) {
        if (familyHistoryOrganizer != null) {
          appendToResultFile(transformationStartMsg);
          FamilyMemberHistory fmHistory = 
              rt.transformFamilyHistoryOrganizer2FamilyMemberHistory(familyHistoryOrganizer);
          appendToResultFile(transformationEndMsg);
          appendToResultFile(fmHistory);
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testFunctionalStatus2Observation() {
    appendToResultFile("## TEST: FunctionalStatus2Observation\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.Observation cdaNull = null;
    Bundle fhirNull = rt.transformFunctionalStatus2Observation(cdaNull);
    Assert.assertNull(fhirNull);

    // instance from file
    FunctionalStatusSection funcStatSec = ResourceTransformerTest.ccd.getFunctionalStatusSection();

    if (funcStatSec != null && !funcStatSec.isSetNullFlavor()) {
      if (funcStatSec.getOrganizers() != null && !funcStatSec.getOrganizers().isEmpty()) {
        for (Organizer funcStatOrg : funcStatSec.getOrganizers()) {
          if (funcStatOrg != null && !funcStatOrg.isSetNullFlavor()) {
            if (funcStatOrg instanceof FunctionalStatusResultOrganizer) {
              if (((FunctionalStatusResultOrganizer) funcStatOrg).getObservations() != null
                  && !((FunctionalStatusResultOrganizer) funcStatOrg).getObservations().isEmpty()) {
                for (org.openhealthtools.mdht.uml.cda.Observation cdaObs : 
                    ((FunctionalStatusResultOrganizer) funcStatOrg)
                    .getObservations()) {
                  appendToResultFile(transformationStartMsg);
                  Bundle fhirObs = rt.transformFunctionalStatus2Observation(cdaObs);
                  appendToResultFile(transformationEndMsg);
                  appendToResultFile(fhirObs);
                }
              }
            }
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testGuardian2Contact() {

    appendToResultFile("## TEST: Guardian2Contact\n");
    // null instance test
    Guardian cdaNull = null;
    ContactComponent fhirNull = rt.transformGuardian2Contact(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getPatientRoles() != null 
        && !ResourceTransformerTest.ccd.getPatientRoles().isEmpty()) {
      for (PatientRole patientRole : ResourceTransformerTest.ccd.getPatientRoles()) {
        if (patientRole != null 
            && !patientRole.isSetNullFlavor() 
            && patientRole.getPatient() != null
            && !patientRole.getPatient().isSetNullFlavor()) {
          for (Guardian guardian : patientRole.getPatient().getGuardians()) {
            if (guardian != null && !guardian.isSetNullFlavor()) {
              appendToResultFile(transformationStartMsg);
              ContactComponent contact = rt.transformGuardian2Contact(guardian);
              appendToResultFile(transformationEndMsg);
              Patient patient = new Patient().addContact(contact);
              appendToResultFile(patient);
            }
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testImmunizationActivity2Immunization() {

    appendToResultFile("## TEST: ImmunizationActivity2Immunization\n");
    // null instance test
    ImmunizationActivity cdaNull = null;
    Bundle fhirNull = rt.transformImmunizationActivity2Immunization(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    ImmunizationsSectionEntriesOptional immSec = 
        ResourceTransformerTest.ccd.getImmunizationsSectionEntriesOptional();

    if (immSec != null && !immSec.isSetNullFlavor()) {
      for (ImmunizationActivity immAct : immSec.getImmunizationActivities()) {
        if (immAct != null && !immAct.isSetNullFlavor()) {
          appendToResultFile(transformationStartMsg);
          Bundle fhirImm = rt.transformImmunizationActivity2Immunization(immAct);
          appendToResultFile(transformationEndMsg);
          appendToResultFile(fhirImm);
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testLanguageCommunication2Communication() {

    appendToResultFile("## TEST: LanguageCommunication2Communication\n");
    // null instance test
    LanguageCommunication cdaNull = null;
    PatientCommunicationComponent fhirNull = 
        rt.transformLanguageCommunication2Communication(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    for (org.openhealthtools.mdht.uml.cda.Patient patient : ResourceTransformerTest
        .ccd.getPatients()) {
      for (LanguageCommunication lc : patient.getLanguageCommunications()) {
        appendToResultFile(transformationStartMsg);
        PatientCommunicationComponent fhirCommunication = rt
            .transformLanguageCommunication2Communication(lc);
        appendToResultFile(transformationEndMsg);
        Patient fhirPatient = new Patient();
        fhirPatient.addCommunication(fhirCommunication);
        appendToResultFile(fhirPatient);
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testManufacturedProduct2Medication() {

    appendToResultFile("## TEST: ManufacturedProduct2Medication\n");
    // null instance test
    ManufacturedProduct cdaNull = null;
    Bundle fhirNull = rt.transformManufacturedProduct2Medication(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    ImmunizationsSectionEntriesOptional immSection = 
        ResourceTransformerTest.ccd.getImmunizationsSectionEntriesOptional();
    if (immSection != null && !immSection.isSetNullFlavor()) {
      if (immSection.getImmunizationActivities() != null 
          && !immSection.getImmunizationActivities().isEmpty()) {
        for (ImmunizationActivity immAct : immSection.getImmunizationActivities()) {
          if (immAct != null && !immAct.isSetNullFlavor()) {
            if (immAct.getConsumable() != null && !immAct.getConsumable().isSetNullFlavor()) {
              if (immAct.getConsumable().getManufacturedProduct() != null
                  && !immAct.getConsumable().getManufacturedProduct().isSetNullFlavor()) {
                // immAct.immSection.immAct.consumable.manuProd
                appendToResultFile(transformationStartMsg);
                Bundle fhirMed = 
                    rt.transformManufacturedProduct2Medication(
                          immAct.getConsumable().getManufacturedProduct());
                appendToResultFile(transformationEndMsg);
                appendToResultFile(fhirMed);
              }
            }
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testMedicationActivity2MedicationStatement() {

    appendToResultFile("## TEST: MedicationActivity2MedicationStatement\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.consol.MedicationActivity cdaNull = null;
    Bundle fhirNull = rt.transformMedicationActivity2MedicationStatement(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getMedicationsSection() != null 
        && !ResourceTransformerTest.ccd.getMedicationsSection().isSetNullFlavor()) {
      if (ResourceTransformerTest.ccd.getMedicationsSection().getMedicationActivities() != null
          && !ResourceTransformerTest.ccd.getMedicationsSection()
              .getMedicationActivities().isEmpty()) {
        for (MedicationActivity cdaMedAct : ResourceTransformerTest
              .ccd
              .getMedicationsSection().getMedicationActivities()) {
          if (cdaMedAct != null && !cdaMedAct.isSetNullFlavor()) {
            appendToResultFile(transformationStartMsg);
            Bundle fhirMedStBundle = rt.transformMedicationActivity2MedicationStatement(cdaMedAct);
            appendToResultFile(transformationEndMsg);
            appendToResultFile(fhirMedStBundle);
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testMedicationDispense2MedicationDispense() {

    appendToResultFile("## TEST: MedicationDispense2MedicationDispense\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.consol.MedicationDispense cdaNull = null;
    Bundle fhirNull = rt.transformMedicationDispense2MedicationDispense(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    // medicationsSection.medicationActivities.medicationDispense
    if (ResourceTransformerTest.ccd.getMedicationsSection() != null 
        && !ResourceTransformerTest.ccd.getMedicationsSection().isSetNullFlavor()) {
      MedicationsSection medSec = ResourceTransformerTest.ccd.getMedicationsSection();
      if (medSec.getMedicationActivities() != null && !medSec.getMedicationActivities().isEmpty()) {
        for (MedicationActivity medAct : medSec.getMedicationActivities()) {
          if (medAct != null && !medAct.isSetNullFlavor()) {
            if (medAct.getMedicationDispenses() != null 
                && !medAct.getMedicationDispenses().isEmpty()) {
              for (org.openhealthtools.mdht.uml.cda.consol.MedicationDispense medDisp : medAct
                  .getMedicationDispenses()) {
                if (medDisp != null && !medDisp.isSetNullFlavor()) {
                  appendToResultFile(transformationStartMsg);
                  Bundle fhirMedDispBundle = 
                      rt.transformMedicationDispense2MedicationDispense(medDisp);
                  appendToResultFile(transformationEndMsg);
                  appendToResultFile(fhirMedDispBundle);
                }
              }
            }
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testObservation2Observation() {

    appendToResultFile("## TEST: Observation2Observation\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.Observation cdaNull = null;
    Bundle fhirNull = rt.transformObservation2Observation(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getSocialHistorySection() != null 
        && !ResourceTransformerTest.ccd.getSocialHistorySection().isSetNullFlavor()) {
      if (ResourceTransformerTest.ccd.getSocialHistorySection().getObservations() != null
          && !ResourceTransformerTest.ccd.getSocialHistorySection().getObservations().isEmpty()) {
        for (org.openhealthtools.mdht.uml.cda.Observation cdaObs : ResourceTransformerTest
            .ccd
            .getSocialHistorySection()
            .getObservations()) {
          if (cdaObs != null && !cdaObs.isSetNullFlavor()) {
            appendToResultFile(transformationStartMsg);
            Bundle obsBundle = rt.transformObservation2Observation(cdaObs);
            appendToResultFile(transformationEndMsg);
            appendToResultFile(obsBundle);
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testOrganization2Organization() {

    appendToResultFile("## TEST: Organization2Organization\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.Organization cdaNull = null;
    Organization fhirNull = rt.transformOrganization2Organization(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    for (org.openhealthtools.mdht.uml.cda.PatientRole patRole : ResourceTransformerTest
        .ccd.getPatientRoles()) {
      org.openhealthtools.mdht.uml.cda.Organization cdaOrg = patRole.getProviderOrganization();
      appendToResultFile(transformationStartMsg);
      Organization fhirOrg = rt.transformOrganization2Organization(cdaOrg);
      appendToResultFile(transformationEndMsg);
      appendToResultFile(fhirOrg);
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testPatientRole2Patient() {

    appendToResultFile("## TEST: PatientRole2Patient\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.PatientRole cdaNull = null;
    Bundle fhirNull = rt.transformPatientRole2Patient(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    for (PatientRole pr : ResourceTransformerTest.ccd.getPatientRoles()) {

      // here we do the transformation by calling the method rt.PatientRole2Patient

      Patient patient = null;

      appendToResultFile(transformationStartMsg);
      Bundle patientBundle = rt.transformPatientRole2Patient(pr);
      appendToResultFile(transformationEndMsg);
      appendToResultFile(patientBundle);

      for (BundleEntryComponent entry : patientBundle.getEntry()) {
        if (entry.getResource() instanceof Patient) {
          patient = (Patient) entry.getResource();
        }
      }

      // patient.identifier
      int idCount = 0;
      for (II id : pr.getIds()) {
        if (id.getRoot() != null && id.getExtension() != null) {
          // since extension may contain "urn:oid:" or "urn:uuid:", assertion is about
          // containing the value as a piece
          Assert.assertTrue("pr.id.extension #" + idCount + " was not transformed",
              patient.getIdentifier().get(idCount).getValue().contains(id.getExtension()));
          Assert.assertTrue("pr.id.root #" + idCount + " was not transformed",
              patient.getIdentifier().get(idCount).getSystem().contains(id.getRoot()));
        } else if (id.getRoot() != null) {
          Assert.assertTrue("pr.id.root #" + idCount + " was not transformed",
              patient.getIdentifier().get(idCount).getValue().contains(id.getRoot()));
        } else if (id.getExtension() != null) {
          Assert.assertTrue("pr.id.root #" + idCount + " was not transformed",
              patient.getIdentifier().get(idCount).getValue().contains(id.getExtension()));
        }
        // codeSystem method is changed and tested

        idCount++;
      }
      // patient.name
      // Notice that patient.name is fullfilled by the method EN2HumanName.
      int nameCount = 0;
      for (EN pn : pr.getPatient().getNames()) {

        // patient.name.use
        if (pn.getUses() == null || pn.getUses().isEmpty()) {
          Assert.assertNull(patient.getName().get(nameCount).getUse());
        } else {
          Assert.assertEquals("pr.patient.name[" + nameCount + "]" + ".use was not transformed",
              vsti.transformEntityNameUse2NameUse(pn.getUses().get(0)).toString().toLowerCase(),
              patient.getName().get(nameCount).getUse());
        }

        // patient.name.text
        Assert.assertEquals(
            "pr.patient.name[" + nameCount + "].text was not transformed", pn.getText(),
            patient.getName().get(nameCount).getText());

        // patient.name.family
        //int familyCount = 0;
        for (ENXP family : pn.getFamilies()) {
          if (family == null || family.isSetNullFlavor()) {
            // It can return null or an empty list
            Assert.assertTrue(patient.getName().get(nameCount).getFamily() == null
                || patient.getName().get(nameCount).getFamily() != null);
          } else {
            Assert.assertEquals(
                "pr.patient.name[" + nameCount + "].family was not transformed", family.getText(),
                patient.getName().get(nameCount).getFamily());
          }
          //familyCount++;
        }

        // patient.name.given
        int givenCount = 0;
        for (ENXP given : pn.getGivens()) {
          if (given == null || given.isSetNullFlavor()) {
            // It can return null or an empty list
            Assert.assertTrue(patient.getName().get(nameCount).getGiven() == null
                || patient.getName().get(nameCount).getGiven().size() == 0);
          } else {
            Assert.assertEquals(
                "pr.patient.name[" + nameCount + "].given was not transformed", given.getText(),
                patient.getName().get(nameCount).getGiven().get(givenCount).getValue());
          }
          givenCount++;
        }

        // patient.name.prefix
        int prefixCount = 0;
        for (ENXP prefix : pn.getPrefixes()) {
          if (prefix == null || prefix.isSetNullFlavor()) {
            // It can return null or an empty list
            Assert.assertTrue(patient.getName().get(nameCount).getPrefix() == null
                || patient.getName().get(nameCount).getPrefix().size() == 0);
          } else {
            Assert.assertEquals(
                "pr.patient.name[" + nameCount + "].prefix was not transformed", prefix.getText(),
                patient.getName().get(nameCount).getPrefix().get(prefixCount).getValue());
          }
          prefixCount++;
        }

        // patient.name.suffix
        int suffixCount = 0;
        for (ENXP suffix : pn.getPrefixes()) {
          if (suffix == null || suffix.isSetNullFlavor()) {
            // It can return null or an empty list
            Assert.assertTrue(patient.getName().get(nameCount).getSuffix() == null
                || patient.getName().get(nameCount).getSuffix().size() == 0);
          } else {
            Assert.assertEquals(
                "pr.patient.name[" + nameCount + "].suffix was not transformed", suffix.getText(),
                patient.getName().get(nameCount).getSuffix().get(suffixCount).getValue());
          }
          suffixCount++;
        }

        // patient.name.period
        if (pn.getValidTime() == null || pn.getValidTime().isSetNullFlavor()) {
          // It can return null or an empty list
          Assert.assertTrue(patient.getName().get(nameCount).getPeriod() == null
              || patient.getName().get(nameCount).getPeriod().isEmpty());
        }
      }

      // patient.telecom
      // Notice that patient.telecom is fullfilled by the method dtt.TEL2ContactPoint
      if (pr.getTelecoms() == null || pr.getTelecoms().isEmpty()) {
        Assert.assertTrue(patient.getTelecom() == null || patient.getTelecom().isEmpty());
      } else {
        // size check
        Assert.assertTrue(pr.getTelecoms().size() == patient.getTelecom().size());
        // We have already tested the method TEL2ContactPoint. Therefore, null-check and
        // size-check is enough for now.
      }

      // patient.gender
      // vst.AdministrativeGenderCode2AdministrativeGenderEnum is used in this
      // transformation.
      // Following test aims to test that ValueSetTransformer method.
      if (pr.getPatient().getAdministrativeGenderCode() == null
          || pr.getPatient().getAdministrativeGenderCode().isSetNullFlavor()) {
        Assert.assertNull(patient.getGender());
      }

      // patient.birthDate
      // Notice that patient.birthDate is fullfilled by the method dtt.TS2Date
      if (pr.getPatient().getBirthTime() == null 
          || pr.getPatient().getBirthTime().isSetNullFlavor()) {
        Assert.assertTrue(patient.getBirthDate() == null);
      }

      // patient.address
      // Notice that patient.address is fullfilled by the method dtt.AD2Address
      if (pr.getAddrs() == null || pr.getAddrs().isEmpty()) {
        Assert.assertTrue(patient.getAddress() == null || patient.getAddress().isEmpty());
      } else {
        // We have already tested the method AD2Address. Therefore, null-check and
        // size-check is enough for now.
        Assert.assertTrue(pr.getAddrs().size() == patient.getAddress().size());
      }

      // patient.maritalStatus
      // vst.MaritalStatusCode2MaritalStatusCodesEnum is used in this transformation.
      // Following test aims to test that ValueSetTransformer method.
      if (pr.getPatient().getMaritalStatusCode() == null 
          || pr.getPatient().getMaritalStatusCode().isSetNullFlavor()) {
        Assert.assertTrue(patient.getMaritalStatus() == null 
            || patient.getMaritalStatus().isEmpty());
      } else {
        Assert.assertTrue(
              patient.getMaritalStatus().getCoding().get(0).getCode().toLowerCase().charAt(0) 
              == pr.getPatient().getMaritalStatusCode().getCode().toLowerCase().charAt(0));
      }

      // patient.languageCommunication
      if (pr.getPatient().getLanguageCommunications() == null
          || pr.getPatient().getLanguageCommunications().isEmpty()) {
        Assert.assertTrue(patient.getCommunication() == null 
            || patient.getCommunication().isEmpty());
      } else {
        Assert.assertTrue(
            pr.getPatient().getLanguageCommunications().size() 
            == patient.getCommunication().size());

        int sizeCommunication = pr.getPatient().getLanguageCommunications().size();
        while (sizeCommunication != 0) {

          // language
          if (pr.getPatient().getLanguageCommunications()
              .get(sizeCommunication - 1).getLanguageCode() == null
              || pr.getPatient().getLanguageCommunications()
                  .get(0).getLanguageCode().isSetNullFlavor()) {
            Assert.assertTrue(patient.getCommunication()
                .get(sizeCommunication - 1).getLanguage() == null
                || patient.getCommunication().get(sizeCommunication - 1).getLanguage().isEmpty());
          } else {
            // We have already tested the method CD2CodeableConcept. Therefore, null-check
            // is enough for now.
          }

          // preference
          if (pr.getPatient().getLanguageCommunications()
              .get(sizeCommunication - 1).getPreferenceInd() == null
              || pr.getPatient().getLanguageCommunications()
                  .get(sizeCommunication - 1).getPreferenceInd()
                  .isSetNullFlavor()) {
            Assert.assertTrue(patient.getCommunication().get(sizeCommunication - 1).getPreferred());
          } else {
            Assert.assertEquals(
                pr.getPatient().getLanguageCommunications()
                    .get(sizeCommunication - 1).getPreferenceInd().getValue(),
                patient.getCommunication().get(sizeCommunication - 1).getPreferred());
          }
          sizeCommunication--;
        }

      }

      // providerOrganization
      if (pr.getProviderOrganization() == null || pr.getProviderOrganization().isSetNullFlavor()) {
        Assert.assertTrue(patient.getManagingOrganization() == null 
            || patient.getManagingOrganization().isEmpty());
      } else {
        if (pr.getProviderOrganization().getNames() == null) {
          Assert.assertTrue(patient.getManagingOrganization().getDisplay() == null);
        }
      }

      // guardian
      if (pr.getPatient().getGuardians() == null || pr.getPatient().getGuardians().isEmpty()) {
        Assert.assertTrue(patient.getContact() == null || patient.getContact().isEmpty());
      } else {
        // Notice that, inside this mapping, the methods dtt.TEL2ContactPoint and
        // dtt.AD2Address are used.
        // Therefore, null-check and size-check are enough
        Assert.assertTrue(pr.getPatient().getGuardians().size() == patient.getContact().size());
      }

      // extensions
      for (Extension extension : patient.getExtension()) {
        Assert.assertTrue(extension.getUrl() != null);
        Assert.assertTrue(extension.getValue() != null);
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testProblemConcernAct2Condition() {

    appendToResultFile("## TEST: ProblemConcernAct2Condition\n");
    // null instance test
    ProblemConcernAct cdaNull = null;
    Bundle fhirNull = rt.transformProblemConcernAct2Condition(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getProblemSection() != null 
        && !ResourceTransformerTest.ccd.getProblemSection().isSetNullFlavor()) {
      if (ResourceTransformerTest.ccd.getProblemSection().getProblemConcerns() != null
          && !ResourceTransformerTest.ccd.getProblemSection().getProblemConcerns().isEmpty()) {
        for (ProblemConcernAct problemConcernAct : ResourceTransformerTest
            .ccd.getProblemSection().getProblemConcerns()) {
          if (problemConcernAct != null && !problemConcernAct.isSetNullFlavor()) {
            appendToResultFile(transformationStartMsg);
            Bundle fhirConditionBundle = rt.transformProblemConcernAct2Condition(problemConcernAct);
            appendToResultFile(transformationEndMsg);
            appendToResultFile(fhirConditionBundle);
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testProcedure2Procedure() {

    appendToResultFile("## TEST: Procedure2Procedure\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.Procedure cdaNull = null;
    Bundle fhirNull = rt.transformProcedure2Procedure(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    if (ResourceTransformerTest.ccd.getProceduresSection() != null 
        && !ResourceTransformerTest.ccd.getProceduresSection().isSetNullFlavor()) {
      if (ResourceTransformerTest.ccd.getProceduresSection().getProcedures() != null
          && !ResourceTransformerTest.ccd.getProceduresSection().getProcedures().isEmpty()) {
        for (org.openhealthtools.mdht.uml.cda.Procedure cdaProcedure : ResourceTransformerTest
            .ccd.getProceduresSection()
            .getProcedures()) {
          // traversing procedures
          appendToResultFile(transformationStartMsg);
          Bundle fhirProcedureBundle = rt.transformProcedure2Procedure(cdaProcedure);
          appendToResultFile(transformationEndMsg);
          appendToResultFile(fhirProcedureBundle);
        }
      }
    }

    if (ResourceTransformerTest.ccd.getEncountersSection() != null 
        && !ResourceTransformerTest.ccd.getEncountersSection().isSetNullFlavor()) {
      if (ResourceTransformerTest.ccd.getEncountersSection().getProcedures() != null
          && !ResourceTransformerTest.ccd.getEncountersSection().getProcedures().isEmpty()) {
        for (org.openhealthtools.mdht.uml.cda.Procedure cdaProcedure : ResourceTransformerTest
            .ccd.getEncountersSection()
            .getProcedures()) {
          // traversing procedures
          appendToResultFile(transformationStartMsg);
          Bundle fhirProcedureBundle = rt.transformProcedure2Procedure(cdaProcedure);
          appendToResultFile(transformationEndMsg);
          appendToResultFile(fhirProcedureBundle);
        }
      }
    }

    if (ResourceTransformerTest.ccd.getAllSections() != null 
        && !ResourceTransformerTest.ccd.getAllSections().isEmpty()) {
      for (Section section : ResourceTransformerTest.ccd.getAllSections()) {
        if (section.getProcedures() != null && !section.getProcedures().isEmpty()) {
          for (org.openhealthtools.mdht.uml.cda.Procedure cdaProcedure : section.getProcedures()) {
            // traversing procedures
            appendToResultFile(transformationStartMsg);
            Bundle fhirProcedureBundle = rt.transformProcedure2Procedure(cdaProcedure);
            appendToResultFile(transformationEndMsg);
            appendToResultFile(fhirProcedureBundle);
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testResultOrganizer2DiagnosticReport() {

    appendToResultFile("## TEST: ResultOrganizer2DiagnosticReport\n");
    // null instance test
    ResultOrganizer cdaNull = null;
    Bundle fhirNull = rt.transformResultOrganizer2DiagnosticReport(cdaNull);
    Assert.assertNull(fhirNull);

    // instance from file
    ResultsSection resultsSec = ResourceTransformerTest.ccd.getResultsSection();

    if (resultsSec != null && !resultsSec.isSetNullFlavor()) {
      if (resultsSec.getOrganizers() != null && !resultsSec.getOrganizers().isEmpty()) {
        for (org.openhealthtools.mdht.uml.cda.Organizer cdaOrganizer : resultsSec.getOrganizers()) {
          if (cdaOrganizer != null && !cdaOrganizer.isSetNullFlavor()) {
            if (cdaOrganizer instanceof ResultOrganizer) {
              appendToResultFile(transformationStartMsg);
              Bundle fhirDiagReport = 
                  rt.transformResultOrganizer2DiagnosticReport((ResultOrganizer) cdaOrganizer);
              appendToResultFile(transformationEndMsg);
              appendToResultFile(fhirDiagReport);
            }
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testSection2Section() {

    appendToResultFile("## TEST: Section2Section\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.Section cdaNull = null;
    SectionComponent fhirNull = rt.transformSection2Section(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    org.openhealthtools.mdht.uml.cda.Section sampleSection = null;

    // assigning sampleSection to one sample section
    if (ResourceTransformerTest.ccd.getEncountersSection() != null 
        && !ResourceTransformerTest.ccd.getEncountersSection().isSetNullFlavor()) {
      if (ResourceTransformerTest.ccd.getEncountersSection().getAllSections() != null
          && !ResourceTransformerTest.ccd.getEncountersSection().getAllSections().isEmpty()) {
        if (ResourceTransformerTest.ccd.getEncountersSection().getAllSections().get(0) != null
            && !ResourceTransformerTest.ccd.getEncountersSection()
                .getAllSections().get(0).isSetNullFlavor()) {
          sampleSection = ResourceTransformerTest
              .ccd.getEncountersSection().getAllSections().get(0);
        }
      }
    }
    if (sampleSection != null) {
      Composition fhirComposition = new Composition();
      appendToResultFile(transformationStartMsg);
      SectionComponent fhirSection = rt.transformSection2Section(sampleSection);
      appendToResultFile(transformationEndMsg);
      fhirComposition.addSection(fhirSection);
      appendToResultFile(fhirComposition);
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testSocialHistory() {

    appendToResultFile("## TEST: SocialHistory\n");
    SocialHistorySection socialHistSec = ResourceTransformerTest.ccd.getSocialHistorySection();

    if (socialHistSec != null && !socialHistSec.isSetNullFlavor()) {
      if (socialHistSec.getObservations() != null && !socialHistSec.getObservations().isEmpty()) {
        for (org.openhealthtools.mdht.uml.cda.Observation cdaObs 
            : socialHistSec.getObservations()) {
          if (cdaObs != null && !cdaObs.isSetNullFlavor()) {
            appendToResultFile(transformationStartMsg);
            Bundle fhirObs = rt.transformObservation2Observation(cdaObs);
            appendToResultFile(transformationEndMsg);
            appendToResultFile(fhirObs);
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  @Test
  public void testVitalSignObservation2Observation() {

    appendToResultFile("## TEST: VitalSignObservation2Observation\n");
    // null instance test
    org.openhealthtools.mdht.uml.cda.consol.VitalSignObservation cdaNull = null;
    Bundle fhirNull = rt.transformVitalSignObservation2Observation(cdaNull);
    Assert.assertNull(fhirNull);

    // instances from file
    VitalSignsSectionEntriesOptional vitalSignsSec = 
        ResourceTransformerTest.ccd.getVitalSignsSectionEntriesOptional();
    if (vitalSignsSec != null && !vitalSignsSec.isSetNullFlavor()) {
      if (vitalSignsSec.getVitalSignsOrganizers() != null 
          && !vitalSignsSec.getVitalSignsOrganizers().isEmpty()) {
        for (VitalSignsOrganizer vitalSignOrganizer : vitalSignsSec.getVitalSignsOrganizers()) {
          if (vitalSignOrganizer != null && !vitalSignOrganizer.isSetNullFlavor()) {
            if (vitalSignOrganizer.getVitalSignObservations() != null
                && !vitalSignOrganizer.getVitalSignObservations().isEmpty()) {
              for (VitalSignObservation vitalSignObservation 
                  : vitalSignOrganizer.getVitalSignObservations()) {
                if (vitalSignObservation != null && !vitalSignObservation.isSetNullFlavor()) {
                  appendToResultFile(transformationStartMsg);
                  Bundle fhirObservation = rt
                      .transformVitalSignObservation2Observation(
                            (VitalSignObservation) vitalSignObservation);
                  appendToResultFile(transformationEndMsg);
                  appendToResultFile(fhirObservation);
                }
              }
            }
          }
        }
      }
    }
    appendToResultFile(endOfTestMsg);
  }

  private void appendToResultFile(Object param) {
    try {
      if (param instanceof String) {
        resultFW.append((String) param);
      } else if (param instanceof Resource) {
        FhirUtil.printJson((Resource) param, resultFW);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}