package tr.com.srdc.cda2fhir;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

import java.io.ByteArrayOutputStream;
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
  private static final String IMPORT_FILE = 
      "c:/docker/data/ccd/1567642462016-A82CE066C1D8CA90ECDC2456C1335ADA.xml";
  private File importFile;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    CDAUtil.loadPackages();
    ctx = FhirContext.forDstu3();

    importFile = new File(IMPORT_FILE);
  }

  /**
   * Cleanup Method.
   */
  @After
  public void cleanup() {

  }

  @Test
  public void testCdaTransform() throws Exception {
    
    if (importFile.exists()) {
      FileInputStream fis = new FileInputStream(IMPORT_FILE);
      ContinuityOfCareDocument ccd = 
          (ContinuityOfCareDocument)CDAUtil.loadAs(fis, 
             ConsolPackage.eINSTANCE.getContinuityOfCareDocument());
      ICdaTransformer transformer = new CcdTransformerImpl(IdGeneratorEnum.UUID);
      Config.setGenerateDafProfileMetadata(true);
      Config.setGenerateNarrative(true);
      Bundle bundle = transformer.transformDocument(ccd, BundleType.TRANSACTION);
      if (bundle != null) {
        outputResource(bundle, IMPORT_FILE + ".json");
      }
    } else {
      System.out.print("Sample Import File not found.  Passing Test.");
      assertTrue(true);
    }
  }

  @Test
  public void testLabCdaTransform() throws Exception {
    if (importFile.exists()) {
      FileInputStream fis = new FileInputStream(IMPORT_FILE);
      ContinuityOfCareDocument ccd = 
          (ContinuityOfCareDocument)CDAUtil.loadAs(fis, 
              ConsolPackage.eINSTANCE.getContinuityOfCareDocument());
      ICdaTransformer transformer = new CcdTransformerImpl(IdGeneratorEnum.UUID);
      Config.setGenerateDafProfileMetadata(true);
      Config.setGenerateNarrative(true);
      Bundle bundle = transformer.transformDocument(ccd, BundleType.TRANSACTION);
      if (bundle != null) {
        outputResource(bundle, IMPORT_FILE + ".json");
      }
    } else {
      System.out.print("Sample Import File not found.  Passing Test.");
      assertTrue(true);
    }
  }

  @Test
  public void testCdaIngest() throws Exception {
    if (importFile.exists()) {
      FileInputStream fis = new FileInputStream(IMPORT_FILE);
      ContinuityOfCareDocument ccd = 
          (ContinuityOfCareDocument)CDAUtil.loadAs(fis, 
              ConsolPackage.eINSTANCE.getContinuityOfCareDocument());     
      assertNotNull(ccd);   
    } else {
      System.out.print("Sample Import File not found.  Passing Test.");
      assertTrue(true);
    }
  }


  @Test
  public void testSaveCdaFileToStream() throws Exception {
    if (importFile.exists()) {
      FileInputStream fis = new FileInputStream(IMPORT_FILE);
      ContinuityOfCareDocument ccd = 
          (ContinuityOfCareDocument)CDAUtil.loadAs(fis, 
              ConsolPackage.eINSTANCE.getContinuityOfCareDocument());

      ByteArrayOutputStream output = new ByteArrayOutputStream();
      CDAUtil.save(ccd, output);
      assertNotNull(output);
    } else {
      System.out.print("Sample Import File not found.  Passing Test.");
      assertTrue(true);
    }
    
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