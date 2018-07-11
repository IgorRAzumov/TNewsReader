package something.ru.newsreader.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import something.ru.newsreader.R;
import something.ru.newsreader.presenter.INewsListPresenter;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder> {
    private final INewsListPresenter presenter;
    private final View.OnClickListener newsClickListener;

    public NewsListAdapter(INewsListPresenter presenter) {
        this.presenter = presenter;
        this.newsClickListener = createNewsClickListener();
    }

    @NonNull
    @Override
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        view.setOnClickListener(newsClickListener);
        return new NewsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListViewHolder holder, int position) {
        holder.itemView.setTag(position);
        presenter.bindNewsListRow(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        presenter.onViewAttach();
    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        presenter.onViewDetached();

    }

    private View.OnClickListener createNewsClickListener() {
        return view -> {
            presenter.onNewsClick((int) view.getTag());
        };
    }

    public class NewsListViewHolder extends RecyclerView.ViewHolder implements INewsRowView {
        @BindView(R.id.tv_news_list_item_news)
        TextView newsText;
        @BindView(R.id.tv_news_list_item_date)
        TextView nesDateText;

        NewsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setNewsText(String news) {
            newsText.setText(news);
        }

        @Override
        public void setPublishDate(String pubDate) {
            nesDateText.setText(pubDate);
        }

    }
}
