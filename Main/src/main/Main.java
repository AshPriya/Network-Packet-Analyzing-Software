/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.swing.JFrame;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.*;
/**
 *
 * @author chinthaka
 */
public class Main extends JFrame{
    
    public static void main(String args[]){
        
        Main gui = new Main();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        gui.pack();
        gui.setSize(1100, 600);
        gui.setTitle("Caputure Machine");
        
    try{ 
        
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/project?" + 
                                   "user=root&password=";
            Connection con = DriverManager.getConnection(connectionUrl);
            
            DatabaseMetaData dm =con.getMetaData();
            String dbversion=dm.getDatabaseProductVersion();
            String dbname=dm.getDatabaseProductName();
            System.out.println("name:"+dbname);
            System.out.println("version:"+dbversion);
        
            /* variables */
            JpcapCaptor captor;
            NetworkInterface[] list;
            int x, choice,pkts=0;

            Scanner in = new Scanner(System.in);

            /* first fetch available interfaces to listen on */
            list = JpcapCaptor.getDeviceList();

            System.out.println("Interfaces: ");

            for(x=0; x<list.length; x++){
                System.out.println(x + " : "+list[x].description); 
            }

            System.out.println("-------------------------\n");

            choice = Integer.parseInt(getInput("Choose the Interface : "));

            System.out.println("Listening on interface -> "+list[choice].datalink_description);

            System.out.println("-------------------------\n");

            /*Setup device listener */

            //JpcapWriter writer = null;
            try {
                captor = JpcapCaptor.openDevice(list[choice], 65535, false, 20);

                /* start listening for packets */
                int i = 0;
                System.out.print("Enter number of packets: ");
                pkts = in.nextInt();
                while (i<pkts) {

                    Packet packet = captor.getPacket();

                    if(packet != null){
                                // get type of packets
                               String s = packet.getClass().toString();
                               String arr[] = s.split(" ");
                               String arr1[] = arr[1].split("\\."); 
                               String pkttype = arr1[2];

                       System.out.println();
                       System.out.println();

                       if(pkttype.equals("UDPPacket")){

                            UDPPacket p = (UDPPacket)packet;

                            // Get the tcp src and dest ports
                            String destPort =""+ p.dst_port ;
                            String srcPort =""+ p.src_port;

                            // Get the src and dest IP addresses from the IP layer
                            String srcIp = p.src_ip.toString();
                            String dstIp = p.dst_ip.toString();

                            int pktversion  = p.version;

                            String dlp= p.datalink.toString();
                            String [] strdlp = dlp.split(" ");
                            String [] mac = strdlp[1].split("->");

                            String srcmac = mac[0];
                            String destmac = mac[1];

                            int pktsize = p.len;
                            int pktNo = i+1;
                            
                            Statement stmt=(Statement)con.createStatement();
                            String insert="INSERT INTO packet VALUES("+pktNo+",'"+pkttype+"','"+pktversion+"','"+srcIp+"','"+dstIp+"','"+srcPort+"','"+destPort+"','"+srcmac+"','"+destmac+"',"+pktsize+")";
                            stmt.executeUpdate(insert);
                            i++;

                        }else if(pkttype.equals("TCPPacket")){
                            
                               TCPPacket p = (TCPPacket)packet;

                                // Get the tcp src and dest ports
                               String destPort =""+ p.dst_port ;
                               String srcPort =""+ p.src_port;

                               // Get the src and dest IP addresses from the IP layer
                               String srcIp = p.src_ip.toString();
                               String dstIp = p.dst_ip.toString();

                               int pktversion  = p.version;
                              // System.out.println(p.version);

                               String dlp= p.datalink.toString();
                               String [] strdlp = dlp.split(" ");
                               String [] mac = strdlp[1].split("->");

                               String srcmac = mac[0];
                               String destmac = mac[1];

                               int pktsize = p.len;
                               int pktNo = i+1;

                               Statement stmt=(Statement)con.createStatement();
                               String insert="INSERT INTO packet VALUES("+pktNo+",'"+pkttype+"','"+pktversion+"','"+srcIp+"','"+dstIp+"','"+srcPort+"','"+destPort+"','"+srcmac+"','"+destmac+"',"+pktsize+")";
                               stmt.executeUpdate(insert);
                               i++;
                               
                            }else if(pkttype.equals("ARPPacket")){

                                ARPPacket p = (ARPPacket)packet;

                                // Get the tcp src and dest ports
                            String destPort ="" ;
                            String srcPort ="";

                                // Get the src and dest IP addresses from the IP layer
                                String srcIp = "";
                                String dstIp = "";

                                int pktversion = -1 ;

                                String dlp= p.datalink.toString();
                                String [] strdlp = dlp.split(" ");
                                String [] mac = strdlp[1].split("->");

                                String srcmac = mac[0];
                                String destmac = mac[1];

                                int pktsize = p.len;
                                int pktNo = i+1;

                                Statement stmt=(Statement)con.createStatement();
                                String insert="INSERT INTO packet VALUES("+pktNo+",'"+pkttype+"','"+pktversion+"','"+srcIp+"','"+dstIp+"','"+srcPort+"','"+destPort+"','"+srcmac+"','"+destmac+"',"+pktsize+")";
                                stmt.executeUpdate(insert);
                                i++;

                            }else if(arr1[2].equals("ICMPPacket")){

                                ICMPPacket p = (ICMPPacket)packet;

                                // Get the tcp src and dest ports
                                String destPort ="" ;
                                String srcPort ="";

                                // Get the src and dest IP addresses from the IP layer
                                String srcIp = p.src_ip.toString();
                                String dstIp = p.dst_ip.toString();

                                int pktversion  = p.version;

                                String dlp= p.datalink.toString();
                                String [] strdlp = dlp.split(" ");
                                String [] mac = strdlp[1].split("->");

                                String srcmac = mac[0];
                                String destmac = mac[1];

                                int pktsize = p.len;
                                int pktNo = i+1;

                                Statement stmt=(Statement)con.createStatement();
                                String insert="INSERT INTO packet VALUES("+pktNo+",'"+pkttype+"','"+pktversion+"','"+srcIp+"','"+dstIp+"','"+srcPort+"','"+destPort+"','"+srcmac+"','"+destmac+"',"+pktsize+")";
                                stmt.executeUpdate(insert);
                                i++;
                            }else{}
                    }

            }//end while
            }catch(IOException ioe) { ioe.printStackTrace(); }
     
            System.out.println();
            System.out.println("\n-------------------------------------------------");  
            
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("select pktType,srcPort,srcIP from packet");
                        String query;
                        
                        while (rs.next()) {
                            String pktType = rs.getString("pktType");
                            String srcIp = rs.getString("srcIP");
                            String srcPort = rs.getString("srcPort");
                            System.out.println("pktType = "+pktType+"\nsrcPort = " + srcPort+"\nsrcIP = "+srcIp);
                        }

            while(true){
                
                System.out.print("\nDo you want to Start Quering?(y/n)");
                char check = in.next().charAt(0);

                if(check == 'y'){

                    System.out.println("\n------------Please chose a option-----------\n");
                    System.out.println(" 1. Packets form source IP \n 2. Packets from Source port. \n 3. Packets from Source IP and Port.\n 4. All IPs or Port Numbers.\n 5. Shered  total bytes.\n 6.Select by Packet Type.\n");
                    System.out.print("\nEnter the option number: ");
                    int num = in.nextInt();

                    if(num == 1 ){

                        System.out.print("\nEnter the source IP: ");
                        String ip =in.next();
                        
                        query = "select srcIP,destIP from packet where srcIP ='"+ip+"'";
                        display(st,rs,query);
                        

                        }else if(num == 2) {

                            System.out.print("\nEnter the source port: ");
                            int port = in.nextInt(); 
                            
                            query = "select srcIP,destIP from packet where srcPort ='"+port+ "'";
                            display(st,rs,query);

                        }else if(num == 3){
                            
                            System.out.print("\nEnter the source IP: ");
                            String srcip = in.next();
                            
                            System.out.print("\nEnter the source port: ");
                            int srcport = in.nextInt();

                            query = "select srcIP,destIP from packet where srcIP ='"+srcip+ "' and srcPort ='"+srcport+"'";
                            display(st,rs,query);
                            
                        }else if(num == 5){
                            
                            int totsize = 0;
                            
                            rs = st.executeQuery("select pktsize from packet");
                            while (rs.next()) {

                                int pktsize = rs.getInt("pktsize");
                                
                                totsize = totsize + pktsize;
                            }
                            
                            System.out.println("Total data: "+ totsize+ " bytes");
                            
                        }else if(num == 4){

                                System.out.print("\n 1. All source IP Addresses \n 2. All dest IP Addresses \n 3. All source ports. \n 4. All dest ports. \n ");
                                System.out.print("\nPlease Enter a option: ");
                                int num1 = in.nextInt();
                                System.out.println();
                                
                                if(num1 == 1){
                                    
                                    rs = st.executeQuery("select srcIP from packet");
                                    
                                    while (rs.next()) {
                                        String srcIp = rs.getString("srcIP");
                                        System.out.println("srcIp = "+srcIp);
                                    }
                                    
                                    System.out.println();
                                    
                                }else if(num1 == 2){
                                    
                                    rs = st.executeQuery("select destIP from packet");
                                    
                                    while (rs.next()) {
                                        String destIp = rs.getString("destIP");
                                        System.out.println("destIp = "+destIp);
                                    }
                                    
                                    System.out.println();
                                    
                                }else if(num1 == 3){
                                    
                                    rs = st.executeQuery("select srcPort from packet");
                                    
                                    while (rs.next()) {
                                        String srcPort = rs.getString("srcPort");
                                        System.out.println("srcPort = "+srcPort);
                                    }
                                    
                                    System.out.println();
                                    
                                }else if(num1 == 4){
                                    
                                    rs = st.executeQuery("select destPort from packet");
                                    
                                    while (rs.next()) {
                                        String destPort = rs.getString("destPort");
                                        System.out.println("destPort = "+destPort);
                                    }
                                    
                                    System.out.println();
                                    
                                }else{
                                    System.out.println("\nWron selection or not select.");
                                }

                        }else if(num == 6){
                            System.out.print("\nEnter the Packet Type: ");
                            String type = in.next();

                            if(type.equals("tcp")){
                                
                                String pktType = "TCPPacket";
                                query = "select pktType,srcIP,destIP from packet where pktType ='"+pktType+"'";
                                display(st,rs,query);
                            
                            }else if(type.equals("udp")){

                                String pktType = "UDPPacket";
                                query = "select pktType,srcIP,destIP from packet where pktType ='"+pktType+"'";
                                display(st,rs,query);

                            }else if(type.equals("arp")){

                                String pktType = "ARPPacket";
                                query = "select pktType,srcIP,destIP from packet where pktType ='"+pktType+"'";
                                display(st,rs,query);

                            }else if(type.equals("icmp")){

                                String pktType = "ICMPPacket";
                                query = "select pktType,srcIP,destIP from packet where pktType ='"+pktType+"'";
                                display(st,rs,query);
                                
                            }else{}
                            
                        }else {}//end else

                    }else{
                        System.out.println("\n------------------------------------------------\n End the Program..");
                        System.exit(1);
                    }
                }//end while

    } catch (SQLException e) {
            System.out.println("SQL Exception: "+ e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: "+ cE.toString());
        }
    }//end Main

    /* get user input */
    
    
    public static void display(Statement st,ResultSet rs,String query){
        try{
            int count = 0;

            rs = st.executeQuery(query);
            while (rs.next()) {

                String pktType = rs.getString("pktType");
                String srcIp = rs.getString("srcIP");
                String destIp = rs.getString("destIP");

                System.out.println("pktType = "+pktType+"\nsrcIP = " + srcIp + "\ndestIP = " + destIp);
                System.out.println("");
                count++;
            }

            System.out.println("\n"+count + " result found.");

            } catch (SQLException e) {
                System.out.println("SQL Exception: "+ e.toString());
        }
    }
    
    
    public static String getInput(String q) {
        
        String input = "";
        System.out.print(q); 
        
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            
            input = bufferedreader.readLine();
            
        }catch(IOException ioexception){}

        return input;
    }//end get input

}//end Main class
