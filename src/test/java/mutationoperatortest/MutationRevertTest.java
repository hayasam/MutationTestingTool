package mutationoperatortest;

import mutation.*;
import org.junit.jupiter.api.Test;
import spoon.reflect.declaration.CtType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MutationRevertTest
{
    @Test
    void testRevertEmptyVoidMethod() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new EmptyVoidMethodOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
    @Test
    void testRevertNegateExpressionMethod() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new NegateExpressionOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
    @Test
    void testRevertReturnDefault() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new ReturnDefaultOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
    @Test
    void testRevertReturnNull() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new ReturnNullOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
    @Test
    void testRevertSwapAndOr() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new SwapAndOrOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
    @Test
    void testRevertPlusMinus() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new SwapPlusMinusOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
}
