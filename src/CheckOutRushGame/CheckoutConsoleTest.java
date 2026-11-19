package CheckOutRushGame;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class CheckoutConsoleTest extends student.TestCase
{
    public void testInvalidInputRecoversThroughTheConsole() throws Exception
    {
        String output = runConsole(
            "", "invalid", "2", "1", "0", "switch", "calc", "calc 1+ +2",
            "calc 2%3", "calc apples", "calc 2.50 + 3.25", "scan", "scan",
            "scan", "scan", "", "apples", "-1", "1e20", "NaN");

        assertTrue(output.contains("Please enter a valid integer."));
        assertTrue(output.contains("Active lane: Express"));
        assertTrue(output.contains("Scan every item before submitting a total."));
        assertTrue(output.contains("Switch lanes between checkouts."));
        assertTrue(output.contains("Expression cannot be empty."));
        assertTrue(output.contains("Consecutive operators are not allowed."));
        assertTrue(output.contains("Invalid character '%' found."));
        assertTrue(output.contains("Invalid character 'a' found."));
        assertTrue(output.contains("Calculator: 5.75"));
        assertTrue(output.contains("All items have already been scanned."));
        assertEquals(5, output.split(
            "Enter a valid amount or use calc followed by an expression.", -1)
            .length - 1);
        assertTrue(output.contains("Checkout Rush closed."));
    }

    private String runConsole(String... commands) throws Exception
    {
        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Process process = new ProcessBuilder(java, "-Duser.language=en",
            "-Duser.country=US", "-cp", System.getProperty("java.class.path"),
            "CheckOutRushGame.Main").redirectErrorStream(true).start();
        try
        {
            try (var input = process.getOutputStream())
            {
                input.write((String.join("\n", commands) + "\n")
                    .getBytes(StandardCharsets.UTF_8));
            }
            assertTrue("Console should exit after input closes",
                process.waitFor(5, TimeUnit.SECONDS));
            String output = new String(process.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8);
            assertEquals(output, 0, process.exitValue());
            return output;
        }
        finally
        {
            if (process.isAlive())
            {
                process.destroyForcibly();
            }
        }
    }
}
