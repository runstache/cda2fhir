package tr.com.srdc.cda2fhir;

import static org.junit.Assert.assertNotNull;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.mdht.uml.cda.consol.ConsolPackage;
import org.openhealthtools.mdht.uml.cda.consol.ContinuityOfCareDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;

import tr.com.srdc.cda2fhir.conf.Config;
import tr.com.srdc.cda2fhir.transform.CcdTransformerImpl;
import tr.com.srdc.cda2fhir.transform.ICdaTransformer;
import tr.com.srdc.cda2fhir.util.IdGeneratorEnum;

public class SampleCdaTransformTest {

  private FhirContext ctx;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    CDAUtil.loadPackages();
    ctx = FhirContext.forDstu3();
  }

  /**
   * Cleanup Method.
   */
  @After
  public void cleanup() {

  }

  @Test
  public void testCdaTransform() throws Exception {
    FileInputStream fis = new FileInputStream("c:/docker/data/ccd/sample.xml");
    ContinuityOfCareDocument ccd = 
        (ContinuityOfCareDocument)CDAUtil.loadAs(fis, 
            ConsolPackage.eINSTANCE.getContinuityOfCareDocument());
    ICdaTransformer transformer = new CcdTransformerImpl(IdGeneratorEnum.UUID);
    Config.setGenerateDafProfileMetadata(true);
    Config.setGenerateNarrative(true);
    Bundle bundle = transformer.transformDocument(ccd, BundleType.TRANSACTION);
    if (bundle != null) {
      outputResource(bundle, "c:/docker/data/ccd/sample.xml.json");
    }
  }

  @Test
  public void testCdaIngest() throws Exception {
    FileInputStream fis = new FileInputStream("c:/docker/data/ccd/sample.xml");
    ContinuityOfCareDocument ccd = 
        (ContinuityOfCareDocument)CDAUtil.loadAs(fis, 
            ConsolPackage.eINSTANCE.getContinuityOfCareDocument());     
    assertNotNull(ccd);   
  }


  private void outputResource(Resource resource, String path) {
    FileWriter writer;
    try {
      File file = new File(path);
      file.getParentFile().mkdirs();
      writer = new FileWriter(file);
      IParser parser = ctx.newJsonParser();
      parser.setPrettyPrint(true);
      writer.append((String)parser.encodeResourceToString(resource));
      writer.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    } 


  }
}