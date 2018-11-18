package mutation;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EmptyVoidMethodOperator extends MutationOperator<CtMethod>
{
    private List<CtStatement> backup;

    public EmptyVoidMethodOperator(BiConsumer<MutationOperator<CtMethod>, CtElement> mutationConsumer)
    {
        super(mutationConsumer);
    }

    @Override
    public void process(CtMethod element) // needed for Spoon to understand that we only want to process methods
    {
        super.process(element);
    }

    @Override
    public boolean isToBeProcessed(CtMethod candidate)
    {
        return super.isToBeProcessed(candidate) && candidate.getType().equals(candidate.getFactory().Type().voidPrimitiveType());
    }

    @Override
    protected void applyMutation(CtMethod ctMethod)
    {
        backup = new ArrayList<>(ctMethod.getBody().getStatements());
        ctMethod.getBody().getStatements().clear();
    }

    @Override
    protected void revertMutation()
    {
        getMethod().getBody().getStatements().addAll(backup);
    }

    @Override
    public String getMutationDescription()
    {
        return "Remove body in void method " + getMethod().getSimpleName();
    }
}
