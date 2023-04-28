# PLSplus-QG-spec
Property Location Service Plus PLSplus QG Version -REST Spec

Property Location Service Plus - QG is offered under a Creative Commons - Attribution 4.0 International licence.

The Department of Resources requests attribution in the following manner:

© State of Queensland, Department of Resources (Property Location Service Plus - QG) 2021

More general information can be found at 
https://www.qld.gov.au/environment/land/title/addressing/finding

This is the Qld Government api only api made available for other agencies to use.
https://qldspatial.information.qld.gov.au/catalogue/custom/viewMetadataDetails.page?uuid=%7BD77C19E4-5C24-46E4-80C3-244164182572%7D
similar to
https://www.data.qld.gov.au/dataset/property-location-service-plus-public-json
and
https://qldspatial.information.qld.gov.au/catalogue/custom/detail.page?fid={D77C19E4-5C24-46E4-80C3-244164182572}


A basic service can be found here
https://geocode.information.qld.gov.au/validate


A commerical endpoint with simliar/same data can be found at
https://developer.geoscape.com.au/ and many other companies out there.

Please read the swagger definition via https://editor.swagger.io/ or simliar tooling for the full documentation. Below allows you to start

# Introduction

This Provides the starting point for using the QG (Qld Govt) version of the Property Location Service (PLS) Plus RESTful Implemenation. The service has a sister implementation using SOAP technology. In this document the RESTful version will always be assumed unless specifically stated as the SOAP implementation.The document describes the service functions and provides a roadmap for implementing this functionality into user systems.

Access to PLSplus-QG is recommended for all government agencies.

Physical address validation is free of charge, however, users are responsible for their own implementation and supporting services. Application support services are only available to the core service by Department of Resources.

The PLSplus-QG service will validate Australian addresses by including matches across unit number, street number, street name, street type, locality, state and postcode. The solution provides users with a selection of address candidates to pick from in the case where there is no exact match. The returned confidence on addresses is mapped as per Table 1 in Section 11.4. A fast AutoComplete address function is available in PLSplus for Queensland addresses. The QG version extends the Public version by the addition of validation of interstate addresses as well as ABS statistical meshblock information for all Australian addresses.

PLSplus uses addressing standards AS4819 and AS4590 as its base.

There may be future enhancements, but at this stage it meets the initial specifications as an address service for point of entry confirmation of physical addressing. Any coding to ‘clean up’ returned candidate addresses needs to be performed by client applications consuming the service.


## 1) Data Source
The Queensland Address Management Framework (QAMF) dataset is used to populate the service data source for Queenland addresses. The dataset is maintained by the Department of Resources (DR) and updated nightly for use by client applications. Interstate addresses are provided by a third party (paid) service and use the GNAFF.

The QAMF is the primary source of property location addressing information for Queensland. It is location centric, meaning that addresses can be geocoded against a variety of locations, eg centroid of the property, centroid of the street alignment, entrance of the property and the main building. It is the point of truth for addressing in Queensland and is continually maintained from data supplied by local government authorities (LGA’s).

Attributes currently stored include&colon;
  * Lot Number
  * Plan Number
  * Geocode
  * Geocode Type
  * Unit Type
  * Unit Number
  * Level
  * Building/Complex Name
  * Street Number
  * Street Name or Property Name
  * Street Suffix
  * Street Type
  * Locality
  * State

Postcode data is not part of the QAMF but is available from the PLSplus service.

Note&colon; Users should be aware that not all properties have an address. These include properties where the LGA does not hold an interest, eg a National Park or unallocated Crown Land. Some properties (like gated or Indeginous communities) have multiple addresses within them. Multi-

Address properties are catered for in PLSplus.

Interstate addresses is sourced from The GNAF (Geocoded National Address File) which has the same Attributes minus the Plan Number of the property.


## 2) Functionality Available

Under a whole of government agreement approved users may access the following PLS plus functionality for the exclusive benefit of the approved user (Licensed Purpose), free of software licensing costs&colon;
  * Address validation against the QAMF
  * Appending latitude/longitude to an address using the QAMF
  * Appending lot/plan information to an address using the QAMF
  * Looking up addresses in the QAMF from a given lot and plan number
  * Looking up addresses in the QAMF from a given latitude and longitude
  * Australian postcodes
  * Australian mesh blocks (ASGS2011)
  * Failover to a 3rd party provider
  * Interstate address validation through a 3rd party provider.

Batch processing on the server side is not offered at this point in time. The Qld Geocoder application, used for batching up to 5000 addresses. LotPlans or geocodes, is available at http&colon;//geocode.information.qld.gov.au. The application requires a comma delimited (CSV) file of addresses and uses the ParseAddress, ValidateLotPlan or ValidateCoordinate operations. Full documentation is available at the above URL.Individual validations are also available with the application.

## 3) Support Services

Department of Resources will supply&colon;
* Business support&colon; described as granting access to the individual services and general business requests, and
* Technical support&colon; described as support for the physical environment and associated hardware.

Integration with applications, training and other support services within the user’s area is the responsibility of each user.

Spatial Help Centre" https://spatial-qld-support.atlassian.net/servicedesk/customer/portals 

Email: support@spatial-qld-support.atlassian.net


## 4) Service Security
The QG versions of the service is made available to approved authenticated users, both internal to Department of Resources and other Queensland Government agencies.

## 5) Obtaining the Service
The QG version of the service requires authentication by way of a username and password.

Access to the QG service and provision of credentials can be obtained by sending a request to OpenData@resources.qld.gov.au and providing the following information&colon;


   * Preferred username*&colon; (eg plspQr-floodinfo)
   * Project or application name&colon; (eg Floodinfo)
   * Service requested - REST Company/Department Name
   * Responsible Officer Name&colon;
   * Responsible Officer Email&colon; (Must be shared mailbox)
   * Responsible Officer Phone&colon;
   * Secondary Contact Name&colon;
   * Secondary Contact Email&colon;
   * Secondary Contact Phone&colon;
   * Give an indication of the category your applications fall into&colon;
     * Life Threatening - Used for emergency purposes (24/7 needed)
     * Critical - Used for business purposes business hours (counter or online)
     * Non Critical - Used for business purposes

 Note&colon; the user name will start with plspQr_ for QG version users.

### Logging and Auditing
Logging and auditing usage of the service helps by providing the metrics for determining requirements for enhancements to the service, infrastructure and data.
* To ensure an accurate record of usage by applications, **please request an account for each separate project or application**.
* **Do not pass the credentials to other staff for use in other projects or applications**.
Following approval from the delegated officer, credentials for the required service/s will be provided.

If the following message is returned from a request it may be due to authentication problems&colon;

"status"&colon; "error", “This request was rejected due to a violation. Please contact OpenData@resources.qld.gov.au, and provide this reference ID&colon; 12719710694116814232"}


Take a note of the time of the request and include it and your username in an email to OpenData@resources.qld.gov.au for assistance.

## 6) Licensing

The Property Location Service Plus – QG is offered under a Creative Commons Attribution licence. To view a copy of this license visit https&colon;//creativecommons.org/licenses/by/4.0/. The Department requests attribution in the following manner&colon; Natural Resources and Mines, Queensland Government, Property Locations Service Plus - QG, licensed under Creative Commons Attribution.

## 7) Charging and Pricing

In this release of the PLSplus-QG there is no intention to develop a charge for Government departments. If, in subsequent releases additional functionality is added, charging for the additional functionality may be considered.


# Notes

Please read the swagger definition via https://editor.swagger.io/ or simliar tooling for the full documentation. 
At this time it is named swagger-v3.yml 


# Cloudformation template

file: plsPlusApi.cfn.yml 

To ensure the username/password of the api is not on the internet, this script creates wrapper that allows multiple nice api keys to pick the user auth with usage tracking and limits if enabled.
 
