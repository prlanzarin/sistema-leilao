package presentation.controller

import java.io._
import java.net.{ InetAddress, ServerSocket, Socket, SocketException }
import business.entities._

object Connection {
    var ia: InetAddress = null
    var socket: Socket = null
    var in: ObjectInputStream = null
    var out: ObjectOutputStream = null

    @throws(classOf[IOException])
    @throws(classOf[SocketException])
    def init = {
        ia = InetAddress.getByName("localhost")
        socket = new Socket(ia, 9999)
        out = new ObjectOutputStream(
            new DataOutputStream(socket.getOutputStream()))
        in = new ObjectInputStream(new DataInputStream(socket.getInputStream()))
    }

    def sendAddIndebtedRequest(indebted: Indebted) {
        val msg: RequestMessage = AddIndebtedRequest(indebted)
        out.writeObject(msg)
        out.flush()
        while (true) {
            val x = in.readObject().asInstanceOf[String]
            println("Massa, valeu")
        }
    }

    def sendAddPropertyRequest(indebted: Indebted, property: Property) {
        //val msg: RequestMessage = AddIndebtedRequest(indebted)
        //out.writeObject(msg)
        out.flush()
        while (true) {
            val x = in.readObject().asInstanceOf[String]
            println("Massa, valeu")
        }
    }
}