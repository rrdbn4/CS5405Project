package code;
//Global singleton for thread control
public class Control 
{
   private static Control inst = null;
   protected Control() {}

   private boolean isPaused = true; //just an example. Add more control later
   public static int MAX_SPEED = 500;
   public static int MIN_SPEED = 1;
   public static int MAX_NUM_OF_ELEMENTS = 500;
   public static int MIN_NUM_OF_ELEMENTS = 10;

   public static Control getInstance() 
   {
      if(inst == null) 
         inst = new Control();

      return inst;
   }

   public void setIsPaused(boolean isPaused)
   {
   		this.isPaused = isPaused;
   }

   public boolean isPaused()
   {
   		return isPaused;
   }
}