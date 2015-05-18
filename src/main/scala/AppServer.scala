
import java.io._
import java.net.{InetAddress,ServerSocket,Socket,SocketException}
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
        }
        catch {
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

            val msg = in.readObject().asInstanceOf[RequestMessage];
            val str = msg match {
                    case AddIndebtedRequest(i) =>
                        println("Server: adding indebted")
                        val serv = new ManagerServices()
                        serv.createIndebted(i)
                        "Recebi requisição de inserção do endividado " + i.name
                    case AddPropertyRequest(i, p) =>
                        "Recebi requisição de inserção de bem"
                }
            println(str);

            out.writeObject("Sucesso");
            out.close();
            in.close();
            socket.close()
        }
        catch {
            case e: SocketException =>
                    () // avoid stack trace when stopping a client with Ctrl-C
            case e: IOException =>
                    e.printStackTrace();
        }
    }

}
  
