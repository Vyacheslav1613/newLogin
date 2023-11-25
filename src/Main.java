import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger length3 = new AtomicInteger(0);
    private static final AtomicInteger length4 = new AtomicInteger(0);
    private static final AtomicInteger length5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        Thread palindrom = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (isPalindrome(texts[i])) {
                    lengthAdd(texts[i]);
                }
            }
        });

        Thread oneLetter = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (isOneLetter(texts[i])) {
                    lengthAdd(texts[i]);
                }
            }
        });

        Thread increase = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (isIncreaseLetter(texts[i])) {
                    lengthAdd(texts[i]);
                }
            }
        });
        oneLetter.start();
        palindrom.start();
        increase.start();

        oneLetter.join();
        palindrom.join();
        increase.join();

        printResult();
    }

    private static void printResult() {
        System.out.println("Красивых слов с длиной 3: " + length3.get() + " шт\n" +
                "Красивых слов с длиной 4: " + length4.get() + " шт \n" +
                "Красивых слов с длиной 5: " + length5.get() + " шт\n");
    }

    private static boolean isIncreaseLetter(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isOneLetter(String text) {
        char firstLetter = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != firstLetter) {
                return false;
            }
        }
        return true;
    }

    private static void lengthAdd(String text) {
        if (text.length() == 3) {
            length3.incrementAndGet();
        } else if (text.length() == 4) {
            length4.incrementAndGet();
        } else if (text.length() == 5) {
            length5.incrementAndGet();
        }
    }

    public static boolean isPalindrome(String text) {
        StringBuilder reversedWord = new StringBuilder(text).reverse();
        String result = reversedWord.toString();
        return result.equalsIgnoreCase(text);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}