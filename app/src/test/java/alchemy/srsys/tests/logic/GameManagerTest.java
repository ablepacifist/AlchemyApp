package alchemy.srsys.tests.logic;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;

import alchemy.srsys.application.MyApp;
import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;

import alchemy.srsys.logic.PotionManager;


public class GameManagerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        // Redirect System.out to capture output for testing startGame and endGame.
        System.setOut(new PrintStream(outContent));
        // Note: GameManager uses a singleton. If needed, reset its state between tests.
    }

    @Test
    public void testGetInstanceSingleton() {
        GameManagerService gm1 = GameManager.getInstance(false);
        GameManagerService gm2 = GameManager.getInstance(false);
        assertNotNull("GameManager instance should not be null", gm1);
        assertSame("GameManager should be a singleton", gm1, gm2);
    }

    @Test
    public void testGetPlayerManagerAndPotionManager() {
        GameManagerService gm = GameManager.getInstance(false);
        assertNotNull("PlayerManager should not be null", gm.getPlayerManager());
        assertNotNull("PotionManager should not be null", gm.getPotionManager());
        assertTrue("PlayerManager should be an instance of PlayerManager",
                gm.getPlayerManager() instanceof PlayerManager);
        assertTrue("PotionManager should be an instance of PotionManager",
                gm.getPotionManager() instanceof PotionManager);
    }

    @Test
    public void testStartGame() {
        GameManagerService gm = GameManager.getInstance(false);
        gm.startGame();
        String output = outContent.toString();
        assertTrue("startGame should print 'Game started!'", output.contains("Game started!"));
    }

    @Test
    public void testEndGame() {
        GameManagerService gm = GameManager.getInstance(false);
        // Clear previous output.
        outContent.reset();
        gm.endGame();
        String output = outContent.toString();
        assertTrue("endGame should print 'Game ended!'", output.contains("Game ended!"));
    }

    @After
    public void restoreStreams() {
        // Restore System.out back to its original stream.
        System.setOut(originalOut);
    }
}
