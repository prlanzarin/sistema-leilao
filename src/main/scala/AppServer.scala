
import java.io._
import java.net.{ InetAddress, ServerSocket, Socket, SocketException }
import java.util.Random
import business.entities._
import business.services._

object AppServer {
    def main(args: Array[String]): Unit = {
        try {
            val listener = new ServerSocket(9999);
            while (true)
                new ServerThread(listener.accept()).start();
            listener.close()
        } catch {
            case e: IOException =>
                System.err.println("Could not listen on port: 9999.");
                System.exit(-1)
        }
    }

}

case class ServerThread(socket: Socket) extends Thread("ServerThread") {
    override def run(): Unit = {
        try {
            val out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
            val in = new ObjectInputStream(
                new DataInputStream(socket.getInputStream()));

            while (socket.isBound()) {
                val msg = in.readObject().asInstanceOf[RequestMessage];
                val r = msg match {
                        case AddIndebtedRequest(i) =>
                            println("Server: adding indebted")
                            val serv = new ManagerServices()
                            if (serv.insertIndebted(i))
                                AddIndebtedReply("Success")
                            else
                                new AddIndebtedReply("Failed")

                    case AddPropertyRequest(i, p) =>
                        println("Server: adding property")
                        val serv = new ManagerServices()
                        if (serv.insertProperty(i, p))
                            AddPropertyReply("Success")
                        else
                            new AddPropertyReply("Failed")
                    case QueryIndebtedsRequest() =>
                        println("Server: querying indebted")
                        val serv = new ManagerServices()
                        val indebted = serv.getIndebteds()
                        if (indebted.isEmpty)
                            QueryIndebtedsReply(None)
                        else
                            QueryIndebtedsReply(Some(indebted))
                    case _ => throw new SocketException // TODO Create other exception
                }

            out.writeObject(r);
            }

            out.close();
            in.close();
            socket.close()
        } catch {
            case e: SocketException =>
                () // avoid stack trace when stopping a client with Ctrl-C
            case e: IOException =>
                e.printStackTrace();
        }
    }

}
  