import java.io.*;
import java.net.*;
import java.util.*;

class Client extends Thread
{
  //Socket socket;
  //ObjectOutputStream out;
  //ObjectInputStream in;
  // BufferedReader br;

  String ans = new String("");

  Client(String ans)
  {
    this.ans=ans;
  }

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
    Socket socket = new Socket("localhost",5020);

    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    out.writeObject(ans);

    if(ans.equals("get"))
    {
        Object o = in.readObject();
        byte[] b = (byte[]) o;

        System.out.println("********************************************");
        System.out.println("Files from remote server : ");
        System.out.println("********************************************");

        for(int i=0;i<b.length;i++)
        {
          System.out.print((char)b[i]);
        }
        //Scanner sc = new Scanner(System.in);

        System.out.println("Enter name of file to be retrived: ");
        String fname = br.readLine();
        int fs=fname.length();
        String fname2 = fname.substring(0,fs-2);

        System.out.println("Enter path of file: ");
        String dir = br.readLine();

        out.writeObject(dir);
        out.writeObject(fname);


        ServerSocket ss = new ServerSocket(5050);
        Socket rs= ss.accept();
        ObjectInputStream rsin = new ObjectInputStream(rs.getInputStream());
      //  BufferedOutputStream rsbout = new BufferedOutputStream(rs.getOutputStream());

        o = rsin.readObject();
        Long size = (Long)o;

        File maindir = new File("C:\\Users\\SHRIKANT\\Desktop\\ftp");
        String maindirpath = "C:\\Users\\SHRIKANT\\Desktop\\ftp";

        long freeSpace = maindir.getFreeSpace();

        if(size > freeSpace)
        {
          System.out.println("ERROR: Disk space not available. Free some space before downloading.");
          System.out.println("Free Space : " + freeSpace/1024/1024 +" MB" );
          System.out.println("Additional free space required: " + (size-freeSpace)/1024/1024 + " MB" );
          return;
        }

        File f = new File(fname2);
        f.createNewFile();

        if(size < 1_000_000)
        {
        //  System.out.println("Recieved from one byte array");
          o = rsin.readObject();
          b=(byte[])o;
          FileOutputStream fout = new FileOutputStream(fname2);
          fout.write(b);
         }

        else
        {
          System.out.println("Downloading from buffered stream...");

          BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(fname2));
          BufferedInputStream bin = new BufferedInputStream(rs.getInputStream());
          byte[] buffer = new byte[1024*8];

          long k=size/100;
          long downloaded = 0;
          long p=0;
          //StringBuilder dbar  = new StringBuilder(p+" %");
            for(int len; (len = bin.read(buffer)) > 0;)
            {
                downloaded+=len;
                p=downloaded/k;
                //dbar=downloaded/k + "%";
                System.out.print("Downloading : "+ p+" %\r");
              //  downloaded = 0;

              bout.write(buffer, 0, len);
              bout.flush();
              if(len<1024*8)
                break;
            }

        }
        System.out.println("File "+fname2+" downloaded successfully.");
      }

      else if(ans.equals("push"))
      {
        System.out.println("Enter name of file to be uploaded: ");
        String fname = br.readLine();
        System.out.println("Enter directory of file : ");
        String maindirpath = br.readLine();


        File f = new File(maindirpath +"\\"+fname);

        long size = f.length();

        FileInputStream fin = new FileInputStream(maindirpath +"\\"+fname);

        out.writeObject(fname);

        out.writeObject(size);

        String fate = (String)in.readObject();

        if(fate.equals("ROGER"))
        {
          ServerSocket ss = new ServerSocket(5060);
          Socket rs = ss.accept();

          BufferedOutputStream bout = new BufferedOutputStream(rs.getOutputStream());
          BufferedInputStream bin = new BufferedInputStream(fin);

          byte [] buffer = new byte[1024*8];

          int k,p=0;
          long k=size/100;
          long downloaded = 0;
          for(int len; (len = bin.read(buffer)) > 0;)
          {
            //System.out.println("heyo len= "+len+"  " + ++k);
            downloaded+=len;
            p=downloaded/k;
            System.out.println("Uploading: "+p+" %\r");
            bout.write(buffer, 0, len);
            bout.flush();
          }
          //System.out.println("out of for loop");

          System.out.println("File sent.");
        }
        else
        {
          System.out.println((String)in.readObject());
          return;
        }

        }
  }

  public static void main(String... args) throws Exception
  {

    System.out.println("Enter get for download, push for upload\n");

    //  for(int i=0;i<100000;i++);

      //System.out.println("\n");
      System.out.print("$:");
      String ans = (new BufferedReader(new InputStreamReader(System.in))).readLine();

        Client c = new Client(ans);
        c.start();

  }
}
