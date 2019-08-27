<!--
Copyright (C) 2016 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. A.S.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

# cda2fhir [![License Info](http://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](https://github.com/srdc/cda2fhir/blob/master/LICENSE.txt)

cda2fhir is a Java library to transform HL7 CDA R2 instances to HL7 FHIR resources. More specifically, cda2fhir enables automatic transformation of
Consolidated CDA (C-CDA) Release 2.1 compliant document instances to the corresponding FHIR DSTU2 resources, wherever possible implementing the
[U.S. Data Access Framework (DAF) FHIR Implementation Guide](http://hl7.org/fhir/DSTU2/daf/daf.html). For this purpose, cda2fhir provides extensible
document transformers, resource transformers, data type transformers and value set transformers. The current implementation provides a
document transformer for Continuity of Care Document (CCD), but further document transformers, e.g. for Discharge Summary or Referral Note,
can be easily introduced by reusing the already existing section and entry transformers. Although the cda2fhir library expects C-CDA R2.1 compliant
documents/entries, it has been tested as well with several older document instances compliant with earlier releases of C-CDA. The official
[HL7 FHIR Validator](https://www.hl7.org/fhir/validation.html#jar) is also integrated for automated validation of the generated FHIR resources.

All the mappings implemented between CDA artifacts and FHIR resources, data types and value sets are documented in this sheet:
[C-CDA CCD to FHIR DAF Mapping](https://docs.google.com/spreadsheets/d/15Kv6PFyPh91sH1JMYwLH7D2yjh4HOTy5pjETjQNRyaU/edit?usp=sharing)

[Model Driven Health Tools (MDHT)](https://projects.eclipse.org/projects/modeling.mdht) is used for CDA manipulation and
[HAPI](http://hapifhir.io/) is used for FHIR manipulation. The current implementation produces DSTU2 resources.
We are planning to cover STU3 resources as well, after the specification becomes official.

## New Fork Additions

This fork has been updated to support HL7 FHIR STU3 Resources and primarily the [US Core Profiles](http://hl7.org/fhir/us/core/) for validation.  All testing has been accomplished primarily with Continuity of Care documents rather than any other specific template of C-CDA artifacts.  As such, the following new additions have been added to enhance the information received from the CCD artifacts.

### Episode of Care

The Episode of Care resource is now being created from CCD documents that contain an Encompassing Encounter data element. This item combined with the Service Event element have been combined to define an Episode of Care FHIR Resource.  All Encounters contained within the CCD document are then attached to this Episode of Care.

### Provenance

For each CCD processed through the library, an accompanying Provenance Resource is created with references to all FHIR Resources created.  This also includes a Document Reference that contains the Raw CCD Document as an attachment.

### Care Team

The Service Event data element is now being mapped to a Care Team to be associated to an Episode of Care. This will only occur when the Encompassing Encounter data element is available.  Care Teams are also created for the Encompassing Encounter when multiple Encounter Participants are defined.  A Care Team is then created and each Participant is added as a Practitioner.

### Patient Contacts

The Participants identified with a typeCode of "IND" are now being mapped as Patient Contact Components for a Patient.

### Concept Maps

Support for leveraging Concept Maps has been added to the Data Types, Resource and Value Sets Transformers.  These will allow more complicated mapping to be utilized instead of the hand mapping defined in some of the methods.  Concept Maps should contain an Identifier with a FHIR Path value as the value.  This will allow the library to determine a map to use in a particular situation.

## Installation

Apache Maven is required to build cda2fhir. Please visit <http://maven.apache.org/> in order to install Maven on your system.

Under the root directory of the cda2fhir project run the following:

  $ cda2fhir> mvn install

In order to make a clean install run the following:

  $ cda2fhir> mvn clean install

These will build the cda2fhir library and also run a number of test cases, which will transform some C-CDA Continuity of Care Document (CCD) instances,
and some manually crafted CDA artifacts (e.g. entry class instances) and datatype instances to corresponding FHIR resources, wherever possible using the DAF profile.

## Transforming a CDA document to a Bundle of corresponding FHIR resources

```java
// Load MDHT CDA packages. Otherwise ContinuityOfCareDocument and similar documents will not be recognised.
// This has to be called before loading the document; otherwise will have no effect.
CDAUtil.loadPackages();

// Read a Continuity of Care Document (CCD) instance, which is the official sample CCD instance
// distributed with C-CDA 2.1 specs, with a few extensions for having a more complete document
FileInputStream fis = new FileInputStream("src/test/resources/C-CDA_R2-1_CCD.xml");
ContinuityOfCareDocument ccd =
    (ContinuityOfCareDocument)CDAUtil.loadAs(fis,
        ConsolPackage.eINSTANCE.getContinuityOfCareDocument());

// Init an object of CCDTransformerImpl class, which implements the generic ICDATransformer interface.
// FHIR resource id generator is a UUID generator.
// The default is UUID;
ICDATransformer ccdTransformer = new CCDTransformerImpl(IdGeneratorEnum.UUID);

// By default, FHIR DSTU2 resources are generated by setting the appropriate DAF profile URLs
// in meta.profile attribute of resources. This is configurable through the statically (i.e. globally)
// managed Config class, and can be turned on or off.
Config.setGenerateDafProfileMetadata(true);

// By default, html formatted narratives are generated in text.div attributes of FHIR DSTU2 resources,
// thanks to the automated narrative generation capability of HAPI that is enabled via thymeleaf library.
// This is configurable through the statically managed Config class, and can be turned on or off.
Config.setGenerateNarrative(true);

// Finally, the CCD document instance is transformed to a FHIR Bundle, where the first entry is
// the Composition corresponding to the ClinicalDocument, and further entries are the ones referenced
// from the Composition.
Bundle bundle = ccdTransformer.transformDocument(cda);

// Through HAPI library, the Bundle can easily be printed in JSON or XML format.
FHIRUtil.printJSON(bundle, "src/test/resources/output/C-CDA_R2-1_CCD-w-daf.json");
```

Further code examples can be found in [CCDTransformerTest](https://github.com/srdc/cda2fhir/blob/master/src/test/java/tr/com/srdc/cda2fhir/CCDTransformerTest.java) class.
The outcome of the above transformation operation for the CCD instance available in the C-CDA 2.1 specification is available here: <https://github.com/srdc/cda2fhir/blob/master/src/test/resources/C-CDA_R2-1_CCD-w-daf.json>

## Transforming a CDA artifact (e.g. an entry class) to the corresponding FHIR resource(s)

```java
// Init an object of ResourceTransformerImpl class, which implements the IResourceTransformer
// interface. When instantiated separately from the CDATransformer context, FHIR resources are
// generated with UUID ids, and a default patient reference is added as "Patient/0"
IResourceTransformer resTransformer = new ResourceTransformerImpl();

// Configuration of DAF profile URL creation in meta.profile and narrative generation in text.div is
// again configurable through the statically managed Config class.
Config.setGenerateDafProfileMetadata(true);
Config.setGenerateNarrative(false);

// Assume we already have a CCD instance in the ccd object below (skipping CDA artifact creation from scratch)
// Traverse all the sections of the CCD instance
for(Section cdaSec: ccd.getSections()) {
    // Transform a CDA section to a FHIR Composition.Section backbone resource
    Composition.Section fhirSec = resTransformer.tSection2Section(cdaSec);

    // if a CDA section is instance of a Family History Section (as identified through its templateId)
    if(cdaSec instanceof FamilyHistorySection) {
        // cast the section to FamilyHistorySection
        FamilyHistorySection famSec = (FamilyHistorySection) cdaSec;
        // traverse the Family History Organizers within the Family History Section
        for(FamilyHistoryOrganizer fhOrganizer : famSec.getFamilyHistories()) {
            // Transform each C-CDA FamilyHistoryOrganizer instance to FHIR (DAF) FamilyMemberHistory instance
            FamilyMemberHistory fmh = resTransformer.tFamilyHistoryOrganizer2FamilyMemberHistory(fhOrganizer);
        }
    }
}

// Again, any FHIR resource can be printed through FHIRUtil methods.
FHIRUtil.printXML(fmh, "src/test/resources/output/family-member-history.xml");
```

It should be noted that most of the time, IResourceTransformer methods return a FHIR Bundle composed of a few FHIR resources,
instead of a single FHIR resource as in the example above. For example, tProblemObservation2Condition method returns a Bundle
that contains the corresponding Condition as the first entry, which can also include other referenced resources such as Encounter, Practitioner.

Further examples can be found in [ResourceTransformerTest](https://github.com/srdc/cda2fhir/blob/master/src/test/java/tr/com/srdc/cda2fhir/ResourceTransformerTest.java) class
and [CCDTransformerImpl](https://github.com/srdc/cda2fhir/blob/master/src/main/java/tr/com/srdc/cda2fhir/transform/CCDTransformerImpl.java) class.

## Validating generated FHIR resources

__This has been deprecated and removed from this version of the project.  It is better to utilize the HL7 FHIR Validator outside of the Java library to validate.__
We have also integrated the official [HL7 FHIR Validator](https://www.hl7.org/fhir/validation.html#jar), although in a bit ugly way since this validator is not available in any
Maven repo. We have implemented a wrapper interface and a class on top of this validator: IValidator and ValidatorImpl. A resource can be validated individually, or a Bundle
containing several resources as in the case of CDA transformation outcome can be validated at once. When (DAF) profile metadata is provided within the resources' meta.profile
attribute, validation takes into account this profile as well. Validation outcome is provided as HTML within an OutputStream.

```java
// Init an object of ValidatorImpl class, which implements the IValidator interface.
IValidator validator = new ValidatorImpl();

// Assume we already have a Bundle object to be validated at hand. Call the validateBundle method
// of the validator and get the validation outcome as HTML in a ByteArrayOutputStream.
ByteArrayOutputStream valOutcomeOs = (ByteArrayOutputStream) validator.validateBundle(bundle);

// The HTML can be printed to a file.
FileOutputStream fos = new FileOutputStream(new File("src/test/resources/output/validation-result-w-profile-for-C-CDA_R2-1_CCD.html"));
valOutcomeOs.writeTo(fos);

// Close the streams
valOutcomeOs.close();
fos.close();
```

Further examples can be found in [ValidatorTest](https://github.com/srdc/cda2fhir/blob/master/src/test/java/tr/com/srdc/cda2fhir/ValidatorTest.java) class. Some of the tests
in this class are ignored, as validating takes some time, especially due to external Terminology Server access dependency. But they do work, users can enable them.

Unfortunately it is not easy to find up and running DSTU2 terminology servers all the time, hence this test can fail when none of the terminology servers configured in
[Config](https://github.com/srdc/cda2fhir/blob/master/src/main/java/tr/com/srdc/cda2fhir/conf/Config.java) is accessible. In this case, if you happen to know an accessible
DSTU2 terminology server, you can either update Config or set via the setTerminologyServer method of the validator. If you cannot find a running terminology server, then
 you can just ignore the validator tests.

## Acknowledgement

This research has received funding from the European Union’s Horizon 2020 research and innovation programme under grant agreement No 689181,
[C3-Cloud Project](http://www.c3-cloud.eu/) (A Federated Collaborative Care Cure Cloud Architecture for Addressing the Needs of Multi-morbidity and Managing Poly-pharmacy).

This research has received funding from the European Union’s Horizon 2020 research and innovation programme under grant agreement No 689444,
[POWER2DM Project](http://www.power2dm.eu/) (Predictive model-based decision support for diabetes patient empowerment).
