package mutation;

import mutationproject.MutationInfo;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.compiler.SnippetCompilationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ReturnDefaultOperator extends MutationOperator<CtMethod>
{
    private List<CtStatement> backup;

    public ReturnDefaultOperator(BiConsumer<MutationOperator<CtMethod>, MutationInfo> mutationConsumer)
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
        return super.isToBeProcessed(candidate) && candidate.getType().isPrimitive() && !candidate.getType().equals(getFactory().Type().voidPrimitiveType());
    }

    @Override
    protected MutationInfo applyMutation(CtMethod ctMethod)
    {
        backup = new ArrayList<>(ctMethod.getBody().getStatements());
        ctMethod.getBody().getStatements().clear();

        if(ctMethod.getType().getSimpleName().equals("boolean")) {
            ctMethod.getBody().addStatement(getFactory().<Boolean>createReturn().setReturnedExpression(getFactory().Code().createLiteral(false)));
        } else {
            ctMethod.getBody().addStatement(SnippetCompilationHelper.compileStatement(
                    getFactory().Code().createCodeSnippetStatement("return 0"),
                    ctMethod.getType()));
        }
       return new MutationInfo("Empty body and return default value", ctMethod.getSimpleName());
    }

    @Override
    protected void revertMutation()
    {
        List<CtStatement> statements = getMethod().getBody().getStatements();
        statements.clear();
        statements.addAll(backup);
    }
}
