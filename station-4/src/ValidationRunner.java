import java.util.ArrayList;
import java.util.List;

/**
 * Führt den Freigabelauf des Release Validators aus.
 *
 * Startet mit einem Delay, damit ein Remote-Debugger an den bereits
 * laufenden Prozess angehängt werden kann, bevor die eigentliche
 * Verarbeitung beginnt.
 *
 * Einstiegspunkt der Station: diese Klasse ausführen bzw. debuggen.
 */
public class ValidationRunner {

    private static final int STARTUP_DELAY_SECONDS =
            Integer.getInteger("validator.startupDelaySeconds", 25);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Release Validator started. Debug port is available on 5005.");
        System.out.println("Processing starts in " + STARTUP_DELAY_SECONDS + " seconds.");
        Thread.sleep(STARTUP_DELAY_SECONDS * 1000L);

        ReleaseValidator validator = new ReleaseValidator();
        ChangeGenerator generator = new ChangeGenerator();

        List<Integer> rejectedChangeNumbers = new ArrayList<>();

        for (int i = 0; i < ChangeGenerator.CHANGE_COUNT; i++) {
            ChangeRequest request = generator.next();

            if (!validator.isValid(request)) {
                rejectedChangeNumbers.add(request.number());
            }
        }

        for (int changeNumber : rejectedChangeNumbers) {
            System.out.println("Change request " + changeNumber + " could not be validated");
        }

        if (rejectedChangeNumbers.isEmpty()) {
            System.out.println("✅ All " + ChangeGenerator.CHANGE_COUNT + " change requests were validated.");
            System.out.println("🏅 Badge unlocked: Remote Ranger");
        } else {
            System.out.println(rejectedChangeNumbers.size() + " of " + ChangeGenerator.CHANGE_COUNT
                    + " change requests could not be validated.");
        }
    }
}
