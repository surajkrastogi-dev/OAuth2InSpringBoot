1.UserDetails

 CREATE TABLE user_detail (
    user_id INT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(100) NOT NULL,
    mobile_no CHAR(10) NOT NULL ,
    email VARCHAR(50) NOT NULL ,
    passwords VARCHAR(100) NOT NULL,
    pincode CHAR(6),
    state_code VARCHAR(20),
    country VARCHAR(20),
    pan_no CHAR(10) ,
    aadhar_no CHAR(12) ,
    date_of_birth DATE,
    address VARCHAR(100),
	created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,,
	active_flag BOOLEAN;
    CONSTRAINT pk_user_id PRIMARY KEY (user_id)
);

select * from user_detail;

INSERT INTO user_detail (  user_name, mobile_no, email, passwords, pincode,state_code,country,pan_no, aadhar_no, date_of_birth,address,
created_on,updated_on,active_flag )
VALUES ( 'Suraj Rastogi','8726664795','suraj@gmail.com','suraj123','221005','UP','India', NULL,
  '240987657890','1999-09-16','New Saket Nagar Colony, Sankat Mochan, Varanasi',now(),now(),true );


INSERT INTO user_detail(user_name ,mobile_no, email ,  passwords , pincode,  state_code , country , pan_no , aadhar_no , date_of_birth, address,
created_on,updated_on,active_flag )
VALUES('Shyam Kumar','8726664875','shyamk@gmail.com','shyam123','110074','DL','India',null,'240987657890','2005-09-12',
'New Saket Nagar Colony,Sankat Mochan, Chhattarpur',now(),now(),true);




ALTER TABLE user_detail
ADD column created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE user_detail
ADD column updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE user_detail
ADD column active_flag BOOLEAN;

ALTER TABLE defaultdb.user_detail
ADD COLUMN is_google_oauth_login boolean;

-----------------------------------------------------------------------------------------------------------------------------

2. Master-Role 

CREATE TABLE IF NOT exists master_roles (
  role_id int NOT NULL AUTO_INCREMENT,
  role_name varchar(50) NOT NULL,
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  active_flag BOOLEAN,
  CONSTRAINT pk_user_id PRIMARY KEY (role_id)
);

select * from master_roles;

INSERT INTO master_roles(role_name,created_on,updated_on,active_flag)
VALUES('USER',now(),now(),true), ('ADMIN',now(),now(),true);

--------------------------------------------------------------------------------------------------------------------------------
3.User_vs_roles


CREATE TABLE IF NOT EXISTS user_vs_roles(
  user_id INT NOT NULL,
  role_id INT NOT NULL ,
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  active_flag BOOLEAN,
  CONSTRAINT pk_user_vs_roles PRIMARY KEY (user_id,role_id),
  CONSTRAINT fk_user_vs_roles_role_id FOREIGN KEY (role_id)
	REFERENCES master_roles (role_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

select * from user_vs_roles;

INSERT INTO user_vs_roles(user_id,role_id,created_on,updated_on,active_flag)
VALUES(1,1,now(),now(),true),
(1,2,now(),now(),true),
(2,1,now(),now(),true);


-------------------------------------------------------------------------------------------------------------------------------------
#.New Table refresh_token Query 
select * from user_detail;

ALTER TABLE user_detail
ADD CONSTRAINT uq_user_email UNIQUE (email);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    token VARCHAR(500) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_refresh_user
        FOREIGN KEY (email)
        REFERENCES user_detail(email)
        ON DELETE CASCADE
);

select * from refresh_tokens;

ALTER TABLE refresh_tokens
ADD COLUMN device_id varchar(100);

ALTER TABLE refresh_tokens
ADD COLUMN revoked boolean default false;

ALTER TABLE refresh_tokens
ADD COLUMN finger_print varchar(64);

-------------------------------------------------------------------------------------
#.Create New Table for User Login History 
CREATE TABLE IF NOT EXISTS defaultdb.login_history(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	email VARCHAR(100),
	device_id VARCHAR(100),
	ip_address varchar(45),
	user_agent varchar(255),
	login_time TIMESTAMP,
	success boolean
);
	
#.Add Field in refresh_tokens 
ALTER TABLE refresh_tokens
ADD COLUMN finger_print varchar(64);

------------------------------------------------------------------------------------
#.Create New Table refresh_token_attempts
CREATE TABLE IF NOT EXISTS defaultdb.refresh_token_attempts(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	refresh_token VARCHAR(500),
	attempt_time TIMESTAMP,
	email VARCHAR(100),
	device_id VARCHAR(200)
);

ALTER TABLE refresh_token_attempts
ADD COLUMN email VARCHAR(100);

ALTER TABLE refresh_token_attempts
ADD COLUMN	device_id VARCHAR(200);

------------------------------------------------------------------------------------
