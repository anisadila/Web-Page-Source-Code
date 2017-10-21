package com.example.adila.webpagesourcecode;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    Spinner spinner;
    String url;
    EditText website;
    TextView result;
    ProgressBar pr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        website = (EditText)findViewById(R.id.input_url);
        result = (TextView)findViewById(R.id.hasil);
        spinner = (Spinner)findViewById(R.id.spinner);
        pr = (ProgressBar)findViewById(R.id.progbar);

        pr.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jenis_sourcecode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


    public void action(View view) {
        url = spinner.getSelectedItem()+website.getText().toString();
        boolean valid = Patterns.WEB_URL.matcher(url).matches();

        if(valid){
            getSupportLoaderManager().restartLoader(0,null,this);
            pr.setVisibility(View.VISIBLE);
            result.setVisibility(View.GONE);
        }
        else {
            Loader loader = getSupportLoaderManager().getLoader(0);
            if (loader!=null){
                loader.cancelLoad();
            }
            result.setText("unvalid URL");
            pr.setVisibility(View.GONE);
            result.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args){
        return new AsyncTaskWebPage(this, url);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data){
        result.setText(data);
        pr.setVisibility(View.GONE);
        result.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader){

    }
}
