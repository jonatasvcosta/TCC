package politcc2017.tcc_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

import politcc2017.tcc_app.Activities.BeAPro.BeAProClassDetailActivity;
import politcc2017.tcc_app.Components.Listeners.CellClickListener;
import politcc2017.tcc_app.Components.RecyclerView.Adapters.GenericAdapter;
import politcc2017.tcc_app.Components.RecyclerView.Data.GenericData;
import politcc2017.tcc_app.Components.RecyclerView.ViewHolders.ViewHolderType;
import politcc2017.tcc_app.R;

import static java.security.AccessController.getContext;

/**
 * Created by Jonatas on 06/08/2017.
 */

public class HomeActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private GenericData data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home);

        mRecyclerView = (RecyclerView) findViewById(R.id.home_cards_list);
        PopulateRecyclerView();
        setActivityTitle(getResString(R.string.app_name));
    }

    private void PopulateRecyclerView(){
        GenericData data = getDataFromServer();
        GenericAdapter mAdapter = new GenericAdapter(data, ViewHolderType.HOME_CARD_VIEW_HOLDER, getApplicationContext());
        mAdapter.RegisterClickListener(new CellClickListener() {
            @Override
            public void onClick(View v, int position) {

            }

            @Override
            public void onClick(ImageView v, String link) {

            }

            @Override
            public void onClick(String message, int position) {
                HandleCellClicks(message, position);
            }

            @Override
            public void onLinkClick(String link) {

            }

            @Override
            public void onClick(View view) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void HandleCellClicks(String message, int position){
        if(message.equals("cardlayout")){
            if(data.getValue(position).get(GenericData.CUSTOM_CARD_TYPE).toString().equals(GenericData.MINI_CLASS)) startClassDetailActivity(position);
            if(data.getValue(position).get(GenericData.CUSTOM_CARD_TYPE).toString().equals(GenericData.LINK)) startClassNavigation(position);
            if(data.getValue(position).get(GenericData.CUSTOM_CARD_TYPE).toString().equals(GenericData.TRENDING_WORDS)) startClassVocabulary(position);
        }
    }

    private void startClassVocabulary(int position){
        Intent i = new Intent(getApplicationContext(), VocabularyActivity.class);
        Hashtable content = data.getValue(position);
        if(content.containsKey(GenericData.CUSTOM_CARD_CONTENT)) i.putExtra(GenericData.TRENDING_WORDS, content.get(GenericData.CUSTOM_CARD_CONTENT).toString());
        startActivity(i);
    }

    private void startClassNavigation(int position){
        Intent i = new Intent(getApplicationContext(), NavigateActivity.class);
        Hashtable content = data.getValue(position);
        if(content.containsKey(GenericData.CUSTOM_CARD_CONTENT)) i.putExtra(GenericData.LINK, content.get(GenericData.CUSTOM_CARD_CONTENT).toString().replace("<link>", "").replace("</link>", ""));
        startActivity(i);
    }

    private void startClassDetailActivity(int position){
        Intent i = new Intent(getApplicationContext(), BeAProClassDetailActivity.class);
        Hashtable content = data.getValue(position);
        if(content.containsKey(GenericData.CUSTOM_CARD_TITLE)) i.putExtra(GenericData.CUSTOM_CARD_TITLE, content.get(GenericData.CUSTOM_CARD_TITLE).toString());
        if(content.containsKey(GenericData.CUSTOM_CARD_CATEGORIES)) i.putExtra(GenericData.CUSTOM_CARD_CATEGORIES, content.get(GenericData.CUSTOM_CARD_CATEGORIES).toString());
        if(content.containsKey(GenericData.CUSTOM_CARD_CONTENT)) i.putExtra(GenericData.CUSTOM_CARD_CONTENT, content.get(GenericData.CUSTOM_CARD_CONTENT).toString());
        if(content.containsKey(GenericData.CUSTOM_CARD_VOTES)) i.putExtra(GenericData.CUSTOM_CARD_VOTES, content.get(GenericData.CUSTOM_CARD_VOTES).toString());
        if(content.containsKey(GenericData.CUSTOM_CARD_URL)) i.putExtra(GenericData.CUSTOM_CARD_URL, content.get(GenericData.CUSTOM_CARD_URL).toString());
        startActivity(i);
    }

    private GenericData getDataFromServer(){
        data = new GenericData(); //replace with proper call to server
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Trump to North Korea: Be very, very nervous");
        titles.add("Check out movies news!");
        titles.add("Expressões Linguísticas");
        titles.add("Trending words");
        ArrayList<String> descriptions = new ArrayList<>();
        descriptions.add("<b> President Donald Trump has warned North Korea it should be \"very, very nervous\" if it does anything to the US</b>.\n" +
                "<p>He said the regime would be in trouble \"like few nations have ever been\" if they do not \"get their act together</p>\n" +
                "<p>His comments came after Pyongyang announced it had a plan to fire four missiles near the US territory of Guam.</p");
        descriptions.add("<link>http://www.imdb.com/news/movie?ref_=nv_nw_mv_2</link>");
        descriptions.add("<blockquote>Express&otilde;es&nbsp;Lingu&iacute;sticas:</blockquote></p><ul><li>Enfiar o p&eacute; na jaca - exagerar</li></ul><ul><li>Botar pra quebrar&nbsp;- exagerar</li></ul><ul><li>Badernar&nbsp;- fazer bagun&ccedil;a</li></ul><ul><li>Encher a cara - embriagar-se</li></ul><ul><li>Ir para o&nbsp;olho da rua - ser demitido</li></ul><ul><li>Enfiar o p&eacute; na jaca - exagerar</li></ul><ul><li>Botar pra quebrar&nbsp;- exagerar</li></ul><ul><li>Badernar&nbsp;- fazer bagun&ccedil;a</li></ul><ul><li>Encher a cara - embriagar-se</li></ul><ul><li>Ir para o&nbsp;olho da rua - ser demitido</li></ul><ul><li>Enfiar o p&eacute; na jaca - exagerar</li></ul><ul><li>Botar pra quebrar&nbsp;- exagerar</li></ul><ul><li>Badernar&nbsp;- fazer bagun&ccedil;a</li></ul><ul><li>Encher a cara - embriagar-se</li></ul><ul><li>Ir para o&nbsp;olho da rua - ser demitido</li></ul><ul><li>Enfiar o p&eacute; na jaca - exagerar</li></ul><ul><li>Botar pra quebrar&nbsp;- exagerar</li></ul><ul><li>Badernar&nbsp;- fazer bagun&ccedil;a</li></ul><ul><li>Encher a cara - embriagar-se</li></ul><ul><li>Ir para o&nbsp;olho da rua - ser demitido</li></ul><ul><li>Enfiar o p&eacute; na jaca - exagerar</li></ul><ul><li>Botar pra quebrar&nbsp;- exagerar</li></ul><ul><li>Badernar&nbsp;- fazer bagun&ccedil;a</li></ul><ul><li>Encher a cara - embriagar-se</li></ul><ul><li>Ir para o&nbsp;olho da rua - ser demitido</li></ul><ul><li>Enfiar o p&eacute; na jaca - exagerar</li></ul><ul><li>Botar pra quebrar&nbsp;- exagerar</li></ul><ul><li>Badernar&nbsp;- fazer bagun&ccedil;a</li></ul><ul><li>Encher a cara - embriagar-se</li></ul><ul><li>Ir para o&nbsp;olho da rua - ser demitido</li></ul><ul><li>Enfiar o p&eacute; na jaca - exagerar</li></ul><ul><li>Botar pra quebrar&nbsp;- exagerar</li></ul><ul><li>Badernar&nbsp;- fazer bagun&ccedil;a</li></ul><ul><li>Encher a cara - embriagar-se</li></ul><ul><li>Ir para o&nbsp;olho da rua - ser demitido</li></ul><ul><li>Enfiar o p&eacute; na jaca - exagerar</li></ul><ul><li>Botar pra quebrar&nbsp;- exagerar</li></ul><ul><li>Badernar&nbsp;- fazer bagun&ccedil;a</li></ul><ul><li>Encher a cara - embriagar-se</li></ul><ul><li>Ir para o&nbsp;olho da rua - ser demitido</li></ul>");
        descriptions.add("<li>Política</li><li>Conflitos</li><li>Bolsa de valores</li><li>Juros</li><li>Lava-jato</li><li>Corrupção</li>");

        ArrayList<String> categories = new ArrayList<>();
        categories.add("conflito, política");
        categories.add("movies");
        categories.add("expressões, linguagem popular");
        categories.add("trending words");

        ArrayList<String> votes = new ArrayList<>();
        votes.add("#15211");
        votes.add("#12");
        votes.add("#1221");

        ArrayList<String> cardType = new ArrayList<>();
        cardType.add(GenericData.NEWS);
        cardType.add(GenericData.LINK);
        cardType.add(GenericData.MINI_CLASS);
        cardType.add(GenericData.TRENDING_WORDS);

        data.addStringsToAllCells(GenericData.CUSTOM_CARD_TITLE, titles);
        data.addStringsToAllCells(GenericData.CUSTOM_CARD_CONTENT, descriptions);
        data.addStringsToAllCells(GenericData.CUSTOM_CARD_CATEGORIES, categories);
        data.addStringsToAllCells(GenericData.CUSTOM_CARD_VOTES, votes);
        data.addStringsToAllCells(GenericData.CUSTOM_CARD_TYPE, cardType);

        return data;
    }
}
