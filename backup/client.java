import java.io.*;
import java.net.*;
import java.util.*;
class Client
{


  public static void main(String... args) throws Exception
  {
     Socket socket = new Socket("169.254.249.164",5020);

     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
     ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

     Object o = in.readObject();
     byte[] b = (byte[]) o;

     System.out.println("********************************************");
     System.out.println("Files from remote server : ");
     System.out.println("********************************************");

     for(int i=0;i<b.length;i++)
     {
       System.out.print((char)b[i]);
     }
     Scanner sc = new Scanner(System.in);

       System.out.println("Enter name of file to be retrived: ");
       String fname = sc.nextLine();
       System.out.println("Enter path of file: ");
       String dir = sc.nextLine();

       out.writeObject(dir);
       out.writeObject(fname);
       o = in.readObject();
       Long size = (Long)o;
       File f = new File(fname);
       f.createNewFile();
       if(size < 1_000)
      {
          System.out.println("Recieved from one byte array");
         o = in.readObject();
         b=(byte[])o;
         FileOutputStream fout = new FileOutputStream(fname);
         fout.write(b);
       }

       else
       {
         System.out.println("Recieved from buffered stream");
         BufferedInputStream bin = new BufferedInputStream(socket.getInputStream());
         BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(fname));
         byte[] buffer = new byte[1024];
         for(int len; (len = bin.read(buffer)) > 0;)
         {
            bout.write(buffer, 0, len);
         }
       }

     out.close();
     socket.close();
  }
}
