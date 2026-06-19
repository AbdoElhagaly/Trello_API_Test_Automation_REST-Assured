# Trello API Test Automation (REST Assured)

A Java-based API test automation framework for the [Trello REST API](https://developer.atlassian.com/cloud/trello/rest/), built with **REST Assured** and **TestNG**, with **Allure** reporting for test result visualization.

The framework covers full CRUD (Create, Read, Update, Delete) operations across Trello's core resources — **Boards, Lists, Cards, and Checklists** — and chains tests together using `dependsOnMethods` to simulate a realistic, end-to-end workflow.

---

## ✨ Features

- ✅ Full CRUD coverage for **Boards**, **Lists**, **Cards**, and **Checklists**
- 🔗 Sequential, dependency-chained test flow (`dependsOnMethods`) that mirrors a real user journey — create a board → create a list → create a card → create a checklist → update → delete
- 🚫 Negative/invalid test scenarios (invalid API key, invalid token, invalid board/card IDs) to verify proper error handling (`401`, `400`, `404`)
- 📊 Allure reporting integration for clean, readable HTML test reports
- 🧱 Centralized configuration (`Config.java`) for API credentials
- 🧪 Custom TestNG listener (`TestListeners`) for console-level pass/fail/skip logging
- 📂 Multiple TestNG XML suites for running specific subsets of the test chain

---

## 🛠️ Tech Stack

| Tool | Purpose |
|---|---|
| **Java** | Core language |
| **REST Assured** | HTTP API testing library |
| **TestNG** | Test runner & lifecycle management |
| **Allure** | Test reporting |
| **AspectJ** | Required for Allure step/attachment instrumentation |
| **Maven** | Build & dependency management |

---

## 📁 Project Structure

TrelloApi/

├── src/

│   └── test/

│       ├── java/

│       │   ├── BaseTest.java              # Sets RestAssured base URI, setup/teardown

│       │   ├── Config.java                # API key & token configuration

│       │   ├── ValidTestCases.java        # All test cases (CRUD + negative scenarios)

│       │   └── CustomListeners/

│       │       └── TestListeners.java     # Console logging for test results

│       └── resources/

│           └── META-INF/services/

│               └── org.testng.ITestNGListener

├── allure-results/                        # Raw Allure result files (generated after a run)

├── createChecklist.xml                    # TestNG suite: run flow up to checklist creation

├── getDeletedCard.xml                     # TestNG suite: run flow up to card deletion

├── getListById.xml                        # TestNG suite: run flow up to fetching a list

├── pom.xml                                # Maven build configuration

└── README.md

---

## 🔗 Test Flow

The test cases in `ValidTestCases.java` are chained in a single logical sequence using `dependsOnMethods`, so each test builds on the resource (board/list/card/checklist) created by the previous one:

1. **Create board** → 2. *Invalid key (401)* → 3. **Get board** → 4. **Create list** → 5. *Invalid board ID (400)* → 6. **Get list** → 7. **Create card** → 8. *Invalid token (401)* → 9. **Get card** → 10. **Create checklist** → 11. *Invalid card ID (400)* → 12. **Get checklist** → 13. **Update board name** → 14. **Verify update** → 15. **Delete checklist** → 16. *Get deleted checklist (404)* → 17. **Delete card** → 18. *Get deleted card (404)* → 19. **Archive list** → 20. **Verify archived** → 21. **Delete board** → 22. *Get deleted list (404)* → 23. *Get deleted board (404)*

Each step validates the relevant status code (`200`, `400`, or `401`/`404` for negative cases) and key response fields (e.g. `name`, `desc`, `closed`, `idBoard`, `idCard`).

---

## ⚙️ Setup & Configuration

### Prerequisites
- Java JDK (see `pom.xml` for the configured compiler version)
- Maven
- A free [Trello account](https://trello.com/) with an API Key and Token ([generate them here](https://trello.com/power-ups/admin))

### Configure credentials

> ⚠️ **Security note:** Never commit real API keys/tokens to a public repository. Trello tokens grant access to your boards and data.

Edit `src/test/java/Config.java` and supply your own credentials — preferably via environment variables rather than hardcoding them:

```java
public class Config {
    protected static final String key = System.getenv("TRELLO_API_KEY");
    protected static final String token = System.getenv("TRELLO_API_TOKEN");
}
```

Then set the environment variables locally:

```bash
export TRELLO_API_KEY=your_key_here
export TRELLO_API_TOKEN=your_token_here
```

If you previously pushed real credentials to this repo, **regenerate your Trello token immediately** from your Trello account settings — pushing to a public repo exposes it to anyone.

---

## ▶️ Running the Tests

Run the full suite with Maven:

```bash
mvn clean test
```

Run a specific TestNG XML suite (e.g. to stop the chain at checklist creation):

```bash
mvn test -DsuiteXmlFile=createChecklist.xml
```

---

## 📊 Generating the Allure Report

After running the tests, results are written to `allure-results/`. Generate and open the HTML report with:

```bash
allure serve allure-results
```

or, if you have the Allure CLI installed separately:

```bash
allure generate allure-results --clean -o allure-report
allure open allure-report
```

---

## 🚧 Roadmap / Possible Improvements

- [ ] Move API key/token to environment variables or a `.gitignore`'d properties file (and rotate any previously exposed token)
- [ ] Add data-driven tests using JSON/Excel test data
- [ ] Add response time / performance assertions
- [ ] Integrate with CI (GitHub Actions) to run tests and publish the Allure report automatically
- [ ] Expand coverage to additional Trello endpoints (members, labels, attachments)

---

## 👤 Author

**Abdo Elhagaly**
Test Automation Engineer — Java | REST Assured | Selenium | TestNG | Allure
