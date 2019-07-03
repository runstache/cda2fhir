package tr.com.srdc.cda2fhir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupUnmappedComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupUnmappedMode;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.dstu3.model.Enumerations;

import org.junit.Before;
import org.junit.Test;

import tr.com.srdc.cda2fhir.transform.IValueSetsTransformer;
import tr.com.srdc.cda2fhir.transform.ValueSetsTransformerImpl;

public class ValueSetsTransformerTests {

  private IValueSetsTransformer vst;
  private ConceptMap map;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    vst = new ValueSetsTransformerImpl();

    map = new ConceptMap();
    SourceElementComponent source = new SourceElementComponent();
    source.setCode("m");
    source.setDisplay("male");
    TargetElementComponent target = new TargetElementComponent();
    target.setCode("male");
    target.setDisplay("Male");
    source.addTarget(target);
    ConceptMapGroupComponent group = new ConceptMapGroupComponent();
    group.setSource("2.16.840.1.113883.4.642.3.1");
    group.setTarget("http://hl7.org/fhir/ValueSet/administrative-gender");
    group.setTargetVersion("1.0");
    group.addElement(source);
    group.setUnmapped(
          new ConceptMapGroupUnmappedComponent()
            .setCode("unknown")
            .setDisplay("Unknown")
            .setMode(ConceptMapGroupUnmappedMode.FIXED));
    map.addGroup(group);
  }

  @Test
  public void testCdaCodeToFhirCodeConceptMap() {
    assertEquals(Enumerations.AdministrativeGender.MALE, 
        vst.transformCdaValueToFhirCodeValue("m", map, Enumerations.AdministrativeGender.class));
  }

  @Test
  public void testCdaCodeToFhirCodeConceptMapUnMapped() {
    assertEquals(Enumerations.AdministrativeGender.UNKNOWN, 
        vst.transformCdaValueToFhirCodeValue("jeff", map, Enumerations.AdministrativeGender.class));
  }

  @Test
  public void testCdaCodeToFhirCodeConceptMapInvalidCode() {
    map.getGroupFirstRep().getUnmapped().setMode(ConceptMapGroupUnmappedMode.PROVIDED);
    assertNull(
          vst.transformCdaValueToFhirCodeValue(
            "jeff", map, Enumerations.AdministrativeGender.class));
  }

  @Test
  public void testCdaCodeToFhirCodingConceptMap() {
    Coding coding = new Coding("http://hl7.org/fhir/ValueSet/administrative-gender", "male", "Male");
    Coding result = vst.transformCdaValueToFhirCodeValue("m", map, Coding.class);
    assertEquals(coding.getSystem(), result.getSystem());
    assertEquals(coding.getCode(), result.getCode());
    assertEquals(coding.getDisplay(), result.getDisplay());

  }

  @Test
  public void testCdaCodeToFhirCodingConceptMapUnmapped() {
    Coding coding = new Coding("http://hl7.org/fhir/ValueSet/administrative-gender", "unknown", "Unknown");
    Coding result = vst.transformCdaValueToFhirCodeValue("jeff", map, Coding.class);
    assertEquals(coding.getSystem(), result.getSystem());
    assertEquals(coding.getCode(), result.getCode());
    assertEquals(coding.getDisplay(), result.getDisplay());

  }

  @Test
  public void testCdCodeToFhirCodeableConcept() {
    Coding coding = new Coding("http://hl7.org/fhir/ValueSet/administrative-gender", "male", "Male");
    Coding result = vst.transformCdaValueToFhirCodeValue("m", map, Coding.class);
    assertEquals(coding.getSystem(), result.getSystem());
    assertEquals(coding.getCode(), result.getCode());
    assertEquals(coding.getDisplay(), result.getDisplay());
  }

  @Test
  public void testCdCodeToFhirCodeableConceptUnmapped() {
    Coding coding = new Coding("http://hl7.org/fhir/ValueSet/administrative-gender", "unknown", "Unknown");
    CodeableConcept concept = 
        vst.transformCdaValueToFhirCodeValue("jeff", map, CodeableConcept.class);
    Coding result = concept.getCodingFirstRep();
    assertEquals(coding.getSystem(), result.getSystem());
    assertEquals(coding.getCode(), result.getCode());
    assertEquals(coding.getDisplay(), result.getDisplay());
  }





}