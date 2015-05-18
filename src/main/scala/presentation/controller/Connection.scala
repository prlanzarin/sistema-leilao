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
    def init {
        ia = InetAddress.getByName("localhost")
        socket = new Socket(ia, 9999)
        out = new ObjectOutputStream(
            new DataOutputStream(socket.getOutputStream()))
        in = new ObjectInputStream(new DataInputStream(socket.getInputStream()))
    }

    def end {
        socket.close()
        in.close()
        out.close()
    }

    def sendAddIndebtedRequest(indebted: Indebted) {
        val msg: RequestMessage = AddIndebtedRequest(indebted)
        out.writeObject(msg)
        out.flush()
        var r = in.readObject().asInstanceOf[ReplyMessage]
        r match {
            case AddIndebtedReply(str) => println("sucesso")
            case _ => ;
        }
    }

    def sendAddPropertyRequest(indebted: Indebted, property: Property) {
        val msg: RequestMessage = AddPropertyRequest(indebted, property)
        out.writeObject(msg)
        out.flush()
        var r = in.readObject().asInstanceOf[ReplyMessage]
        r match {
            case AddPropertyReply(str) => println("sucesso")
            case _ => ; // ignore wrong typed message
        }
    }

    def sendQueryIndebtedsRequest(): List[Indebted] = {
        val msg: RequestMessage = QueryIndebtedsRequest()
        out.writeObject(msg)
        out.flush()
        var r = in.readObject().asInstanceOf[ReplyMessage]
        r match {
            case QueryIndebtedsReply(loi) => loi
            case _ => List() // ignore wrong typed message
        }
    }
}
