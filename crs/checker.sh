javac ./src/main/*.java
export CLASSPATH="../../junit-4.12.jar:../../hamcrest-core-1.3.jar:./src:."
javac ./test/*.java
java org.junit.runner.JUnitCore test.TestCases
rm ./src/main/*.class
rm ./test/*.class
