package tr.com.srdc.cda2fhir.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.Substance;



public class UuidFactory {

  private HashMap<String, UUID> guids;

  public UuidFactory() {
    guids = new HashMap<String, UUID>();
  }

  /**
   * Adds a given Key to the Guids Listing and returns the Guid value added.
   * @param key Key to add.
   * @return UUID
   */
  public UUID addKey(String key) {
    if (!guids.containsKey(key)) {
      guids.put(key,UUID.randomUUID());
    } 
    return guids.get(key);
  }

  /**
   * Adds a specific UUID to the guids list.
   */
  private UUID addKey(String key, UUID guid) {
    guids.put(key, guid);
    return guids.get(key);
  }

  /**
   * Adds a given resource to the Guid Listing.
   * @param resource FHIR Resource to add to the Guid Listing.
   * @return UUID
   */
  @SuppressWarnings("unchecked")
  public UUID addKey(Resource resource) {
    try {
      Method method = resource.getClass().getMethod("getIdentifier");
      List<Identifier> ids = (List<Identifier>)method.invoke(resource);
      if (ids != null && ids.size() > 0) {
        UUID guid = null;
        for (Identifier id : ids) {
          String keyValue = 
              resource.getClass().getName() 
                + "|" 
                + id.getSystem() 
                + "|" 
                + id.getValue();
          if (guids.containsKey(keyValue)) {
            UUID tempGuid = guids.get(keyValue);
            if (guid != null && tempGuid != guid) {
              guids.put(keyValue, guid);            
            } else {
              guid = tempGuid;
            }
          } else {
            if (guid != null) {
              guid = addKey(keyValue, guid);
            } else {
              guid = addKey(keyValue);
            }
          }
        }
        return guid;
      } else {
        //No Identifiers in the Resource.
        String key = "";
        if (resource instanceof Organization) {
          Organization org = (Organization)resource;
          if (org.getName() != null) {
            return addKey(org.getClass().getName() + "|" + org.getName());
          }
        }

        if (resource instanceof Substance) {
          Substance substance = (Substance)resource;
          if (substance.getCode() != null) {
            return addKey(substance.getClass().getName() 
                + "|" 
                + substance.getCode().getCodingFirstRep().getCode());
          }
        }
        
        if (resource.getId() != null) {
          key = resource.getClass().getName() + "|" + resource.getId();        
        } else {
          key = resource.getClass().getName();
        }
        return addKey(key);
      }      
    } catch (Exception ex) {
      //No Identifiers in the Resource.
      String key = "";
      if (resource instanceof Medication) {
        Medication med = (Medication)resource;
        if (med.getCode() != null && med.getCode().getCoding() != null) {
          Coding mdCode = med.getCode().getCodingFirstRep();
          if (mdCode.getSystem() != null && mdCode.getCode() != null) {
            key = resource.getClass().getName() + "|" + mdCode.getSystem() + "|" + mdCode.getCode();
            return addKey(key);
          } else {
            if (mdCode.getCode() != null) {
              key = resource.getClass().getName() + "|" + mdCode.getCode();
              return addKey(key);
            }
          }
        }
      }
      
      if (resource.getId() != null) {
        key = resource.getClass().getName() + "|" + resource.getId();        
      } else {
        key = resource.getClass().getName();
      }
      return addKey(key);
    }
  }

  public UUID getGuid(String key) {
    return addKey(key);
  }

  public UUID getGuid(Resource resource) {
    return addKey(resource);
  }

  public void clear() {
    guids.clear();
  }
}