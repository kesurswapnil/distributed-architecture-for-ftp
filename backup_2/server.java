import java.io.*;
import java.net.*;
import java.util.*;
class Server
{

  public static void main(String... args) throws Exception
  {
    ServerSocket ss = new ServerSocket(5020);

    while(true)
    {
      Socket socket = ss.accept();

      ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

      BufferedOutputStream bout = new BufferedOutputStream(socket.getOutputStream());

      ThreadComm tr = new ThreadComm(out,in,bout);
    //  BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
    }
  }
}


class ThreadComm extends Thread
{
      static String t = new String("\t");
       ObjectOutputStream out;
       ObjectInputStream in;
       BufferedOutputStream bout;
      static FileOutputStream fout;

      ThreadComm(ObjectOutputStream out,ObjectInputStream in,BufferedOutputStream bout)
      {
        this.out = out;
        this.in = in;
        this.bout=bout;
        //this.fout=fout;
        start();
      }

      static void RecursivePrint(File[] arr,int index,int level) throws Exception
         {
             // terminate condition
             if(index == arr.length)
                 return;

             // tabs for internal levels
             for (int i = 0; i < level; i++)
              {
                // System.out.print("\t");
                 fout.write(t.getBytes());
               }

             // for files
             if(arr[index].isFile())
              {
                  // System.out.println(arr[index].getName());
                   String s = new String(arr[index].getName()+"\n");
                   fout.write(s.getBytes());
              }
             // for sub-directories
             else if(arr[index].isDirectory())
             {
                 //System.out.println("[" + arr[index].getName() + "]");
                 String s = new String("[" + arr[index].getName() + "]"+"\n");
                 fout.write(s.getBytes());
                 // recursion for sub-directories
                 RecursivePrint(arr[index].listFiles(), 0, level + 1);
             }

             // recursion for main directory
             RecursivePrint(arr,++index, level);
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


    synchronized  void talk() throws Exception
      {
      fout = new FileOutputStream("C:\\Users\\SHRIKANT\\Desktop\\ftp\\details.txt");
      String maindirpath = new String("J:\\cs");
      File maindir = new File(maindirpath);
      if(maindir.exists() && maindir.isDirectory())
        {

            File arr[] = maindir.listFiles();
            // Calling recursive method
            RecursivePrint(arr,0,0);
          }

        File f = new File("C:\\Users\\SHRIKANT\\Desktop\\ftp\\details.txt");
        FileInputStream fin = new FileInputStream("C:\\Users\\SHRIKANT\\Desktop\\ftp\\details.txt");

        byte [] b = new byte[(int)f.length()];
        int c=0;
        int i =0;
        while((i = fin.read())!=-1)
        {
          b[c] = (byte) i;
          c++;
        }

        out.writeObject(b);
        Object obj = in.readObject();
        Object obj2 = in.readObject();
        String dir = (String)obj;
        String fname = (String)obj2;
        //System.out.println(fname);
        if(dir.equals("."))
        {
          fin = new FileInputStream (maindirpath+"\\"+fname);
          f = new File(maindirpath+"\\"+fname);
        }
        else
        {
          fin = new FileInputStream(maindirpath+"\\"+dir+"\\"+fname);
          f = new File(maindirpath+"\\"+dir+"\\"+fname);
        }

        System.out.println(f.length());
        long size = f.length();
        out.writeObject(size);
        if(size < 1_000_000)
        {
          b = new byte[(int)size];
          c=0;
          i =0;
          while((i = fin.read())!=-1)
          {
            b[c] = (byte) i;
            c++;
          }

          out.writeObject(b);
        }
        else
        {
          BufferedInputStream bin = new BufferedInputStream(fin);
        //  bout = new BufferedOutputStream(socket.getOutputStream());
          byte [] buffer = new byte[1024*8];

          int k=0;
            for(int len; (len = bin.read(buffer)) > 0;)
            {
              System.out.println("heyo len= "+len+"  " + ++k);
              bout.write(buffer, 0, len);
              bout.flush();

            }

            System.out.println("out of for loop");
        }
        System.out.println("out of else");



    //in.close();
  //  ss.close();
    //socket.close();

  }
}
