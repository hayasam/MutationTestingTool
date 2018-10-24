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
import java.util.Collection;
import java.util.List;

public class MutationProject {

    private static SpoonAPI spoon;
    private static String projectPath, testProjectPath;
    private static DefaultJavaPrettyPrinter printer;

    public static void main(String[] args) {
        projectPath = "/home-reseau/hpasic/5A/Test/SampleProject"; // TODO utiliser args
        testProjectPath = projectPath + "_MutationTest";

        StandardEnvironment env = new StandardEnvironment();
        env.setAutoImports(true);
        env.setComplianceLevel(8);
        env.useTabulations(true);
        // env.setSourceOutputDirectory(new File(testProjectPath));
        printer = new DefaultJavaPrettyPrinter(env);

        spoon = new Launcher();
        spoon.addInputResource(projectPath);
        spoon.addProcessor(new VoidMethodProcessor());
        spoon.addProcessor(new PrimitiveTypeProcessor());
        // spoon.addProcessor(new JavaOutputProcessor(printer));

        spoon.run();
    }

    public static void testMutation(String path) {


        System.out.println(path);

        /*
        try {
            FileUtils.copyDirectory(new File(projectPath), new File(testProjectPath));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        System.out.println("Testing mutation");
//        spoon.buildModel();
//        CtModel model = spoon.getModel();

    }

}
/*
import org.junit.Before;
import org.junit.Test;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.StandardEnvironment;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class SampleClassTest {

    SpoonAPI spoon;
    DefaultJavaPrettyPrinter printer;

    @Before
    public void setUp() {
        StandardEnvironment env = new StandardEnvironment();
        env.setAutoImports(true);
        env.setComplianceLevel(8);
        env.useTabulations(true);
        spoon = new Launcher();
        printer = new DefaultJavaPrettyPrinter(env);
    }

    @Test
    public void testIncrement() {
        VoidMethodProcessor proc = new VoidMethodProcessor();
        spoon.addProcessor(proc);
        spoon.addInputResource("src/main/java/SampleClass.java");
        spoon.run();
		printer.calculate(null, new ArrayList<>(spoon.getModel().getAllTypes()));
        System.out.println(printer.getResult());
        SampleClass o = new SampleClass();
        o.increment();

        assertEquals(o.getCounter(), 1);

    }

    @Test
    public void testGetCounter() {
        PrimitiveTypeProcessor proc = new PrimitiveTypeProcessor();
        spoon.addProcessor(proc);
        spoon.addInputResource("src/main/java/SampleClass.java");
        spoon.run();
        printer.calculate(null, new ArrayList<>(spoon.getModel().getAllTypes()));
        System.out.println(printer.getResult());
        SampleClass o = new SampleClass();
        o.increment();

        assertEquals(o.getCounter(), 1);

    }

}

 */