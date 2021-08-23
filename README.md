## Tabeo App Selenium Tests

[Manual Test Cases](https://docs.google.com/document/d/1zsMuGfmutuWqNOJcDFVWDsHQwtGffO0G1CjPKU0yhtw/edit?usp=sharing) covers the sign-up and payment user flow for [Tabeo](https://qa-challenge-tabeo.vercel.app/) web application that allows downloading .zip files after successful payment by authorized users.


Current repo contains java - based automated scripts for regression suite of sign-up and payment test cases with positive and negative scenaries.
Regression suite contains 14 different tests that covers almost all manual cases.
1 failed test added to demonstrate test reporting with sceenshot attachment.

**Stack of technologies:** *java, maven, Selenium Webdriver, TestNG, allure-report*

Average time spent: ~18 h

### Requirements to run automation tests
* Mac OS X
* Installed [Apache Maven](https://maven.apache.org/install.html)
* Installed Java SE 11
* Installed Chrome browser with latest version

### To run autotests:
* `git clone git@github.com:Mynziak/tabeo_app.git`  clone project on local OSX machine
* `cd {root_prj_dir} +/tabeo_app` cd to root project dir
* `mvn compile` compile maven based java project
* `mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/xmls/tabeo_regression_tests.xml` run regression suite and wait for finishing

### Open Test Report
* `allure serve target/allure-results` run after autotests completion to generate allure-report with test results

