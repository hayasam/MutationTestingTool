package mutation;

import mutationproject.MutationInfo;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

import java.util.function.BiConsumer;

public abstract class MutationOperator<T extends CtElement> extends AbstractProcessor<T>
{
    private BiConsumer<MutationOperator<T>, MutationInfo> mutationConsumer;
    private CtMethod method;

    public MutationOperator(BiConsumer<MutationOperator<T>, MutationInfo> mutationConsumer)
    {
        this.mutationConsumer = mutationConsumer;
    }

    protected CtMethod getMethod()
    {
        return method;
    }

    @Override
    public boolean isToBeProcessed(T candidate)
    {
        CtElement element = candidate;
        while(element != null && !(element instanceof CtMethod))
            element = element.getParent();

        if(element != null)
        {
            method = (CtMethod)element;
            return !getFactory().Method().getMainMethods().contains(method) // not main method
                && !method.isAbstract(); // method with a body
        }
        return false;
    }

    @Override
    public void process(T element)
    {
        MutationInfo mutationInfo = applyMutation(element);
        if(mutationConsumer != null)
        {
            mutationConsumer.accept(this, mutationInfo);
            revertMutation();
        }
    }

    protected abstract MutationInfo applyMutation(T element);
    protected abstract void revertMutation(); // TODO tester
}
