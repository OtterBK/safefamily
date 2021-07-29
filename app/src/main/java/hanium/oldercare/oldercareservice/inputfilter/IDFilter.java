package hanium.oldercare.oldercareservice.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import java.util.regex.Pattern;

public class IDFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if(source.toString().matches("^[a-zA-Z0-9]+$")){
            return source;
        } else {
            return "";
        }
    }
}
