package server;

import java.util.ArrayList;

import jlibrtp.Participant;

public class VoIPWorker extends Thread {
  private static ArrayList<PacketInfo> sipClients = new ArrayList<PacketInfo>();
  private PacketInfo client;
  private Boolean busy;

  public VoIPWorker(PacketInfo client) {
    this.client = client;
    this.addClient(client);
  }

  public void run() {
    try {
      this.busy = true;

      SoundSender aDemo = new SoundSender(false, "C:\\setup\\music.wav");
      Participant p = new Participant(this.client.senderAddress,
      		Integer.parseInt(this.client.senderRtpPort), 0);
      aDemo.rtpSession.addParticipant(p);
      aDemo.start();
      try {
          while(aDemo.isAlive()){
              Thread.sleep(800);
              System.out.println("Connection with client "
	              + this.client.senderAddress + ":" + this.client.senderRtpPort
	              + "is still " + aDemo.isAlive());
          }
      } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      //Thread.sleep(5000);//for Basic grade

      this.busy = false;
//      SIPMessages.Bye(this.client);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    

    
    this.removeClient(this.client);
  }

  private void addClient(PacketInfo packetInfo) {
    VoIPWorker.sipClients.add(packetInfo); 
  }

  private void removeClient(PacketInfo candidateClient) {
    VoIPWorker.sipClients.remove(candidateClient);
  }

  private PacketInfo getClient(String address) {
    for(PacketInfo obj : VoIPWorker.sipClients){
      if(obj.senderUsername.equals(address)) {
        return obj;
      }
    }
    return null;
  }

  public static Integer numClients() {
    return VoIPWorker.sipClients.size();
  }
}
