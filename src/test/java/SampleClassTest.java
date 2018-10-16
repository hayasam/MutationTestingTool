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
