# InvoiceApp

This is a simple full-stack application to **upload and view invoice records**.  
The application supports uploading a **CSV file** containing invoice data, which is then stored in a MySQL database and displayed via a web UI.

---

## 🚀 Features
- Upload invoice records via CSV file
- View paginated invoices
- Backend: Spring Boot (Java)
- Frontend: Angular
- Database: MySQL
- Containerized using Docker & Docker Compose

---

## 📂 CSV File Format
The CSV file must follow this format:
    
    
    Customer ID,Invoice Number,Invoice Date,Description,Amount
    CUST001,INV001,2025-01-01,Web Hosting,100.00
    CUST002,INV002,2025-01-05,Cloud Services,250.50  


A sample test file is available at Invoice/../test/test-data.csv

## 🐳 Run with Docker Compose

1. Clone the repository:

   ```bash
   git clone https://github.com/rronak1418/InvoiceApp.git
   cd InvoiceApp

3. Update **docker-compose.yml** with your MySQL credentials if necessary.
4. Database: MySQL
   Make sure you update **application.properties** with your MySQL username and password.
5. Start the application with **docker-compose up --build**.
6. Test cases can be run with "mvn test".
