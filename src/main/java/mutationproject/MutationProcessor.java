package mutationproject;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;

public abstract class MutationProcessor<T extends CtElement> extends AbstractProcessor<T> {
    public abstract void revertChanges();
    public abstract String getMutationDescription();
}
