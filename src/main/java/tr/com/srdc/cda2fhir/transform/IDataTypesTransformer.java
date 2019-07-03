package tr.com.srdc.cda2fhir.transform;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.Base64BinaryType;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Range;
import org.hl7.fhir.dstu3.model.Ratio;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Timing;
import org.hl7.fhir.dstu3.model.UriType;
import org.openhealthtools.mdht.uml.cda.StrucDocText;
import org.openhealthtools.mdht.uml.hl7.datatypes.AD;
import org.openhealthtools.mdht.uml.hl7.datatypes.BIN;
import org.openhealthtools.mdht.uml.hl7.datatypes.BL;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.datatypes.CV;
import org.openhealthtools.mdht.uml.hl7.datatypes.ED;
import org.openhealthtools.mdht.uml.hl7.datatypes.EN;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.INT;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVL_PQ;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVL_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.PIVL_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.PQ;
import org.openhealthtools.mdht.uml.hl7.datatypes.REAL;
import org.openhealthtools.mdht.uml.hl7.datatypes.RTO;
import org.openhealthtools.mdht.uml.hl7.datatypes.ST;
import org.openhealthtools.mdht.uml.hl7.datatypes.TEL;
import org.openhealthtools.mdht.uml.hl7.datatypes.TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.URL;

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

public interface IDataTypesTransformer {

  /**
   * Transforms a CDA AD instance to a FHIR AddressDt composite datatype instance.
   * 
   * @param ad A CDA AD instance
   * @return An Address composite datatype instance
   */
  Address transformAD2Address(AD ad);

  /**
   * Transforms a CDA BIN instance to a FHIR Base64BinaryDt primitive datatype
   * instance.
   * 
   * @param bin A CDA BIN instance
   * @return A Base64Binary primitive datatype instance
   */
  Base64BinaryType transformBin2Base64Binary(BIN bin);

  /**
   * Transforms a CDA BL instance to a FHIR BooleanDt primitive datatype instance.
   * 
   * @param bl A CDA BL instance
   * @return A BooleanType primitive datatype instance
   */
  BooleanType transformBL2Boolean(BL bl);

  /**
   * Transforms a CDA CD instance to a FHIR CodeableConceptDt composite datatype
   * instance. Translations of the CD instance are also included.
   * 
   * @param cd A CDA CD instance
   * @return A CodeableConcept composite datatype instance
   */
  CodeableConcept transformCD2CodeableConcept(CD cd);

  /**
   * Transforms a CDA CD Instance to a FHIR Codeable Concept 
   * utilizing a Concept Map for any Coding values.
   * @param cd CDA CD instance
   * @param includeTranslations Boolean flag to include the Translations in the coding.
   * @param map Concept Map to apply.
   * @return CodeableConcept.
   */
  CodeableConcept transformCD2CodeableConcept(CD cd, boolean includeTranslations, ConceptMap map);

  /**
   * Transforms a CDA CD instance to a FHIR CodeableConceptDt composite datatype
   * instance. Translations of the CD instance are excluded.
   * 
   * @param cd A CDA CD instance
   * @return A CodeableConceptDt composite datatype instance
   */
  CodeableConcept transformCD2CodeableConceptExcludingTranslations(CD cd);



  /**
   * Transforms a CDA CV instance to a FHIR CodingDt composite datatype instance.
   * 
   * @param cv A CDA CV instance
   * @return A Coding composite datatype instance
   */
  Coding transformCV2Coding(CV cv);

  /**
   * Transforms a CDA CV instance to a FHIR CodingDt composite datatype instance.
   * @param cv A CDA CV Instance
   * @param map A ConceptMap to apply to the CV Element.
   * @return A FHIR Coding
   */
  Coding transformCV2Coding(CV cv, ConceptMap map);

 
  /**
   * Transforms a CDA ED instance to a FHIR AttachmentDt composite datatype
   * instance.
   * 
   * @param ed A CDA ED instance
   * @return An AttachmentDt composite datatype instance
   */
  Attachment transformED2Attachment(ED ed);

  /**
   * Transforms a CDA EN instance to a FHIR HumanNameDt composite datatype
   * instance.
   * 
   * @param en A CDA EN instance
   * @return A HumanNameDt composite datatype instance
   */
  HumanName transformEN2HumanName(EN en);

  /**
   * Transforms a CDA II instance to a FHIR IdentifierDt composite datatype
   * instance.
   * 
   * @param ii A CDA II instance
   * @return A IdentifierDt composite datatype instance
   */
  Identifier transformII2Identifier(II ii);

  /**
   * Transforms a CDA INT instance to a FHIR IntegerDt primitive datatype
   * instance.
   * 
   * @param myInt A CDA INT instance
   * @return A IntegerDt primitive datatype instance
   */
  IntegerType transformInt2Integer(INT myInt);

  /**
   * Transforms a CDA IVL_PQ instance to a FHIR RangeDt composite datatype
   * instance.
   * 
   * @param ivlpq A CDA IVL_PQ instance
   * @return A RangeDt composite datatype instance
   */
  Range transformIvl_PQ2Range(IVL_PQ ivlpq);

  /**
   * Transforms a CDA IVL_TS instance to a FHIR PeriodDt composite datatype
   * instance.
   * 
   * @param ivlts A CDA IVL_TS instance
   * @return A PeriodDt composite datatype instance
   */
  Period transformIvl_TS2Period(IVL_TS ivlts);

  /**
   * Transforms a CDA PIVL_TS instance to a FHIR TimingDt composite datatype
   * instance.
   * 
   * @param pivlts A CDA PIVL_TS instance
   * @return A TimingDt composite datatype instance
   */
  Timing transformPivl_TS2Timing(PIVL_TS pivlts);

  /**
   * Transforms a CDA PQ instance to a FHIR QuantityDt composite datatype
   * instance.
   * 
   * @param pq A CDA PQ instance
   * @return A QuantityDt composite datatype instance
   */
  Quantity transformPQ2Quantity(PQ pq);

  /**
   * Transforms a CDA PQ instance to a FHIR SimpleQuantityDt composite datatype
   * instance.
   * 
   * @param pq A CDA PQ instance
   * @return A SimpleQuantityDt composite datatype instance
   */
  SimpleQuantity transformPQ2SimpleQuantity(PQ pq);

  /**
   * Transforms a CDA REAL instance to a FHIR DecimalDt primitive datatype
   * instance.
   * 
   * @param real A CDA REAL instance
   * @return A DecimalDt primitive datatype instance
   */
  DecimalType transformReal2Decimal(REAL real);

  /**
   * Transforms a CDA RTO instance to a FHIR RatioDt composite datatype instance.
   * 
   * @param rto A CDA RTO instance
   * @return A RatioDt composite datatype instance
   */
  Ratio transformRto2Ratio(RTO rto);

  /**
   * Transforms a CDA ST instance to a FHIR StringDt primitive datatype instance.
   * 
   * @param st A CDA ST instance
   * @return A StringDt datatype
   */
  StringType transformST2String(ST st);

  /**
   * Transforms a String that includes a date in CDA format to a FHIR DateTimeDt
   * primitive datatype instance.
   * 
   * @param date A String that includes a date in CDA format
   * @return A DateTimeDt primitive datatype instance
   */
  DateTimeType transformString2DateTime(String date);

  /**
   * Transforms a CDA StrucDocText instance to a FHIR NarrativeDt composite
   * datatype instance.
   * 
   * @param sdt A CDA StrucDocText instance
   * @return A NarrativeDt composite datatype instance
   */
  Narrative transformStrucDocText2Narrative(StrucDocText sdt);

  /**
   * Transforms a CDA TEL instance to a FHIR ContactPointDt composite datatype
   * instance.
   * 
   * @param tel A CDA TEL instance
   * @return A ContactPointDt composite datatype instance
   */
  ContactPoint transformTel2ContactPoint(TEL tel);

  /**
   * Transforms a CDA TS instance to a FHIR DateDt primitive datatype instance.
   * 
   * @param ts A CDA TS instance
   * @return A DateDt primitive datatype instance
   */
  DateType transformTS2Date(TS ts);

  /**
   * Transforms a CDA TS instance to a FHIR DateTimeDt primitive datatype
   * instance.
   * 
   * @param ts A CDA TS instance
   * @return A DateTimeDt primitive datatype instance
   */
  DateTimeType transformTS2DateTime(TS ts);

  /**
   * Transforms a CDA TS instance to a FHIR InstantDt primitive datatype instance.
   * 
   * @param ts A CDA TS instance
   * @return A InstantDt primitive datatype instance
   */
  InstantType transformTS2Instant(TS ts);

  /**
   * Transforms a CDA URL instance to a FHIR UriDt primitive datatype instance.
   * 
   * @param url A CDA URL instance
   * @return A UriDt primitive datatype instance
   */
  UriType transformUrl2Uri(URL url);

}
