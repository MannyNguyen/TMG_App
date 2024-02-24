package adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

public class EditSupport extends EditText
{
    public EditSupport(Context context)
    {
        super(context);
    }

    public EditSupport(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EditSupport(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return conn;
    }
}
