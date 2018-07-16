package something.ru.newsreader.view;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import something.ru.newsreader.R;

public class UiUtils {
    public static Snackbar createSnackbar(int duration, String message, String action, View view,
                                          View.OnClickListener actionListener) {
        Resources resources = view.getContext().getResources();
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setAction(action, actionListener);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(resources.getColor(R.color.orange_600));
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(resources.getInteger(R.integer.snackbar_max_lines));
        return snackbar;
    }
}
