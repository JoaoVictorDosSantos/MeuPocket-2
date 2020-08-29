package br.pro.ednilsonrossi.meupocket.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.pro.ednilsonrossi.meupocket.R;
import br.pro.ednilsonrossi.meupocket.dao.SiteDao;
import br.pro.ednilsonrossi.meupocket.model.Site;

public class CadastroSiteActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextTitulo;
    private EditText editTextEndereco;
    private CheckBox checkBoxFavorito;
    private Button buttonSalvar;
    private Site mSite;
    private List<Site> siteList = null;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_site);

        editTextTitulo = findViewById(R.id.edittext_titulo);
        editTextEndereco = findViewById(R.id.edittext_endereco);
        checkBoxFavorito = findViewById(R.id.checkbox_favorito);
        buttonSalvar = findViewById(R.id.button_salvar);

        buttonSalvar.setOnClickListener(this);

        mSharedPreferences = this.getSharedPreferences(getString(R.string.file_site ), MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSalvar){
            cadastrarSite();
        }
    }

    private void cadastrarSite() {
        if(validaCadastro()){
            adicionarSite();
            finish();
        }
    }

    private Boolean validaCadastro(){
        String titulo = editTextTitulo.getText().toString();
        String endereco = editTextEndereco.getText().toString();
        if (titulo.isEmpty() || endereco.isEmpty()){
            Toast.makeText(this, R.string.erro_cadastro_msg, Toast.LENGTH_SHORT).show();
            return false;
        }else{
            mSite = new Site(titulo, endereco);
            if(checkBoxFavorito.isChecked()){
                mSite.doFavotite();
            }else{
                mSite.undoFavorite();
            }
            return true;
        }
    }

    private void adicionarSite(){
        SiteDao.inserir(getApplicationContext(), mSite);
    }
}
