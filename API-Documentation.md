----------------#.API Documentation for the SpringBootJWTApplication Project -----------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------
#. USER ROLE Register and Login API

1.Register User API 

curl --request POST \
  --url http://localhost:8292/api/auth/register \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
  "userName": "Arvind Kejriwal",
  "mobileNo": "8726664795",
  "email": "arvind@example.com",
  "password": "arvind123",
  "pincode": "221005",
  "stateCode": "UP",
  "country": "India",
  "panNo": "ABCDE1234F",
  "aadharNo": "123456789012",
  "dateOfBirth": "1999-09-16",
  "address": "New Saket Nagar Colony, Sankat Mochan, Varanasi"
}
'
---Response---
{
	"success": true,
	"message": "User registered successfully"
}
------------------------------------------------------------------------------------------------------------------------
2.Login User API 

curl --request POST \
  --url http://localhost:8292/api/auth/login \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
	"email": "arvind@example.com",
	"password": "arvind123"
}'

---Response---
{
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnZpbmRAZXhhbXBsZS5jb20iLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY3NjAyNjQ3LCJleHAiOjE3Njc2ODkwNDd9.MQDGAJpf5sdOAgEXrclb1aW4maKXJNMDTkrdF50NsJw",
	"message": "Login successfull!"
}
-----------------------------------------------------------------------------------------------------------------------------------
#. ADMIN ROLE Register and Login API

3.Register Admin API

curl --request POST \
  --url http://localhost:8292/api/auth/registerAdmin \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
  "userName": "Shaurya Gupta",
  "mobileNo": "8726664795",
  "email": "shaurya@example.com",
  "password": "arvind123",
  "pincode": "221005",
  "stateCode": "UP",
  "country": "India",
  "panNo": "ABCDE1234F",
  "aadharNo": "123456789012",
  "dateOfBirth": "1999-09-16",
  "address": "New Saket Nagar Colony, Sankat Mochan, Varanasi"
}
'
---Response---
{
	"success": true,
	"message": "User registered successfully"
}
-------------------------------------------------------------------------------------------------------------------------
4.Login Admin API 

curl --request POST \
  --url http://localhost:8292/api/auth/login \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
  "email": "shaurya@example.com",
  "password": "arvind123"
}'

---Response---
{
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGF1cnlhQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE3Njc0MzY1MzMsImV4cCI6MTc2NzUyMjkzM30.PevGIPnnBCza9zERy8_Rok1s2CSA1qS08lVJAZ640KI",
	"message": "Login successful!"
}
----------------------------------------------------------------------------------------------------------------------------------
#.Get Secured API Access with Autorization Headers only User who have JWT Token granted can access these API 

5.Secure API for both ROLE Admin and User 

curl --request GET \
  --url http://localhost:8292/api/test/secure \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnZpbmRAZXhhbXBsZS5jb20iLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY3NDM2MjE3LCJleHAiOjE3Njc1MjI2MTd9.VH8X8WBMK1KVfYp1TY8sic6DE8aXTx_v1FQ3sLn8DT8' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8

---Response---
JWT Access Granted for secured user only!

--------------------------------------------------------------------------------------------------------- 
6.API for User ROLE Only

curl --request GET \
  --url http://localhost:8292/api/test/user \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnZpbmRAZXhhbXBsZS5jb20iLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzY3NDM2MjE3LCJleHAiOjE3Njc1MjI2MTd9.VH8X8WBMK1KVfYp1TY8sic6DE8aXTx_v1FQ3sLn8DT8' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8
  
---Response---
JWT User Access Granted!

 -------------------------------------------------------------------------------------------------------- 
7.API for Admin ROLE Only

curl --request GET \
  --url http://localhost:8292/api/test/admin \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGF1cnlhQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE3Njc0MzY1MzMsImV4cCI6MTc2NzUyMjkzM30.PevGIPnnBCza9zERy8_Rok1s2CSA1qS08lVJAZ640KI' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8
  
---Response---
JWT Admin Access Granted!

--------------------------------------------------------------------------------------------------------------
8.API for Both User and Admin Role Only

curl --request GET \
  --url http://localhost:8292/api/test/userActivity \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnZpbmRAZXhhbXBsZS5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE3Njc2MDU0MDUsImV4cCI6MTc2NzYwNjMwNX0.s8gZn7pyEA9va1JVLKmZWmo3GHh827LVk6sr9Pk2FS4' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8


-----------------------------------------------------------------------------------------------------------------------------------
---------------- Refresh Token and Access Token Logic ---------------------------
#.Refresh Token and Access Token Login API

curl --request POST \
  --url http://localhost:8292/api/auth/refreshlogin \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
	"email": "arvind@example.com",
	"password": "arvind123"
}'

---Response---
{
	"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnZpbmRAZXhhbXBsZS5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE3Njc2MDI3MjEsImV4cCI6MTc2NzYwMzYyMX0.nJgacRjvg5GInV8igW0CbBZsZw9Ewq8AzeZf18AUDFQ",
	"refreshToken": "bb52194b-8df8-418e-90f5-db2370d7cc04",
	"message": "Refresh Login SuccessFull!"
}
#.New Refresh Login API payload with deviceId for Multi-device Login
curl --request POST \
  --url http://localhost:8292/api/auth/refreshlogin \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
	"email": "arvind@example.com",
	"password": "arvind123",
	"deviceId":"WEBSITE"
}'

Response---
{
	"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnZpbmRAZXhhbXBsZS5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE3Njc5MzM1MjMsImV4cCI6MTc2NzkzNDQyM30.v8sY3ayDXHzhaQOBZ8h9_Lq-KMGHwhQQogjpYO02MXQ",
	"refreshToken": "73c42ee5-8fb2-436c-a4f5-f96cf211129a",
	"message": "Refresh Login SuccessFull!"
}

#.JWT Decoded Payload JSON -----

{
  "sub": "arvind@example.com",
  "roles": [
    {
      "authority": "ROLE_USER"
    }
  ],
  "iat": 1767602721,
  "exp": 1767603621
}
--------------------------------------------------------------------------------------------------------------------------
#.Refresh API for new AccessToken Generation

curl --request POST \
  --url http://localhost:8292/api/auth/refresh \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
  "refreshToken": "bb52194b-8df8-418e-90f5-db2370d7cc04"
}'

---Response---
{
	"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnZpbmRAZXhhbXBsZS5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE3Njc2MDMwNzIsImV4cCI6MTc2NzYwMzk3Mn0.L2Gpp16Zj-SnL__JJWFhadTWyZMmxGhKE3_-mkLfvm8",
	"refreshToken": "bb52194b-8df8-418e-90f5-db2370d7cc04"
}

---------------------------------------------------------------------------------------------------------------------
#.Logout User API which delete the Refresh Token of User from DB

curl --request POST \
  --url http://localhost:8292/api/auth/logout \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
  "username": "shaurya@example.com"
}'

---Response---
{
	"success": true,
	"message": "Logged out Success"
}

#.New Logout From All-devices
curl --request POST \
  --url http://localhost:8292/api/auth/logoutAllDevice \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
  "username": "arvind@example.com"
}'
Response---
{
	"success": true,
	"message": "Logged out Success"
}

#.New Logout From single-device API with token
curl --request POST \
  --url http://localhost:8292/api/auth/logout \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/12.2.0' \
  --cookie JSESSIONID=170D7A3EA8A1B188DF40368DD78AB2D8 \
  --data '{
  "token": "73c42ee5-8fb2-436c-a4f5-f96cf211129a"
}'
Response---
{
	"success": true,
	"message": "Logged out Success"
}
--------------------------------------------------------------------------------------------------------------------
