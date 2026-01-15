----------- API Documnetation for OAuth2 ----------------------------------
1.API Register USer with Google OAuth2

https://developers.google.com/oauthplayground/?code=4%2F0ASc3gC1__qi80w_AsUHdU0U087x0yqVUu9jJrM7M1c1MJB6D-HDndy0etZvbVH3ajMShyg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=consent

curl --request GET \
  --url 'http://localhost:8292/auth/google/getOauthUserAuthorization?code=4%2F0ASc3gC36PLBPTrY4RmA7pJ0quNXQiUEpHR3ltOj7nXhiHlaK8E3R643tnhbOsNG6qWEvGw' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8
  
Steps--
1.Go to site 
https://developers.google.com/oauthplayground

2.Add the url 
https://www.googleapis.com/auth/userinfo.email

3.In setting set these
---- OAuth 2.0 configuration-----
OAuth flow:Server-side
OAuth endpoints:Google
Authorization endpoint:https://accounts.google.com/o/oauth2/v2/auth
Token endpoint: https://oauth2.googleapis.com/token
Access token location: Authorization header w/ Bearer prefix
Access type:offline
Force prompt:Consent Screen

Use your own OAuth credentials
clientId - 329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com
clientSecret - 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

--------------------
and press the "Authorize API's" button 
-------------

4.Continue on the Consent screen 

5.Get the Authorization code
Authorization code :4/0ASc3gC1__qi80w_AsUHdU0U087x0yqVUu9jJrM7M1c1MJB6D-HDndy0etZvbVH3ajMShyg

Copy the "Authorization code"

6.Call the API 

curl --request GET \
  --url 'http://localhost:8292/auth/google/getOauthUserAuthorization?code=4%2F0ASc3gC36PLBPTrY4RmA7pJ0quNXQiUEpHR3ltOj7nXhiHlaK8E3R643tnhbOsNG6qWEvGw' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8
 
Pass the "Authorization code" in params
Authorization code :4/0ASc3gC1__qi80w_AsUHdU0U087x0yqVUu9jJrM7M1c1MJB6D-HDndy0etZvbVH3ajMShyg

http://localhost:8292/auth/google/getOauthUserAuthorization
?code=4%2F0ASc3gC36PLBPTrY4RmA7pJ0quNXQiUEpHR3ltOj7nXhiHlaK8E3R643tnhbOsNG6qWEvGw

Response --
{
userName=suraj19rastogi@gmail.com, 
accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXJhajE5cmFzdG9naUBnbWFpbC5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VS
In1dLCJpYXQiOjE3NjgzOTI0MzMsImV4cCI6MTc2ODM5MzMzM30.ITag7iuVl2Re3mOayYCZJo5cIAbk_ZDhyzxlPXNy_yo, 
refreshToken=4d0174b4-a813-48ba-b537-a20b73bef196
}
--------------------------------------------------------------------------------------------------
1.First it call these API of Google for token
/** 1.Exchange authorization code for tokens by calling google /token API **/
String tokenRequestUri = "https://oauth2.googleapis.com/token";
String redirectUri = "https://developers.google.com/oauthplayground";

MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
params.add("code",code);
params.add("client_id",clientId);
params.add("client_secret",clientSecret);
params.add("redirect_uri",redirectUri);
params.add("grant_type","authorization_code");
			
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params,headers);

ResponseEntity<Map> tokenApiResponse = restTemplate.postForEntity(tokenRequestUri, entity, Map.class);

Response--------
{
  "access_token": "ya29.a0AUMWg_L456dMmSvKp1KCmFj-gastxvcnNKgL_N8NiYOD6xRiK6BihV5DyE55NNshgyIYu1ihulhNV-Omgnm5hMDQFPk6NAG8_xWDBF7uu94qf41Q49leqOf2B0y9MqTdmIbmR-LCMcgrCwg5PHIgeaLgV--2YxxPqgPDDyi8PjRnvapxAQ7GjBR09I6FGL3EM9Mz_qgaCgYKARASARMSFQHGX2MiXon4u_6j_dPVk-74xKZ52A0206", 
  "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjRiYTZlZmVmNWUxNzIxNDk5NzFhMmQzYWJiNWYzMzJlMGY3ODcxNjUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIzMjk2NzYwMzI1MzktNmhqMnNxc3RvbTNzMTNvdWFyajg1OWpzZGZ0MTByMmUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIzMjk2NzYwMzI1MzktNmhqMnNxc3RvbTNzMTNvdWFyajg1OWpzZGZ0MTByMmUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDI4NDQ3NDQ5NTgxMTg2NzM1ODEiLCJlbWFpbCI6InN1cmFqMTlyYXN0b2dpQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiRUZpN0EzS0NIamRGNERFVkpiQ0hUUSIsImlhdCI6MTc2ODMwNTcwNywiZXhwIjoxNzY4MzA5MzA3fQ.tN-sksS8F0tZg4cM790jVIGdMAzfB4jxsIma2Ia-fjAW5KxKCL0mfzR5LcA7d9pEGq-iJNzTsiWI95NKGIY43OZao4euFcaXFvjFG2ohR1iEcuc1T_lRkkqD3x9zIhLHx1Vh_dBtsv1NUddwQ1uUVSMYKcqGNvdlI9iBffUACvL7vSmz0x5glkaYiTpeO_KlYdsIYQOL8Bf65IJsZdsySE9ZPZJFK5fltS91F9KGSR6p3pFyt-cI32QX08pnm1g0Q4D8jbQKWQw_Q0iSVgHh9xyafb498fngoZWyPuAgcv-dPTMU-BVEXMxwtDv7VjWr24HbMmtMojesqKH1sJ84GQ", 
  "expires_in": 3599, 
  "token_type": "Bearer", 
  "scope": "https://www.googleapis.com/auth/userinfo.email openid", 
  "refresh_token": "1//04N0g3EntRsgdCgYIARAAGAQSNwF-L9Irbx5t-GzXw7JV6X5XhgRNk0WqggB3KDAj47W-UDIvMS39xcW5ht8xQrCAALRT3CIf3Wg"
}
--------------------------------------------------------------------

2.Call the /tokeninfo api with the "id_token" for userdetails
String tokenInfoRequestUri = "https://oauth2.googleapis.com/tokeninfo?id_token=";

/** 2.userInfor from the Google Server with the TokenId by using the /tokeninfo?id_token=?1 **/
ResponseEntity<Map> tokenInfoApiResponse = restTemplate.getForEntity(finalTokenInfoRequestUri, Map.class);

Response -----
{
  "iss": "https://accounts.google.com",
  "azp": "329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com",
  "aud": "329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com",
  "sub": "102844744958118673581",
  "email": "suraj19rastogi@gmail.com",
  "email_verified": true,
  "at_hash": "Rften10D-q2vqTTsETMkjA",
  "iat": 1768373284,
  "exp": 1768376884
}

----------------------------------------------------------------

3.Get the other userDetails like name
String otherUserInfoGoogleApi = "https://openidconnect.googleapis.com/v1/userinfo";
//Retrieve others details of the User using the Google API
HttpHeaders newHeaders = new HttpHeaders();
newHeaders.add("Authorization", "Bearer "+accessTokenGoogle);
				
Map<String,String> newRequestParam = new HashMap<>();
newRequestParam.put("scope","openid profile email");
				
HttpEntity<Map> newEntity = new HttpEntity<Map>(newRequestParam,newHeaders);
				
ResponseEntity<Map> otherUserInfoGoogleApiResponse = 
		restTemplate.exchange(otherUserInfoGoogleApi, HttpMethod.GET, newEntity, Map.class);

------------------------------------------------------------------
curl --request GET \
  --url 'https://openidconnect.googleapis.com/v1/userinfo?scope=openid%20profile' \
  --header 'Authorization: Bearer ya29.a0AUMWg_LIuQJyIwttPoysUWDeJkCMdtMUwN5azqytzoqdxYS6SsyLIGJBuNlnEQG_X_qAsQGTuZK9gDyUHjK1_3j5kNf6Ky94JHgtFinuPlwzmTIAqPrzyOaxil0Jpie71jWMqPs-G_ry5FNzKsCaJUfsGPtAJzNCuAjiAQHwiuqqC2mX-ar-vhHZ0il01zpkpqqziMcaCgYKAbwSARMSFQHGX2MizC44c7zX61eJ3q5J9XdOSQ0206' \
  --header 'User-Agent: insomnia/12.2.0'
  
Response--
{
	"sub": "102844744958118673581",
	"name": "Suraj Rastogi",
	"picture": "https://lh3.googleusercontent.com/a-/ALV-UjVnTtdR9HO_aHZi5-qyVJQsJ5DGbssJ9E6yNoZRDqHXnKZA8Q=s96-c",
	"email": "suraj19rastogi@gmail.com",
	"email_verified": true
}

------------------------------------------------------------------------------------------------
#.Other UserInfo Google API 

curl --request GET \
  --url 'https://openidconnect.googleapis.com/v1/userinfo?scope=openid%20profile%20email' \
  --header 'Authorization: Bearer ya29.a0AUMWg_IvMLrVw5xzeMBza6tUnmGhGSQ80eEehCMdG87yp7hVSYOawAcaUiUJyuvqpU2pKJjJ4Yt50_8QTtWBie6TsbNJ8RM3X2Luy411HoxY4Wqxy7JiZy2oIg6pivpzzg-zWItbyq0QxanCvkIyjl8lbgEHPJcqgJlhSryRSd23PQjxtLWXztdr-StQ9kJBK-Vqr-gaCgYKAf4SARMSFQHGX2MiTgy8V_jxTMcP_26FgvyHNQ0206' \
  --header 'User-Agent: insomnia/12.2.0'
  
https://openidconnect.googleapis.com/v1/userinfo?scope=openid%20profile%20email

Headers --
Authorization - Bearer ya29.a0AUMWg_IvMLrVw5xzeMBza6tUnmGhGSQ80eEehCMdG87yp7hVSYOawAcaUiUJyuvqpU2pKJjJ4Yt50_8QTtWBie6TsbNJ8RM3X2Luy411HoxY4Wqxy7JiZy2oIg6pivpzzg-zWItbyq0QxanCvkIyjl8lbgEHPJcqgJlhSryRSd23PQjxtLWXztdr-StQ9kJBK-Vqr-gaCgYKAf4SARMSFQHGX2MiTgy8V_jxTMcP_26FgvyHNQ0206

Response --
{
	"sub": "102844744958118673581",
	"name": "Suraj Rastogi",
	"picture": "https://lh3.googleusercontent.com/a-/ALV-UjVnTtdR9HO_aHZi5-qyVJQsJ5DGbssJ9E6yNoZRDqHXnKZA8Q=s96-c",
	"email": "suraj19rastogi@gmail.com",
	"email_verified": true
}

----------------------------------------------------------------------------------------------
2./tokeninfo?id_token=?1

Response <200 OK
{iss=https://accounts.google.com, 
azp=329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com, 
aud=329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com, 
sub=102844744958118673581, 
email=suraj19rastogi@gmail.com, 
email_verified=true, 
at_hash=Rften10D-q2vqTTsETMkjA, 
iat=1768373284, 
exp=1768376884, 
alg=RS256, 
kid=7bf595489a0bb158b085e23e7b52bf9891e04538, 
typ=JWT
}

#.INFO inside "id_token" "sub" is unique UserId 
{
  "iss": "https://accounts.google.com",
  "azp": "329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com",
  "aud": "329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com",
  "sub": "102844744958118673581",
  "email": "suraj19rastogi@gmail.com",
  "email_verified": true,
  "at_hash": "Rften10D-q2vqTTsETMkjA",
  "iat": 1768373284,
  "exp": 1768376884
}

1./token API 
Response <200 OK 
{
access_token=ya29.a0AUMWg_LIuQJyIwttPoysUWDeJkCMdtMUwN5azqytzoqdxYS6SsyLIGJBuNlnEQG_X_qAsQGTuZK9gDyUHjK1_3j5kNf6Ky94JHgtFinuPlwzmTIAqPrzyOaxil0Jpie71jWMqPs-G_ry5FNzKsCaJUfsGPtAJzNCuAjiAQHwiuqqC2mX-ar-vhHZ0il01zpkpqqziMcaCgYKAbwSARMSFQHGX2MizC44c7zX61eJ3q5J9XdOSQ0206, 
expires_in=3599, 
refresh_token=1//0g-nc76fxCbobCgYIARAAGBASNwF-L9Irkpjjqubn5g4XzjmbU13VE8ZYOOBymlLRqaWKLFYypokmhjbXW6ZPRinNkhyh5zJSKJI, 
scope=https://www.googleapis.com/auth/userinfo.email openid, 
token_type=Bearer, 
id_token=eyJhbGciOiJSUzI1NiIsImtpZCI6IjdiZjU5NTQ4OWEwYmIxNThiMDg1ZTIzZTdiNTJiZjk4OTFlMDQ1MzgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIzMjk2NzYwMzI1MzktNmhqMnNxc3RvbTNzMTNvdWFyajg1OWpzZGZ0MTByMmUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIzMjk2NzYwMzI1MzktNmhqMnNxc3RvbTNzMTNvdWFyajg1OWpzZGZ0MTByMmUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDI4NDQ3NDQ5NTgxMTg2NzM1ODEiLCJlbWFpbCI6InN1cmFqMTlyYXN0b2dpQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiUmZ0ZW4xMEQtcTJ2cVRUc0VUTWtqQSIsImlhdCI6MTc2ODM3MzI4NCwiZXhwIjoxNzY4Mzc2ODg0fQ.e8B6Qm3jjjich0eXX6szn5edr5Pec2cO2reFvuoX4_6X_HCn9-Qcn4xMcMdsbVsx8O4EyvBuuRRJuQSus-3YU6pV8xtAMjKM-iF9DHIo9uPDUtfJJqKo2rM4mLvOZcYjSIFCw0CjeNxL6L49QyxfDDEX1ClHU_PgJa4QpIoF2PKNul7Ar-IkSNm5scNhTkrQ-O8iva6lvXfonZ6tfsiRk0Q59mcCStWLK642Ism5JPA5bXZA_T7BrTG6Qe0_tfF_hBUCVDnDjlqLyG1V1I7qTk1gDZY0yfmbPuR9y3wM90P0sGyO0Naa2vxJb5j5FKnpFxqy4vrOqTmQnkL15Vi3cw
},

----Request for Token Access
https://developers.google.com/oauthplayground
Request / Response
POST /token HTTP/1.1
Host: oauth2.googleapis.com
Content-length: 317
content-type: application/x-www-form-urlencoded
user-agent: google-oauth-playground
code=4%2F0ASc3gC0I5bRACziIcPGP9Ynyc3eEsXcJ-iYyz8Rpszyuf3py1sjM3YftKAucdmC7VO7JJQ
&redirect_uri=https%3A%2F%2Fdevelopers.google.com%2Foauthplayground
&client_id=329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com
&client_secret=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
&scope=
&grant_type=authorization_code

