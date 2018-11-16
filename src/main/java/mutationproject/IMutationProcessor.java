package mutationproject;

public interface IMutationProcessor
{
    void revertChanges();
    String getMutationDescription();
}
