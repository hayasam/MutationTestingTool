import org.apache.commons.io.FileUtils;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.StandardEnvironment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MutationProject {

    private static StandardEnvironment env;
    private static String currentFilePath, testProjectPath;
    private static int mutationCount;
    private static List<String> survivingMutants;

    public static void main(String[] args) { // TODO utiliser 2eme arg optionnel comme chemin vers maven
        String projectPath = "../TEST_PROJECT_S9_SampleProject"; // TODO utiliser args
        File project = new File(projectPath);
        if(!project.exists()) {
            System.err.println("Error: directory " + projectPath + " does not exist!");
            System.exit(1);
        }
        if(!project.isDirectory()) {
            System.err.println("Error: " + projectPath + " is not a directory!");
            System.exit(1);
        }

        // create a copy of the project, and make sure the directory doesn't already exist

        testProjectPath = projectPath + "_MutationTest";
        File testProject = new File(testProjectPath);

        while(testProject.exists()) {
            testProjectPath += "_";
            testProject = new File(testProjectPath);
        }

        try {
            FileUtils.copyDirectory(project, testProject);
        } catch (IOException e) {
            System.err.println("Something went wrong while copying the project!");
            e.printStackTrace();
            System.exit(1);
        }

        env = new StandardEnvironment();
        env.setAutoImports(true);
        env.setComplianceLevel(8);
        env.useTabulations(true);

        String testProjectSrc = testProjectPath + "/src/main/java";
        if(!new File(testProjectSrc).exists()) {
            System.err.println("Error: directory " + testProjectSrc + " does not exist!");
            System.exit(1);
        }

        mutationCount = 0;
        survivingMutants = new ArrayList<>();

        try {
            Files.walk(Paths.get(testProjectSrc)).filter(f -> f.toString().endsWith(".java")).forEach(f -> {
                currentFilePath = f.toAbsolutePath().toString();
                byte[] fileContent = null;

                try
                {
                    fileContent = Files.readAllBytes(f);
                } catch (IOException e) {
                    System.err.println("Something went wrong while reading file " + f.toAbsolutePath().toString());
                    e.printStackTrace();
                    System.exit(1);
                }

                SpoonAPI spoon = new Launcher();
                spoon.addInputResource(currentFilePath);
                // spoon.addProcessor(new VoidMethodProcessor());
                // spoon.addProcessor(new PrimitiveTypeProcessor());
                spoon.addProcessor(new PlusMinusProcessor());
                spoon.addProcessor(new AndOrProcessor());
                spoon.run();

                try {
                    Files.write(f, fileContent);
                } catch (IOException e) {
                    System.err.println("Something went wrong while writing to " + currentFilePath);
                    e.printStackTrace();
                    System.exit(1);
                }
            });
        } catch (IOException e) {
            System.err.println("Something went wrong while iterating over source files!");
            e.printStackTrace();
            System.exit(1);
        }

        // print results

        System.out.println();
        System.out.println("--------- MUTATION TESTING RESULTS ---------");
        System.out.println();
        System.out.println("Created mutants: " + mutationCount);
        System.out.println("Surviving mutants: " + survivingMutants.size() + " (" + (Math.floor(survivingMutants.size() * 10000.0 / mutationCount) / 100) + "%)");
        if(!survivingMutants.isEmpty())
        {
            System.out.println();
            System.out.println("Surviving mutants list:");
            for(String s : survivingMutants)
                System.out.println("* " + s);
        }

        // delete the mutated project

        try {
            Files.walk(testProject.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            System.err.println("Something went wrong while deleting the mutated project!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void testMutation(CtElement mutatedElement) {
        while(!(mutatedElement instanceof CtType) || !((CtType)mutatedElement).isTopLevel())
            mutatedElement = mutatedElement.getParent();

        DefaultJavaPrettyPrinter printer = new DefaultJavaPrettyPrinter(env);
        printer.calculate(null, Collections.singletonList((CtType<?>) mutatedElement));

        try {
            Files.write(Paths.get(currentFilePath), printer.getResult().getBytes());
        } catch (IOException e) {
            System.err.println("Something went wrong while writing to " + currentFilePath);
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            ProcessBuilder ps = new ProcessBuilder("mvn","-f", testProjectPath, "clean", "compile", "test");
            ps.redirectErrorStream(true);
            Process pr = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            boolean gotResults = false, hasFailures = false;
            while ((line = in.readLine()) != null)
            {
                if(line.startsWith("Tests run:"))
                {
                    hasFailures = !line.contains("Failures: 0,");
                    gotResults = true;
                    break;
                }
            }

            if(!gotResults)
            {
                System.err.println("Error: could not get tests results after running the command!");
                System.exit(1);
            }

            ++mutationCount;
            if(!hasFailures)
            {
                survivingMutants.add(mutatedElement.toString()); // TODO description d'une mutation
            }

            pr.waitFor();
            in.close();

        } catch(Exception e) {
            System.err.println("Something went wrong while executing tests");
            e.printStackTrace();
            System.exit(1);
        }
    }
}