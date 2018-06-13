import java.io.*;
import java.net.*;
import java.util.*;
class Client extends Thread
{
  //Socket socket;
  //ObjectOutputStream out;
  //ObjectInputStream in;
  // BufferedReader br;

  public void run()
  {
    try
    {
      talk();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  synchronized void talk() throws Exception
  {
    Socket socket = new Socket("169.254.108.157",5020);

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
      if(size < 1_000_000)
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

        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(fname));
        BufferedInputStream bin = new BufferedInputStream(socket.getInputStream());
        byte[] buffer = new byte[1024*8];

        int k=0;
          for(int len; (len = bin.read(buffer)) > 0;)
          {
            System.out.println("heyoo" +" len="+len + " "+ ++k);
            bout.write(buffer, 0, len);
            bout.flush();
            if(len<1024*8)
              break;
          }
      //    System.out.println("out of for loop");
      }
    //  System.out.println("out of else");
  //  out.close();
    //socket.close();

  }

  public static void main(String... args) throws Exception
  {
      Client c = new Client();
      c.start();
  }
}
