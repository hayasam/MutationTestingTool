public abstract class TestClass
{
    private int counter;

    public TestClass()
    {
        counter = 0;
    }

    public void incrementCounter()
    {
        ++counter;
    }

    public void addToCounter(int value)
    {
        counter = counter + value;
    }

    public void subtractFromCounter(int value)
    {
        counter = counter - value;
    }

    public int getCounter()
    {
        return counter;
    }

    public Integer getCounterObject()
    {
        Integer i = Integer.valueOf(counter);
        return i;
    }

    public boolean isCounterHigh()
    {
        return counter > 10;
    }

    public boolean isCounterCloseToZero()
    {
        return counter < 5 && counter > -5;
    }

    public boolean isCounterFarFromZero()
    {
        return counter > 10 || counter < -10;
    }

    public abstract void abstractMethod();

    public static void main(String[] args)
    {
        System.out.println("Hello world");
    }
}