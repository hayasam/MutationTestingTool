package processor;

import mutationproject.IMutationProcessor;
import mutationproject.MutationProject;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

public class PrimitiveTypeProcessor extends AbstractProcessor<CtMethod> implements IMutationProcessor {

    private CtMethod method;
    private List<CtStatement> backup;

    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return candidate.getType().isPrimitive() && !candidate.getType().equals(getFactory().Type().voidPrimitiveType()) // primitive type
                && candidate.getAnnotation(org.junit.Test.class) == null
                && !candidate.isAbstract(); // not an interface method declaration


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
        method = ctMethod;
        backup = new ArrayList<>(method.getBody().getStatements());
        method.getBody().getStatements().clear();
        CtTypeReference type = method.getType();

        switch(type.getSimpleName()) { // TODO refactoring
            case "int":
                method.getBody().addStatement(getFactory().<Integer>createReturn().setReturnedExpression(getFactory().Code().createLiteral(0)));
                break;
            case "char":
                method.getBody().addStatement(getFactory().<Character>createReturn().setReturnedExpression(getFactory().Code().createLiteral((char)0)));
                break;
            case "short":
                method.getBody().addStatement(getFactory().<Short>createReturn().setReturnedExpression(getFactory().Code().createLiteral((short)0)));
                break;
            case "long":
                method.getBody().addStatement(getFactory().<Long>createReturn().setReturnedExpression(getFactory().Code().createLiteral(0L)));
                break;
            case "byte":
                method.getBody().addStatement(getFactory().<Byte>createReturn().setReturnedExpression(getFactory().Code().createLiteral((byte)0)));
                break;
            case "float":
                method.getBody().addStatement(getFactory().<Float>createReturn().setReturnedExpression(getFactory().Code().createLiteral(0F)));
                break;
            case "double":
                method.getBody().addStatement(getFactory().<Double>createReturn().setReturnedExpression(getFactory().Code().createLiteral(0D)));
                break;
            case "boolean":
                method.getBody().addStatement(getFactory().<Boolean>createReturn().setReturnedExpression(getFactory().Code().createLiteral(false)));
                break;
        }
        System.out.println("primitive type " + method.getSimpleName()); // TODO remove
        MutationProject.testMutation(this, method);
    }

    @Override
    public void revertChanges() {

        method.getBody().getStatements().clear();
        method.getBody().getStatements().addAll(backup);
    }

    @Override
    public String getMutationDescription() {
        return "Remove body and return default value in method " + method.getSimpleName();
    }
}
