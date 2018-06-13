

import java.rmi.*;
import java.rmi.server.*;
 class RMIServer2
{
  public static void main(String... args) throws Exception
  {
    serverSearchInterface2 stub = new serverSearchRemote2();
    Naming.rebind("rmi://localhost:5010/bakya",stub);
  }
}
