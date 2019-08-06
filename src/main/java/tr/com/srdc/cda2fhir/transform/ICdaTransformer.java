package tr.com.srdc.cda2fhir.transform;

import java.util.List;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.ConceptMap;

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

import org.hl7.fhir.dstu3.model.Reference;

import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import tr.com.srdc.cda2fhir.util.IdGeneratorEnum;

public interface ICdaTransformer {
  /**
   * Returns a ResourceReferenceDt for the patient of the CDA document.
   * 
   * @return A ResourceReferenceDt that references the patient (i.e.
   *         recordTarget/patientRole) of the document
   */
  Reference getPatientRef();

  /**
   * A consistent unique resource id generator.
   * 
   * @return a unique resource id
   */
  String getUniqueId();

  /**
   * Sets the resource id generator format, which is either an incremental COUNTER.
   * or UUID
   * 
   * @param idGen The id generator enumeration to be set
   */
  void setIdGenerator(IdGeneratorEnum idGen);

  /**
   * Transforms a Clinical Document Architecture (CDA) instance to a Bundle of
   * corresponding FHIR resources.
   * 
   * @param cda A ClinicalDocument (CDA) instance to be transformed
   * @return A FHIR Bundle that contains a Composition corresponding to the CDA
   *         document and all other resources that are referenced within the
   *         Composition.
   */
  Bundle transformDocument(ClinicalDocument cda);

  /**
   * Transforms a Clinical Document Architecture Document
   * into an instance of a FHIR Bundle of Resources with a given type.
   * @param cda Clinical Document Instance to transfrom.
   * @param type Bundle Type to return.
   * @return A FHIR Bundle that contains a Composition corresponding to the CDA
   *         document and all other resources that are referenced within the
   *         Composition.
   */
  Bundle transformDocument(ClinicalDocument cda, BundleType type);

  /**
   * Returns the Concept Maps that were passed in during instantiation.
   * @return List of FHIR Concept Maps.
   */
  List<ConceptMap> getMaps();

  
}
