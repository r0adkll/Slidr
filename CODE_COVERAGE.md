# Code Coverage Report generation

To generate the code coverage report, execute the following command:

For windows:

> gradlew cleanTest testDebugUnitTestCoverage

For linux and OSx

> ./gradlew cleanTest testDebugUnitTestCoverage

This will generate code coverage report in each of the projects. In order to view the same, open the following file in your browser.
> ./library/build/reports/jacoco/testDebugUnitTestCoverage/html/index.html



