package up.carrinhoremoto;

import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by aluno on 19/07/2016.
 */
public class conexao extends Thread{
    private Socket socket;
    private boolean conectado;
    private Handler handler;

    public conexao (Socket socket, Handler handler){
        this.socket = socket;
        this.handler = handler;
        conectado = true;
    }

    @Override
    public void run(){
        super.run();
        try{
            InputStream inputStream = socket.getInputStream();
            while(conectado){
                if(inputStream.available() > 0){
                    byte[] bytes = new byte[1024];
                    inputStream.read(bytes);
                    String mensagem = new String(bytes);

                    Message message = new Message();
                    message.obj = mensagem;
                    handler.sendMessage(message);
                }
                Thread.sleep(50);
            }
        }catch (Exception e){

        }
    }

    public boolean enviaDados(String mensagem){
        try{
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(mensagem.getBytes());
            outputStream.flush();
            return true;
        }catch (Exception e){
            conectado = false;
            return false;
        }
    }

    public void setConectado(boolean conectado){
        this.conectado = conectado;
    }
}
