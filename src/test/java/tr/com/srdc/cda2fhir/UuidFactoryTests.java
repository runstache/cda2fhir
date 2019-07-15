package tr.com.srdc.cda2fhir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Provenance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tr.com.srdc.cda2fhir.util.UuidFactory;

public class UuidFactoryTests {

  private UuidFactory factory;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    factory = new UuidFactory();
  }


  /**
   * Cleanup Method.
   */
  @After
  public void cleanup() {

  }

  @Test
  public void testAddKeyString() {
    String key = "MyKey";
    assertNotNull(factory.addKey(key));
  }

  @Test
  public void testAddKeyFhirResource() {
    Patient patient = new Patient();
    Identifier id = new Identifier();
    id.setSystem("TEST.MRN.OID");
    id.setValue("1234");
    patient.addIdentifier(id);
    assertNotNull(factory.addKey(patient));
  }

  @Test
  public void testAddKeyFhirResourceMultiId() {
    Patient patient = new Patient();
    Identifier id = new Identifier();
    id.setSystem("TEST.MRN.OID");
    id.setValue("12345");
    patient.addIdentifier(id);
    Identifier id2 = new Identifier();
    id2.setSystem("TEST.SSN.OID");
    id2.setValue("1234567");
    patient.addIdentifier(id2);

    UUID guid = factory.addKey(patient);
    assertEquals(guid, factory.getGuid("TEST.SSN.OID|1234567"));
    assertEquals(guid, factory.getGuid("TEST.MRN.OID|12345"));
  }

  @Test
  public void testGetGuid() {
    String key = "MyKey";
    UUID guid = factory.addKey(key);
    assertNotNull(guid);
    assertEquals(guid, factory.getGuid(key));
  }

  @Test
  public void testGetGuidResource() {
    Patient patient = new Patient();
    Identifier id = new Identifier();
    id.setSystem("TEST.MRN.OID");
    id.setValue("12345");
    patient.addIdentifier(id);

    UUID guid = factory.addKey(patient);
    assertNotNull(guid);
    assertEquals(guid, factory.getGuid(patient));
  }

  @Test
  public void testGetGuidResourceMultiId() {
    Patient patient = new Patient();
    Identifier id = new Identifier();
    id.setSystem("TEST.MRN.OID");
    id.setValue("12345");
    patient.addIdentifier(id);
    Identifier id2 = new Identifier();
    id2.setSystem("TEST.SSN.OID");
    id2.setValue("1234567");
    patient.addIdentifier(id2);

    UUID guid = factory.addKey(patient);
    Patient patient2 = new Patient();
    patient2.addIdentifier(id2);
    assertNotNull(guid);
    assertEquals(guid, factory.getGuid(patient2));
  }

  @Test
  public void testAddKeyNoIdentifier() {
    Provenance prov = new Provenance();
    prov.setId(new IdType("Provenance", 1L));
    assertNotNull(prov);
  }

  @Test
  public void testGetGuidNoIdentifier() {
    Provenance prov = new Provenance();
    prov.setId(new IdType("Provenance", 1L));
    UUID guid = factory.addKey(prov);
    assertNotNull(guid);
    assertEquals(guid, factory.getGuid(prov));
  }

}