package hw2;

import java.util.*;

public class Main {
    
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numberOfRoutes = 1000;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numberOfRoutes; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);

                int countR = 0;
                for (int j = 0; j < route.length(); j++) {
                    if (route.charAt(j) == 'R') {
                        countR++;
                    }
                }

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                }
            });

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        printResult();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void printResult() {
        if (sizeToFreq.isEmpty())
            return;

        Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        System.out.println("Самое частое количество повторений " + maxEntry.getKey() + " (встретилось "
                + maxEntry.getValue() + " раз");
        System.out.println("Другие размеры:");

        sizeToFreq.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(maxEntry.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз"));
    }
}
