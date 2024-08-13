Elemental Concept Tech Test
Overview:

The Elemental Concept Tech Test is a backend project built with Java and Spring Boot, designed to demonstrate core software engineering concepts, particularly focused on file processing, IP validation, and request logging. The project is structured to simulate a typical real-world application where files are uploaded via a REST API, processed, and the results are returned while ensuring that incoming requests are validated based on IP address information.

Tech stack:
Java 17
Spring Boot
Spring Data JPA
MySQL
JUnit 5
WireMock
Maven

Key Components:
File Processing Service:
FileProcessingService: This service handles the processing of uploaded files. It reads the file's content, validates it according to certain business rules, and returns a list of OutputRecord objects representing the processed data.
FileProcessingController: Exposes a REST API endpoint (/api/files/process) that accepts file uploads. The uploaded file is passed to the FileProcessingService for processing. The controller also handles request validation and logging.
IP Validation Service:
IPValidationService: A service responsible for validating the IP address of incoming requests. It makes an external API call to ip-api.com to retrieve geographical and ISP information about the IP address. The service checks if the IP is from a blocked country or ISP and returns an IpInfo object containing the validation results.
WireMock Tests: The service is thoroughly tested using WireMock to simulate external API responses, ensuring the service behaves correctly under various scenarios, such as blocked countries or ISPs.
Request Logging:
RequestLog: A JPA entity that captures details of each incoming request, including the IP address, country code, ISP, URI, response code, and processing time.
RequestLogRepository: A Spring Data JPA repository for persisting RequestLog entities into a MySQL database.
The FileProcessingController uses the RequestLogRepository to store request logs after processing each file upload, enabling auditability and tracking.
Database Integration:
The project uses MySQL as the primary database. All incoming requests and their associated metadata are logged into the request_log table.
The database schema is managed through JPA annotations in the RequestLog entity class, ensuring seamless integration and management of relational data.
Configuration:
Application Properties: Configuration settings such as the root URL for IP validation are managed through Spring's application.properties or environment variables. The project is flexible, allowing configurations like skipping file validation or changing the external API URL through properties.
Testing:
Unit and integration tests are implemented using JUnit 5 and WireMock. The project emphasizes test-driven development (TDD) principles by covering various edge cases, such as handling blocked countries/ISPs and ensuring that all components work together as expected.

To run the project use the following configuration

![image](https://github.com/user-attachments/assets/30e03e9e-b16c-4b1c-9acc-cc8bbf059a86)

DB Configuration.
1. Install MySQL
2. CREATE DATABASE file_processing_db;
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

