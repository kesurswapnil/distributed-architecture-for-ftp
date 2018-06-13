
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import java.rmi.server.*;
import java.io.*;

class serverSearchRemote extends UnicastRemoteObject implements serverSearchInterface
{

    static String maindirpath;
    static File maindir;
    static FileOutputStream fout;
    static FileInputStream fin;
    static String t = "\t";

  protected serverSearchRemote() throws RemoteException
  {
    super();
    try
    {
      maindirpath = new String("C:\\Users\\SHRIKANT\\Desktop\\CN");
      maindir = new File(maindirpath);
      fout = new FileOutputStream(maindirpath+"\\details.txt");
      fin = new FileInputStream(maindirpath+"\\details.txt");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
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


  public void createDetailsFile() throws RemoteException
  {
    try
    {
      if(maindir.exists() && maindir.isDirectory())
      {
          File arr[] = maindir.listFiles();
          // Calling recursive method
          RecursivePrint(arr,0,0);
        }
     }
     catch(Exception e)
     {
       e.printStackTrace();
     }
   }
  public void sendDetailsFile() throws RemoteException
  {
    try{

    File f = new File(maindirpath+"\\details.txt");
    byte [] b = new byte[(int)f.length()];
    int c=0;
    int i =0;
    while((i = fin.read())!=-1)
    {
      b[c] = (byte) i;
      c++;
    }
    Socket s = new Socket("localhost",5090);
    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
    out.writeObject(b);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }


  public void sendFile(String ipAdd, String fname, String fdir) throws RemoteException
  {
    try
    {
    //slave node to send file to client
    Socket s = new Socket(ipAdd,5050);
  //  maindirpath = new String("C:\\Users\\SHRIKANT\\Desktop");
    File f;
    FileInputStream fin ;
    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
    BufferedOutputStream bout = new BufferedOutputStream(s.getOutputStream());
    if(fdir.equals("."))
     {
       fin = new FileInputStream (maindirpath+"\\"+fname);
       f = new File(maindirpath+"\\"+fname);
     }
     else
     {
       fin = new FileInputStream(maindirpath+"\\"+fdir+"\\"+fname);
       f = new File(maindirpath+"\\"+fdir+"\\"+fname);
     }
     long size = f.length();
     out.writeObject(size);
     if(size < 1_000_000)
     {
       byte[] b = new byte[(int)size];
       int c=0;
       int i =0;
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

  }
  catch(Exception e)
  {
    e.printStackTrace();
  }
  }

}
