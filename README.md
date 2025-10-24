# 🧩 Player API Automation Framework

Automation framework for testing REST API of the **Player Management System**  
(covering CRUD operations: Create, Get, Update, Delete).

Built with **Java 11+, RestAssured, TestNG, Allure, Maven**, and custom configuration  
for parallel execution, environment setup, and reporting.

---

## 🚀 Tech Stack

| Component | Purpose |
|------------|----------|
| **Java 11+** | Core language |
| **RestAssured** | API testing library |
| **TestNG** | Test runner, assertions, data providers |
| **Allure** | Reporting and logging |
| **Maven** | Build and dependency management |
| **Jackson** | JSON serialization/deserialization |
| **Log4j2** | Logging |
| **CI/CD** | Jenkins / GitHub Actions ready |

---

## ⚙️ Configuration

### 🧩 `СonfigProvider.java`
Defines base URI and request spec used by all clients:
```
Override base URL dynamically:
```bash
mvn test -Dbase.url=http://staging.example.com
```

### ⚙️ `configuration.properties`
```properties
base.url=http://3.68.165.45
thread.count=5
create.thread.count=3
update.thread.count=3
get.thread.count=3
delete.thread.count=3

logging.enabled=true
```

---

## 🧠 Test Coverage

| Module | Endpoint | Description | Status |
|--------|-----------|--------------|--------|
| **PlayerCreateTests** | `GET /player/create/{editor}` | Positive + Negative validation + Auth | ✅ |
| **PlayerGetTests** | `POST /player/get`, `GET /player/get/all` | Get by ID, list all | ✅ |
| **PlayerUpdateTests** | `PATCH /player/update/{editor}/{id}` | Field-level updates + role logic + duplicate checks | ✅ |
| **PlayerDeleteTests** | `DELETE /player/delete/{editor}` | Delete access control | ✅ |

---

## 🔬 Test Logic Highlights

- **DTO-driven approach** — request/response models via builders (`PlayerCreateRequestDto`, `PlayerUpdateRequestDto`, etc.)
- **JSON-based DataProviders** for flexible parametrization.
- **Role-based access matrix**:
  ```
  supervisor → can edit all except other supervisors  
  admin      → can edit self, users, admins (not supervisors)  
  user       → can edit only self
  ```
- **Parallel execution** via TestNG suites and dynamic threads.
- **SoftAssert** to verify both changed and unchanged fields.
- **Automatic cleanup** using `TestDataHelper.registerForCleanup()`.

---

## 🧪 Example Test Flows

### 🟢 PlayerCreateTests
- Supervisor creates new users (`admin` / `user`)
- Validation:
  - Age boundaries (16–60)
  - Password format
  - Role access (403 / 401)
  - Uniqueness of `login` and `screenName`

### 🟡 PlayerUpdateTests
- Supports full or partial PATCH updates.
- Includes:
  - Duplicate `login` and `screenName` (409)
  - Invalid field values (400)
  - Role-based restrictions (403)

### 🔴 PlayerDeleteTests
- Tests delete access per role.
- Supervisor cannot delete another supervisor.

---

## 🧰 Running Tests

### ▶️ Run all tests
```bash
mvn clean test
```

### ▶️ Custom threads and environment
```bash
mvn clean test -Dthread.count=5 -Dbase.url=http://staging.env
```

### ▶️ Run specific suite
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/suites/player_create_suite.xml -Dcreate.thread.count=5
```

---

## 📊 Allure Report

### ▶️ Serve report
```bash
allure serve allure-results
```

### ▶️ Generate static HTML report
```bash
allure generate allure-results --clean -o allure-report
```

**Report includes:**
- Request/Response logs  
- CURL equivalents via `CurlHelper`  
- Steps from `AllureHelper`  
- Error trace & attachments  

---

## 🧩 Key Utilities

| Class | Description |
|--------|--------------|
| **AllureHelper** | Attaches requests/responses & custom logs to Allure |
| **CurlHelper** | Generates CURL command from RestAssured request |
| **TestDataHelper** | Creates dynamic players and performs cleanup |
| **TestListener** | Global Allure listener for logging and reporting |

---

## ✅ Future Improvements

- Add JSON Schema validation  
- Integrate DB verification layer  
- Add environment profiles (`dev`, `qa`, `prod`)  
- Extend Allure templates for request/response prettification  

---

## ⚡ Quick Start

```bash
# 1️⃣ Clone repo
git clone https://github.com/ValeriiaKyba/interview_test_app.git
cd interview_test_app

# 2️⃣ Install dependencies
mvn clean install

# 3️⃣ Run tests
mvn test -Dbase.url=http://3.68.165.45 -Dthread.count=3

# 4️⃣ View Allure report
allure serve allure-results
```

---

## 👩‍💻 Author

**QA Automation Engineer:** Kyba Valeriia  
**Role:** QA Java Automation Engineer  
**Experience:** 4.5 years in API/Backend Automation
