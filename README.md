# SpringSecurityWithOAuth2
Implemented the Spring Security WIth the OAuth-2.0 in Microservices.
#. if any error of pull request rule voilation due to add the secret and password in the commit then use these git command
git checkout --orphan clean-main
git rm -rf --cached .
git add .
git commit -m "Clean config: remove all secrets and use env variables"
git branch -D main
git branch -m main
git push -u origin main --force

------------------------------------------------------

-DCLIENT_PORT=8292
-DDB_PASSWORD=AVNS_ZYXlR98IJmnhaUy3E3Y
-DDB_USERNAME=avnadmin
-DDB_URL=jdbc:mysql://mysql-1ac4844e-suraj19rastogi-febd.e.aivencloud.com:21956/defaultdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
-DDRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
-DJPA_DB_PLATFORM=org.hibernate.dialect.MySQL8Dialect
-DJPA_SHOW_SQL=true
-DHIBERNATE_FORMAT_SQL=true
-DHIBERNATE_DDL_AUTO=update
-DJWT_SECRET=jwtsecret$#dh6667newsecrefor66security
-DJWT_EXPIRY_MILLISEC=86400000
-DJWT_A_EXPIRY_MILLISEC=900000
-DJWT_R_EXPIRY_MILLISEC=604800000
-DRefresh_Token_Rate_limit_Time_in_Seconds=60
-DOAuth_Client_ID=329676032539-6hj2sqstom3s13ouarj859jsdft10r2e.apps.googleusercontent.com
-DOAuth_Client_SECRET=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
