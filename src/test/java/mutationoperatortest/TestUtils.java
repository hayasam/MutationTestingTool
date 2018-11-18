package mutationoperatortest;

import mutation.EmptyVoidMethodOperator;
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
    private static Map<String, String> originalMethodContent;

    public static CtType<?> mutateTestClass(MutationOperator<?> mutationOperator) throws IOException
    {
        InputStream in = TestUtils.class.getResourceAsStream("/TestClass.java");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder fileContent = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null)
        {
            fileContent.append(line);
            fileContent.append(System.lineSeparator());
        }
        SpoonResource testFile = new VirtualFile(fileContent.toString());

        // get original content for each method
        Launcher spoon = new Launcher();
        spoon.addInputResource(testFile);
        spoon.run();
        CtType<?> type = spoon.getFactory().Type().getAll().get(0);
        originalMethodContent = new HashMap<>();
        for(CtMethod method : type.getMethods())
            originalMethodContent.put(method.getSimpleName(), method.toString());

        // create a new model with the mutations
        spoon = new Launcher();
        spoon.addInputResource(testFile);
        spoon.addProcessor(mutationOperator);
        spoon.run();
        return spoon.getFactory().Type().getAll().get(0);
    }

    public static String getOriginalContent(String methodName)
    {
        return originalMethodContent.get(methodName);
    }
}
