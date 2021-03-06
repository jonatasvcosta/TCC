package politcc2017.tcc_app.Activities;

/**
 * Created by Jonatas on 13/08/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import politcc2017.tcc_app.Activities.Bookshelf.BookshelfActivity;
import politcc2017.tcc_app.Common.ResourcesHelper;
import politcc2017.tcc_app.Components.CustomButton;
import politcc2017.tcc_app.Components.CustomCard;
import politcc2017.tcc_app.Components.CustomSearchToolbar;
import politcc2017.tcc_app.Components.Helpers.SQLiteHelper.SqlHelper;
import politcc2017.tcc_app.Components.Helpers.SharedPreferencesHelper;
import politcc2017.tcc_app.Components.Listeners.CellClickListener;
import politcc2017.tcc_app.Components.Listeners.FragmentListener;
import politcc2017.tcc_app.Components.RecyclerView.Adapters.GenericAdapter;
import politcc2017.tcc_app.Components.RecyclerView.Data.GenericData;
import politcc2017.tcc_app.Components.RecyclerView.ViewHolders.ViewHolderType;
import politcc2017.tcc_app.R;
import politcc2017.tcc_app.Volley.ServerConstants;
import politcc2017.tcc_app.Volley.ServerRequestHelper;

import static politcc2017.tcc_app.Components.WordContextDialog.CONTEXT_ADD_TEXT;

public class NewsFragment extends Fragment{

    private CustomCard mNewsCard;
    private CustomSearchToolbar mSearchToolbar;
    private RecyclerView trendingRecyclerView, newsRecyclerView;
    private FloatingActionsMenu ratingMenu;
    private GenericAdapter mAdapter;
    private GenericData newsData;
    private FloatingActionButton addBookshelfFAB, rateGoodFAB, rateMediumFAB, rateBadFAB, fabRatedGood, fabRatedMedium, fabRatedBad;
    private FragmentListener listener;
    private long referenceTime;
    private CustomButton backBtn;

    public NewsFragment() {
        // Required empty public constructor
    }

    public NewsFragment(FragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_news, container, false);
        referenceTime = System.currentTimeMillis();
        mSearchToolbar = (CustomSearchToolbar) v.findViewById(R.id.news_activity_search_toolbar);
        trendingRecyclerView = (RecyclerView) v.findViewById(R.id.news_activity_trendingtopics_recyclerview);
        newsRecyclerView = (RecyclerView) v.findViewById(R.id.news_activity_news_recyclerview);
        ratingMenu = (FloatingActionsMenu) v.findViewById(R.id.news_activity_rating_floating_menu);
        addBookshelfFAB = (FloatingActionButton) v.findViewById(R.id.news_activity_add_bookshelf_btn);
        rateGoodFAB = (FloatingActionButton) v.findViewById(R.id.news_activity_rate_good_btn);
        rateMediumFAB = (FloatingActionButton) v.findViewById(R.id.news_activity_rate_neutral_btn);
        rateBadFAB = (FloatingActionButton) v.findViewById(R.id.news_activity_rate_bad_btn);
        fabRatedGood = (FloatingActionButton) v.findViewById(R.id.news_activity_rated_good);
        fabRatedMedium = (FloatingActionButton) v.findViewById(R.id.news_activity_rated_neutral);
        fabRatedBad = (FloatingActionButton) v.findViewById(R.id.news_activity_rated_bad);
        trendingRecyclerView.setVisibility(View.VISIBLE);
        trendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchToolbar.setAutoCompleteSearchBar();
        mSearchToolbar.setAdvancedFilter(getActivity(), ResourcesHelper.getStringArrayAsArrayList(getContext(), R.array.news_search_advanced_filter));

        setSuggestionList();
        mNewsCard = (CustomCard) v.findViewById(R.id.activity_news_cell);
        mNewsCard.setContentMarkable();
        mNewsCard.setUnlimitedLines();
        GenericData mData = new GenericData();
        loadTrendingTopics(mData);

        mAdapter = new GenericAdapter(mData, ViewHolderType.TRENDING_TOPICS_VIEW_HOLDER, getContext());

        setupListeners();
        trendingRecyclerView.setAdapter(mAdapter);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mSearchToolbar.registerRecyclerViewScrollListener(trendingRecyclerView, displayMetrics.heightPixels);
        backBtn = (CustomButton) v.findViewById(R.id.news_detail_return_btn);
        if(listener != null) listener.onMessageSent("NEWS_FRAGMENT", "READY");
        return v;
    }

    private long getTimePassed(){
        return System.currentTimeMillis() - referenceTime;
    }

    private void loadTrendingTopics(GenericData data){
        //call server here
        ArrayList<String> topics = new ArrayList<>();
        topics.add("Politics");
        topics.add("Refugees");
        topics.add("Global warming");
        topics.add("North Korea");
        topics.add("G5");
        data.addStringsToAllCells(GenericData.TRENDING_TOPIC, topics);
    }

    private void detailNewsCard(int position){
        String title = newsData.getValue(position).get(GenericData.CUSTOM_CARD_TITLE).toString();
        String content = newsData.getValue(position).get(GenericData.CUSTOM_CARD_CONTENT).toString();
        String categories = newsData.getValue(position).get(GenericData.CUSTOM_CARD_CATEGORIES).toString();
        String votes = newsData.getValue(position).get(GenericData.CUSTOM_CARD_VOTES).toString();
        mNewsCard.setTitle(title);
        mNewsCard.setContent(content);
        mNewsCard.setUnlimitedLines();
        mNewsCard.setCategory(categories);
        mNewsCard.setVotes(votes);
    }

    private void populateData(JSONArray response){
        if(response == null || response.length() == 0) return;
        newsData = new GenericData();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> texts = new ArrayList<>();
        ArrayList<String> cardType = new ArrayList<>();
        ArrayList<String> votes = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            try {
                JSONObject news = response.getJSONObject(i);
                String id = news.getString("id");
                String title = news.getString("title");
                titles.add(title);
                String text = news.getString("text");
                texts.add(text);
                votes.add("");
                categories.add("");
                cardType.add(GenericData.NEWS);
                String url = news.getString("url");
            } catch (JSONException e) {}
        }
        newsData.addStringsToAllCells(GenericData.CUSTOM_CARD_TITLE, titles);
        newsData.addStringsToAllCells(GenericData.CUSTOM_CARD_CONTENT, texts);
        newsData.addStringsToAllCells(GenericData.CUSTOM_CARD_TYPE, cardType);
        newsData.addStringsToAllCells(GenericData.CUSTOM_CARD_CATEGORIES, categories);
        newsData.addStringsToAllCells(GenericData.CUSTOM_CARD_VOTES, votes);
        final GenericAdapter newsAdapter = new GenericAdapter(newsData, ViewHolderType.HOME_CARD_VIEW_HOLDER, getContext());
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter.RegisterClickListener(new CellClickListener() {
            @Override
            public void onClick(View v, int position) {

            }

            @Override
            public void onClick(ImageView v, String link) {

            }

            @Override
            public void onClick(String message, int position) {
                detailNewsCard(position);
                mNewsCard.setVisibility(View.VISIBLE);
                newsRecyclerView.setVisibility(View.GONE);
                ratingMenu.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.VISIBLE);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ratingMenu.setVisibility(View.GONE);
                        backBtn.setVisibility(View.GONE);
                        mNewsCard.setVisibility(View.GONE);
                        newsRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onLinkClick(String link) {

            }

            @Override
            public void onClick(View view) {

            }
        });
        newsRecyclerView.setVisibility(View.VISIBLE);
        mNewsCard.setVisibility(View.GONE);
        trendingRecyclerView.setVisibility(View.GONE);
        ratingMenu.setVisibility(View.GONE);

    }

    private void loadNews(String search){
        search = search.replace(" ","");
        search = search.replace("\n","");
        String params = "?query="+search+"&language="+ SharedPreferencesHelper.getString(SharedPreferencesHelper.LEARNING_LANGUAGE_LOCALE);
        ServerRequestHelper.getAuthorizedJSONArrayRequest(getContext(), ServerConstants.NEWS_SEARCH_ENDPOINT+params, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                populateData(response);
            }
        });
        mNewsCard.setVisibility(View.GONE);
        trendingRecyclerView.setVisibility(View.GONE);
        ratingMenu.setVisibility(View.VISIBLE);
    }

    public void switchRecyclerView(){
        mNewsCard.setVisibility(View.GONE);
        trendingRecyclerView.setVisibility(View.VISIBLE);
        ratingMenu.setVisibility(View.GONE);
    }

    public void setupSpeechInput(String input){
        mSearchToolbar.setSuggestionText(input);
    }

    private void setupListeners(){
        addBookshelfFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) listener.onMessageSent("NEWS_FRAGMENT", SqlHelper.RULE_ADD_TEXT_BOOKSHELF);
                Intent intent = new Intent(getContext(), BookshelfActivity.class);
                intent.putExtra(CONTEXT_ADD_TEXT, mNewsCard.getContent());
                getContext().startActivity(intent);
            }
        });
        rateGoodFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Good! - " +(getTimePassed()/1000)+ " s", Toast.LENGTH_SHORT).show();
                if(listener != null) listener.onMessageSent("NEWS_FRAGMENT", SqlHelper.RULE_RATE_TEXT);
                fabRatedGood.setVisibility(View.VISIBLE);
                ratingMenu.setVisibility(View.GONE);
            }
        });
        rateMediumFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Medium! - " +(getTimePassed()/1000)+ " s", Toast.LENGTH_SHORT).show();
                if(listener != null) listener.onMessageSent("NEWS_FRAGMENT", SqlHelper.RULE_RATE_TEXT);
                fabRatedMedium.setVisibility(View.VISIBLE);
                ratingMenu.setVisibility(View.GONE);
            }
        });
        rateBadFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Bad! - "+(getTimePassed()/1000)+ " s", Toast.LENGTH_SHORT).show();
                if(listener != null) listener.onMessageSent("NEWS_FRAGMENT", SqlHelper.RULE_RATE_TEXT);
                fabRatedBad.setVisibility(View.VISIBLE);
                ratingMenu.setVisibility(View.GONE);
            }
        });
        mSearchToolbar.registerMicrophoneListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) listener.onMessageSent("NEWS_FRAGMENT", "PROMPT_SPEECH");
            }
        });
        mSearchToolbar.registerSearchListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNews(mSearchToolbar.getSuggestionText());
            }
        });

        mAdapter.RegisterClickListener(new CellClickListener() {
            @Override
            public void onClick(View v, int position) {

            }

            @Override
            public void onClick(ImageView v, String link) {

            }

            @Override
            public void onClick(String message, int position) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                mSearchToolbar.setSuggestionText(message);
                loadNews(message);
            }

            @Override
            public void onLinkClick(String link) {

            }

            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setNewsText(String text){
        mNewsCard.setVisibility(View.VISIBLE);
        trendingRecyclerView.setVisibility(View.GONE);
        ratingMenu.setVisibility(View.VISIBLE);
        mNewsCard.setContent(text);
    }

    private void setSuggestionList(){
        //load suggestions from server, based on user learning language

        mSearchToolbar.setAutoCompleteSuggestionList(new String[] {
                "turismo", "viagens", "economia", "mercado financeiro", "finanças", "esportes", "lazer",
                "informática", "tecnologia", "TI", "empreendedorismo", "inovação", "ciência", "filosofia"});
    }
}
