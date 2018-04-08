package com.example.portillo.appnetvf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.util.Patterns;

public class MainActivity extends AppCompatActivity {
    EditText ip, bit;
    TextView idip, broad, host, pared, parost;
    Button procesar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciar();
        procesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    calcular();
                }
            }
        });
    }
    private boolean validate(){
        String ip1 = ip.getText().toString();
        String mask = bit.getText().toString();
        if (ip1.isEmpty() || !Patterns.IP_ADDRESS.matcher(ip1).matches()) {
            ip.setError("IP inválida");
            return false;
        }else if(mask.isEmpty()|| !mask.matches("(3[0-2]|2[0-9]|1[0-9]|0[1-9]|[1-9])")) {
            bit.setError("Máscara inválida");
            return false;
        } else {
            ip.setError(null);
            bit.setError(null);
            return true;
        }
    }
    public void calcular(){
        String[] ip_separada = ip.getText().toString().split("\\.");

        long up =0;
        long maskup = 0;
        long maskinvr = 0;
        int mask = Integer.parseInt(bit.getText().toString());

        if(ip_separada.length != 4) return;

        for(int i=3; i>=0; i--) {
            up |= (Long.parseLong(ip_separada[3-i])) << (i*8);
        }

        for(int i=1; i <= mask; i++) {
            maskup |= 1L << (32-i);
        }
        maskinvr = ~maskup;

        idip.setText(longToIP(up & maskup));
        broad.setText(longToIP(up | maskinvr));

        host.setText((int)(Math.pow(2, (double)(32-mask))) -2 + "");
        pared.setText(mask + "");
        parost.setText((32-mask) + "");

    }

    public String longToIP(long ip){
        String sepa="";

        for(int i=3; i>=0; i--) {
            sepa += (0b1111_1111 & (ip >> (i*8) )) + (i!=0? ".": "");
        }

        return sepa;
    }
    private void iniciar(){
        ip = findViewById(R.id.ipprincipal);
        bit = findViewById(R.id.mascarared);
        idip = findViewById(R.id.text_idred);
        broad = findViewById(R.id.text_broadcast);
        host = findViewById(R.id.text_host);
        pared = findViewById(R.id.text_pared);
        parost = findViewById(R.id.text_parost);
        procesar = findViewById(R.id.proceso);
    }
}
