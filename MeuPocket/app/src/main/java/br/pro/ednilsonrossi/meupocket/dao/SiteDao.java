package br.pro.ednilsonrossi.meupocket.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.pro.ednilsonrossi.meupocket.R;
import br.pro.ednilsonrossi.meupocket.model.Site;

public class SiteDao {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static final String TAG = "SITE-DAO";

    public static final List<Site> buscaTodos(Context context) {
        sharedPreferences  = context.getSharedPreferences(context.getString(R.string.key_sites_db), context.MODE_PRIVATE);
        String sites = sharedPreferences.getString(context.getString(R.string.key_sites_db), "");
        JSONObject jsonObject;
        JSONArray jsonArray;
        List<Site> siteList = new ArrayList<>();
        try{
            jsonArray = new JSONArray(sites);
            for(int i=0; i<jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                Site  mSite = new Site(jsonObject.getString("titulo"),
                        jsonObject.getString("endereco"),
                        jsonObject.getBoolean("favorito"));
                System.out.println("Nome: " + jsonObject.getString("titulo"));
                siteList.add(mSite);
            }
        } catch (JSONException ex){
            siteList.clear();
            Log.e(TAG,"Erro ao recuperar sites");
        }
        return siteList;
    }

    public static final void inserir(Context context, Site site) {
        List<Site> siteList = buscaTodos(context);
        siteList.add(site);
        inserirLista(context, siteList);
    }

    public static void update(Context context, Site site) {
        List<Site> siteList = buscaTodos(context);
        for (Site s : siteList) {
            if(s.equals(site)) {
                if(site.isFavorito()){
                    s.doFavotite();
                }else{
                    s.undoFavorite();
                }
            }
        }
        inserirLista(context, siteList);
    }

    private static void inserirLista(Context context, List<Site> siteList){
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.key_sites_db), context.MODE_PRIVATE);
        mEditor = sharedPreferences.edit();
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();

        for(Site s : siteList){
            jsonObject = new JSONObject();
            try {
                jsonObject.put("titulo", s.getTitulo());
                jsonObject.put("endereco", s.getEndereco());
                jsonObject.put("favorito", s.isFavorito());
                jsonArray.put(jsonObject);
            }catch (JSONException e){
                Log.e(TAG,context.getString(R.string.erro_recupera_lista));
            }
        }
        String sites = jsonArray.toString();
        mEditor.putString(context.getString(R.string.key_sites_db), sites);
        mEditor.commit();
    }

}