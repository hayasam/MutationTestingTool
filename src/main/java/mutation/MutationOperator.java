package mutation;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

import java.util.function.BiConsumer;

public abstract class MutationOperator<T extends CtElement> extends AbstractProcessor<T>
{
    private BiConsumer<MutationOperator<T>, CtElement> mutationConsumer;
    private CtMethod method;

    public MutationOperator(BiConsumer<MutationOperator<T>, CtElement> mutationConsumer)
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
            // TODO use getFactory().Method().getMainMethods() to check for main method?
            method = (CtMethod)element;
            return !(method.getSimpleName().equals("main") && method.isStatic()) // not static main method
                && !method.isAbstract(); // method with a body

            /* TODO find a better way to check for tests (not depending on junit version)
            method.getAnnotation(org.junit.Test.class) == null; // not a Test
             */
        }
        return false;
    }

    @Override
    public void process(T element)
    {
        applyMutation(element);
        if(mutationConsumer != null)
        {
            mutationConsumer.accept(this, element);
            revertMutation();
        }
    }

    protected abstract void applyMutation(T element);
    protected abstract void revertMutation(); // TODO tester
    public abstract String getMutationDescription(); // TODO refactor
}
