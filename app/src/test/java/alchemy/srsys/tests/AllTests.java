package alchemy.srsys.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import alchemy.srsys.tests.data.HSQLDatabaseTest; // Update to match actual package structure
import alchemy.srsys.tests.data.StubDatabaseTest; // Update to match actual package structure
import alchemy.srsys.tests.logic.GameManagerTest; // Update to match actual package structure
import alchemy.srsys.tests.logic.PlayerManagerTest; // Update to match actual package structure
import alchemy.srsys.tests.logic.InventoryTest; // Update to match actual package structure
import alchemy.srsys.tests.logic.PotionManagerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HSQLDatabaseTest.class,
        StubDatabaseTest.class,
        GameManagerTest.class,
        PlayerManagerTest.class,
        InventoryTest.class,
        PotionManagerTest.class
})
public class AllTests {
    // This class remains empty and serves only as a holder for the above annotations.
}
