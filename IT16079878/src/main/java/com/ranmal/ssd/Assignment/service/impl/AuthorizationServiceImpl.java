package com.ranmal.ssd.Assignment.service.impl;

  import java.io.IOException;
  import javax.annotation.PostConstruct;
  import java.io.InputStreamReader;
  import javax.servlet.http.HttpServletRequest;
  import com.ranmal.ssd.Assignment.util.ApplicationConfig;
  import org.springframework.stereotype.Service;
  import com.ranmal.ssd.Assignment.service.AuthorizationService;
  import org.slf4j.Logger;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.slf4j.LoggerFactory;
  import com.google.api.client.util.store.FileDataStoreFactory;
  import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
  import com.google.api.client.auth.oauth2.Credential;
  import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
  import com.ranmal.ssd.Assignment.constant.ApplicationConstant;
  import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
  import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

  private GoogleAuthorizationCodeFlow myflow;

  private Logger mylogger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
  private FileDataStoreFactory mydataStoreFactory;

  @Autowired
  private ApplicationConfig myconfig;

  @PostConstruct
  public void init() throws Exception {

    InputStreamReader myreader = new InputStreamReader(myconfig.getDriveSecretKeys().getInputStream());
    mydataStoreFactory = new FileDataStoreFactory(myconfig.getCredentialsFolder().getFile());

    GoogleClientSecrets myclientSecrets = GoogleClientSecrets.load(ApplicationConstant.JSON_FACTORY, myreader);
    myflow = new GoogleAuthorizationCodeFlow.Builder(ApplicationConstant.HTTP_TRANSPORT, ApplicationConstant.JSON_FACTORY, myclientSecrets,
      ApplicationConstant.SCOPES).setDataStoreFactory(mydataStoreFactory).build();
  }

  //To get Credentials
  @Override
  public Credential getCredentials() throws IOException {
    return myflow.loadCredential(ApplicationConstant.USER_IDENTIFIER_KEY);
  }


  // To Check Authentication status
  @Override
  public boolean isUserAuthenticated() throws Exception {
    Credential credential = getCredentials();
    if (credential != null) {
      boolean isTokenValid = credential.refreshToken();
      mylogger.debug("isTokenValid, " + isTokenValid);
      return isTokenValid;
    }
    return false;
  }

  // To exchange the code
  @Override
  public void exchangeCodeForTokens(String code) throws Exception {
    GoogleTokenResponse tokenResponse = myflow.newTokenRequest(code).setRedirectUri(myconfig.getCALLBACK_URI()).execute();
    myflow.createAndStoreCredential(tokenResponse, ApplicationConstant.USER_IDENTIFIER_KEY);
  }

  //To Remove User Session
  @Override
  public void removeUserSession(HttpServletRequest request) throws Exception {

    mydataStoreFactory.getDataStore(myconfig.getCredentialsFolder().getFilename()).clear();
  }

  //To Authenticate with google
  @Override
  public String authenticateUserViaGoogle() throws Exception {
    GoogleAuthorizationCodeRequestUrl url = myflow.newAuthorizationUrl();
    String redirectUrl = url.setRedirectUri(myconfig.getCALLBACK_URI()).setAccessType("offline").build();
    mylogger.debug("redirectUrl, " + redirectUrl);
    return redirectUrl;
  }





}
