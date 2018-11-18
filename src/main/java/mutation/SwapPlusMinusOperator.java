package mutation;

import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

import java.util.function.BiConsumer;

public class SwapPlusMinusOperator extends MutationOperator<CtBinaryOperator>
{
    private CtBinaryOperator binaryOperator;

    public SwapPlusMinusOperator(BiConsumer<MutationOperator<CtBinaryOperator>, CtElement> mutationConsumer)
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

    private static void swapPlusMinus(CtBinaryOperator bo)
    {
        if(bo.getKind() == BinaryOperatorKind.PLUS)
            bo.setKind(BinaryOperatorKind.MINUS);
        else if(bo.getKind() == BinaryOperatorKind.MINUS)
            bo.setKind(BinaryOperatorKind.PLUS);
    }

    @Override
    protected void applyMutation(CtBinaryOperator element)
    {
        binaryOperator = element;
        swapPlusMinus(binaryOperator);
    }

    @Override
    public void revertMutation()
    {
        swapPlusMinus(binaryOperator);
    }

    @Override
    public String getMutationDescription()
    {
        return String.format("Swap plus and minus between %s and %s in method %s",
            binaryOperator.getLeftHandOperand(),  binaryOperator.getRightHandOperand(), getMethod().getSimpleName());
    }
}
