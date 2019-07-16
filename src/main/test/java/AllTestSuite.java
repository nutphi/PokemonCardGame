/**
 * Created by nutph on 5/23/2017.
 */
import controller.ControllerTestSuite;
import model.ModelTestSuite;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({ModelTestSuite.class,
        ControllerTestSuite.class})
public class AllTestSuite {
}