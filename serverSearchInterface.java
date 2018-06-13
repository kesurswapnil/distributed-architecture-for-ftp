

import java.rmi.*;

public interface serverSearchInterface extends Remote
{
  public void sendFile(String ipAdd,String fname,String fdir) throws RemoteException;
  public void createDetailsFile() throws RemoteException;
  public void sendDetailsFile() throws RemoteException;
  public long getSpace() throws RemoteException;
  public void recvFile(String fname,String ipAdd) throws RemoteException;
}
