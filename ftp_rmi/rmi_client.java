

import java.rmi.*;
import java.util.*;
 class RMIClient
{
  static String ipAdd = new String("");
  static String fname = new String("");
  static String fdir = new String("");

  public RMIClient(String ipAdd,String fname,String fdir)
  {
    this.ipAdd = ipAdd;
    this.fname = fname;
    this.fdir = fdir;
  }

  public static void main(String... args) throws Exception
  {
    serverSearchInterface stub = (serverSearchInterface)Naming.lookup("rmi://localhost:5000/bakya");
    //System.out.println("Enter expression as : \"operand 1\"  \"operator\"  \"operand 2\"  ");
    //String s = (new Scanner(System.in)).nextLine();
    //Mul2 stub2 = (Mul2)Naming.lookup("rmi://localhost:6000/bakya");
    stub.sendFile(RMIClient.ipAdd,RMIClient.fname,RMIClient.fdir);
  }
}
