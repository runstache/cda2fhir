package tr.com.srdc.cda2fhir.conf;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.ConditionVerificationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.EncounterStateEnum;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;

import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.Charset;

public class Config {

  private static FhirContext fhirCtx;

  // Default values for some mandatory attributes, which cannot be retrieved from
  // CDA document
  public static final String DEFAULT_COMMUNICATION_LANGUAGE_CODE_SYSTEM = "urn:ietf:bcp:47";
  public static final ConditionVerificationStatusEnum DEFAULT_CONDITION_VERIFICATION_STATUS = ConditionVerificationStatusEnum.CONFIRMED;
  public static final CompositionStatusEnum DEFAULT_COMPOSITION_STATUS = CompositionStatusEnum.PRELIMINARY;
  public static final ContactPointSystem DEFAULT_CONTACT_POINT_SYSTEM = ContactPointSystem.PHONE;
  public static final CodingDt DEFAULT_ENCOUNTER_PARTICIPANT_TYPE_CODE = new CodingDt()
      .setSystem("http://hl7.org/fhir/v3/ParticipationType").setCode("PART").setDisplay("Participation");
  public static final EncounterStateEnum DEFAULT_ENCOUNTER_STATUS = EncounterStateEnum.FINISHED;
  public static final CodingDt DEFAULT_DIAGNOSTICREPORT_PERFORMER_DATA_ABSENT_REASON_CODE = new CodingDt()
      .setSystem("http://hl7.org/fhir/data-absent-reason").setCode("unknown").setDisplay("Unknown");
  public static final boolean DEFAULT_IMMUNIZATION_REPORTED = false;

  public static final String VALIDATION_DEFINITION_PATH = "src/main/resources/validation-min.xml.zip";
  public static final int DEFAULT_VALIDATOR_TERMINOLOGY_SERVER_CHECK_TIMEOUT = 10000; // in milliseconds, > 0
  // if the array containing URLs doesn't give an accessible URL, this URL will be
  // used
  public static final String DEFAULT_VALIDATOR_TERMINOLOGY_SERVER_URL = "http://tx.fhir.org/r2";
  public static final String[] VALIDATOR_TERMINOLOGY_SERVER_URLS = { "http://tx.fhir.org/r2", "http://test.fhir.org/r2",
      "http://fhir.i2b2.org/srv-dstu2-0.2/api/open" };

  public static final String NARRATIVE_PROPERTIES_FILE_PATH = "file:src/main/resources/narrative/customnarrative.properties";

  private static boolean generateNarrative = true;
  private static INarrativeGenerator narrativeGenerator;

  private static boolean generateDafProfileMetadata = true;

  private static final Logger logger = LoggerFactory.getLogger(Config.class);

  static {
    fhirCtx = FhirContext.forDstu2();
    narrativeGenerator = new CustomThymeleafNarrativeGenerator(NARRATIVE_PROPERTIES_FILE_PATH);
    if (generateNarrative) {
      fhirCtx.setNarrativeGenerator(narrativeGenerator);
    }

    logger.info("System file encoding is: " + Charset.defaultCharset().displayName());
  }

  public static FhirContext getFhirContext() {
    return fhirCtx;
  }

  public static void setGenerateNarrative(boolean generateNar) {
    generateNarrative = generateNar;
    if (generateNarrative) {
      fhirCtx.setNarrativeGenerator(narrativeGenerator);
    } else {
      fhirCtx.setNarrativeGenerator(null);
    }
  }

  public static void setGenerateDafProfileMetadata(boolean generateDafProfileMeta) {
    generateDafProfileMetadata = generateDafProfileMeta;
  }

  public static boolean isGenerateDafProfileMetadata() {
    return generateDafProfileMetadata;
  }

}
