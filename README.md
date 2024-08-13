To run the project use the following configuration

![image](https://github.com/user-attachments/assets/30e03e9e-b16c-4b1c-9acc-cc8bbf059a86)

DB Configuration.
1. Install MySQL
2. Create DB (CREATE DATABASE file_processing_db;)
3. CREATE TABLE request_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id CHAR(36) NOT NULL,
    request_uri VARCHAR(255) NOT NULL,
    request_timestamp DATETIME NOT NULL,
    response_code INT NOT NULL,
    request_ip_address VARCHAR(45) NOT NULL,
    request_country_code VARCHAR(10),
    request_ip_provider VARCHAR(255),
    time_lapsed BIGINT NOT NULL
    );

To run a file through use Postman with the following setup. Make sure your in Debug mode to override the internal IP adrress as that will fail the validation.

![image](https://github.com/user-attachments/assets/d0275427-3641-4a49-8b84-b82400bd96e2)
![image](https://github.com/user-attachments/assets/32e6b672-23e5-4b8d-89ec-c0bc47c4731a)

To enable or disable validation update the application properties acordigly 

![image](https://github.com/user-attachments/assets/38895d84-7777-44b9-b7cf-cfe83d7101ea)

