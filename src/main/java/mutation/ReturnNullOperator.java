package mutation;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ReturnNullOperator extends MutationOperator<CtMethod>
{
    private List<CtStatement> backup;

    public ReturnNullOperator(BiConsumer<MutationOperator<CtMethod>, CtElement> mutationConsumer)
    {
        super(mutationConsumer);
    }

    @Override
    public void process(CtMethod ctMethod) // needed for Spoon to understand that we only want to process methods
    {
        super.process(ctMethod);
    }

    @Override
    public boolean isToBeProcessed(CtMethod candidate)
    {
        return super.isToBeProcessed(candidate) &&
            (candidate.getType().equals(getFactory().Type().objectType().getTopLevelType()) // Object type
            || candidate.getType().getSuperclass() != null); // extends Object
    }

    @Override
    protected void applyMutation(CtMethod ctMethod)
    {
        backup = new ArrayList<>(ctMethod.getBody().getStatements());
        ctMethod.getBody().getStatements().clear();
        ctMethod.getBody().addStatement(getFactory().createReturn().setReturnedExpression(getFactory().Code().createLiteral(null)));
    }

    @Override
    public void revertMutation()
    {
        List<CtStatement> statements = getMethod().getBody().getStatements();
        statements.clear();
        statements.addAll(backup);
    }

    @Override
    public String getMutationDescription()
    {
        return "Remove body and return null in method " + getMethod().getSimpleName();
    }
}
