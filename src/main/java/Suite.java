import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;

@org.junit.platform.suite.api.Suite
@SuiteDisplayName("Egzaminas")
@SelectClasses({FirstTask.class,SecondTask.class})
public class Suite {
}
