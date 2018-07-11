package something.ru.newsreader.presenter;

import something.ru.newsreader.view.adapters.INewsRowView;

public interface INewsListPresenter {
    void bindNewsListRow(int position, INewsRowView rowView);

    void onNewsClick(int position);

    void onViewAttach();

    void onViewDetached();

    int getItemCount();


}
