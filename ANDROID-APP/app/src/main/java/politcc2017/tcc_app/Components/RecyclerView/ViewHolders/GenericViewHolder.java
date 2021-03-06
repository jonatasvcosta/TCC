package politcc2017.tcc_app.Components.RecyclerView.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Hashtable;

import politcc2017.tcc_app.Components.Listeners.CellClickListener;
import politcc2017.tcc_app.R;

/**
 * Created by Jonatas on 26/10/2016.
 */

public abstract class GenericViewHolder extends RecyclerView.ViewHolder {
    protected CellClickListener mClickListener;

    public GenericViewHolder(View itemView, CellClickListener listener) {
        super(itemView);
        this.mClickListener = listener;
    }

    public static int getLayoutViewByPosition(int position, ViewHolderType type){
        if(type == ViewHolderType.DRAWER_VIEW_HOLDER) return R.layout.drawer_item_cell;
        if(type == ViewHolderType.BROWSER_SUGGESTION_ITEM_VIEW_HOLDER) return R.layout.browser_activity_suggestion_cell;
        if(type == ViewHolderType.BOOKSHELF_WORD_VIEW_HOLDER) return R.layout.bookshelf_word_cell;
        if(type == ViewHolderType.RANKING_VIEW_HOLDER) return R.layout.user_ranking_cell;
        if(type == ViewHolderType.VOCABULARY_WORD_VIEW_HOLDER) return R.layout.vocabulary_word_cell;
        if(type == ViewHolderType.CUSTOM_CARD_VIEW_HOLDER || type == ViewHolderType.HOME_CARD_VIEW_HOLDER) return R.layout.custom_card_item_cell;
        if(type == ViewHolderType.DICTIONARY_CELL_VIEW_HOLDER) return R.layout.dictionary_cell;
        if(type == ViewHolderType.TRENDING_TOPICS_VIEW_HOLDER) return R.layout.trending_topic_cell;
        return -1;
    }
    public abstract void setViewContent(Hashtable cellData);
}
