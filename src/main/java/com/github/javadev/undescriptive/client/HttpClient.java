package com.github.javadev.undescriptive.client;

import com.github.underscore.lodash.$;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClient implements GameClient {
    private static final String BASE_URL = "http://www.dragonsofmugloar.com";
    private static final String WEATHER_URL = "/weather/api/report/";

    private static final int[][] SOLUTIONS = {
        {8, 8, 4, 0}, {10, 6, 3, 1},
        {8, 7, 5, 0}, {10, 5, 4, 1},
        {8, 6, 6, 0}, {10, 4, 4, 2},
        {7, 7, 6, 0}, {10, 4, 4, 2},
        {8, 8, 3, 1}, {10, 6, 2, 2},
        {8, 7, 4, 1}, {10, 5, 4, 1},
        {8, 6, 5, 1}, {10, 4, 5, 1},
        {7, 7, 5, 1}, {9, 5, 4, 2},
        {7, 6, 6, 1}, {9, 4, 4, 3},
        {8, 8, 2, 2}, {10, 6, 3, 1},
        {8, 7, 3, 2}, {10, 5, 4, 1},
        {8, 6, 4, 2}, {10, 4, 3, 3},
        {7, 7, 4, 2}, {10, 5, 4, 1},
        {8, 5, 5, 2}, {10, 5, 4, 1},
        {7, 6, 5, 2}, {10, 4, 4, 2},
        {6, 6, 6, 2}, {10, 4, 3, 3},
        {8, 6, 3, 3}, {10, 4, 4, 2},
        {7, 7, 3, 3}, {10, 4, 4, 2},
        {8, 5, 4, 3}, {10, 4, 4, 2},
        {7, 6, 4, 3}, {9, 4, 5, 2},
        {7, 5, 5, 3}, {9, 5, 4, 2},
        {6, 6, 5, 3}, {8, 5, 4, 3},
        {8, 4, 4, 4}, {10, 4, 3, 3},
        {7, 5, 4, 4}, {10, 4, 3, 3},
        {6, 6, 4, 4}, {10, 4, 3, 3},
        {6, 5, 5, 4}, {10, 4, 3, 3},
        {5, 5, 5, 5}, {10, 4, 3, 3}
        };

    private final String baseUrl;

    private HttpClient(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static HttpClient createDefault() {
        return new HttpClient(BASE_URL);
    }

    private $.FetchResponse get(final String resourceUrl) {
        return $.fetch(this.baseUrl + resourceUrl);
    }

    private Map<String, Object> put(final String resourceUrl, final Map<String, Object> params) {
        Map<String, Object> result = (Map<String, Object>) $.fetch(this.baseUrl + resourceUrl, "PUT", $.toJson(params)).json();
        return result;
    }

    @Override
    public Map<String, Object> getGame() {
        return (Map<String, Object>) get("/api/game").json();
    }

    @Override
    public Map<String, Object> getWeather(Long id) {
        return (Map<String, Object>) $.fromXml(get(WEATHER_URL + id).text());
    }

    @Override
    public Map<String, Object> sendSolution(Long id, Map<String, Object> solutionRequest) {
        return put("/api/game/" + id + "/solution", solutionRequest);
    }

    @Override
    public Map<String, Object> generateGameSolution(Map<String, Object> gameResponseItem, Map<String, Object> weatherResponse) {
        if ("T E".equals((String) $.get(weatherResponse, "code"))) {
            return new HashMap<String, Object>() { {
                put("dragon", new HashMap<String, Object>() { {
                    put("scaleThickness", 5);
                    put("clawSharpness", 5);
                    put("wingStrength", 5);
                    put("fireBreath", 5);
               } });
            } };
        } else if ("HVA".equals((String) $.get(weatherResponse, "code"))) {
            return new HashMap<String, Object>() { {
                put("dragon", new HashMap<String, Object>() { {
                    put("scaleThickness", 10);
                    put("clawSharpness", 10);
                    put("wingStrength", 0);
                    put("fireBreath", 0);
               } });
            } };
        }

        final List<Long> knightAttrs = Arrays.asList((Long) $.get(gameResponseItem, "attack"),
            (Long) $.get(gameResponseItem, "armor"), (Long) $.get(gameResponseItem, "agility"),
            (Long) $.get(gameResponseItem, "endurance"));
        final Integer[] indexes = { 0, 1, 2, 3 };

        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override public int compare(final Integer o1, final Integer o2) {
                return knightAttrs.get(o1).compareTo(knightAttrs.get(o2));
            }
        });
        int maxIndex = indexes[3];
        int secondMaxIndex = indexes[2];
        int thirdMaxIndex = indexes[1];
        int forthMaxIndex = indexes[0];
        final int[] dragonAttrs = new int[] {0, 0, 0, 0};
        for (int index = 0; index < SOLUTIONS.length; index += 2) {
            if (knightAttrs.get(maxIndex) == SOLUTIONS[index][0]
                && knightAttrs.get(secondMaxIndex) == SOLUTIONS[index][1]
                && knightAttrs.get(thirdMaxIndex) == SOLUTIONS[index][2]
                && knightAttrs.get(forthMaxIndex) == SOLUTIONS[index][3]) {
                dragonAttrs[maxIndex] = SOLUTIONS[index + 1][0];
                dragonAttrs[secondMaxIndex] = SOLUTIONS[index + 1][1];
                dragonAttrs[thirdMaxIndex] = SOLUTIONS[index + 1][2];
                dragonAttrs[forthMaxIndex] = SOLUTIONS[index + 1][3];
                break;
            }
        }
        return new HashMap<String, Object>() { {
            put("dragon", new HashMap<String, Object>() { {
                put("scaleThickness", dragonAttrs[0]);
                put("clawSharpness", dragonAttrs[1]);
                put("wingStrength", dragonAttrs[2]);
                put("fireBreath", dragonAttrs[3]);
            } });
        } };
    }

    private static class CallableImpl implements Callable<Void> {

        private final HttpClient httpClient;
        private final GameCounters gameCounters;

        public CallableImpl(HttpClient httpClient, GameCounters gameCounters) {
            this.httpClient = httpClient;
            this.gameCounters = gameCounters;
        }

        public Void call() {
            try {
                final Map<String, Object> game = httpClient.getGame();
                final Map<String, Object> weatherResponse = httpClient.getWeather((Long) $.get(game, "gameId"));
                if ("SRO".equals((String) $.get(weatherResponse, "code"))) {
                    gameCounters.getStormCount().getAndIncrement();
                } else {
                    final Map<String, Object> request = httpClient.generateGameSolution((Map<String, Object>) $.get(game, "knight"), weatherResponse);
                    final Map<String, Object> response = httpClient.sendSolution((Long) $.get(game, "gameId"), request);
                    if ("Victory".equals((String) $.get(response, "status"))) {
                        gameCounters.getVictoryCount().getAndIncrement();
                    }
                }
            } catch (Exception ex) {
                gameCounters.getErrorCount().getAndIncrement();
            }
            return null;
        }
    }

    @Override
    public GameCounters getAndSolveGames(int amountOfGames) {
        final GameCounters gameCounters = new GameCounters();
        final ExecutorService executor = Executors.newFixedThreadPool(100);
        final List<Callable<Void>> callables = new ArrayList<Callable<Void>>();
        for (int gameIndex = 0; gameIndex < amountOfGames; gameIndex += 1) {
            callables.add(new CallableImpl(this, gameCounters));
        }
        try {
            executor.invokeAll(callables);
        } catch (InterruptedException ex) {
        }
        executor.shutdown();
        return gameCounters;
    }

    public static void main(String ... args) {
        final String message = "Dragons of Mugloar solution.\n\n"
            + "For docs, license, tests, and downloads, see: https://github.com/javadev/undescriptive-project";
        System.out.println(message);
    }
}
