package com.vladimirpandurov.serverManagerApp2.service;

import com.vladimirpandurov.serverManagerApp2.domain.Server;
import com.vladimirpandurov.serverManagerApp2.enumeration.Status;
import com.vladimirpandurov.serverManagerApp2.repository.ServerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServerService {

    private final ServerRepository serverRepository;

    public Server create(Server server){
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    public Server ping(String ipAddress) throws IOException {
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000)? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    public Collection<Server> list(int limit){
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    public Server get(Long id){
        return serverRepository.findById(id).get();
    }

    public Server update(Server server){
        return serverRepository.save(server);
    }

    public Boolean delete(Long id){
        serverRepository.deleteById(id);
        return Boolean.TRUE;
    }

    private String setServerImageUrl(){
        String[] imageNames = { "server1.png", "server2.png", "server3.png", "server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }

    private boolean isReachable(String ipAddress, int port, int timeOut){
        try{
            try(Socket socket = new Socket()){
                socket.connect(new InetSocketAddress(ipAddress, port), timeOut);
            }
            return true;
        }catch (IOException exception){
            return false;
        }
    }

}
