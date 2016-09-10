package com.github.javadev.undescriptive.client;

import java.util.Map;
import com.github.underscore.lodash.$;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpClientTest {
    private static GameClient client = HttpClient.createDefault();

    @Test
    public void weatherParser() {
        Map<String, Object> result = (Map<String, Object>) $.fromXml(
        "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
        + "<report>"
        + "    <time>Sat Sep 10 2016 13:50:19 GMT+0000 (UTC)</time>"
        + "    <coords>"
        + "        <x>3916.234</x>"
        + "        <y>169.914</y>"
        + "        <z>6.33</z>"
        + "    </coords><code>NMR</code>"
        + "    <message>Another day of everyday normal regular weather, business as usual, "
        + "unless it’s going to be like the time of the Great Paprika Mayonnaise Incident of 2014, that was some pretty nasty stuff.</message>"
        + "    <varX-Rating>8</varX-Rating>"
        + "</report>");
        assertEquals("NMR", $.get(result, "code"));
    }

    @Test
    public void testSolution() throws Exception {
        GameCounters gameCounters = client.getAndSolveGames(20000);
        System.out.println("victoryCount - " + gameCounters.getVictoryCount().get());
        System.out.println("errorCount - " + gameCounters.getErrorCount().get());
        assertEquals("victoryCount + stormCount + errorCount should be 20000", 20000,
            gameCounters.getVictoryCount().get() + gameCounters.getStormCount().get()
            + gameCounters.getErrorCount().get());
    }

    @Test
    public void main() {
        HttpClient.main("test");
    }
}
