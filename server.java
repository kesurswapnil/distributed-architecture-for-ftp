import java.io.*;
import java.net.*;
import java.util.*;

import java.rmi.server.*;
import java.rmi.*;

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
      String ans = (String)in.readObject();
      //System.out.println(s);
      if(ans.equals("get"))
      {
        ThreadComm tr = new ThreadComm(out,in,bout,socket);
      }
      else if(ans.equals("push"))
      {
          ThreadCommUp trup = new ThreadCommUp(out,in,bout,socket);
            //  System.out.println("vgbh");
      }
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
      Socket socket;

      ThreadComm(ObjectOutputStream out,ObjectInputStream in,BufferedOutputStream bout,Socket socket)
      {
        this.out = out;
        this.in = in;
        this.bout=bout;
        this.socket=socket;
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
        /*String maindirpath = new String("C:\\Users\\SHRIKANT\\Desktop\\CN");
        File maindir = new File(maindirpath);
        if(maindir.exists() && maindir.isDirectory())
        {

            File arr[] = maindir.listFiles();
            // Calling recursive method
            RecursivePrint(arr,0,0);
        }
        */
        serverSearchInterface stub = (serverSearchInterface)Naming.lookup("rmi://localhost:5000/bakya");
        stub.createDetailsFile();

        serverSearchInterface2 stub2 = (serverSearchInterface2)Naming.lookup("rmi://localhost:5010/bakya");
        stub2.createDetailsFile();

        ServerSocket frs = new ServerSocket(5090);

        stub.sendDetailsFile();
        Socket frss = frs.accept();
        //System.out.println("gdvrgvgrg");
        stub2.sendDetailsFile();
        Socket frss2 = frs.accept();

        ObjectInputStream frssin = new ObjectInputStream(frss.getInputStream());
        ObjectInputStream frssin2 = new ObjectInputStream(frss2.getInputStream());
        File f = new File("C:\\Users\\SHRIKANT\\Desktop\\ftp\\details.txt");

        Object df = frssin.readObject();
        Object df2 = frssin2.readObject();
        //  System.out.println("gdvrgvgrg");
        byte[] dfile = (byte[])df;
        byte[] dfile2=(byte[])df2;

        byte[] dfinal = new byte[dfile.length + dfile2.length];
        System.arraycopy(dfile, 0, dfinal, 0, dfile.length);
        System.arraycopy(dfile2, 0, dfinal, dfile.length, dfile2.length);
  //System.out.println("gdvrgvgrg");
        //for(int i=0;i<dfile.length;i++)
      //  {
        //  System.out.println((char)dfile[i]);
      //  }

        frss.close();
        frss2.close();
        frs.close();



        out.writeObject(dfinal);
      //  FileInputStream fin = new FileInputStream("C:\\Users\\SHRIKANT\\Desktop\\ftp\\details.txt");



        Object obj = in.readObject();
        Object obj2 = in.readObject();

        String dir = (String)obj;
        String fname = (String)obj2;

        String ipAdd = socket.getInetAddress().getHostAddress();
        int fs = fname.length();
        if(fname.charAt(fs-1)=='1')
        {
            String fname2 = fname.substring(0,fs-2);
            System.out.println(fname2);
            stub.sendFile(ipAdd,fname2,dir);
        }
        else if(fname.charAt(fs-1)=='2')
        {
            String fname2 = fname.substring(0,fs-2);
            System.out.println(fname2);
            stub2.sendFile(ipAdd,fname2,dir);
        }

  }
}


class ThreadCommUp extends Thread
{
    Socket s;
  //  BufferedInputStream bin ;
    BufferedOutputStream bout ;
    ObjectInputStream in;
    ObjectOutputStream out;

    ThreadCommUp(ObjectOutputStream out,ObjectInputStream in, BufferedOutputStream bout,Socket s)
    {
      try
      {
        this.s=s;
        this.bout = bout;
      //  bin = new BufferedInputStream(s.getInputStream());
        this.out = out;
        this.in = in;
        start();
      }
      catch(Exception e)
      {
        System.out.println(e);
      }
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
        String fname = (String)in.readObject();
        Long size = (Long)in.readObject();
    //    System.out.println(fname+"  "+size);
        serverSearchInterface stub = (serverSearchInterface)Naming.lookup("rmi://localhost:5000/bakya");
        //stub.createDetailsFile();
  //  System.out.println(fname+"  "+size);
        serverSearchInterface2 stub2 = (serverSearchInterface2)Naming.lookup("rmi://localhost:5010/bakya");
        //stub2.createDetailsFile();
  //  System.out.println(fname+"  "+size);
        long free1 = stub.getSpace();
        long free2 = stub2.getSpace();
        //System.out.println(fname+"  "+size+" "+free1+"  "+free2);
        if(size > free1 && size > free2)
        {
          String fate = new String("ERROR");
          out.writeObject(fate);
          String reply = new String("ERROR: Disk space at servers not available.\nAvailable Space in different partitions is a follows:"+free1/1024/1024 + " MB\n"+free2/1024/1024 +" MB");
          out.writeObject(reply);
          return;
        }
        else
        {
          String fate = new String("ROGER");
          out.writeObject(fate);
          String ipAdd = s.getInetAddress().getHostAddress();
          if(free1 - size <= free2-size)
          {
            stub.recvFile(fname,ipAdd);

          }
          else
          {
            stub2.recvFile(fname,ipAdd);
          }
        }


    }


}
