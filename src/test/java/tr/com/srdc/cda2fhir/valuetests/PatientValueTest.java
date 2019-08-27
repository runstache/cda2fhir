package tr.com.srdc.cda2fhir.valuetests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;

import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.mdht.uml.cda.consol.ConsolPackage;
import org.openhealthtools.mdht.uml.cda.consol.ContinuityOfCareDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;

import tr.com.srdc.cda2fhir.conf.Config;
import tr.com.srdc.cda2fhir.transform.CcdTransformerImpl;
import tr.com.srdc.cda2fhir.transform.ICdaTransformer;
import tr.com.srdc.cda2fhir.util.IdGeneratorEnum;

public class PatientValueTest {

  /**
   * Maps the current Gold Standard CDA to a FHIR Bundle and validates the values
   * mapped to the Patient are the expected value.
   */

  private Bundle bundle;

  /**
   * Setup Method.
   * 
   * @throws Exception Exception
   */
  @Before
  public void setup() throws Exception {
    CDAUtil.loadPackages();
    FileInputStream fis = new FileInputStream("src/test/resources/170.315_b1_toc_gold_sample2_v1.xml");

    ContinuityOfCareDocument cda = (ContinuityOfCareDocument) CDAUtil.loadAs(fis,
        ConsolPackage.eINSTANCE.getContinuityOfCareDocument());
    ICdaTransformer ccdTransformer = new CcdTransformerImpl(IdGeneratorEnum.UUID);
    Config.setGenerateDafProfileMetadata(true);
    Config.setGenerateNarrative(true);
    bundle = ccdTransformer.transformDocument(cda);
  }

  @Test
  public void testHasPatient() {
    assertTrue(bundle.getEntry().stream().anyMatch(c -> c.getResource() instanceof Patient));
  }

  @Test
  public void testPatientIdentifier() {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      assertFalse(patient.getIdentifier().isEmpty());
      Identifier id = patient.getIdentifierFirstRep();
      assertEquals("urn:oid:2.16.840.1.113883.4.1", id.getSystem());
      assertEquals("414122222", id.getValue());
    } else {
      fail("No Patient in Bundle");
    }
  }

  @Test
  public void testPatientAddress() {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      assertFalse(patient.getAddress().isEmpty());
      Address addr = patient.getAddressFirstRep();
      assertEquals("Beaverton", addr.getCity());
      assertEquals("97006", addr.getPostalCode());
      assertEquals(AddressUse.HOME, addr.getUse());
      assertEquals("1357 Amber Dr", addr.getLine().get(0).getValue());
      assertEquals("OR", addr.getState());
      assertEquals("US", addr.getCountry());

    } else {
      fail("No Patient in Bundle");
    }
  }

  @Test
  public void testPatientPhone() {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      assertFalse(patient.getTelecom().isEmpty());
      assertEquals(2, patient.getTelecom().size());
      assertTrue(patient.getTelecom().stream().anyMatch(c -> c.getValue().equalsIgnoreCase("+1(555)-777-1234")));
      assertTrue(patient.getTelecom().stream().anyMatch(c -> c.getValue().equalsIgnoreCase("+1(555)-723-1544")));
      assertTrue(patient.getTelecom().stream().anyMatch(c -> c.getUse().equals(ContactPointUse.HOME)));
      assertTrue(patient.getTelecom().stream().anyMatch(c -> c.getUse().equals(ContactPointUse.MOBILE)));
    } else {
      fail("No Patient in Bundle");
    }
  }

  @Test
  public void testPatientName() {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      assertFalse(patient.getName().isEmpty());
      assertEquals(1, patient.getName().size());
      assertTrue(patient.getName().stream().anyMatch(c -> c.getGivenAsSingleString().equalsIgnoreCase("Richard")));
      assertTrue(patient.getName().stream().anyMatch(c -> c.getFamily().equalsIgnoreCase("Maur")));
      assertTrue(patient.getName().stream().anyMatch(c -> c.getSuffixAsSingleString().equalsIgnoreCase("jr")));
      assertTrue(patient.getName().stream().anyMatch(c -> c.getUse().equals(NameUse.USUAL)));

    } else {
      fail("No Patient in Bundle");
    }
  }

  @Test
  public void testPatientGender() {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      assertNotNull(patient.getGender());
      assertEquals(AdministrativeGender.MALE, patient.getGender());
    } else {
      fail("No Patient in Bundle");
    }
  }

  @Test
  public void testReligionExtension() {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      assertFalse(patient.getExtension().isEmpty());
      assertTrue(patient.getExtension().stream()
          .anyMatch(c -> c.getUrl().equalsIgnoreCase("http://hl7.org/fhir/StructureDefinition/us-core-religion")));
      CodeableConcept concept = (CodeableConcept) patient
          .getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/us-core-religion").get(0).getValue();
      assertEquals("urn:oid:2.16.840.1.113883.5.1076", concept.getCodingFirstRep().getSystem());
      assertEquals("1013", concept.getCodingFirstRep().getCode());
      assertEquals("Christian (non-Catholic, non-specific)", concept.getCodingFirstRep().getDisplay());

    } else {
      fail("No Patient in Bundle");
    }
  }

  @Test
  public void testMaritalStatus() {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      assertTrue(patient.getMaritalStatus().getCoding().isEmpty());
    } else {
      fail("No Patient in Bundle");
    }
  }

  @Test
  public void testPatientLanguage() {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      assertFalse(patient.getCommunication().isEmpty());
      CodeableConcept concept = patient.getCommunicationFirstRep().getLanguage();
      assertEquals("en", concept.getCodingFirstRep().getCode());
      assertTrue(patient.getCommunicationFirstRep().getPreferred());
    } else {
      fail("No Patient in Bundle");
    }
  }

  @Test
  public void testPatientBirthDate() throws ParseException {
    Optional<BundleEntryComponent> patientEntry = bundle.getEntry().stream()
        .filter(c -> c.getResource() instanceof Patient).findFirst();
    if (patientEntry.isPresent()) {
      Patient patient = (Patient) patientEntry.get().getResource();
      SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMdd");
      assertEquals(new DateType(sdt.parse("19800801")).toString(), patient.getBirthDateElement().toString());

    } else {
      fail("No Patient in Bundle");
    }
  }

  
}