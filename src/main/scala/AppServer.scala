
import java.io._
import java.net.{ InetAddress, ServerSocket, Socket, SocketException }
import java.util.Random
import business.entities._
import business.services._
import database.Database

object AppServer {
    def main(args: Array[String]): Unit = {
        try {
            val listener = new ServerSocket(9999)
            while (true)
                new ServerThread(listener.accept()).start()
            listener.close()
        } catch {
            case e: IOException =>
                System.err.println("Could not listen on port: 9999.")
                System.exit(-1)
        }
    }

}

case class ServerThread(socket: Socket) extends Thread("ServerThread") {
    override def run(): Unit = {
        try {
            Database.initialize()
            val out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))
            val in = new ObjectInputStream(
                new DataInputStream(socket.getInputStream()))

            while (socket.isBound()) {
                val msg = in.readObject().asInstanceOf[RequestMessage]
                val r = msg match {
                    case LoginRequest(u, p) =>
                        println("Server: login request from user \"" + u + "\"")
                        val serv = new UserServices
                        val user: Option[User] = serv.getUser(u, p)
                        LoginReply(user)

                    case AddUserRequest(u) =>
                        println("Server: adding user")
                        val serv = new UserServices
                        serv.addUser(u) // TODO check if user was really added
                        AddUserReply("Success")

                    case AddIndebtedRequest(i) =>
                        println("Server: adding indebted")
                        val serv = new ManagerServices()
                        if (serv.insertIndebted(i))
                            AddIndebtedReply("Success")
                        else
                            AddIndebtedReply("Failed")

                    case AddPropertyRequest(i, p) =>
                        println("Server: adding property")
                        val serv = new ManagerServices()
                        if (serv.insertProperty(i, p))
                            AddPropertyReply("Success")
                        else
                            AddPropertyReply("Failed")

                    case AddAuctionRequest(p, b, e) =>
                        println("Server: adding auction")
                        val serv = new ManagerServices
                        if(serv.insertAuction(p, b, e))
                            AddPropertyReply("Success")
                        else
                            AddPropertyReply("Failed")

                    case QueryIndebtedPropertiesRequest(iCpf: String) =>
                        println("Server: querying indebted properties")
                        val serv = new ManagerServices
                        val pl = serv.getIndebtedProperties(iCpf)
                        QueryIndebtedPropertiesReply(pl)

                    case QueryIndebtedsRequest() =>
                        println("Server: querying indebted")
                        val serv = new ManagerServices()
                        val indebted = serv.getIndebteds
                        indebted.foreach {
                            x => out.writeObject(QueryIndebtedsReply(x))}
                        QueryIndebtedsReply(null) // Might be empty

                    case QueryPropertiesRequest(k, i) =>
                        println("Server: querying properties")
                        val serv = new ManagerServices
                        val pl = serv.getProperties(k,i)
                        QueryPropertiesReply(pl)

                    case QueryOpenedAuctionsRequest(pn, pk) =>
                        println("Server: querying open auctions")
                        val serv = new ManagerServices
                        val oa = serv.getOpenAuctions(pn, pk)
                        QueryOpenedAuctionsReply(oa)

                    case QueryClosedAuctionsRequest(pn, pk) =>
                        println("Server: querying open auctions")
                        val serv = new ManagerServices
                        val ca = serv.getClosedAuctions(pn, pk)
                        QueryClosedAuctionsReply(ca)

                    case QueryAuctionHistoryRequest(cl, pn, pk) =>
                        println("Server: querying open auctions")
                        val serv = new ClientServices
                        val ah = serv.queryAuctionHistory(cl, pn, pk)
                        QueryAuctionHistoryReply(ah)

                    case AddBidRequest(uid, aid, value) =>
                        println("Server: adding bid")
                        val serv = new ClientServices
                        if(serv.createBid(uid, aid, value))
                            AddBidReply("Success")
                        else
                            AddBidReply("Failed")

                    case CancelBidRequest(uid, aid, value) =>
                        println("Server: adding bid")
                        val serv = new ClientServices
                        if(serv.createBid(uid, aid, value))
                            CancelBidReply("Success")
                        else
                            CancelBidReply("Failed")

                    case _ => throw new SocketException
                    // TODO Create other exception

                }
                out.writeObject(r)
                out.flush()
            }

            out.close()
            in.close()
            socket.close()
        } catch {
            case e: SocketException =>
                () // avoid stack trace when stopping a client with Ctrl-C
            case e: IOException =>
                e.printStackTrace()
        }
    }
}
  
