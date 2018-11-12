public interface IMutationProcessor
{
    void revertChanges();
    String getMutationDescription();
}
