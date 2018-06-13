

import java.rmi.*;
import java.rmi.server.*;
 class RMIServer
{
  public static void main(String... args) throws Exception
  {
    serverSearchInterface stub = new serverSearchRemote();
    Naming.rebind("rmi://localhost:5000/bakya",stub);
  }
}
