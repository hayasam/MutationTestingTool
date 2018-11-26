package mutation;

import mutationproject.MutationInfo;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.function.BiConsumer;

public class ConditionalBoundaryOperator extends MutationOperator<CtBinaryOperator>
{
    private CtBinaryOperator binaryOperator;

    public ConditionalBoundaryOperator(BiConsumer<MutationOperator<CtBinaryOperator>, MutationInfo> mutationConsumer)
    {
        super(mutationConsumer);
    }

    @Override
    public void process(CtBinaryOperator ctBinaryOperator) // needed for Spoon to understand that we only want to process binary operators
    {
        super.process(ctBinaryOperator);
    }

    @Override
    public boolean isToBeProcessed(CtBinaryOperator candidate)
    {
        return super.isToBeProcessed(candidate) &&
                (candidate.getKind() == BinaryOperatorKind.PLUS || candidate.getKind() == BinaryOperatorKind.MINUS);
    }

    private static void swapConditionalsBoundary(CtBinaryOperator bo)
    {
        if(bo.getKind() == BinaryOperatorKind.LT) bo.setKind(BinaryOperatorKind.LE);
        else if(bo.getKind() == BinaryOperatorKind.LE) bo.setKind(BinaryOperatorKind.LT);
        else if(bo.getKind() == BinaryOperatorKind.GT) bo.setKind(BinaryOperatorKind.GE);
        else if(bo.getKind() == BinaryOperatorKind.GE) bo.setKind(BinaryOperatorKind.GT);
    }

    @Override
    protected MutationInfo applyMutation(CtBinaryOperator element)
    {
        binaryOperator = element;
        swapConditionalsBoundary(binaryOperator);
        return new MutationInfo(String.format("Swap (< and <=) or (> and >=) between \"%s\" and \"%s\"", binaryOperator.getLeftHandOperand(),
                binaryOperator.getRightHandOperand()), getMethod().getSimpleName());
    }

    @Override
    public void revertMutation()
    {
        swapConditionalsBoundary(binaryOperator);
    }
}
