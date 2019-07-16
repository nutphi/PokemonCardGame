package model;

/**
 * Created by nutph on 5/24/2017.
 */
import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({AbilityTest.class, CardList.class, CardTest.class,
        PlayerTest.class, PokemonAbilityTest.class,
        PokemonCardTest.class})
public class ModelTestSuite {
}
