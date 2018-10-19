import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

public class PrimitiveTypeProcessor extends AbstractProcessor<CtMethod> {


    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return candidate.getType().isPrimitive() && !candidate.getType().equals(getFactory().Type().voidPrimitiveType());

    }

    public void process(CtMethod ctMethod) {
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
        System.out.println("primitive type " + ctMethod.getSimpleName()); // TODO
        MutationProject.testMutation();
    }
}
