# Pay Now App
## Java Spring Boot app using JDK 17 with Maven
Acts as SP, so far is configured with Keycloak as Idp. I am running Keycloak on https://localhost:6771 and Spring on https://localhost:7171.

### Steps to run app
Download and unzip keycloak (https://www.keycloak.org/downloads)

Have Java 17 installed and running. If missing, install Eclipse Adoptium Temurin JDK 17.

Download my keycloakFiles.zip in the parent folder.

Paste mycert.pfx in bin folder

Paste data folder in main folder

username: temp1

password: !uLHjq!kWh98akE


### OR: 

You'll need a valid keystore (.pfx or .jks).
If you don't have one, create a self-signed .pfx with

keytool -genkeypair -alias mykey -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore mycert.pfx -validity 365

Make sure .pfx is in keycloak bin folder 

Use command prompt, go to bin directory, run command to start Keycloak using tcp:

.\kc.bat start-dev ^
  --https-port=6771 ^
  --https-key-store-file="C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot\keystore\mycert.pfx" ^
  --https-key-store-password=yourpassword ^
  --https-key-store-type=PKCS12 ^
  --hostname-strict=false ^
  --spi-hostname-strict-https=false
  
Console will show e.g. Listening on: https://0.0.0.0:6771

Got to https://localhost:6771, this is keycloak admin page

### If you don't use my files, or if they aren't working: 
Sign in with username admin, password admin, create new password once logged in

Create new realm, go to realm settings, click on link Endpoints: Saml 2.0 Indentity Provider Metadata
Will open in https://localhost:6771/realms/{realmName}/ protocol/saml/descriptor

This is the metadata

While running app, go to https://localhost:7171/saml2/service-provider-metadata/keycloak
Will download xml file


Go to Keycloak, Clients, import Client, import xml and it will fill out information

(If needed) In Client, Keys, click Import Key, import public cert that was created with private .pfx. Certificate string will match what is in xml

ClientId in Keycloak = SP entity ID

In Client->Client Details->settings, make sure to check on Sign Documents, Sign Assertions, Force Post Binding. In Advanced, check Assertion Consumer Service POST Binding URL set to https://localhost:{port}/login/saml2/sso/{registration-Id} (appears in XML file downloaded)

Go to Users, create User. 

Fill in provided fields. Go to Credentials and set password.

Go to Realm Settings. 

Go to User Profile and Add Attributes. E.g. for student_id, use that for name and ${student_id} for display name. Set user and view and edit.

Go to Client Scopes, Create Client Scope, create studentAttribute scope and choose protocol Saml. Choose that Client Scope, go to Mappers, Configure a new mapper. 

Choose Mapper Type: User Attributes. Create attributes e.g. use student_id for all names. 

Go back to Users, your user, a new field will show up. Test student_id 1000039.

Create attributes and give them dummy values:

account_balance

address

city

state

zip

country

phone

term_code

### Colleague API

For authentication, put API key in environmental variables with the format ELLUCIAN_BEARER_TOKEN={password}. 

In Intellij Idea, this is in run configuration. 

Colleague API endpoints used: /persons, /addresses, account-activity/admin.

Run app with Run Maven button and it should bring up https://localhost:7171/index in browser.

Credentials after starting java app:

user name: student

password: student

Optional proxy settings are located in RestTemplateConfig.java

App is currently using Sandbox API, to change, edit url in FlywireService



