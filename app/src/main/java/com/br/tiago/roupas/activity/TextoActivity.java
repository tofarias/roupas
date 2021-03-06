package com.br.tiago.roupas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.br.tiago.roupas.R;

public class TextoActivity extends AppCompatActivity {

    private TextView textViewTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String texto = getIntent().getStringExtra("txt");

        this.textViewTexto = (TextView) findViewById(R.id.textViewTexto);
        this.textViewTexto.setText(texto);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if( item.getItemId() == android.R.id.home ){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
