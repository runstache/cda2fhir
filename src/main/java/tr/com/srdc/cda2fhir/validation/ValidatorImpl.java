package tr.com.srdc.cda2fhir.validation;

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
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.FhirPublication;
import org.hl7.fhir.r4.validation.ValidationEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import tr.com.srdc.cda2fhir.conf.Config;
import tr.com.srdc.cda2fhir.util.FhirUtil;

public class ValidatorImpl implements IValidator {

  private String termServerUrl = null;
  
  private final ValidationEngine validationEngine = new ValidationEngine();
  private final Logger logger = LoggerFactory.getLogger(ValidatorImpl.class);

  /**
   * Constructs a validator using the default configuration.
   * 
   * @throws Exception Exception
   */
  public ValidatorImpl() throws Exception {
    // searching for an available terminology server
    for (String tserverUrlString : Config.VALIDATOR_TERMINOLOGY_SERVER_URLS) {
      /*
       * if the request is successful, set tServerURL with that available terminology
       * server and break the loop. .. otherwise, catch the exception and continue
       * with the next terminology server URL string contained in the config.
       */
      boolean checkResult = checkServer(tserverUrlString);
      if (checkResult) {
        this.termServerUrl = tserverUrlString;
        logger.info("Terminology server is successfully set as: {}", tserverUrlString);
        break;
      } else {
        logger.warn(
              "Could not reach terminology server at {} . Trying the next alternative ...", 
              tserverUrlString);
        continue;
      }
    }

    // if the set of terminology servers did not work, proceed with the fallback
    // option even if it is not reachable
    // because the HL7 validation engine mandates setting a terminology server
    if (this.termServerUrl == null) {
      this.termServerUrl = Config.DEFAULT_VALIDATOR_TERMINOLOGY_SERVER_URL;
      logger.warn(
          "None of the terminology server alternatives was reachable."
          + " Proceeding with the fallback option {}",
          this.termServerUrl);
    }

    // reading definitions
    try {
      validationEngine.loadInitialDefinitions(Config.VALIDATION_DEFINITION_PATH);
    } catch (IOException e) {
      logger.error("IOException occurred while trying to read"
          + " the definitions for the validator", e);
    } catch (SAXException e) {
      logger.error("Improper definition for the validator", e);
    } catch (FHIRException e) {
      logger.error("FHIRException occurred while trying to read"
          + " the definitions for the validator", e);
    }

    // calling the validation engine's connect method for terminology service
    setTerminologyServer(this.termServerUrl);
  }

  /**
   * Sets the Terminology Server Url.
   */
  public void setTerminologyServer(String paramTServerUrl) {
    this.termServerUrl = paramTServerUrl;
    try {
      validationEngine.connectToTSServer(termServerUrl, "", FhirPublication.STU3);
    } catch (URISyntaxException ex) {
      logger.error("Terminology server URL string could not be parsed as a URI reference", ex);
    }
  }

  /**
   * Validates a Bundle.
   */
  public OutputStream validateBundle(Bundle bundle) {
    if (bundle == null) {
      logger.warn("The bundle to be validated is null. Returning null.");
      return null;
    }
    if (bundle.getEntry().isEmpty()) {
      logger.warn("The bundle to be validated is empty. Returning null");
      return null;
    }

    logger.info("Validating the bundle containing " + bundle.getEntry().size() + " entries");

    // create an output stream to return
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    // init the html output
    try {
      outputStream.write("<html>\n\t<body>".getBytes("UTF-8"));
    } catch (IOException e) {
      logger.error("Could not write to the output stream.", e);
    }

    // traverse the entries of the bundle
    for (BundleEntryComponent entry : bundle.getEntry()) {
      if (entry != null && entry.getResource() != null) {
        try {
          // validate the resource contained in the entry
          ByteArrayOutputStream byteArrayOutputStream = 
              (ByteArrayOutputStream) validateResource(entry.getResource());

          if (byteArrayOutputStream != null) {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (byteArray != null) {
              outputStream.write(byteArray);
            }
          }
        } catch (IOException e) {
          logger.error(
              "Exception occurred while trying to write the validation"
              + " outcome to the output stream. Ignoring",
              e);
        }
      } else {
        logger.warn("An entry of the bundle validator was"
            + " running on was found null. Ignoring the entry.");
      }
    }

    // last touch to the html output
    try {
      outputStream.write("\n\t</body>\n</html>".getBytes("UTF-8"));
    } catch (IOException e) {
      logger.error("Could not write to the output stream.", e);
    }

    return outputStream;
  }

  /**
   * Validates the Resource.
   * @param resource Resource
   * @return OutputStream
   */
  public OutputStream validateResource(Resource resource) {
    if (resource == null) {
      logger.warn("The resource to be validated is null. Returning null");
      return null;
    }

    if (resource instanceof Bundle) {
      logger.error(
          "Bundle is not a proper parameter for the method Validator.validateResource."
          + " Use Validator.validateBundle instead.");
      return null;
    }

    logger.info("Validating resource " + resource.getId());

    // set resource
    //this.validationEngine.setSource(this.transformIResource2ByteArray(resource));

    // validate!
    try {
      throw new FHIRException("Not Implemented");
    } catch (FHIRException e) {
      logger.error("Exception occurred while trying to validate the"
          + " FHIR resource. Returning exception message", e);
      String exceptionAsHtml = "<h3>" + resource.getId() + "</h3>"
          + "Exception occurred while validating this resource:<br>" + e.getMessage() + "<hr>";
      try {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(exceptionAsHtml.getBytes("UTF-8"));
        return outputStream;
      } catch (IOException e1) {
        logger.error("Exception occurred while trying to write the"
            + " exception outcome to the output stream. Ignoring ");
      }
    }

    // direct outcome string to an output stream
    ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();

    // notice that html tag is not included in the outcome string
    try {
      String outcomeText = "";
      //this.validationEngine.getOutcome().getText().getDivAsString();
      outcomeText = "<h3>" + resource.getId() + "</h3>" + outcomeText + "<hr>";
      outputStream.write(outcomeText.getBytes("UTF-8"));
    } catch (IOException e) {
      logger.error(
          "Exception occurred while trying to write the validation"
          + " outcome to the output stream. Returning null", e);
      return null;
    }

    return outputStream;
  }

  /**
   * Sends an HTTP GET request to a server to check if the server is available.
   * 
   * @param serverURLString A string that contains the URL of the server
   * @return A boolean indicating if the server is available
   */
  private boolean checkServer(String serverUrlString) {
    try {
      URL url = new URL(serverUrlString);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setConnectTimeout(Config.DEFAULT_VALIDATOR_TERMINOLOGY_SERVER_CHECK_TIMEOUT);
      con.connect();
      if (con.getResponseCode() < 300) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      logger.error("Exception occurred while trying to reach the server at {}", serverUrlString, e);
      return false;
    }
  }

  /**
   * Transforms a FHIR IResource instance to a byte array.
   * 
   * @param paramResource A FHIR IResource instance
   * @return A byte array
   */
  private byte[] transformIResource2ByteArray(Resource paramResource) {
    try {
      return FhirUtil.encodeToXml(paramResource).getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      logger.error("Could not encode resource {} in UTF-8 encoding", paramResource.getId(), e);
    }
    return null;
  }

}
