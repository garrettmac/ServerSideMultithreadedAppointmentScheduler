 package com.macmacdevelopment.ServerSideMultithreadedAppointmentScheduler;

    import java.io.*;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.net.UnknownHostException;


    /**
     * This class Main is the server side of a server-client appointment scheduling system.
     * This application uses sockets to send and retrieve data from the client. The client
     * operates on android devices and sends requests to the server to request appointments.
     * If their requested day is available then the date is scheduled. If the date is not
     * available than the client gets a message containing the array set of days already
     * reserved.
     *
     * @author MacMacDevelopment - Garrett McMillan
     *  4/16/2015
     */
    public class ServerSideMultithreadedAppointmentScheduler {


        private static int count = 0;

        public static void main(String[] args) throws IOException, UnknownHostException {

            new ServerSideMultithreadedAppointmentScheduler().RunServer();

        }
        /** If the sockets handshake then increment to count, display appropriate message for
         * the different scheduling cases via sockets
         */
        private void RunServer ()throws IOException {

            ServerSocket serverSocket = new ServerSocket(9998);

            System.out.println("Listening for a connection...... \n");

            while (true) {

                Socket socket = serverSocket.accept();
                new ServerSocketThread(socket).start();
                count++;
                System.out.println("\n###########| Client Request Info |########### \nClient's Request Number: " + count);

            }


        }
    }
