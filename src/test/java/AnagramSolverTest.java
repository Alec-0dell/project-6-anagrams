import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnagramSolverTest {
    private AnagramSolver anagram;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testPrint() {
        AnagramSolver as = getSolver("dict0.txt");
        assertThrows(IllegalArgumentException.class, () -> as.print("george bush", -1));
    }

    @ParameterizedTest
    @MethodSource
    void testDict0(String text, int count, List<String> expected) {
        testPrint("dict0.txt", text, count, expected);
    }

    private Stream<Arguments> testDict0() {
        return getArguments("dict0_expected.txt").stream();
    }

    @ParameterizedTest
    @MethodSource
    void testEleven(String text, int count, List<String> expected) {
        testPrint("eleven.txt", text, count, expected);
    }

    private Stream<Arguments> testEleven() {
        return getArguments("eleven_expected.txt").stream();
    }


    @ParameterizedTest
    @MethodSource
    void testDict1(String text, int count, List<String> expected) {
        testPrint("dict1.txt", text, count, expected);
    }

    private Stream<Arguments> testDict1() {
        return getArguments("dict1_expected.txt").stream();
    }

    @ParameterizedTest
    @MethodSource
    void testDict3(String text, int count, List<String> expected) {
        testPrint("dict3.txt", text, count, expected);
    }

    private Stream<Arguments> testDict3() {
        return getArguments("dict3_expected.txt").stream();
    }

    @ParameterizedTest
    @MethodSource
    void testDict4(String text, int count, List<String> expected) {
        testPrint("dict4.txt", text, count, expected);
    }

    private Stream<Arguments> testDict4() {
        return getArguments("dict4_expected.txt").stream();
    }

    void testPrint(String dictionary, String text, int count, List<String> expected) {
        AnagramSolver as = getSolver(dictionary);
        as.print(text,count);
        if (expected.isEmpty()) {
            assertEquals("", outputStreamCaptor.toString());
        } else {
            List<String> actual = List.of(outputStreamCaptor.toString().split("\\n"));
            assertEquals(expected, actual);
            outputStreamCaptor.reset();
        }
    }


    @AfterAll
    public void tearDown() {
        System.setOut(System.out);
    }

    private List<Arguments> getArguments(String filename) {
        List<Arguments> arguments = new LinkedList<>();
        File expectedFile = new File(
                AnagramSolverTest.class.getClassLoader().getResource(filename).getFile()
        );
        try {
            Scanner scanner = new Scanner(expectedFile);
            do {
                String[] line = scanner.nextLine().split(",");
                String word = line[0];
                int count = Integer.parseInt(line[1]);
                List<String> outputLines = new LinkedList<>();
                while (scanner.hasNextLine()) {
                    String output = scanner.nextLine();
                    if (output.isBlank()) break;
                    outputLines.add(output);
                }
                arguments.add(Arguments.of(word,count,outputLines));
            } while (scanner.hasNextLine());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return arguments;
    }

    private AnagramSolver getSolver(String filename) {
        File dictionaryFile = new File(
                AnagramMain.class.getClassLoader().getResource(filename).getFile()
        );
        Scanner input = null;
        try {
            input = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // read dictionary into an ArrayList
        List<String> dictionary = new ArrayList<>();
        while (input.hasNextLine()) {
            dictionary.add(input.nextLine());
        }

        // solve anagrams
        List<String> dictionary2 = Collections.unmodifiableList(dictionary);
        return new AnagramSolver(dictionary2);
    }

}