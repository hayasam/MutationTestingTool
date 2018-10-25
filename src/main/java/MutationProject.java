import org.apache.commons.io.FileUtils;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.JavaOutputProcessor;
import spoon.support.StandardEnvironment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.*;

public class MutationProject {

    private static StandardEnvironment env;
    private static String currentFilePath;

    public static void main(String[] args) {
        String projectPath = "/home-reseau/ptestart/Bureau/SampleProject"; // TODO utiliser args
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

        // create a copy of the project, and make sure the directory doesn't already exist

        String testProjectPath = projectPath + "_MutationTest";
        File testProject = new File(testProjectPath);

        while(testProject.exists())
        {
            testProjectPath += "_";
            testProject = new File(testProjectPath);
        }

        try
        {
            FileUtils.copyDirectory(project, testProject);
        }
        catch (IOException e)
        {
            System.err.println("Something went wrong while copying the project!");
            e.printStackTrace();
            System.exit(1);
        }

        env = new StandardEnvironment();
        env.setAutoImports(true);
        env.setComplianceLevel(8);
        env.useTabulations(true);

        String testProjectSrc = testProjectPath + "/src/main/java";
        if(!new File(testProjectSrc).exists())
        {
            System.err.println("Error: directory " + testProjectSrc + " does not exist!");
            System.exit(1);
        }

        try
        {
            Files.walk(Paths.get(testProjectSrc)).filter(f -> f.toString().endsWith(".java")).forEach(f -> {
                currentFilePath = f.toAbsolutePath().toString();
                byte[] fileContent = null;

                try
                {
                    fileContent = Files.readAllBytes(f);
                }
                catch (IOException e)
                {
                    System.err.println("Something went wrong while reading file " + f.toAbsolutePath().toString());
                    e.printStackTrace();
                    System.exit(1);
                }

                SpoonAPI spoon = new Launcher();
                spoon.addInputResource(currentFilePath);
                spoon.addProcessor(new VoidMethodProcessor());
                spoon.addProcessor(new PrimitiveTypeProcessor());
                spoon.run();

                /* TODO
                try
                {
                    Files.write(f, fileContent);
                }
                catch (IOException e)
                {
                    System.err.println("Something went wrong while writing to " + currentFilePath);
                    e.printStackTrace();
                    System.exit(1);
                }*/
            });
        }
        catch (IOException e)
        {
            System.err.println("Something went wrong while iterating over source files!");
            e.printStackTrace();
            System.exit(1);
        }

        // TODO supprimer dossier de projet test
    }

    public static void testMutation(CtElement mutatedElement)
    {
        while(!(mutatedElement instanceof CtType) || !((CtType)mutatedElement).isTopLevel())
            mutatedElement = mutatedElement.getParent();

        DefaultJavaPrettyPrinter printer = new DefaultJavaPrettyPrinter(env);
        printer.calculate(null, Collections.singletonList((CtType<?>) mutatedElement));

        try
        {
            Files.write(Paths.get(currentFilePath), printer.getResult().getBytes());
        }
        catch (IOException e)
        {
            System.err.println("Something went wrong while writing to " + currentFilePath);
            e.printStackTrace();
            System.exit(1);
        }
    }
}