package stalendar.srsys.tests.logic;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import stalendar.srsys.logic.EventManagerImpl;
import stalendar.srsys.data.EventRepositoryImpl;
import stalendar.srsys.object.Event;

import java.util.Date;

public class EventManagerTest {

    private EventManagerImpl eventManager;
    private EventRepositoryImpl eventRepository;

    @Before
    public void setUp() {
        eventRepository = new EventRepositoryImpl();
        eventManager = new EventManagerImpl(eventRepository);
    }

    @Test
    public void testDeleteEvent() {
        // Create a sample event
        Event event = new Event("1", "Test Event", new Date(), "12:00", "13:00");
        eventRepository.saveEvent(event);

        // Delete the event
        eventManager.deleteEvent(event);

        // Verify the event is deleted
        assertNull(eventRepository.fetchEventById("1"));
    }
}
