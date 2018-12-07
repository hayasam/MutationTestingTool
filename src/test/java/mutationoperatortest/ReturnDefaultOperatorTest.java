package mutationoperatortest;

import mutation.ReturnDefaultOperator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReturnDefaultOperatorTest
{
    private static CtType<?> type;

    @BeforeAll
    static void setup() throws IOException
    {
        type = TestUtils.mutateTestClass(new ReturnDefaultOperator(null, false));
    }
    @ParameterizedTest
    @ValueSource(strings = {"incrementCounter", "addToCounter", "subtractFromCounter", "getCounterObject", "abstractMethod", "main"})
    void testUnchangedMethods(String methodName)
    {
        CtMethod method = type.getMethodsByName(methodName).get(0);
        assertEquals(TestUtils.getOriginalMethodContent(methodName), method.toString());
    }
    @Test
    void testMutationGetCounter()
    {
        CtMethod method = type.getMethod("getCounter");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return 0", method.getBody().getStatements().get(0).toString());
    }
    @Test
    void testMutationIsCounterHigh()
    {
        CtMethod method = type.getMethod("isCounterHigh");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return false", method.getBody().getStatements().get(0).toString());
    }
    @Test
    void testMutationIsCounterCloseToZero()
    {
        CtMethod method = type.getMethod("isCounterCloseToZero");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return false", method.getBody().getStatements().get(0).toString());
    }
    @Test
    void testMutationIsCounterFarFromZero()
    {
        CtMethod method = type.getMethod("isCounterFarFromZero");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return false", method.getBody().getStatements().get(0).toString());
    }
    @Test
    void testMutationIsCounterVeryFarFromZero()
    {
        CtMethod method = type.getMethod("isCounterVeryFarFromZero");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return false", method.getBody().getStatements().get(0).toString());
    }
    @Test
    void testRevert() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new ReturnDefaultOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
}
