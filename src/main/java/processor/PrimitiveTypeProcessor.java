import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

public class PrimitiveTypeProcessor extends AbstractProcessor<CtMethod> {


    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return candidate.getType().isPrimitive() && !candidate.getType().equals(getFactory().Type().voidPrimitiveType())
                && candidate.getAnnotation(org.junit.Test.class) == null; // not a Test


    }

    /*
    @Test
	public void testCompileStatementWithReturn() {
		// contract: a snippet with return can be compiled.
		CtElement el = SnippetCompilationHelper.compileStatement(
				factory.Code().createCodeSnippetStatement("return 3"),
				factory.Type().INTEGER
		);
		assertTrue(CtReturn.class.isAssignableFrom(el.getClass()));
		assertEquals("return 3", el.toString());
	}

     */

    public void process(CtMethod ctMethod) {
        List<CtStatement> backup = new ArrayList<>(ctMethod.getBody().getStatements());
        ctMethod.getBody().getStatements().clear();
        CtTypeReference type = ctMethod.getType();

        switch(type.getSimpleName()) { // TODO refactoring
            case "int":
                ctMethod.getBody().addStatement(getFactory().<Integer>createReturn().setReturnedExpression(getFactory().Code().createLiteral(0)));
                break;
            case "char":
                ctMethod.getBody().addStatement(getFactory().<Character>createReturn().setReturnedExpression(getFactory().Code().createLiteral((char)0)));
                break;
            case "short":
                ctMethod.getBody().addStatement(getFactory().<Short>createReturn().setReturnedExpression(getFactory().Code().createLiteral((short)0)));
                break;
            case "long":
                ctMethod.getBody().addStatement(getFactory().<Long>createReturn().setReturnedExpression(getFactory().Code().createLiteral(0L)));
                break;
            case "byte":
                ctMethod.getBody().addStatement(getFactory().<Byte>createReturn().setReturnedExpression(getFactory().Code().createLiteral((byte)0)));
                break;
            case "float":
                ctMethod.getBody().addStatement(getFactory().<Float>createReturn().setReturnedExpression(getFactory().Code().createLiteral(0F)));
                break;
            case "double":
                ctMethod.getBody().addStatement(getFactory().<Double>createReturn().setReturnedExpression(getFactory().Code().createLiteral(0D)));
                break;
            case "boolean":
                ctMethod.getBody().addStatement(getFactory().<Boolean>createReturn().setReturnedExpression(getFactory().Code().createLiteral(false)));
                break;
        }
        System.out.println("primitive type " + ctMethod.getSimpleName()); // TODO remove
        MutationProject.testMutation(ctMethod);

        ctMethod.getBody().getStatements().clear();
        ctMethod.getBody().getStatements().addAll(backup);
    }
}
