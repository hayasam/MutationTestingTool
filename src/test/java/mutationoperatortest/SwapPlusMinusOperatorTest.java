package mutationoperatortest;

import mutation.SwapPlusMinusOperator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SwapPlusMinusOperatorTest
{
    private static CtType<?> type;

    @BeforeAll
    static void setup() throws IOException
    {
        type = TestUtils.mutateTestClass(new SwapPlusMinusOperator(null, false));
    }
    @ParameterizedTest
    @ValueSource(strings = {"incrementCounter", "getCounter", "getCounterObject", "isCounterHigh", "isCounterCloseToZero", "isCounterFarFromZero", "abstractMethod", "main"})
    void testUnchangedMethods(String methodName)
    {
        CtMethod method = type.getMethodsByName(methodName).get(0);
        assertEquals(TestUtils.getOriginalMethodContent(methodName), method.toString());
    }
    @Test
    void testMutationAddToCounter()
    {
        CtMethod method = type.getMethod("addToCounter", type.getFactory().Type().integerPrimitiveType());
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("counter = (counter) - value", method.getBody().getStatements().get(0).toString());
    }
    @Test
    void testMutationSubtractFromCounter()
    {
        CtMethod method = type.getMethod("subtractFromCounter", type.getFactory().Type().integerPrimitiveType());
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("counter = (counter) + value", method.getBody().getStatements().get(0).toString());
    }
    @Test
    void testRevert() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new SwapPlusMinusOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
}
