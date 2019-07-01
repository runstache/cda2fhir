package tr.com.srdc.cda2fhir.transform;

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

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.DataFormatException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.Base64BinaryType;
import org.hl7.fhir.dstu3.model.BaseDateTimeType;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupUnmappedMode;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Range;
import org.hl7.fhir.dstu3.model.Ratio;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Timing;
import org.hl7.fhir.dstu3.model.Timing.TimingRepeatComponent;
import org.hl7.fhir.dstu3.model.UriType;

import org.openhealthtools.mdht.uml.cda.StrucDocText;
import org.openhealthtools.mdht.uml.hl7.datatypes.AD;
import org.openhealthtools.mdht.uml.hl7.datatypes.ADXP;
import org.openhealthtools.mdht.uml.hl7.datatypes.BIN;
import org.openhealthtools.mdht.uml.hl7.datatypes.BL;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.datatypes.CV;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.ED;
import org.openhealthtools.mdht.uml.hl7.datatypes.EN;
import org.openhealthtools.mdht.uml.hl7.datatypes.ENXP;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.INT;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVL_PQ;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVL_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.PIVL_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.PQ;
import org.openhealthtools.mdht.uml.hl7.datatypes.PQR;
import org.openhealthtools.mdht.uml.hl7.datatypes.REAL;
import org.openhealthtools.mdht.uml.hl7.datatypes.RTO;
import org.openhealthtools.mdht.uml.hl7.datatypes.ST;
import org.openhealthtools.mdht.uml.hl7.datatypes.SXCM_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.TEL;
import org.openhealthtools.mdht.uml.hl7.datatypes.TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.URL;
import org.openhealthtools.mdht.uml.hl7.vocab.EntityNameUse;
import org.openhealthtools.mdht.uml.hl7.vocab.PostalAddressUse;
import org.openhealthtools.mdht.uml.hl7.vocab.TelecommunicationAddressUse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.com.srdc.cda2fhir.conf.Config;
import tr.com.srdc.cda2fhir.util.StringUtil;

public class DataTypesTransformerImpl implements IDataTypesTransformer, Serializable {

  public static final long serialVersionUID = 2L;

  private IValueSetsTransformer vst = new ValueSetsTransformerImpl();

  private final Logger logger = LoggerFactory.getLogger(DataTypesTransformerImpl.class);

  /**
   * Transforms an AD item to Address.
   * @param ad CDA AD
   * @return Address
   */
  public Address transformAD2Address(AD ad) {
    if (ad == null || ad.isSetNullFlavor()) {
      return null;
    }

    Address address = new Address();

    // use -> use
    if (ad.getUses() != null && !ad.getUses().isEmpty()) {
      // We get the address.type and address.use from the list ad.uses
      for (PostalAddressUse postalAddressUse : ad.getUses()) {
        // If we catch a valid value for type or use, we assign it
        if (postalAddressUse == PostalAddressUse.PHYS || postalAddressUse == PostalAddressUse.PST) {
          address.setType(vst.transformPostalAddressUse2AddressType(postalAddressUse));
        } else if (postalAddressUse == PostalAddressUse.H || postalAddressUse == PostalAddressUse.HP
            || postalAddressUse == PostalAddressUse.WP || postalAddressUse == PostalAddressUse.TMP
            || postalAddressUse == PostalAddressUse.BAD) {
          address.setUse(vst.transformPostalAdressUse2AddressUse(postalAddressUse));
        }
      }
    }

    // text -> text
    if (ad.getText() != null && !ad.getText().isEmpty()) {
      address.setText(ad.getText());
    }

    // streetAddressLine -> line
    if (ad.getStreetAddressLines() != null && !ad.getStreetAddressLines().isEmpty()) {
      for (ADXP adxp : ad.getStreetAddressLines()) {
        if (adxp != null && !adxp.isSetNullFlavor()) {
          address.addLine(adxp.getText());
        }
      }
    }

    // deliveryAddressLine -> line
    if (ad.getDeliveryAddressLines() != null && !ad.getDeliveryAddressLines().isEmpty()) {
      for (ADXP adxp : ad.getDeliveryAddressLines()) {
        if (adxp != null && !adxp.isSetNullFlavor()) {
          address.addLine(adxp.getText());
        }
      }
    }

    // city -> city
    if (ad.getCities() != null && !ad.getCities().isEmpty()) {
      for (ADXP adxp : ad.getCities()) {
        // Asserting that at most one city information exists
        if (adxp != null && !adxp.isSetNullFlavor()) {
          address.setCity(adxp.getText());
        }
      }
    }

    // county -> district
    if (ad.getCounties() != null && !ad.getCounties().isEmpty()) {
      for (ADXP adxp : ad.getCounties()) {
        // Asserting that at most one county information exists
        if (adxp != null && !adxp.isSetNullFlavor()) {
          address.setDistrict(adxp.getText());
        }
      }

    }

    // country -> country
    if (ad.getCountries() != null && !ad.getCountries().isEmpty()) {
      for (ADXP adxp : ad.getCountries()) {
        if (adxp != null && !adxp.isSetNullFlavor()) {
          address.setCountry(adxp.getText());
        }
      }

    }

    // state -> state
    if (ad.getStates() != null && !ad.getStates().isEmpty()) {
      for (ADXP adxp : ad.getStates()) {
        if (adxp != null && !adxp.isSetNullFlavor()) {
          address.setState(adxp.getText());
        }
      }
    }

    // postalCode -> postalCode
    if (ad.getPostalCodes() != null && !ad.getPostalCodes().isEmpty()) {
      for (ADXP adxp : ad.getPostalCodes()) {
        if (adxp != null && !adxp.isSetNullFlavor()) {
          address.setPostalCode(adxp.getText());
        }
      }
    }

    // useablePeriods[0] -> start, usablePeriods[1] -> end
    if (ad.getUseablePeriods() != null && !ad.getUseablePeriods().isEmpty()) {
      Period period = new Period();
      int sxcmCounter = 0;
      for (SXCM_TS sxcmts : ad.getUseablePeriods()) {
        if (sxcmts != null && !sxcmts.isSetNullFlavor()) {
          if (sxcmCounter == 0) {
            period.setStartElement(transformString2DateTime(sxcmts.getValue()));
            sxcmCounter++;
          } else if (sxcmCounter == 1) {
            period.setEndElement(transformString2DateTime(sxcmts.getValue()));
            sxcmCounter++;
          }
        }
      }
      address.setPeriod(period);
    }
    return address;
  }

  /**
   * Converts the CDA BIN item to a Base64BinaryType.
   * @param bin CDA BIN
   * @return Base64BinaryType
   */
  public Base64BinaryType transformBin2Base64Binary(BIN bin) {
    if (bin == null || bin.isSetNullFlavor()) {
      return null;
    }
    if (bin.getRepresentation().getLiteral() != null) {
      // TODO: It doesn't seem convenient. There should be a way to get the value of
      // BIN.
      Base64BinaryType base64BinaryDt = new Base64BinaryType();
      base64BinaryDt.setValue(bin.getRepresentation().getLiteral().getBytes());
      return base64BinaryDt;
    } else {
      return null;
    }

  }

  /**
   * Transforms the CDA BL to a Boolean Type.
   */
  public BooleanType transformBL2Boolean(BL bl) {
    return (bl == null || bl.isSetNullFlavor()) ? null : new BooleanType(bl.getValue());
  }

  /**
   * Transforms the CDA CD to a Codeable Concept.
   */
  public CodeableConcept transformCD2CodeableConcept(CD cd) {
    CodeableConcept myCodeableConcept = transformCD2CodeableConceptExcludingTranslations(cd);

    if (myCodeableConcept == null) {
      return null;
    }

    // translation
    if (cd.getTranslations() != null && !cd.getTranslations().isEmpty()) {
      for (CD myCd : cd.getTranslations()) {
        Coding coding = new Coding();
        boolean isEmpty = true;

        // codeSystem -> system
        if (myCd.getCodeSystem() != null && !myCd.getCodeSystem().isEmpty()) {
          coding.setSystem(vst.transformOid2Url(myCd.getCodeSystem()));
          isEmpty = false;
        }

        // code -> code
        if (myCd.getCode() != null && !myCd.getCode().isEmpty()) {
          coding.setCode(myCd.getCode());
          isEmpty = false;
        }

        // codeSystemVersion -> version
        if (myCd.getCodeSystemVersion() != null && !myCd.getCodeSystemVersion().isEmpty()) {
          coding.setVersion(myCd.getCodeSystemVersion());
          isEmpty = false;
        }

        // displayName -> display
        if (myCd.getDisplayName() != null && !myCd.getDisplayName().isEmpty()) {
          coding.setDisplay(myCd.getDisplayName());
          isEmpty = false;
        }

        if (isEmpty == false) {
          myCodeableConcept.addCoding(coding);
        }          
      }
    }

    return myCodeableConcept;
  }

  /**
   * Transforms CDA CD to Codeable Concept.
   */
  public CodeableConcept transformCD2CodeableConceptExcludingTranslations(CD cd) {
    if (cd == null || cd.isSetNullFlavor()) {
      return null;      
    }
    
    CodeableConcept myCodeableConcept = new CodeableConcept();

    // .
    Coding coding = new Coding();
    boolean isEmpty = true;

    // codeSystem -> system
    if (cd.getCodeSystem() != null && !cd.getCodeSystem().isEmpty()) {
      coding.setSystem(vst.transformOid2Url(cd.getCodeSystem()));
      isEmpty = false;
    }

    // code -> code
    if (cd.getCode() != null && !cd.getCode().isEmpty()) {
      coding.setCode(cd.getCode());
      isEmpty = false;
    }

    // codeSystemVersion -> version
    if (cd.getCodeSystemVersion() != null && !cd.getCodeSystemVersion().isEmpty()) {
      coding.setVersion(cd.getCodeSystemVersion());
      isEmpty = false;
    }

    // displayName -> display
    if (cd.getDisplayName() != null && !cd.getDisplayName().isEmpty()) {
      coding.setDisplay(cd.getDisplayName());
      isEmpty = false;
    }

    if (!isEmpty) {
      myCodeableConcept.addCoding(coding);
      return myCodeableConcept;
    } else {
      return null;
    }
  }

  /**
   * converts CDA CV to Coding.
   * @param cv CDA CV
   * @return Coding.
   */
  public Coding transformCV2Coding(CV cv) {
    if (cv == null || cv.isSetNullFlavor()) {
      return null;
    }

    Coding coding = new Coding();

    // codeSystem -> system
    if (cv.getCodeSystem() != null && !cv.getCodeSystem().isEmpty()) {
      coding.setSystem(cv.getCodeSystem());
    }

    // codeSystemVersion -> version
    if (cv.getCodeSystemVersion() != null && !cv.getCodeSystemVersion().isEmpty()) {
      coding.setVersion(cv.getCodeSystemVersion());
    }

    // code -> code
    if (cv.getCode() != null && !cv.getCode().isEmpty()) {
      coding.setCode(cv.getCode());
    }

    // displayName -> display
    if (cv.getDisplayName() != null && !cv.getDisplayName().isEmpty()) {
      coding.setDisplay(cv.getDisplayName());
    }
    return coding;
  }

  /**
   * Transforms a CDA CV Section to a Coding Value using a Concept Map.
   * @param cv CDA CV Item
   * @param map Concept Maps to apply.
   * @return FHIR Coding
   */
  public Coding transformCV2Coding(CV cv, ConceptMap map) {

    if (cv == null || cv.isSetNullFlavor()) {
      return null;
    }
    Coding coding = transformCV2Coding(cv);
    if (coding == null) {
      coding = new Coding();
    }
    applyConceptMap(coding, map);
    return coding;
  }


  /**
   * Applies a FHIR Concept Map the provided Entity.
   * @param <T> Entity Type
   * @param entity Entity to apply Concept Map to.
   * @param map Concept Map to apply
   * @return The Entity with the applied concept map.
   */
  public <T> T applyConceptMap(T entity, ConceptMap map) {

    for (ConceptMapGroupComponent group : map.getGroup()) {
      Method[] methods = entity.getClass().getMethods();
      for (int i = 0; i < methods.length; i++) {
        Method method = methods[i];
        if (method.getName().equalsIgnoreCase("get" + group.getSource())) {
          Method[] targetMethods = entity.getClass().getMethods();
          for (int j = 0; j < targetMethods.length; j++) {          
            Method targetMethod = targetMethods[j];
            if (targetMethod.getName().equalsIgnoreCase("set" + group.getTarget())) {            
              try {            
                final String value = method.invoke(entity).toString();            
                Optional<SourceElementComponent> source = 
                    group.getElement()
                        .stream()
                        .filter(c -> c.getCode().equalsIgnoreCase(value))
                        .findFirst();
                if (source.isPresent()) {
                  targetMethod.invoke(entity, source.get().getTargetFirstRep().getCode());
                } else {
                  //Process the unmapped
                  if (group.getUnmapped() != null 
                      && !group.getUnmapped()
                          .getMode()
                          .equals(ConceptMapGroupUnmappedMode.PROVIDED)) {
                    targetMethod.invoke(entity, group.getUnmapped().getCode());
                    
                  } else {
                    targetMethod.invoke(entity, value);
                  }
                }
              } catch (Exception ex) {
                logger.warn("Unable to apply Concept Map: " + ex.getMessage());
              }
            }
          }
          break;
        }
      }
    }
    return entity;
  }
  /**
   * Transforms a CDA ED to an Attachment.
   * @param ed CDA ED
   * @return Attachments
   */
  public Attachment transformED2Attachment(ED ed) {
    if (ed == null || ed.isSetNullFlavor()) {
      return null;
    }

    Attachment attachment = new Attachment();

    // mediaType -> contentType
    if (ed.isSetMediaType() && ed.getMediaType() != null && !ed.getMediaType().isEmpty()) {
      attachment.setContentType(ed.getMediaType());
    }

    // language -> language
    if (ed.getLanguage() != null && !ed.getLanguage().isEmpty()) {
      attachment.setLanguage(ed.getLanguage());
    }

    // text.bytes -> data
    if (ed.getText() != null && !ed.getText().isEmpty()) {
      if (ed.getText().getBytes() != null) {
        attachment.setData(ed.getText().getBytes());
      }
    }

    // reference.value -> url
    if (ed.getReference() != null && !ed.getReference().isSetNullFlavor()) {
      if (ed.getReference().getValue() != null && !ed.getReference().getValue().isEmpty()) {
        attachment.setUrl(ed.getReference().getValue());
      }
    }

    // integrityCheck -> hash
    if (ed.getIntegrityCheck() != null) {
      attachment.setHash(ed.getIntegrityCheck());
    }
    return attachment;
  }

  /**
   * Transforms a CDA EN Entitiy to a Human Name.
   * @param en CDA EN Entity.
   * @return Human Name
   */
  public HumanName transformEN2HumanName(EN en) {
    if (en == null || en.isSetNullFlavor()) {
      return null;
    }

    HumanName myHumanName = new HumanName();

    // text -> text
    if (en.getText() != null && !en.getText().isEmpty()) {
      myHumanName.setText(en.getText());
    }

    // use -> use
    if (en.getUses() != null && !en.getUses().isEmpty()) {
      for (EntityNameUse entityNameUse : en.getUses()) {
        if (entityNameUse != null) {
          myHumanName.setUse(vst.transformEntityNameUse2NameUse(entityNameUse));
        }
      }
    }

    // family -> family
    if (en.getFamilies() != null && !en.getFamilies().isEmpty()) {
      for (ENXP family : en.getFamilies()) {
        myHumanName.setFamily(family.getText());
      }
    }

    // given -> given
    if (en.getGivens() != null && !en.getGivens().isEmpty()) {
      for (ENXP given : en.getGivens()) {
        myHumanName.addGiven(given.getText());
      }
    }

    // prefix -> prefix
    if (en.getPrefixes() != null && !en.getPrefixes().isEmpty()) {
      for (ENXP prefix : en.getPrefixes()) {
        myHumanName.addPrefix(prefix.getText());
      }
    }

    // suffix -> suffix
    if (en.getSuffixes() != null && !en.getSuffixes().isEmpty()) {
      for (ENXP suffix : en.getSuffixes()) {
        myHumanName.addSuffix(suffix.getText());
      }
    }

    // validTime -> period
    if (en.getValidTime() != null && !en.getValidTime().isSetNullFlavor()) {
      myHumanName.setPeriod(transformIvl_TS2Period(en.getValidTime()));
    }
    return myHumanName;
  }

  /**
   * Transforms a CDA II Entity to an Identifier.
   * @param ii CDA II Entity.
   * @return Identifier
   */
  public Identifier transformII2Identifier(II ii) {
    if (ii == null || ii.isSetNullFlavor()) {
      return null;
    }

    Identifier identifier = new Identifier();

    // if both root and extension are present, then
    // root -> system
    // extension -> value
    if (ii.getRoot() != null 
        && !ii.getRoot().isEmpty() 
        && ii.getExtension() != null 
        && !ii.getExtension().isEmpty()) {
      // root is oid
      if (StringUtil.isOid(ii.getRoot())) {
        identifier.setSystem("urn:oid:" + ii.getRoot());
      } else if (StringUtil.isUuid(ii.getRoot())) {
        // root is uuid
        identifier.setSystem("urn:uuid:" + ii.getRoot());
      } else {
        identifier.setSystem(ii.getRoot());
      }
      identifier.setValue(ii.getExtension());
    } else if (ii.getRoot() != null && !ii.getRoot().isEmpty()) {
      // else if only the root is present, then
      // root -> value
      identifier.setValue(ii.getRoot());
    } else if (ii.getExtension() != null && !ii.getExtension().isEmpty()) {
      // this is not very likely but, if there is only the extension, then
      // extension -> value
      identifier.setValue(ii.getExtension());
    }
    return identifier;

  }

  /**
   * Tranforms an Int to and IntegerType.
   * @param myInt Integer value.
   * @return IntegerType
   */
  public IntegerType transformInt2Integer(INT myInt) {
    return (myInt == null || myInt.isSetNullFlavor() || myInt.getValue() == null) ? null
        : new IntegerType(myInt.getValue().toString());
  }

  /**
   * Transform CDA PQ to a Range.
   * @param ivlpq CDA IVL PQ
   * @return Range
   */
  public Range transformIvl_PQ2Range(IVL_PQ ivlpq) {
    if (ivlpq == null || ivlpq.isSetNullFlavor()) {
      return null;
    }

    Range range = new Range();

    // low -> low
    if (ivlpq.getLow() != null && !ivlpq.getLow().isSetNullFlavor()) {
      range.setLow(transformPQ2SimpleQuantity(ivlpq.getLow()));
    }

    // high -> high
    if (ivlpq.getHigh() != null && !ivlpq.getHigh().isSetNullFlavor()) {
      range.setHigh(transformPQ2SimpleQuantity(ivlpq.getHigh()));
    }

    // low is null, high is null and the value is carrying the low value
    // value -> low
    if (ivlpq.getLow() == null && ivlpq.getHigh() == null && ivlpq.getValue() != null) {
      SimpleQuantity low = new SimpleQuantity();
      low.setValue(ivlpq.getValue());
      range.setLow(low);
    }
    return range;
  }

  /**
   * Transforms CDA IVL TS Entity to a Period.
   * @param ivlts CDA IVL_TS Entity.
   * @return Period.
   */
  public Period transformIvl_TS2Period(IVL_TS ivlts) {
    if (ivlts == null || ivlts.isSetNullFlavor()) {
      return null;
    }

    Period period = new Period();

    // low -> start
    if (ivlts.getLow() != null && !ivlts.getLow().isSetNullFlavor()) {
      String date = ivlts.getLow().getValue();
      period.setStartElement(transformString2DateTime(date));
    }

    // high -> end
    if (ivlts.getHigh() != null && !ivlts.getHigh().isSetNullFlavor()) {
      String date = ivlts.getHigh().getValue();
      period.setEndElement(transformString2DateTime(date));
    }

    // low is null, high is null and the value is carrying the low value
    // value -> low
    if (ivlts.getLow() == null && ivlts.getHigh() == null 
        && ivlts.getValue() != null 
        && !ivlts.getValue().equals("")) {
      period.setStartElement(transformString2DateTime(ivlts.getValue()));
    }
    return period;
  }

  /**
   * Transforms CDA PIVL TS Entity to a Timing Type.
   * @param pivlts CDA PIVL TS Entity
   * @return Timing
   */
  public Timing transformPivl_TS2Timing(PIVL_TS pivlts) {
    // http://wiki.hl7.org/images/c/ca/Medication_Frequencies_in_CDA.pdf
    // http://www.cdapro.com/know/24997
    if (pivlts == null || pivlts.isSetNullFlavor()) {
      return null;
    }

    Timing timing = new Timing();

    // period -> period
    if (pivlts.getPeriod() != null && !pivlts.getPeriod().isSetNullFlavor()) {
      TimingRepeatComponent repeat = new TimingRepeatComponent();
      timing.setRepeat(repeat);
      // period.value -> repeat.period
      if (pivlts.getPeriod().getValue() != null) {
        repeat.setPeriod(pivlts.getPeriod().getValue());
      }
      // period.unit -> repeat.periodUnits
      if (pivlts.getPeriod().getUnit() != null) {
        repeat.setPeriodUnit(vst.transformPeriodUnit2UnitsOfTime(pivlts.getPeriod().getUnit()));
      }

      // phase -> repeat.bounds
      if (pivlts.getPhase() != null && !pivlts.getPhase().isSetNullFlavor()) {
        repeat.setBounds(transformIvl_TS2Period(pivlts.getPhase()));
      }
    }
    return timing;
  }

  /**
   * Transforms a CDA PQ Entity to a Quantity.
   * @param pq CDA PQ Entitiy.
   * @return Quantity
   */
  public Quantity transformPQ2Quantity(PQ pq) {
    if (pq == null || pq.isSetNullFlavor()) {
      return null;
    }

    Quantity quantity = new Quantity();

    // value -> value
    if (pq.getValue() != null) {
      quantity.setValue(pq.getValue());
    }

    // unit -> unit
    if (pq.getUnit() != null && !pq.getUnit().isEmpty()) {
      quantity.setUnit(pq.getUnit());
    }

    // translation -> system & code
    for (PQR pqr : pq.getTranslations()) {
      if (pqr != null && !pqr.isSetNullFlavor()) {
        // codeSystem -> system
        if (pqr.getCodeSystem() != null && !pqr.getCodeSystem().isEmpty()) {
          quantity.setSystem(pqr.getCodeSystem());
        }

        // code -> code
        if (pqr.getCode() != null && !pqr.getCode().isEmpty()) {
          quantity.setCode(pqr.getCode());
        }
      }
    }
    return quantity;
  }

  /**
   * Transforms CDA PQ to a Simple Quantity.
   * @param pq CDA PQ Entity
   * @return Simple Quantity
   */
  public SimpleQuantity transformPQ2SimpleQuantity(PQ pq) {
    if (pq == null || pq.isSetNullFlavor()) {
      return null;
    }

    SimpleQuantity simpleQuantity = new SimpleQuantity();

    // value -> value
    if (pq.getValue() != null) {
      simpleQuantity.setValue(pq.getValue());
    }

    // unit -> unit
    if (pq.getUnit() != null && !pq.getUnit().isEmpty()) {
      simpleQuantity.setUnit(pq.getUnit());
    }

    // translation -> system and code
    if (pq.getTranslations() != null && !pq.getTranslations().isEmpty()) {
      for (org.openhealthtools.mdht.uml.hl7.datatypes.PQR pqr : pq.getTranslations()) {
        if (pqr != null && !pqr.isSetNullFlavor()) {
          // codeSystem -> system
          if (pqr.getCodeSystem() != null && !pqr.getCodeSystem().isEmpty()) {
            simpleQuantity.setSystem(vst.transformOid2Url(pqr.getCodeSystem()));
          }

          // code -> code
          if (pqr.getCode() != null && !pqr.getCode().isEmpty()) {
            simpleQuantity.setCode(pqr.getCode());
          }
        }
      }
    }
    return simpleQuantity;
  }

  /**
   * Transforms a CDA REAL Entity to a Deciaml Type.
   * @param real CDA REAL Entity.
   * @return Deciaml Type.
   */
  public DecimalType transformReal2Decimal(REAL real) {
    return (real == null 
      || real.isSetNullFlavor() 
      || real.getValue() == null) ? null : new DecimalType(real.getValue());
  }

  /**
   * Transforms a CDA RTO Entity to a Ratio.
   * @param rto CDA RTO Entity.
   * @return Ratio
   */
  public Ratio transformRto2Ratio(RTO rto) {
    if (rto == null || rto.isSetNullFlavor()) {
      return null;
    }
    Ratio myRatio = new Ratio();

    // numerator -> numerator
    if (rto.getNumerator() != null && !rto.getNumerator().isSetNullFlavor()) {
      Quantity quantity = new Quantity();
      REAL numerator = (REAL) rto.getNumerator();
      if (numerator.getValue() != null) {
        quantity.setValue(numerator.getValue().doubleValue());
        myRatio.setNumerator(quantity);
      }
    }

    // denominator -> denominator
    if (!rto.getDenominator().isSetNullFlavor()) {
      Quantity quantity = new Quantity();
      REAL denominator = (REAL) rto.getDenominator();
      if (denominator.getValue() != null) {
        quantity.setValue(denominator.getValue().doubleValue());
        myRatio.setDenominator(quantity);
      }
    }
    return myRatio;
  }

  /**
   * Transforms a CDA ST Entity to a StringType.
   */
  public StringType transformST2String(ST st) {
    return (st == null || st.isSetNullFlavor() 
        || st.getText() == null) ? null : new StringType(st.getText());
  }

  /**
   * Transforms a String to a DateTimeType.
   * @param date String Date
   * @return DateTime Type.
   */
  public DateTimeType transformString2DateTime(String date) {
    TS ts = DatatypesFactory.eINSTANCE.createTS();
    ts.setValue(date);
    return transformTS2DateTime(ts);
  }

  /**
   * Transforms a CDA Stuct Doc Text to a Narrative.
   * @param sdt CDA Stuct Doc Text.
   * @return Narrative.
   */
  public Narrative transformStrucDocText2Narrative(StrucDocText sdt) {
    if (sdt != null) {
      Narrative narrative = new Narrative();
      String narrativeDivString = transformStrucDocText2String(sdt);

      try {        
        narrative.setDivAsString(narrativeDivString);
      } catch (DataFormatException e) {
        return null;
      }
      narrative.setStatus(NarrativeStatus.ADDITIONAL);
      return narrative;
    }
    return null;
  }

  /**
   * Tranforms a Tel Entity to a Contact Point.
   * @param tel CDA Tel Entity.
   * @return Contact Point.
   */
  public ContactPoint transformTel2ContactPoint(TEL tel) {
    if (tel == null || tel.isSetNullFlavor()) {
      return null;
    }

    ContactPoint contactPoint = new ContactPoint();

    // value and system -> value
    if (tel.getValue() != null && !tel.getValue().isEmpty()) {
      String value = tel.getValue();
      String[] systemType = value.split(":");

      // for the values in form tel:+1(555)555-1000
      if (systemType.length > 1) {
        ContactPointSystem contactPointSystem = 
            vst.transformTelValue2ContactPointSystem(systemType[0]);
        // system
        if (contactPointSystem != null) {
          contactPoint.setSystem(contactPointSystem);
        } else {          
          contactPoint.setSystem(Config.DEFAULT_CONTACT_POINT_SYSTEM);
        }
        // value
        contactPoint.setValue(systemType[1]);
      } else if (systemType.length == 1) {
        // for the values in form +1(555)555-5000
        contactPoint.setValue(systemType[0]);
        // configurable default system value
        contactPoint.setSystem(Config.DEFAULT_CONTACT_POINT_SYSTEM);
      }
    }

    // useablePeriods -> period
    if (tel.getUseablePeriods() != null && !tel.getUseablePeriods().isEmpty()) {
      Period period = new Period();
      int sxcmCounter = 0;
      for (SXCM_TS sxcmts : tel.getUseablePeriods()) {
        if (sxcmts != null && !sxcmts.isSetNullFlavor()) {
          // useablePeriods[0] -> period.start
          // useablePeriods[1] -> period.end
          if (sxcmCounter == 0) {
            if (sxcmts.getValue() != null && !sxcmts.getValue().isEmpty()) {
              period.setStartElement(transformString2DateTime(sxcmts.getValue()));
            }
          } else if (sxcmCounter == 1) {
            if (sxcmts.getValue() != null && !sxcmts.getValue().isEmpty()) {
              period.setEndElement(transformString2DateTime(sxcmts.getValue()));
            }
          }
          sxcmCounter++;
        }
      }
      contactPoint.setPeriod(period);
    }

    // use -> use
    if (tel.getUses() != null && !tel.getUses().isEmpty()) {
      for (TelecommunicationAddressUse telAddressUse : tel.getUses()) {
        if (telAddressUse != null) {
          contactPoint.setUse(
                vst.transformTelecommunicationAddressUse2ContactPointUse(telAddressUse));
        }
      }
    }
    return contactPoint;
  }

  /**
   * Transforms CDA TS Entity to DateType.
   */
  public DateType transformTS2Date(TS ts) {
    DateType date = (DateType) transformTS2BaseDateTime(ts, DateType.class);
    if (date == null) {
      return null;
    }

    // TimeZone is NOT permitted
    if (date.getTimeZone() != null) {
      date.setTimeZone(null);
    }

    // precision should be YEAR, MONTH or DAY. otherwise, set it to DAY
    if (date.getPrecision() != TemporalPrecisionEnum.YEAR 
        && date.getPrecision() != TemporalPrecisionEnum.MONTH
        && date.getPrecision() != TemporalPrecisionEnum.DAY) {
      date.setPrecision(TemporalPrecisionEnum.DAY);
    }

    return date;
  }

  /**
   * Transforms a CDA TS Entity to a DateTimeType.
   * @param ts CDA TS Entity.
   * @return DateTimeType
   */
  public DateTimeType transformTS2DateTime(TS ts) {
    DateTimeType dateTime = (DateTimeType) transformTS2BaseDateTime(ts, DateTimeType.class);

    if (dateTime == null) {
      return null;
    }

    // if the precision is not YEAR or MONTH, TimeZone SHALL be populated
    if (dateTime.getPrecision() != TemporalPrecisionEnum.YEAR
        && dateTime.getPrecision() != TemporalPrecisionEnum.MONTH) {
      if (dateTime.getTimeZone() == null) {
        dateTime.setTimeZone(TimeZone.getDefault());
      }
    }

    // if the precision is MINUTE, seconds SHALL be populated
    if (dateTime.getPrecision() == TemporalPrecisionEnum.MINUTE) {
      dateTime.setPrecision(TemporalPrecisionEnum.SECOND);
      dateTime.setSecond(0);
    }

    return dateTime;
  }

  /**
   * Transforms a CDA TS Entity to a Instant Type.
   * @param ts CDA TS Entity.
   * @return Instant Type.
   */
  public InstantType transformTS2Instant(TS ts) {
    InstantType instant = (InstantType) transformTS2BaseDateTime(ts, InstantType.class);
    if (instant == null) {
      return null;
    }

    // if the precision is not SECOND or MILLI, convert its precision to SECOND
    if (instant.getPrecision() != TemporalPrecisionEnum.SECOND
        && instant.getPrecision() != TemporalPrecisionEnum.MILLI) {
      instant.setPrecision(TemporalPrecisionEnum.SECOND);
    }

    // if it doesn't include a timezone, add the local timezone
    if (instant.getTimeZone() == null) {
      instant.setTimeZone(TimeZone.getDefault());
    }
    return instant;
  }

  /**
   * Transforms a URL to Uri Type.
   * @param url URL
   * @return UriType
   */
  public UriType transformUrl2Uri(URL url) {
    return (url == null 
        || url.isSetNullFlavor() 
        || url.getValue() == null) ? null : new UriType(url.getValue());
  }

  // Helper Methods
  /**
   * Extracts the attributes of an HTML element This method is the helper for the
   * method getTags, which is already a helper for tStrucDocText2String.
   * 
   * @param entry A EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry
   *              instance
   * @return A Java String list containing the attributes of an HTML element in
   *         form: attributeName="attributeValue". Each element corresponds to
   *         distinct attributes for the same tag
   */
  private List<String> getAttributesHelperForTStructDocText2String(
      EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry entry) {
    if (entry == null) {
      return null;
    }

    List<String> attributeList = new ArrayList<String>();
    if (entry.getValue() 
        instanceof org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl) {
      for (FeatureMap.Entry attribute : 
          ((org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl) entry.getValue())
          .getAnyAttribute()) {
        String name = attribute.getEStructuralFeature().getName();
        String value = attribute.getValue().toString();
        if (name != null && !name.isEmpty()) {
          String attributeToAdd = "";
          // we may have attributes which doesn't have any value
          attributeToAdd = attributeToAdd + name;
          if (value != null && !value.isEmpty()) {
            attributeToAdd = attributeToAdd + "=\"" + value + "\"";
          }
          attributeList.add(attributeToAdd);
        }
      }
    }
    return attributeList;
  }

  /**
   * Extracts the tags and the attributes of an HTML element. Also, this method
   * transforms the CDA formatted tags to HTML formatted tags. This method is the
   * helper for the method tStrucDocText2String.
   * 
   * @param entry A EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry
   *              instance
   * @return A Java String list containing the start tag and end tag of an HTML
   *         element in form: &lt;tagName attribute="attributeValue"&gt;. While
   *         first element of the list correspons to the start tag, second element
   *         of the list corresponds to the end tag.
   */
  private List<String> getTagsHelperForTStructDocText2String(
      org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry entry) {
    if (entry == null) {
      return null;
    }

    String tagName = entry.getEStructuralFeature().getName();
    if (tagName == null || tagName.equals("")) {
      return null;
    }
    List<String> attributeList = getAttributesHelperForTStructDocText2String(entry);
    

    String attributeToRemove = null;

    // removing id attribute from the attributeList
    for (String attribute : attributeList) {
      if (attribute.toLowerCase().startsWith("id=\"", 0)) {
        attributeToRemove = attribute;
      }
    }

    if (attributeToRemove != null) {
      attributeList.remove(attributeToRemove);
    }

    // removing styleCode attribute from the attributeList
    for (String attribute : attributeList) {
      if (attribute.toLowerCase().startsWith("stylecode=\"", 0)) {
        attributeToRemove = attribute;
      }
    }
    if (attributeToRemove != null) {
      attributeList.remove(attributeToRemove);
    }

    // case tag.equals("list"). we need to transform it to "ul" or "ol"
    if (tagName.equals("list")) {
      // first, think of the situtation no attribute exists about ordered/unordered
      tagName = "ul";
      attributeToRemove = null;
      for (String attribute : attributeList) {
        // if the attribute is listType, make the transformation
        if (attribute.toLowerCase().contains("listtype")) {
          // notice that the string "unordered" also contains "ordered"
          // therefore, it is vital to check "unordered" firstly.
          // if "unordered" is not contained by the attribute, then we may check for
          // "ordered"
          if (attribute.toLowerCase().contains("unordered")) {
            tagName = "ul";
          } else if (attribute.toLowerCase().contains("ordered")) {
            tagName = "ol";
          }
          attributeToRemove = attribute;
        }
      }
      // if we found the "listType" attribute, we assigned it to attributeToRemove
      // from now on, we have nothing to do with this attribute. let's remove it from
      // the list.
      if (attributeToRemove != null) {
        attributeList.remove(attributeToRemove);
      }
    } else {
      switch (tagName.toLowerCase()) {
        case "paragraph":
          tagName = "p";
          break;
        case "content":
          tagName = "span";
          break;
        case "item":
          tagName = "li";
          break;
        case "linkhtml":
          tagName = "a";
          break;
        case "renderMultimedia":
          tagName = "img";
          break;
        case "list":
          tagName = "ul";
          break;
        default: // do nothing. let the tagName be as it is
          break;
      }
    }
    String startTag = "";
    
    // now, it is time to prepare our tag by using tagName and attributes
    startTag = "<" + tagName;
    // adding attributes to the start tag
    for (String attribute : attributeList) {
      startTag += " " + attribute;
    }
    String endTag = "";
    // closing the start tag
    startTag += ">";
    endTag = "</" + tagName + ">";

    List<String> tagList = new ArrayList<String>();
    // 1st element of the returning list: startTag
    tagList.add(startTag);
    // 2nd element of the returning list: endTag
    tagList.add(endTag);

    return tagList;
  }

  /**
   * Transforms A CDA StructDocText instance to a Java String containing the
   * transformed text. Since the method is a recursive one and handles with
   * different types of object, parameter is taken as Object. However, parameters
   * of type StructDocText should be given by the caller.
   * 
   * @param param A CDA StructDocText instance
   * @return A Java String containing the transformed text
   */
  private String transformStrucDocText2String(Object param) {
    if (param instanceof org.openhealthtools.mdht.uml.cda.StrucDocText) {
      org.openhealthtools.mdht.uml.cda.StrucDocText paramStrucDocText = 
          (org.openhealthtools.mdht.uml.cda.StrucDocText) param;
      return "<div>" + transformStrucDocText2String(paramStrucDocText.getMixed()) + "</div>";
    } else if (param instanceof BasicFeatureMap) {
      String returnValue = "";
      for (Object object : (BasicFeatureMap) param) {
        String pieceOfReturn = transformStrucDocText2String(object);
        if (pieceOfReturn != null && !pieceOfReturn.isEmpty()) {
          returnValue = returnValue + pieceOfReturn;
        }
      }
      return returnValue;
    } else if (param instanceof EStructuralFeatureImpl.SimpleFeatureMapEntry) {
      String elementBody = 
          ((EStructuralFeatureImpl.SimpleFeatureMapEntry) param).getValue().toString();
      // deletion of unnecessary content (\n, \t)
      elementBody = elementBody.replaceAll("\n", "").replaceAll("\t", "");

      // replacement of special characters
      elementBody = 
          elementBody.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;");
      // if there was a well-formed char sequence "&amp;", after replacement it will
      // transform to &amp;amp;
      // the following line of code will remove these type of typos
      elementBody = elementBody.replaceAll("&amp;amp;", "&amp;");

      String typeName = 
          ((EStructuralFeatureImpl.SimpleFeatureMapEntry) param).getEStructuralFeature().getName();
      typeName = typeName.toLowerCase();
      if (typeName.equals("comment")) {
        return "<!-- " + elementBody + " -->";
      } else if (typeName.equals("text")) {
        return elementBody;
      } else {
        logger.warn(
            "Unknown element type was found while transforming a" 
            + " StrucDocText instance to Narrative. Returning the value of the element");
        return elementBody;
      }
    } else if (param instanceof EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry) {
      EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry entry = 
          (EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry) param;
      List<String> tagList = getTagsHelperForTStructDocText2String(entry);
      return tagList.get(0) + transformStrucDocText2String(entry.getValue()) + tagList.get(1);
    } else if (param instanceof org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl) {
      // since the name and the attributes are taken already, we just send the mixed
      // of anyTypeImpl
      return transformStrucDocText2String(((org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl) param)
          .getMixed());
    } else {
      logger.warn("Parameter for the method tStrucDocText2String is unknown. Returning null", 
          param.getClass());
      return null;
    }
  }

  /**
   * Transforms a CDA TS instance or a string including the date information in
   * CDA format to a FHIR BaseDateTimeDt primitive datatype instance. Since
   * BaseDateTimeDt is an abstract class, the second parameter of this method
   * (Class&lt;?&gt; classOfReturningObject) determines the class that initiates
   * the BaseDateTimeDt object the method is to return.
   * 
   * @param tsObject               A CDA TS instance or a Java String including
   *                               the date information in CDA format
   * @param classOfReturningObject A FHIR class that determines the initiater for
   *                               the BaseDateTimeDt object the method is to
   *                               return. DateDt.class, DateTimeDt.class or
   *                               InstantDt.class are expected.
   * @return A FHIR BaseDateTimeDt primitive datatype instance
   */
  private BaseDateTimeType transformTS2BaseDateTime(
        Object tsObject, Class<?> classOfReturningObject) {
    if (tsObject == null) {
      return null;
    }

    String dateString;
    // checking the type of tsObject, assigning dateString accordingly
    if (tsObject instanceof TS) {
      // null-flavor check
      if (((TS) tsObject).isSetNullFlavor() || ((TS) tsObject).getValue() == null) {
        return null;
      } else {
        dateString = ((TS) tsObject).getValue();
      }
    } else if (tsObject instanceof String) {
      dateString = (String) tsObject;
    } else {
      // unexpected situtation
      // 1st parameter of this method should be either an instanceof TS or String
      return null;
    }

    BaseDateTimeType date;
    // initializing date
    if (classOfReturningObject == DateType.class) {
      date = new DateType();
    } else if (classOfReturningObject == DateTimeType.class) {
      date = new DateTimeType();
    } else if (classOfReturningObject == InstantType.class) {
      date = new InstantType();
    } else {
      // unexpected situtation
      // caller of this method must have a need of DateDt, DateTimeDt or InstantDt
      // otherwise, the returning object will be of type DateDt
      date = new DateType();
    }

    /*
     * Possible date forms YYYY: year YYYYMM: year month YYYYMMDD: year month day
     * YYYYMMDDHHMM: year month day hour minute YYYYMMDDHHMMSS.S: year month day
     * hour minute second YYYYMMDDHHMM+TIZO: year month day hour minute timezone
     */

    TemporalPrecisionEnum precision = null;
    TimeZone timeZone = null;

    // getting the timezone
    // once got the timezone, crop the timezone part from the string
    if (dateString.contains("+")) {
      timeZone = TimeZone.getTimeZone("GMT" + dateString.substring(dateString.indexOf('+')));
      dateString = dateString.substring(0, dateString.indexOf('+'));
    } else if (dateString.contains("-")) {
      timeZone = TimeZone.getTimeZone("GMT" + dateString.substring(dateString.indexOf('-')));
      dateString = dateString.substring(0, dateString.indexOf('-'));
    }

    // determining precision
    switch (dateString.length()) {
      case 4: // yyyy
        precision = TemporalPrecisionEnum.YEAR;
        break;
      case 6: // yyyymm
        precision = TemporalPrecisionEnum.MONTH;
        break;
      case 8: // yyyymmdd
        precision = TemporalPrecisionEnum.DAY;
        break;
      case 12: // yyyymmddhhmm
        precision = TemporalPrecisionEnum.MINUTE;
        break;
      case 14: // yyyymmddhhmmss
        precision = TemporalPrecisionEnum.SECOND;
        break;
      case 16: // yyyymmddhhmmss.s
        precision = TemporalPrecisionEnum.MILLI;
        break;
      case 17: // yyyymmddhhmmss.ss
        precision = TemporalPrecisionEnum.MILLI;
        break;
      case 18: // yyyymmddhhmmss.sss
        precision = TemporalPrecisionEnum.MILLI;
        break;
      case 19: // yyyymmddhhmmss.ssss
        precision = TemporalPrecisionEnum.MILLI;
        break;
      default:
        precision = null;
    }

    // given string may include up to four digits of fractions of a second
    // therefore, there may be cases where the length of the string is 17,18 or 19
    // and the precision is MILLI.
    // for those of cases where the length causes conflicts, let's check if dot(.)
    // exists in the string

    // setting precision
    if (precision != null) {
      date.setPrecision(precision);
    } else {
      // incorrect format
      return null;
    }

    // if timeZone is present, setting it
    if (timeZone != null) {
      date.setTimeZone(timeZone);
    }

    if (precision == TemporalPrecisionEnum.MILLI) {
      // get the integer starting from the dot(.) char 'till the end of the string as
      // the millis
      int millis = new Integer(dateString.substring(dateString.indexOf('.') + 1));
      // if millis is given as .4 , it corresponds to 400 millis.
      // therefore, we need a conversion.
      if (millis > 0 && millis < 1000) {
        while (millis * 10 < 1000) {
          millis *= 10;
        }
      } else if (millis >= 1000) {
        // unexpected situtation
        millis = 999;
      } else {
        // unexpected situtation
        millis = 0;
      }

      // setting millis
      date.setMillis(millis);

      // setting second, minute, hour, day, month, year..
      date.setSecond(new Integer(dateString.substring(12, 14)));
      date.setMinute(new Integer(dateString.substring(10, 12)));
      date.setHour(new Integer(dateString.substring(8, 10)));
      date.setDay(new Integer(dateString.substring(6, 8)));
      date.setMonth(new Integer(dateString.substring(4, 6)) - 1);
      date.setYear(new Integer(dateString.substring(0, 4)));

    } else {
      // since there are strange situtations where the index changes upon the
      // precision, we set every value in its precision block
      switch (precision) {
        case SECOND:
          date.setSecond(new Integer(dateString.substring(12, 14)));
          date.setMinute(new Integer(dateString.substring(10, 12)));
          date.setHour(new Integer(dateString.substring(8, 10)));
          date.setDay(new Integer(dateString.substring(6, 8)));
          date.setMonth(new Integer(dateString.substring(4, 6)) - 1);
          date.setYear(new Integer(dateString.substring(0, 4)));
          break;
        case MINUTE:
          date.setMinute(new Integer(dateString.substring(10, 12)));
          date.setHour(new Integer(dateString.substring(8, 10)));
          date.setDay(new Integer(dateString.substring(6, 8)));
          date.setMonth(new Integer(dateString.substring(4, 6)) - 1);
          date.setYear(new Integer(dateString.substring(0, 4)));
          break;
        case DAY:
          date.setDay(new Integer(dateString.substring(6, 8)));
          date.setMonth(new Integer(dateString.substring(4, 6)) - 1);
          date.setYear(new Integer(dateString.substring(0, 4)));
          break;
        case MONTH:
          date.setMonth(new Integer(dateString.substring(4, 6)));
          date.setYear(new Integer(dateString.substring(0, 4)));
          break;
        case YEAR:
          date.setYear(new Integer(dateString.substring(0, 4)) + 1);
          break;
        default:
          date = null;
      }
    }
    return date;
  }
}
