package tr.com.srdc.cda2fhir;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;

import tr.com.srdc.cda2fhir.conf.Config;
import tr.com.srdc.cda2fhir.transform.CcdTransformerImpl;
import tr.com.srdc.cda2fhir.transform.ICdaTransformer;
import tr.com.srdc.cda2fhir.util.FhirUtil;
import tr.com.srdc.cda2fhir.util.IdGeneratorEnum;
import tr.com.srdc.cda2fhir.validation.IValidator;
import tr.com.srdc.cda2fhir.validation.ValidatorImpl;

public class ValidatorTest {

  /**
   * Before Class Initialization.
   */
  @BeforeClass
  public static void init() {
    // Load MDHT CDA packages. Otherwise ContinuityOfCareDocument and similar
    // documents will not be recognised.
    // This has to be called before loading the document; otherwise will have no
    // effect.
    CDAUtil.loadPackages();
  }

  // 170.315_b1_toc_gold_sample2_v1.xml without profile
  @Ignore
  @Test
  public void testGoldSampleBundleWithoutProfile() throws Exception {
    String cdaResourcePath = "src/test/resources/170.315_b1_toc_gold_sample2_v1.xml";
    String targetPathForFhirResource = 
        "src/test/resources/output/170.315_b1_toc_gold_sample2_v1-wo-profile-validation.xml";
    String targetPathForResultFile = 
        "src/test/resources/output/"
        + "validation-result-wo-profile-for-170.315_b1_toc_gold_sample2_v1.html";
    boolean generateDafProfileMetadata = false;
    transformAndValidate(cdaResourcePath, targetPathForFhirResource, targetPathForResultFile,
        generateDafProfileMetadata);
  }

  // 170.315_b1_toc_gold_sample2_v1.xml with profile
  @Ignore
  @Test
  public void testGoldSampleBundleWithProfile() throws Exception {
    String cdaResourcePath = "src/test/resources/170.315_b1_toc_gold_sample2_v1.xml";
    String targetPathForFhirResource = 
        "src/test/resources/output/170.315_b1_toc_gold_sample2_v1-w-profile-validation.xml";
    String targetPathForResultFile = 
        "src/test/resources/output/"
        + "validation-result-w-profile-for-170.315_b1_toc_gold_sample2_v1.html";
    boolean generateDafProfileMetadata = true;
    transformAndValidate(cdaResourcePath, targetPathForFhirResource, targetPathForResultFile,
        generateDafProfileMetadata);
  }

  // 170.315_b1_toc_inp_ccd_r21_sample1_v5.xml without profile
  @Ignore
  @Test
  public void testInpSampleBundleWithoutProfile() throws Exception {
    String cdaResourcePath = "src/test/resources/170.315_b1_toc_inp_ccd_r21_sample1_v5.xml";
    String targetPathForFhirResource = 
        "src/test/resources/output/170.315_b1_toc_inp_ccd_r21_sample1_v5-wo-profile-validation.xml";
    String targetPathForResultFile = 
        "src/test/resources/output/"
        + "validation-result-wo-profile-for-170.315_b1_toc_inp_ccd_r21_sample1_v5.html";
    boolean generateDafProfileMetadata = false;
    transformAndValidate(cdaResourcePath, targetPathForFhirResource, targetPathForResultFile,
        generateDafProfileMetadata);
  }

  // 170.315_b1_toc_inp_ccd_r21_sample1_v5.xml without profile
  @Ignore
  @Test
  public void testInpSampleBundleWithProfile() throws Exception {
    String cdaResourcePath = "src/test/resources/170.315_b1_toc_inp_ccd_r21_sample1_v5.xml";
    String targetPathForFhirResource = 
        "src/test/resources/output/170.315_b1_toc_inp_ccd_r21_sample1_v5-w-profile-validation.xml";
    String targetPathForResultFile = 
        "src/test/resources/output/"
        + "validation-result-w-profile-for-170.315_b1_toc_inp_ccd_r21_sample1_v5.html";
    boolean generateDafProfileMetadata = true;
    transformAndValidate(cdaResourcePath, targetPathForFhirResource, targetPathForResultFile,
        generateDafProfileMetadata);
  }

  // C-CDA_R2-1_CCD.xml without DAF profile
  @Ignore
  @Test
  public void testReferenceCcdBundleWithoutProfile() throws Exception {
    String cdaResourcePath = "src/test/resources/C-CDA_R2-1_CCD.xml";
    String targetPathForFhirResource = 
        "src/test/resources/output/C-CDA_R2-1_CCD-wo-profile-validation.xml";
    String targetPathForResultFile = 
        "src/test/resources/output/validation-result-wo-profile-for-C-CDA_R2-1_CCD.html";
    boolean generateDafProfileMetadata = false;
    transformAndValidate(cdaResourcePath, targetPathForFhirResource, targetPathForResultFile,
        generateDafProfileMetadata);
  }

  // C-CDA_R2-1_CCD.xml with DAF profile
  @Ignore
  @Test
  public void testReferenceCcdBundleWithProfile() throws Exception {
    String cdaResourcePath = "src/test/resources/C-CDA_R2-1_CCD.xml";
    String targetPathForFhirResource = 
        "src/test/resources/output/C-CDA_R2-1_CCD-w-profile-validation.xml";
    String targetPathForResultFile = 
        "src/test/resources/output/validation-result-w-profile-for-C-CDA_R2-1_CCD.html";
    boolean generateDafProfileMetadata = true;
    transformAndValidate(cdaResourcePath, targetPathForFhirResource, targetPathForResultFile,
        generateDafProfileMetadata);
  }

  // Vitera_CCDA_SMART_Sample.xml without profile
  @Ignore
  @Test
  public void testViteraBundleWithoutProfile() throws Exception {
    String cdaResourcePath = "src/test/resources/Vitera_CCDA_SMART_Sample.xml";
    String targetPathForFhirResource = 
        "src/test/resources/output/Vitera_CCDA_SMART_Sample-wo-profile-validation.xml";
    String targetPathForResultFile = 
        "src/test/resources/output/validation-result-wo-profile-for-Vitera_CCDA_SMART_Sample.html";
    boolean generateDafProfileMetadata = false;
    transformAndValidate(cdaResourcePath, targetPathForFhirResource, targetPathForResultFile,
        generateDafProfileMetadata);
  }

  // Vitera_CCDA_SMART_Sample.xml with profile
  @Ignore
  @Test
  public void testViteraBundleWithProfile() throws Exception {
    String cdaResourcePath = 
        "src/test/resources/Vitera_CCDA_SMART_Sample.xml";
    String targetPathForFhirResource = 
        "src/test/resources/output/Vitera_CCDA_SMART_Sample-w-profile-validation.xml";
    String targetPathForResultFile = 
        "src/test/resources/output/validation-result-w-profile-for-Vitera_CCDA_SMART_Sample.html";
    boolean generateDafProfileMetadata = true;
    transformAndValidate(cdaResourcePath, targetPathForFhirResource, targetPathForResultFile,
        generateDafProfileMetadata);
  }

  /**
   * Transforms a CDA resource to a FHIR resource, validates the FHIR resource and
   * prints the validation result to the target path.
   * 
   * @param cdaResourcePath            A file path of the CDA resource that is to
   *                                   be transformed
   * @param targetPathForFHIRResource  A file path where the FHIR resource is to
   *                                   be created
   * @param targetPathForResultFile    A file path where the validation result
   *                                   file is to be created
   * @param generateDAFProfileMetadata A boolean indicating whether the generated
   *                                   resources will include DAF profile
   *                                   declarations in meta.profile
   * @throws Exception Exception
   */
  private void transformAndValidate(String cdaResourcePath, String targetPathForFhirResource,
      String targetPathForResultFile, boolean generateDafProfileMetadata) throws Exception {
    IValidator validator = new ValidatorImpl();
    ByteArrayOutputStream os = null;

    // file to be transformed
    FileInputStream fis = new FileInputStream(cdaResourcePath);

    ClinicalDocument cda = CDAUtil.load(fis);
    ICdaTransformer ccdTransformer = new CcdTransformerImpl(IdGeneratorEnum.COUNTER);

    // set whether DAF Profile URLs will be created in meta.profile of relevant
    // resources
    Config.setGenerateDafProfileMetadata(generateDafProfileMetadata);

    // make the transformation
    Bundle bundle = ccdTransformer.transformDocument(cda);
    if (bundle != null) {
      // print the bundle for checking against validation results
      // printed as XML, because HL7 FHIR Validator works with XML encoded resources
      FhirUtil.printXml(bundle, targetPathForFhirResource);
      os = (ByteArrayOutputStream) validator.validateBundle(bundle);
    }

    if (os != null) {
      File validationFile = new File(targetPathForResultFile);
      validationFile.getParentFile().mkdirs();

      FileOutputStream fos = new FileOutputStream(validationFile);
      os.writeTo(fos);
      os.close();
      fos.close();
    }
  }

}
