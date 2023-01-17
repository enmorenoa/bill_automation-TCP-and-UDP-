package pcd.util;

import java.net.*;

import pcd.util.TextIO4GUI;

public class WhoIs {
  public static void main(String[] args) 
      throws Exception {
	  TextIO4GUI TextIO = new TextIO4GUI("Who Is Who");
    if(args.length != 1) {
      System.err.println(
        "Uso: WhoIs Nombre");
      System.exit(1);
    }
    InetAddress a = InetAddress.getByName(args[0]);
    TextIO.getlnString();
    TextIO.putln(a);
    
  }
} 
