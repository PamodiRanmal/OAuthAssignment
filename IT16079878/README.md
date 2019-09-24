## OAuth2 - Google Drive Sample Application

A demo application to showcase the OAuthe2 Framework with google Drive.

### Prerequisites 
1. You should have a project created on [google cloud console](https://console.cloud.google.com)
2. Google drive API needs to be enabled for the project in [API Library](https://console.cloud.google.com/apis/library)
3. Generate the credentials using the [wizard](https://console.cloud.google.com/apis/credentials/wizard)
4. Edit gsuite_drive_keys.json file in src/main/resources/keys directory to match your project credentials


### Steps to run
1. Build using `mvn clean install`
2. Run using `mvn spring-boot:run` or run the .jar file under /target directory
3. The application is accessible at [http://localhost:8080/](http://localhost:8080/)
4. Allow google sign-in to access the application

---
App Login Page
![Login page](./readme-resources/01-login-page.png?raw=true "Login Page")

Gmail Login Page
![Gmail Login page](./readme-resources/02-gmail-login-page.png?raw=true "Gmail Login Page")

2-Step Verification
![Login page](./readme-resources/03-2step-verification.png?raw=true "2-Step Verification")

File Upload Page
![Login page](./readme-resources/04-file-upload-page.png?raw=true "File Upload Page")

Select file to upload
![Login page](./readme-resources/05-select-upload-file.png?raw=true "Select file to upload")

Upload Success Page
![Login page](./readme-resources/06-upload-success.png?raw=true "Upload Success Page")

Verify the upload in google drive
![Login page](./readme-resources/07-file-in-google-drive.png?raw=true "Verify the upload in google drive")