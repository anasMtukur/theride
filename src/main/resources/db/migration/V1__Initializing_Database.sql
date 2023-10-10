CREATE SCHEMA IF NOT EXISTS theride;
SET SCHEMA 'theride';
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE auditable_entity
(
  created_at TIMESTAMP DEFAULT now() NOT NULL,
  updated_at TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE end_user
(
  id          UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  username    		VARCHAR(255)                                  NOT NULL,
  password    		VARCHAR(255)                                  NOT NULL,
  fullname    		VARCHAR(255)                                  NOT NULL,
  email       		VARCHAR(255)                                  NOT NULL,
  mobile       		VARCHAR(255)                                  NOT NULL,
  block_code  		VARCHAR(255)                                  	  NULL,
  is_deleted   		BOOLEAN DEFAULT FALSE                         NOT NULL,
  is_blocked   		BOOLEAN DEFAULT FALSE                         NOT NULL,
  reset_token 		TEXT										      NULL
)
INHERITS (auditable_entity);

CREATE TABLE log_login (
  id		UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) NOT NULL,
  end_user	UUID                                          NOT NULL,
  channel	VARCHAR(255)                                  NOT NULL
)INHERITS (auditable_entity);
ALTER TABLE log_login
  ADD FOREIGN KEY (end_user) REFERENCES end_user (id);

CREATE TABLE roles
(
  id          UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  name	    		VARCHAR(255)                                  NOT NULL
)
INHERITS (auditable_entity);

CREATE TABLE enduser_role
(
  id          UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  end_user    UUID                                  			  NOT NULL,
  role		  UUID                                  			  NOT NULL
)
INHERITS (auditable_entity);

CREATE UNIQUE INDEX enduser_unique_username_constraint
  ON end_user (username);
CREATE UNIQUE INDEX enduser_unique_email_constraint
  ON end_user (email);
ALTER TABLE end_user
  ADD CONSTRAINT email_lower CHECK (email = lower(email));
ALTER TABLE end_user ADD CONSTRAINT email_check_end_user CHECK (end_user.email ~* '^[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+[.][A-Za-z]+$');

ALTER TABLE enduser_role
  ADD FOREIGN KEY (end_user) REFERENCES end_user (id);
  
CREATE OR REPLACE FUNCTION check_username_availability(name TEXT)
  RETURNS BOOLEAN AS $$
BEGIN
  IF EXISTS(SELECT 1
            FROM end_user e
            WHERE e.username = name)
  THEN
    RETURN FALSE;
  ELSE
    RETURN TRUE;
  END IF;
END
$$ LANGUAGE plpgsql;
  
ALTER TABLE enduser_role
  ADD FOREIGN KEY (role) REFERENCES roles (id);
  
-- Create Driver & Car Tables
CREATE TABLE drivers
(
  id                UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  end_user          UUID                                  			  NOT NULL,
  first_name    	VARCHAR(100)                                  	  NOT NULL,
  last_name    		VARCHAR(100)                                  	  NOT NULL,
  gender    		VARCHAR(20)                                  	  NOT NULL,
  license_number    VARCHAR(255)                                  	  NOT NULL,
  active_city       VARCHAR(255)                                  	  NOT NULL,
  approval_status   VARCHAR(255)                                  	  NOT NULL
)
INHERITS (auditable_entity);

CREATE TABLE cars
(
  id          	UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  driver      	UUID                                  			  NOT NULL,
  car_type    	VARCHAR(20)                                  	  NOT NULL,
  color    		VARCHAR(255)                                  	  NOT NULL,
  brand_model   VARCHAR(255)                                  	  NOT NULL,
  plate_no   	VARCHAR(255)                                  	  NOT NULL

)
INHERITS (auditable_entity);

ALTER TABLE drivers
  ADD FOREIGN KEY (end_user) REFERENCES end_user (id);
  
ALTER TABLE cars
  ADD FOREIGN KEY (driver) REFERENCES drivers (id);

CREATE TABLE passengers
(
  id                UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  end_user          UUID                                  			  NOT NULL,
  first_name    	VARCHAR(100)                                  	  NOT NULL,
  last_name    		VARCHAR(100)                                  	  NOT NULL,
  gender    		VARCHAR(20)                                  	  NOT NULL
)
INHERITS (auditable_entity);

ALTER TABLE passengers
  ADD FOREIGN KEY (end_user) REFERENCES end_user (id);

CREATE TABLE locations
(
  id          	UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  driver      	UUID                                  			  NOT NULL,
  latitude    	VARCHAR(255)                                  	  NOT NULL,
  longitude 	VARCHAR(255)                                  	  NOT NULL,
  geo_hash_zone VARCHAR(255)                                  	  NOT NULL

)
INHERITS (auditable_entity);
  
ALTER TABLE locations
  ADD FOREIGN KEY (driver) REFERENCES drivers (id);

CREATE TABLE last_known_locations
(
  id          	UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  driver      	UUID                                  			  NOT NULL,
  location    	UUID                                          	  NOT NULL
)
INHERITS (auditable_entity);
  
ALTER TABLE last_known_locations
  ADD FOREIGN KEY (driver) REFERENCES drivers (id);

ALTER TABLE last_known_locations
  ADD FOREIGN KEY (location) REFERENCES locations (id);

CREATE TABLE bookings
(
  id          	UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  driver      	UUID                                  			  	  NULL,
  passenger    	UUID                                  			  NOT NULL,
  booking_type	VARCHAR(255)                                  	  NOT NULL,
  status	 	VARCHAR(255)                                  	  NOT NULL,
  geo_hash_zone VARCHAR(255)                                  	  NOT NULL
)
INHERITS (auditable_entity);
  
ALTER TABLE bookings
  ADD FOREIGN KEY (driver) REFERENCES drivers (id);

ALTER TABLE bookings
  ADD FOREIGN KEY (passenger) REFERENCES passengers (id);

CREATE TABLE booking_points
(
  id          	UUID PRIMARY KEY DEFAULT (uuid_generate_v1()) 	  NOT NULL,
  booking      	UUID                                  			  NOT NULL,
  latitude    	VARCHAR(255)                                  	  NOT NULL,
  longitude 	VARCHAR(255)                                  	  NOT NULL,
  point_type 	VARCHAR(255)                                  	  NOT NULL,
  geo_hash_zone VARCHAR(255)                                  	  NOT NULL

)
INHERITS (auditable_entity);
  
ALTER TABLE booking_points
  ADD FOREIGN KEY (booking) REFERENCES bookings (id);