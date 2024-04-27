package swimmingcompetition.networking.utils;


import swimmingcompetition.networking.json.SwimmingCompetitionClientJsonWorker;
import swimmingcompetition.networking.protobuf.ProtoWorker;
import swimmingcompetition.services.ISwimmingCompetitionServices;

import java.net.Socket;

public class SwimmingCompetitionJsonConcurrentServer extends AbsConcurrentServer{
    private ISwimmingCompetitionServices swimmingCompetitionserver;
    public SwimmingCompetitionJsonConcurrentServer(int port, ISwimmingCompetitionServices swimmingCompetitionserver) {
        super(port);
        this.swimmingCompetitionserver = swimmingCompetitionserver;
        System.out.println("SwimmingCompetition- SwimmingCompetitionJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoWorker worker=new ProtoWorker(swimmingCompetitionserver, client);

        Thread tw=new Thread(worker);
        return tw;
    }
}
