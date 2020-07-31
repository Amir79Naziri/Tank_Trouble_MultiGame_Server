package Server;

import Game.GameFrameMulti;
import Game.GameLoopMulti;
import Game.GameLoopMulti;
import Game.GameStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{
    private Socket connectionSocket;
    private ArrayList<Character> data = new ArrayList<>();
    private GameLoopMulti game;

    public ArrayList<Character> getData()
    {
        return data;
    }
    public ObjectOutputStream outputStream = null;
    public InputStream inputStream = null;

    public ClientHandler(Socket connectionSocket , GameLoopMulti game)
    {
        this.connectionSocket = connectionSocket;
        try
        {
            inputStream = connectionSocket.getInputStream();
            outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        this.game = game;
    }

    @Override
    public void run()
    {
        try
        {
            byte[] buff = new byte[6];
            int l = inputStream.read(buff);

            String temp = new String(buff,0,l);

            data.clear();
            data.add(temp.charAt(0));
            data.add(temp.charAt(1));
            data.add(temp.charAt(2));
            data.add(temp.charAt(3));
            data.add(temp.charAt(4));

            GameStatus status = game.getState().getStatus();
            System.out.println("In sending:" + status.getTanks().get(0).getDegree());
            outputStream.reset();
            outputStream.writeObject(status);

        }
        catch (IllegalArgumentException | IOException e)
        {
            e.printStackTrace();
        }
    }


}
