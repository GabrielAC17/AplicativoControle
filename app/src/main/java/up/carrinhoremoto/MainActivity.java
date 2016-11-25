package up.carrinhoremoto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button Button_up;
    private Button Button_down;
    private Button Button_left;
    private Button Button_right;
    private Button Button_connect;
    private conexao conexao;
    private TextView Mensagem;
    private TextView Texto;
    //private VideoView Video;
    private WebView webView;
    private EditText ipText;
    private String Ip;
    public boolean stconnect = true;

    private final static int PORTA = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button_up = (Button) findViewById(R.id.button_up);
        Button_down = (Button) findViewById(R.id.button_down);
        Button_left = (Button) findViewById(R.id.button_left);
        Button_right = (Button) findViewById(R.id.button_right);
        Button_connect = (Button) findViewById(R.id.button_connect);
        Mensagem = (TextView) findViewById(R.id.textView);
        Texto = (TextView) findViewById(R.id.textView2);
        //Video = (VideoView) findViewById(R.id.videoView);
        webView = (WebView) findViewById(R.id.webView);
        ipText = (EditText) findViewById(R.id.editText);


        Button_connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                configuraCliente();
                ScreenVisibility(stconnect);
                webView.loadUrl("http://"+ipText.getText().toString()+":8090/webcam.mjpeg");
                toastMensagem("http://"+ipText.getText().toString()+":8090/webcam.mjpeg");
            }
        });

        Button_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ButtonBlock(1);
                    conexao.enviaDados("1");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ButtonUnblock(1);
                    conexao.enviaDados("0");
                }
                return true;
            }
        });

        Button_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ButtonBlock(2);
                    conexao.enviaDados("2");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ButtonUnblock(2);
                    conexao.enviaDados("0");
                }
                return true;
            }
        });

        Button_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ButtonBlock(3);
                    conexao.enviaDados("3");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ButtonUnblock(3);
                    conexao.enviaDados("0");
                }
                return true;
            }
        });

        Button_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ButtonBlock(4);
                    conexao.enviaDados("4");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ButtonUnblock(4);
                    conexao.enviaDados("0");
                }
                return true;
            }
        });
        //setContentView(webView);
        //webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //Video.setVideoURI(Uri.parse("http://"+ipText.getText().toString()+":8090/webcam.webm"));
        //Video.requestFocus();


    }

    private void configuraCliente() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Ip = ipText.getText().toString();
                    Socket cliente = new Socket(Ip, PORTA);
                    conexao = new conexao(cliente, handlerAtualiza);
                    conexao.start();
                    toastMensagem("Conectado");
                } catch (Exception e) {
                    toastMensagem("Erro de conexão");
                }
            }
        }).start();
    }



    private Handler handlerAtualiza = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            String string = (String)msg.obj;
            Mensagem.setText(string);
        }
    };

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String string = (String) msg.obj;
            Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
        }
    };

    private void toastMensagem(final String mensagem){
        Message message = new Message();
        message.obj = mensagem;
        handler.sendMessage(message);
    }

    private void ScreenVisibility(boolean status){
        if(status){
            Button_connect.setVisibility(View.INVISIBLE);
            Texto.setVisibility(View.INVISIBLE);
            ipText.setVisibility(View.INVISIBLE);
            Button_up.setVisibility(View.VISIBLE);
            Button_down.setVisibility(View.VISIBLE);
            Button_left.setVisibility(View.VISIBLE);
            Button_right.setVisibility(View.VISIBLE);
            //Video.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            Mensagem.setVisibility(View.VISIBLE);
        }

        else{
            Button_connect.setVisibility(View.VISIBLE);
            Texto.setVisibility(View.VISIBLE);
            ipText.setVisibility(View.VISIBLE);
            Button_up.setVisibility(View.INVISIBLE);
            Button_down.setVisibility(View.INVISIBLE);
            Button_left.setVisibility(View.INVISIBLE);
            Button_right.setVisibility(View.INVISIBLE);
            //Video.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.INVISIBLE);
            Mensagem.setVisibility(View.INVISIBLE);
        }
    }

    private void ButtonBlock(int botao){
        switch(botao){
            case 1:
                Button_down.setEnabled(false);
                Button_left.setEnabled(false);
                Button_right.setEnabled(false);
                break;

            case 2:
                Button_up.setEnabled(false);
                Button_left.setEnabled(false);
                Button_right.setEnabled(false);
                break;

            case 3:
                Button_down.setEnabled(false);
                Button_up.setEnabled(false);
                Button_right.setEnabled(false);
                break;

            case 4:
                Button_down.setEnabled(false);
                Button_left.setEnabled(false);
                Button_up.setEnabled(false);
                break;
        }
    }

    private void ButtonUnblock(int botao){
        switch(botao){
            case 1:
                Button_down.setEnabled(true);
                Button_left.setEnabled(true);
                Button_right.setEnabled(true);
                break;

            case 2:
                Button_up.setEnabled(true);
                Button_left.setEnabled(true);
                Button_right.setEnabled(true);
                break;

            case 3:
                Button_down.setEnabled(true);
                Button_up.setEnabled(true);
                Button_right.setEnabled(true);
                break;

            case 4:
                Button_down.setEnabled(true);
                Button_left.setEnabled(true);
                Button_up.setEnabled(true);
                break;
        }
    }
}
