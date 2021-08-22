## Tabeo App Selenium Tests

[Manual Test Cases](https://docs.google.com/document/d/1zsMuGfmutuWqNOJcDFVWDsHQwtGffO0G1CjPKU0yhtw/edit?usp=sharing) that covers the sign-up and payment flow.

Current repo contains Java-based application with automated scripts of regression suite of test cases that used next stack of technologies: Selenium WebDriver, Maven, TestNG
Suite contains 14 different tests that covers almost all manual cases.

### Requirements to run automation tests

* Mac OS X
* Installed [Apache Maven](https://maven.apache.org/install.html)
* Installed Java

### To run autotests:
* clone project on local OS X machine
* cd to project dir
* `mvn compile`
* `mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/xmls/tabeo_regression_tests.xml` and wait for finishing

### Open Test Report
After the completion of the autotests
run `allure serve target/allure-results` to generate allure-report with test results

