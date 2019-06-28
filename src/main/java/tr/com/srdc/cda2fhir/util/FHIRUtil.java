package tr.com.srdc.cda2fhir.util;

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
import ca.uhn.fhir.parser.IParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.hl7.fhir.dstu3.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.cda2fhir.conf.Config;

public class FhirUtil {

  private static IParser jsonParser;
  private static IParser xmlParser;

  private static final Logger logger = LoggerFactory.getLogger(FhirUtil.class);

  static {
    jsonParser = Config.getFhirContext().newJsonParser();
    xmlParser = Config.getFhirContext().newXmlParser();
    jsonParser.setPrettyPrint(true);
    xmlParser.setPrettyPrint(true);
  }

  public static String encodeToJson(Resource res) {
    return jsonParser.encodeResourceToString(res);
  }

  public static String encodeToXml(Resource res) {
    return xmlParser.encodeResourceToString(res);
  }

  public static void printJson(Resource res) {
    System.out.println(jsonParser.encodeResourceToString(res));
  }

  /**
   * Prints a Resource in JSON Format.
   */
  public static void printJson(Resource res, String filePath) {
    File f = new File(filePath);
    f.getParentFile().mkdirs();
    try {
      jsonParser.encodeResourceToWriter(res, new FileWriter(f));
    } catch (IOException e) {
      logger.error("Could not print FHIR JSON to file", e);
    }
  }

  /**
   * Prints a Resource in Json.
   */
  public static void printJson(Resource res, Writer writer) {
    try {
      jsonParser.encodeResourceToWriter(res, writer);
    } catch (IOException e) {
      logger.error("Could not print FHIR JSON to writer", e);
    }
  }

  public static void printXml(Resource res) {
    System.out.println(xmlParser.encodeResourceToString(res));
  }

  /**
   * Prints a Resource as XML.
   */
  public static void printXml(Resource res, Writer writer) {
    try {
      xmlParser.encodeResourceToWriter(res, writer);
    } catch (IOException e) {
      logger.error("Could not print FHIR XML to writer", e);
    }
  }

  /**
   * Prints a Resource in XML.
   */
  public static void printXml(Resource res, String filePath) {
    File f = new File(filePath);
    f.getParentFile().mkdirs();
    try {
      xmlParser.encodeResourceToWriter(res, new FileWriter(f));
    } catch (IOException e) {
      logger.error("Could not print FHIR XML to file", e);
    }
  }

}
