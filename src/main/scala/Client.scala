
import java.io._
import java.net.{ InetAddress, ServerSocket, Socket, SocketException }
import java.util.Random
import business.entities.Indebted
import business.entities._
import java.util.Date

object Client {

    def main(args: Array[String]) {
        try {
            val ia = InetAddress.getByName("localhost")
            val socket = new Socket(ia, 9999)
            val out = new ObjectOutputStream(
                new DataOutputStream(socket.getOutputStream()))
            val in = new ObjectInputStream(new DataInputStream(socket.getInputStream()))

            val indebted = new Indebted("Fulano de Tal", new Date, 1000, "982231482-00")
            val property = new Property("Ford Bustang", 150000.00, PropertyKind.VEHICLE)
            //val indebted = new Indebted("Gringo", new Date, 1000, "01111111111")
            val msg: RequestMessage = AddIndebtedRequest(indebted)
            //val msg: RequestMessage = AddPropertyRequest(indebted, property)
            out.writeObject(msg)
            out.flush()

            while (true) {
                val x = in.readObject().asInstanceOf[ReplyMessage]
                    println("Massa, valeu")
            }
            out.close()
            in.close()
            socket.close()
        } catch {
            case e: SocketException =>
                () // avoid stack trace when stopping a client with Ctrl-C
            case e: IOException =>
                ()
            //e.printStackTrace()
        }
    }

}

