package mutationoperatortest;

import mutation.MutationOperator;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.support.compiler.VirtualFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TestUtils
{
    private static String originalClassContent;
    private static Map<String, String> originalMethodContent;

    private static SpoonResource loadTestFile() throws IOException
    {
        InputStream in = TestUtils.class.getResourceAsStream("/TestClass.java");
        if(in == null)
        {
            System.err.println("Error: could not find resource /TestClass.java");
            System.exit(1);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder fileContent = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null)
        {
            fileContent.append(line);
            fileContent.append(System.lineSeparator());
        }
        return new VirtualFile(fileContent.toString());
    }

    public static CtType<?> mutateTestClass(MutationOperator<?> mutationOperator) throws IOException
    {
        SpoonResource testFile = loadTestFile();
        Launcher spoon = new Launcher();
        spoon.addInputResource(testFile);

        if(originalClassContent == null)
        {
            // get original content for each method
            spoon.run();
            CtType<?> type = spoon.getFactory().Type().getAll().get(0);
            originalClassContent = type.toString();
            originalMethodContent = new HashMap<>();
            for(CtMethod method : type.getMethods())
                originalMethodContent.put(method.getSimpleName(), method.toString());
            spoon = new Launcher();
            spoon.addInputResource(testFile);
        }

        // create a new model with the mutations
        spoon.addProcessor(mutationOperator);
        spoon.run();
        return spoon.getFactory().Type().getAll().get(0);
    }

    public static String getOriginalClassContent()
    {
        return originalClassContent;
    }

    public static String getOriginalMethodContent(String methodName)
    {
        return originalMethodContent.get(methodName);
    }
}
