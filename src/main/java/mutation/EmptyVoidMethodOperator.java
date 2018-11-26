package mutation;

import mutationproject.MutationInfo;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EmptyVoidMethodOperator extends MutationOperator<CtMethod>
{
    private List<CtStatement> backup;

    public EmptyVoidMethodOperator(BiConsumer<MutationOperator<CtMethod>, MutationInfo> mutationConsumer, boolean revertAfterChange)
    {
        super(mutationConsumer, revertAfterChange);
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
    protected MutationInfo applyMutation(CtMethod ctMethod)
    {
        backup = new ArrayList<>(ctMethod.getBody().getStatements());
        ctMethod.getBody().getStatements().clear();
        return new MutationInfo("Empty method body", ctMethod.getSimpleName());
    }

    @Override
    protected void revertMutation()
    {
        getMethod().getBody().getStatements().addAll(backup);
    }
}
