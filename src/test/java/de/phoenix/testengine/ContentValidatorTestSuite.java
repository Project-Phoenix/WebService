package de.phoenix.testengine;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({IOContentCheckTest.class, NetworkingContentCheckTest.class, ValidatorEngineTest.class})
public class ContentValidatorTestSuite {

}
