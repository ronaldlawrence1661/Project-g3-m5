import java.io.*;

public class OOS extends ObjectOutputStream {
    //  for appending objects to a file without corrupting the stream.
    //modifies how stream header is written

    OOS() throws IOException
    {
 
        
        super();
    }
 
    
    //Parameterized constructor
    OOS(OutputStream o) throws IOException
    {
        super(o);
    }
 
    // Method of this class
    public void writeStreamHeader() throws IOException
    {
        return;
    }

} 


