package mutationoperatortest;

import mutation.EmptyVoidMethodOperator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmptyVoidMethodOperatorTest
{
    private static CtType<?> type;

    @BeforeAll
    static void setup() throws IOException
    {
        type = TestUtils.mutateTestClass(new EmptyVoidMethodOperator(null, false));
    }
    @ParameterizedTest
    @ValueSource(strings = {"getCounter", "getCounterObject", "isCounterHigh", "isCounterCloseToZero", "isCounterFarFromZero", "isCounterVeryFarFromZero", "abstractMethod", "main"})
    void testUnchangedMethods(String methodName)
    {
        CtMethod method = type.getMethodsByName(methodName).get(0);
        assertEquals(TestUtils.getOriginalMethodContent(methodName), method.toString());
    }
    @Test
    void testMutationIncrementCounter()
    {
        assertEquals(0, type.getMethod("incrementCounter").getBody().getStatements().size());
    }
    @Test
    void testMutationAddToCounter()
    {
        assertEquals(0, type.getMethod("addToCounter", type.getFactory().Type().integerPrimitiveType()).getBody().getStatements().size());
    }
    @Test
    void testMutationSubtractFromCounter()
    {
        assertEquals(0, type.getMethod("subtractFromCounter", type.getFactory().Type().integerPrimitiveType()).getBody().getStatements().size());
    }
    @Test
    void testRevert() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new EmptyVoidMethodOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
}
