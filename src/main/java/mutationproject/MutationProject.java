package mutationproject;

import com.fasterxml.jackson.databind.PropertyName;
import mutation.*;
import org.apache.commons.io.FileUtils;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.StandardEnvironment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MutationProject
{
    private static StandardEnvironment env;
    private static String slash, currentFilePath, testProjectPath, mavenCommand;
    private static File testProject;
    private static int mutationCount;
    private static List<MutationInfo> killedMutants, survivingMutants;

    public static void main(String[] args)
    {
        if(args.length == 0)
        {
            System.out.println("Please supply path to the Maven project in the program arguments");
            System.exit(0);
        }

        String projectPath = args[0];
        slash = projectPath.contains("/") ? "/" : "\\";
        while(projectPath.endsWith(slash))
            projectPath = projectPath.substring(0, projectPath.length() - 1);

        File project = new File(projectPath);
        if(!project.exists())
        {
            System.err.println("Error: directory " + projectPath + " does not exist!");
            System.exit(1);
        }
        if(!project.isDirectory())
        {
            System.err.println("Error: " + projectPath + " is not a directory!");
            System.exit(1);
        }

        File projectSrc = new File(projectPath + slash + "src"), projectPom = new File(projectPath + slash + "pom.xml");
        if(!projectSrc.exists())
        {
            System.err.println("Error: " + projectPath + " does not have a src directory!");
            System.exit(1);
        }
        if(!projectPom.exists())
        {
            System.err.println("Error: " + projectPath + " does not have a pom.xml file!");
            System.exit(1);
        }

        if(args.length >= 2)
        {
            mavenCommand = args[1];
        }
        else
        {
            mavenCommand = "mvn";
        }

        // create a copy of the project, and make sure the directory doesn't already exist

        testProjectPath = projectPath + "_MutationTest";
        testProject = new File(testProjectPath);

        while(testProject.exists()) {
            testProjectPath += "_";
            testProject = new File(testProjectPath);
        }

        try {
            if(!testProject.mkdir())
            {
                System.err.println("Error: could not create test project directory!");
                System.exit(1);
            }
            FileUtils.copyDirectory(projectSrc, new File(testProjectPath + slash + "src"));
            FileUtils.copyFile(projectPom, new File(testProjectPath + slash + "pom.xml"));
        } catch (IOException e) {
            System.err.println("Something went wrong while copying the project!");
            e.printStackTrace();
            System.exit(1);
        }

        env = new StandardEnvironment();
        env.setAutoImports(true);
        env.setComplianceLevel(8);
        env.useTabulations(true);

        String testProjectSrc = testProjectPath + slash + "src" + slash + "main" + slash + "java";
        if(!new File(testProjectSrc).exists()) {
            System.err.println("Error: directory " + testProjectSrc + " does not exist!");
            deleteTestProject();
            System.exit(1);
        }

        mutationCount = 0;
        killedMutants = new ArrayList<>();
        survivingMutants = new ArrayList<>();

        /* First list files, then process them. We are not processing files as we encounter them, because it prevents
        from deleting the test project when an error occurs.
         */
        List<Path> filesToProcess = new ArrayList<>();
        try {
            Files.walk(Paths.get(testProjectSrc)).filter(f -> f.toString().endsWith(".java")).forEach(filesToProcess::add);
        } catch (IOException e) {
            System.err.println("Something went wrong while iterating over source files!");
            e.printStackTrace();
            deleteTestProject();
            System.exit(1);
        }

        for(Path f : filesToProcess)
        {
            currentFilePath = f.toAbsolutePath().toString();
            byte[] fileContent = null;

            try
            {
                fileContent = Files.readAllBytes(f);
            } catch (IOException e) {
                System.err.println("Something went wrong while reading file " + f.toAbsolutePath().toString());
                e.printStackTrace();
                deleteTestProject();
                System.exit(1);
            }

            SpoonAPI spoon = new Launcher();
            spoon.addInputResource(currentFilePath);

            // Retrieve enabled processors from property file and add them to spoon
            addMutationOperators(spoon);

            spoon.run();

            try {
                Files.write(f, fileContent);
            } catch (IOException e) {
                System.err.println("Something went wrong while writing to " + currentFilePath);
                e.printStackTrace();
                deleteTestProject();
                System.exit(1);
            }
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
            System.out.println("Surviving mutants:");
            for(MutationInfo mutationInfo : survivingMutants)
            {
                System.out.println();
                System.out.println("File: " + mutationInfo.getFilePath());
                System.out.println("Method: " + mutationInfo.getMethodName());
                System.out.println("Mutation: " + mutationInfo.getDescription());
            }
        }

        deleteTestProject();
    }

    private static void deleteTestProject()
    {
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

    public static <T extends CtElement> void testMutation(MutationOperator<T> operator, MutationInfo mutationInfo)
    {
        DefaultJavaPrettyPrinter printer = new DefaultJavaPrettyPrinter(env);
        printer.calculate(null, operator.getFactory().Type().getAll());

        try {
            Files.write(Paths.get(currentFilePath), printer.getResult().getBytes());
        } catch (IOException e) {
            System.err.println("Something went wrong while writing to " + currentFilePath);
            e.printStackTrace();
            deleteTestProject();
            System.exit(1);
        }

        try
        {
            ProcessBuilder ps = new ProcessBuilder(mavenCommand, "-q", "-f", testProjectPath, "clean", "compile", "test");
            ps.redirectErrorStream(true);
            Process pr = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            List<String> commandOutput = new ArrayList<>();
            boolean gotResults = false, hasFailures = false;
            while ((line = in.readLine()) != null)
            {
                commandOutput.add(line);
                if(!gotResults && line.startsWith("Tests run:"))
                {
                    hasFailures = !line.contains("Failures: 0,");
                    gotResults = true;
                    // don't break the loop here: it's necessary to continue reading so that the buffer doesn't fill up
                }
            }

            if(!gotResults)
            {
                System.err.println("Error: could not get tests results after running the command!");
                System.err.println("Command output:");
                for(String s : commandOutput)
                    System.err.println(s);

                deleteTestProject();
                System.exit(1);
            }

            ++mutationCount;

            int substringIndex = currentFilePath.indexOf(slash + "src" + slash + "main" + slash + "java" + slash);
            if(substringIndex == -1)
                substringIndex = currentFilePath.indexOf("/src/main/java/");

            mutationInfo.setFilePath(currentFilePath.substring(substringIndex + 15));

            if(hasFailures)
            {
                killedMutants.add(mutationInfo);
            }
            else
            {
                survivingMutants.add(mutationInfo);
            }

            pr.waitFor();
            in.close();

        } catch(Exception e) {
            System.err.println("Something went wrong while executing tests");
            e.printStackTrace();
            deleteTestProject();
            System.exit(1);
        }
    }

    private static void addMutationOperators(SpoonAPI spoon) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("./config.properties"));
            Enumeration<?> enumeration = prop.propertyNames();
            while(enumeration.hasMoreElements()) {
                String propertyName = enumeration.nextElement().toString();
                String propertyValue = prop.getProperty(propertyName);
                if(propertyValue.trim().startsWith("1")) {
                    switch(propertyName) {
                        // case "conditional" : spoon.addProcessor(new ConditionalBoundaryOperator(MutationProject::testMutation)); break;
                        case "void" : spoon.addProcessor(new EmptyVoidMethodOperator(MutationProject::testMutation)); break;
                        case "negate" : spoon.addProcessor(new NegateExpressionOperator(MutationProject::testMutation)); break;
                        case "default" : spoon.addProcessor(new ReturnDefaultOperator(MutationProject::testMutation)); break;
                        case "null" : spoon.addProcessor(new ReturnNullOperator(MutationProject::testMutation)); break;
                        case "swapAndOr" : spoon.addProcessor(new SwapAndOrOperator(MutationProject::testMutation)); break;
                        case "swapPlusMinus" : spoon.addProcessor(new SwapPlusMinusOperator(MutationProject::testMutation)); break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: could not retrieve the config.properties file. All mutation operators will be used by default.");
            spoon.addProcessor(new EmptyVoidMethodOperator(MutationProject::testMutation));
            spoon.addProcessor(new ReturnDefaultOperator(MutationProject::testMutation));
            spoon.addProcessor(new ReturnNullOperator(MutationProject::testMutation));
            spoon.addProcessor(new NegateExpressionOperator(MutationProject::testMutation));
            spoon.addProcessor(new SwapPlusMinusOperator(MutationProject::testMutation));
            spoon.addProcessor(new SwapAndOrOperator(MutationProject::testMutation));
        }

    }
}