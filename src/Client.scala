import java.io._
import java.net.{InetAddress,ServerSocket,Socket,SocketException}
import java.util.Random

object Client {

    def main(args: Array[String]) {
        try {
            val ia = InetAddress.getByName("localhost")
                val socket = new Socket(ia, 9999)
                val out = new ObjectOutputStream(
                        new DataOutputStream(socket.getOutputStream()))
                val in = new ObjectInputStream(new DataInputStream(socket.getInputStream()))

                val str : String = "Endividado Fulano de Tal"
                out.writeObject(str)
                out.flush()

                while (true) {
                    val x = in.readObject().asInstanceOf[String]
                        println("Massa, valeu")
                }
            out.close()
                in.close()
                socket.close()
        }
        catch {
            case e: SocketException =>
                    () // avoid stack trace when stopping a client with Ctrl-C
            case e: IOException =>
                    ()
                    //e.printStackTrace()
        }
    }

}

