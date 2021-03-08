mvn clean install -DskipTests
mvn exec:java -Dexec.classpathScope=test -Dexec.mainClass=io.cucumber.core.cli.Main -Dexec.args="src/test/resources/cucumber --glue cucumber --order random:455"