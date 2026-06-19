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
