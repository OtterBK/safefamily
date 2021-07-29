package hanium.oldercare.oldercareservice.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

public class NumberFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if(source.toString().matches("^[0-9]+$")){
            return source;
        } else {
            return "";
        }
    }
}
