package mutationoperatortest;

import mutation.ReturnNullOperator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReturnNullOperatorTest
{
    private static CtType<?> type;

    @BeforeAll
    static void setup() throws IOException
    {
        type = TestUtils.mutateTestClass(new ReturnNullOperator(null, false));
    }
    @ParameterizedTest
    @ValueSource(strings = {"incrementCounter", "addToCounter", "subtractFromCounter", "getCounter", "isCounterHigh", "isCounterCloseToZero", "isCounterFarFromZero", "abstractMethod", "main"})
    void testUnchangedMethods(String methodName)
    {
        CtMethod method = type.getMethodsByName(methodName).get(0);
        assertEquals(TestUtils.getOriginalMethodContent(methodName), method.toString());
    }
    @Test
    void testMutationGetCounter()
    {
        CtMethod method = type.getMethod("getCounterObject");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return null", method.getBody().getStatements().get(0).toString());
    }
    @Test
    void testRevert() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new ReturnNullOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
}
