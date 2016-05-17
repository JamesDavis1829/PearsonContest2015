package sample;

import com.pearson.pdn.learningstudio.oauth.OAuth2PasswordService;
import com.pearson.pdn.learningstudio.oauth.OAuthServiceFactory;
import com.pearson.pdn.learningstudio.oauth.config.OAuthConfig;
import com.pearson.pdn.learningstudio.oauth.request.OAuth2Request;

public class Auth {

    private final String applicationId   	= "af676d40-c3bc-4a34-bd35-a8fa7c246f5f";
    private final String clientString    	= "gbtestc";
    public String token;

    public Auth(String userName, String password){

        try
        {
            OAuthConfig config = new OAuthConfig();
            config.setApplicationId(applicationId);
            config.setClientString(clientString);

            OAuthServiceFactory oauthFactory = new OAuthServiceFactory(config);

            OAuth2PasswordService oauthService = oauthFactory.build(OAuth2PasswordService.class);

            OAuth2Request oauthRequest = oauthService.generateOAuth2PasswordRequest(userName, password);

            token = oauthRequest.getAccessToken();

        } catch (Exception e){
            token ="";
        }

    }

    public String getToken (){

        if (token.contains(applicationId)){
            return token;
        }
        else {
            return token;
        }
    }

}
