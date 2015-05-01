        package com.macmacdevelopment.ServerSideMultithreadedAppointmentScheduler;

        import java.io.*;
        import java.net.Socket;
        import java.net.SocketException;
        import java.util.LinkedHashSet;
        import java.util.Set;

        /**
         * Created by garrettmac on 4/16/15.
         */
        public class ServerSocketThread extends Thread {
            /** The public variables set a count and input and output from client.
             * This includes sockets,serverSockets,inputStreams,outputStreams and
             * an empty string for in/out stream messages
             * @param serverSocket The server side Socket
             * @param socket The client side Socket
             * @param dataInputStream Data Input Stream
             * @param dataOutputStream Data Output Stream
             * @param messageToClient Empty String for storing messages to the Client from a stream
             * */
            private static DataInputStream dataInputStream = null;
            private static DataOutputStream dataOutputStream = null;
            /**@param serverDaysReserveredSet stores the set of booked/reserved days on the server side from the client  */
            private static Set< String > serverDaysReserveredSet = new LinkedHashSet< >();
            Socket socket;

            ServerSocketThread(Socket socket){

                this.socket = socket;
            }

            public void run(){
                // empty sting for storing incoming InputStream string
                String messageToClient = "";


                try {

                    System.out.println("Client IP:" + socket.getInetAddress()
                            + "\nClient Port:" + socket.getLocalPort() + "\n");
                    // + "\nClient Port:" + "9998" + "\n");
                    System.out.println("Current Thread: "+ currentThread());


                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    /** client number incrementation */
                    // System.out.println(client += count);

                    /** Writes to output if appropriate */
                    dataOutputStream.writeUTF("These are the days already reserved: " + serverDaysReserveredSet);

                    //String msgToClient = dataInputStream.readUTF();
                    /**@param clientDayRequest Stores the current day the client requested via a DataInputStream */
                    String clientDayRequest = dataInputStream.readUTF();

                    //NOT AVAILABLE
                    /** Displays appropriate messages to the client and server
                     * if the day the client requested is already reserved*/
                    if (serverDaysReserveredSet.contains(clientDayRequest)) {

                        /** Sends message to the client they have to reschedule an available day*/
                        dataOutputStream.writeUTF("Sorry, " + clientDayRequest +
                                "'s already booked. These days are already reserved.\n " + serverDaysReserveredSet + "\n");

                        /** Sends message to the server about the clients request*/
                        System.out.println("A Client tried to book "+clientDayRequest+
                                " but it is one of the following days already booked.\n " + serverDaysReserveredSet + "\n\n");
                    }
                    //AVAILABLE
                    /** Handles the case the requested client day is available to book and sets that day to reserved*/
                    if (!serverDaysReserveredSet.contains(clientDayRequest)) {

                        /** Sends message to the server about the clients request*/
                        System.out.println("A Client just Booked " + clientDayRequest + "! It will now be added to this " +
                                "list of booked days!\n"+serverDaysReserveredSet);

                        /** Sends message to the client they were available to book there requested day*/
                        dataOutputStream.writeUTF("Great! We got you booked for " + clientDayRequest + "! We'll see you then!\n");
                        // dataOutputStream.writeUTF("You Are connected to the server!\n" + messageToClient);
                        //messageToClient ="1Great! We got you booked for " + clientDayRequest + "! We'll see you then!\n";
                        //msgToClient ="2Great! We got you booked for " + clientDayRequest + "! We'll see you then!\n";

                        /** Sets the new scheduled appointment to unavailable*/
                        serverDaysReserveredSet.add(clientDayRequest);
                        try {
                            socket.close();System.out.print("\n.....Socket Disconnected\n\n");
                        }
                        catch (SocketException e1) {
                            e1.printStackTrace();
                        }
                    }
                    /** Display Appropriate messages on Exit if necessary*/
                    // dataOutputStream.writeUTF("These are the days already reserved: " + serverDaysReserveredSet);
                    // dataOutputStream.writeUTF("You Selected: " + clientDayRequest);

                } catch (NullPointerException e1) {
                    e1.printStackTrace();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }

