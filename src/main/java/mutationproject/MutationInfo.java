package mutationproject;

public class MutationInfo
{
    private String description, filePath, methodName;

    public MutationInfo(String d, String method)
    {
        description = d;
        methodName = method;
    }

    public void setFilePath(String path)
    {
        filePath = path;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public String getDescription()
    {
        return description;
    }
}
