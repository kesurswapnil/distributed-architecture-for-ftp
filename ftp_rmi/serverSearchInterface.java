

import java.rmi.*;

public interface serverSearchInterface extends Remote
{
  public void sendFile(String ipAdd,String fname,String fdir) throws RemoteException;
  public void createDetailsFile() throws RemoteException;
}
