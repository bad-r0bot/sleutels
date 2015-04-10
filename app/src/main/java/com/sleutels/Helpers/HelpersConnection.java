package com.sleutels.Helpers;

/**
 * Created by Jim
 * Thanks to Raphael.
 */

import android.content.Context;
import android.os.AsyncTask;

import com.sleutels.Models.ModelsSettings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class HelpersConnection extends AsyncTask<Void, Void, String>
{

    private String message;
    private String ip;
    private String reply = null;
    private Context context;
    private int port;


    ModelsSettings modelsSettingsData = ModelsSettings.getInstance();

    public HelpersConnection(Context context, String ip, int port, String message)
    {
        this.ip = ip;
        this.port = port;
        this.context = context;
        this.message = message;
    }

    private void sendMessage( String message, Socket serverSocket )
    {
        OutputStreamWriter outputStreamWriter = null;
        try
        {
            outputStreamWriter = new OutputStreamWriter(serverSocket.getOutputStream());
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }

        if( outputStreamWriter != null )
        {

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            PrintWriter writer = new PrintWriter( bufferedWriter, true );

            writer.println(message);
        }
    }

    @Override
    protected String doInBackground(Void... params)
    {
        try
        {
            Socket serverSocket = new Socket();
            serverSocket.connect( new InetSocketAddress( this.ip, this.port ), 4444 );

            this.sendMessage(message, serverSocket);

            InputStream input;
            try
            {
                input = serverSocket.getInputStream();
                BufferedReader replyStream = new BufferedReader(new InputStreamReader(input));

                StringBuilder stringBuilderItem = new StringBuilder();

                int i =0;

                String line;

                while ((line = replyStream.readLine()) != null)
                {

                    if(i <= 0){

                    }
                    else{
                        stringBuilderItem.append(line);
                    }
                    i++;
                }
                replyStream.close();

                this.reply = stringBuilderItem.toString();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        catch( UnknownHostException e )
        {
            modelsSettingsData.isConnected(false);
        }

        catch( SocketTimeoutException e )
        {
            modelsSettingsData.isConnected(false);
        }

        catch (IOException e)
        {
            e.printStackTrace();
            modelsSettingsData.isConnected(false);
        }
        return reply;
    }
}

